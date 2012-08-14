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
package com.testingtech.ttworkbench.utp.ttcn3.legacy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;

import com.testingtech.ttworkbench.utp.core.TTmodelerConsts;

public class NameMaker {

  // global elements which maps to modules 
  // package, model, interface, class, testcontext
  public static Map<NamedElement, Map<NameKind, String>> globalElements = new HashMap<NamedElement, Map<NameKind, String>>();

  // set of reserved words
  public static Set<String> reservedWords = new HashSet<String>();

  public static boolean globalNames = false;

  // ttcn3 reserved words
  public final static String TTCN3_KEYWORDS[] = {
    "action", "activate", "address", "alive", "all", "alt", "altstep", "and",
    "and4b", "any", "anytype", "bitstring", "boolean", "case", "call",
    "catch", "char", "charstring", "check", "clear", "complement",
    "component", "connect", "const", "control", "create", "deactivate",
    "default", "disconnect", "display", "do", "done", "else", "encode",
    "enumerated", "error", "except", "exception", "execute", "extends",
    "extension", "external", "fail", "false", "float", "for", "from",
    "function", "getverdict", "getcall", "getreply", "goto", "group",
    "hexstring", "if", "ifpresent", "import", "in", "inconc", "infinity",
    "inout", "integer", "interleave", "kill", "killed", "label", "language",
    "length", "log", "map", "match", "message", "mixed", "mod", "modifies",
    "module", "modulepar", "mtc", "noblock", "none", "not", "not4b",
    "nowait", "null", "octetstring", "of", "omit", "on", "optional", "or",
    "or4b", "out", "override", "param", "pass", "pattern", "port",
    "procedure", "raise", "read", "receive", "record", "rem", "repeat",
    "reply", "return", "running", "runs", "select", "self", "send", "sender",
    "set", "setverdict", "signature", "start", "stop", "subset", "superset",
    "system", "template", "testcase", "timeout", "timer", "to", "trigger",
    "true", "type", "union", "universal", "unmap", "value", "valueof", "var",
    "variant", "verdicttype", "void", "while", "with", "xor", "xor4b", "verdict"
  };

  public static enum NameKind{
    ADDRESS_TYPE,
    ADDRESS_PAR,
    ATTRIBUTE_SIGN_GET,
    ATTRIBUTE_SIGN_SET,
    ATTRIBUTE_FIELD,
    DATA2OBJ_FUNC,
    DATA_PAR,
    DATA_TYPE,
    DEFAULT_PORT,
    ENUM,
    FUNC_PAR,
    INIT_FUNC,
    MODULE,
    OBJ2DATA_FUNC,
    OPERATION_SIGN,
    OPERATION_FUNC,
    RETURN_PAR,
    DATA_SELECTOR_FUNC,
    TEMPLATE,
    TEST_CONFIG_FUNC,
    TEST_CASE,
    TSI_COMP_TYPE,
    TSI_ELM_PRF,
    TSI_MAIN_TYPE,
    TSI_PAR,
    VARIABLE
  }

  GeneralModuleCreator creator;

  /**
   * Constructor.
   *
   */
  public NameMaker(GeneralModuleCreator creator){
    this.creator = creator;
  }

  /**
   * Get moduleCreator for given moduleName.
   * 
   * @param moduleName
   * @return
   */
  private GeneralModuleCreator getModuleCreator(String moduleName) {
    if(creator.getModuleName().equals(moduleName))
      return creator;
    else {
      return creator.getManager().getTransformers().get(
          moduleName);
    }
  }
  //
  /**
   * Get data type name for given module name.
   * @param moduleName
   * @return
   */
  public String getDataTypeName (String moduleName){
    GeneralModuleCreator gc = getModuleCreator(moduleName);
    if(gc != null && gc instanceof NamespaceModuleCreator){
      Namespace ns = ((NamespaceModuleCreator)gc).getNamespace();
      return createName(ns);
    }
    return null;
  }

  /**
   * Get composite name from part1 and part2.
   * @param part1
   * @param part2
   * @return
   */
  public static String createCompositeName(String part1, String part2){
    return part1 + TTmodelerConsts.GLOBAL_NAME_SEPARATOR + part2;
  }
 
  public static String createName(org.eclipse.uml2.uml.NamedElement element) {

    StringBuffer name = new StringBuffer(element.getName());

    if (globalNames)
    { 
      // while there is an owning element which has a name
      for (NamedElement owningElement = (NamedElement) element.getOwner();
      owningElement != null;
      owningElement = (NamedElement) owningElement.getOwner()) {

        // insert the name of the owning element at the beginning of the
        // resulting name.
        String owningElementName = owningElement.getName();          
        name.insert(0, owningElementName + TTmodelerConsts.GLOBAL_NAME_SEPARATOR);        
      }
    }

    return name.toString();

  }

}
