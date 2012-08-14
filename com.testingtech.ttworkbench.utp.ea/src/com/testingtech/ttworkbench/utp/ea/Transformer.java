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
package com.testingtech.ttworkbench.utp.ea;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.ne.so_net.ga2.no_ji.jcom.JComException;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import com.testingtech.ttworkbench.utp.ea.access.element.EAElement;
import com.testingtech.ttworkbench.utp.ea.access.elementFeatures.EAAttribute;
import com.testingtech.ttworkbench.utp.ea.access.elementFeatures.Method;
import com.testingtech.ttworkbench.utp.ea.access.repository.EACollection;
import com.testingtech.ttworkbench.utp.ea.access.repository.EAPackage;
import com.testingtech.ttworkbench.utp.ea.access.repository.EARepository;
import com.testingtech.ttworkbench.utp.ea.uml2.model.Helper;
import com.testingtech.ttworkbench.utp.ea.uml2.model.ModelCreator;
import com.testingtech.ttworkbench.utp.ea.uml2.model.U2TPHelper;
import com.testingtech.ttworkbench.utp.ea.uml2.model.UMLElementFactory;
import com.testingtech.ttworkbench.utp.ea.uml2.model.UMLElementFeaturesFactory;

/**
 * @author Diana Alina Serbanescu
 */

public class Transformer {

  Logger logger;

  /* List of models, which are entry-points in a EA Project */
  LinkedList<Package> uml2Models;

  /* UML 2.0 Testing Profile */
  static public Profile u2tp = null;

  UMLElementsCache elementsCache;

  /**
   * The constructor for the "Transformer" class.
   * @throws Exception 
   */
  public Transformer(final String uriName) throws Exception {

    logger = Logger.getLogger(Transformer.class);
    BasicConfigurator.configure();

    elementsCache = new UMLElementsCache();
    uml2Models = new LinkedList<Package>();

    Helper.registerResourceFactories();
    Helper.registerPathmaps(URI.createURI(uriName));
  }

  /**
   * The constructor for the "Transformer" class.
   * @throws Exception 
   */
  public Transformer() throws Exception {

    logger = Logger.getLogger(Transformer.class);
    BasicConfigurator.configure();
    elementsCache = new UMLElementsCache();
    uml2Models = new LinkedList<Package>();

    Helper.registerResourceFactories();
  }

  /**
   * @param monitor
   *          a progress monitor
   * @param source
   *          the source file as a file system path
   * @param destination
   *          the destination file in the workspace to be created
   * @return
   */
  public boolean transform(final IProgressMonitor monitor, final String source,
      final IFile destination) {

    long startTime = System.currentTimeMillis();

    monitor.beginTask("Transforming Model: ", 10);

    /* Creates a new ReleaseManager */
    final ReleaseManager rm = new ReleaseManager();

    try {

      /* Creates an EA repository object */
      final EARepository eaRepository = new EARepository(rm);
      monitor.subTask("Creating EA EARepository");
      monitor.worked(1);
      if (monitor.isCanceled()) {
        return false;
      }

      /* Opens an *.EAP file and associates this file with the repository object */
      eaRepository.openFile(source);
      monitor.subTask("Opening EA EARepository: " + source);
      monitor.worked(1);
      if (monitor.isCanceled()) {
        return false;
      }

      /* Creates the URI where the resource is saved */
      final URI uri = URI.createURI(destination.getLocationURI().toString());

      monitor.subTask("Dumping Model");

      final Resource uml2Resource = Helper.RESOURCE_SET.createResource(uri);
      /* First iteration through the tree */
      dumpModel(new SubProgressMonitor(monitor, 4), eaRepository, uml2Resource);
      // monitor.worked(4);
      if (monitor.isCanceled()) {
        return false;
      }

      /* Load the profile */
      monitor.subTask("Loading U2TP Profile");
      URI u2tpProfileURI = URI
          .createURI("platform:/plugin/com.testingtech.ttworkbench.utp.core/model/u2tp.profile.uml");
      if (u2tpProfileURI == null) {
        System.err.println("problems getting U2TP profile URI.");
        return false;
      }
      u2tp = Helper.loadProfile(u2tpProfileURI);

      monitor.worked(1);

      if (monitor.isCanceled()) {
        return false;
      }

      monitor.subTask("Completing Model");

      /* Second iteration through the tree */
      completeModelsOfResource(eaRepository);
      saveModels(uml2Resource);

      monitor.worked(1);

      if (monitor.isCanceled()) {
        return false;
      }

      /* Closes the file */
      eaRepository.closeFile();
      eaRepository.exit();
    }
    catch (final JComException e) {
      // TODO better error handling
      e.printStackTrace();
    }
    catch (final Throwable e) {
      // TODO better error handling
      e.printStackTrace();
    }
    finally {
      rm.release();
    }
    long duration = (System.currentTimeMillis() - startTime);
    // DEBUG
    System.out.println("transformation took " + duration / 1000.0 + " seconds");

    return true;
  }

