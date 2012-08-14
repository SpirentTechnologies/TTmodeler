/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2003-2012.  All Rights Reserved.
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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import com.testingtech.ttworkbench.metamodel.core.extensionpoint.IForeignModelImporterDescriptor;
import com.testingtech.ttworkbench.metamodel.core.foreignmodel.ForeignModelImporterManager;


public class ModelImporterSelectionPage extends WizardSelectionPage
implements ISelectionChangedListener{

  public static final String MODEL_IMPORTER_SELECTION_PAGE_ID = "ModelImporterSelectionPageId"; //$NON-NLS-1$
  
  public static final String MODEL_IMPORTER_SELECTION_PAGE_LABLE = Messages.Lbl__Select_model_importer;
  
  public static final String MODEL_IMPORTER_SELECTION_PAGE_DESC = ""; //$NON-NLS-1$
  
  protected TableViewer descriptorTableViewer;
  
  protected boolean firstTime = true;
  
  protected Set initializedWizards = new HashSet();

  protected IForeignModelImporterDescriptor descriptor;
  
  private ForeignModelImporterManager modelImporterManager = ForeignModelImporterManager.INSTANCE;
  private NewModuleCreationPage previousPage;
  
  protected IStructuredSelection selection;
  protected IWorkbench workbench;  
  
  
  public ModelImporterSelectionPage(String pageId, IWorkbench workbench, 
      IStructuredSelection selection, NewModuleCreationPage previousPage){
    super(pageId);
    this.workbench = workbench;
    this.selection = selection;
    this.previousPage = previousPage;
  }
  
  public void createControl(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    {
      GridLayout layout = new GridLayout();
      layout.numColumns = 1;
      layout.verticalSpacing = 12;
      composite.setLayout(layout);

      GridData data = new GridData();
      data.verticalAlignment = GridData.FILL;
      data.grabExcessVerticalSpace = true;
      data.horizontalAlignment = GridData.FILL;
      composite.setLayoutData(data);
    }

    Label label = new Label(composite, SWT.NONE);
    label.setText(MODEL_IMPORTER_SELECTION_PAGE_LABLE);
    {
      GridData data = new GridData();
      data.verticalAlignment = SWT.FILL;
      data.horizontalAlignment = SWT.FILL;
      label.setLayoutData(data);
    }

    Table descriptorTable = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
    {
      GridData data = new GridData();
      data.widthHint = Display.getCurrent().getBounds().width / 5;
      data.heightHint = Display.getCurrent().getBounds().height / 3;
      data.verticalAlignment = SWT.FILL;
      data.horizontalAlignment = SWT.FILL;
      data.grabExcessHorizontalSpace = true;
      data.grabExcessVerticalSpace = true;
      descriptorTable.setLayoutData(data);
    }

    descriptorTableViewer = new TableViewer(descriptorTable);
    descriptorTableViewer.setContentProvider(new ArrayContentProvider());
    descriptorTableViewer.setLabelProvider(modelImporterManager.getLabelProvider());
    descriptorTableViewer.setSorter(new ViewerSorter());

    descriptorTableViewer.addDoubleClickListener(new IDoubleClickListener()
      {
        public void doubleClick(DoubleClickEvent event)
        {
          if (canFlipToNextPage())
          {
            getContainer().showPage(getNextPage());
          }
        }
      });

    descriptorTableViewer.setInput(getTableInput());
    descriptorTableViewer.addSelectionChangedListener(this);
    if (getModelImporterDescriptor() != null)
    {
      descriptorTableViewer.setSelection(new StructuredSelection(getModelImporterDescriptor()), true);
    }
    setControl(composite);
  }

  public IWizardPage getNextPage()
  {
    IWizard wz = (IWizard)getSelectedNode().getWizard();
    if (initializedWizards.add(wz))
    {
      if (wz instanceof Wizard)
      {
        Wizard wizard = (Wizard)wz;
        if (wizard.getWindowTitle() == null)
        {
          wizard.setWindowTitle(getWizard().getWindowTitle());
        }
      }
      
      if (wz instanceof IWorkbenchWizard)
      {
        ((IWorkbenchWizard)wz).init(workbench, selection);
      }
    }

    IWizardPage wizardPage = super.getNextPage();
    
    IWizardNode wizardNode = getSelectedNode();
    if (wizardNode instanceof ForeignModelImporterManager.ModelImporterWizardNode)
    {
      ForeignModelImporterManager.ModelImporterWizardNode fwn =(ForeignModelImporterManager.ModelImporterWizardNode)wizardNode;
      fwn.setContentCreated(true);
      fwn.setTarget(previousPage.getNewModuleDescriptor());
    }
    return wizardPage;
  }
  
  public void selectionChanged(SelectionChangedEvent event) {
    ISelection selection = event.getSelection();
    if (!selection.isEmpty() && selection instanceof IStructuredSelection)
    {
      Object selectedObject = ((IStructuredSelection)selection).getFirstElement();
      if (selectedObject instanceof IForeignModelImporterDescriptor)
      {
        descriptor = (IForeignModelImporterDescriptor)selectedObject;
        
        setMessage(descriptor.getDescription(), IMessageProvider.NONE);
        setSelectedNode((IWizardNode)modelImporterManager.getNode(descriptor));
        return;
      }
    }

    setPageComplete(false);
  }

  protected Object[] getTableInput()
  {
    return modelImporterManager.getModelImporterDescriptors().toArray();
  }
  
  public IForeignModelImporterDescriptor getModelImporterDescriptor()
  {
    return descriptor;
  }

  public void setVisible(boolean visible)
  {
    super.setVisible(visible);
    if (visible && firstTime)
    {
      firstTime = false;
      Table table = descriptorTableViewer.getTable();
      table.setFocus();
    }
  }

  @Override
  public void dispose() {
    if (initializedWizards != null)
    {
      initializedWizards.clear();
      initializedWizards = null;
    }
    super.dispose();
  }
  
}
