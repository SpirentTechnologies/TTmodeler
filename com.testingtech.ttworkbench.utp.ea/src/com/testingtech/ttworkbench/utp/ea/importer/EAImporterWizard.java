/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2007-2012.  All Rights Reserved.
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
package com.testingtech.ttworkbench.utp.ea.importer;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import com.testingtech.ttworkbench.metamodel.core.foreignmodel.ForeignModelTargetDescriptor;

public class EAImporterWizard extends Wizard implements IWorkbenchWizard {

  private ForeignModelTargetDescriptor target;

  private EAImporterWizardPage page;

  public EAImporterWizard(ForeignModelTargetDescriptor des) {

    target = des;
  }

  @Override
  public void addPages() {

    super.addPages();
    addPage(page);
  }

  @Override
  public boolean performFinish() {

    // IFile outputFile = page.createNewFile();
    // if (outputFile == null)
    // return false;
    //        
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
    //        
    // return true;

    return false;
  }

  public void init(IWorkbench workbench, IStructuredSelection selection) {

    setWindowTitle("Enterprise Architect Model Import"); // NON-NLS-1
    setNeedsProgressMonitor(true);
    page = new EAImporterWizardPage("Enterprise Architect Model", target
        .getContainer());
  }

  public void setTarget(ForeignModelTargetDescriptor des) {

    target = des;
  }
}
