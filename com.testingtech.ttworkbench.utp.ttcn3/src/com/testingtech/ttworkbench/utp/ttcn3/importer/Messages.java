/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2001-2012.  All Rights Reserved.
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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS{
  private static final String BUNDLE_NAME = "com.testingtech.ttworkbench.utp.ttcn3.importer.messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
      .getBundle(BUNDLE_NAME);

  private Messages() {

  }

  public static String getString(String key) {

    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }
  
  public static String Lbl__Module_name;

  public static String Lbl__Select_model_importer;

  public static String Lbl__Source_folder;

  public static String Msg__Container_x_does_not_exist;

  public static String Msg__Do_not_generate_main_module;

  public static String Msg__Module_already_exists;

  public static String Msg__Unable_to_create_module_file_source_folder_not_under_locations;

  public static String Ttl__New_TTCN3_Module;

    public static String Lbl__Browse;

    public static String Name__untitled;

    public static String Msg__File_container_must_be_specified;

    public static String Msg__File_container_must_exist;

    public static String Msg__Project_must_be_writable;

    public static String Msg__Module_name_must_be_specified;

    public static String Msg__Module_name_may_not_start_or_end_with_minus;

    public static String Msg__Unallowed_char_sequence_minus_minus;

    public static String Msg__Select_new_module_container;

    public static String Msg__Unallowed_char_in_module_name;

    public static String Msg__file_already_exists_in_that_folder;

    public static String Msg__not_a_TTCN3_project;

    public static String Msg__Error_could_not_find_the_containing_project;

    public static String Lbl__New_TTCN3_Module;

    public static String Msg__Create_a_TTCN3_module;

    public static String Msg__Overwrite_existing_root_module;

    public static String Lbl__Root_module_name;

    public static String Msg__Root_module_already_exists;

  public static String Ttl__Error;

  public static String Ttl__New_ttcn_module;

  public static String Ttl__New_Ttcn_Module;
  
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }
  
}
