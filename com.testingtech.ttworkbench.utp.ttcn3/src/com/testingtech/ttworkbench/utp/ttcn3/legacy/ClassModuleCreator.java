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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;

import com.testingtech.muttcn.auxiliary.DataFieldDeclaration;
import com.testingtech.muttcn.auxiliary.FieldDeclaration;
import com.testingtech.muttcn.auxiliary.ParameterDeclaration;
import com.testingtech.muttcn.statements.ConstantDeclaration;
import com.testingtech.muttcn.statements.Declaration;
import com.testingtech.muttcn.statements.TypeDeclaration;
import com.testingtech.muttcn.types.RecordType;
import com.testingtech.muttcn.types.SetType;
import com.testingtech.muttcn.types.SignatureType;
import com.testingtech.ttcn.metamodel.DefaultDuplicator;
import com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator;
import com.testingtech.ttworkbench.utp.core.TTmodelerConsts;

public class ClassModuleCreator extends InterfaceModuleCreator {

  protected TypeDeclaration dataType = null;
  
  /**
   * Constructor.
   * 
   * @param namespace
   * @param mgr
   * @param doOverwrite
   * @param utpResource
   */
  public ClassModuleCreator(org.eclipse.uml2.uml.Namespace namespace,
      UTP2TTCN3TransformationManager mgr, boolean doOverwrite,
      XMIResource utpResource) {

    super(namespace, mgr, doOverwrite, utpResource);
  }
 
  /**
   * Create function for operations
   *
   */
  protected void createOperationFunction() {
    // operation in this namespace
    createOperationFunction(signatures);
    // inherited operations
    List<GeneralModuleCreator> parentCreators = getParentNamespaceCreators();
    for(GeneralModuleCreator cr : parentCreators){
      if(cr instanceof InterfaceModuleCreator){
        createOperationFunction(((InterfaceModuleCreator)cr).signatures);
      }
    }
  }

  /**
   * Create external function for each given signature.
   * @param sigs
   */
  protected void createOperationFunction(Map<TypeDeclaration, Operation> sigs) {

    for (TypeDeclaration sig : sigs.keySet()) {
      Operation op = sigs.get(sig);
      String name = getOperationFunctionName(op);
      SignatureType sv = (SignatureType) sig.getTheType();
      ConstantDeclaration decl = null;
      if (sv != null) {
        decl = createExternalFunction(name, sv.getTheType());
      }
      else
        decl = createExternalFunction(name, null);
      // address type as first parameter
      ParameterDeclaration fp = DeclarationGenerator.generateParameterDeclaration(getAddressParName(decl, op));
      fp.setIsIn(true);
      fp.setTheType(createIdentifier(getAddressTypeName()));
      addParameterToFunction(decl, fp);
      // other parameters
      EList pars = op.getOwnedParameters();
      for (int i = 0; i < pars.size(); i++) {
        Parameter sigPar = (Parameter) pars.get(i);
        if (sigPar.getDirection() != ParameterDirectionKind.RETURN_LITERAL) {
           org.eclipse.uml2.uml.Type tp = sigPar.getType();
           com.testingtech.muttcn.kernel.Expression typeRef = getBasicTypeOrIdentifier(tp);
           if (typeRef != null) {
             String parName = NameMaker.createName(sigPar);
             ParameterDeclaration fPar = createParameterDeclaration(typeRef, parName, sigPar
                    .getDirection());
             addParameterToFunction(decl, fPar);
          }
        }
      }
      addDeclaration2Module(module, decl);
    }
  }

  /**
   * Get address parameter name.
   * @param decl
   * @param op
   * @return
   */
  public String getAddressParName(ConstantDeclaration decl, Operation op) {
    return NameMaker.createName(op);
  }

  /** 
   * Get function name for given operation.
   */
  public String getOperationFunctionName(Operation op) {
    return NameMaker.createName(op);
  }

  /**
   * Create data type fields in additional to signatures as for interface
   * @param prop
   * @return
   */
  protected TypeDeclaration createAttributeSignature(Property prop) {
    TypeDeclaration sig = super.createAttributeSignature(prop);
    createAttributeDataField(prop);
    return sig;
  }

  /**
   * Transform property that is used by association.
   * @param prop
   */
  protected void transformAssociationProperty(Property prop) {

    // TODO Auto-generated method stub
    
  }

  /**
   * Create data field in data type for given attribute/property.
   * @param prop
   */
  protected void createAttributeDataField(Property prop) {

    org.eclipse.uml2.uml.Type tp = prop.getType();
    if (dataType != null && tp != null) {
      com.testingtech.muttcn.kernel.Expression typeRef = getTypeReference(prop);
      if (typeRef != null) {
        DataFieldDeclaration fd = DeclarationGenerator
            .generateDataFieldDeclaration(getAttributeFieldName(prop), typeRef);
        // TODO: optional if lower==0
        SetType rec = (SetType) dataType.getTheType();
        rec.getTheFieldDeclarations().add(fd);
      }
    }
  }

