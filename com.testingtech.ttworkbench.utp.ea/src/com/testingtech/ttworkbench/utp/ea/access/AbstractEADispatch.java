/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2012.  All Rights Reserved.
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
package com.testingtech.ttworkbench.utp.ea.access;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;

import org.apache.log4j.Logger;

public class AbstractEADispatch extends IDispatch {

  public static final String DEFAULT_NAME = "*";

  public static final String DISPATCHKEY_NAME = "Name";

  public static final String DISPATCHKEY_TYPE = "Type";

  public static final String DISPATCHKEY_OBJECT_TYPE = "ObjectType";

  public static final String DISPATCHKEY_UPDATE = "Update";

  protected Logger logger;

  public AbstractEADispatch(IDispatch disp) {

    super(disp);
  }

  /**
   * Name is a read-write attribute. The name of the connector.
   */
  public String getName() {

    try {
      final String name = (String) get(DISPATCHKEY_NAME);
      return name != null ? name : DEFAULT_NAME;
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setName(String name) {

    try {
      put(DISPATCHKEY_NAME, name);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read/Write. UMLConnectorFactory type.
   */
  public String getType() {

    try {
      return (String) get(DISPATCHKEY_TYPE);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public void setType(String type) {

    try {
      put(DISPATCHKEY_TYPE, type);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Read only. Distinguishes objects referenced through Dispatch interface.
   */
  public Integer getObjectType() {

    try {
      return ((Integer) get(DISPATCHKEY_OBJECT_TYPE));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Update the current UMLConnectorFactory object after modification or
   * appending a new item.
   */
  public Boolean update() {

    try {
      return (Boolean) method(DISPATCHKEY_UPDATE, null);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

}
