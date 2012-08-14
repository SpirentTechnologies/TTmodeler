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
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;

import org.apache.log4j.Logger;

import com.testingtech.ttworkbench.utp.ea.access.connector.Connector;
import com.testingtech.ttworkbench.utp.ea.access.diagram.Diagram;
import com.testingtech.ttworkbench.utp.ea.access.element.EAElement;

/**
 * The EARepository is the main container of all models, packages, elements, etc. 
 * you can iteratively begin accessing the model using the Models collection. 
 * Also has some convenience methods to directly access elements, packages, etc.
 * without having to locate them in hierarchy first.
 */
public class EARepository extends IDispatch {

  Logger logger;

  public EARepository(ReleaseManager rm) throws JComException {

    super(rm, "EA.Repository");
    logger = Logger.getLogger(EARepository.class);
    // BasicConfigurator.configure();
  }

  public EARepository(IDispatch disp) {

    super(disp);
  }

  /**
   * Models is a read-only EACollection of elements of type EAPackage.
   */
  public EACollection getModels() {

    try {
      return (new EACollection((IDispatch) get("Models")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Datatypes is a read-only EACollection of elements.
   */
  public EACollection getDatatypes() {

    try {
      return (new EACollection((IDispatch) this.get("Datatypes")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Stereotypes is a read-only EACollection.
   */
  public EACollection getStereotypes() {

    try {
      return (new EACollection((IDispatch) this.get("Stereotypes")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * PropertyTypes is a read-only EACollection.
   */
  public EACollection getPropertyTypes() {

    try {
      return (new EACollection((IDispatch) this.get("PropertyTypes")));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * ConnectionString is a read-only attribute. The filename/connection string
   * of the current EARepository.
   */
  public String getConnectionString() {

    try {
      return ((String) this.get("ConnectionString"));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Read only. Distinguishes objects referenced through Dispatch interface.
   */
  public Integer getObjectType() {

    try {
      return ((Integer) this.get("ObjectType"));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Provide the filename of a valid EA project and call this to open it and
   * work with the contained objects.
   */
  public Boolean openFile(String filename) {

    Object[] arglist = new Object[1];
    arglist[0] = filename;
    try {
      return (Boolean) method("OpenFile", arglist);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Closes any open file.
   */
  public void closeFile() {

    try {
      method("CloseFile", null);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Shut down EA immediately.
   */
  public void exit() {

    try {
      method("Exit", null);
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
    }
  }

  /**
   * Get a pointer to an UMLElementFactory using an absolute reference number
   * (local ID).
   */
  public EAElement getElementByID(Double id) {

    Object[] arglist = new Object[1];
    arglist[0] = id;
    try {
      return EAElement
          .getInstance((IDispatch) method("GetElementByID", arglist));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Get a pointer to an EAPackage using an absolute reference number (local
   * ID).
   */
  public EAPackage getPackageByID(Double id) {

    Object[] arglist = new Object[1];
    arglist[0] = id;
    try {
      return new EAPackage((IDispatch) method("GetPackageByID", arglist));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Searches the repository for a connector with matching ID.
   */
  public Connector getConnectorByID(Double id) {

    Object[] arglist = new Object[1];
    arglist[0] = id;
    try {
      return new Connector((IDispatch) method("GetConnectorByID", arglist));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * Get a pointer to an Diagram using an absolute reference number (local ID).
   */
  public Diagram getDiagramByID(Double id) {

    Object[] arglist = new Object[1];
    arglist[0] = id;
    try {
      return new Diagram((IDispatch) method("GetDiagramByID", arglist));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }

  /**
   * The list type to get Get a pointer to EAReference List object. The
   * parameter specifies the list type to get. Valid lists are: Diagram,
   * UMLElementFactory, EAConstraint, Requirement, ConnectorFactory, Status,
   * Cardinality, Effort, Metric, EAScenario, Status, Test.
   */
  public EAReference getReferenceList(String type) {

    Object[] arglist = new Object[1];
    arglist[0] = type;
    try {
      return new EAReference((IDispatch) method("GetReferenceList", arglist));
    }
    catch (JComException e) {
      logger.warn(e.getMessage());
      return null;
    }
  }
}
