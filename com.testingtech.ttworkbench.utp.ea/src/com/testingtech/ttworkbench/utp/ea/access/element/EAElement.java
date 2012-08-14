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
package com.testingtech.ttworkbench.utp.ea.access.element;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;

import org.apache.log4j.Logger;

import com.testingtech.ttworkbench.utp.ea.access.AbstractEADispatch;
import com.testingtech.ttworkbench.utp.ea.access.repository.EACollection;

/**
 * An UMLElementFactory is the main modeling
 * unit. It corresponds to Class, Use Case, Node, Component & etc. You
 * create new Elements by adding to the EAPackage Elements collection.
 * Once you have created an element, you can add it to the
 * DiagramObjects collection of a diagram to include it in a diagram.
 */
public class EAElement extends AbstractEADispatch {

  public static final String TYPE_PROVIDED_INTERFACE = "ProvidedInterface";

  public static final String STEREOTYPE_ENUMERATION = "enumeration";

  public static final String TYPE_INTERFACE = "Interface";

  public static final String TYPE_CLASS = "Class";

  public static final String TYPE_PORT = "Port";

  public static final String TYPE_REQUIRED_INTERFACE = "RequiredInterface";

  private EAElement(final IDispatch disp) {

    super(new IDispatch(disp));
    logger = Logger.getLogger(EAElement.class);
    // BasicConfigurator.configure();
  }

  public static EAElement getInstance(final IDispatch disp) {

    return new EAElement(disp);
  }

