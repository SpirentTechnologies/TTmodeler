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
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.VisibilityKind;

import com.testingtech.ttworkbench.utp.ea.access.element.EAElement;
import com.testingtech.ttworkbench.utp.ea.access.elementFeatures.EAAttribute;
import com.testingtech.ttworkbench.utp.ea.access.repository.EACollection;
import com.testingtech.ttworkbench.utp.ea.access.repository.EAPackage;

public class UMLElementFactory extends Helper {

  /**
   * Method that creates and returns a package with a specified name in a
   * specified nesting package.
   */
  public static Package createPackage(Package nestingPackage,
      EAPackage eaPackage) {

    Package createdPackage = nestingPackage.createNestedPackage(eaPackage
        .getName());
    /* Visibility */
    createdPackage.setVisibility(VisibilityKind.get(((eaPackage.getElement())
        .getVisibility()).toLowerCase()));

    out("EAPackage '" + createdPackage.getQualifiedName() + "' created.");

    return createdPackage;
  }

  /**
   * Creates and returns a(n) abstract class with a specified name in a
   * specified package.
   */
  public static Class createClass(Package aPackage, EAElement anEAClass) {

    /* Creates "Class" element */
    Class createdClass = aPackage.createOwnedClass(anEAClass.getName(),
        anEAClass.getAbstract());

    /* Visibility */
    createdClass.setVisibility(VisibilityKind.get((anEAClass.getVisibility())
        .toLowerCase()));
    createdClass.setIsLeaf(anEAClass.getIsLeaf());

    out("Class '" + createdClass.getQualifiedName() + "' created.");

    return createdClass;
  }

  /**
   * Creates and returns an interface with a specified name in a specified
   * package.
   */
  public static Interface createInterface(Package aPackage,
      EAElement anEAInterface) {

    /* Creates the "Interface" element */
    Interface createdInterface = aPackage.createOwnedInterface(anEAInterface
        .getName());

    /* IsAbstract */
    createdInterface.setIsAbstract(anEAInterface.getAbstract());

    /* Visibility */
    createdInterface.setVisibility(VisibilityKind.get((anEAInterface
        .getVisibility()).toLowerCase()));

    /* IsLeaf */
    createdInterface.setIsLeaf(anEAInterface.getIsLeaf());

    out("Interface '" + createdInterface.getQualifiedName() + "' created.");

    return createdInterface;
  }

  /**
   * Creates and returns an enumeration with a specified name in a specified
   * package.
   */
  public static Enumeration createEnumeration(Package aPackage,
      EAElement anEAEnumeration) {

    Enumeration createdEnumeration = aPackage
        .createOwnedEnumeration(anEAEnumeration.getName());
    createdEnumeration.setIsAbstract(anEAEnumeration.getAbstract());

    /* Visibility */
    createdEnumeration.setVisibility(VisibilityKind.get((anEAEnumeration
        .getVisibility()).toLowerCase()));
    createdEnumeration.setIsLeaf(anEAEnumeration.getIsLeaf());

    /* Get EnumerationLiterals from the EA model */
    EACollection eaEnumerationLiterals = anEAEnumeration.getAttributes();
    int count = eaEnumerationLiterals.getCount();

    for (int i = 0; i < count; i++) {

      EAAttribute eaEnumerationLiteral = new EAAttribute(eaEnumerationLiterals
          .getAt(i));
      createEnumerationLiteral(createdEnumeration, eaEnumerationLiteral);
    }

    out("Enumeration '" + createdEnumeration.getQualifiedName() + "' created.");

    return createdEnumeration;
  }

  /**
   * Creates and returns an enumeration literal with a specified name ia a
   * specified enumeration.
   */
  public static EnumerationLiteral createEnumerationLiteral(
      Enumeration enumeration, EAAttribute eaEnumerationLiteral) {

    EnumerationLiteral enumerationLiteral = enumeration
        .createOwnedLiteral(eaEnumerationLiteral.getName());

    /* Visibility */
    enumerationLiteral.setVisibility(VisibilityKind.get((eaEnumerationLiteral
        .getVisibility()).toLowerCase()));

    out("Enumeration literal '" + enumerationLiteral.getQualifiedName()
        + "' created.");

    return enumerationLiteral;
  }

  /**
   * Creates and returns an uml2 element such as: class, interface, enumeration,
   * etc.
   */
  public static Element createElement(Package aPackage, EAElement anEAElement) {

    String type = anEAElement.getType();

    if (type.equals(EAElement.TYPE_CLASS)) {

      String stereotype = anEAElement.getStereotype();
      if (stereotype.equals(EAElement.STEREOTYPE_ENUMERATION)) {
        return createEnumeration(aPackage, anEAElement);
      }
      else {
        return createClass(aPackage, anEAElement);
      }
    }
    else if (type.equals(EAElement.TYPE_INTERFACE)) {
      return createInterface(aPackage, anEAElement);
    }
    else if (type.equals(EAElement.TYPE_PROVIDED_INTERFACE)) {
      return createProvidedInterface(aPackage, anEAElement);
    }
    else if (type.equals(EAElement.TYPE_PORT)) {
      System.err.println("TODO check: no creating of port in createElement");
      return null;
    }
    else if (type.compareTo("Object") == 0) {
      System.out
          .println("no UML element created for type \"Object\" (element name = \""
                   + anEAElement.getName() + "\"");
      return null;
    }
    else {
      System.err.println("TODO: implement creation for elements of type \""
                         + type + "\"");
      return null;
    }
  }

  private static Element createProvidedInterface(Package aPackage,
      EAElement anEAElement) {

    System.err
        .println("TODO implement UMLElementFactory#createProvidedInterface() for "
                 + anEAElement.getName() + " in package " + aPackage.getName());
    return null;
  }

  public static Port createOwnedPort(Class umlClassElement, EAElement eaElement) {

    return umlClassElement.createOwnedPort(eaElement.getName(), (Type) null);
  }
}
