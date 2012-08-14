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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.InstanceValue;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Relationship;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.util.UMLSwitch;

import com.testingtech.muttcn.auxiliary.ParameterDeclaration;
import com.testingtech.muttcn.auxiliary.PortFieldDeclaration;
import com.testingtech.muttcn.auxiliary.RecordField;
import com.testingtech.muttcn.expressions.FieldAssignment;
import com.testingtech.muttcn.functionalabstractions.FunctionValue;
import com.testingtech.muttcn.kernel.Expression;
import com.testingtech.muttcn.kernel.Identifier;
import com.testingtech.muttcn.statements.ConstantDeclaration;
import com.testingtech.muttcn.statements.ModuleDeclaration;
import com.testingtech.muttcn.statements.NamedElementDeclaration;
import com.testingtech.muttcn.statements.TypeDeclaration;
import com.testingtech.muttcn.statements.VarDeclaration;
import com.testingtech.muttcn.statements.VariableDeclaration;
import com.testingtech.muttcn.types.BasicType;
import com.testingtech.muttcn.types.ComponentType;
import com.testingtech.muttcn.types.EnumeratedType;
import com.testingtech.muttcn.types.FunctionType;
import com.testingtech.muttcn.types.ProcedurePortType;
import com.testingtech.muttcn.types.RecordOfType;
import com.testingtech.muttcn.types.SetOfType;
import com.testingtech.muttcn.types.SignatureType;
import com.testingtech.muttcn.types.TemplateType;
import com.testingtech.muttcn.values.FieldValue;
import com.testingtech.ttcn.metamodel.Reducer;
import com.testingtech.ttcn.tools.runtime.TypeClass;
import com.testingtech.ttworkbench.core.ide.EMFModuleView;
import com.testingtech.ttworkbench.metamodel.core.exception.ModelProcessingException;
import com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator;
import com.testingtech.ttworkbench.utp.core.TTmodelerConsts;
import com.testingtech.ttworkbench.utp.core.UMLConsts;
import com.testingtech.ttworkbench.utp.core.UTPConsts;
import com.testingtech.ttworkbench.utp.core.exception.UTPApplicationException;

public class NamespaceModuleCreator extends GeneralModuleCreator {

  protected org.eclipse.uml2.uml.Namespace namespace = null;

  protected ModuleDeclaration module = null;

  protected List<NamedElementDeclaration> pendingDeclarations = new ArrayList<NamedElementDeclaration>();

  // map of uml element to ttcn3 element
  protected final Map<EObject, EObject> elementMap = new HashMap<EObject, EObject>();

  // list of imported identifiers sorted by the containing module name 
  protected Map<String, List<String>> importedIdentifiers = new HashMap<String, List<String>>();


  /**
   * Constructor.
   * 
   * @param pkg
   * @param target
   * @param rep
   */
  public NamespaceModuleCreator(org.eclipse.uml2.uml.Namespace namespace,
      UTP2TTCN3TransformationManager mgr,
      boolean doOverwrite, 
      XMIResource utpResource) {

    super(mgr, doOverwrite, utpResource);
    this.namespace = namespace;
  }

  /**
   * Transform UML package to TTCN-3
   */
  public void create() throws ModelProcessingException {

    if (namespace != null) {
      nameMaker = new NameMaker(this);
      createModule();
      transform(module);
      update();
    }
    else {
      throw new UTPApplicationException(namespace.getName());
    }
  }

  @Override
  public void dispose() {
    elementMap.clear();
    pendingDeclarations.clear();
    importedIdentifiers.clear();
    namespace = null;
    module = null;
    super.dispose();
  }

  /**
   * Get module name for this namespace.
   */
  public String getModuleName() {
    return NameMaker.createName(namespace);
  }





  @Override
  public ModuleDeclaration getModuleDeclaration() {

    return module;
  }

  /**
   * Get namespace for this module.
   * @return
   */
  public Namespace getNamespace(){
    return namespace;
  }

