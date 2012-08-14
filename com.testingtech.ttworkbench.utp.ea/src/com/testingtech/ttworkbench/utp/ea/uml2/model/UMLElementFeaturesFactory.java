/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2005-2012.  All Rights Reserved.
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
package com.testingtech.ttworkbench.utp.ea.uml2.model;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.ParameterEffectKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import com.testingtech.ttworkbench.utp.ea.UMLElementsCache;
import com.testingtech.ttworkbench.utp.ea.access.elementFeatures.EAAttribute;
import com.testingtech.ttworkbench.utp.ea.access.elementFeatures.EAParameter;
import com.testingtech.ttworkbench.utp.ea.access.elementFeatures.Method;
import com.testingtech.ttworkbench.utp.ea.access.repository.EACollection;

public class UMLElementFeaturesFactory {

  public static Property createAttribute(Class aClass,
      EAAttribute anEAAttribute, UMLElementsCache cache) {

    /* LowerBound, UpperBound */
    int lowerBound = Integer.valueOf(anEAAttribute.getLowerBound()).intValue();

    /* Type */
    String attributeType = anEAAttribute.getType();
    Type type = cache.getType(attributeType, anEAAttribute.getClassifierID());

    assert type != null : "type should be non-null";

    int upperBound = convertCardinality(anEAAttribute.getUpperBound());
    /* Creates the property for the specified class */
    Property attribute = aClass.createOwnedAttribute(anEAAttribute.getName(),
        type, lowerBound, upperBound);

    StringBuffer sb = new StringBuffer();
    sb.append("Attribute '");
    sb.append(attribute.getQualifiedName());
    sb.append("' : ");
    sb.append(type.getQualifiedName());
    sb.append(" [");
    sb.append(lowerBound);
    sb.append("..");
    sb.append(LiteralUnlimitedNatural.UNLIMITED == upperBound ? "*" : String
        .valueOf(upperBound));
    sb.append("]");
    sb.append(" created.");
    Helper.out(sb.toString());

    return attribute;
  }

  public static Property createInterfaceAttribute(Interface anInterface,
      EAAttribute anEAAttribute, UMLElementsCache cache) {

    /* Type */
    String attributeType = anEAAttribute.getType();
    Type type = cache.getType(attributeType, anEAAttribute.getClassifierID());

    /* LowerBound, UpperBound */
    /* Creates the property for the specified interface */
    Property ownedAttribute = anInterface.createOwnedAttribute(anEAAttribute
        .getName(), type, convertCardinality(anEAAttribute.getLowerBound()),
        convertCardinality(anEAAttribute.getUpperBound()));

    StringBuffer sb = new StringBuffer();
    sb.append("Attribute '");
    sb.append(ownedAttribute.getQualifiedName());
    sb.append("' : ");
    sb.append(type.getQualifiedName());
    sb.append(" created.");
    Helper.out(sb.toString());

    return ownedAttribute;
  }

  public static Operation createInterfaceOperation(Interface interface_,
      Method eaMethod, UMLElementsCache cache) {

    return createOperationInternal(interface_, eaMethod, cache);
  }

  /**
   * Method that creates and return an operation of a specified class.
   */
  public static Operation createOperation(Class class_, Method eaMethod,
      UMLElementsCache cache) {

    return createOperationInternal(class_, eaMethod, cache);
  }

  private static Operation createOperationInternal(Object target,
      Method eaMethod, UMLElementsCache cache) {

    Operation operation = null;
    Type umlReturnType = cache.getType(eaMethod.getReturnType(), eaMethod
        .getClassifierID());
    /**
     * we have a similar method in interface and class but they do not share a
     * common superclass, so we'll using this switch.
     */
    if (target instanceof Interface) {
      Interface umlInterface = (Interface) target;
      if (umlReturnType != null) {
        operation = umlInterface.createOwnedOperation(eaMethod.getName(), null,
            null, umlReturnType);
      }
      else {
        operation = umlInterface.createOwnedOperation(eaMethod.getName(), null,
            null);
      }
    }
    else if (target instanceof Class) {
      Class umlClass = (Class) target;
      if (umlReturnType != null) {
        operation = umlClass.createOwnedOperation(eaMethod.getName(), null,
            null, umlReturnType);
      }
      else {
        operation = umlClass.createOwnedOperation(eaMethod.getName(), null,
            null);
      }
    }
    if (operation != null) {
      createOperationParameters(eaMethod, cache, operation);
      setOperationBounds(eaMethod, operation);
    }
    else {
      System.err.println("UML operation '" + eaMethod.getName()
                         + "' could not be created");
    }

    return operation;
  }

  public static void createOperationParameters(Method eaMethod,
      UMLElementsCache cache, Operation operation) {

    EACollection eaParameters = eaMethod.getParameters();
    int count = eaParameters.getCount();
    /* Iterates through the specific attributes */
    for (int i = 0; i < count; i++) {
      EAParameter eaParameter = new EAParameter(eaParameters.getAt(i));
      Type parameterType = cache.getType(eaParameter.getType(), eaParameter
          .getClassifierID());
      // TODO default value for direction and effect? compute real values!
      createParameter(operation, eaParameter.getName(), null, null,
          parameterType);
    }
  }

  public static Parameter createParameter(Operation operation, String name,
      ParameterDirectionKind direction, ParameterEffectKind effect, Type type) {

    Parameter parameter = operation.createOwnedParameter(name, type);
    parameter.setDirection(direction);
    parameter.setEffect(effect);

    StringBuffer sb = new StringBuffer();
    sb.append("OwnedParameter '");
    sb.append(parameter.getQualifiedName());
    sb.append("' : ");
    sb.append(type != null ? type.getQualifiedName() : "(no type)");
    sb.append(" , ");
    sb.append(direction != null ? direction.getName() : "(no direction)");
    sb.append(" , ");
    sb.append(effect != null ? effect.getName() : "(no effect)");
    sb.append(" created.");
    Helper.out(sb.toString());

    return parameter;
  }

  private static void setOperationBounds(Method eaMethod, Operation operation) {

    operation.setLower(1);
    operation
        .setUpper(eaMethod.getReturnIsArray() ? LiteralUnlimitedNatural.UNLIMITED
            : 1);
  }

  private static int convertCardinality(String aUMLCardinality) {

    if (aUMLCardinality.equals("*"))
      return LiteralUnlimitedNatural.UNLIMITED;
    else {
      try {
        int result = Integer.valueOf(aUMLCardinality).intValue();
        return result;
      }
      catch (NumberFormatException e) {
        Helper.out("cardinality of \"" + aUMLCardinality + "\" is an integer");
        return 0;
      }
    }
  }
}
