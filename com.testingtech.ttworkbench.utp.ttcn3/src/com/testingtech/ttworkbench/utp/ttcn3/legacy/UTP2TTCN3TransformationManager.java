/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2006-2012.  All Rights Reserved.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *     Testing Technologies - initial API and implementation
 *
 *  All copies of this program, whether in whole or in part, and whether
 *  modified or not, must display this and all other embedded copyright
 *  and ownership notices in full.
 *
 *  See the file COPYRIGHT for details of redistribution and use.
 *
 *  You should have received a copy of the COPYRIGHT file along with
 *  this file; if not, write to the Testing Technologies,
 *  Michaelkirchstr. 17/18, 10179 Berlin, Germany.
 *
 *  TESTING TECHNOLOGIES DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS
 *  SOFTWARE. IN NO EVENT SHALL TESTING TECHNOLOGIES BE LIABLE FOR ANY
 *  SPECIAL, DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 *  WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN
 *  AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,
 *  ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF
 *  THIS SOFTWARE.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
 *  EITHER EXPRESSED OR IMPLIED, INCLUDING ANY KIND OF IMPLIED OR
 *  EXPRESSED WARRANTY OF NON-INFRINGEMENT OR THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 * -----------------------------------------------------------------------------
 */
package com.testingtech.ttworkbench.utp.ttcn3.legacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jface.dialogs.IDialogSettings;

import com.testingtech.ttworkbench.core.CorePlugin;
import com.testingtech.ttworkbench.core.ide.EMFModuleView;
import com.testingtech.ttworkbench.metamodel.core.MetamodelCorePlugin;
import com.testingtech.ttworkbench.metamodel.core.builder.ViewKind;
import com.testingtech.ttworkbench.metamodel.core.exception.ModelProcessingException;
import com.testingtech.ttworkbench.metamodel.core.exception.ModuleAlreadyExistException;
import com.testingtech.ttworkbench.metamodel.core.foreignmodel.ForeignModelTargetDescriptor;
import com.testingtech.ttworkbench.metamodel.core.repository.DevelopmentRepositoryView;
import com.testingtech.ttworkbench.metamodel.core.repository.FileSetRepositoryView;
import com.testingtech.ttworkbench.metamodel.core.repository.IViewRepository;
import com.testingtech.ttworkbench.metamodel.core.repository.ModuleDescriptor;
import com.testingtech.ttworkbench.metamodel.core.repository.ViewDescriptor;
import com.testingtech.ttworkbench.metamodel.muttcn.util.ModelElementUtils;
import com.testingtech.ttworkbench.utp.core.TTmodelerConsts;
import com.testingtech.ttworkbench.utp.core.UTPConsts;
import com.testingtech.ttworkbench.utp.core.exception.UTPApplicationException;
import com.testingtech.ttworkbench.utp.core.model.UTPModelLoader;
import com.testingtech.ttworkbench.utp.ttcn3.importer.NewModuleCreationPage;
import com.testingtech.ttworkbench.utp.ttcn3.importer.UTPImporterWizardPage;
import com.testingtech.ttworkbench.utp.ttcn3.importer.UmlTtcn3ImporterPlugin;

public class UTP2TTCN3TransformationManager {

  private List<org.eclipse.uml2.uml.Package> pkgs = new ArrayList<org.eclipse.uml2.uml.Package>();

  private ForeignModelTargetDescriptor target;

  private XMIResource utpResource = null;

  private Map<String, GeneralModuleCreator> transformers = new HashMap<String, GeneralModuleCreator>();

  private boolean rollback = false;

  private RootModuleCreator rootModuleCreator = null;

  private static int counter = 0;

  public UTP2TTCN3TransformationManager(String modelFilePath,
      ForeignModelTargetDescriptor target) {

    this.target = target;
    getUTPPackages(modelFilePath);
    counter++;
  }

  /**
   * Add module with moduleName to root module as imported.
   *
   * @param moduleName
   */
  public void addImport2RootModule(String moduleName) {
    if (rootModuleCreator != null)
      rootModuleCreator.addImport(moduleName);
  }

  public boolean hasRootModule() {
    return  ! (rootModuleCreator == null);
  }
  
  /**
   * Transform packages.
   *
   * @throws ModelProcessingException
   */
  public void transform() throws ModelProcessingException {

    if (pkgs != null) {
      // root
      rootModuleCreator = makeRootModule();
      // packages
      List<String> conflictModules = new ArrayList<String>();
      List<String> conflictPackages = new ArrayList<String>();
      IDialogSettings dialogSettings = UmlTtcn3ImporterPlugin.getDefault()
          .getDialogSettings();
      boolean doOverwrite = dialogSettings
          .getBoolean(UTPImporterWizardPage.OVERWRITE_MODULE_LABLE);
      for (org.eclipse.uml2.uml.Package pkg : pkgs) {
        if (packageTransformed(pkg)) {
          conflictModules.add(pkg.getName());
          break;
        }
        NamespaceModuleCreator conv = new NamespaceModuleCreator(pkg, this,
            doOverwrite, utpResource);
        try {
          conv.create();
          transformers.put(pkg.getName(), conv);
        }
        catch (ModuleAlreadyExistException ex) {
          conflictModules.addAll(ex.getModulePaths());
        }
        catch (UTPApplicationException ex) {
          conflictPackages.addAll(ex.getPackages());
        }
      }
      if (conflictModules.size() > 0)
        throw new ModuleAlreadyExistException(conflictModules);
      if (conflictPackages.size() > 0)
        throw new UTPApplicationException(conflictPackages);
      if (rootModuleCreator != null)
        rootModuleCreator.update();
    }
    try {
      target.getProject().refreshLocal(IResource.DEPTH_INFINITE,
          new NullProgressMonitor());
    }
    catch (CoreException e) {
      // ignore refresh errors
    }
  }

