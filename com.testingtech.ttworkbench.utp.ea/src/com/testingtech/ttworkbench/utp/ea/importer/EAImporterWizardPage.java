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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.fieldassist.FieldAssistColors;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.testingtech.ttworkbench.utp.ea.EAPlugin;

public class EAImporterWizardPage extends WizardPage implements Listener {

  // constants
  private static final int SIZING_TEXT_FIELD_WIDTH = 250;

  FileFieldEditor editor;

  String inputFileName = "";

  String outputFileName = "";

  IPath containerPath;

  Text outputFileField;

  public EAImporterWizardPage(final String pageName, final IPath containerPath) {

    super(pageName);
    this.containerPath = containerPath;
    setPageComplete(false);
  }

  public void createControl(final Composite parent) {

    initializeDialogUnits(parent);
    // top level group
    final Composite topLevel = new Composite(parent, SWT.NONE);
    topLevel.setLayout(new GridLayout());
    topLevel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
                                        | GridData.HORIZONTAL_ALIGN_FILL));
    topLevel.setFont(parent.getFont());

    createInputControls(topLevel);
    createOutputControls(topLevel);
    setErrorMessage(null);
    setMessage(null);
    setControl(topLevel);
  }

  private void createOutputControls(final Composite parent) {

    final Composite outputArea = new Composite(parent, SWT.NONE);
    final GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    layout.makeColumnsEqualWidth = false;
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    outputArea.setLayout(layout);
    outputArea.setFont(parent.getFont());
    GridData gd = new GridData(GridData.GRAB_HORIZONTAL
                               | GridData.FILL_HORIZONTAL);
    outputArea.setLayoutData(gd);

    final Label label = new Label(outputArea, SWT.NONE);
    label.setText("UML2 File Name:");

    outputFileField = new Text(outputArea, SWT.BORDER);
    gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
    gd.widthHint = SIZING_TEXT_FIELD_WIDTH;
    outputFileField.setLayoutData(gd);
    outputFileField.setFont(outputArea.getFont());
    outputFileField.setBackground(FieldAssistColors
        .getRequiredFieldBackgroundColor(outputFileField));
    outputFileField.addModifyListener(new ModifyListener() {

      public void modifyText(final ModifyEvent e) {

        final IPath path = containerPath.append(outputFileField.getText());
        final IFile newFile = createFileHandle(path);
        final IStatus status = validatePathLocation(newFile, containerPath);
        if (status.getSeverity() == IStatus.OK) {
          setMessage(null);
          setErrorMessage(null);
          outputFileName = newFile.getName();
        }
        else if (status.getSeverity() == IStatus.WARNING) {
          setMessage(status.getMessage());
          setErrorMessage(null);
        }
        else {
          setMessage(null);
          setErrorMessage(status.getMessage());
        }
      }
    });
  }

  protected void createInputControls(final Composite parent) {

    final Composite fileSelectionArea = new Composite(parent, SWT.NONE);
    final GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL
                                                    | GridData.FILL_HORIZONTAL);
    fileSelectionArea.setLayoutData(fileSelectionData);

    final GridLayout fileSelectionLayout = new GridLayout();
    fileSelectionLayout.numColumns = 3;
    fileSelectionLayout.makeColumnsEqualWidth = false;
    fileSelectionLayout.marginWidth = 0;
    fileSelectionLayout.marginHeight = 0;
    fileSelectionArea.setLayout(fileSelectionLayout);

    editor = new FileFieldEditor("EA_MODEL", "EA Model: ", fileSelectionArea); // NON-NLS-1
    // //NON-NLS-2
    editor.getTextControl(fileSelectionArea).addModifyListener(
        new ModifyListener() {

          public void modifyText(final ModifyEvent e) {

            final IPath path = new Path(EAImporterWizardPage.this.editor
                .getStringValue());
            inputFileName = path.lastSegment();
            outputFileField.setText(inputFileName);
          }
        });
    final String[] extensions = new String[]{
      "*.eap"
    }; // NON-NLS-1
    editor.setFileExtensions(extensions);
    fileSelectionArea.moveAbove(null);
  }

  /**
   * Creates a file resource handle for the file with the given workspace path.
   * This method does not create the file resource; this is the responsibility
   * of <code>createFile</code>.
   * 
   * @param filePath
   *          the path of the file resource to create a handle for
   * @return the new file resource handle
   * @see #createFile
   */
  protected IFile createFileHandle(final IPath filePath) {

    return ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
  }

  /**
   * Validates path locations.
   * 
   * @param linkHandle
   *          The target to check
   * @return IStatus indicating the validation result. IStatus.OK if the
   *         specified link target is valid given the linkHandle.
   */
  public IStatus validatePathLocation(final IResource linkHandle,
      final IPath path) {

    if (outputFileName == "") {
      return createStatus(IStatus.OK, "");
    }
    final IWorkspace workspace = ResourcesPlugin.getWorkspace();

    if (path.isUNC()) {
      return createStatus(IStatus.OK, "");
    }

    final IStatus locationStatus = workspace.validateLinkLocation(linkHandle,
        path);

    return locationStatus;
  }

  public void handleEvent(final Event event) {

    // TODO Auto-generated method stub

  }

  /**
   * Returns a new status object with the given severity and message.
   * 
   * @return a new status object with the given severity and message.
   */
  private IStatus createStatus(final int severity, final String message) {

    return new Status(severity, EAPlugin.getDefault().getBundle()
        .getSymbolicName(), severity, message, null);
  }
}
