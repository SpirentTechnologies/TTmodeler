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
package com.testingtech.ttworkbench.utp.ea.access.repository;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;

import org.apache.log4j.Logger;

import com.testingtech.ttworkbench.utp.ea.access.AbstractEADispatch;
import com.testingtech.ttworkbench.utp.ea.access.element.EAElement;

/**
 * A EAPackage object corresponds to a package element in the EA Project Browser. 
 * It is accessed either through the EARepository Models collection 
 * (a Model is a special form of EAPackage) or through the EAPackage Packages collection.
 */
public class EAPackage extends AbstractEADispatch {

  public EAPackage(IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(EAPackage.class);
    // BasicConfigurator.configure();
  }

  /**
   * UMLElementFactory is a read-only attribute.The associated UMLElementFactory
   * object. Use to set UMLElementFactory type information for a package.
   */
  public EAElement getElement() {

    try {
      return EAElement.getInstance((IDispatch) get("Element"));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * PackageID is a read-only attribute. The local EAPackage ID number. Valid
   * only in this model file.
   */
  public Integer getPackageID() {

    try {
      return (Integer) get("PackageID");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * ParentID is a read-write attribute. The ID of the EAPackage that is the
   * Parent of this one. 0 indicates this package is a Model (ie. it has no
   * parent).
   */
  public Integer getParentID() {

    try {
      return (Integer) this.get("ParentID");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setParentID(Integer id) {

    try {
      this.put("ParentID", id);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * TreePos is a read-write attribute.The relative position in the tree
   * compared to other packages (use to sort packages).
   */
  public Double getTreePos() {

    try {
      return (Double) this.get("TreePos");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setTreePos(Double pos) {

    try {
      this.put("TreePos", pos);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Packages is a read-only attribute.
   */
  public EACollection getPackages() {

    try {
      return (new EACollection((IDispatch) this.get("Packages")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Elements is a read-only attribute.
   */
  public EACollection getElements() {

    try {
      return (new EACollection((IDispatch) this.get("Elements")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Connectors is a read-only attribute.
   */
  public EACollection getConnectors() {

    try {
      return (new EACollection((IDispatch) this.get("Connectors")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Diagrams is a read-only attribute.
   */
  public EACollection getDiagrams() {

    try {
      return (new EACollection((IDispatch) this.get("Diagrams")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * IsProtected is a read-write attribute.
   */
  public Boolean getIsProtected() {

    try {
      return (Boolean) this.get("IsProtected");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setIsProtected(Boolean ip) {

    try {
      this.put("IsProtected", ip);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * IsModel is a read-only attribute. Indicates if package is a Model or
   * EAPackage. TODO find a way to let the caller check that there was no
   * JComException
   */
  public boolean getIsModel() {

    try {
      return ((Boolean) get("IsModel")).booleanValue();
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return false;
    }
  }

  /**
   * Notes is a read-write attribute. Note about this package.
   */
  public String getNotes() {

    try {
      return (String) this.get("Notes");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setNotes(String notes) {

    try {
      this.put("Notes", notes);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }
}