  /**
   * Get creators for parent namespaces of this namespace.
   * @return
   */
  protected List<GeneralModuleCreator> getParentNamespaceCreators(){
    List<GeneralModuleCreator> result = new ArrayList<GeneralModuleCreator>();
    EList rels = namespace.getRelationships();
    for (int i = 0; i < rels.size(); i++) {
      Relationship dp = (Relationship)rels.get(i);
      if(dp instanceof Generalization){
        Classifier gen = ((Generalization)dp).getGeneral();
        GeneralModuleCreator cr = getParentNamespaceCreator(gen);
        if(cr != null)
          result.add(cr);
      }else if(dp instanceof Realization){
        EList suppliers = ((Realization)dp).getSuppliers();
        for (int j = 0; j < suppliers.size(); j++) {
          Object obj = suppliers.get(j);
          if(obj instanceof Classifier){
            GeneralModuleCreator cr = getParentNamespaceCreator((Classifier)obj);
            if(cr != null)
              result.add(cr);
          }
        }
      }
    }
    return result;
  }

  private GeneralModuleCreator getParentNamespaceCreator(Classifier gen){
    if(gen.equals(namespace))
      return null;
    String moduleName = NameMaker.createName(gen);
    GeneralModuleCreator cr = manager.getTransformers().get(moduleName);
    if(cr == null){
      makeModule(gen);
      cr = manager.getTransformers().get(moduleName);
    }
    return cr;
  }

  /**
   * Get ttcn3 declaration for given type name and namespace.
   * @param typeName
   * @param ns
   * @return
   */
  public NamedElementDeclaration getTTCN3Declaration(GeneralModuleCreator creator, 
      String typeName, Namespace ns) {

    if(typeName == null || ns == null)
      return null;
    String pkgName = NameMaker.createName(ns);
    if (pkgName != null) {
      EMFModuleView emfView = manager.getEmfModuleView(pkgName);
      if(emfView == null && ns != null){
        emfView = makeModule(ns); 
      }
      creator.addImport(emfView.getName());
      EObject obj = getTTCN3Declaration(emfView, typeName);
      if(obj != null){
        return (NamedElementDeclaration) obj;
      }
    }
    return null;
  }

  /**
   * Get ttcn3 declaration for given umlType.
   * @param umlType
   * @return
   */
  public NamedElementDeclaration getTTCN3Declaration(
      GeneralModuleCreator creator, org.eclipse.uml2.uml.Type umlType) {

    NamedElementDeclaration result = 
      getTTCN3DeclarationForPredefined(creator, umlType);
    if (result == null) {
      result = getTTCN3Declaration(creator, NameMaker.createName(umlType), getNamespace(creator, umlType));
    }
    return result;
  }


  /**
   * Get ttcn3 declaration for predefined utp type.
   * @param umlType
   * @return
   */
  public NamedElementDeclaration getTTCN3DeclarationForPredefined(
      GeneralModuleCreator creator, org.eclipse.uml2.uml.Type umlType) {
    String typeName = NameMaker.createName(umlType);
    return getTTCN3DeclarationForPredefined(creator, typeName);
  }

  public NamedElementDeclaration getTTCN3DeclarationForPredefined(
      GeneralModuleCreator creator, String typeName) {
    NamedElementDeclaration result = null;
    EMFModuleView view = manager.getPredefinedModule();
    if(view != null){
      result = getTTCN3Declaration(view, typeName);
      if(result != null)
        creator.addImport(view.getName());
    }
    return result;
  }

