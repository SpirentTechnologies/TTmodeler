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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.VisibilityKind;

import com.testingtech.muttcn.auxiliary.ElementWithAnnotation;
import com.testingtech.muttcn.auxiliary.SignatureParameterDeclaration;
import com.testingtech.muttcn.compilerkernel.Name;
import com.testingtech.muttcn.kernel.Expression;
import com.testingtech.muttcn.kernel.Identifier;
import com.testingtech.muttcn.statements.Declaration;
import com.testingtech.muttcn.statements.ImportDeclaration;
import com.testingtech.muttcn.statements.ModuleDeclaration;
import com.testingtech.muttcn.statements.NamedElementDeclaration;
import com.testingtech.muttcn.statements.TypeDeclaration;
import com.testingtech.muttcn.types.BasicType;
import com.testingtech.muttcn.types.SignatureType;
import com.testingtech.muttcn.values.ModuleValue;
import com.testingtech.ttcn.metamodel.ActionMatcher;
import com.testingtech.ttcn.metamodel.DefaultDuplicator;
import com.testingtech.ttcn.metamodel.Reducer;
import com.testingtech.ttworkbench.core.ide.EMFModuleView;
import com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator;
import com.testingtech.ttworkbench.utp.core.TTmodelerConsts;

public class InterfaceModuleCreator extends NamespaceModuleCreator {

  protected Map<TypeDeclaration, Operation> signatures = new HashMap<TypeDeclaration, Operation>();


  /**
   * Constructor.
   * 
   * @param namespace
   * @param mgr
   * @param doOverwrite
   * @param utpResource
   */
  public InterfaceModuleCreator(org.eclipse.uml2.uml.Namespace namespace,
      UTP2TTCN3TransformationManager mgr, boolean doOverwrite,
      XMIResource utpResource) {

    super(namespace, mgr, doOverwrite, utpResource);
  }

  protected void transform(ModuleDeclaration moduleDeclaration) {
    
    
    
    Ttcn3Creator t3c = new Ttcn3Creator(this, moduleDeclaration);
    UML2TTCN3Transformer umlTransformer = new UML2TTCN3Transformer(t3c);
    EList packagedElements = namespace.getOwnedMembers();
    for (int i = 0; i < packagedElements.size(); i++) {
      EObject obj = (EObject) packagedElements.get(i);
      umlTransformer.doSwitch(obj);
    }
    resolveInheritance();
  }

  public void dispose() {
    signatures.clear();
    super.dispose();
  }

  /**
   * Get address type name for this namespace.
   */
  public String getAddressTypeName(){
    return NameMaker.createName(namespace) + TTmodelerConsts.ADDRESS_APPENDIX;
  }

  /**
   * Get name of get-signature for attribute with input as name.
   * @param input
   * @return
   */
  public String getAttributeGetSignatureName(Property prop){
    return NameMaker.createName(prop);
  }

  public String getAttributeSetSignatureName(Property prop){
    return NameMaker.createName(prop);
  }

  public String getInitFunctionName() {
    return NameMaker.createName(namespace) + TTmodelerConsts.EXTERNAL_FUNCTION_APPENDIX;
  }

  /**
   * Get name of signature for operation.
   * @param op
   * @return
   */
  public String getOperationSignatureName(Operation op){
    return NameMaker.createName(op);
  }

  /**
   * Get signature declaration for given name.
   * @param name
   * @return
   */
  protected TypeDeclaration getSignatureDeclaration(String name){
    for(TypeDeclaration decl : signatures.keySet()){
      if(decl.getTheName().getTheName().equals(name))
        return decl;
    }
    return null;
  }

  /**
   * Resolve inheritance.
   * e.g. include public properties of parent. 
   *
   */
  protected void resolveInheritance(){
    EList rels = namespace.getRelationships();
    for (int i = 0; i < rels.size(); i++) {
      Relationship dp = (Relationship)rels.get(i);
      if(dp instanceof Generalization){
        Classifier gen = ((Generalization)dp).getGeneral();
        addInherited(gen);
      }else if(dp instanceof Realization){
        EList suppliers = ((Realization)dp).getSuppliers();
        for (int j = 0; j < suppliers.size(); j++) {
          Object obj = suppliers.get(j);
          if(obj instanceof Classifier){
            addInherited((Classifier)obj);
          }
        }
      }
    }
  }

  /**
   * Add elements of gen as inherited.
   * @param gen
   */
  private void addInherited(Classifier gen) {

    if(gen.equals(namespace))
      return;
    ModuleDeclaration decl = getModuleDeclaration(gen);
    if(decl != null){
      ModuleValue mv = decl.getTheModuleValue();
      if(mv != null){
        EList list = mv.getTheDeclarations();
        List<Declaration> copies = makeCopies(decl.getTheName().getTheName(),
            list);
        checkImport(copies, decl);
      }
    }
  }

