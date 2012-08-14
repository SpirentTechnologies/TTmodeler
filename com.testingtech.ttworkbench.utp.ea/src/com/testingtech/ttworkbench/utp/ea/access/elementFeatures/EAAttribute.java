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
package com.testingtech.ttworkbench.utp.ea.access.elementFeatures;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;

import org.apache.log4j.Logger;

import com.testingtech.ttworkbench.utp.ea.access.AbstractEADispatch;
import com.testingtech.ttworkbench.utp.ea.access.repository.EACollection;

/**
 * An Attribute corresponds to a UML Attribute.
 * It contains further collections for constraints and tagged values.
 * Attributes are accessed from the UMLElementFactory Attributes collection.
 */
public class EAAttribute extends AbstractEADispatch {

  public EAAttribute(final IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(EAAttribute.class);
    // BasicConfigurator.configure();
  }

  /**
   * Read/Write. A value for the collection lower bound.
   */
  public String getLowerBound() {

    try {
      final String lb = (String) this.get("LowerBound");

      if (lb == null) {
        return "1";
      }
      return lb;
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setLowerBound(final String lb) {

    try {
      this.put("LowerBound", lb);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. A value for the collection upper bound.
   */
  public String getUpperBound() {

    try {
      final String ub = (String) this.get("UpperBound");

      if (ub == null) {
        return "1";
      }
      else {
        return ub;
      }
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setUpperBound(final String ub) {

    try {
      this.put("UpperBound", ub);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Default value associated with attribute.
   */
  public String getDefault() {

    try {
      return (String) this.get("Default");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setDefault(final String def) {

    try {
      this.put("Default", def);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Sets or gets the EAStereotype for this attribute.
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
   * Visibility is a read-write attribute. The Scope of this attribute current
   * package. Valid values are: Public, Private, Protected or EAPackage.
   */
  public String getVisibility() {

    try {
      return (String) this.get("Visibility");
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
   * Read/Write. Indicates if the current attribute is a static feature or not.
   */
  public Boolean getIsStatic() {

    try {
      return (Boolean) this.get("IsStatic");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setIsStatic(final Boolean isstatic) {

    try {
      this.put("IsStatic", isstatic);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Flag indicating if Attribute is Const or not.
   */
  public Boolean getIsConst() {

    try {
      return (Boolean) this.get("IsConst");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setIsConst(final Boolean isconst) {

    try {
      this.put("IsConst", isconst);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Indicates if the current feature is a collection or not.
   */
  public Boolean getIsCollection() {

    try {
      return (Boolean) this.get("IsCollection");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setIsCollection(final Boolean iscollection) {

    try {
      this.put("IsCollection", iscollection);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The container type.
   */
  public String getContainer() {

    try {
      return (String) this.get("Container");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setContainer(final String container) {

    try {
      this.put("Container", container);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
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
   * Read only. Local ID number of Attribute.
   */
  public Double getAttributeID() {

    try {
      return (Double) this.get("AttributeID");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. Returns the ElementID of the UMLElementFactory that this
   * attribute is a part of.
   */
  public Double getParentID() {

    try {
      return (Double) this.get("ParentID");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read/Write. Classifier ID if appropriate - indicates base type associated
   * with attribute if not a primitive type.
   */
  public Integer getClassifierID() {

    try {
      return Integer.valueOf(get("ClassifierID").toString());
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setClassifierID(final Integer id) {

    try {
      this.put("ClassifierID", id);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Attribute length where applicable.
   */
  public String getLength() {

    try {
      return (String) this.get("Length");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setLength(final String length) {

    try {
      this.put("Length", length);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read only. A collection of AttributeConstraints. Used to access and manage
   * constraints associated with this Attribute.
   */
  public EACollection getConstraints() {

    try {
      return (new EACollection((IDispatch) this.get("Constraints")));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. A collection of AttributeTags.
   */
  public EACollection getTaggedValues() {

    try {
      return (new EACollection((IDispatch) this.get("TaggedValues")));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. EACollection of EATaggedValue objects belonging to the current
   * Attribute and its parent attributes.
   */
  public EACollection getTaggedValuesEx() {

    try {
      return (new EACollection((IDispatch) this.get("TaggedValuesEx")));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }
}
