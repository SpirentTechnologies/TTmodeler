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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * 
 * we have this declaration in plugin.xml. uncomment to use this class.
 * 
      <!--
      <wizard
            category="com.testingtech.ttworkbench.utp.ttcn3.importer"
            class="com.testingtech.ttworkbench.utp.ttcn3.importer.NewTestModelWizard"
            id="com.testingtech.ttworkbench.utp.ttcn3.importer.newTestModelWizard"
            name="%NewTestModel.label">
        <description>
            %NewTestModel.description
        </description>
      </wizard>
      -->
 *
 */
public class NewTestModelWizard extends Wizard implements INewWizard {

  protected IStructuredSelection selection;
  protected IWorkbench workbench;
  
  protected ModelImporterSelectionPage selectionPage;
  
  private NewModuleCreationPage page = null;
  
  @Override
  public boolean performFinish() {

    // TODO Auto-generated method stub
    return false;
  }

  public void init(IWorkbench workbench, IStructuredSelection selection) {
    this.workbench = workbench;
    this.selection = selection;
  }

  @Override
  public void addPages() {
    page = new NewModuleCreationPage(selection);
    addPage(page);
    
    addSelectionPage(page);
  }

  protected void addSelectionPage(NewModuleCreationPage previousPage)
  {
    selectionPage = 
      new ModelImporterSelectionPage(
          ModelImporterSelectionPage.MODEL_IMPORTER_SELECTION_PAGE_ID,
          workbench, selection, previousPage);
    selectionPage.setTitle(ModelImporterSelectionPage.MODEL_IMPORTER_SELECTION_PAGE_LABLE);
    addPage(selectionPage);
  }
  
  
}