  public void dispose() {
    cleanRepository();
    cleanNames();
    for (GeneralModuleCreator conv : transformers.values()) {
      conv.dispose();
    }
    transformers.clear();
    pkgs.clear();
    utpResource = null;
    target = null;
  }

  /**
   * Clean global registered names.
   */
  private void cleanNames() {
    NameMaker.globalElements.clear();
    NameMaker.reservedWords.clear();
  }

  /**
   * Get all transformers.
   *
   * @return
   */
  public Map<String, GeneralModuleCreator> getTransformers() {
    return transformers;
  }

  /**
   * Get module with predefined elements.
   *
   * @return
   */
  public EMFModuleView getPredefinedModule() {

    PredefinedModuleCreator creator = (PredefinedModuleCreator) getTransformers()
        .get(UTPConsts.PREDEFINED_MODULE_NAME);
    if (creator == null) {
      try {
        creator = makePredefinedModule();
      }
      catch (ModelProcessingException ex) {
        reportError(ex);
      }
    }
    return getEmfModuleView(UTPConsts.PREDEFINED_MODULE_NAME);
  }

  public EMFModuleView getEmfModuleView(String moduleName) {
    String projectName = target.getProject().getName();
    ModuleDescriptor moduleDescriptor = makeModuleDescriptor(moduleName);
    EMFModuleView view = ModelElementUtils.getEMFModuleView(moduleDescriptor,
        DevelopmentRepositoryView.getProjectRepositoryView(projectName));
    return view;
  }

  /**
   * Make module descriptor for given module name.
   *
   * @param moduleName
   * @return
   */
  private ModuleDescriptor makeModuleDescriptor(String moduleName) {
    String projectName = target.getProject().getName();
    ModuleDescriptor moduleDescriptor = new ModuleDescriptor(moduleName);
    moduleDescriptor.setViewKind(ViewKind.EMF);
    moduleDescriptor.setExtension(ViewDescriptor.PROJECT, projectName);
    moduleDescriptor.setExtension(ViewDescriptor.FILE,
        getModuleDescriptorFileTag());
    return moduleDescriptor;
  }

  public IViewRepository getModelRepository() {
    return MetamodelCorePlugin.getDefault().getViewRepository();
  }

  public String getModuleDescriptorFileTag() {
    Integer ct = new Integer(counter);
    return TTmodelerConsts.GLOBAL_NAME_SEPARATOR
      + UTPConsts.LANGUAGE_TAG
      + TTmodelerConsts.GLOBAL_NAME_SEPARATOR
      + ct.toString();
  }

  /**
   * Get name of root module.
   *
   * @return
   */
  public String getRootModuleName() {
    if (rootModuleCreator != null)
      return rootModuleCreator.getModuleName();
    return null;
  }

  public ForeignModelTargetDescriptor getTarget() {
    return target;
  }

  /**
   * Check if utp is applied to given package.
   *
   * @param pkg
   * @return
   */
  public static boolean isUTPApplied(org.eclipse.uml2.uml.Package pkg) {
    String profileName = UTPConsts.PROFILE_NAME;
    return UTPModelLoader.isProfileApplied(pkg, profileName);
  }

  /**
   * Make root module.
   *
   * @return
   * @throws ModelProcessingException
   */
  public RootModuleCreator makeRootModule() throws ModelProcessingException {
    if (target.getRootFileName() == null)
      // TODO: add error handling here. don't know the exact error yet.
      return null;
    // check if root module shall be overwritten
    boolean doOverwrite = CorePlugin.getDefault().getDialogSettings()
        .getBoolean(NewModuleCreationPage.OVERWRITE_ROOT_LABLE);
    RootModuleCreator root = new RootModuleCreator(this, doOverwrite,
        utpResource);
    root.create();
    transformers.put(root.getModuleName(), root);
    return root;
  }

  /**
   * Create predefined module if not exists.
   *
   * @return
   */
  public PredefinedModuleCreator makePredefinedModule() throws ModelProcessingException {
    PredefinedModuleCreator creator = new PredefinedModuleCreator(this, utpResource);
    creator.create();
    transformers.put(creator.getModuleName(), creator);
    return creator;
  }

  public void reportError(ModelProcessingException ex) {
    // FIXME report errors
    //SWTUtil.createInformationDialog("TTmodeller processing error", message);
    // CorePlugin.getDefault().eclipseLog("TTmodeler processing error: " + ex.msg, ex);
  }

  public void rollback() {
    rollback = true;
  }

  public void updateTarget() throws ModelProcessingException, CoreException {
    if (rollback)
      return;
    for (GeneralModuleCreator conv : transformers.values()) {
      //TODO: add error handling here - perhaps this is a good spot for duplivate name module checking
      conv.updateTarget();
    }
  }

  private void cleanRepository() {
    if (target != null) {
      FileSetRepositoryView repView = new FileSetRepositoryView(target.getProject().getName());
      repView.addFile(getModuleDescriptorFileTag());
      getModelRepository().clear(repView);
    }
  }

  private void getUTPPackages(String modelFilePath) {
    URI uri = URI.createURI(modelFilePath);
    ResourceSet set = new ResourceSetImpl();
    Resource resource = set.getResource(uri, true);
    pkgs = UTPModelLoader.loadPackages(uri);
    if (resource instanceof XMIResource)
      utpResource = (XMIResource) resource;
  }

  /**
   * Check if pkg already transformed.
   *
   * @param pkg
   * @return
   */
  private boolean packageTransformed(org.eclipse.uml2.uml.Package pkg) {
    if (transformers.get(pkg.getName()) != null) {
      return true;
    }
    return false;
  }

}
