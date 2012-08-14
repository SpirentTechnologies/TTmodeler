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

import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * At the root of every UML2 model is a model element. It contains a (hierarchical)set 
 * of elements that together describe the physical system being modeled.
 */
public class ModelCreator {

  /**
   * Method that creates and returns a model with a specified name.
   */
  public static Model createModel(String name) {

    Model model = UMLFactory.eINSTANCE.createModel();
    model.setName(name);

    Helper.out("Model '" + model.getQualifiedName() + "' created.");

    return model;
  }

  // TODO nobody uses this
  /**
   * Method that creates and returnes a primitive type with a specified name in
   * a specified package.
   */
  public static PrimitiveType createPrimitiveType(Package aPackage,
      String aTypeName) {

    PrimitiveType primitiveType = aPackage.createOwnedPrimitiveType(aTypeName);
    Helper.out("Primitive type '" + primitiveType.getQualifiedName()
               + "' created.");

    return primitiveType;
  }

  /**
   * Method that imports the primitive type with a specified name from the UML2
   * primitive types library into a specified package and returns it.
   */
  public static PrimitiveType importPrimitiveType(Package aPackage,
      String aTypeName) {

    Model uml2Library = (Model) Helper.load(URI
        .createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI));
    PrimitiveType primitiveType = (PrimitiveType) uml2Library
        .getOwnedType(aTypeName);
    aPackage.createElementImport(primitiveType, VisibilityKind.PUBLIC_LITERAL);
    // package_.importElement(VisibilityKind.PUBLIC_LITERAL, primitiveType);

    Helper.out("Primitive type '" + primitiveType.getQualifiedName()
               + "' imported.");

    return primitiveType;
  }
}