  @Override
  protected com.testingtech.muttcn.kernel.Expression getBaseTypeReference(Property prop) {
    org.eclipse.uml2.uml.Type type = prop.getType();
    com.testingtech.muttcn.kernel.Expression typeRef = null;
    if (type instanceof org.eclipse.uml2.uml.Class) {
      String dataTypeName = NameMaker.createName(type);
      getTTCN3Declaration(this, dataTypeName, (org.eclipse.uml2.uml.Class)type);
      typeRef = createIdentifier(dataTypeName); 
    }else
      typeRef = super.getBaseTypeReference(prop);
    return typeRef;
  }

  /**
   * Get attribute field name for given property.
   * @param prop
   * @return
   */
  private String getAttributeFieldName(Property prop) {
    return NameMaker.createName(prop);
  }

  /**
   * Get data type name for this class.
   * @return
   */
  public String getDataTypeName() {
    return NameMaker.createName(namespace);
  }

  /**
   * Create data type.
   *
   */
  protected void createDataType() {
    
    String name = getDataTypeName();
    
    //TODO: add further complex type handling here.
    
    if (TransformationUtil.hasKeyword(namespace, TTmodelerConsts.RECORD_TYPE_APPENDIX)) {
      name += TTmodelerConsts.RECORD_TYPE_APPENDIX;
      TypeDeclaration decl = DeclarationGenerator.generateTypeDeclaration(name);
      RecordType record = DeclarationGenerator.generateRecordType();
      decl.setTheType(record);
      addDeclaration2Module(module, decl);
      dataType = decl;
      return;
    }
    
    TypeDeclaration decl = DeclarationGenerator.generateTypeDeclaration(name);
    SetType st = DeclarationGenerator.generateSetType();
    decl.setTheType(st);
    addDeclaration2Module(module, decl);
    dataType = decl;
  }

  public void dispose() {
    dataType = null;
    super.dispose();
  }

  /**
   * Make copies of given list of declarations.
   * @param list
   * @return
   */
  protected List<Declaration> makeCopies(String moduleName, EList list){
//  TODO: copy names
    List<Declaration> copies = new ArrayList<Declaration>();
    for (int j = 0; j < list.size(); j++) {
      Object obj = list.get(j);
      if(obj instanceof TypeDeclaration && 
         ((TypeDeclaration)obj).getTheType() instanceof SignatureType){
        makeSignatureCopy(copies, (TypeDeclaration)obj);
      }else if(obj instanceof TypeDeclaration){
        String tName = ((TypeDeclaration)obj).getTheName().getTheName();
        
        if(((TypeDeclaration)obj).getTheType() instanceof SetType
            && tName.equals(nameMaker.getDataTypeName(moduleName))){
          makeDataFieldCopy(copies, (TypeDeclaration)obj);
          
        }
      }
    }
    return copies;
  }

  /**
   * Make copy of data fields of given obj.
   * @param copies
   * @param obj
   */
  protected void makeDataFieldCopy(List<Declaration> copies, TypeDeclaration obj) {

    EList fields = ((SetType)obj.getTheType()).
    getTheFieldDeclarations();
    for (int i = 0; i < fields.size(); i++) {
      FieldDeclaration fd = (FieldDeclaration)fields.get(i);
      FieldDeclaration ret = copyDataTypeField(fd);
      if(ret != null)
        copies.add((FieldDeclaration)ret);
    }
  }

  /**
   * Get data2obj function name.
   * @return
   */
  public String getData2ObjFunctionName() {
    return NameMaker.createName(namespace);
  }

  /**
   * Get obj2data function name.
   * @return
   */
  public String getObj2DataFunctionName() {
    return NameMaker.createName(namespace);
  }

  /**
   * Make copy of signature
   * @param copies
   * @param obj
   */
  protected void makeSignatureCopy(List<Declaration> copies, 
      TypeDeclaration obj) {

    if(!alreadyDeclared(obj)){
      copyDeclaration(obj);
      copies.add(obj);
    }
  }
  
  /**
   * Make copy of field fd in this data type if not already declared.
   * @param fd
   * @return
   */
  protected FieldDeclaration copyDataTypeField(FieldDeclaration fd) {
    if(dataType != null){
      EList thisFields = ((SetType)dataType.getTheType()).
      getTheFieldDeclarations();
      FieldDeclaration found = null;
      for (int i = 0; i < thisFields.size(); i++) {
        FieldDeclaration d = (FieldDeclaration)thisFields.get(i);
        if(d.getTheName().getTheName().equals(fd.getTheName().getTheName())){
          found = d;
          return null;
        }
      }
      if(found == null){
        FieldDeclaration copy = (FieldDeclaration)(new DefaultDuplicator()).visit(fd);
        
        ((SetType)dataType.getTheType()).
        getTheFieldDeclarations().add(copy);
        return copy;
      }
    }
    return null;
  }
}
