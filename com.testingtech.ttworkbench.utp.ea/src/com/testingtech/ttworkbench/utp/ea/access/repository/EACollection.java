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

import java.util.Iterator;
import java.util.NoSuchElementException;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.JComException;

import org.apache.log4j.Logger;

import com.testingtech.ttworkbench.utp.ea.access.AbstractEADispatch;
import com.testingtech.ttworkbench.utp.ea.access.element.EAElement;
import com.testingtech.ttworkbench.utp.ea.uml2.model.Helper;

/**
 * This is the main collection class used by all elements within the automation
 * interface. It contains methods to iterate through the collection, refresh the
 * collection and delete an item from the collection. It is important to realize
 * that when AddNew is called, the item is not automatically added to the
 * current collection.
 */
public class EACollection extends AbstractEADispatch implements Iterable<EAElement> {

  private final class IteratorImpl implements Iterator<EAElement> {

    private int index = 0;
    private int maxIndex = 0;
    
    IteratorImpl() {
      maxIndex = EACollection.this.getCount() - 1;
    }
    
    public void remove() {
    
      throw new UnsupportedOperationException();
    }

    public EAElement next() {
    
      if (index <= maxIndex) {
        return EAElement.getInstance(EACollection.this.getAt(index++));
      }
      throw new NoSuchElementException();
    }

    public boolean hasNext() {

      return index <= maxIndex;
    }
  }

  private static final String METHOD_DELETE = "Delete";

  private static final String METHOD_GET_AT = "GetAt";

  private static final String PROPERTY_COUNT = "Count";

  public EACollection(IDispatch disp) {

    super(disp);
    logger = Logger.getLogger(EACollection.class);
    // BasicConfigurator.configure();
  }

  /**
   * Count is a read-only collection attribute. Represents the number of objects
   * referenced by this collection.
   */
  public int getCount() throws NumberFormatException {

    try {
      return Integer.parseInt(get(PROPERTY_COUNT).toString());
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return -1;
    }
  }

  /**
   * Retrieves the array object using a numerical index. If the index is out of
   * bounds, an error will occur.
   */
  public IDispatch getAt(int idx) {

    Object[] arglist = new Object[]{
      Integer.valueOf(idx)
    };
    try {
      return (IDispatch) method(METHOD_GET_AT, arglist);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Delete the item at the selected reference. TODO remove because unused?
   */
  public void delete(Short idx) {

    Object[] arglist = new Object[1];
    arglist[0] = idx;
    try {
      method(METHOD_DELETE, arglist);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Delete the item at the selected index. Also provides an option to refresh
   * the collection after the deletion.
   */
  public void deleteAt(Boolean refresh, Short idx) {

    Object[] arglist = new Object[2];
    arglist[0] = refresh;
    arglist[1] = idx;
    try {
      method("DeleteAt", arglist);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Get an item in the current collection by Name. Only applies to the main
   * objects - UMLElementFactory, EAPackage etc.
   */
  public IDispatch getByName(String name) {

    Object[] arglist = new Object[1];
    arglist[0] = name;
    try {
      return (IDispatch) method("GetByName", arglist);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Refresh the collection by re-querying the model and reloading the
   * collection.
   */
  public void refresh() {

    try {
      method("Refresh", null);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Add a new item to the current collection. You must provide a Name and Type
   * argument. What these are used for depend on the actual collection member.
   * Item not automatically added to current collection.
   * 
   * @see EACollection
   */
  public IDispatch addNew(String type, String name) {

    Object[] arglist = new Object[2];
    arglist[0] = type;
    arglist[1] = name;
    try {
      return (IDispatch) method("AddNew", arglist);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  public Iterator<EAElement> iterator() {

    
    return new IteratorImpl();
  }
}
