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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import com.testingtech.ttworkbench.core.CorePlugin;
import com.testingtech.ttworkbench.core.ttcn3.ITTCN3Project;
import com.testingtech.ttworkbench.metamodel.core.foreignmodel.ForeignModelTargetDescriptor;
import com.testingtech.ttworkbench.metamodel.core.util.ModelOperationUtil;

public class NewModuleCreationPage extends WizardNewFileCreationPage {

  public static final String NEW_MODULE_PAGE_ID = "NewTTCN3ModulePageId"; //$NON-NLS-1$

  public static final String NEW_MODULE_PAGE_LABEL = Messages.Lbl__New_TTCN3_Module;

  public static final String NEW_MODULE_PAGE_DESC = Messages.Msg__Create_a_TTCN3_module;

  public static final String OVERWRITE_ROOT_LABLE = Messages.Msg__Overwrite_existing_root_module;

  public static final String NO_MAIN_MODULE_LABLE = Messages.Msg__Do_not_generate_main_module;

  private IDialogSettings curSettings = CorePlugin.getDefault().getDialogSettings();

  private Map<String, Button> boxes= new HashMap<String, Button>();
  
  private Composite resourceNameGroup;
  /**
   * Constructor.
   */
  public NewModuleCreationPage(IStructuredSelection selection) {

    super(NEW_MODULE_PAGE_ID, selection);
    setTitle(NEW_MODULE_PAGE_LABEL);
    setDescription(NEW_MODULE_PAGE_DESC);
  }
  
  

  @Override
  public void createControl(Composite parent) {

    super.createControl(parent);
    Control control = getControl();
    
    resourceNameGroup = getResourceNameGroup((Composite)control, Messages.Lbl__Root_module_name);
    
    // overwrite root module flag
    Button overwriteFlag = addCheckBox((Composite)control, OVERWRITE_ROOT_LABLE, 0);
    indent(overwriteFlag);
    overwriteFlag.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent e) {
        checkOverwriteOption();
      }

      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });
    // "do not generate" flag
    Button noMainModuleFlag = addCheckBox((Composite)control, NO_MAIN_MODULE_LABLE, 0);
    noMainModuleFlag.addSelectionListener(new SelectionListener() {

      public void widgetSelected(SelectionEvent e) {
        checkMainModuleOption();
      }

      public void widgetDefaultSelected(SelectionEvent e) {

      }
    });
    
    checkMainModuleOption();
  }

  protected void createAdvancedControls(Composite parent) {

    // do not create advance
  }

  protected String getNewFileLabel() {

    return Messages.Lbl__Root_module_name;
  }

  @Override
  public String getFileName() {
    String moduleName = super.getFileName();
    if(!resourceNameGroup.isEnabled() || moduleName == null) {
      return null;
    }
    return moduleName+'.'+ITTCN3Project.TTCN3_MODULE_DEFAULT_EXTENSION;
  }

  public ForeignModelTargetDescriptor getNewModuleDescriptor() {

    IPath cont = getContainerFullPath();
    String nm = getFileName();
    return new ForeignModelTargetDescriptor(cont, nm);
  }
  
  /**
   * Get text field with given label recursively from given control.
   * @param control
   * @param label
   * @return
   */
  protected Composite getResourceNameGroup(Composite control, String label){
    Composite result = null;
    Control[] children = (control).getChildren();
    for (int i = 0; i < children.length; i++) {
      Control ch = children[i];
      if(ch instanceof Label){
        if(((Label)ch).getText().equals(label)){
          result =((Label)ch).getParent();
          break;
        }
      }else if(ch instanceof Composite){
        Composite found = getResourceNameGroup((Composite)ch, label);
        if(found != null){
          result = found;
          break;
        }
      }
    }
    return result;
  }

  /**
   * Enable/disable group according to doEnable.
   * @param group
   * @param doEnable
   */
  protected void enableResourceNameGroup(Composite group, boolean doEnable){
    if(group != null){
      group.setEnabled(doEnable);
      Control [] children = group.getChildren();
      for (int i = 0; i < children.length; i++) {
        children[i].setEnabled(doEnable);
      }
    }
  }
  
  @Override
  protected boolean validatePage() {

    boolean result = true;
    result = checkContainer();
    result = result && checkOptions();
    setPageComplete(result);
    return result;
  }

  /**
   * Add button with given label and indentation in parent.
   * @param parent
   * @param label
   * @param indentation
   * @return
   */
  protected Button addCheckBox(Composite parent, String label, int indentation) {   
    Button checkBox= new Button(parent, SWT.CHECK);
    checkBox.setText(label);
    
    GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
    gd.horizontalIndent= indentation;
    gd.horizontalSpan= 2;
    checkBox.setLayoutData(gd);
    checkBox.setSelection(curSettings.getBoolean(label));
    boxes.put(label, checkBox);
    return checkBox;
  }
  
  protected boolean checkContainer(){
    boolean result = true;
    setPageComplete(result);
    return result;
  }
  
  /**
   * Check option for overwriting existing main module.
   * @return
   */
  protected boolean checkOverwriteOption(){
    boolean result = true;
    setErrorMessage(null);
    setMessage(null);
    
    Button overwriteFlag = boxes.get(OVERWRITE_ROOT_LABLE);
    if(overwriteFlag.isEnabled()){
      // check if target file already exists
      boolean overwriteFlagValue = boxes.get(OVERWRITE_ROOT_LABLE).getSelection();
      if (!overwriteFlagValue) {
        IPath cont = getContainerFullPath();
        String nm = getFileName();
        if (cont != null && nm != null) {
          String fullName = ModelOperationUtil.getFullFileName(cont, nm);
          // TODO: check also if module in other container or referenced
          // projects
          File file = new File(fullName);
          if (file.exists()) {
            setErrorMessage(Messages.Msg__Root_module_already_exists);
            result = false;
          }
        }
      }
    }
    updateValue(OVERWRITE_ROOT_LABLE);
    return result;
  }

  /**
   * Check option for generating main module and enable/disable
   * related options for main module.
   * @return
   */
  protected boolean checkMainModuleOption(){
    boolean noMainModule = boxes.get(NO_MAIN_MODULE_LABLE).getSelection();
    boolean result = true;
    if(noMainModule){
      boxes.get(OVERWRITE_ROOT_LABLE).setEnabled(false);
      enableResourceNameGroup(resourceNameGroup, false);
    }
    else {
      boxes.get(OVERWRITE_ROOT_LABLE).setEnabled(true);
      enableResourceNameGroup(resourceNameGroup, true);
      result = checkOverwriteOption();
    }
    updateValue(NO_MAIN_MODULE_LABLE);
    return result;
  }
  
  /**
   * Check options in this page.
   * @return
   */
  protected boolean checkOptions(){
    if(boxes.size() == 0)
      return true;
    boolean result = checkMainModuleOption();
    return result;
  }
  
  /**
   * Create indentation for given contol.
   * @param control
   */
  protected static void indent(Control control) {
    GridData gridData= new GridData();
    gridData.horizontalIndent= 20;
    control.setLayoutData(gridData);    
  }
   
  /**
   * Save values for options.
   * @param label
   */
  protected void updateValue(String label){
    curSettings.put(label, boxes.get(label).getSelection());
  }
}
