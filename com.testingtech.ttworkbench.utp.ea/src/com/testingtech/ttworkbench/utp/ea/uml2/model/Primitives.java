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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.resource.UMLResource;

public class Primitives {

  private static final String UNLIMITED_NATURAL = "UnlimitedNatural";

  private static final String INTEGER = "Integer";

  private static final String BOOLEAN = "Boolean";

  private static final String STRING = "String";

  public final PrimitiveType stringType;

  public final PrimitiveType booleanType;

  public final PrimitiveType integerType;

  public final PrimitiveType unlimitedNaturalType;

  private final Map<String, PrimitiveType> primitivesMap = new HashMap<String, PrimitiveType>();

  public Primitives() throws Exception {

    Model uml2Library = (Model) Helper.load(URI
        .createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));

    if (uml2Library == null) {
      throw new Exception("UML lib could not be created");
    }
    
    stringType = importPrimitiveType(uml2Library, STRING);
    booleanType = importPrimitiveType(uml2Library, BOOLEAN);
    integerType = importPrimitiveType(uml2Library, INTEGER);
    unlimitedNaturalType = importPrimitiveType(uml2Library, UNLIMITED_NATURAL);
  }

  /**
   * Method that imports the primitive type with a specified name from the UML2
   * primitive types library into a specified package and returns it.
   */
  private PrimitiveType importPrimitiveType(Model lib, String aTypeName) {

    PrimitiveType primitiveType = (PrimitiveType) lib.getOwnedType(aTypeName);

    primitivesMap.put(aTypeName, primitiveType);

    return primitiveType;
  }

  public void createPackageImports(Package uml2Package) {

    for (PrimitiveType type : primitivesMap.values()) {
      uml2Package.createElementImport(type, VisibilityKind.PUBLIC_LITERAL);
    }
  }

  public PrimitiveType getBooleanType() {

    return primitivesMap.get(BOOLEAN);
  }

  public PrimitiveType getIntegerType() {

    return primitivesMap.get(INTEGER);
  }

  public PrimitiveType getStringType() {

    return primitivesMap.get(STRING);
  }

  public PrimitiveType getUnlimitedNaturalType() {

    return primitivesMap.get(UNLIMITED_NATURAL);
  }

}
