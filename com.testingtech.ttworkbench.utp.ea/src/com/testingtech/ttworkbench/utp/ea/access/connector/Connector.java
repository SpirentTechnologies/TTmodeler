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
package com.testingtech.ttworkbench.utp.ea.access.connector;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;

import org.apache.log4j.Logger;

import com.testingtech.ttworkbench.utp.ea.access.AbstractEADispatch;
import com.testingtech.ttworkbench.utp.ea.access.repository.EACollection;

/**
 * A Connector represents the various kinds of Connections between UML Elements.
 * It is accessed from either the Client or Supplier Element - using the
 * Connectors collection of that element. When creating a new connector you must
 * assign it a valid type. These are: 
 * 
 * 		-	Aggregation 
 * 		- Association 
 * 		-	Collaboration
 * 		-	Dependency
 * 		-	Generalization
 * 		-	Instantiation
 * 		-	Nesting
 * 		-	NoteLink
 * 		-	Realization
 * 		-	Sequence
 * 		-	StateFlow
 * 		-	UseCase
 */
public class Connector extends AbstractEADispatch {

  public Connector(IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(Connector.class);
  }

  /**
   * Read/Write. UMLConnectorFactory Direction. May be set to one of the
   * following: 1. Unspecified, 2. Bi-Directional, 3. Source -> Destination, 4.
   * Destination -> Source.
   */
  public String getDirection() {

    try {
      return (String) this.get("Direction");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setDirection(String direction) {

    try {
      this.put("Direction", direction);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read only. Local identifier for the current connector. System generated.
   */
  public Double getConnectorID() {

    try {
      return (Double) this.get("ConnectorID");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. EACollection of EAConstraint objects.
   */
  public EACollection getConstraints() {

    try {
      return (new EACollection((IDispatch) this.get("Constraints")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. EACollection of EATaggedValue objects.
   */
  public EACollection getTaggedValues() {

    try {
      return (new EACollection((IDispatch) this.get("TaggedValues")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. Returns a collection of Advanced properties associated with an
   * element in the form of CustomProperty objects.
   */
  public EACollection getCustomProperties() {

    try {
      return (new EACollection((IDispatch) this.get("CustomProperties")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. A pointer to the a ConnectorEnd object that represents the
   * supplier end of the relationship.
   */
  public ConnectorEnd getSupplierEnd() {

    try {
      return (new ConnectorEnd((IDispatch) this.get("SupplierEnd")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. A pointer to the a ConnectorEnd object that represents the
   * client end of the relationship.
   */
  public ConnectorEnd getClientEnd() {

    try {
      return (new ConnectorEnd((IDispatch) this.get("ClientEnd")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read/Write. Descriptive notes about connection.
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

  /**
   * Read/Write. A possible subtype to refine the meaning of the connection.
   */
  public String getSubtype() {

    try {
      return (String) this.get("Subtype");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setSubtype(String subtype) {

    try {
      this.put("Subtype", subtype);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Sets or gets the EAStereotype for this connector end.
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
}
