/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2007-2012.  All Rights Reserved.
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

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;

import com.testingtech.muttcn.auxiliary.ParameterDeclaration;
import com.testingtech.muttcn.auxiliary.SignatureParameterDeclaration;
import com.testingtech.muttcn.statements.ConstantDeclaration;
import com.testingtech.muttcn.statements.Declaration;
import com.testingtech.muttcn.statements.ModuleDeclaration;
import com.testingtech.muttcn.statements.TypeDeclaration;
import com.testingtech.muttcn.types.SignatureType;
import com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator;
import com.testingtech.ttworkbench.utp.core.UTPConsts;

public class DataModuleCreator extends ClassModuleCreator {

  /**
   * Constructor.
   * 
   * @param namespace
   * @param mgr
   * @param doOverwrite
   * @param utpResource
   */
  public DataModuleCreator(org.eclipse.uml2.uml.Namespace namespace,
      UTP2TTCN3TransformationManager mgr, boolean doOverwrite,
      XMIResource utpResource) {

    super(namespace, mgr, doOverwrite, utpResource);
  }

  protected void transform(ModuleDeclaration m) {

    createDataType();
    
    Ttcn3Creator t3c = new Ttcn3Creator(this);
    UML2TTCN3Transformer umlTransformer = new UML2TTCN3Transformer(t3c);
    EList packagedElements = namespace.getOwnedMembers();
    for (int i = 0; i < packagedElements.size(); i++) {
      EObject obj = (EObject) packagedElements.get(i);
      umlTransformer.doSwitch(obj);
    }
    createOperationFunction();
    resolveInheritance();
  }
  
  /**
   * Add parameter/return to given declaration
   * @param par
   * @param decl
   * @param direction
   */
  private void addParameter(ParameterDeclaration par, 
      ConstantDeclaration decl, 
      ParameterDirectionKind direction) {
    if (direction == ParameterDirectionKind.RETURN_LITERAL) {
      setFunctionReturn(decl, par.getTheType());
    }
    else{
      addParameterToFunction(decl, par);
    }
  }

  @Override
  protected void addSignatureDeclaration(Operation op, TypeDeclaration ttcn3Elm, EObject parent) {
    signatures.put(ttcn3Elm, op);
  }

  @Override
  protected TypeDeclaration createAttributeSignature(Property prop) {
    // do not create signature, but only data field.
    createAttributeDataField(prop);
    return null;
  }
  
  @Override
  protected void makeSignatureCopy(List<Declaration> copies, TypeDeclaration obj) {
    // do not copy signature
  }

  protected class UML2TTCN3Transformer extends
  ClassModuleCreator.UML2TTCN3Transformer {

    public UML2TTCN3Transformer(Ttcn3Creator ttcn3Creator) {

      super(ttcn3Creator);
    }

    @Override
    public Object caseParameter(Parameter e) {
      Operation operation = e.getOperation();
      
      if (operation == null) {
        return super.caseParameter(e);
      }
      else {
        EObject ttcn3Elm = ttcn3Creator.doCreate(e);
        elementMap.put(e, ttcn3Elm);
        defaultCase(e);
    
        EObject parent = (EObject) doSwitch(operation);
        if (ttcn3Elm instanceof SignatureParameterDeclaration
            && parent instanceof TypeDeclaration &&
            ((TypeDeclaration)parent).getTheType() instanceof SignatureType) {
          addParameter((SignatureParameterDeclaration) ttcn3Elm,
              (TypeDeclaration) parent, e.getDirection());
        }else if (ttcn3Elm instanceof ParameterDeclaration
            && parent instanceof ConstantDeclaration) {
          addParameter((ParameterDeclaration) ttcn3Elm,
              (ConstantDeclaration) parent, e.getDirection());
        }
        return e;
      }
    }

    
  }

  protected class Ttcn3Creator extends ClassModuleCreator.Ttcn3Creator {

    public Ttcn3Creator(GeneralModuleCreator creator) {

      super(creator);
    }

    public EObject doCreate(org.eclipse.uml2.uml.Element umlElm) {

      EObject result = null;
      EList stTypes = umlElm.getAppliedStereotypes();
      if (stTypes.size() > 0) {
        if (umlElm instanceof Operation) {
          if (isStereotypeApplied(umlElm, UTPConsts.DATA_SELECTOR_NAME))
            result = transformDataSelector((Operation)umlElm);
        }
      }
      if(result == null)
        result = super.doCreate(umlElm);
      return result;
    }
    
    public EObject transformDataSelector(Operation umlElm){
      String name = getDataSelectorFunctionName(umlElm);
      ConstantDeclaration decl = createExternalFunction(name, null);
      // address type as first parameter
      ParameterDeclaration fp = DeclarationGenerator.generateParameterDeclaration(getAddressParName(decl, umlElm));
      fp.setIsIn(true);
      fp.setTheType(createIdentifier(getAddressTypeName()));
      addParameterToFunction(decl, fp);
      return decl;
    }
  }

  /**
   * Get function name for given operation as data selector.
   * @param umlElm
   * @return
   */
  public String getDataSelectorFunctionName(Operation umlElm) {
    return NameMaker.createName(umlElm);
  }
}
