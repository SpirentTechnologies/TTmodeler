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

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.mapping.ecore2xml.util.Ecore2XMLResource;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UML22UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;

import com.testingtech.ttworkbench.utp.ea.EAPlugin;

/**
 * Base class for articles on how to develop tools using UML2.
 */
public class Helper {

  public static boolean DEBUG = EAPlugin.getDefault().isDebugging();

  public static final ResourceSet RESOURCE_SET = new ResourceSetImpl();

  private static Resource outputResource;

  public static void out(String output) {

    if (DEBUG) {
      System.out.println(output);
    }
  }

  public static void out(Object obj) {

    if (DEBUG) {
      System.out.println(obj);
    }
  }

  public static void err(String error) {

    System.err.println(error);
  }

  /**
   * Assocciates a outputResource of uml2 type with the specific ".uml2"
   * extension.
   */
  @SuppressWarnings("unchecked")
  public static void registerResourceFactories() {

    EPackage.Registry.INSTANCE.put(UML22UMLResource.UML2_METAMODEL_NS_URI,
        UMLPackage.eINSTANCE);

    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
        UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
        Ecore2XMLResource.FILE_EXTENSION, Ecore2XMLResource.Factory.INSTANCE);
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
        UML22UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);

  }

  /**
   * Register and map the paths for existing libraries, metamodels and profiles.
   */
  @SuppressWarnings("unchecked")
  public static void registerPathmaps(URI uri) {

    URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uri
        .appendSegment("libraries").appendSegment(""));

    URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri
        .appendSegment("metamodels").appendSegment(""));

    URIConverter.URI_MAP.put(URI.createURI(UML22UMLResource.PROFILES_PATHMAP),
        uri.appendSegment("profiles").appendSegment(""));
  }

  /**
   * Saves a specified package to a outputResource with a specified URI.
   */
  @SuppressWarnings("unchecked")
  public static void save(Package aPackage, URI aURI) {

    getOutputResource(aURI).getContents().add(aPackage);

    try {
      getOutputResource(aURI).save(null);
      out("Done.");
    }
    catch (IOException ioe) {
      err(ioe.getMessage());
    }
  }

  /**
   * Saves a specified package to a outputResource with a specified URI.
   */
  @SuppressWarnings("unchecked")
  public static void save(org.eclipse.uml2.uml.Package aPackage,
      Resource aResource) {

    aResource.getContents().add(aPackage);

    try {
      aResource.save(null);

      out("Done.");
    }
    catch (IOException ioe) {
      err(ioe.getMessage());
    }
  }

  public static Resource getOutputResource(URI uri) {

    if (outputResource == null) {
      outputResource = RESOURCE_SET.createResource(uri);
    }
    return outputResource;
  }

  /**
   * Loads a package from a outputResource with a specified URI.
   */
  public static Package load(URI uri) {

    try {
      Resource resource = RESOURCE_SET.getResource(uri, true);
      return (Package) EcoreUtil.getObjectByType(resource.getContents(),
          UMLPackage.eINSTANCE.getPackage());
    }
    catch (WrappedException we) {
      err(we.getMessage());
      return null;
    }
  }

  /**
   * Loads a profile from a outputResource with a specified URI.
   */
  public static Profile loadProfile(URI uri) {

    try {
      Resource resource = RESOURCE_SET.getResource(uri, true);

      return (Profile) EcoreUtil.getObjectByType(resource.getContents(),
          UMLPackage.eINSTANCE.getProfile());
    }
    catch (WrappedException we) {
      err(we.getMessage());
      return null;
    }
  }
}