  /**
   * 
   * @param umlElm
   * @return
   */
  public EMFModuleView makeModule(Namespace umlElm){
    EMFModuleView result = null;
    if (isStereotypeApplied(umlElm,
        UTPConsts.TEST_CONTEXT_NAME)){
      result = makeModule(umlElm, UTPConsts.TEST_CONTEXT_NAME);
    }
    else if (isStereotypeApplied(umlElm,
        UTPConsts.DATA_PARTITION_NAME)){
      result = makeModule(umlElm, UTPConsts.DATA_PARTITION_NAME);
    }
    else if (isStereotypeApplied(umlElm,
        UTPConsts.DATA_POOL_NAME)){
      result = makeModule(umlElm, UTPConsts.DATA_POOL_NAME);
    }else
      result = makeModule(umlElm, umlElm.getClass().getSimpleName());
    return result;
  }

  /**
   * Transform namespace ns to a module.
   * @param ns
   * @param kind
   * @return
   */
  public EMFModuleView makeModule(Namespace ns, String kind) {
    // do not create module for private class, interface etc.
    // TODO: handel inherited elements
    if(ns.getVisibility() == VisibilityKind.PRIVATE_LITERAL)
      return null;

    String moduleName = NameMaker.createName(ns);
    if(moduleName.equals(UTPConsts.PROFILE_NAME)) {
      return manager.getPredefinedModule();
    }
    Resource res = ns.eResource();
    if (manager.getTransformers().get(moduleName) == null 
        && res != null && res instanceof XMIResource) {
      NamespaceModuleCreator genCreator;
      if (kind.equals(UTPConsts.TEST_CONTEXT_NAME))
        genCreator = new TestcontextModuleCreator(ns, manager, true,
            (XMIResource) res);
      else if (kind.equals(UTPConsts.DATA_PARTITION_NAME))
        genCreator = new DataPartitionModuleCreator(ns, manager, true,
            (XMIResource) res);
      else if (kind.equals(UTPConsts.DATA_POOL_NAME))
        genCreator = new DataPoolModuleCreator(ns, manager, true,
            (XMIResource) res);
      else if(kind.equals(UMLConsts.UML_INTERFACE_NAME)
          || kind.equals(UMLConsts.UML_INTERFACE_IMPL_NAME))
        genCreator = new InterfaceModuleCreator(ns, manager, true,
            (XMIResource) res);
      else if(kind.equals(UMLConsts.UML_CLASS_NAME)
          || kind.equals(UMLConsts.UML_CLASS_IMPL_NAME))
        genCreator = new ClassModuleCreator(ns, manager, true,
            (XMIResource) res);
      else
        genCreator = new NamespaceModuleCreator(ns, manager, true,
            (XMIResource) res);
      return makeModule(genCreator);
    }
    return null;
  }

  /**
   * Create ttcn3 module for this uml2 package.
   */
  protected void createModule() throws ModelProcessingException{

    if (namespace != null) {
      String name = getModuleName();     
      checkEMFModuleView(name);
      module = DeclarationGenerator.generateModuleDeclaration(name);
      if (moduleWithSameNameExists()) {
        System.out.println("A module with the same name exists. Please check the naming of the main module and UML enitites.");
        // TODO: add error handling here: see comemnt, and replace all ModelProcessingExceptions (delete class file) with ModeltransformationException.
        // also delete the parameter...error/exception
        //throw new ModelTransformationException(module, "A module with the same name exists. Please check the naming of the main module and UML enitites.");
      }
      modelObjects.add(module);
    }
  }

  private boolean moduleWithSameNameExists() {

    String currentModuleName = module.getTheName().getTheName();
    for (Object modelObject : modelObjects) {
      String checkedModuleName = ((ModuleDeclaration) modelObject).getTheName().getTheName();
      if (checkedModuleName.equals(currentModuleName)) {
        return true;
      }
    }
    return false;
  }

  protected void transform(ModuleDeclaration m) {

    UML2TTCN3Transformer umlTransformer = new UML2TTCN3Transformer(this);
    EList packagedElements = namespace.getOwnedMembers();
    for (int i = 0; i < packagedElements.size(); i++) {
      EObject obj = (EObject) packagedElements.get(i);
      umlTransformer.doSwitch(obj);
    }
  }

