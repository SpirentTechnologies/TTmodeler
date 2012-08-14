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
 * The DiagramObjects collection holds a list of
 * element ID's and presentation information that indicates what will be
 * displayed in a diagram and how it will be shown.
 */
public class DiagramObjects extends AbstractEADispatch {

  public DiagramObjects(IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(DiagramObjects.class);
  }

  /**
   * Read/Write. Read only attribute. Holds the link identifier for the current
   * model.
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
   * The ElementID of the object instance in this diagram.
   */
  public Double getElementID() {

    try {
      return (Double) this.get("ElementID");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setElementID(Double id) {

    try {
      this.put("ElementID", id);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The left position of the element.
   */
  public Double getLeft() {

    try {
      return (Double) this.get("left");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setLeft(Double left) {

    try {
      this.put("left", left);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The right position of the element.
   */
  public Double getRight() {

    try {
      return (Double) this.get("right");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setRight(Double right) {

    try {
      this.put("right", right);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The top position of the element.
   */
  public Double getTop() {

    try {
      return (Double) this.get("top");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setTop(Double top) {

    try {
      this.put("top", top);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. The bottom position of the element.
   */
  public Double getBottom() {

    try {
      return (Double) this.get("bottom");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setBottom(Double bottom) {

    try {
      this.put("bottom", bottom);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. Additional style information for this object: BCol = Background
   * Color BFol = Font Color LCol = Line Color LWth = Line Width The color value
   * is a decimal value. eg. DiagObj.Style =
   * "BCol=35723;BFol=9342520;LCol=9342520;LWth=1;"
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
}
