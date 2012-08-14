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
package com.testingtech.ttworkbench.utp.core;

@SuppressWarnings("nls")
public class TTmodelerConsts {



  /**
   * the name given to an implicitly generated return value, in case the UML Model 
   * did not specify a field to store the value
   */
  public static final String IMPLICIT_RETURN_VALUE_NAME = "__returnValue"; 

  public static final String UNIQUE_VAR_NAME_APPENDIX = ""; // "_var"; 
  public static final String UNIQUE_TIMER_NAME_APPENDIX = ""; 
  public static final int VARIABLE = 0;
  public static final int TIMER = 1;

  // constants for return results of transformation error checking

  public static final int NO_TRANSFORMATION_ERROR = 0;
  public static final int RECOVERABLE_TRANSFORMATION_ERROR = 1;
  public static final int IRRECOVERABLE_TRANSFORMATION_ERROR = 2;

  public static final String ARBITER_SETVERDICT_OPERATION_NAME = "setVerdict";

  public static final boolean COMMENT_FORMAT_PLAINTEXT = false;
  public static final boolean COMMENT_FORMAT_HTML = true;


  /**
   * the string used to separate element names while constructing a long, global name.
   */
  public static final String GLOBAL_NAME_SEPARATOR = "__";

  /**
   * name prefix of function for test configuration
   */
  public static final String TEST_CONFIG_FUNCTION_NAME = "CONFIG";

  public static final String INIT_FUNC_NAME = "INIT";

  public static final String DEFAULT_MTC_TYPE_NAME = "MTC__DEFAULT";

  public static final String TSI_PAR_NAME = "tsi";

  /**
   * name of test component as tsi main 
   */
  public static final String TSI_MAIN_TYPE_NAME = "TSI__MAIN";
  
  
  public static final String DEFAULT_PORT_NAME = "PORT__DEFAULT";


  /**
   * name prefix of test component for test system interface
   */
  public static final String TSI_COMP_TYPE_NAME_PRF = "TSI";
  
  
  // name appendices applied to the transformed ttcn-3 elements 

  public static final String LIFELINE_FUNCTION_APPENDIX = "__lifeline";

  public static final String RECORD_OF_TYPE_APPENDIX = "__recordOf";
  
  public static final String RECORD_TYPE_APPENDIX = "__record";

  public static final String SET_OF_TYPE_APPENDIX = "__setOf";


  public static final String ADDRESS_APPENDIX = "__address";
  
  public static final String EXTERNAL_FUNCTION_APPENDIX = "__extfunc";

  
  public static final String ANY_TIMER = "any timer";
  public static final String ALL_TIMER = "all timer";
  public static final String ANY = "any";
  public static final String ALL = "all";

  public static final int TIMER_OPERAND_TYPE = 0;
  
  
  // keywords, later to turn into stereotypes
  
  
  public static final String TIMEOUT = "timeout";
  
  /**
   * the name of the base type attribute of an exception class
   */
  public static final String EXCEPTION_BASETYPE_ATTRIBUTE_NAME = "baseType"; 
  
  /**
   * the keyword to mark a comment as containing the 'runs on' component name
   */
  public static final String RUNS_ON = "runsOn"; 
  
  /**
   * the keyword to mark an element as containing the name of a variable reference
   */
  public static final String VARIABLE_REFERENCE = "variable"; 
  
  /**
   * the keyword to mark an element as containing a value
   */
  public static final String VALUE_REFERENCE = "value"; 
  
  /**
   * the keyword to mark an element as containing a timer
   */
  public static final String TIMER_REFERENCE = "timer"; 
  
  /**
   * the keyword to mark an element as representing a TTCN-3 alt
   */
  public static final String ALT_TTCN3 = "alt"; 
  
  /**
   * the keyword to mark a UML attribute for generation an external initialization function
   */
  public static final String EXTERNAL_INIT_FUNCTION = "externalInit"; 

  /**
   * the name of an attribute representing a type
   */
  public static final String TYPE_ATTRIBUTE_NAME = "type"; 
  
  public static final String RETURN_PARAM_NAME = "returnParam"; 
  
}
