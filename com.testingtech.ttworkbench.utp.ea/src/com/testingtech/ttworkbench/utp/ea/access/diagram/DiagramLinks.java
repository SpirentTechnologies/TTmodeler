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

/**
 * A DiagramLink is an object which holds display
 * information about a connection between two elements in a specific
 * diagram. It includes the custom points, display appearance & etc.
 * Accessed from the Diagram DiagramLinks collection.
 */
public class DiagramLinks extends AbstractEADispatch {

  public DiagramLinks(IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(DiagramLinks.class);
  }

  /**
   * Read only attribute. Holds the link identifier for the current model.
   */
  public Double getInstanceID() {

    try {
      return (Double) this.get("InstanceID");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read/Write. The local ID for the associated diagram.
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

  public void setDiagramID(Double id) {

    try {
      this.put("DiagramID", id);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The ID of the associated connector.
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

  public void setConnectorID(Double id) {

    try {
      this.put("ConnectorID", id);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The geometry associated with the current connector in this
   * diagram.
   */
  public String getGeometry() {

    try {
      return (String) this.get("Geometry");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setGeometry(String geometry) {

    try {
      this.put("Geometry", geometry);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The path of the connector in this diagram.
   */
  public String getPath() {

    try {
      return (String) this.get("Path");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setPath(String path) {

    try {
      this.put("Path", path);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Additional style information - eg. color, thickness.
   */
  public String getStyle() {

    try {
      return (String) this.get("Style");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setStyle(String style) {

    try {
      this.put("Style", style);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Flag to indicate if this item is hidden or not.
   */
  public Boolean getIsHidden() {

    try {
      return (Boolean) this.get("IsHidden");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setIsHidden(Boolean ishidden) {

    try {
      this.put("IsHidden", ishidden);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }
}