  /**
   * Saves the models created
   */
  public void saveModels(final URI uri) {

    final Resource uml2Resource = Helper.RESOURCE_SET.createResource(uri);
    for (int i = 0; i < uml2Models.size(); i++) {
      Helper.save(uml2Models.get(i), uml2Resource);
    }
  }

  /**
   * Saves the models created
   */
  public void saveModels(final Resource uml2Resource) {

    for (int i = 0; i < uml2Models.size(); i++) {
      Helper.save(uml2Models.get(i), uml2Resource);
    }
  }

  /**
   * Creates elements of different types.
   */
  public void dumpElement(final IProgressMonitor monitor,
      final EAElement eaElement, final Package uml2Package) {

    final Element uml2Element = UMLElementFactory.createElement(uml2Package,
        eaElement);

    monitor.beginTask("Dumping UMLElementFactory", 1);

    if (uml2Element != null) {
      /* Gets the Local Identifier for the element and transforms it to a key */
      /* Puts the element in the global table */
      elementsCache.putUMLElement(eaElement, uml2Element);
    }
    monitor.worked(1);
  }

  /**
   * Sets the visibility for the package and creates (dumps) the contained
   * classes and packages.
   */
  public void dumpPackage(final IProgressMonitor monitor,
      final EAPackage eaPackage, final Package uml2Package) {

    /* Iterates through the contained elements */
    final EACollection eaElements = eaPackage.getElements();
    final int countElements = eaElements.getCount();

    monitor.beginTask("Dumping EAPackage", 2);

    SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);
    subMonitor.beginTask("Duming Elements", countElements);

    for (int i = 0; i < countElements; i++) {

      final EAElement eaElement = EAElement.getInstance(eaElements.getAt(i));

      dumpElement(new SubProgressMonitor(subMonitor, 1), eaElement, uml2Package);
      subMonitor.worked(1);
    }
    monitor.worked(1);

    /* Iterates through the contained packages */
    final EACollection eaPackages = eaPackage.getPackages();
    final int countPackages = eaPackages.getCount();

    subMonitor = new SubProgressMonitor(monitor, 1);
    subMonitor.beginTask("Dumping Packages", countPackages);

    for (int i = 0; i < countPackages; i++) {

      final EAPackage eaSubPackage = new EAPackage(eaPackages.getAt(i));

      final Package uml2SubPackage = UMLElementFactory.createPackage(
          uml2Package, eaSubPackage);

      dumpPackage(subMonitor, eaSubPackage, uml2SubPackage);
      subMonitor.worked(1);
    }

