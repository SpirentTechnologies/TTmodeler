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

import java.io.File;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.testingtech.ttworkbench.metamodel.core.extensionpoint.IForeignModelImporterDescriptor;
import com.testingtech.ttworkbench.metamodel.core.foreignmodel.ForeignModelImporterManager;

public class UTPImporterWizardPage extends WizardPage implements Listener {

  public static final String UTP_IMPORTER_PAGE_ID = Messages.getString("UTPImporterWizardPage.UTPImporterPageId"); //$NON-NLS-1$

  public static final String UTP_IMPORTER_PAGE_LABEL = Messages.getString("UTPImporterWizardPage.UMLImportEclipse"); //$NON-NLS-1$
  
  public static final String UTP_IMPORTER_PAGE_DESC = Messages.getString("UTPImporterWizardPage.ChooseOptions"); //$NON-NLS-1$

  public static final String OVERWRITE_MODULE_LABLE = Messages.getString("UTPImporterWizardPage.OverwriteModules"); //$NON-NLS-1$

  protected Text uriText;

  protected Button browseFileSystemButton;

  protected Button browseWorkspaceButton;

  protected boolean handlingEvent = true;

  private IDialogSettings curSettings = UmlTtcn3ImporterPlugin.getDefault()
  .getDialogSettings();
  
  private Button overwriteFlag = null;

  private boolean overwriteFlagValue = curSettings.getBoolean(OVERWRITE_MODULE_LABLE);

  
  public UTPImporterWizardPage() {

    super(UTP_IMPORTER_PAGE_ID);
    setTitle(UTP_IMPORTER_PAGE_LABEL);
    setDescription(UTP_IMPORTER_PAGE_DESC);
  }

  protected void addURIControl(Composite parent) {

  }

  protected void addControl(Composite parent) {

  }

  protected boolean browseFileSystem() {

    FileDialog fileDialog = new FileDialog(getShell(),
        SWT.OPEN | (supportMultipleURIs() ? SWT.MULTI : SWT.SINGLE));
    fileDialog.setFilterExtensions(getFilterExtensions());

    if (fileDialog.open() != null && fileDialog.getFileNames().length > 0) {
      String[] fileNames = fileDialog.getFileNames();
      StringBuffer text = new StringBuffer();
      for (int i = 0; i < fileNames.length; ++i) {
        String filePath = fileDialog.getFilterPath() + File.separator
                          + fileNames[i];
        text.append(URI.createFileURI(filePath).toString());
        text.append(" "); //$NON-NLS-1$
      }
      setURIText(text.toString());
      return true;
    }
    return false;
  }

  protected boolean browseWorkspace() {

    IFile[] files = WorkspaceResourceDialog.openFileSelection(getShell(), null,
        null, supportMultipleURIs(), null, null);
    if (files.length > 0) {
      StringBuffer text = new StringBuffer();
      for (int i = 0; i < files.length; ++i) {
        if (isValidWorkspaceResource(files[i])) {
          text.append(URI.createURI(URI.createPlatformResourceURI(
              files[i].getFullPath().toString()).toString(), true));
          text.append("  "); //$NON-NLS-1$
        }
      }
      setURIText(text.toString());
      return true;
    }
    return false;
  }