  /**
   * Make copies of given list of declarations.
   * @param moduleName
   * @param list
   * @return
   */
  protected List<Declaration> makeCopies(String moduleName, EList list){
    // TODO: copy names
    List<Declaration> copies = new ArrayList<Declaration>();
    for (int j = 0; j < list.size(); j++) {
      Object obj = list.get(j);
      if(obj instanceof TypeDeclaration){
        if(!alreadyDeclared((Declaration)obj)){
          copyDeclaration((Declaration)obj);
          copies.add((Declaration)obj);
        }
      }
    }
    return copies;
  }

  /**
   * Get module declaration for the given classifier.
   * @param gen
   * @return
   */
  protected ModuleDeclaration getModuleDeclaration(Classifier gen) {
    String moduleName = NameMaker.createName(gen);
    ModuleDeclaration result = null;
    EMFModuleView emfView = manager.getEmfModuleView(moduleName);
    if(emfView == null){
      emfView = makeModule(gen); 
    }
    EObject obj = getTTCN3Declaration(emfView, moduleName);
    if(obj != null && obj instanceof ModuleDeclaration){
      result = (ModuleDeclaration) obj;
    }
    return result;
  }

  /**
   * Add signature declaration to parent.
   * @param op
   * @param ttcn3Elm
   * @param parent
   */
  protected void addSignatureDeclaration(Operation op, TypeDeclaration ttcn3Elm, EObject parent) {

    addDeclaration(parent, ttcn3Elm);
    signatures.put(ttcn3Elm, op);
  }

  /**
   * Check if the given declaration already know to this module.
   * @param declaration
   * @return
   */
  protected boolean alreadyDeclared(Declaration declaration) {
    boolean result = false;
    ModuleValue mv = module.getTheModuleValue();
    if(mv != null) {
      EList list = mv.getTheDeclarations();
      for (int i = 0; i < list.size(); i++) {
        Object obj = list.get(i);
        if(obj instanceof TypeDeclaration 
            && declaration instanceof TypeDeclaration){
          DeclarationComparator dc = new DeclarationComparator((TypeDeclaration)obj);
          if(dc.equals((TypeDeclaration)declaration)){
            result = true;
            break;
          }    
        }
      }
    }
    return result;
  }


  /**
   * Make a copy of the given declaration in this module.
   * @param declaration
   */
  protected void copyDeclaration(Declaration declaration) {
    if(declaration instanceof ElementWithAnnotation){
      duplicate(declaration);
    }

  }

  /**
   * Check imports from the parent module
   * for this module, according to the given declarations.
   * @param declarations
   * @param parent
   */
  protected void checkImport(List<Declaration> declarations, ModuleDeclaration parent) {
    // add imports from parent
    ModuleValue mv = parent.getTheModuleValue();
    if(mv != null) {
      EList list = mv.getTheDeclarations();
      for (int i = 0; i < list.size(); i++) {
        Object obj = list.get(i);
        if(obj instanceof ImportDeclaration){
          String mn = ((ImportDeclaration)obj).getTheSourceModule().getTheName().getTheName();
          addImport(mn);
        }
      }
    }
    // references to address type defined in parent module
    String moduleName = parent.getTheName().getTheName();
    for (Declaration decl : declarations) {
      new ActionMatcher() {
        private String mn;

        public void check(String moduleName, Declaration decl){
          mn = moduleName;
          visit(decl);
        }

        @Override
        protected Object action(Identifier arg0) {

          String idName = arg0.getTheName().getTheName();
          if (idName.equals(mn)) {
            addImport(mn, "type", idName);
          }
          return arg0;
        }

      }.check(moduleName, decl);
    }
  }

  /**
   * Add duplicate of the given declaration to this module.
   * 
   * @param declaration
   */
  protected void duplicate(ElementWithAnnotation declaration) {

    Object obj = (new DefaultDuplicator()).visit(declaration);
    if(obj != null && obj instanceof NamedElementDeclaration){
      addDeclaration2Module(module, (NamedElementDeclaration)obj);
    }
  }

