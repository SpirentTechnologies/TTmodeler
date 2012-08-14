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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;

public class U2TPHelper {

  /**
   * Method that applies a specified profile to a specified package
   */
  public static void applyProfile(Package aPackage, Profile aProfile) {

    aPackage.applyProfile(aProfile);
    Helper.out("Profile '" + aProfile.getQualifiedName()
               + "' applied to package '" + aPackage.getQualifiedName() + "'.");
  }

  /**
   * Method that applies a specified stereotype to a specified (named) element
   */
  public static void applyStereotype(NamedElement namedElement,
      Stereotype stereotype) {

    namedElement.applyStereotype(stereotype);

    EObject stereotypeApplication = namedElement
        .getStereotypeApplication(stereotype);
    if (stereotypeApplication == null) {
      Helper.err("stereotype application failed");
    }

    Helper.out("EAStereotype '" + stereotype.getQualifiedName()
               + "' applied to element '" + namedElement.getQualifiedName()
               + "'.");
  }

  /**
   * Method that gets and returns the value of a specified property of a
   * specified stereotype for a specified element.
   */
  public static Object getStereotypePropertyValue(NamedElement namedElement,
      Stereotype stereotype, Property property) {

    Object value = namedElement.getValue(stereotype, property.getName());
    Helper.out("Value of stereotype property '" + property.getQualifiedName()
               + "' on element '" + namedElement.getQualifiedName() + "' is "
               + String.valueOf(value) + ".");

    return value;
  }

  /**
   * Sets the value of a specified property of a specified stereotype for a
   * specified element to a specified value.
   */
  public static void setStereotypePropertyValue(NamedElement namedElement,
      Stereotype stereotype, Property property, Object value) {

    namedElement.setValue(stereotype, property.getName(), value);
    Helper.out("Value of stereotype property '" + property.getQualifiedName()
               + "' on element '" + namedElement.getQualifiedName() + ".");
  }

  /**
   * Check if the given stereotype belongs to the u2tp.
   */
  public static boolean isFromU2TP(String stereotype) {

    if (stereotype == null) {
      return false;
    }
    else if (stereotype.equals("TestComponent")) {
      return true;
    }
    else if (stereotype.equals("TestContext")) {
      return true;
    }
    else if (stereotype.equals("SUT")) {
      return true;
    }
    else if (stereotype.equals("TestObjective")) {
      return true;
    }
    else if (stereotype.equals("TestCase")) {
      return true;
    }
    else if (stereotype.equals("Default")) {
      return true;
    }
    else if (stereotype.equals("DefaultApplication")) {
      return true;
    }
    else if (stereotype.equals("ValidationAction")) {
      return true;
    }
    else if (stereotype.equals("LogAction")) {
      return true;
    }
    else if (stereotype.equals("FinishAction")) {
      return true;
    }
    else if (stereotype.equals("TestLog")) {
      return true;
    }
    else if (stereotype.equals("TestLogApplication")) {
      return true;
    }
    else if (stereotype.equals("CodingRule")) {
      return true;
    }
    else if (stereotype.equals("LiteralAny")) {
      return true;
    }
    else if (stereotype.equals("LiteralAnyOrNull")) {
      return true;
    }
    else if (stereotype.equals("DataPool")) {
      return true;
    }
    else if (stereotype.equals("DataPartition")) {
      return true;
    }
    else if (stereotype.equals("DataSelector")) {
      return true;
    }
    else if (stereotype.equals("TimeOut")) {
      return true;
    }
    else if (stereotype.equals("TimeOutMessage")) {
      return true;
    }
    else if (stereotype.equals("TimeOutAction")) {
      return true;
    }
    else if (stereotype.equals("StartTimerAction")) {
      return true;
    }
    else if (stereotype.equals("StopTimerAction")) {
      return true;
    }
    else if (stereotype.equals("ReadTimerAction")) {
      return true;
    }
    else if (stereotype.equals("TimerRunningAction")) {
      return true;
    }
    else if (stereotype.equals("GetTimezoneAction")) {
      return true;
    }
    else if (stereotype.equals("SetTimezoneAction")) {
      return true;
    }
    return false;
  }
}
