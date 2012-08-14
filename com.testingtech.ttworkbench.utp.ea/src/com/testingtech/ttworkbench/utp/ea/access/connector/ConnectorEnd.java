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
 * A ConnectorEnd contains information about a
 * single end of a ConnectorFactory. It may be a Supplier or a Client. A
 * ConnectorEnd is accessed from the ConnectorFactory as either the
 * ClientEnd or SupplierEnd.
 */
public class ConnectorEnd extends AbstractEADispatch {

  public ConnectorEnd(IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(ConnectorEnd.class);
  }

  /**
   * Read only collection.
   */
  public EACollection getTaggedvalues() {

    try {
      return (new EACollection((IDispatch) this.get("TaggedValues")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. The End this connectorEnd object applies to - Client or
   * Supplier.
   */
  public String getEnd() {

    try {
      return (String) this.get("End");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read/Write. Cardinality associated with this end.
   */
  public String getCardinality() {

    try {
      return (String) this.get("Cardinality");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setCardinality(String card) {

    try {
      this.put("Cardinality", card);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Scope associated with this connection end. Valid types are:
   * Public, Private, Protected and EAPackage.
   */
  public String getVisibility() {

    try {
      return (String) this.get("Visibility");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setVisibility(String visibility) {

    try {
      this.put("Visibility", visibility);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The connection end role.
   */
  public String getRole() {

    try {
      return (String) this.get("Role");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setRole(String role) {

    try {
      this.put("Role", role);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The Role type applied to this end of the connection.
   */
  public String getRoleType() {

    try {
      return (String) this.get("RoleType");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setRoleType(String rtype) {

    try {
      this.put("RoleType", rtype);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Notes associated with the role of this connection end.
   */
  public String getRoleNote() {

    try {
      return (String) this.get("RoleNote");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setRoleNote(String rnote) {

    try {
      this.put("RoleNote", rnote);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Containment type applied to this connection end.
   */
  public String getContainment() {

    try {
      return (String) this.get("Containment");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setContainment(String containment) {

    try {
      this.put("Containment", containment);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Aggregation as it applies to this end. Valid values are: 0 =
   * None, 1 = Shared, 2 = Composite.
   */
  public Double getAggregation() {

    try {
      return (Double) this.get("Aggregation");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setAggregation(Double aggregation) {

    try {
      this.put("Aggregation", aggregation);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Ordering for this connection end.
   */
  public Double getOrdering() {

    try {
      return (Double) this.get("Ordering");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setOrdering(Double ordering) {

    try {
      this.put("Ordering", ordering);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. A qualifier that may apply to connection end.
   */
  public String getQualifier() {

    try {
      return (String) this.get("Qualifier");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setQualifier(String qualifier) {

    try {
      this.put("Qualifier", qualifier);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. A constraint that may be applied to this connection end.
   */
  public String getConstraint() {

    try {
      return (String) this.get("EAConstraint");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setConstraint(String constraint) {

    try {
      this.put("EAConstraint", constraint);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Flag indicating this end is navigable from the other.
   */
  public Boolean getIsNavigable() {

    try {
      return (Boolean) this.get("IsNavigable");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setIsNavigable(Boolean isnavigable) {

    try {
      this.put("IsNavigable", isnavigable);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Flag indicating this end is changeable or not.
   */
  public Boolean getIsChangeable() {

    try {
      return (Boolean) this.get("IsChangeable");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setIsChangeable(Boolean ischangeable) {

    try {
      this.put("IsChangeable", ischangeable);
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
