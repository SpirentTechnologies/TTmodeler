/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.testingtech.ttworkbench.utp.ea.importer;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.testingtech.ttworkbench.core.CorePlugin;
import com.testingtech.ttworkbench.utp.ea.Transformer;

public class EAImportWizard extends Wizard implements IImportWizard {

  EAImportWizardPage mainPage;

  public EAImportWizard() {

    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
  public boolean performFinish() {

    final IFile destination = mainPage.createNewFile();
    if (destination == null)
      return false;

    final String source = mainPage.editor.getStringValue();

    IRunnableWithProgress runnable = new IRunnableWithProgress() {

      public void run(IProgressMonitor monitor)
          throws InvocationTargetException, InterruptedException {

        try {
          Transformer transformer = new Transformer();
          boolean isTransformed = transformer.transform(monitor, source,
              destination);

          if (!isTransformed) {
            destination.delete(true, monitor);
          }
          destination.refreshLocal(IResource.DEPTH_ZERO, monitor);
        }
        catch (Exception e) {
          CorePlugin.out().ERROR.printExceptionMsg(e);
        }
        finally {
          monitor.done();
        }

      }

    };

    try {
      getContainer().run(false, false, runnable);
    }
    catch (InvocationTargetException e) {
      CorePlugin.out().ERROR.printExceptionMsg(e);
      return false;
    }
    catch (InterruptedException e) {
      CorePlugin.out().ERROR.printExceptionMsg(e);
      return false;
    }

    // Transformer transformer = new Transformer();
    //		
    // /* Creates a new ReleaseManager */
    // ReleaseManager rm = new ReleaseManager();
    //		
    // try {
    //			
    // /* Creates an EA repository object */
    // EARepository eaRepository = new EARepository(rm);
    //			
    // /* Opens an *.EAP file and associates this file with the repository
    // object */
    // eaRepository.openFile(mainPage.editor.getStringValue());
    //			
    // /* I keep only the name of the file without path and without the *.EAP
    // extension */
    // // int lastIndex = args[2].lastIndexOf("\\");
    // // String fileName = args[2].substring(lastIndex + 1,
    // (args[2].length()-4));
    //			
    // //File inputFile = new File(mainPage.getFileName());
    //			
    // /* Creates the URI where the resource is saved */
    // URI uri = URI.createURI(outputFile.getLocationURI().toString());
    //			
    // /* First iteration through the tree */
    // transformer.dumpModel(eaRepository, uri);
    //			
    // Activator u2tpcore = Activator.getDefault();
    // Bundle bundle = null;//u2tpcore.getBundle();
    //			
    //			
    // bundle = Platform.getBundle("com.testingtech.ttworkbench.u2tp");
    // System.out.println(bundle.getSymbolicName());
    //			
    //			
    // Path path = new Path("/model/u2tp.profile.uml");
    // final URL srcFileURL = FileLocator.find(
    // bundle, path, //$NON-NLS-1$
    // null);
    //		    
    // if (srcFileURL == null) {
    // return false;
    // }
    //			
    // /* Load the profile */
    // try {
    // transformer.u2tp =
    // Apply_U2TP.loadProfile(URI.createURI(srcFileURL.toURI().toString()));
    // } catch (URISyntaxException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //					
    // /* Second iteration through the tree */
    // transformer.completeModel(eaRepository, uri);
    //					
    // /* Closes the file */
    // eaRepository.closeFile();
    //			
    // eaRepository.exit();
    //			
    // } catch (JComException e) { e.printStackTrace(); }
    //		
    // finally { rm.release(); }

    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
   *      org.eclipse.jface.viewers.IStructuredSelection)
   */
  public void init(IWorkbench workbench, IStructuredSelection selection) {

    setWindowTitle("Enterprise Architect Model Import"); // NON-NLS-1
    setNeedsProgressMonitor(true);
    mainPage = new EAImportWizardPage("Enterprise Architect Model", selection); // NON-NLS-1
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.jface.wizard.IWizard#addPages()
   */
  public void addPages() {

    super.addPages();
    addPage(mainPage);
  }
}
