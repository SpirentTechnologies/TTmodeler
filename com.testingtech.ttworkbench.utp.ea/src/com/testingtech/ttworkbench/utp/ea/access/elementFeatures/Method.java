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
 * A Method represents a UML operation. It is accessed from the 
 * UMLElementFactory Methods collection and includes collections for parameters, 
 * constraints and tagged values.
 */
public class Method extends AbstractEADispatch {

  public Method(final IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(Method.class);
    // BasicConfigurator.configure();
  }

  /**
   * Read only. A local ID for the current method.
   */
  public Double getMethodID() {

    try {
      return (Double) this.get("MethodID");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Visibility is a read-write attribute. The Scope of this method. Valid
   * values are: Public, Private, Protected or EAPackage.
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
   * Read only. The Parameters collection for the current Method.
   */
  public EACollection getParameters() {

    try {
      return new EACollection((IDispatch) get("Parameters"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. PreConditions (constraints) as they apply to this method.
   * Returns a MethodConstraint object of type "pre".
   */
  public EACollection getPreConditions() {

    try {
      return new EACollection((IDispatch) get("PreConditions"));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. PostConditions (constraints) as they apply to this method.
   * Returns a MethodConstraint object of type "post".
   */
  public EACollection getPostConditions() {

    try {
      return (new EACollection((IDispatch) this.get("PostConditions")));
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. TaggedValues collection for the current method. Access a list of
   * MethodTag objects.
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
   * Read/Write. Return type for the method - may be a primitive data type or a
   * class or interface type.
   */
  public String getReturnType() {

    try {
      return (String) this.get("ReturnType");
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setReturnType(final String type) {

    try {
      this.put("ReturnType", type);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Flag to indicate return value is an array.
   */
  public boolean getReturnIsArray() {

    try {
      Boolean object = (Boolean) get("ReturnIsArray");
      return object != null ? object.booleanValue() : false;
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return false;
    }
  }

  public void setReturnIsArray(final Object isArray) {

    try {
      this.put("ReturnIsArray", isArray);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The method stereotype (optional).
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
   * Read/Write. Flag to indicate a static method.
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
   * Read/Write. Flag indicating method is Const.
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
   * Read/Write. ClassifierID that applies to the ReturnType.
   */
  public Integer getClassifierID() {

    try {

      Object object = get("ClassifierID");
      return Integer.valueOf(object.toString());
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setClassifierID(final String cid) {

    try {
      this.put("ClassifierID", cid);
    }
    catch (final JComException e) {
      logger.warn(e.getMessage());
    }
  }

}