  /**
   * Create signature(s) for property as attribute of Interface or Class.
   * @param prop
   * @return
   */
  protected TypeDeclaration createAttributeSignature(Property prop) {

    // only public property to signature
    if(prop.getVisibility() != VisibilityKind.PUBLIC_LITERAL)
      return null;

    org.eclipse.uml2.uml.Type tp = prop.getType();
    TypeDeclaration getSignature = null;
    if (tp != null) {
      com.testingtech.muttcn.kernel.Expression typeRef = getTypeReference(prop);
      boolean isReadOnly = prop.isReadOnly();
      getSignature = DeclarationGenerator
      .generateSignatureDeclaration(getAttributeGetSignatureName(
          prop));
      String returnName = TTmodelerConsts.RETURN_PARAM_NAME;
      SignatureParameterDeclaration getReturn = TransformationUtil.createSignatureParameterDeclaration(
          typeRef, returnName, ParameterDirectionKind.RETURN_LITERAL);
      addParameter(getReturn, getSignature,
          ParameterDirectionKind.RETURN_LITERAL);
      if (!isReadOnly) {
        TypeDeclaration setSignature = DeclarationGenerator
        .generateSignatureDeclaration(getAttributeSetSignatureName(
            prop));
        String parName = NameMaker.createName(prop);
        SignatureParameterDeclaration setPar = TransformationUtil.createSignatureParameterDeclaration(
            typeRef, parName, ParameterDirectionKind.IN_LITERAL);
        addParameter(setPar, setSignature,
            ParameterDirectionKind.IN_LITERAL);
        pendingDeclarations.add(setSignature);
      }
    }
    return getSignature;
  }

  class Ttcn3Creator extends NamespaceModuleCreator.Ttcn3Creator {

    protected ModuleDeclaration moduleDeclaration;

    public Ttcn3Creator(GeneralModuleCreator creator){
      super(creator);
    }


    public Ttcn3Creator(InterfaceModuleCreator interfaceModuleCreator,
        ModuleDeclaration m) {
      this(interfaceModuleCreator);
      moduleDeclaration = m;
    }


    /**
     * Transform operation to signature.
     * 
     * @return
     */
    public EObject transform(Operation operation) {
      // only public operation to signature
      if(operation.getVisibility() != VisibilityKind.PUBLIC_LITERAL)
        return null;
      org.eclipse.uml2.uml.Namespace ns = operation.getNamespace();
      if (ns != null && 
          (ns instanceof Interface || ns instanceof org.eclipse.uml2.uml.Class)) {
        TypeDeclaration decl = DeclarationGenerator
        .generateSignatureDeclaration(getOperationSignatureName(operation));        
        processOperationExceptions(decl, operation);
        return decl;
      }
      return null;
    }

    public void processOperationExceptions(TypeDeclaration signature, Operation operation) {

      for (Type exceptionType : operation.getRaisedExceptions()) {
        // first add exception type to signature declaration
        final SignatureType signatureType = (SignatureType) signature.getTheType();
        final String exceptionTypeName = exceptionType.getName();
        Name signatureTypeName = Reducer.makeName(exceptionTypeName);
        Identifier signatureTypeExpression = Reducer.makeIdent(signatureTypeName);
        signatureType.getTheExceptionTypes().add(signatureTypeExpression);
        // now collect the base type of the exception type and add the respective type declaration to the interface module
        for (Property prop : ((org.eclipse.uml2.uml.Class)exceptionType).getAllAttributes()) {
          if (prop.getName().equals(TTmodelerConsts.EXCEPTION_BASETYPE_ATTRIBUTE_NAME)){
            Expression exceptionBaseType = getTypeReference(prop);
            TypeDeclaration exceptionTypeDecl = DeclarationGenerator.generateTypeDeclaration(signatureTypeName.getTheName());
            exceptionTypeDecl.setTheType(exceptionBaseType);
            addDeclaration2Module(moduleDeclaration, exceptionTypeDecl);
          }
        }
      }
    }

    /**
     * Property of test context is transformed to configuration part.
     * 
     * @param umlElm
     * @return
     */
    public EObject transform(Property umlElm) {

      org.eclipse.uml2.uml.Namespace ns = umlElm.getNamespace();
      if (ns != null && 
          (ns instanceof Interface || ns instanceof org.eclipse.uml2.uml.Class) && 
          !(umlElm instanceof org.eclipse.uml2.uml.Port)) {
        return createAttributeSignature(umlElm);
      }
      return null;
    }

    public EObject transform(Parameter umlElm) {

      TypeDeclaration sig = getSignatureDeclaration(getOperationSignatureName(umlElm.getOperation()));
      org.eclipse.uml2.uml.Type tp = umlElm.getType();
      if (tp != null) {
        com.testingtech.muttcn.kernel.Expression typeRef = getBasicTypeOrIdentifier(tp);
        if (typeRef != null) {
          String name = TTmodelerConsts.RETURN_PARAM_NAME;
          if (umlElm.getDirection() != ParameterDirectionKind.RETURN_LITERAL)
            name = NameMaker.createName(umlElm);
          SignatureParameterDeclaration decl = TransformationUtil.createSignatureParameterDeclaration(
              typeRef, name, umlElm.getDirection());
          return decl;
        }
      }
      return null;
    }