  /**
   * Add declaration decl to parent.
   * @param parent
   * @param decl
   */
  protected void addDeclaration(EObject parent, EObject decl){
    // add pending declarations, e.g. port types, used by test component
    for (EObject elm : pendingDeclarations){
      if(elm instanceof NamedElementDeclaration)
        addNamedElementDeclaration(parent, (NamedElementDeclaration)elm);
    }
    pendingDeclarations.clear();

    if(decl instanceof NamedElementDeclaration)
      addNamedElementDeclaration(parent, (NamedElementDeclaration)decl);
  }

  protected com.testingtech.muttcn.kernel.Expression getBasicTypeOrIdentifier(
      org.eclipse.uml2.uml.Type type) {

    com.testingtech.muttcn.kernel.Expression eType = null;
    if (type != null) {
      if (type.equals(namespace)){
        EMFModuleView view = manager.getPredefinedModule();
        if(view != null){
          this.addImport(view.getName());
        }
      }
      else {
        eType = getPrimitiveType(type.getQualifiedName());
        if (eType == null) {
          NamedElementDeclaration decl = getTTCN3Declaration(this, type);
          if (decl != null)
            eType = createIdentifier(decl.getTheName().getTheName());
          else
            eType = createIdentifier(NameMaker.createName(type));
        }
      }
    }
    return eType;
  }

  /**
   * Get reference for property type.
   * @param prop
   * @return
   */
  protected Expression getTypeReference(Property prop) {
    com.testingtech.muttcn.kernel.Expression typeRef = null;
    int upper = prop.getUpper();
    typeRef = getBaseTypeReference(prop);
    if(upper>1){
      //TODO: infinity
      if(typeRef != null){
        String baseTypeName = getTypeReferenceName(typeRef);
        String name = null;
        com.testingtech.muttcn.kernel.Expression st = null;
        if(prop.isOrdered()){
          name = NameMaker.createCompositeName(baseTypeName, TTmodelerConsts.RECORD_OF_TYPE_APPENDIX);
          st = Reducer.typeFac.createRecordOfType();
          ((RecordOfType)st).setTheType(typeRef);
        }else{
          name = NameMaker.createCompositeName(baseTypeName, TTmodelerConsts.SET_OF_TYPE_APPENDIX);
          st = Reducer.typeFac.createSetOfType();
          ((SetOfType)st).setTheType(typeRef);
        }
        TypeDeclaration decl = DeclarationGenerator.generateTypeDeclaration(name);
        decl.setTheType(st);
        addDeclaration2Module(module, decl);
        typeRef = createIdentifier(name);
      }
    }
    return typeRef;
  }

  /**
   * Get base type reference for property.
   * @param prop
   * @return
   */
  protected com.testingtech.muttcn.kernel.Expression getBaseTypeReference(Property prop){
    return getBasicTypeOrIdentifier(prop.getType());
  }

  /**
   * Get name of type reference
   * @param typeRef
   * @return
   */
  protected String getTypeReferenceName(com.testingtech.muttcn.kernel.Expression typeRef){
    String result = null;
    if(typeRef instanceof BasicType){
      result = ((BasicType)typeRef).getTheName().getTheName();
    }else if(typeRef instanceof com.testingtech.muttcn.kernel.Identifier){
      result = ((com.testingtech.muttcn.kernel.Identifier)typeRef).getTheName().getTheName();
    }
    return result;
  }


  /**
   * Transform UML element to TTCN-33 element.
   * 
   * @author mli
   */
  protected class Ttcn3Creator {

    protected GeneralModuleCreator creator;

    public Ttcn3Creator(GeneralModuleCreator creator){
      this.creator = creator;
    }

    public EObject doCreate(org.eclipse.uml2.uml.Element umlElm) {

      if (isStereotypeApplied(umlElm,
          UTPConsts.TEST_COMPONENT_NAME))
        return transformTestComponent((Class) umlElm);
      else if (umlElm instanceof Enumeration) {
        return transform((Enumeration) umlElm);
      }
      else if (umlElm instanceof Namespace){
        makeModule((Namespace) umlElm);
        return null;
      }
      else if(umlElm instanceof InstanceSpecification){
        return transform((InstanceSpecification)umlElm);
      }
      return null;
    }

