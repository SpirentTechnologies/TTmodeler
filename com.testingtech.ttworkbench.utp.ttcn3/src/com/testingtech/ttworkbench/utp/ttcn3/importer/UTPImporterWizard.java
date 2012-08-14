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
package com.testingtech.ttworkbench.utp.ttcn3.importer;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import com.testingtech.ttworkbench.core.CorePlugin;
import com.testingtech.ttworkbench.core.ui.SWTUtil;
import com.testingtech.ttworkbench.metamodel.core.exception.ModelProcessingException;
import com.testingtech.ttworkbench.metamodel.core.foreignmodel.ForeignModelTargetDescriptor;
import com.testingtech.ttworkbench.utp.ttcn3.legacy.UTP2TTCN3TransformationManager;

public class UTPImporterWizard extends Wizard implements IWorkbenchWizard {

  protected IStructuredSelection selection;
  protected IWorkbench workbench;
  
  private UTPImporterWizardPage page = null;
  
  private ForeignModelTargetDescriptor target;
  
  UTP2TTCN3TransformationManager transformer = null;
  
  public static final String UTP_IMPORTER_WIZARD_LABLE = "UML Import (Eclipse)";
  
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    this.workbench = workbench;
    this.selection = selection;
  }

  @Override
  public boolean performFinish() {
    if(page != null && page.getURITextContent() != null){
      String path = page.getURITextContent();
      if(path != null){
        transformer = new UTP2TTCN3TransformationManager(path, target);
        try{
          transformer.transform();
          transformer.updateTarget();
        }catch(Exception ex){    
          transformer.rollback();
          SWTUtil.createErrorDialog(UTP_IMPORTER_WIZARD_LABLE, ex.getMessage(), ex, CorePlugin.getDefault().getBundle(), this
              .getClass().getName());
          
          
          MessageDialog.openInformation(this.getShell(), UTP_IMPORTER_WIZARD_LABLE, 
              ex.getMessage());
        }finally{
          transformer.dispose();
          transformer = null;
        }
      }
    }
    return true;
  }

  
  @Override
  public boolean performCancel() {
    if(transformer != null){
      transformer.rollback();
      transformer.dispose();
      transformer = null;
    }
    return super.performCancel();
  }

  @Override
  public void addPages() {
    page = new UTPImporterWizardPage();
    addPage(page);
  }

  public void setTarget(ForeignModelTargetDescriptor des){
    target = des;
  }
  
}
