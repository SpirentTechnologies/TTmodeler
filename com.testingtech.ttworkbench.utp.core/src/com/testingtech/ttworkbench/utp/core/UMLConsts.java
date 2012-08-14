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

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.InterfaceImpl;


public class UMLConsts {

  public static final String UML_CLASS_NAME = Class.class.getSimpleName();
  public static final String UML_CLASS_IMPL_NAME = ClassImpl.class.getSimpleName();
  public static final String UML_INTERFACE_NAME = Interface.class.getSimpleName();
  public static final Object UML_INTERFACE_IMPL_NAME = InterfaceImpl.class.getSimpleName();
  // unfortunately, no type name constants seem to be stored anywhere in the eclipse
  // uml2 package. at least i couldn't find them.
  public static final String UML_PRIMITIVE_TYPE_BOOLEAN_NAME = "Boolean"; //$NON-NLS-1$
  public static final String UML_PRIMITIVE_TYPE_STRING_NAME = "String"; //$NON-NLS-1$
  public static final String UML_PRIMITIVE_TYPE_INTEGER_NAME = "Integer"; //$NON-NLS-1$
  public static final String UML_TIMEOUT_AFTER_PREFIX = "after "; //$NON-NLS-1$  
  public static final String UML_ALT_FRAGMENT_OPERATOR_NAME = "alt"; //$NON-NLS-1$
  public static final String UML_ALT_ELSE_OPERAND_NAME = "else"; //$NON-NLS-1$
  public static final String UML_ELSE_KEYWORD = "else"; //$NON-NLS-1$


}