    /**
     * Instance specification of class maps to template
     * @param umlElm
     * @return
     */
    public EObject transform(InstanceSpecification umlElm) {
      EList classifiers = umlElm.getClassifiers();
      // should be one classifier which is a class
      if(classifiers.size() == 1){
        Object obj = classifiers.get(0);
        if(obj instanceof Class){
          Class umlClass = (Class)obj;
          String dataTypeName = 
            NameMaker.createName(umlClass);
          NamedElementDeclaration dataType = getTTCN3Declaration(creator, 
              dataTypeName, umlClass);
          if(dataType != null && dataType instanceof TypeDeclaration){
            EList slots = umlElm.getSlots();
            String templateName = NameMaker.createName(umlElm);
            return createTemplate(templateName, (TypeDeclaration)dataType, slots);
          }
        }
      }
      return null;
    }



    /**
     * Transform UML enumeration to TTCN-3 enumerated type.
     * @param umlElm
     * @return
     */
    public EObject transform(Enumeration umlElm){
      String name = NameMaker.createName(umlElm);
      TypeDeclaration type = DeclarationGenerator.generateTypeDeclaration(name);
      EnumeratedType enumType = DeclarationGenerator.generateEnumeratedType();
      type.setTheType(enumType);
      EList lits = umlElm.getOwnedLiterals();
      for(int i=0; i<lits.size(); i++){
        EnumerationLiteral enumLiteral = (EnumerationLiteral)lits.get(i);
        String ltName = NameMaker.createName(enumLiteral);
        enumType.getTheEnumLiterals().add(DeclarationGenerator.generateEnumFieldDeclaration(ltName));
      }
      return type;
    }

    /**
     * Transform UML test component to TTCN-3 test component.
     * 
     * @param umlElm
     * @return
     */
    public EObject transformTestComponent(Class umlElm) {
      String typeName = NameMaker.createName(umlElm);
      TypeDeclaration decl = TransformationUtil.createTestComponent(umlElm, typeName);
      EList ports = createPorts(umlElm, typeName, false);
      if(decl != null && ports.size() > 0)
        ((ComponentType)decl.getTheType()).getTheFieldDeclarations().addAll(ports);
      return decl;
    }

    /**
     * Create ttcn3 port type for given uml port.
     * If inverse is true, invert in and out types of including port types that
     * are generated from ports of the uml type.
     * @param containerName
     * @param uml2Port
     * @param inverse
     * @return
     */
    protected com.testingtech.muttcn.kernel.Expression 
    createPortType(String containerName,
        org.eclipse.uml2.uml.Port uml2Port, boolean inverse){
      String name = NameMaker.createCompositeName(containerName, uml2Port.getName());
      TypeDeclaration typeDecl = 
        DeclarationGenerator.generateTypeDeclaration(name);
      ProcedurePortType portType = DeclarationGenerator.generateProcedureType();
      typeDecl.setTheType(portType);
      pendingDeclarations.add(typeDecl);
      EList dependencies = uml2Port.getClientDependencies();
      if(dependencies.size() == 0){
        org.eclipse.uml2.uml.Type uml2PortType = uml2Port.getType();
        if(uml2PortType != null){
          dependencies = uml2PortType.getClientDependencies();
        }
      }
      completePortType(inverse, portType, dependencies);
      com.testingtech.muttcn.kernel.Expression result = createIdentifier(name);
      elementMap.put(uml2Port, result);
      return result;
    }