  public void createControl(Composite parent) {

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH
                                         | GridData.GRAB_VERTICAL));

    GridLayout layout = new GridLayout();
    layout.verticalSpacing = 8;
    composite.setLayout(layout);

    createURIControl(composite);
    addControl(composite);
    
    overwriteFlag = new Button(composite, SWT.CHECK);
    overwriteFlag.setText(OVERWRITE_MODULE_LABLE);
    overwriteFlag.setSelection(overwriteFlagValue);

    overwriteFlag.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent e) {

        updateValue();
      }

      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });
    setControl(composite);
  }

  protected void createURIControl(Composite parent) {

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL
                                         | GridData.GRAB_HORIZONTAL));
    {
      GridLayout layout = new GridLayout(2, false);
      layout.marginLeft = -5;
      layout.marginRight = -5;
      composite.setLayout(layout);
    }

    Label uriLabel = new Label(composite, SWT.LEFT);
    uriLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
    uriLabel.setText(getURITextLabel());

    Composite buttonComposite = new Composite(composite, SWT.NONE);
    buttonComposite
        .setLayoutData(new GridData(GridData.FILL_HORIZONTAL
                                    | GridData.HORIZONTAL_ALIGN_END));
    {
      RowLayout layout = new RowLayout();
      layout.justify = true;
      layout.pack = true;
      layout.spacing = 5;
      layout.marginRight = 0;
      buttonComposite.setLayout(layout);
    }

    browseFileSystemButton = new Button(buttonComposite, SWT.PUSH);
    browseFileSystemButton.setText(getBrowseFileSystemButtonLabel());
    browseFileSystemButton.addListener(SWT.Selection, this);

    browseWorkspaceButton = new Button(buttonComposite, SWT.PUSH);
    browseWorkspaceButton.setText(getBrowseWorkspaceButtonLabel());
    browseWorkspaceButton.addListener(SWT.Selection, this);

    Composite uriComposite = new Composite(parent, SWT.NONE);
    uriComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    {
      GridLayout layout = new GridLayout(2, false);
      layout.marginTop = -5;
      layout.marginLeft = -5;
      layout.marginRight = -5;
      uriComposite.setLayout(layout);
    }

    uriText = new Text(uriComposite, SWT.SINGLE | SWT.BORDER);
    uriText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    setURIText(getURITextInitialValue());
    if (uriText.getText().length() > 0) {
      uriText.selectAll();
    }
    uriText.addListener(SWT.Modify, this);

    addURIControl(uriComposite);
  }

  protected void doHandleEvent(Event event) {

    if (event.type == SWT.Modify && event.widget == uriText) {
      uriTextModified(uriText.getText().trim());
    }
    else if (event.type == SWT.Selection
             && event.widget == browseFileSystemButton) {
      browseFileSystem();
    }
    else if (event.type == SWT.Selection
             && event.widget == browseWorkspaceButton) {
      browseWorkspace();
    }

    getContainer().updateButtons();
  }

  protected String getURITextContent() {
    if(uriText != null)
      return uriText.getText();
    return null;
  }

  protected String getURITextLabel() {

    return ""; //$NON-NLS-1$
  }

  protected String getURITextInitialValue() {

    return ""; //$NON-NLS-1$
  }

  protected String getBrowseFileSystemButtonLabel() {

    return Messages.getString("UTPImporterWizardPage.8"); //$NON-NLS-1$
  }

  protected String getBrowseWorkspaceButtonLabel() {

    return Messages.getString("UTPImporterWizardPage.9"); //$NON-NLS-1$
  }

  protected String[] getFilterExtensions() {
    IForeignModelImporterDescriptor des = getModelImporterDescriptor();
    if(des == null)
      return new String[0];
    List<String> ls = des.getFileExtensions();
    String[] result = new String[ls.size()];
    for(int i=0; i<ls.size(); i++){
      String s = ls.get(i);
      if(s != null && !s.equals("")){ //$NON-NLS-1$
        result[i] = "*." + s; //$NON-NLS-1$
      }
    }
    return result;
  }

  protected IForeignModelImporterDescriptor getModelImporterDescriptor() {
    Set<IForeignModelImporterDescriptor> descriptors = ForeignModelImporterManager.INSTANCE.getModelImporterDescriptors();
    for(IForeignModelImporterDescriptor des : descriptors){
      if(des instanceof UTPImporterDescriptor)
        return des;
    }
    return null;
  }

  public void handleEvent(Event event) {

    if (isHandlingEvent()) {
      doHandleEvent(event);
    }
  }

  public boolean isHandlingEvent() {

    return handlingEvent;
  }

  protected boolean isValidWorkspaceResource(IResource resource) {

    if (resource.getType() == IResource.FILE) {
      String[] filterExtensions = getFilterExtensions();
      if (filterExtensions.length > 0) {
        for (int i = 0; i < filterExtensions.length; i++) {
          if (filterExtensions[i].endsWith(".*") //$NON-NLS-1$
              || filterExtensions[i]
                  .endsWith("." + resource.getFileExtension())) { //$NON-NLS-1$
            return true;
          }
        }
      }

    }
    return false;
  }

  public void setHandlingEvent(boolean handlingEvent) {

    this.handlingEvent = handlingEvent;
  }

  protected void setURIText(String uri) {

    uri = uri.trim();
    StringBuffer text = new StringBuffer(uriText.getText());
    if (!uri.equals(text)) {
      if (supportMultipleURIs()) {
        Point textSelection = uriText.getSelection();
        text.delete(textSelection.x, textSelection.y);
        uri = text.append(" ").append(uri).toString(); //$NON-NLS-1$
      }
      uriText.setText(uri.trim());
    }
  }

  protected boolean supportMultipleURIs() {

    return false;
  }

  protected void uriTextModified(String text) {

    setErrorMessage(null);
    setMessage(null);
  }

  protected void updateValue() {

    if (overwriteFlag != null)
      curSettings.put(OVERWRITE_MODULE_LABLE, overwriteFlag.getSelection());
  }
}
