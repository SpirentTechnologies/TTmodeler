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

/**
 * A EADatatype is a named type that may be associated with attribute or method
 * types.
 */
public class EADatatype extends AbstractEADispatch {

  public EADatatype(IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(EADatatype.class);
    // BasicConfigurator.configure();
  }

  /**
   * DatatypeID is a read-write attribute. Instance ID for this datatype within
   * the current model. System maintained.
   */
  public Double getDatatypeID() {

    try {
      return (Double) this.get("DatatypeID");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setDatatypeID(Double id) {

    try {
      this.put("DatatypeID", id);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Size is a read-write attribute. Indicates the datatype size.
   */
  public Double getSize() {

    try {
      return (Double) this.get("Size");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setSize(Double size) {

    try {
      this.put("Size", size);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * HasLength is a read-write attribute. Indicates datatype has a length
   * component.
   */
  public String getHasLength() {

    try {
      return (String) this.get("HasLength");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setHasLength(String haslength) {

    try {
      this.put("HasLength", haslength);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * GenericType is a read-write attribute. The associated generic type for this
   * data type.
   */
  public String getGenericType() {

    try {
      return (String) this.get("GenericType");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setGenericType(String gtype) {

    try {
      this.put("GenericType", gtype);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }
}
