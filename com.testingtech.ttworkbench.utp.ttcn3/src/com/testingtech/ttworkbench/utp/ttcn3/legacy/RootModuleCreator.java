/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2006-2012.  All Rights Reserved.
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
package com.testingtech.ttworkbench.utp.ttcn3.legacy;

import org.eclipse.emf.ecore.xmi.XMIResource;

import com.testingtech.muttcn.statements.ModuleDeclaration;
import com.testingtech.ttworkbench.metamodel.core.exception.ModelProcessingException;
import com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator;

public class RootModuleCreator extends GeneralModuleCreator {

  private String moduleName = null;
  
  private ModuleDeclaration module = null;

  /**
   * Constructor.
   * 
   * @param pkg
   * @param target
   * @param rep
   */
  public RootModuleCreator(UTP2TTCN3TransformationManager mgr,
      boolean doOverwrite, XMIResource utpResource) {

    super(mgr, doOverwrite, utpResource);
    if(target != null)
      moduleName = getModuleName(target.getRootFileName());
  }
  

  @Override
  public void create() throws ModelProcessingException {

    checkEMFModuleView(moduleName);
    createModule();
  }

  
  @Override
  public void dispose() {
    super.dispose();
    moduleName = null;
    module = null;
  }

  private void createModule() {

    module = DeclarationGenerator
        .generateModuleDeclaration(moduleName);
    modelObjects.add(module);
  }

  public String getModuleName(){
    return moduleName;
  }
  
  
  @Override
  public ModuleDeclaration getModuleDeclaration() {
    return module;
  }

  private String getModuleName(String fileName) {

    String result = null;
    if (fileName != null) {
      result = fileName
          .substring(0, fileName.indexOf(TransformationUtil.CL_SOURCE_FILE_EXTENSION));
    }
    return result;
  }
  
}