    public EObject doCreate(org.eclipse.uml2.uml.Element umlElm) {

      EObject result = null;
      EList stTypes = umlElm.getAppliedStereotypes();
      if (stTypes.size() == 0) {
        if (umlElm instanceof Operation) {
          result = transform((Operation) umlElm);
        }
        else if (umlElm instanceof Parameter) {
          result = transform((Parameter) umlElm);
        }
        else if (umlElm instanceof Property) {
          result = transform((Property) umlElm);
        }
      }
      if (result == null)
        result = super.doCreate(umlElm);
      return result;
    }
  }

  protected class UML2TTCN3Transformer extends
  NamespaceModuleCreator.UML2TTCN3Transformer {

    public UML2TTCN3Transformer(
        Ttcn3Creator ttcn3Creator) {

      super(ttcn3Creator);
    }

    public Object caseOperation(Operation e) {
      Namespace namespace = e.getNamespace();
      if (!(namespace instanceof org.eclipse.uml2.uml.Class)
          && !(namespace instanceof Interface)) {
        return super.caseOperation(e);
      } else {
        EObject ttcn3Elm = ttcn3Creator.doCreate(e);        
        if (ttcn3Elm != null) {
          EObject parent = (EObject) doSwitch(namespace);
          if(ttcn3Elm instanceof TypeDeclaration)
            addSignatureDeclaration(e, (TypeDeclaration)ttcn3Elm, parent);
          else
            addDeclaration(parent, ttcn3Elm);
          elementMap.put(e, ttcn3Elm);
          defaultCase(e);
        }
        return e;
      }
    }

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
              (TypeDeclaration) parent, 
              e.getDirection());
        }
        return e;
      }
    }

    public Object caseProperty(Property e) {
      Namespace namespace = e.getNamespace();
      if (!(namespace instanceof org.eclipse.uml2.uml.Class)
          && !(namespace instanceof Interface)) {
        return super.caseProperty(e);
      } else {
        EObject ttcn3Elm = ttcn3Creator.doCreate(e);        
        if (ttcn3Elm != null) {
          elementMap.put(e, ttcn3Elm);
          defaultCase(e);
          EObject parent = (EObject) doSwitch(namespace);
          addDeclaration(parent, ttcn3Elm);
        }
        return e;
      }
    }
  }

  /**
   * Compare declarations.
   * @author mli
   *
   */
  protected class DeclarationComparator extends ActionMatcher{

    private Declaration decl;

    private boolean result = false;

    /**
     * Constructor.
     * @param elm
     */
    public DeclarationComparator(Declaration elm){
      this.decl = elm;
    }

    public boolean equals(Declaration input){
      visit(input);
      return result;
    }

    @Override
    protected Object match(TypeDeclaration input) {
      if(decl instanceof TypeDeclaration){
        TypeDeclaration thisDecl = (TypeDeclaration)decl;
        if(thisDecl.getTheName().getTheName().equals(input.getTheName().getTheName())){
          SignatureType thisSV = (SignatureType) thisDecl.getTheType();
          SignatureType inputSV = (SignatureType) input.getTheType();
          if(thisSV != null && inputSV != null){
            EList thisPars = thisSV.getTheFieldDeclarations();
            EList inputPars = thisSV.getTheFieldDeclarations();
            if(thisPars != null && inputPars != null
                && thisPars.size() == inputPars.size()){
              result = true;
              for (int i = 0; i < thisPars.size(); i++) {
                SignatureParameterDeclaration thisPar = (SignatureParameterDeclaration)thisPars.get(i);
                SignatureParameterDeclaration inputPar = (SignatureParameterDeclaration)inputPars.get(i);
                Expression thisParType = thisPar.getTheType();
                Expression inputParType = inputPar.getTheType();
                if(!equalType(thisParType, inputParType)){
                  result = false;
                  break;
                }
              }
            }
          }
        }
      }
      return input;
    }

    /**
     * Return true if types t1 equals t2.
     * @param t1
     * @param t2
     * @return
     */
    private boolean equalType(Expression t1, Expression t2) {
      boolean result = false;
      if(t1 instanceof BasicType && t2 instanceof BasicType){
        if(((BasicType)t1).getTheName().getTheName()
            .equals(((BasicType)t2).getTheName().getTheName()))
          result = true;
      }else if(t1 instanceof NamedElementDeclaration
          && t2 instanceof NamedElementDeclaration){
        if(((NamedElementDeclaration)t1).getTheName().getTheName()
            .equals(((NamedElementDeclaration)t2).getTheName().getTheName()))
          result = true;
      }else if (t1 instanceof Identifier && t1 instanceof Identifier){
        if(((Identifier)t1).getTheName().getTheName()
            .equals(((Identifier)t2).getTheName().getTheName()))
          result = true;
      }
      return result;
    }
  }

}
