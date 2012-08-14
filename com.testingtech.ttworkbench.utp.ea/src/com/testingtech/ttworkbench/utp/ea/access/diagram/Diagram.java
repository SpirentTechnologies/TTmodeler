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
package com.testingtech.ttworkbench.utp.ea.access.diagram;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;

import org.apache.log4j.Logger;

import com.testingtech.ttworkbench.utp.ea.access.AbstractEADispatch;
import com.testingtech.ttworkbench.utp.ea.access.repository.EACollection;

/**
 * A Diagram corresponds to a single EA diagram.
 * It is accessed through the EAPackage Diagrams collection and in turn
 * contains a collection of diagram objects and diagram links. Adding to
 * the DiagramObjects collection will add an element to the Diagram (the
 * UMLElementFactory must already exist). When adding a new diagram, you
 * must set the diagram type to a valid type - these are:
 * 
 * 		-	Activity
 *    -	Analysis
 *    -	Component
 *    -	Custom
 *    -	Deployment
 *    -	Logical
 *    -	Sequence
 *    -	Statechart
 *    -	Use Case
 */
public class Diagram extends AbstractEADispatch {

  public Diagram(IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(Diagram.class);
  }

  /**
   * Read only. A local ID for the diagram.
   */
  public Double getDiagramID() {

    try {
      return (Double) this.get("DiagramID");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read/Write. An ID of the package that this diagram belongs to.
   */
  public Double getPackageID() {

    try {
      return (Double) this.get("PackageID");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setPackageID(Double id) {

    try {
      this.put("PackageID", id);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. An optional ID of an element that 'owns' this diagram - eg. a
   * Sequence diagram owned by a Use Case.
   */
  public Double getParentID() {

    try {
      return (Double) this.get("ParentID");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setParentID(Double id) {

    try {
      this.put("ParentID", id);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Sets or gets the stereotype for this diagram.
   */
  public String getStereotype() {

    try {
      return (String) get("Stereotype");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setStereotype(String stereotype) {

    try {
      put("Stereotype", stereotype);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Page orientation - use "P" or "L" for Portrait or Landscape
   * respectively.
   */
  public String getOrientation() {

    try {
      return (String) this.get("Orientation");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setOrientation(String orientation) {

    try {
      this.put("Orientation", orientation);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The X dimension of the diagram (800 is default).
   */
  public Double getCx() {

    try {
      return (Double) this.get("cx");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setCx(Double cx) {

    try {
      this.put("cx", cx);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The Y dimension of diagram (1100 is default).
   */
  public Double getCy() {

    try {
      return (Double) this.get("cy");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setCy(Double cy) {

    try {
      this.put("cy", cy);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The zoom scale - 100 is default.
   */
  public Double getScale() {

    try {
      return (Double) this.get("Scale");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setScale(Double scale) {

    try {
      this.put("Scale", scale);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read only. A collection of references to DiagramObjects. A DiagramObject is
   * an instance of an UMLElementFactory in a Diagram - and includes size and
   * display characteristics.
   */
  public EACollection getDiagramObjects() {

    try {
      return (new EACollection((IDispatch) this.get("DiagramObjects")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. A list of DiagramLink objects - each containing information
   * about the display characteristics of a connector in a diagram. Note: A
   * Diagram link is only created once a user modifies a connector in a diagram
   * in some way. Until this condition has been met default values are used and
   * the diagram link is not in use.
   */
  public EACollection getDiagramLinks() {

    try {
      return (new EACollection((IDispatch) this.get("DiagramLinks")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read/Write. Set/retrieve notes for this diagram.
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
