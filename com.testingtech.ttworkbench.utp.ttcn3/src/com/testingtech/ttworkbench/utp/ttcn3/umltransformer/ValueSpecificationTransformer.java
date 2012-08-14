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
package com.testingtech.ttworkbench.utp.ttcn3.umltransformer;

import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.ValueSpecification;

import com.testingtech.muttcn.kernel.Expression;
import com.testingtech.muttcn.kernel.Value;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.ValueConstructor;

@SuppressWarnings("nls")
public class ValueSpecificationTransformer implements UML2TTCN3Transformer<ValueSpecification, Expression>{
  
  

  private static String VALUE_FALSE = "false";
  private static String VALUE_TRUE = "true";

  public Expression transform(ValueSpecification valueSpecification) {
    
    ValueConstructor valueConstructor = new ValueConstructor();
    
    // value type defined by the type of value specification
    Value value = null;
    
    if (valueSpecification instanceof LiteralString) {
      String stringVal = ((LiteralString) valueSpecification).getValue();
      value = valueConstructor.constructCharstringValue(stringVal);
    }
    else if (valueSpecification instanceof LiteralInteger) {
      int intVal = ((LiteralInteger) valueSpecification).getValue();
      value = valueConstructor.constructIntegerValue(intVal);
    }
    else if (valueSpecification instanceof LiteralBoolean) {
      boolean booleanVal = ((LiteralBoolean) valueSpecification).isValue();
      String booleanValAsString = VALUE_FALSE;
      if (booleanVal == true) {
        booleanValAsString = VALUE_TRUE;
      }
      value = valueConstructor.constructConstantValue(booleanValAsString);
    }
    else if (valueSpecification instanceof OpaqueExpression) {
      OpaqueExpression valueSpecificationAsOpaque = (OpaqueExpression) valueSpecification;
      return new OpaqueExpressionTransformer().transform(valueSpecificationAsOpaque);
    }

    return value;
    
  }
  
}
