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

import static com.testingtech.ttcn.metamodel.Reducer.makeName;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMIResource;

import com.testingtech.muttcn.kernel.Expression;
import com.testingtech.muttcn.kernel.Identifier;
import com.testingtech.muttcn.statements.ImportDeclaration;
import com.testingtech.muttcn.statements.ModuleDeclaration;
import com.testingtech.muttcn.values.ModuleValue;
import com.testingtech.ttcn.common.TTCN3EditionConstants;
import com.testingtech.ttworkbench.core.ide.CLSource;
import com.testingtech.ttworkbench.core.ide.EMFModuleStateKind;
import com.testingtech.ttworkbench.core.ide.EMFModuleView;
import com.testingtech.ttworkbench.core.ide.SourceModuleView;
import com.testingtech.ttworkbench.metamodel.core.builder.ViewKind;
import com.testingtech.ttworkbench.metamodel.core.exception.ModelProcessingException;
import com.testingtech.ttworkbench.metamodel.core.exception.ModuleAlreadyExistException;
import com.testingtech.ttworkbench.metamodel.core.exception.ModuleFileCreationException;
import com.testingtech.ttworkbench.metamodel.core.foreignmodel.ForeignModelTargetDescriptor;
import com.testingtech.ttworkbench.metamodel.core.repository.DevelopmentRepositoryView;
import com.testingtech.ttworkbench.metamodel.core.repository.IRepositoryView;
import com.testingtech.ttworkbench.metamodel.core.repository.ModuleDescriptor;
import com.testingtech.ttworkbench.metamodel.core.repository.TransientStorage;
import com.testingtech.ttworkbench.metamodel.core.repository.ViewDescriptor;
import com.testingtech.ttworkbench.metamodel.core.repository.ViewStorage;
import com.testingtech.ttworkbench.metamodel.core.util.ModelOperationUtil;
import com.testingtech.ttworkbench.metamodel.muttcn.emfview.MuTTCNEMFModuleView;
import com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator;
import com.testingtech.ttworkbench.metamodel.muttcn.util.DeclarationsUtil;
import com.testingtech.ttworkbench.metamodel.muttcn.util.MuTTCNModelOperationUtil;
import com.testingtech.ttworkbench.metamodel.muttcn.util.MuTTCNModelerUtil;
import com.testingtech.ttworkbench.utp.core.resource.UTPTextFileSource;

public abstract class GeneralModuleCreator extends TransformationUtil {

  protected UTP2TTCN3TransformationManager manager;

  protected ForeignModelTargetDescriptor target;

  protected EMFModuleView emfModuleView = null;

  protected EList modelObjects = new BasicEList();

  protected CLSource clSource = null;

  protected List<String> importedModuleNames = new ArrayList<String>();

  protected IRepositoryView repositoryView = null;

  private boolean doOverwrite;

  private XMIResource utpResource = null;

  protected NameMaker nameMaker;


  public GeneralModuleCreator(UTP2TTCN3TransformationManager mgr, boolean doOverwrite, XMIResource utpResource) {
    super(mgr);
    this.manager = mgr;
    this.doOverwrite = doOverwrite;
    this.target = mgr.getTarget();
    this.utpResource = utpResource;
    repositoryView = DevelopmentRepositoryView.getProjectRepositoryView(target.getProject().getName());
  }

  public void dispose() {

    emfModuleView = null;
    repositoryView = null;
    clSource = null;
    modelObjects.clear();
    importedModuleNames.clear();
    utpResource = null;
    target = null;
    manager = null;
    super.dispose();
  }

  /**
   * Add import declaration (kind: all) for given moduleName.
   *
   * @param moduleName
   */
  protected void addImport(String moduleName) {
    addImport(moduleName, "all", "all");
  }

  /**
   * Add import declaration for given moduleName, kind and identifier.
   * @param moduleName
   * @param kind
   * @param identifier
   */
  protected void addImport(String moduleName, String kind, String identifier) {
    if(moduleName.equals(getModuleName()))
      return;
    ModuleDeclaration moduleDecl = getModuleDeclaration();
    if(moduleDecl != null){
      ModuleValue module = moduleDecl.getTheModuleValue();
      if(module == null){
        module = DeclarationGenerator.generateModuleValue(moduleDecl);
      }
      // check if already imported
      if (importedModuleNames.contains(moduleName)) {
        EList list = module.getTheDeclarations();
        for (int i = 0; i < list.size(); i++) {
          Object obj = list.get(i);
          if (obj instanceof ImportDeclaration) {
            ImportDeclaration imp = (ImportDeclaration)obj;
            if(imp.getTheSourceModule().getTheName().getTheName().equals(moduleName)){
              if(imp.getTheName().getTheName().equals(kind)){
                EList imports = imp.getTheImports();
                for (int j = 0; j < imports.size(); j++) {
                  Identifier id = (Identifier) imports.get(j);
                  if(id.getTheName().getTheName().equals(identifier))
                    return;
                }
              }
            }
          }
        }
      }
      ImportDeclaration importDecl = DeclarationGenerator.generateImportDeclaration(moduleName);
      importDecl.setTheName(makeName(kind));
      importDecl.setRecursive(new Boolean(false));
      importDecl.setTheLanguage(TTCN3EditionConstants.TTCN3_DEFAULT);
      Expression id = DeclarationGenerator.generateIdentifier(identifier);
      importDecl.getTheImports().add(id);
      module.getTheDeclarations().add(importDecl);
      importedModuleNames.add(moduleName);   

    }
  }

  /**
   * Clean ttcn3 model mapped for this package.
   */
  protected void cleanModel() {
    if (emfModuleView != null && emfModuleView.getObjects().size() > 0) {
      emfModuleView.clean();
    }
  }

