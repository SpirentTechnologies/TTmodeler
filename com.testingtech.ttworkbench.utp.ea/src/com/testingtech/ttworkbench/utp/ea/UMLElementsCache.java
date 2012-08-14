/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2012.  All Rights Reserved.
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
package com.testingtech.ttworkbench.utp.ea;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Type;

import com.testingtech.ttworkbench.utp.ea.access.element.EAElement;
import com.testingtech.ttworkbench.utp.ea.access.repository.EAPackage;
import com.testingtech.ttworkbench.utp.ea.uml2.model.Primitives;

public class UMLElementsCache {

  private Primitives primitives = null;

  // TODO clarify which elements are put into this table and why
  /*
   * A hash table that contains some elements from the model, indexed by their
   * GUID from EA.
   */
  protected final Map<Integer, Element> elementsGlobalTable;

  /*
   * packages, because IDs overlap
   */
  protected final Map<Integer, Package> packagesGlobalTable;

  public UMLElementsCache() throws Exception {

    elementsGlobalTable = new Hashtable<Integer, Element>();
    packagesGlobalTable = new Hashtable<Integer, Package>();

    primitives = new Primitives();
  }

  public void createPrimitiveImports(Package uml2Package) {

    primitives.createPackageImports(uml2Package);
  }

  public Element getUMLElement(EAElement elem) {

    return elementsGlobalTable.get(elem.getElementID());
  }

  public Element getUMLElement(Integer id) {

    return elementsGlobalTable.get(id);
  }

  public void putUMLElement(EAElement ea, Element uml) {

    elementsGlobalTable.put(ea.getElementID(), uml);
  }

  public Package getUMLPackage(EAPackage pack) {

    return packagesGlobalTable.get(pack.getPackageID());
  }

  public void putUMLPackage(EAPackage ea, Package uml) {

    packagesGlobalTable.put(ea.getPackageID(), uml);
  }

  public Type getType(String type, Integer classifierID) {

    assert type != null : "type should be non-null";

    Type rtnType = null;

    if (classifierID.intValue() == 0) {
      if ("Integer".equals(type)) {
        rtnType = primitives.getIntegerType();
      }
      else if ("String".equals(type)) {
        rtnType = primitives.getStringType();
      }
      else if ("Boolean".equals(type)) {
        rtnType = primitives.getBooleanType();
      }
    }
    else {
      rtnType = (Type) getUMLElement(classifierID);
    }
    return rtnType;
  }
}
