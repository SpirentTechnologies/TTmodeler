/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2006-2012.  All Rights Reserved.
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
package com.testingtech.ttworkbench.utp.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLPackage;

public class UTPModelLoader {

  /**
   * Load uml2 packages.
   * 
   * @param uri
   * @return
   */
  public static List<org.eclipse.uml2.uml.Package> loadPackages(URI uri) {
    List<org.eclipse.uml2.uml.Package> result = 
      new ArrayList<org.eclipse.uml2.uml.Package>();
    try {
      ResourceSet set = new ResourceSetImpl();
      Resource resource = set.getResource(uri, true);
      Collection co = EcoreUtil.getObjectsByType(resource.getContents(),
          UMLPackage.Literals.PACKAGE);
      if (co != null) {
        Iterator it = co.iterator();
        while (it.hasNext()) {
          Object obj = it.next();
          if(obj instanceof org.eclipse.uml2.uml.Package)
            result.add((org.eclipse.uml2.uml.Package)obj);
        }
      }
    }
    catch (WrappedException we) {
      System.out.println(we.getMessage());
    }
    return result;
  }

  /**
   * Check if profile with qualifiedName applies to pkg
   * 
   * @param pkg
   * @param qualifiedName
   * @return
   */
  public static boolean isProfileApplied(org.eclipse.uml2.uml.Package pkg,
      String qualifiedName) {

    boolean result = false;
    Profile profile = pkg.getAppliedProfile(qualifiedName, true);
    if (profile != null)
      result = true;
    return result;
  }
}