  protected CLSource createCLSource(String fileName) throws ModelProcessingException {
    CLSource result = null;
    int begin = 0;
    int sep = fileName.lastIndexOf(File.separatorChar);
    if (sep > -1)
      begin = sep + 1;
    String moduleName = fileName.substring(begin, fileName.indexOf(TransformationUtil.CL_SOURCE_FILE_EXTENSION));
    File f = new File(fileName);
    if (!f.exists()) {
      try {
        f.createNewFile();
      } catch (IOException ex) {
        throw new ModuleFileCreationException(fileName);
      }
    }
    result = ModelOperationUtil.createSource(target.getProject().getName(), moduleName, fileName);
    return result;
  }

  abstract public String getModuleName();

  abstract public ModuleDeclaration getModuleDeclaration();

  public UTP2TTCN3TransformationManager getManager(){
    return manager;
  }

  /**
   * Get name maker for this module.
   * @return
   */
  public NameMaker getNameMaker(){
    return nameMaker;
  }


  /**
   * Check if cl source of emfView matches src.
   *
   * @param emfView
   * @param src
   * @return
   */
  protected boolean moduleExists(String moduleName) {
    boolean result = false;
    IPath cont = target.getContainer();
    String nm = moduleName;
    if (cont != null && nm != null) {
      String fullName = ModelOperationUtil.getFullFileName(cont, nm + TransformationUtil.CL_SOURCE_FILE_EXTENSION);
      File file = new File(fullName);
      if (file.exists()) {
        result = true;
      }
    }
    return result;
  }

  protected void update() throws ModelProcessingException{
    updateModel();
  }

  public void updateTarget()throws ModelProcessingException, CoreException{
    updateCLSource();
    updateUTPSource();
  }

  /**
   * generate TTCN-3 file
   * @throws CoreException 
   */
  protected void updateCLSource() throws CoreException {
    if (emfModuleView instanceof MuTTCNEMFModuleView) {
      // this check ensures that only modules with content
      // (modules that contain declarations) will be created
      // as ttcn-3 source files.
      MuTTCNEMFModuleView view = (MuTTCNEMFModuleView) emfModuleView;
      ModuleDeclaration moduleDeclaration = view.getModuleDeclaration();
      if (DeclarationsUtil.hasDeclarations(moduleDeclaration)) {
        MuTTCNModelOperationUtil.updateCLSource(view, repositoryView);
      }
      // because an empty file is created before filling the module, we have to delete this file in case
      // the module remains empty.
      else {
        IFile moduleFile = ModelOperationUtil.getModuleSourceFile(view);
        moduleFile.delete(true, null);
      }
    }
  }
 
  protected void updateUTPSource() {

    if (utpResource == null)
      return;
    if (emfModuleView == null)
      return;
    synchronized (emfModuleView) {
      EList dependencies = emfModuleView.getDependencies();
      UTPTextFileSource utpSource = null;
      boolean exists = false;
      for (int i = 0; i < dependencies.size(); i++) {
        EObject view = (EObject) dependencies.get(i);
        if (view instanceof UTPTextFileSource) {
          utpSource = (UTPTextFileSource) view;
          exists = true;
          break;
        }
      }
      // overwrite utpSource
      utpSource = new UTPTextFileSource(utpResource);
      if (!exists) {
        emfModuleView.getDependencies().add(utpSource);
        utpSource.getDependants().add(emfModuleView);
      }
    }
  }

  /**
   * Update target file with transformed model.
   */
  protected EMFModuleView updateModel() {

    synchronized (emfModuleView) {
      cleanModel();
      emfModuleView.setObjects(modelObjects);
      emfModuleView.setState(EMFModuleStateKind.TYPE_CHECKED_LITERAL);
      emfModuleView.update();
    }
    return emfModuleView;
  }

  /**
   * Check EMFModuleView for module mapped to this package.
   */
  protected void checkEMFModuleView(String moduleName)
      throws ModelProcessingException {

    if (target != null) {
      String fileName = ModelOperationUtil.getFullFileName(target
          .getContainer(), moduleName + TransformationUtil.CL_SOURCE_FILE_EXTENSION);
      if (!doOverwrite && moduleExists(moduleName))
        throw new ModuleAlreadyExistException(fileName);
      emfModuleView = manager.getEmfModuleView(moduleName);
      if(emfModuleView == null)
        makeEMFModuleView(moduleName);
    }
  }

  abstract public void create() throws ModelProcessingException;

  /**
   * Get EMFModuleView for module mapped to this package.
   */
  protected void makeEMFModuleView(String moduleName)
      throws ModelProcessingException {

    String fileName = ModelOperationUtil.getFullFileName(target.getContainer(),
        moduleName + TransformationUtil.CL_SOURCE_FILE_EXTENSION);
    if(clSource == null)
      clSource = createCLSource(fileName);
    if (clSource == null) {
      throw new NotATTCN3FolderException(fileName);
    }
    if (target != null && emfModuleView == null) {
      ViewDescriptor viewDescriptor = new ModuleDescriptor(moduleName);
      viewDescriptor.setViewKind(ViewKind.EMF);
      viewDescriptor.setExtension(ViewDescriptor.PROJECT, target.getProject().getName());
      viewDescriptor.setExtension(ViewDescriptor.FILE, manager.getModuleDescriptorFileTag());

      SourceModuleView src = MuTTCNModelOperationUtil.getSourceModuleView(viewDescriptor);
      emfModuleView = MuTTCNModelerUtil
          .createEMFModuleView(repositoryView,
              moduleName, src);
      ViewStorage<EMFModuleView> viewStorage = new TransientStorage<EMFModuleView>(emfModuleView);
      manager.getModelRepository().addView(repositoryView, viewDescriptor, viewStorage);
    }
  }
}