    /**
     * Complete ttcn3 port type by resolve given dependencies which are specified
     * for the related uml port.
     * @param inverse
     * @param portType
     * @param dependencies
     */
    private void completePortType(boolean inverse, ProcedurePortType portType, EList dependencies) {

      for (Iterator iter = dependencies.iterator(); iter.hasNext();) {
        org.eclipse.uml2.uml.Dependency dependency = 
          (org.eclipse.uml2.uml.Dependency) iter.next();
        EList suppliers = dependency.getSuppliers();;
        for (Iterator iterator = suppliers.iterator(); iterator.hasNext();) {
          org.eclipse.uml2.uml.NamedElement supplier = (org.eclipse.uml2.uml.NamedElement ) iterator.next();
          if (supplier instanceof Interface) {
            String supplierName = NameMaker.createName(supplier);
            NamedElementDeclaration ttcn3Type = 
              getTTCN3Declaration(creator, 
                  supplierName, 
                  getNamespace(creator, (Interface)supplier));
            if (ttcn3Type != null
                && ttcn3Type instanceof ModuleDeclaration
                && ((ModuleDeclaration) ttcn3Type).getTheModuleValue() != null) {
              EList decls = ((ModuleDeclaration) ttcn3Type)
              .getTheModuleValue().getTheDeclarations();
              for (int i = 0; i < decls.size(); i++) {
                EObject elm = (EObject) decls.get(i);
                if (elm instanceof TypeDeclaration && 
                    ((TypeDeclaration)elm).getTheType() instanceof SignatureType) {
                  String sigName = getUniqueName(((ModuleDeclaration)ttcn3Type).getTheName().getTheName(),
                      ((TypeDeclaration) elm).getTheName()
                      .getTheName());
                  com.testingtech.muttcn.kernel.Expression sigId = createIdentifier(sigName);
                  if (dependency instanceof Usage) {
                    if (inverse)
                      portType.getTheInTypes().add(sigId);
                    else
                      portType.getTheOutTypes().add(sigId);
                  }
                  else if (dependency instanceof InterfaceRealization){
                    if (!inverse)
                      portType.getTheInTypes().add(sigId);
                    else
                      portType.getTheOutTypes().add(sigId);
                  }
                }
              }
            }
          }
        }
      }
    }

    /**
     * Check if id already imported from module with moduleName. If so,
     * make "dotted name". 
     * @param moduleName
     * @param id
     * @return
     */
    protected String getUniqueName(String moduleName, String id){
      List<String> lst = importedIdentifiers.get(moduleName);
      String result = id;
      if(lst == null){
        lst = new ArrayList<String>();
        importedIdentifiers.put(moduleName, lst);
      }
      if(lst.contains(id)){
        result = moduleName + "." + id;
      }else
        lst.add(id);
      return result;
    }

    /**
     * Create ttcn3 port declaration for given uml type.
     * If inverse is true, invert in and out types of including port types that
     * are generated from ports of the uml type.
     * @param tp
     * @param containerName
     * @param inverse
     * @return
     */
    protected EList createPorts(org.eclipse.uml2.uml.Type tp, String containerName, boolean inverse){
      EList ports = new UMLSwitch(){
        EList result = new BasicEList();
        boolean inverse = false;
        String containerName = null;
        public EList doCreate(String containerName, org.eclipse.uml2.uml.Type tp, boolean inverse){
          this.containerName = containerName;
          this.inverse = inverse;
          doSwitch(tp);
          return result;
        }

        @Override
        public Object casePort(org.eclipse.uml2.uml.Port object) {

          String name = object.getName();
          PortFieldDeclaration portDecl = 
            DeclarationGenerator.generatePortFieldDeclaration(name);

          com.testingtech.muttcn.kernel.Expression portType = null;
          EObject found = elementMap.get(object);
          if(found != null && found instanceof com.testingtech.muttcn.kernel.Expression)
            portType = (com.testingtech.muttcn.kernel.Expression)found;
          if(portType == null)
            portType = createPortType(containerName, object, inverse);
          if(portType != null)
            portDecl.setTheType(portType);
          result.add(portDecl);
          return portDecl;      
        }

        public Object defaultCase(EObject eObject) {

          for (Iterator eContents = eObject.eContents().iterator(); eContents
          .hasNext();) {
            doSwitch((EObject) eContents.next());
          }
          return super.defaultCase(eObject);
        }
      }.doCreate(containerName, tp, inverse);
      return ports;
    }

  }