    /* Gets the Global Identifier for the element and transforms it to a key */
    /* Puts the element in the global table */
    elementsCache.putUMLPackage(eaPackage, uml2Package);
    monitor.worked(1);
  }

  /**
   * Starts at the model level. Dumps all the models from the repository.
   */
  public void dumpModel(final IProgressMonitor monitor,
      final EARepository eaRepository, final Resource uml2Resource) {

    final EACollection eaModels = eaRepository.getModels();
    if (eaModels == null) {
      System.err.println("Transformer#dumpModel(): no models created!");
      return;
    }

    final int count = eaModels.getCount();

    monitor.beginTask("Dumping Models", count + 1);

    for (int i = 0; i < count; i++) {

      final EAPackage eaModel = new EAPackage(eaModels.getAt(i));

      final Package uml2Model = ModelCreator.createModel(eaModel.getName());

      dumpPackage(new SubProgressMonitor(monitor, 1), eaModel, uml2Model);

      /* Add the model to the list of models */
      uml2Models.add(uml2Model);
      monitor.worked(1);
    }

    saveModels(uml2Resource);
    monitor.worked(1);
  }

  /**
   * Applies to a package the appropriate stereotype.
   */
  public void applyStereotypeToPackage(final EAPackage eaPackage) {

    /* IsModel */
    if (!eaPackage.getIsModel()) {

      /* Apply the appropriate stereotype to this package */
      final String stereotype = (eaPackage.getElement()).getStereotype();
      final Element uml2Package = elementsCache.getUMLPackage(eaPackage);

      if ((uml2Package != null) && U2TPHelper.isFromU2TP(stereotype)) {
        U2TPHelper.applyStereotype((NamedElement) uml2Package, u2tp
            .getOwnedStereotype(stereotype));
      }
    }
  }

  // TODO currently nuot used, do we need it?
  /**
   * Applies the appropriate attributes to the elements.
   */
  private void applyAttributesToElement(final EAElement eaElement,
      final NamedElement uml2Element) {

    final String type = eaElement.getType();
    final String stereotype = eaElement.getStereotype();

    final EACollection eaAttributes = eaElement.getAttributes();
    final int count = eaAttributes.getCount();

    /* Iterates through the specific attributes */
    for (int i = 0; i < count; i++) {

      final EAAttribute eaAttribute = new EAAttribute(eaAttributes.getAt(i));

      if (type.equals(EAElement.TYPE_CLASS)
          && !stereotype.equals(EAElement.STEREOTYPE_ENUMERATION)) {

        final Property uml2Attribute = UMLElementFeaturesFactory
            .createAttribute((Class) uml2Element, eaAttribute, elementsCache);
        Helper.out("Transformer: created uml2Attribute: "
                   + uml2Attribute.getName());

      }
      else if (type.equals(EAElement.TYPE_INTERFACE)) {

        final Property uml2Attribute = UMLElementFeaturesFactory
            .createInterfaceAttribute((Interface) uml2Element, eaAttribute,
                elementsCache);

        Helper.out("Transformer: created uml2Attribute: "
                   + uml2Attribute.getName());
      }
    }
  }

  /**
   * Applies the appropriate operations/methods to the elements.
   */
  private void applyOperationsToElement(final EAElement eaElement,
      final NamedElement uml2Element) {

    final String type = eaElement.getType();
    final String stereotype = eaElement.getStereotype();

    final EACollection eaMethods = eaElement.getMethods();
    final int count = eaMethods.getCount();

    /* Iterates through the specific attributes */
    for (int i = 0; i < count; i++) {

      final Method eaMethod = new Method(eaMethods.getAt(i));
      final String methodStereotype = eaMethod.getStereotype();
      Operation uml2Operation = null;

      if (type.equals(EAElement.TYPE_CLASS)
          && !stereotype.equals(EAElement.STEREOTYPE_ENUMERATION)) {

        if (uml2Element instanceof Class) {
          uml2Operation = UMLElementFeaturesFactory.createOperation(
              (Class) uml2Element, eaMethod, elementsCache);
        }
        else {
          Helper.err("applyOperationsToElement: UML element "
                     + uml2Element.getName()
                     + " supposed to be a Class! It's a "
                     + uml2Element.getClass().getName());
        }

      }
      else if (type.equals(EAElement.TYPE_INTERFACE)) {

        if (uml2Element instanceof Interface) {
          uml2Operation = UMLElementFeaturesFactory.createInterfaceOperation(
              (Interface) uml2Element, eaMethod, elementsCache);
        }
        else {
          Helper.err("applyOperationsToElement: UML element "
                     + uml2Element.getName()
                     + " supposed to be an Interface! It's a "
                     + uml2Element.getClass().getName());
        }
      }

      /* Apply a stereotype to an operation */
      if ((uml2Operation != null) && U2TPHelper.isFromU2TP(methodStereotype)) {
        // Apply_U2TP.applyStereotype(uml2Operation,
        // u2tp.getOwnedStereotype(stereotype));
      }

      // TODO check if we need the created UML operation
    }
  }

  /**
   * Applies the appropriate stereotypes to the elements.
   */
  public void completeElement(final EAElement eaElement,
      final NamedElement uml2Element) {

    // DEBUG
    if (uml2Element == null) {
      Helper.err("completing element: UML element is null for EA element: "
                 + eaElement.getName());
    }

    final String stereotype = eaElement.getStereotype();
    if ((uml2Element != null) && U2TPHelper.isFromU2TP(stereotype)) {
      U2TPHelper.applyStereotype(uml2Element, u2tp
          .getOwnedStereotype(stereotype));
    }

    // TODO do we still need applying of attributes?
    // applyAttributesToElement(eaElement, uml2Element);

    applyOperationsToElement(eaElement, uml2Element);
    applyRealizationsToElement(eaElement, uml2Element);

    // // TODO process any other connectors (purpose??)
    // printCollection(eaElement.getConnectors(), "connectors: ");

    applyGeneralizationsToElement(eaElement, uml2Element);
    applyExposedInterfacesToElement(eaElement, uml2Element);

    applyOwnedPortsToElement(eaElement, uml2Element);
  }

  private void applyExposedInterfacesToElement(EAElement eaElement,
      final NamedElement uml2Element) {

    if (uml2Element == null) {
      Helper.err("given uml2element must not be null");
      return;
    }
    EACollection elements = eaElement.getEmbeddedElements();
    for (EAElement element : elements) {
      if (EAElement.TYPE_PROVIDED_INTERFACE.equals(element.getType())) {

        // we expect to have the interface UML object already in our
        // repository
        Element providedInterface = elementsCache.getUMLElement(element
            .getClassifierID());

        if (providedInterface != null) {
          uml2Element.createDependency((NamedElement) providedInterface);
        }
        else {
          Helper.err("no corresponding uml element for "
                     + element.getClassifierType());
        }
      }

    }
  }

  private void applyOwnedPortsToElement(EAElement eaElement,
      final NamedElement uml2Element) {

    if (!eaElement.getType().equals(EAElement.TYPE_CLASS)) {
      return;
    }
    EACollection elements = eaElement.getEmbeddedElements();
    for (EAElement port : elements) {
      if (EAElement.TYPE_PORT.equals(port.getType())) {

        List<NamedElement> required = new ArrayList<NamedElement>();
        List<NamedElement> provided = new ArrayList<NamedElement>();

        EACollection elementsOfPort = port.getEmbeddedElements();
        for (EAElement portElement : elementsOfPort) {
          String portElementType = portElement.getType();
          if (EAElement.TYPE_PROVIDED_INTERFACE.equals(portElementType)) {
            provided.add((NamedElement) elementsCache.getUMLElement(portElement
                .getClassifierID()));
          }
          if (EAElement.TYPE_REQUIRED_INTERFACE.equals(portElementType)) {
            required.add((NamedElement) elementsCache.getUMLElement(portElement
                .getClassifierID()));
          }
        }
        // TODO implement "required interfaces"
        if (required.size() > 0) {
          System.out.print("required interfaces: ");
          for (NamedElement element2 : required) {
            System.out.print(element2.getName() + " ");
          }
          System.out.println();
        }

        // apply collected information
        if (uml2Element instanceof Class) {
          Port createdUMLPort = ((Class) uml2Element).createOwnedPort(port
              .getName(), (Type) null);
          for (NamedElement provElement : provided) {
            createdUMLPort.createDependency(provElement);
          }
        }
        else {
          Helper.err("applyOwnedPortsToElement: UML element "
                     + uml2Element.getName() + " is not of type Class! It's a "
                     + uml2Element.getClass().getName());
        }
      }

    }
  }

  private void applyGeneralizationsToElement(final EAElement eaElement,
      final NamedElement uml2Element) {

    EACollection baseClasses = eaElement.getBaseClasses();
    for (EAElement baseClass : baseClasses) {
      if (baseClass != null) {
        final NamedElement uml2BaseClassElement = (NamedElement) elementsCache
            .getUMLElement(baseClass.getElementID());
        if (uml2Element instanceof Class
            && uml2BaseClassElement instanceof Classifier) {
          ((Class) uml2Element)
              .createGeneralization((Classifier) uml2BaseClassElement);
        }
      }
      else {
        Helper.err("base class is null???");
      }
    }
  }

  private void applyRealizationsToElement(final EAElement eaElement,
      final NamedElement uml2Element) {

    EACollection realizedInterfaces = eaElement.getRealizes();
    for (EAElement realizedInterface : realizedInterfaces) {
      if (realizedInterface != null) {
        final NamedElement uml2InterfaceElement = (NamedElement) elementsCache
            .getUMLElement(realizedInterface.getElementID());
        if (uml2Element instanceof Classifier
            && uml2InterfaceElement instanceof Interface) {
          ((Class) uml2Element).createInterfaceRealization((String) null,
              (Interface) uml2InterfaceElement);
        }
      }
      else {
        Helper.err("realized interface is null??");
      }
    }
  }

  void printCollection(EACollection aCollection, String info) {

    System.out.print(info);
    for (int i = 0; i < aCollection.getCount(); i++) {
      final EAElement elem = EAElement.getInstance(aCollection.getAt(i));
      if (i > 0) {
        System.out.print(", ");
      }
      System.out.print(elem.getName() + " (" + elem.getType() + ")");
    }
    System.out.println();
  }

  /**
   * Applies the u2tp iteratively in the tree.
   */
  public void completePackage(final EAPackage eaPackage) {

    /* Iterates through the contained elements */
    final EACollection eaElements = eaPackage.getElements();
    final int countElements = eaElements.getCount();
    for (int i = 0; i < countElements; i++) {
      final EAElement eaElement = EAElement.getInstance(eaElements.getAt(i));

      /* Retrieve the element from the dictionary */
      final NamedElement uml2Element = (NamedElement) elementsCache
          .getUMLElement(eaElement.getElementID());

      /*
       * Apply the stereotype from the profile to the element - if it is the
       * case
       */
      completeElement(eaElement, uml2Element);
    }

    /* Iterates through the contained packages */
    final EACollection eaPackages = eaPackage.getPackages();
    final int countPackages = eaPackages.getCount();
    for (int i = 0; i < countPackages; i++) {
      completePackage(new EAPackage(eaPackages.getAt(i)));
    }

    applyStereotypeToPackage(eaPackage);
  }

  /**
   * Applies the u2tp profile to the models and completes all packages
   */
  public void completeModelsOfResource(final EARepository eaRepository) {

    final EACollection eaModels = eaRepository.getModels();
    final int countModels = eaModels.getCount();
    for (int i = 0; i < countModels; i++) {
      U2TPHelper.applyProfile(uml2Models.get(i), u2tp);
      EAPackage modelPackage = new EAPackage(eaModels.getAt(i));
      final Package uml2Package = elementsCache.getUMLPackage(modelPackage);
      elementsCache.createPrimitiveImports(uml2Package);
      completePackage(modelPackage);
    }
  }

}