  /**
   * Read only. A globally unique ID for this UMLElementFactory - unique across
   * all model files. If you need to set this value manually, you should only do
   * so when the element is first created - and make sure you format the GUID
   * exactly as EA expects.
   */
  public Object getElementGUID() {

    try {
      return get("ElementGUID");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * ElementID is a read-only attribute. The local ID of the UMLElementFactory.
   * Valid for this file only.
   */
  public Integer getElementID() {

    try {
      return (Integer) get("ElementID");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Can be used to set or retrieve the If this element is a child of another,
   * the ElementID of the other element. If not, returns 0.
   */
  public Integer getParentID() {

    try {
      return (Integer) get("ParentID");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setParentID(final Integer id) {

    try {
      put("ParentID", id);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * PackageID is a read-write attribute. A local ID for the package containing
   * this element.
   */
  public Integer getPackageID() {

    try {
      return (Integer) get("PackageID");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setPackageID(final Integer id) {

    try {
      put("PackageID", id);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * ClassifierID is aread-write attribute. Local ID of a Classifier associated
   * with this UMLElementFactory - that is the base type. Only valid for
   * instance type elements (eg. Object).
   */
  public Integer getClassifierID() {

    try {
      return (Integer) get("ClassifierID");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setClassifierID(final Integer cid) {

    try {
      this.put("ClassifierID", cid);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Sets or gets the tree position.
   */
  public Double getTreePos() {

    try {
      return (Double) this.get("TreePos");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setTreePos(final Double pos) {

    try {
      this.put("TreePos", pos);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * ClassifierName is a read-write attribute.
   */
  public String getClassifierName() {

    try {
      return (String) this.get("ClassifierName");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setClassifierName(final String cname) {

    try {
      this.put("ClassifierName", cname);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * ClassifierType is a read-only attribute.
   */
  public String getClassifierType() {

    try {
      return (String) this.get("ClassifierType");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Subtype is a read-write attribute. A numeric subtype which varies the type
   * of the main element: � For Event type 0 = Receiver, 1 = Sender � For
   * Class type, 1 = Template class, 2 = Parameterised Template Class � For
   * Note type, 1= Note linked to connector, 2 = EAConstraint linked to
   * connector � For StateNode, 100 = ActivityIntitial, 101 = ActivityFinal
   * � For Activity type 0=Activity, 8 = SubActivity
   */
  public Integer getSubtype() {

    try {
      return (Integer) this.get("Subtype");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setSubtype(final Double subtype) {

    try {
      this.put("Subtype", subtype);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Boolean value indicating element is leaf node or not. 1 = true,
   * 0 = false. yields null upon error.
   */
  public boolean getIsLeaf() {

    try {
      final Boolean isLeaf = (Boolean) get("IsLeaf");
      return isLeaf == null || isLeaf.booleanValue();
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return false;
    }
  }

  public void setIsLeaf(final Integer isLeaf) {

    try {
      this.put("IsLeaf", isLeaf);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * EAStereotype is a read-write attribute.
   */
  public String getStereotype() {

    try {
      return (String) get("Stereotype");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setStereotype(final String stereotype) {

    try {
      put("Stereotype", stereotype);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Returns all the applied stereotypes of the element in a
   * comma-separated list
   */
  public String getStereotypeEx() {

    try {
      return (String) this.get("StereotypeEx");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setStereotypeEx(final String stereotype) {

    try {
      this.put("StereotypeEx", stereotype);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * ExtensionPoints is a read-write attribute. Optional extension points for a
   * Use Case as a comma-separated list.
   */
  public String getExtensionPoints() {

    try {
      return (String) this.get("ExtensionPoints");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setExtensionPoints(final String ep) {

    try {
      this.put("ExtensionPoints", ep);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Visibility is a read-write attribute. The Scope of this element within the
   * current package. Valid values are: Public, Private, Protected or EAPackage.
   */
  public String getVisibility() {

    try {
      final String visibility = (String) this.get("Visibility");
      if (visibility == null) {
        return "Public";
      }
      else {
        return visibility;
      }
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setVisibility(final String visibility) {

    try {
      this.put("Visibility", visibility);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Multiplicity is a read-write attribute.
   */
  public String getMultiplicity() {

    try {
      return (String) this.get("Multiplicity");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setMultiplicity(final String mul) {

    try {
      this.put("Multiplicity", mul);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Abstract is a read-write attribute. Indicates if UMLElementFactory is
   * Abstract (1) or Concrete (0)
   */
  public boolean getAbstract() {

    try {
      final String isAbstract = (String) get("Abstract");

      return !(isAbstract == null || isAbstract.equals("0"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return true;
    }
  }

  public void setAbstract(final String abs) {

    try {
      this.put("Abstract", abs);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Attributes is a read-only attribute. EACollection of Attribute objects for
   * current element.
   */
  public EACollection getAttributes() {

    try {
      return new EACollection((IDispatch) this.get("Attributes"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * AttributesEx is a read-only attribute. EACollection of Attribute objects
   * for current element and its parent elements.
   */
  public EACollection getAttributesEx() {

    try {
      return new EACollection((IDispatch) this.get("AttributesEx"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Methods is a read-only attribute. EACollection of Method objects for
   * current element.
   */
  public EACollection getMethods() {

    try {
      return new EACollection((IDispatch) this.get("Methods"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * MethodsEx is a read-only attribute. EACollection of Method objects for
   * current element and its parent elements.
   */
  public EACollection getMethodsEx() {

    try {
      return new EACollection((IDispatch) this.get("MethodsEx"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * TaggedValues is a read-only attribute. EACollection of EATaggedValue
   * objects for current element.
   */
  public EACollection getTaggedValues() {

    try {
      return new EACollection((IDispatch) this.get("TaggedValues"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * TaggedValuesEx is a read-only attribute. EACollection of EATaggedValue
   * objects for current element and its parent elements.
   */
  public EACollection getTaggedValuesEx() {

    try {
      return new EACollection((IDispatch) this.get("TaggedValuesEx"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Constraints is a read-only attribute. EACollection of EAConstraint objects
   * for current element.
   */
  public EACollection getConstraints() {

    try {
      return new EACollection((IDispatch) this.get("Constraints"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * ConstraintsEx is a read-only attribute. EACollection of EAConstraint
   * objects for current element and its parent elements.
   */
  public EACollection getConstraintsEx() {

    try {
      return new EACollection((IDispatch) get("ConstraintsEx"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Scenarios is a read-only attribute. EACollection of EAScenario objects for
   * current element.
   */
  public EACollection getScenarios() {

    try {
      return new EACollection((IDispatch) get("Scenarios"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * The child elements of this element.
   */
  public EACollection getElements() {

    try {
      return new EACollection((IDispatch) get("Elements"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Returns a collection containing the connectors to other elements.
   */
  public EACollection getConnectors() {

    try {
      return new EACollection((IDispatch) get("Connectors"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * The child diagrams of this element.
   */
  public EACollection getDiagrams() {

    try {
      return new EACollection((IDispatch) get("Diagrams"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * List of logical partitions into which an element may be divided. Only valid
   * for elements which support partitions, such as Activities and States.
   */
  public EACollection getPartitions() {

    try {
      return new EACollection((IDispatch) get("Partitions"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * List of "Advanced Properties" for an element. The collection of advanced
   * properties will change depending on element type � for example an Action
   * and an Activity have different Advanced Properties.
   */
  public EACollection getCustomProperties() {

    try {
      return new EACollection((IDispatch) get("CustomProperties"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * List of State Transitions that an element may support. Applies in
   * particular to Timing Elements.
   */
  public EACollection getStateTransitions() {

    try {
      return new EACollection((IDispatch) get("StateTransitions"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * List of elements which are embedded into this element. Includes Ports,
   * Parts, Pins, Parameter Sets & etc.
   */
  public EACollection getEmbeddedElements() {

    try {
      return new EACollection((IDispatch) get("EmbeddedElements"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * List of Base Classes for this element presented as a collection for
   * convenience.
   */
  public EACollection getBaseClasses() {

    try {
      return new EACollection((IDispatch) get("BaseClasses"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * List of Interfaces realized by this element for convenience.
   */
  public EACollection getRealizes() {

    try {
      return new EACollection((IDispatch) get("Realizes"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Notes is a read-write attribute.
   */
  public String getNotes() {

    try {
      return (String) this.get("Notes");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setNotes(final String notes) {

    try {
      this.put("Notes", notes);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Refresh the UMLElementFactory features in the Project Browser tree.
   */
  public void refresh() {

    try {
      this.method("Refresh", null);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }
}