  /**
   * Create parameter declaration in ttcn-3 for given uml parameter
   * @param par
   * @return
   */
  protected ParameterDeclaration createParameterDeclaration(ConstantDeclaration decl, Parameter par){
    String name = NameMaker.createName(par);
    org.eclipse.uml2.uml.Type tp = par.getType();
    if(name != null && tp != null){
      ParameterDeclaration parDecl = DeclarationGenerator
      .generateParameterDeclaration(name);
      switch(par.getDirection().getValue()){
      case ParameterDirectionKind.OUT:
        parDecl.setIsOut(Boolean.TRUE);
        break;
      case ParameterDirectionKind.INOUT:
        parDecl.setIsIn(Boolean.TRUE);
        parDecl.setIsOut(Boolean.TRUE);
        break;
      default:
        parDecl.setIsIn(Boolean.TRUE);
      }
      parDecl.setTheType(getBasicTypeOrIdentifier(tp));
      return parDecl;
    }
    return null;
  }

  /**
   * Create parameter declaration in ttcn-3 for given uml property.
   * @param decl
   * @param prop
   * @return
   */
  protected ParameterDeclaration createParameterDeclaration(
      ConstantDeclaration decl, Property prop) {

    String name = NameMaker.createName(prop);
    org.eclipse.uml2.uml.Type tp = prop.getType();
    if (name != null && tp != null) {
      ParameterDeclaration parDecl = DeclarationGenerator
      .generateParameterDeclaration(name);
      parDecl.setIsIn(Boolean.TRUE);
      parDecl.setIsOut(Boolean.TRUE);
      parDecl.setTheType(getBasicTypeOrIdentifier(tp));
      return parDecl;
    }
    return null;
  }

  /**
   * Create template delcaration for given type and instance specification slots.
   * @param name
   * @param declaration
   * @param slots
   * @return
   */
  protected EObject createTemplate(String name, TypeDeclaration declaration, EList slots) {
    ConstantDeclaration decl = DeclarationGenerator.generateConstantDeclaration(name);
    FunctionType fType = DeclarationGenerator.generateFunctionType();
    decl.setTheType(fType);
    TemplateType tType = DeclarationGenerator.generateTemplateType();
    tType.setTheType(createIdentifier(declaration.getTheName().getTheName()));
    fType.setTheToType(tType);
    FunctionValue fValue = DeclarationGenerator.generateFunctionValue();
    decl.setTheValue(fValue);
    FieldValue fv = DeclarationGenerator.generateFieldValue();
    fValue.setTheResult(fv);
    if(slots.size() > 0){
      for (int i = 0; i < slots.size(); i++) {
        FieldAssignment field = DeclarationGenerator.generateFieldAssignment();
        Slot sl = (Slot)slots.get(i);
        StructuralFeature feature = sl.getDefiningFeature();
        if(feature != null){
          String featureName = NameMaker.createName(feature);
          RecordField rField = DeclarationGenerator.generateRecordField();
          rField.setTheField((Identifier) createIdentifier(featureName));
          field.setTheField(rField);
        }

        EList slValue = sl.getValues();
        // take first value
        if(slValue.size() > 0){
          Object obj  = slValue.get(0);
          if(obj instanceof ValueSpecification){
            com.testingtech.muttcn.kernel.Expression val = transformValueSpecification((ValueSpecification)obj);
            if(val != null)
              field.setTheOperand(val);
            else{
              if(obj instanceof InstanceValue){
                InstanceSpecification ins = ((InstanceValue)obj).getInstance();
                if(ins != null){
                  Namespace insNs = ins.getNamespace();
                  String nsName = NameMaker.createName(insNs);
                  getTTCN3Declaration(this, nsName, insNs);
                  field.setTheOperand(createIdentifier(NameMaker.createName(ins)));
                }
              }
            }

          }
        }
        fv.getTheFieldAssignments().add(field);          
      }

    }
    return decl;
  }

