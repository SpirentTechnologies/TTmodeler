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
 * A EAPropertyType object represents a defined property that may be applied to
 * UML elements as a tagged value. Accessed using the EARepository PropertyTypes
 * collection. Each EAPropertyType corresponds to one of the predefined tagged
 * values defined for the model.
 */
public class EAPropertyType extends AbstractEADispatch {

  public EAPropertyType(IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(EAPropertyType.class);
    // BasicConfigurator.configure();
  }

  /**
   * Tag is a read-write attribute.Name of the property (Tag Name)
   */
  public String getTag() {

    try {
      return (String) this.get("Tag");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setTag(String tag) {

    try {
      this.put("Tag", tag);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Description is a read-write attribute.Short description for the property.
   */
  public String getDescription() {

    try {
      return (String) this.get("Description");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setDescription(String description) {

    try {
      this.put("Description", description);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Detail is a read-write attribute.Configuration information for the
   * property.
   */
  public String getDetail() {

    try {
      return (String) this.get("Detail");
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setDetail(String detail) {

    try {
      this.put("Detail", detail);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }
}