  /**
   * Create variable declaration for given id and typename.
   * @param id
   * @param typeName
   * @return
   */
  protected VarDeclaration createVarDeclaration(String id, String typeName){
    VarDeclaration decl = DeclarationGenerator.generateVarDeclaration(id);
    decl.setTheType(DeclarationGenerator.generateType(typeName));
    return decl;
  }

  /**
   * This function replaces the UTP ITimer interface type with
   * the TTCN-3 timer type.
   * @param decl
   */
  protected void utpITImer2TTCN3Timer(VariableDeclaration decl) {

    if (decl.getTheType().getTheName().getTheName()
        .contains(UTPConsts.UTP_TIMER_INTERFACE_NAME)) {
      decl.setTheType(TransformationUtil
          .createIdentifier(TypeClass.Timer.getTtcn3String()));
      ParameterDeclaration parDecl = (ParameterDeclaration)decl;
      (parDecl).setIsIn(false);
      (parDecl).setIsOut(false);    
    }
  }

  protected class UML2TTCN3Transformer extends UMLSwitch{
    protected Ttcn3Creator ttcn3Creator = null;

    public UML2TTCN3Transformer(GeneralModuleCreator creator){
      if(ttcn3Creator == null)
        ttcn3Creator = new Ttcn3Creator(creator);
    }

    public UML2TTCN3Transformer(Ttcn3Creator ttcn3Creator){
      this.ttcn3Creator = ttcn3Creator;
    }

    public Object doSwitch(EObject eObject) {
      if(eObject instanceof org.eclipse.uml2.uml.Namespace){
        if(((org.eclipse.uml2.uml.Namespace)eObject).equals(namespace))
          return module;
      }
      Object eModelElement = elementMap.get(eObject);
      return eModelElement == null
      ? super.doSwitch(eObject)
          : eModelElement;
    }

    public Object defaultCase(EObject eObject) {

      for (Iterator eContents = eObject.eContents().iterator(); eContents
      .hasNext();) {
        doSwitch((EObject) eContents.next());
      }
      return super.defaultCase(eObject);
    }

    public Object caseClass(org.eclipse.uml2.uml.Class e) {
      org.eclipse.uml2.uml.Package package_ = e.getNearestPackage();
      if (package_ == null) {
        return super.caseClass(e);
      } else {
        EObject ttcn3Elm = ttcn3Creator.doCreate(e); 
        if (ttcn3Elm != null) {
          elementMap.put(e, ttcn3Elm);
          defaultCase(e);
          EObject parent = (EObject) doSwitch(package_);
          addDeclaration(parent, ttcn3Elm);
        }
        return e;
      }
    }

    @Override
    public Object caseEnumeration(Enumeration e) {
      Namespace namespace = e.getNamespace();
      EObject ttcn3Elm = ttcn3Creator.doCreate(e);
      if (ttcn3Elm != null) {
        elementMap.put(e, ttcn3Elm);
        defaultCase(e);
        EObject parent = (EObject) doSwitch(namespace);
        addDeclaration(parent, ttcn3Elm);
      }
      return e;

    }

    @Override
    public Object caseInstanceSpecification(InstanceSpecification e) {
      EObject ttcn3Elm = ttcn3Creator.doCreate(e);
      if (ttcn3Elm != null) {
        elementMap.put(e, ttcn3Elm);
        defaultCase(e);
        addDeclaration(module, ttcn3Elm);
      }
      return e;
    }

    @Override
    public Object caseInterface(Interface e) {
      makeModule(e);
      return e;
    }

    @Override
    public Object casePackage(Package e) {
      makeModule(e);
      return e;
    }

  }
}
