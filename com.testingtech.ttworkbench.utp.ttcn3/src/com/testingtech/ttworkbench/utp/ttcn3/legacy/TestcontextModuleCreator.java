/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2008-2012.  All Rights Reserved.
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

import static com.testingtech.ttcn.metamodel.Reducer.compFac;
import static com.testingtech.ttcn.metamodel.Reducer.makeName;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateAltAlternative;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateAltStatement;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateAltstepBehaviorType;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateAltstepBehaviorValue;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateAny;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateBinaryOperation;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateCallTimeout;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateCallValue;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateConnectStatement;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateDeactivateAltStatement;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateExceptionValue;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateFieldAssignment;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateFieldValue;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateFunctionBehaviorType;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateFunctionBehaviorValue;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateFunctionType;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateFunctionValue;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateGetPort;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateIdentifier;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateIfStatement;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateInlineTemplate;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateMapStatement;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateName;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateParameterDeclaration;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateReceived;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateRecordField;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateReplyValue;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateResultAssignment;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateSendStatement;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateSequenceValue;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateStatementBlock;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateTestcaseBehaviorType;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateTestcaseBehaviorValue;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateTupleValue;
import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateUnaryOperation;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.ARBITER_SETVERDICT_OPERATION_NAME;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.COMMENT_FORMAT_HTML;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.IMPLICIT_RETURN_VALUE_NAME;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.EXTERNAL_INIT_FUNCTION;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.RUNS_ON;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.TIMER_REFERENCE;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.VALUE_REFERENCE;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.VARIABLE_REFERENCE;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.NO_TRANSFORMATION_ERROR;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.RECOVERABLE_TRANSFORMATION_ERROR;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.TIMER;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.TYPE_ATTRIBUTE_NAME;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.VARIABLE;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.uml2.uml.AcceptCallAction;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.BehaviorExecutionSpecification;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.ConnectableElement;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.DestructionEvent;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionConstraint;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageEnd;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.MessageSort;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.ReceiveOperationEvent;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.ReplyAction;
import org.eclipse.uml2.uml.SendOperationEvent;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.Vertex;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.util.UMLSwitch;

import com.testingtech.muttcn.auxiliary.ElementWithAnnotation;
import com.testingtech.muttcn.auxiliary.ElementWithOperand;
import com.testingtech.muttcn.auxiliary.ParameterDeclaration;
import com.testingtech.muttcn.auxiliary.RecordField;
import com.testingtech.muttcn.compilerkernel.InvalidValue;
import com.testingtech.muttcn.compilerkernel.Name;
import com.testingtech.muttcn.compilerkernel.TextLocator;
import com.testingtech.muttcn.expressions.FieldAssignment;
import com.testingtech.muttcn.functionalabstractions.AltstepBehaviorValue;
import com.testingtech.muttcn.functionalabstractions.FunctionBehaviorValue;
import com.testingtech.muttcn.functionalabstractions.TestcaseBehaviorValue;
import com.testingtech.muttcn.kernel.Expression;
import com.testingtech.muttcn.kernel.Identifier;
import com.testingtech.muttcn.kernel.Statement;
import com.testingtech.muttcn.operation.BinaryOperation;
import com.testingtech.muttcn.operation.CallTimeout;
import com.testingtech.muttcn.operation.Done;
import com.testingtech.muttcn.operation.GetPort;
import com.testingtech.muttcn.operation.NullaryOperation;
import com.testingtech.muttcn.operation.Read;
import com.testingtech.muttcn.operation.ReceiveOperation;
import com.testingtech.muttcn.operation.Received;
import com.testingtech.muttcn.operation.Timeout;
import com.testingtech.muttcn.operation.UnaryOperation;
import com.testingtech.muttcn.statements.AltAlternative;
import com.testingtech.muttcn.statements.AltStatement;
import com.testingtech.muttcn.statements.AssignStatement;
import com.testingtech.muttcn.statements.CallAltStatement;
import com.testingtech.muttcn.statements.ConnectionStatement;
import com.testingtech.muttcn.statements.ConstantDeclaration;
import com.testingtech.muttcn.statements.DeactivateAltStatement;
import com.testingtech.muttcn.statements.ExecuteStatement;
import com.testingtech.muttcn.statements.IfStatement;
import com.testingtech.muttcn.statements.ModuleDeclaration;
import com.testingtech.muttcn.statements.NamedElementDeclaration;
import com.testingtech.muttcn.statements.SendStatement;
import com.testingtech.muttcn.statements.SetVerdictStatement;
import com.testingtech.muttcn.statements.StartStatement;
import com.testingtech.muttcn.statements.StatementBlock;
import com.testingtech.muttcn.statements.StopStatement;
import com.testingtech.muttcn.statements.TypeDeclaration;
import com.testingtech.muttcn.statements.ValuedElementDeclaration;
import com.testingtech.muttcn.statements.VariableDeclaration;
import com.testingtech.muttcn.types.ComponentType;
import com.testingtech.muttcn.values.CallValue;
import com.testingtech.muttcn.values.CharStringValue;
import com.testingtech.muttcn.values.ExceptionValue;
import com.testingtech.muttcn.values.FieldValue;
import com.testingtech.muttcn.values.FloatValue;
import com.testingtech.muttcn.values.ReplyValue;
import com.testingtech.muttcn.values.SequenceValue;
import com.testingtech.muttcn.values.TupleValue;
import com.testingtech.muttcn.values.constraints.InlineTemplate;
import com.testingtech.ttcn.metamodel.Annotations;
import com.testingtech.ttcn.metamodel.Reducer;
import com.testingtech.ttworkbench.core.CorePlugin;
import com.testingtech.ttworkbench.metamodel.core.exception.ModelProcessingException;
import com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator;
import com.testingtech.ttworkbench.metamodel.muttcn.generator.ValueGenerator;
import com.testingtech.ttworkbench.utp.core.StateMachineConsts;
import com.testingtech.ttworkbench.utp.core.TTCN3PredefinedConsts;
import com.testingtech.ttworkbench.utp.core.TTmodelerConsts;
import com.testingtech.ttworkbench.utp.core.UMLConsts;
import com.testingtech.ttworkbench.utp.core.UTPConsts;
import com.testingtech.ttworkbench.utp.core.exception.UTPStereotypeException;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.ExpressionConstructor;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.ValueConstructor;
import com.testingtech.ttworkbench.utp.ttcn3.umlextractor.UMLExtractionUtil;
import com.testingtech.ttworkbench.utp.ttcn3.umltransformer.AltStateTransformer;
import com.testingtech.ttworkbench.utp.ttcn3.umltransformer.TimerOpActionTransformer;
import com.testingtech.ttworkbench.utp.ttcn3.umltransformer.TimerOpMessageTransformer;

@SuppressWarnings("nls")
public class TestcontextModuleCreator extends NamespaceModuleCreator {

  // Map of function declarations to function names
  private Map<String, ConstantDeclaration> functions = new HashMap<String, ConstantDeclaration>();

  // Map of local properties to containing namespace Map<Namespace, <Map<PropertyName, Property>>
  private Map<Namespace, Map<String, Property>> localProperties = new HashMap<Namespace, Map<String, Property>>();

  //Map of local parameters to containing namespace Map<Namespace,<Map<ParameterName, Parameter>>
  private Map<Namespace, Map<String, Parameter>> localParameters = new HashMap<Namespace, Map<String, Parameter>>();

  // map of global declarations in this module
  private Map<String, EObject> globalDeclarations = new HashMap<String, EObject>();

  // pending ttcn3 statements mapped to uml element
  private Map<org.eclipse.uml2.uml.Element, com.testingtech.muttcn.kernel.Statement> pendingStatements = 
    new HashMap<org.eclipse.uml2.uml.Element, com.testingtech.muttcn.kernel.Statement>();

  // tsi main test component type for this module
  private TypeDeclaration tsiMain = null;

  // test context property kind
  public enum TestContextPropertyKind{
    Attribute, // attribute
    NonTCComposite, // property of non test component type used in composite structure
    TCComposite, // property of test component type used in composite structure
    SUTComposite // sut property used in composite structure
  }
  // properties of this test context
  Map<TestContextPropertyKind, Map<String, Property>> contextProperties = 
    new HashMap<TestContextPropertyKind, Map<String, Property>>();

  public TestcontextModuleCreator(org.eclipse.uml2.uml.Namespace namespace,
      UTP2TTCN3TransformationManager mgr, boolean doOverwrite,
      XMIResource utpResource) {

    super(namespace, mgr, doOverwrite, utpResource);
    mgr.addImport2RootModule(getModuleName());
  }

  public void dispose() {

    functions.clear();
    contextProperties.clear();
    localProperties.clear();
    localParameters.clear();
    globalDeclarations.clear();
    pendingStatements.clear();
    tsiMain = null;
    super.dispose();
  }

  /**
   * If connEnd refers to a sut component, add it to parent of tsi main.
   * @param connEnd
   */
  protected void addTSIMainParent(ConnectorEndInterpreter connEnd) {
    Property prop = connEnd.prop;
    if(prop == null)
      return;
    if(connEnd.getStereoType().equals(UTPConsts.SUT_NAME)){
      ComponentType comp = (ComponentType)tsiMain.getTheType();
      EList parents = comp.getBaseComponentTypes();
      String parentName = getTSITypeName(prop);
      boolean found = false;
      for (int i = 0; i < parents.size(); i++) {
        if(((com.testingtech.muttcn.kernel.Identifier)parents.get(i))
            .getTheName().getTheName().equals(parentName)){
          found = true;
          break;
        }
      }
      if(!found)
        comp.getBaseComponentTypes().add(createIdentifier(parentName));
    }
  }

  /**
   * Add elements to test configuration function.
   * @param connEnd
   */
  protected void addTestConfigurationElement(ConnectorEndInterpreter connEnd) {

    String sType = connEnd.getStereoType();
    if(sType.equals(UTPConsts.SUT_NAME))
      return;
    Property prop = connEnd.prop;
    if(prop == null)
      return;
    // add parameters to test config function
    ConstantDeclaration functionDecl = getTestConfigFunction();
    String varName = NameMaker.createName(prop);
    if(functionContainsParameter(functionDecl, varName))
      return;
    org.eclipse.uml2.uml.Type propType = prop.getType();
    String typeName = NameMaker.createName(propType);
    ParameterDeclaration parDecl = generateParameterDeclaration(varName);
    parDecl.setIsIn(Boolean.TRUE);
    parDecl.setTheType(getBasicTypeOrIdentifier(propType));
    parDecl.setIsOut(Boolean.TRUE);

    if(propType instanceof org.eclipse.uml2.uml.Class || 
        propType instanceof org.eclipse.uml2.uml.Interface){
      parDecl.setTheType(getBasicTypeOrIdentifier(propType));
      parDecl.setIsOut(Boolean.TRUE);
      // TODO: init object

    }   

    addParameterToFunction(functionDecl, parDecl);
  }

  /**
   * Get name for tsi. 
   * @param prop
   * @return
   */
  protected String getTSITypeName(Property prop) {
    return NameMaker.createName(prop);
  }

  /**
   * Get name for test configuration function.
   * @return
   */
  protected String getTestConfigFunctionName() {
    return TTmodelerConsts.TEST_CONFIG_FUNCTION_NAME;
  }

  protected void transform(ModuleDeclaration m) {

    UML2TTCN3Transformer umlTransformer = new UML2TTCN3Transformer(
        new Ttcn3Creator(this, m));
    EList packagedElements = namespace.getOwnedMembers();
    for (int i = 0; i < packagedElements.size(); i++) {
      EObject obj = (EObject) packagedElements.get(i);
      umlTransformer.doSwitch(obj);
    }
  }

  /**
   * Add activate and deactivate default operations.
   * @param behavior
   * @param behavior
   */
  private void addDefaultOperation(Behavior behavior,
      List<Behavior> defaultBehaviors){
    List<ConstantDeclaration> behaviorFunctions = createFunctions(behavior);
    if(behaviorFunctions.size()>0){
      for(ConstantDeclaration bf : behaviorFunctions){
        String bfName = bf.getTheName().getTheName();
        for (Behavior df : defaultBehaviors) {
          List<String> defaultAltsteps = createFunctionNames(df);
          if (defaultAltsteps.size() > 0) {
            List<String> parValues = getDefaultParameterValues(bf,
                df);
            for (String dfName : defaultAltsteps) {
              if (matchDefaultName(bfName, dfName)) {
                String varName = dfName;
                VariableDeclaration var = createDefaultVariableDeclaration(
                    dfName, varName, parValues);
                Statement st = createDefaultDeactivateStatement(varName);
                addDefaultStatements(bf, var, st);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Add default variable declaration and deactivate statement to func.
   * @param func
   * @param varDecl
   * @param st
   */
  private void addDefaultStatements(ConstantDeclaration func, 
      VariableDeclaration varDecl, Statement st){
    com.testingtech.muttcn.functionalabstractions.FunctionValue fValue =
      (com.testingtech.muttcn.functionalabstractions.FunctionValue)func.getTheValue();
    if(fValue == null){
      fValue = generateFunctionValue();
      func.setTheValue(fValue);
    }
    FunctionBehaviorValue bValue = (FunctionBehaviorValue)fValue.getTheResult();
    if (bValue != null) {
      com.testingtech.muttcn.kernel.Statement body = bValue
      .getTheBehaviorBody();
      if (body != null) {
        com.testingtech.muttcn.statements.StatementBlock block = DeclarationGenerator
        .generateStatementBlock();
        if (body instanceof StatementBlock) {
          block.getTheStatements().add(varDecl);
          block.getTheStatements().addAll(
              ((com.testingtech.muttcn.statements.StatementBlock) body)
              .getTheStatements());
          block.getTheStatements().add(st);
        }
        else {
          block.getTheStatements().add(varDecl);
          block.getTheStatements().add(body);
          block.getTheStatements().add(st);
        }
        bValue.setTheBehaviorBody(block);
      }
    }
  }

  /**
   * Add umlElm to local variables
   * @param umlElm
   */
  private void addLocalProperty(Property umlElm){
    Namespace ns = umlElm.getNamespace();
    if (ns.equals(namespace)) {
      addContextProperty(umlElm);
    }
    else {
      Map<String, Property> vars = localProperties.get(ns);
      if (vars == null) {
        vars = new HashMap<String, Property>();
        localProperties.put(ns, vars);
      }
      vars.put(umlElm.getName(), umlElm);
    }
  }

  /**
   * Add context property.
   * @param umlElm
   */
  private void addContextProperty(Property umlElm) {
    boolean isConnected = isConnected(umlElm);
    if (isConnected) {
      if (isStereotypeApplied(umlElm, UTPConsts.SUT_NAME)) 
        addContextProperty(umlElm, TestContextPropertyKind.SUTComposite);
      else if (isStereotypeApplied(umlElm.getType(), UTPConsts.TEST_COMPONENT_NAME))
        addContextProperty(umlElm, TestContextPropertyKind.TCComposite);
      else
        addContextProperty(umlElm, TestContextPropertyKind.NonTCComposite);
    }else
      addContextProperty(umlElm, TestContextPropertyKind.Attribute);
  }

  /**
   * Add context property
   * @param umlElm
   * @param kind
   */
  private void addContextProperty(Property umlElm, TestContextPropertyKind kind){
    Map<String, Property> props = contextProperties.get(kind);
    if(props == null){
      props = new HashMap<String, Property>();
      contextProperties.put(kind, props);
    }
    props.put(umlElm.getName(), umlElm);
  }

  /**
   * Add umlElm to local parameters
   * @param umlElm
   */
  private void addLocalParameter(Parameter umlElm){
    Namespace ns = umlElm.getNamespace();
    Map<String, Parameter> vars = localParameters.get(ns);
    if(vars == null){
      vars = new HashMap<String, Parameter>();
      localParameters.put(ns, vars);
    }
    vars.put(umlElm.getName(), umlElm);
  }

  /**
   * Create function with the given name.
   * @param name
   * @return
   */
  private ConstantDeclaration createFunction(String name) {

    ConstantDeclaration func = DeclarationGenerator
    .generateConstantDeclaration(name);
    com.testingtech.muttcn.types.FunctionType fType = generateFunctionType();
    fType.setTheToType(generateFunctionBehaviorType());
    func.setTheType(fType);
    com.testingtech.muttcn.functionalabstractions.FunctionValue fValue =
      generateFunctionValue();
    FunctionBehaviorValue bValue = generateFunctionBehaviorValue();
    bValue.setTheBehaviorBody(generateStatementBlock());
    fValue.setTheResult(bValue);
    func.setTheValue(fValue);
    return func;
  }

  private void addFunctionDeclaration2Module(String name,
      ConstantDeclaration func) {

    addDeclaration2Module(module, func);
    functions.put(name, func);
  }

  /**
   * Create altstep declaration.
   * @param name
   * @return
   */
  private ConstantDeclaration createAltstep(String name) {

    ConstantDeclaration func = DeclarationGenerator
    .generateConstantDeclaration(name);
    com.testingtech.muttcn.types.FunctionType fType = generateFunctionType();
    fType.setTheToType(generateAltstepBehaviorType());
    func.setTheType(fType);
    com.testingtech.muttcn.functionalabstractions.FunctionValue fValue =
      generateFunctionValue();
    AltstepBehaviorValue bValue = generateAltstepBehaviorValue();
    bValue.setTheBehaviorBody(generateStatementBlock());
    fValue.setTheResult(bValue);
    func.setTheValue(fValue);

    addFunctionDeclaration2Module(name, func);
    return func;
  }

  /**
   * Create test case with given name.
   * @param testCaseName
   * @return
   */
  private ConstantDeclaration createTestcase(String testCaseName) {

    ConstantDeclaration testCaseFunction = DeclarationGenerator
    .generateConstantDeclaration(testCaseName);
    com.testingtech.muttcn.types.FunctionType fType = generateFunctionType();
    fType.setTheToType(generateTestcaseBehaviorType());
    testCaseFunction.setTheType(fType);
    com.testingtech.muttcn.functionalabstractions.FunctionValue fValue =
      generateFunctionValue();
    TestcaseBehaviorValue bValue = generateTestcaseBehaviorValue();
    bValue.setTheBehaviorBody(generateStatementBlock());
    fValue.setTheResult(bValue);
    testCaseFunction.setTheValue(fValue);

    addFunctionDeclaration2Module(testCaseName, testCaseFunction);
    return testCaseFunction;
  }

  /**
   * Create GetPort.
   * @param decl
   * @param end
   * @return
   */
  private GetPort createGetPort(ConnectorEndInterpreter end){
    if (end != null) {
      String sType = end.getStereoType();
      GetPort getPort = generateGetPort();
      getPort.setTheName(generateName(end.port));
      if(sType.equals(UTPConsts.SUT_NAME)){
        getPort.setTheOperand(createIdentifier(TTmodelerConsts.TSI_PAR_NAME));
      }else
        getPort.setTheOperand(createIdentifier(end.part));
      return getPort;
    }
    return null;
  }

  /**
   * Create call operation.
   * @param portName
   * @param signatureName
   * @param arguments
   * @return
   */
  private Statement createCallOperation(String portName, 
      String signatureName,
      List<com.testingtech.muttcn.kernel.Expression> arguments){

    CallValue callValue = createCallValue(signatureName, arguments);

    SendStatement callStatement = generateSendStatement();
    Expression portIdentifier = generateIdentifier(portName);
    callStatement.setTheOperand(portIdentifier);
    callStatement.setTheMessage(callValue);

    CallAltStatement callAltStatement = Reducer.stmtFac.createCallAltStatement();
    callAltStatement.setTheCallMessage(callValue);

    StatementBlock callStatementBlock = generateStatementBlock();
    callStatementBlock.getTheStatements().add(callStatement);
    callStatementBlock.getTheStatements().add(callAltStatement);

    return callStatementBlock;
  }

  /**
   * Create getcall operation.
   * @param portName
   * @param signatureName
   * @param arguments
   * @return
   */
  private Statement createGetcallOperation(String portName, 
      String signatureName,
      List<com.testingtech.muttcn.kernel.Expression> arguments){
    Statement result = null;
    AltStatement altStatement = generateAltStatement();
    AltAlternative getReplyAlternative = createGetCallAltAlternative(portName,
        signatureName, arguments);

    altStatement.getTheAlternatives().add(getReplyAlternative);
    result = altStatement;    
    result = checkPreceedingPortOperation(altStatement);
    return altStatement;
  }

  private AltAlternative createGetCallAltAlternative(String portName,
      String signatureName,
      List<com.testingtech.muttcn.kernel.Expression> arguments) {

    AltAlternative alt = generateAltAlternative();
    com.testingtech.muttcn.operation.Received received = createReceived(portName, "getcall");
    alt.setTheExpression(received);

    CallValue val = createCallValue(signatureName, arguments);
    received.setTheExpectedMessage(val);

    return alt;
  }

  /**
   * Create call value.
   * @param signatureName
   * @param arguments
   * @return
   */
  private CallValue createCallValue(String signatureName,
      List<com.testingtech.muttcn.kernel.Expression> arguments){
    CallValue val = generateCallValue();
    if(signatureName != null){
      val.setTheProcedure(createIdentifier(signatureName));
      // if there are parameters, put them into the call
      if (! arguments.isEmpty()) {
        com.testingtech.muttcn.kernel.Expression constraint = getConstraint(arguments);
        if (constraint != null)
          val.setTheParameterList(constraint);
      }
      // if the method is without parameters, just add an empty parameter list
      else  {
        val.setTheParameterList(generateFieldValue());
      }

    }
    return val;
  }


  /**
   * Create reply operation
   * @param portName
   * @param signatureName
   * @param arguments
   * @param altStatement 
   * @return
   */
  private AltAlternative createGetreplyAlternative(
      String portName, 
      String signatureName,
      List<Expression> arguments,
      Expression returnValue, 
      Expression returnVar,
      Statement body) {
    Received received = generateReceived();
    received.setTheName(generateName("getreply"));
    received.setTheOperand(createIdentifier(portName));
    ReplyValue replyValue = createReplyValue(signatureName, arguments, returnValue);
    received.setTheExpectedMessage(replyValue);
    AltAlternative getReplyAltAlternative = createAltAlternative(null, received, body);
    return getReplyAltAlternative;
  }

  /**
   * Create reply operation
   * @param portName
   * @param signatureName
   * @param arguments
   * @param altStatement 
   * @return
   */
  private AltAlternative createGetCallAlternative(
      String portName, 
      String signatureName,
      Expression params,
      Statement body) {
    Received received = generateReceived();
    received.setTheName(generateName("getcall"));
    received.setTheOperand(createIdentifier(portName));
    CallValue callValue = createCallValue(signatureName, new ArrayList<Expression>());
    callValue.setTheParameterList(params);
    received.setTheExpectedMessage(callValue);
    AltAlternative result = createAltAlternative(null, received, body);
    return result;
  }


  /**
   * 
   * @param portName
   * @param signatureName
   * @param type
   * @param constraint
   * @param body
   * @return
   */
  private AltAlternative createExceptionAlternative(
      String portName, 
      String signatureName,
      Expression type,
      Expression constraint, 
      Statement body) {
    Received received = generateReceived();
    received.setTheName(generateName("catch"));
    received.setTheOperand(createIdentifier(portName));
    ExceptionValue replyValue = createExceptionValue(signatureName, type, constraint);
    received.setTheExpectedMessage(replyValue);
    AltAlternative getReplyAltAlternative = createAltAlternative(null, received, body);
    return getReplyAltAlternative;
  }

  private AltAlternative createCatchTimeoutAlternative(
      String portName, 
      Statement body) {
    CallTimeout timeout = generateCallTimeout();
    timeout.setTheOperand(createIdentifier(portName));
    AltAlternative catchTimeoutAltAlternative = createAltAlternative(null, timeout, body);
    return catchTimeoutAltAlternative;
  }


  /**
   * @param guard
   * @param event
   * @param body
   * @return
   */
  private AltAlternative createAltAlternative(Expression guard, Expression event, Statement body) {
    AltAlternative alt = generateAltAlternative();
    alt.setTheAltGuard(guard);
    alt.setTheExpression(event);
    alt.setTheAlternativeBody(body);
    return alt;
  }

  /**
   * Create reply operation.
   * @param portName
   * @param signatureName
   * @param arguments
   * @param returnValue
   * @return
   */
  private Statement createReplyOperation(String portName, String signatureName,
      List<Expression> arguments, com.testingtech.muttcn.kernel.Expression returnValue) {
    SendStatement stat = generateSendStatement();
    stat.setTheOperand(createIdentifier(portName));
    ReplyValue val = createReplyValue(signatureName, arguments, returnValue);
    stat.setTheMessage(val);
    return stat;
  }

  /**
   * Check default application to given behavior.
   * Add activate and deactivate of default altstep if applied.
   * @param behavior
   */
  private void checkDefaultApplication(Behavior behavior){
    EList dependencies = behavior.getClientDependencies();
    List<Behavior> defaultBehaviors = 
      new ArrayList<Behavior>();
    for (int i = 0; i < dependencies.size(); i++) {
      Dependency dp = (Dependency) dependencies.get(i);
      EList suppliers = dp.getSuppliers();
      for (int j = 0; j < suppliers.size(); j++) {
        Object obj = suppliers.get(j);
        if (obj instanceof Behavior
            && isStereotypeApplied((Behavior) obj,
                UTPConsts.DEFAULT_NAME)) {
          defaultBehaviors.add((Behavior) obj);
        }
      }
    }
    if(defaultBehaviors.size() > 0){
      defaultBehaviors = inverseOrder(defaultBehaviors);
      addDefaultOperation(behavior, defaultBehaviors);
    }
  }

  /**
   * Check if related preceeding port operation is pending.
   * If true, build statement block.
   * @param result
   * @param altStatement
   * @return
   */
  private Statement checkPreceedingPortOperation(AltStatement altStatement) {

    Statement result = altStatement;
    if(pendingStatements.size() == 1){
      // connect to send statement
      org.eclipse.uml2.uml.Element elm = 
        (org.eclipse.uml2.uml.Element)pendingStatements.keySet().toArray()[0];
      if(elm instanceof Message && isBlocking((Message)elm)){
        //get the statements that were created during call message processing
        Statement statement = (Statement) pendingStatements.values().toArray()[0];
        // if the statement is a pending call construct
        if (statement instanceof StatementBlock
            && ((StatementBlock) statement).getTheStatements().get(1) instanceof CallAltStatement) {
          // get the call alt statement and add the new alternative to it
          CallAltStatement callAltStatement = (CallAltStatement) ((StatementBlock) statement).getTheStatements().get(1);
          AltAlternative alternative = (AltAlternative) altStatement.getTheAlternatives().get(0);
          callAltStatement.getTheAlternatives().add(alternative);
          result = statement;          
        }
        else {
          StatementBlock block = generateStatementBlock();
          block.getTheStatements().add(statement);                
          block.getTheStatements().add(altStatement);          
          result = block;
        }
        pendingStatements.remove(elm);
      }
    }
    return result;
  }

  /**
   * Create deactivate statment.
   * @param varName
   * @return
   */
  private Statement createDefaultDeactivateStatement(String varName){
    DeactivateAltStatement st = generateDeactivateAltStatement();
    st.setTheDeactivatedAlt(createIdentifier(varName));
    return st;
  }

  /**
   * Create variable declaration for default activation.
   * @param funcName
   * @param varName
   * @return
   */
  private VariableDeclaration createDefaultVariableDeclaration(String funcName,
      String varName, List<String> parValues){
    VariableDeclaration varDecl = createVarDeclaration(varName, "default");
    BinaryOperation op = createInvocation(createIdentifier(funcName), parValues);
    UnaryOperation uop = generateUnaryOperation("activate");
    uop.setTheOperand(op);
    varDecl.setTheValue(uop);
    return varDecl;
  }

  /**
   * Create alt statement for receive event.
   * @param portName
   * @return
   */
  private com.testingtech.muttcn.operation.Received createReceived(String portName, String opName){

    com.testingtech.muttcn.operation.Received received = 
      generateReceived();
    received.setTheName(generateName(opName));
    received.setTheOperand(createIdentifier(portName));
    return received;
  }

  /**
   * Create reply value.
   * @param signatureName
   * @param arguments
   * @param returnValue
   * @return
   */
  private ReplyValue createReplyValue(String signatureName,
      List<com.testingtech.muttcn.kernel.Expression> arguments,
      com.testingtech.muttcn.kernel.Expression returnValue){
    ReplyValue val = generateReplyValue();

    if(signatureName != null){
      val.setTheProcedure(createIdentifier(signatureName));
      // if there are parameters, put them into the reply
      if (! arguments.isEmpty()) {
        com.testingtech.muttcn.kernel.Expression constraint = getConstraint(arguments);
        if (constraint != null)
          val.setTheOutput(constraint);
      }
      // if the method is without parameters, just add an empty parameter list
      else  {
        val.setTheOutput(generateFieldValue());
      } 
    }

    if(returnValue != null)
      val.setTheReply(returnValue);
    return val;
  }

  /**
   * Create reply value.
   * @param signatureName
   * @param arguments
   * @param returnValue
   * @return
   */
  private ExceptionValue createExceptionValue(String signatureName,
      Expression type, Expression constraint){
    ExceptionValue val = generateExceptionValue();

    if(signatureName != null){
      val.setTheProcedure(createIdentifier(signatureName));
      InlineTemplate template = generateInlineTemplate();
      template.setTheSupertype(type);
      if (constraint == null) {
        constraint = generateAny();
      }
      template.setTheConstraint(constraint);
      val.setTheException(template);
    }
    return val;
  }

  /**
   * Create port operation as statement.
   * @param portName
   * @param signatureName
   * @param arguments
   * @param returnValue
   * @return
   */
  private Statement createPortOperation(int kind, 
      String portName, String signatureName,
      List<com.testingtech.muttcn.kernel.Expression> arguments, 
      com.testingtech.muttcn.kernel.Expression returnValue){
    Statement result = null;
    switch(kind){
    case PORT_OP_KIND_CALL:
      result = createCallOperation(portName, signatureName,
          arguments);
      break;
    case PORT_OP_KIND_GETREPLY:
      result = createGetreplyOperation(portName, signatureName,
          arguments, returnValue);
      break;
    case PORT_OP_KIND_GETCALL:
      result = createGetcallOperation(portName, signatureName,
          arguments);
      break;
    case PORT_OP_KIND_REPLY:
      result = createReplyOperation(portName, signatureName,
          arguments, returnValue);
      break;
    }
    return result;
  }

  /**
   * Create reply operation
   * @param portName
   * @param signatureName
   * @param arguments
   * @return
   */
  private Statement createGetreplyOperation(String portName, 
      String signatureName,
      List<com.testingtech.muttcn.kernel.Expression> arguments,
      Expression returnValue){
    Statement result = null;
    AltStatement altStatement = generateAltStatement();
    AltAlternative getReplyAltAlternative = 
      createGetreplyAlternative(portName, signatureName, arguments, returnValue, null, null);
    altStatement.getTheAlternatives().add(getReplyAltAlternative);
    result = checkPreceedingPortOperation(altStatement);

    return result;
  }


  /**
   * @param umlElm
   * @return
   */
  private ConstantDeclaration createTestConfiguration() {
    String name = getTestConfigFunctionName();
    ConstantDeclaration func =  createFunction(name);
    addFunctionDeclaration2Module(name, func);
    return func;
  }

  /**
   * Create tsi main test component type.
   * @return
   */
  private TypeDeclaration createTSIMain(Connector e){
    // creat component
    TypeDeclaration decl = TransformationUtil.createTestComponent(getTSIMainTypeName());
    addDeclaration2Module(module, decl);
    // add parameter to config function
    ConstantDeclaration functionDecl = getTestConfigFunction();
    if(functionDecl != null){
      ParameterDeclaration parDecl = createTSIParameter(functionDecl, e);
      addParameterToFunction(functionDecl, parDecl);
    }
    return decl;
  }

  /**
   * Get test config function.
   * @return
   */
  private ConstantDeclaration getTestConfigFunction() {
    if(functions.get(getTestConfigFunctionName()) == null){
      createTestConfiguration();  
    }
    return (ConstantDeclaration)functions.get(getTestConfigFunctionName());
  }

  /**
   * Get name of tsi component type.
   * @return
   */
  private String getTSIMainTypeName() {
    return TTmodelerConsts.TSI_MAIN_TYPE_NAME;
  }

  /**
   * Create parameter declaration for given property that is mapped to TSI.
   * @param decl
   * @param prop
   * @return
   */
  private ParameterDeclaration createTSIParameter(ConstantDeclaration decl, Connector e){
    String name = TTmodelerConsts.TSI_PAR_NAME;
    ParameterDeclaration parDecl = DeclarationGenerator
    .generateParameterDeclaration(name);
    parDecl.setTheType(TransformationUtil
        .createIdentifier(getTSIMainTypeName()));
    parDecl.setIsIn(Boolean.TRUE);
    return parDecl;  
  }

  /**
   * Create initialization function for according variable,
   * and return its invocation.
   * @param prop
   * @return
   */
  public BinaryOperation createExternalInitFunc(
      Property prop) {

    Type tp = prop.getType();
    if (tp != null) {
      String name = getInitFunctionName(prop);
      ConstantDeclaration decl = (ConstantDeclaration)globalDeclarations.get(name);
      if (decl == null) {
        // create external function
        decl = createExternalFunction(name,
            getBasicTypeOrIdentifier(tp));
        addDeclaration2Module(module, decl);
        globalDeclarations.put(name, decl);
      }
      // create invocation
      return createInvocation(createIdentifier(name), null);
    }
    return null;
  }

  /**
   * Create setverdict statement for given value.
   * @param value
   * @return
   */
  private Statement createSetVerdictStatement(String value){
    SetVerdictStatement sv = Reducer.stmtFac.createSetVerdictStatement();
    sv.setTheOperand(createIdentifier(value));
    return sv;
  }

  /**
   * Create invocation of function.
   * @param function
   * @param parValue
   * @return
   */
  private BinaryOperation createInvocation(com.testingtech.muttcn.kernel.Expression function,
      List<String> parValues) {

    // create invocation
    BinaryOperation op = generateBinaryOperation("apply");
    op.setTheLeftOperand(function);
    TupleValue tuple = generateTupleValue();
    op.setTheRightOperand(tuple);
    if (parValues != null) {
      for (String parValue : parValues) {
        tuple.getTheComponents().add(createIdentifier(parValue));
      }
    }
    return op;
  }

  /**
   * Create invocation statement for given function and arguments.
   * @param function
   * @param parValues
   * @return
   */
  private Statement createInvocationStatement(Expression function,
      List<Expression> parValues) {

    // create invocation
    BinaryOperation op = generateBinaryOperation("apply");
    op.setTheLeftOperand(function);
    TupleValue tuple = generateTupleValue();
    op.setTheRightOperand(tuple);
    if (parValues != null) {
      for (Expression parValue : parValues) {
        tuple.getTheComponents().add(parValue);
      }
    }
    return createExecuteStatement(op);
  }

  /**
   * Get list of arguments for given message.
   * @param msg
   * @param isReply 
   * @return
   */
  private Map<String, Expression> getMessageArguments(Message msg, EList sigPars, boolean isReply) {
    Map<String, Expression> args = new HashMap<String, Expression>();
    if(msg.getArguments().size() > 0){
      EList argValues = msg.getArguments();
      // TODO: add error handling here: abort if not all parameters of the call have arguments( -1 because of the uml return 'parameter')
      //      if (argValues < sigPars.size() -1) {
      //        
      //      }
      int argIndex = 0;
      for (int i = 0; i < sigPars.size() && argIndex < argValues.size(); i++) {
        Parameter par = (Parameter) sigPars.get(i);
        int direction = par.getDirection().getValue();
        // TODO: add error handling here: if not enough arguments were specified 
        // (or is there an auto omit for this?) of if the matching fails.
        ValueSpecification arg;
        if (!isReply) {
          if (direction == ParameterDirectionKind.IN ||
              direction == ParameterDirectionKind.INOUT) {
            arg = (ValueSpecification) argValues.get(argIndex++);
          } else {
            // if OUT parameter, ignore
            continue;
          }
        } else {
          if (direction == ParameterDirectionKind.OUT ||
              direction == ParameterDirectionKind.INOUT) {
            arg = (ValueSpecification) argValues.get(argIndex++);
          } else {
            // if IN parameter, ignore
            continue;
          }
        }
        Expression varRefOrValue = null;
        if ( ! hasAnyKeyword(arg)) {
          // TODO: add error handling here: user forgot to specify the assignment
        }        
        // for variable references, include the name as identifier
        else if (arg.hasKeyword(TTmodelerConsts.VARIABLE_REFERENCE)) {
          varRefOrValue = new ExpressionConstructor().construct(arg.stringValue());
        }
        else if (arg.hasKeyword(TTmodelerConsts.VALUE_REFERENCE)) {          
          varRefOrValue = transformValueSpecification(arg);
        }
        args.put(par.getName(), varRefOrValue);
      }

    }
    return args;
  }

  private boolean isValidIdentifier(String strVal) {
    if(strVal == null || strVal.equals(""))
      return false;
    try{
      new BigDecimal(strVal);
    }catch(NumberFormatException ex){
      if(strVal.equals(VALUE_TRUE) || strVal.equals(VALUE_FALSE))
        return false;
      if(strVal.length() > 0){
        if(Character.isDigit(strVal.charAt(0)))
          return false;
      }
      return true;
    }
    return false;
  }

  /**
   * Make reference to the component type that is associated with represents.
   * 
   * @param represents
   * @return
   */
  private com.testingtech.muttcn.kernel.Expression getComponentType(
      ConnectableElement represents){
    com.testingtech.muttcn.kernel.Expression result = null;
    if(represents instanceof org.eclipse.uml2.uml.Port){
      org.eclipse.uml2.uml.Class theClass =
        ((org.eclipse.uml2.uml.Port)represents).getClass_();
      if(isStereotypeApplied(theClass,
          UTPConsts.TEST_COMPONENT_NAME)){
        result = getBasicTypeOrIdentifier(theClass);
      }
    }else if(represents instanceof Property){
      org.eclipse.uml2.uml.Type theType =
        ((org.eclipse.uml2.uml.Property)represents).getType();
      if(isStereotypeApplied(theType,
          UTPConsts.TEST_COMPONENT_NAME)){
        result = getBasicTypeOrIdentifier(theType);
      }
    }
    return result;
  }

  /**
   * Make constraint expression from argument list
   * @param list
   * @return
   */
  private com.testingtech.muttcn.kernel.Expression getConstraint(List<com.testingtech.muttcn.kernel.Expression > list){
    SequenceValue seq = generateSequenceValue();
    for(com.testingtech.muttcn.kernel.Expression v : list){
      seq.getTheElements().add(v);
    }
    return seq;
  }

  /**
   * Get parameter values for given behavior description df.
   * @param df
   * @return
   */
  private List<String> getParameterValues(Behavior df){
    List<String> result = new ArrayList<String>();
    EList pars = df.getOwnedParameters();
    for (int i = 0; i < pars.size(); i++) {
      Parameter par = (Parameter) pars.get(i);
      result.add(par.getName());
    }
    return result;
  }

  /**
   * Get parameter values for invocation of default operation based on
   * model of altstep in the scope of func.
   * @param func
   * @param altstep
   * @return
   */
  private List<String> getDefaultParameterValues(ConstantDeclaration func, 
      Behavior altstep){
    List<String> result = new ArrayList<String>();
    EList funcPars = createFunctionParameters(func);
    List<String> altPars = getParameterValues(altstep);
    if(funcPars != null){
      // include all parameters of function
      for (int i = 0; i < funcPars.size(); i++) {
        ParameterDeclaration p = (ParameterDeclaration)funcPars.get(i);
        result.add(p.getTheName().getTheName());
      }
    }
    if(altPars != null){
      // local parameters of altstep
      result.addAll(altPars);
    }
    return result;
  }

  /**
   * Create function name for given interaction name.
   * @param behavior
   * @param lifeline
   * @return
   */
  private String createInteractionFunctionName(
      Behavior behavior, Lifeline lifeline) {

    String functionName = NameMaker.createName(lifeline.getRepresents())
    + TTmodelerConsts.LIFELINE_FUNCTION_APPENDIX;
    return functionName;
  }

  /**
   * Create function declarations for given behavior.
   * @param behavior
   * @return
   */
  private List<ConstantDeclaration> createFunctions(Behavior behavior){
    List<ConstantDeclaration> result = new ArrayList<ConstantDeclaration>();

    List<String> fcts = createFunctionNames(behavior);
    for (String fn : fcts) {
      if(fn != null){
        ConstantDeclaration decl = functions.get(fn);
        if(decl != null)
          result.add(decl);
      }
    }
    return result;
  }

  private List<String> createFunctionNames(Behavior behavior){
    List<String> result = new ArrayList<String>();
    if(behavior instanceof Interaction){
      EList lifelines = ((Interaction)behavior).getLifelines();
      for (int i = 0; i < lifelines.size(); i++) {
        Lifeline lifeline = (Lifeline)lifelines.get(i);
        String functionName = NameMaker.createName(lifeline.getRepresents())
        + TTmodelerConsts.LIFELINE_FUNCTION_APPENDIX;
        if(functionName != null){
          result.add(functionName);
        }
      }
    }
    else if (behavior instanceof StateMachine) { 
      //for state machines, this is only the first function, the rest is compuited during state processing
      String functionName = NameMaker.createName(behavior)
      + StateMachineConsts.STATEMACHINE_FUNCTION_APPENDIX;
      result.add(functionName);
    }

    return result;
  }


  /**
   * Get function declarations for given behavior.
   * @param behaviors
   * @return
   */
  private List<ConstantDeclaration> getFunctions(List<Behavior> behaviors){
    List<ConstantDeclaration> result = new ArrayList<ConstantDeclaration>();
    for(Behavior behavior : behaviors){
      List<ConstantDeclaration> found = createFunctions(behavior);
      if(found.size() > 0)
        result.addAll(found);
    }
    return result;
  }

  /**
   * Get function names for given behavior
   * @param behaviors
   * @return
   */
  private List<String> getFunctionNames(List<Behavior> behaviors){
    List<String> result = new ArrayList<String>();
    for(Behavior behavior : behaviors){
      List<String> found = createFunctionNames(behavior);
      if(found.size() > 0)
        result.addAll(found);
    }
    return result;
  }

  /**
   * Get init function for given property.
   * @param prop
   * @return
   */
  protected String getInitFunctionName(Property prop) {
    return prop.getName() + TTmodelerConsts.GLOBAL_NAME_SEPARATOR
    + TTmodelerConsts.INIT_FUNC_NAME;
  }

  /**
   * Check if event is a blocking message event.
   * @param event
   * @return
   */
  private boolean isBlocking(org.eclipse.uml2.uml.Message msg) {

    boolean result = false;
    if (msg.getMessageSort().getValue() == MessageSort.SYNCH_CALL) {
      MessageEnd msgEnd = msg.getSendEvent();
      if (msgEnd instanceof MessageOccurrenceSpecification) {
        org.eclipse.uml2.uml.Event event = ((MessageOccurrenceSpecification) msgEnd)
        .getEvent();
        // blocking if return and out parameter are present
        Operation op = null;
        if (event instanceof SendOperationEvent)
          op = ((SendOperationEvent) event).getOperation();
        else if(event instanceof ReceiveOperationEvent)
          op = ((ReceiveOperationEvent)event).getOperation();
        if(op != null){
          EList pars = op.getOwnedParameters();
          for (int i = 0; i < pars.size(); i++) {
            Parameter par = (Parameter) pars.get(i);
            int direction = par.getDirection().getValue();
            if (direction == ParameterDirectionKind.INOUT
                || direction == ParameterDirectionKind.OUT
                || direction == ParameterDirectionKind.RETURN) {
              result = true;
              break;
            }
          }
        }
      }
    }
    return result;
  }


  /**
   * Return true if behaviorName matches defaultName, otherwise
   * return false.
   * @param behaviorName
   * @param defaultName
   * @return
   */
  private boolean matchDefaultName(String behaviorName, String defaultName){
    boolean result = false;
    int idx = behaviorName.lastIndexOf(TTmodelerConsts.GLOBAL_NAME_SEPARATOR);
    if(idx > -1){
      String suffix = behaviorName.substring(idx, behaviorName.length()-1);
      if(defaultName.lastIndexOf(suffix) > -1)
        result = true;
    }
    return result;
  }

  class ParameterValueException extends ModelProcessingException {

    public ParameterValueException(String parName){
      msg = PARAMETER_VALUE_EXCEPTION_MESSAGE + parName; 
    }
  }

  /**
   * Report error.
   * @param parName
   */
  private void reportParameterValueError(String parName){
    ParameterValueException ex = new ParameterValueException(parName);
    manager.reportError(ex);
  }

  class Ttcn3Creator extends NamespaceModuleCreator.Ttcn3Creator {

    protected List<VariableDeclaration> nonTCVars;
    private ModuleDeclaration moduleDeclaration;
    private boolean verdictTransformedSuccessfully;

    public Ttcn3Creator(GeneralModuleCreator creator, ModuleDeclaration m){
      super(creator);
      moduleDeclaration = m;
    }

    /**
     * Transform Connector to map statement in case both ends are instances of 
     * test components or their ports, including tsi. 
     * In case the test component instances are connected directly (by roles),
     * the default ports are used for the map statement.
     * In case at least one of the ends is not a test component or its port,
     * e.g. a property of a class, no map statement is generated.
     * 
     * @return
     */
    public EObject transform(Connector umlElm) {

      ConnectorInterpreter conn = new ConnectorInterpreter(
          umlElm);
      if(!conn.isValid)
        return null;
      ConnectionStatement decl = null;
      if (conn.kind == CONNECTION_KIND_MAP
          || conn.kind == CONNECTION_KIND_CONNECT) {
        // add sut to parent of tsi main
        addTSIMainParent(conn.source);
        addTSIMainParent(conn.target);
        // add parameters
        addTestConfigurationElement(conn.source);
        addTestConfigurationElement(conn.target);


        if (conn.kind == CONNECTION_KIND_MAP) {
          decl = generateMapStatement();
        }
        else if (conn.kind == CONNECTION_KIND_CONNECT) {
          decl = generateConnectStatement();
        }
        if (decl != null) {
          ConstantDeclaration functionDecl = functions.get(
              TTmodelerConsts.TEST_CONFIG_FUNCTION_NAME);
          GetPort port = createGetPort(conn.source);
          if (port != null) {
            decl.setTheSource(port);
          }
          port = createGetPort(conn.target);
          if (port != null) {
            decl.setTheTarget(port);
          }
          addStatementToFunction(functionDecl, decl);
        }
      }
      return decl;
    }


    /**
     * Lifeline mapped to function.
     */
    public EObject transform(Lifeline umlElm) {
      ConnectableElement represents = umlElm.getRepresents();
      if(represents == null || represents.getType() == null)
        return null;
      // only if test component instance
      if(!isStereotypeApplied(represents.getType(), UTPConsts.TEST_COMPONENT_NAME))
        return null;

      Interaction interaction = umlElm.getInteraction();
      String name = createInteractionFunctionName(interaction, umlElm);
      if(name == null)
        return null;
      ConstantDeclaration decl = null;
      if(isStereotypeApplied(interaction, UTPConsts.DEFAULT_NAME)) {
        decl = createAltstep(name);
      }
      else {
        decl = createFunction(name);
        addFunctionDeclaration2Module(name, decl);
      }
      // local properties to variables
      Map<String, Property> props = localProperties.get(interaction);
      if(props != null){
        for(Property p : props.values()){
          ValuedElementDeclaration valuedElemDecl = createVarOrTimerDeclAndInit(p);
          // TODO: add error handling here
          if (valuedElemDecl != null) {
            addStatementToFunction(decl, valuedElemDecl);
          }
        }
      }
      // properties of test context as parameters,
      // Exclude SUT properties and properties of TestComponent type used in composite structure
      Namespace interactionNs = interaction.getNamespace();
      if(interactionNs != null && isStereotypeApplied(interactionNs, UTPConsts.TEST_CONTEXT_NAME)){
        Map<String, Property> nsProps = contextProperties.get(TestContextPropertyKind.Attribute);
        if(nsProps != null){
          properties2functionDeclarationParameters(nsProps, decl);
        }
        nsProps = contextProperties.get(TestContextPropertyKind.NonTCComposite);
        if(nsProps != null){
          properties2functionDeclarationParameters(nsProps, decl);
        }
      }
      // local parameters
      Map<String, Parameter> pars = localParameters.get(interaction);
      if(pars != null){
        for(Parameter p : pars.values()){
          ParameterDeclaration parDecl = createParameterDeclaration(decl, p);
          if(parDecl != null)
            addParameterToFunction(decl, parDecl);
        }
      }

      // runs on
      com.testingtech.muttcn.kernel.Expression runson = getComponentType(represents);
      if(runson != null)
        setFunctionRunson(decl, runson);
      return decl;
    }


    public EObject transform(OccurrenceSpecification umlElm){
      if(umlElm.getEvent() instanceof DestructionEvent){
        Interaction interaction = umlElm.getEnclosingInteraction();
        EList covs = umlElm.getCovereds();
        for(int i=0; i<covs.size(); i++){
          Lifeline lifeline = (Lifeline)covs.get(i);
          if(lifeline != null){
            String funcName = createInteractionFunctionName(interaction, lifeline);
            ConnectableElement represents = lifeline.getRepresents();
            if(funcName != null && represents != null){
              ConstantDeclaration func = functions.get(funcName);
              if (func != null) {
                // if no setverdict statement was created, create a default 
                if ( ! verdictTransformedSuccessfully) {
                  // TODO: move if validate action can be used
                  createDefaultSetVerdictStatement(func);
                }
                // if one was created, reset the flag for the next message
                else {
                  verdictTransformedSuccessfully = false;
                }    
              }
            }
          }
        }
      }
      return null;
    }

    private void createDefaultSetVerdictStatement(ConstantDeclaration func) {

      Statement setVerdictStatement = createSetVerdictStatement(Reducer.PASS);

      generateComment(setVerdictStatement,
      "Default setverdict statement, in case none was explicitly modeled.");
      generateComment(setVerdictStatement,"Feel free to delete.");
      addStatementToFunction(func, setVerdictStatement);
    }


    /**
     * Message occurrences mapped to port operation.
     * @param umlElm
     * @return
     */
    public EObject transform(CombinedFragment umlElm){
      Interaction interaction = umlElm.getEnclosingInteraction();
      EList covs = umlElm.getCovereds();
      for(int i=0; i<covs.size(); i++){
        Lifeline lifeline = (Lifeline)covs.get(i);
        if(lifeline != null){
          String funcName = createInteractionFunctionName(interaction, lifeline);
          final ConnectableElement representedComponent = lifeline.getRepresents();
          if(funcName != null && representedComponent != null){
            ConstantDeclaration functionDeclaration = functions.get(funcName);
            String interactionOpName = umlElm.getInteractionOperator().getName();
            if (functionDeclaration != null 
                && interactionOpName.equals(UMLConsts.UML_ALT_FRAGMENT_OPERATOR_NAME)) {  
              transformAltInteractionOperands(umlElm, functionDeclaration, representedComponent);
            }
          }
        }
      }
      return umlElm;
    }

    private void transformAltInteractionOperands(CombinedFragment umlElm,
        ConstantDeclaration functionDeclaration, final ConnectableElement representedComponent) {

      EList<InteractionOperand> operands = umlElm.getOperands();
      List branches = new ArrayList<Statement>(operands.size());
      InteractionOperand lastOperand = operands.get(operands.size()-1);
      for (InteractionOperand operand : operands) {
        InteractionConstraint opGuard = operand.getGuard();
        OpaqueExpression guardCondition= (OpaqueExpression) opGuard.getSpecification();
        String guardConditionText = guardCondition.getBodies().get(0);
        final StatementBlock ifOrElseBranch = DeclarationGenerator.generateStatementBlock();
        EList<InteractionFragment> fragments = operand.getFragments();
        for (InteractionFragment fragment : fragments) {
          // only treat behavior fragments, because message fragments are redundant
          // and treating them produces the call several times. 
          if (fragment instanceof BehaviorExecutionSpecification) {
            BehaviorExecutionSpecification behaviorFragment = (BehaviorExecutionSpecification)fragment;
            MessageOccurrenceSpecification msg = (MessageOccurrenceSpecification) behaviorFragment.getStart();
            new UMLSwitch() {
              public Object caseMessageOccurrenceSpecification(
                  MessageOccurrenceSpecification msg) {

                return transform(msg, representedComponent, ifOrElseBranch);
              }
            }.doSwitch(msg);
          }
        }
        // if this is the first branch, generate if statement
        // if it is neither the first nor the last, generate else-if statement
        if (operand != lastOperand) {
          IfStatement ifStatement = DeclarationGenerator.generateIfStatement();
          ifStatement.setTheCondition(ValueGenerator.generateFreeText(guardConditionText));
          ifStatement.setTheThenStatement(ifOrElseBranch);
          branches.add(ifStatement);
          // otherwise generate else
        } else if (guardConditionText.equals(UMLConsts.UML_ELSE_KEYWORD)) {
          branches.add(ifOrElseBranch);
        }
        else {
          // TODO: add error handling here: missing guard condition, must be boolean expression or 'else' keyword
        }
      }
      IfStatement statement = createIfStatement(branches);
      addStatementToFunction(functionDeclaration, statement);
    }



    /**
     * Message occurrences mapped to port operation.
     * @param umlElm
     * @return
     */
    public EObject transform(MessageOccurrenceSpecification umlElm){
      Interaction interaction = umlElm.getEnclosingInteraction();
      EList covs = umlElm.getCovereds();
      for(int i=0; i<covs.size(); i++){
        Lifeline lifeline = (Lifeline)covs.get(i);
        if(lifeline != null){
          String funcName = createInteractionFunctionName(interaction, lifeline);
          ConnectableElement represents = lifeline.getRepresents();
          if(funcName != null && represents != null){
            ConstantDeclaration functionDeclaration = functions.get(funcName);
            if (functionDeclaration != null) {
              return transform(umlElm, represents, getBody(functionDeclaration));
            }
          }
        }
      }
      return umlElm;
    }

    private EObject transform(MessageOccurrenceSpecification umlElm,
        ConnectableElement component, StatementBlock statementBlock) {

      Message msg = umlElm.getMessage();
      org.eclipse.uml2.uml.Event event = umlElm.getEvent();
      if (msg != null && event != null) {
        NamedElement signature = msg.getSignature();
        String messageSignatureName = signature.getName();
        boolean isReply = false;
        isReply = isReplyMsg(msg);
        int operationKind = determineOperationKind(event, isReply);
        // determine port
        String portName = null;
        org.eclipse.uml2.uml.Property nonTCInstance = null;
        if(component instanceof org.eclipse.uml2.uml.Port) {
          portName  = component.getName();
        }
        else if(component instanceof org.eclipse.uml2.uml.Property){
          
          Element signatureParent = signature.getOwner(); 
          // the signature name should not be used here: as soon as the interface
          // is identified as arbiter, a new transformer should be called which
          // handles the setverdict and getverdict operation, new design!
          
          // make sure this signature is really from the setverdict operation of the arbiter interface
          if (isInterface(signatureParent)
              && ((Interface) signatureParent).getName().equals(UTPConsts.UTP_ARBITER_INTERFACE_NAME)
              && messageSignatureName.equals(ARBITER_SETVERDICT_OPERATION_NAME)) {
            
            // since the setverdict operation does not have a connected port,
            // simply create a 'setverdict' statement and return
            transformSetVerdictMessage(statementBlock, msg);
            return umlElm;
          }
          // same goes for all timer operations
          // this condition will be part of the Message2StatementTransformer in the new design
          else if (isUTPTimerInterface(signatureParent)) {
            Statement timerOpStatement = new TimerOpMessageTransformer().transform(msg);
            addStatementToBlock(timerOpStatement, statementBlock);
          }
          else {
            Connector conn = msg.getConnector();
            // TODO: add error handling here: if connector is null, user forgot to assign
            // the 'Connector' property to the message 
            EList ends = conn.getEnds();
            for (int k = 0; k < ends.size(); k++) {
              ConnectorEnd end = (ConnectorEnd) ends.get(k);
              if ((end.getPartWithPort() != null)
                  && end.getPartWithPort().equals(component)) {
                portName = end.getRole().getName();
                //TODO: add error handling here: if portname == null, user forgot to set the connector of the reply message arrow 
                break;
              } else if (end.getPartWithPort() == null
                  && end.getRole() != null
                  && end.getRole().equals(component)) {
                for (int m = 0; m < ends.size(); m++) {
                  ConnectorEnd t = (ConnectorEnd) ends.get(m);
                  if (t.getPartWithPort() == null
                      && t.getRole() != null
                      && t.getRole() instanceof org.eclipse.uml2.uml.Property
                      && !t.getRole().equals(component))
                    nonTCInstance = (org.eclipse.uml2.uml.Property) t
                    .getRole();
                }
              }
            }
          }
        }
        determineParamsAndReturnVal(statementBlock, msg, signature, isReply,
            operationKind, portName, nonTCInstance);
      }
      return umlElm;
    }

    private boolean isUTPTimerInterface(Element element) {
      if (isInterface(element)
          && ((Interface) element).getName().equals(UTPConsts.UTP_TIMER_INTERFACE_NAME)) {
        return true;
      }
      return false;
    }
    
    private boolean isInterface(Element signatureParent) {

      return signatureParent instanceof Interface;
    }

    private void determineParamsAndReturnVal(StatementBlock statementBlock,
        Message msg, NamedElement signature, boolean isReply,
        int operationKind, String portName,
        org.eclipse.uml2.uml.Property nonTCInstance) {

      if (signature != null && signature instanceof Operation) {
        String signatureName = NameMaker.createName(signature);
        List<com.testingtech.muttcn.kernel.Expression> argValues = 
          new ArrayList<com.testingtech.muttcn.kernel.Expression>();
        com.testingtech.muttcn.kernel.Expression returnValue = null;
        EList signaturePars = ((Operation) signature)
        .getOwnedParameters();
        Map<String, Expression> args = getMessageArguments(msg, signaturePars, isReply);
        if (false) {

        }
        // if no storage variable was defined for the return value
        //          if ( isReply && args.isEmpty()) {
        //            argValues.add(valueUnchanged);
        //          }
        else {
          // assume parameters and arguments are in the same order: par, par,..., returnValue
          // if this is a reply message, only process the return parameter
          // TODO: handling for possible out parameters
          for (int m = 0; m < (signaturePars.size()); m++) {
            com.testingtech.muttcn.kernel.Value valueUnchanged = 
              DeclarationGenerator.generateUnchanged();
            Parameter par = (Parameter) signaturePars.get(m);
            com.testingtech.muttcn.kernel.Expression value = args.get(par.getName());
            if (value != null)
              value = updateValue(par, value);

            int direction = par.getDirection().getValue();
            String parName = par.getName();
            com.testingtech.muttcn.kernel.Value valueAny = DeclarationGenerator
            .generateAny();
            if (!isReply) {
              // send
              if (value != null) {
                // value set
                if (direction == ParameterDirectionKind.RETURN) {
                  returnValue = valueUnchanged;
                  reportParameterValueError(parName);
                }
                else if (direction == ParameterDirectionKind.OUT) {
                  argValues.add(valueUnchanged);
                  reportParameterValueError(parName);
                }
                else
                  argValues.add(value);
              }
              else {
                // value not set
                if (direction == ParameterDirectionKind.RETURN) {
                  returnValue = valueUnchanged;
                }
                else if (direction == ParameterDirectionKind.OUT) {
                  argValues.add(valueUnchanged);
                }
                else {
                  // actually omit not allow for in parameters
                  argValues.add(valueUnchanged);
                  reportParameterValueError(parName);
                }
              }
            }
            else {
              // receive
              if (value != null) {
                // value set
                if (direction == ParameterDirectionKind.RETURN) {
                  returnValue = value;
                }
                else if (direction == ParameterDirectionKind.IN) {
                  argValues.add(valueUnchanged);
                  reportParameterValueError(parName);
                }
                else
                  argValues.add(value);
              }
              else {
                // value not set
                if (direction == ParameterDirectionKind.RETURN) {
                  // so far, do nothing, because an unset value is interpreted as
                  // 'no value matching shall be done on the return value'
                }
                else if (direction == ParameterDirectionKind.IN) {
                  argValues.add(valueUnchanged);
                }
                else {
                  argValues.add(valueAny);
                  // so far, do nothing, because an unset value is interpreted as
                  // 'no value matching shall be done on the return value'
                }
              }
            }
          }
        }

        Statement st = null;
        StatementBlock alternativeBody = null;
        if (portName == null){
          // non-port operation but invocation of external function
          if(nonTCInstance != null){
            String callee = NameMaker.createName(nonTCInstance.getType());
            String firstParName = NameMaker.createName(nonTCInstance);
            String opName = NameMaker.createName(signature);
            Expression function = getQualifiedName(new String[]{
                callee, opName}); 
            List<Expression> newArgs = new ArrayList<Expression>();
            newArgs.add(createIdentifier(firstParName));
            newArgs.addAll(argValues);
            st = createInvocationStatement(function, newArgs);
          }
        } 
        else {
          EList<ValueSpecification> msgArguments = msg.getArguments();
          st = createPortOperation(operationKind, portName,
              signatureName, argValues, returnValue);           
          // if there is a storage variable assignment for the return value, process it 
          if ( ! msgArguments.isEmpty() && operationKind == PORT_OP_KIND_GETREPLY) {
            if (hasKeyword(msgArguments.get(0), TTmodelerConsts.VARIABLE_REFERENCE)) {
              String returnValueStorageName = msgArguments.get(0).stringValue();
              Expression returnValueStorageExp = generateIdentifier(returnValueStorageName);
              Statement altStatement;
              List<AltAlternative> theAlternatives = new ArrayList<AltAlternative>();
              // create port operation sometimes creates an alt statement,
              // other times a statement block with an alt statement as second
              // statement, so we have to distinguish.
              if (st instanceof AltStatement) {
                altStatement = st;
                theAlternatives = ((AltStatement) st).getTheAlternatives();
              }    
              else if (st instanceof CallAltStatement) {
                altStatement = st;
                theAlternatives = ((CallAltStatement) st).getTheAlternatives();
              }
              else if (st instanceof StatementBlock){
                EList theStatements = ((StatementBlock) st).getTheStatements();
                altStatement = (CallAltStatement) theStatements.get(1);
                theAlternatives = ((CallAltStatement) altStatement).getTheAlternatives();
              }
              // if there is only one alternative, this is the getreply alternative, if there are more
              // than one, the first one is the timeout alt, and the second one is the getreply alt
              AltAlternative altAlternative = (theAlternatives.size() == 1 ? theAlternatives.get(0) : theAlternatives.get(1));
              ReceiveOperation altEvent = (ReceiveOperation)altAlternative.getTheExpression();
              ReplyValue replyValue = (ReplyValue)altEvent.getTheExpectedMessage();
              Expression portIdentifier = createIdentifier(portName);
              Statement resultAssignment = 
                generateResultAssignment(returnValueStorageExp, portIdentifier , replyValue);
              alternativeBody = (StatementBlock) altAlternative.getTheAlternativeBody();
              if (alternativeBody == null) {
                alternativeBody = generateStatementBlock();
                altAlternative.setTheAlternativeBody(alternativeBody);
              }
              alternativeBody .getTheStatements().add(resultAssignment);  

            }
          }


          if (st != null) {
            if (isBlocking(msg) && operationKind == PORT_OP_KIND_CALL) {

              // if this is a call, look for timeout comment and resolve the call timeout
              String timeout = UMLExtractionUtil.getCommentFromElement(msg, TTmodelerConsts.TIMEOUT, TTmodelerConsts.COMMENT_FORMAT_HTML);
              // if a timeout comment exists
              if (timeout != null) {
                // and it is not empty
                if ( ! timeout.equals("")) {
                  Expression timeoutAsFloat = ValueGenerator
                  .generateFloatValue(timeout);
                  // and value transformation did succeed
                  if ( ! (timeoutAsFloat instanceof InvalidValue)) {  
                    // get the call alternative statement which was created duribng the preceeding send/call message evaluation
                    CallAltStatement callAlt = (CallAltStatement) ((StatementBlock) st).getTheStatements().get(1);
                    callAlt.setTheCallTimeout(timeoutAsFloat);
                    // add timeout alternative
                    AltAlternative timeoutAlt = createCatchTimeoutAlternative(portName, null);
                    callAlt.getTheAlternatives().add(timeoutAlt);                      

                  } else {
                    ((InvalidValue) timeoutAsFloat).getReason();
                    //TODO: add error handling here: user did not use floating number characters
                  }
                }
                else {
                  //TODO: add error handling here: user forgot to fill timeout comment
                }
              }
              pendingStatements.put(msg, st);
            }
            else
              addStatementToBlock(st, statementBlock);
          }
        }
      }
    }

    /**
     * Update value in scope of given function.
     * @param interaction
     * @param par
     * @param value
     */
    private Expression updateValue(Parameter par, Expression value) {
      Expression result = value;
      if (value instanceof com.testingtech.muttcn.kernel.Identifier) {
        String id = ((com.testingtech.muttcn.kernel.Identifier) value)
        .getTheName().getTheName();
        result = createIdentifier(id);
      }
      return result;
    }



    /**
     * TODO: better comment
     * State Machine mapped to function.
     */
    public EObject transform(StateMachine sm) {

      boolean smHasProperties = false;

      ConstantDeclaration smFunction = null;

      String combinedFunctionName = NameMaker.createName(sm)
      + StateMachineConsts.STATEMACHINE_FUNCTION_APPENDIX;
      if(combinedFunctionName == null) {
        return null;
      }
      // what case is this if for? is this really a practical solution?
      // a complete state machine as one single altstep?
      if(isStereotypeApplied(sm, UTPConsts.DEFAULT_NAME)) {
        smFunction = createAltstep(combinedFunctionName);
      }
      else {
        smFunction = createFunction(combinedFunctionName);
        addFunctionDeclaration2Module(combinedFunctionName, smFunction);
      }

      final ComponentType fieldContainerComponentType;
      Name fieldContainerComponentName = null;
      List<Property> smAttributes = sm.getAttributes();


      if( ! smAttributes.isEmpty()){
        smHasProperties = true;
        // if the state machine has attribute properties, these are transformed and stored
        // into a component which is derived from the state machine's 'runsOn' component,
        // and all functions of the state machine run on that component. this saves 
        // propagating the global fields through all the sm's functions. 

        //create derived component and name it
        fieldContainerComponentType = DeclarationGenerator
        .generateComponentType(combinedFunctionName);
        String componentTypeName = getRunsOnComponentNameAsName(sm).getTheName();
        String smName = sm.getName();
        final String fieldContainerComponentNameString = computeSubComponentTypeName(componentTypeName, smName);
        fieldContainerComponentName = Reducer
        .makeName(fieldContainerComponentNameString);
        fieldContainerComponentType.setTheName(fieldContainerComponentName);

        // set the component base type for the derived component
        fieldContainerComponentType.getBaseComponentTypes()
        .add(generateIdentifier(getRunsOnComponentType(sm).getTheName()));        
        // add fields
        for (Property attribute : smAttributes) {
          // ignore the property which specifies the runson component
          if (! hasKeyword(attribute, RUNS_ON)) {
            // add timers and variables
            ValuedElementDeclaration timerOrVarDecl = createVarOrTimerDeclAndInit(attribute);
            addDeclaration2ComponentType(fieldContainerComponentType, timerOrVarDecl);
          }       
        }

        // add component type declaration to test suite module
        TypeDeclaration fieldContainerComponentTypeDecl = DeclarationGenerator
        .generateTypeDeclaration(fieldContainerComponentNameString); 
        fieldContainerComponentTypeDecl.setTheType(fieldContainerComponentType);
        addDeclaration2Module(moduleDeclaration, fieldContainerComponentTypeDecl);

      }

      // properties of test context as parameters,
      // Exclude SUT properties and properties of TestComponent type used in composite structure
      Namespace interactionNs = sm.getNamespace();
      if(interactionNs != null && isStereotypeApplied(interactionNs, UTPConsts.TEST_CONTEXT_NAME)){
        Map<String, Property> nsProps = contextProperties.get(TestContextPropertyKind.Attribute);
        if(nsProps != null){
          properties2functionDeclarationParameters(nsProps, smFunction);
        }
        nsProps = contextProperties.get(TestContextPropertyKind.NonTCComposite);
        if(nsProps != null){
          properties2functionDeclarationParameters(nsProps, smFunction);
        }
      }
      // local parameters
      Map<String, Parameter> smAttributesAsParameters = localParameters.get(sm);
      if(smAttributesAsParameters != null){
        for(Parameter p : smAttributesAsParameters.values()){
          ParameterDeclaration parDecl = createParameterDeclaration(smFunction, p);
          if(parDecl != null)
            addParameterToFunction(smFunction, parDecl);
        }
      }

      // create 'runs on' declaration for sm (= test case function)
      // if the state machine has attributes, use the derived attribute storage component
      Expression tcMainFuncRunsOn = smHasProperties ? 
          Reducer.makeIdent(fieldContainerComponentName) : computeRunsOnIdentifier(sm);

          if(tcMainFuncRunsOn != null) {
            setFunctionRunson(smFunction, tcMainFuncRunsOn);
          }

          // create a test case subfunction for every state in the state machine
          // (for now, only one region is anticipated)
          for (Region region : sm.getRegions()) {

            nameUnnamedVertices(region);

            for (Vertex v : region.getSubvertices()) {
              // do nothing for the final state
              if (v.getName().equals(StateMachineConsts.SM_FINAL_STATE_NAME)) {
                continue;
              }

              computeStateFunction(v, sm, smFunction, tcMainFuncRunsOn);
            }
          }
          return smFunction;
    }

    private String computeSubComponentTypeName(String componentTypeName,
        String smName) {

      return NameMaker.createCompositeName(smName, componentTypeName);
    }

    private void computeStateFunction(Vertex vertex, StateMachine sm,
        ConstantDeclaration smFunction, Expression tcMainFuncRunsOn) {

      String nodeName = computeStateFunctionName(vertex, sm);
      ConstantDeclaration nodeFunction = createFunction(nodeName);
      //properties2functionDeclarationParameters(props, nodeFunction);     
      // create 'runs on' declaration for test case subfunctions
      if(tcMainFuncRunsOn != null) {
        setFunctionRunson(nodeFunction, tcMainFuncRunsOn);
      }
      if (vertex instanceof State) {
        State state = (State) vertex;
        Activity doActivity = (Activity )state.getDoActivity();
        if (hasKeyword(vertex, TTmodelerConsts.ALT_TTCN3)) {
          AltStatement altStatement = new AltStateTransformer().transform(state);
          addStatementToFunction(nodeFunction, altStatement);
        }
        else if (doActivity != null) {
          transformDoActivity(state, nodeFunction, doActivity, sm);
        }       
      }
      else if (vertex instanceof Pseudostate) {
        Pseudostate pseudoState = (Pseudostate) vertex;
        if (pseudoState.getKind() == PseudostateKind.CHOICE_LITERAL) {
          IfStatement ifStatement = createIfStatement(pseudoState.getOutgoings(), sm);
          addStatementToFunction(nodeFunction, ifStatement);
        }
      }

      processCompletionTransitions(sm, smFunction, vertex, nodeFunction);
      // no separate function shall be created for the initial state.
      if (! vertex.getName().equals(StateMachineConsts.SM_INITIAL_STATE_NAME)) {
        addFunctionDeclaration2Module(nodeName, nodeFunction);
      }
    }

    private String computeStateFunctionName(Vertex vertex, StateMachine sm) {

      String stateMachineName = NameMaker.createName(sm);
      String nodeName = NameMaker.createCompositeName(stateMachineName, vertex.getName())
      + StateMachineConsts.STATE_FUNCTION_APPENDIX;
      return nodeName;
    }

    private IfStatement createIfStatement(EList<Transition> outgoings, StateMachine sm) {
      List<Statement> statements = new ArrayList<Statement>(outgoings.size());
      for (Transition alternative : outgoings) {
        // get infos from model
        Vertex altTargetState = alternative.getTarget();
        String condition = alternative.getGuard().getSpecification().stringValue();

        // generate ttcn-3
        ExecuteStatement statement = createTargetStateFunctionCall(sm, altTargetState);
        if (condition.equals(UMLConsts.UML_ELSE_KEYWORD)) {        
          statements.add(statement);
        }
        else if ( ! condition.equals("")) {
          IfStatement ifStatement = generateIfStatement();
          ifStatement.setTheCondition(ValueGenerator.generateFreeText(condition));
          ifStatement.setTheThenStatement(statement);
          statements.add(ifStatement);
        }
        else {
          // TODO: add error handling here: missing guard condition, must be boolean expression or 'else' keyword
        }
      }
      return createIfStatement(statements);
    }

    /**
     * This method builds a compound if-else statement from 
     * a list of if statements (without else branches) and
     * possibly an else statement.
     * 
     * @param statements
     * @return
     */
    private IfStatement createIfStatement(List<Statement> statements) {

      // sort the statements, so that the else statement is last
      List<Statement> sortedStatements = new ArrayList<Statement>();
      Statement elseStatement = null;
      for (Statement statement : statements) {
        if (statement instanceof IfStatement) {
          sortedStatements.add(statement);
        }
        else if (statement instanceof Statement) {
          elseStatement = statement;
        }        
      }
      if (elseStatement != null) {
        sortedStatements.add(elseStatement);
      }

      IfStatement result = null;
      IfStatement lastIf = null;
      for (Statement statement : sortedStatements) {
        if (lastIf != null) {
          lastIf.setTheElseStatement(statement);
          if (statement instanceof IfStatement) {
            lastIf = (IfStatement) statement;
          }
        } else {
          result = (IfStatement) statement;
          lastIf = result;
        }
      }
      return result;
    }

    public void generateComment(ElementWithAnnotation decl, String comment) {
      EList comments = Annotations.annotations(decl).getComments();
      TextLocator textLoc = compFac.createTextLocator();
      textLoc.setText("// " + comment);
      comments.add(textLoc);
    }

    /**
     * Create variable declaration for given property
     * 
     * @param prop
     * @return
     */
    protected ValuedElementDeclaration createVarOrTimerDeclAndInit(Property prop){
      String name = prop.getName();
      final Type tp = prop.getType();

      final ValuedElementDeclaration decl;

      if(tp != null){
        if (isTimer(prop)) {
          name = TransformationUtil.createUniqueName(name, TIMER);
          decl = DeclarationGenerator.generateTimerDeclaration(name);
          decl.setTheType(Reducer.makeBasicType(Reducer.makeName(Reducer.TIMER)));
        }
        else {
          name = TransformationUtil.createUniqueName(name, VARIABLE);
          decl = DeclarationGenerator.generateVarDeclaration(name);
          decl.setTheType(getBasicTypeOrIdentifier(tp));
        }
        // if marked for external init function
        if (prop.getKeywords().contains(EXTERNAL_INIT_FUNCTION)) {
          BinaryOperation invocation = createExternalInitFunc(prop);
          decl.setTheValue(invocation);
        }
        else {
          ValueSpecification vs = prop.getDefaultValue();
          try {
            if (vs != null) {
              if (vs.getType().getName().equals(UMLConsts.UML_PRIMITIVE_TYPE_INTEGER_NAME)) {
                decl.setTheValue(ValueGenerator.generateIntegerValue(vs.integerValue()));          
              }
              else if (vs.getType().getName().equals(UMLConsts.UML_PRIMITIVE_TYPE_BOOLEAN_NAME)) {
                decl.setTheValue(ValueGenerator.generateBooleanValue(vs.booleanValue()));          
              }
              else if (vs.getType().getName().equals(UMLConsts.UML_PRIMITIVE_TYPE_STRING_NAME)) {
                decl.setTheValue(ValueGenerator.generateCharStringValue("\"" + vs.stringValue() + "\""));          
              }
              else if (vs.getType().getName().equals(UTPConsts.UTP_TIMER_INTERFACE_NAME)) {
                decl.setTheValue(ValueGenerator.generateFloatValue(Double.parseDouble(vs.stringValue())));   
              }
              else if (vs.getType().getName().equals(TTCN3PredefinedConsts.TYPES_FLOAT)) {
                decl.setTheValue(new ValueConstructor().constructFloatValue(vs.stringValue()));   
              }
            }
          }
          catch (Exception e) {
            // TODO: add error handling here:
            // something went wrong while initializing a variable or timer 
          }
        }
        return decl;
      }
      // TODO: add error handling here
      return null;
    }



    private boolean isTimer(Property prop) {

      if (prop.getType().getName().contains(
          UTPConsts.UTP_TIMER_INTERFACE_NAME)) {
        return true;
      }
      return false;
    }

    private void transformDoActivity(State state, ConstantDeclaration nodeFunction,
        Activity doActivity, StateMachine sm) {

      for (ActivityNode node : doActivity.getNodes()) {
        if (node instanceof CallOperationAction) {
          transformCallOperationAction(state, nodeFunction, node, sm);
        }
        else if (node instanceof AcceptCallAction) {
          transformAcceptCallAction(state, nodeFunction, (AcceptCallAction) node, sm);
        }
        else if (node instanceof ReplyAction) {
          transformReplyAction(state, nodeFunction, (ReplyAction) node, sm);
        }
      }
    }

    private void transformReplyAction(State state,
        ConstantDeclaration nodeFunction, ReplyAction replyAction, StateMachine sm) {

      final Trigger triggerReplyToCall = replyAction.getReplyToCall();
      final CallEvent callEvent = (CallEvent) triggerReplyToCall.getEvent();
      final Operation operation = callEvent.getOperation();
      final String operationName = operation.getName();
      String portName = triggerReplyToCall.getPorts().get(0).getName();

      // generate a reply statement
      transformReplyAction(state, nodeFunction, replyAction, operation,
          operationName, sm, portName);       

    }

    private void transformReplyAction(State state,
        ConstantDeclaration nodeFunction, ReplyAction replyAction,
        Operation operation, String operationName, StateMachine sm, String portName) {

      //generate ttcn-3 structures.getr
      SendStatement replyStatement = Reducer.stmtFac.createSendStatement();
      ReplyValue replyValue = generateReplyValue();
      Expression operationIdentifier = generateIdentifier(operationName);
      replyValue.setTheProcedure(operationIdentifier);
      replyStatement.setTheMessage(replyValue);
      Expression portIdentifier = generateIdentifier(portName);
      replyStatement.setTheOperand(portIdentifier);
      replyStatement.setTheMessage(replyValue);
      FieldValue fieldValue = generateFieldValue();

      for (Parameter operationParam : operation.getOwnedParameters()) {
        String directionLiteral = operationParam.getDirection().getLiteral();
        if ( directionLiteral.equals(ParameterDirectionKind.RETURN_LITERAL.getName())) {
          //assign the returned reply value
          InputPin replyParam = replyAction.getReturnInformation();
          Expression assignment = TransformationUtil.transformElementWithAssignmentComment(replyParam);
          replyValue.setTheReply(assignment);
        } 
        else {
          FieldAssignment fieldAssignment = generateFieldAssignment();
          RecordField recordField = generateRecordField();
          recordField.setTheField((Identifier) generateIdentifier(operationParam.getName()));
          fieldAssignment.setTheField(recordField);
          // default case, if no assignment is specified
          fieldAssignment.setTheOperand(ValueGenerator.generateOmit());
          //assign the parameters
          for (OutputPin actionParam : replyAction.getOutputs()) {
            String actionParamName = actionParam.getName();
            if (actionParamName != null && 
                actionParamName.equals(operationParam.getName())) {              
              Expression assignment = TransformationUtil.transformElementWithAssignmentComment(actionParam);
              fieldAssignment.setTheOperand(assignment);
              break;
            }
          }
          fieldValue.getTheFieldAssignments().add(fieldAssignment);    
        } 
      }
      replyValue.setTheOutput(fieldValue);
      addStatementToFunction(nodeFunction, replyStatement);
    }

    String getComponentTypeName(String componentName) {
      Map<String, Property> nsProps = 
        contextProperties.get(TestContextPropertyKind.TCComposite);
      if (nsProps != null) {
        for (Property p : nsProps.values()) {
          if (p.getName().equals(componentName)) {
            return p.getType().getName();
          }
        }
      }
      return null;
    }

    private void transformCallOperationAction(State currentState, ConstantDeclaration nodeFunction,
        ActivityNode callOperationActionNode, StateMachine sm) {

      final CallOperationAction callOperationAction = (CallOperationAction) callOperationActionNode;
      final Operation operation = callOperationAction.getOperation();
      final String operationName = operation.getName();
      final Interface operationInterface = operation.getInterface(); 

      // if call operation is a call to the arbiter's set verdict method,
      // generate a 'set verdict' statement
      if (operationInterface.getName().equals(UTPConsts.UTP_ARBITER_INTERFACE_NAME) &&
          operationName.equals(UTPConsts.UTP_ARBITER_SETVERDICT_METHOD_NAME)) { 
        transformSetVerdictAction(nodeFunction, callOperationAction);
      } 
      // if it is a timer op call, generate the respective timerop statement
      else if (operationInterface.getName().equals(UTPConsts.UTP_TIMER_INTERFACE_NAME)) {
        Statement timerOpStatement = new TimerOpActionTransformer().transform(callOperationAction);
        addStatementToFunction(nodeFunction, timerOpStatement);
      }
      // otherwise generate a call statement
      else {
        transformCallAction(currentState, nodeFunction, callOperationAction, operation,
            operationName, sm); 
      }
    }

    private void transformCallAction(State currentState, ConstantDeclaration nodeFunction,
        final CallOperationAction callOperationAction,
        final Operation operation, final String operationName, StateMachine sm) {

      //generate ttcn-3 structures
      //first the call value
      CallValue callValue = generateCallValue();
      Expression operationIdentifier = generateIdentifier(operationName);
      callValue.setTheProcedure(operationIdentifier);
      // then the send statement
      SendStatement callStatement = generateSendStatement();
      String portName = callOperationAction.getOnPort().getName();
      Expression portIdentifier = generateIdentifier(portName);
      callStatement.setTheOperand(portIdentifier);
      callStatement.setTheMessage(callValue);
      // then the following call alt statement
      CallAltStatement callAltStatement = Reducer.stmtFac.createCallAltStatement();
      callAltStatement.setTheCallMessage(callValue);
      FieldValue fieldValue = generateFieldValue();
      StatementBlock alternativeBody = generateStatementBlock();

      for (Parameter operationParam : operation.getOwnedParameters()) {
        String directionLiteral = operationParam.getDirection().getLiteral();
        // do not collect the return value, this one will be used for getReply
        if ( ! directionLiteral.equals(ParameterDirectionKind.RETURN_LITERAL.getName())) {
          FieldAssignment fieldAssignment = generateFieldAssignment();
          RecordField recordField = generateRecordField();
          recordField.setTheField((Identifier) generateIdentifier(operationParam.getName()));
          fieldAssignment.setTheField(recordField);
          //assign the parameters
          for (InputPin actionParam : callOperationAction.getInputs()) {
            String actionParamName = actionParam.getName();
            if (actionParamName != null && /*WHY??*/ actionParamName.equals(operationParam.getName())) {
              Expression assignedParameter = TransformationUtil
              .transformElementWithAssignmentComment(actionParam);
              fieldAssignment.setTheOperand(assignedParameter);
              break;
            }
          }
          fieldValue.getTheFieldAssignments().add(fieldAssignment);    

        }
        // now process return parameter (= getreply alt)
        else {
          checkAndProcessReturnParam(currentState, callOperationAction,
              operationName, sm, callAltStatement, portName, portIdentifier,
              operationParam, alternativeBody);
        }
      }
      callValue.setTheParameterList(fieldValue);
      StatementBlock statementBlock = generateStatementBlock();
      statementBlock.getTheStatements().add(callStatement);
      statementBlock.getTheStatements().add(callAltStatement);
      addStatementToFunction(nodeFunction, statementBlock);


      checkForExceptionTransitionAndCreateAlt(currentState, sm, callAltStatement, portIdentifier, operationName);
      checkForTimeoutTransitionAndCreateAlt(currentState, sm, callAltStatement, portIdentifier, operationName);

    }

    private void transformAcceptCallAction(State currentState,
        ConstantDeclaration nodeFunction,
        final AcceptCallAction acceptCallAction, StateMachine sm) {


      EList<Trigger> triggers = acceptCallAction.getTriggers();
      for (Trigger trigger : triggers) {
        Event event = trigger.getEvent();
        if (event instanceof CallEvent) {
          CallEvent callEvent = (CallEvent) event;

          String portName = trigger.getPorts().get(0).getName();

          Operation operation = callEvent.getOperation();
          String operationName = operation.getName();
          FieldValue fieldValue = generateFieldValue();

          StatementBlock alternativeBody = generateStatementBlock();
          CallValue callValue = generateCallValue();
          callValue.setTheParameterList(fieldValue);
          AltStatement altStatement = generateAltStatement();
          altStatement.getTheAlternatives().add(createGetCallAlternative(portName, operationName, fieldValue, alternativeBody));
          addStatementToFunction(nodeFunction, altStatement);
          // find the assignments for the operation's params
          for (Parameter operationParam : operation.getOwnedParameters()) {
            String directionLiteral = operationParam.getDirection().getLiteral();
            // do not collect the return value, this one will be used for reply
            if ( ! directionLiteral.equals(ParameterDirectionKind.RETURN_LITERAL.getName())) {
              FieldAssignment fieldAssignment = generateFieldAssignment();
              RecordField recordField = generateRecordField();
              recordField.setTheField((Identifier) generateIdentifier(operationParam.getName()));
              fieldAssignment.setTheField(recordField);
              //assign the parameters
              if (directionLiteral.equals(ParameterDirectionKind.OUT_LITERAL.getName())) {
                fieldAssignment.setTheOperand(ValueGenerator.generateUnchanged());
              }
              else {
                Expression any = generateAny();
                fieldAssignment.setTheOperand(any);                   	
              }
              fieldValue.getTheFieldAssignments().add(fieldAssignment);    
            }
          }
          checkForCallTransitionAndAugmentGetCallAlt(currentState, sm, alternativeBody, trigger);

        }
      }
    }

    private void checkAndProcessReturnParam(State currentState,
        final CallOperationAction callOperationAction,
        final String operationName, StateMachine sm,
        CallAltStatement callAltStatement, String portName,
        Expression portIdentifier, Parameter operationParam,
        StatementBlock alternativeBody) {
      // first get the return param 
      Expression returnParam = null;
      returnParam = computeReturnParamIdentifier(callOperationAction,
          operationName, operationParam, returnParam);
      ArrayList arguments = new ArrayList();
      // so far matching templates are not supported
      arguments.add(DeclarationGenerator.generateAny());
      AltAlternative getReplyAlt = 
        createGetreplyAlternative(portName, operationName, arguments, null, returnParam, alternativeBody);
      // now check if something should be done with the getreply result ->
      // transition(s) with <<result>> keyword
      checkForReplyTransitionAndAugmentGetReplyAlt(currentState, sm,
          portIdentifier, alternativeBody, returnParam, getReplyAlt);
      callAltStatement.getTheAlternatives().add(getReplyAlt);          

    }

    private void checkForReplyTransitionAndAugmentGetReplyAlt(State currentState,
        StateMachine sm, Expression portIdentifier, StatementBlock alternativeBody,
        Expression returnValue, AltAlternative getReplyAlt) {

      for (Transition resultTransition : getTransitionsByKeyword(currentState, 
          StateMachineConsts.REPLY_TRANSITION_KEYWORD, StateMachineConsts.OUTGOING)) {
        ExecuteStatement funcCall;
        if (resultTransition != null) {
          Vertex targetState = resultTransition.getTarget();
          // if target is final state, do nothing
          if (! (targetState instanceof FinalState)) {
            Expression expectedMessage = 
              ((Received)getReplyAlt.getTheExpression()).getTheExpectedMessage();
            Statement resultAssignment = 
              generateResultAssignment(returnValue, portIdentifier, expectedMessage);
            alternativeBody.getTheStatements().add(resultAssignment);
            funcCall = createTargetStateFunctionCall(sm, targetState);
            alternativeBody.getTheStatements().add(funcCall);
          }
        }
      }
    }

    private void checkForCallTransitionAndAugmentGetCallAlt(State currentState,
        StateMachine sm, StatementBlock alternativeBody, Trigger trigger) {

      for (Transition transition : currentState.getOutgoings()) {
        boolean contains = false;
        for (Trigger transTrigger : transition.getTriggers()) {
          if (transTrigger.getEvent().equals(trigger.getEvent())) {
            contains = true;
            break;
          }
        }
        if (contains) {
          ExecuteStatement funcCall;
          Vertex targetState = transition.getTarget();
          // if target is final state, do nothing
          if (! (targetState instanceof FinalState)) {
            funcCall = createTargetStateFunctionCall(sm, targetState);
            alternativeBody.getTheStatements().add(funcCall);
          }
        }
      }
    }


    private void checkForExceptionTransitionAndCreateAlt(State currentState,
        StateMachine sm, CallAltStatement callAltStatement, Expression portIdentifier, String operationName) {

      for (Transition exceptionTransition : getTransitionsByKeyword(currentState, 
          StateMachineConsts.EXCEPTION_TRANSITION_KEYWORD, StateMachineConsts.OUTGOING)) {
        ExecuteStatement funcCall;

        String portName = portIdentifier.getTheName().getTheName();

        Vertex targetState = exceptionTransition.getTarget();
        // if target is final state, do nothing
        if (! (targetState instanceof FinalState)) {
          EList<Trigger> triggers = exceptionTransition.getTriggers();
          for (Trigger trigger : triggers) {
            Event event = trigger.getEvent();
            if (event instanceof SignalEvent) {
              SignalEvent exceptionEvent = (SignalEvent) event;
              Signal exceptionSignal = exceptionEvent.getSignal();
              String exceptionType = null;
              for (Property attr : exceptionSignal.getAllAttributes()) {
                if (attr.getName().equals(TYPE_ATTRIBUTE_NAME)) {
                  exceptionType = attr.getType().getName();
                }
              }
              Expression expectedException = generateIdentifier(exceptionType);
              StatementBlock alternativeBody = generateStatementBlock();
              AltAlternative catchExceptionAlt = 
                createExceptionAlternative(portName, operationName, expectedException, null, alternativeBody);
              funcCall = createTargetStateFunctionCall(sm, targetState);
              alternativeBody.getTheStatements().add(funcCall);
              callAltStatement.getTheAlternatives().add(catchExceptionAlt);
            }
          }
        }
      }
    }

    private void checkForTimeoutTransitionAndCreateAlt(State currentState,
        StateMachine sm, CallAltStatement callAltStatement, Expression portIdentifier, String operationName) {

      for (Transition timeoutTransition : currentState.getOutgoings()) {
        ExecuteStatement funcCall;
        String portName = portIdentifier.getTheName().getTheName();

        Vertex targetState = timeoutTransition.getTarget();
        // if target is final state, do nothing
        if (! (targetState instanceof FinalState)) {
          EList<Trigger> triggers = timeoutTransition.getTriggers();
          // find timeout trigger
          for (Trigger trigger : triggers) {
            // resolve timeout timespan
            for (String keyword : trigger.getKeywords()) {
              if (keyword.startsWith(UMLConsts.UML_TIMEOUT_AFTER_PREFIX)) {
                float timeout = Float.parseFloat(keyword.substring(UMLConsts
                    .UML_TIMEOUT_AFTER_PREFIX.length()));
                callAltStatement.setTheCallTimeout(ValueGenerator.generateFloatValue(timeout));
                // build alternative
                StatementBlock alternativeBody = generateStatementBlock();
                AltAlternative catchTimeoutAlt = 
                  createCatchTimeoutAlternative(portName, alternativeBody);
                funcCall = createTargetStateFunctionCall(sm, targetState);
                alternativeBody.getTheStatements().add(funcCall);
                callAltStatement.getTheAlternatives().add(catchTimeoutAlt);
                break;
              } 
            }
          }
        }
      }
    }

    private Expression computeReturnParamIdentifier(
        final CallOperationAction callOperationAction,
        final String operationName, Parameter operationParam,
        Expression returnValue) {

      for (OutputPin actionParam : callOperationAction.getOutputs()) {
        String actionParamName = actionParam.getName();
        if (actionParamName != null && actionParamName.equals(operationParam.getName())) {
          Expression assignedVariable = TransformationUtil
          .transformElementWithAssignmentComment(actionParam);
          if (assignedVariable == null) {
            returnValue = generateIdentifier(operationName + IMPLICIT_RETURN_VALUE_NAME);
          }
          returnValue = assignedVariable;
          break;
        }
      }
      return returnValue;
    }

    private Transition getNamedTransition(State state, String transitionName, int direction) {

      EList<Transition> transitions = getDirectedTransitions(state, direction);

      for (Transition trans : transitions) {
        if (trans.getName().equals(transitionName)) {
          return trans;
        }
      }

      return null;
    }

    private EList<Transition> getDirectedTransitions(State state, int direction) {

      EList<Transition> transitions = null;;

      if (direction == StateMachineConsts.INCOMING) {
        transitions = state.getIncomings();
      }
      else if (direction == StateMachineConsts.OUTGOING) {
        transitions = state.getOutgoings();
      }
      return transitions;
    }

    private Transition getTransitionByKeyword(State state, String keyword, int direction) {

      EList<Transition> transitions = getDirectedTransitions(state, direction);

      return getElementbyKeyword(keyword, transitions);
    }

    // this method can be removed after switch to new design, already copied to umlextractuionutil
    private <T extends NamedElement> T getElementbyKeyword(String keyword,
        List<T> list) {

      for (T element : list) {
        if (hasKeyword(element, keyword)) {
          return element;
        }
      }

      return null;
    }

    private List<Transition> getTransitionsByKeyword(State state, String keyword, int direction) {

      EList<Transition> transitions = getDirectedTransitions(state, direction);
      List<Transition> foundTransitions = new ArrayList<Transition>();
      for (Transition trans : transitions) {
        if (hasKeyword(trans, keyword)) {
          foundTransitions.add(trans);
        }
      }

      return foundTransitions;
    }



    /**
     * This is used for transforming state machines, where setVerdict is a CallOperationAction
     * @param nodeFunction
     * @param setVerdictOperationAction
     */
    private void transformSetVerdictAction(ConstantDeclaration nodeFunction,
        CallOperationAction setVerdictOperationAction) {

      String verdict = null;

      for (InputPin actionParam : setVerdictOperationAction.getInputs()) {
        String actionParamName = actionParam.getName();
        if (actionParamName != null && actionParamName.equals(UTPConsts.UTP_ARBITER_SETVERDICT_PARAMETER_NAME)) {
          verdict = UMLExtractionUtil
          .getCommentFromElement(actionParam, VALUE_REFERENCE,
              TTmodelerConsts.COMMENT_FORMAT_HTML);
          if (verdict == null) {
            //TODO: ad error handling here: missing verdict comment on setverdict operation
          }
          Statement setVerdictStatement = createSetVerdictStatement(verdict);
          addStatementToFunction(nodeFunction, setVerdictStatement);
          break;
        }

      }
      errorHandlingMissingVerdictComment(verdict);

    }

    /**
     *  This is used for transforming message sequence charts, where setVerdict is a Call Message
     * @param statementBlock
     * @param setVerdictOperationAction
     */
    private void transformSetVerdictMessage(StatementBlock statementBlock,
        Message callMessage) {
      ValueSpecification verdictArgument = callMessage.getArguments().get(0);
      // TODO: add error handling here: if there is no verdict argument present
      String verdict = ((CharStringValue) transformValueSpecification(verdictArgument)).getTheContent();
      if (errorHandlingMissingVerdictComment(verdict) == NO_TRANSFORMATION_ERROR) {
        Statement setVerdictStatement = createSetVerdictStatement(verdict);          
        addStatementToBlock(setVerdictStatement, statementBlock);            
      }


    }

    private int errorHandlingMissingVerdictComment(String verdict) {
      // this is not necessary anymore, because missing verdict is already caught before this call.
      // refactor when implementing error handling
      if (verdict != null) {
        //TODO: error handling here if verdict isp resent, but none of pass, fail or inconc
        verdictTransformedSuccessfully = true;
        return NO_TRANSFORMATION_ERROR;
      }
      else{
        //TODO: error handling for missing verdict comment
        CorePlugin.getDefault().eclipseLog("The setVerdict() operation was wrongly modeled.", new ModelProcessingException());
        return RECOVERABLE_TRANSFORMATION_ERROR;
      }
    }


    /**
     * create a subfunction call for every completion transition,except to the final state.
     * this will only be done for completion transitions, as all other transitions
     * belong to some context of the state and are handled in that context
     * (action, trigger etc.)
     * 
     * @param smFunction
     * @param v
     * @param nodeFunction
     */
    private void processCompletionTransitions(StateMachine sm,
        ConstantDeclaration smFunction, Vertex v,
        ConstantDeclaration nodeFunction) {

      // don't process transitions of choice-points. this special case has already
      // been handled.
      if (v instanceof Pseudostate
          && ((Pseudostate) v).getKind() == PseudostateKind.CHOICE_LITERAL) {
        return;
      }

      for (Transition t : v.getOutgoings()) {
        // only do this if it is a completion transition without keywords
        if (t.getTriggers().isEmpty() && ! hasAnyKeyword(t)) {
          if (t.getGuard() == null) {
            // if it is a simple transition without guard
            Vertex targetState = t.getTarget();
            if (targetState instanceof FinalState) {
              continue;
            }
            ExecuteStatement invocationExecuteStatement = createTargetStateFunctionCall(
                sm, targetState);
            if (v.getName().equals(StateMachineConsts.SM_INITIAL_STATE_NAME)) {
              addStatementToFunction(smFunction, invocationExecuteStatement);
            } else {
              addStatementToFunction(nodeFunction, invocationExecuteStatement);
            }
          }
          // if the transition has a guard
          else {
            BasicEList tList = new BasicEList();
            tList.add(t);
            IfStatement ifStatement = createIfStatement(tList, sm);
            addStatementToFunction(nodeFunction, ifStatement);
          }
        }
      }
    }


    private ExecuteStatement createTargetStateFunctionCall(StateMachine sm, Vertex targetState) {

      String targetStateFunctionName = computeStateFunctionName(targetState, sm);
      // create invocation with name and global parameters
      Expression invocationIdentifier = createIdentifier(targetStateFunctionName);
      Expression targetStateFunctionInvocation = createInvocation(
          invocationIdentifier, new ArrayList());
      ExecuteStatement invocationExecuteStatement =
        createExecuteStatement(targetStateFunctionInvocation);

      return invocationExecuteStatement;
    }



    private void nameUnnamedVertices(Region region) {

      int unnamedChoiceStateCounter = 1;
      int unnamedSimpleStateCounter = 1;
      for (Vertex v : region.getSubvertices()) {
        String vertexName = v.getName();
        if (vertexName == null || vertexName.equals("")) {
          if (v instanceof Pseudostate) {
            if (((Pseudostate)v).getKind().equals(PseudostateKind.CHOICE_LITERAL)){
              v.setName(StateMachineConsts.SM_UNNAMED_CHOICE_NAME
                  + unnamedChoiceStateCounter++);
            }
            if (((Pseudostate)v).getKind().equals(PseudostateKind.INITIAL_LITERAL)){
              v.setName(StateMachineConsts.SM_INITIAL_STATE_NAME);
            }
          }
          else if (v instanceof FinalState) {
            v.setName(StateMachineConsts.SM_FINAL_STATE_NAME);
          }
          else if (v instanceof State) {
            v.setName(StateMachineConsts.SM_UNNAMED_STATE_NAME 
                + unnamedSimpleStateCounter++);
          }
        }
      }
    }

    /**
     * add the sm attribute properties as parameters. since they are global in the state
     * machine context, they must be accessible in every state subfunction.
     * @param props
     * @param function
     */
    private void properties2functionDeclarationParameters(
        Map<String, Property> props, ConstantDeclaration function) {

      for(Property p : props.values()){
        ParameterDeclaration parDecl = null;
        parDecl = createParameterDeclaration(function, p);
        if(parDecl != null) {
          // replace UTP ITimer interface type with TTCN-3 timer type
          utpITImer2TTCN3Timer(parDecl);
          addParameterToFunction(function, parDecl);
        }
      }
    }

    private Expression computeRunsOnIdentifier(StateMachine sm) {

      Name runsOnName = getRunsOnComponentNameAsName(sm);
      Identifier runsOnIdent = Reducer.makeIdent(runsOnName);
      return runsOnIdent;

    }

    private Name getRunsOnComponentNameAsName(StateMachine sm) {

      String runsOnString = null;

      for (Property p : sm.getAttributes()) {
        if (hasKeyword(p, RUNS_ON)) {
          runsOnString = p.getName();
          break;
        }
      }

      if (runsOnString != null) {
        Name runsOnName = Reducer.makeName(runsOnString);
        return runsOnName;
      }
      //TODO: add error handling here -> runsON component was not defined in model
      return null;
    }

    private Name getRunsOnComponentType(StateMachine sm) {

      String runsOnType = null;

      for (Property p : sm.getAttributes()) {
        if (hasKeyword(p, RUNS_ON)) {
          runsOnType = p.getType().getName();
          break;
        }
      }

      if (runsOnType != null) {
        Name runsOnName = Reducer.makeName(runsOnType);
        return runsOnName;
      }
      //TODO: add error handling here -> runsON component was not defined in model
      return null;
    }

    /**
     * Collect parameters.
     * @param umlElm
     * @return
     */
    public EObject transform(Parameter umlElm) {
      Namespace ns = umlElm.getNamespace();
      if(ns instanceof Operation){
        org.eclipse.uml2.uml.Type tp = umlElm.getType();
        if (tp != null) {
          com.testingtech.muttcn.kernel.Expression typeRef = getBasicTypeOrIdentifier(tp);
          if (typeRef != null) {
            String name = TTmodelerConsts.RETURN_PARAM_NAME;
            if (umlElm.getDirection() != ParameterDirectionKind.RETURN_LITERAL){
              Operation op = umlElm.getOperation();
              EObject obj = elementMap.get(op);
              if(obj != null && obj instanceof ConstantDeclaration)
                name = NameMaker.createName(umlElm);
            }
            if(name != null){
              ParameterDeclaration decl = createParameterDeclaration(
                  typeRef, name, umlElm.getDirection());
              return decl;
            }
          }
        }
      }else
        addLocalParameter(umlElm);
      return null;
    }

    /**
     * Property of test context can be used by different parts, remember it
     * first.
     * 
     * @param umlElm
     * @return
     */
    public EObject transform(Property umlElm) {
      addLocalProperty(umlElm);
      return null;
    }

    /**
     * Transform sut to extended type component.
     * 
     * @return
     */
    public EObject transformSUT(Property umlElm) {

      org.eclipse.uml2.uml.Type tp = umlElm.getType();
      addLocalProperty(umlElm);
      if (tp != null) {
        String prefix = TTmodelerConsts.TSI_COMP_TYPE_NAME_PRF;
        String sutTypeName = getTSITypeName(umlElm);
        TypeDeclaration decl = TransformationUtil.createTestComponent(tp, sutTypeName);
        EList ports = createPorts(tp, prefix, true);
        if(decl != null && ports.size() > 0)
          ((ComponentType)decl.getTheType()).getTheFieldDeclarations().addAll(ports);
        return decl;
      }
      return null;
    }

    /**
     * 'Transform operation to test case.
     * @param operation
     * @return
     */
    public EObject transformTestcase(Operation operation) {
      Namespace ns = operation.getNamespace();
      if(!isStereotypeApplied(ns, UTPConsts.TEST_CONTEXT_NAME))
        return null;
      TypeDeclaration mtc = getMTC();
      TypeDeclaration tsiMain = getTSIMain();
      if (mtc != null && tsiMain != null) {
        String tcName = NameMaker.createName(operation);
        ConstantDeclaration decl = createTestcase(tcName);
        setFunctionRunson(decl,
            createIdentifier(TTmodelerConsts.DEFAULT_MTC_TYPE_NAME));
        setTestcaseSystem(decl, createIdentifier(getTSIMainTypeName()));
        functions.put(tcName, decl);

        // add parameters for all properties 
        // of test context which are not used for composite structure
        List<ParameterDeclaration> localPars = new ArrayList<ParameterDeclaration>();
        Map<String, Property> nsProps = contextProperties.get(TestContextPropertyKind.Attribute);
        if(nsProps != null){
          for(Property p : nsProps.values()){
            ParameterDeclaration parDecl = null;
            parDecl = createParameterDeclaration(decl, p);
            if(parDecl != null){
              addParameterToFunction(decl, parDecl);
              localPars.add(parDecl);
            }
          }
        }
        EList methods = operation.getMethods();
        // take the first - assume one behavior description
        if (methods.size() > 0) {
          nonTCVars = new ArrayList<VariableDeclaration>();
          nsProps = contextProperties.get(TestContextPropertyKind.NonTCComposite);
          if (nsProps != null) {
            for (Property p : nsProps.values()) {
              org.eclipse.uml2.uml.Type type = p.getType();
              VariableDeclaration varDecl = (VariableDeclaration) createVarOrTimerDeclAndInit(p);
              nonTCVars.add(varDecl);
              addStatementToFunction(decl, varDecl);
              if (type instanceof org.eclipse.uml2.uml.Class) {
                String typeName = NameMaker.createName(type);
                if(typeName != null){
                  GeneralModuleCreator cr = manager.getTransformers().get(typeName);
                  if(cr != null && cr instanceof InterfaceModuleCreator){
                    String initFuncName = ((InterfaceModuleCreator)cr).getInitFunctionName();
                    BinaryOperation invocation = createInvocation(
                        getQualifiedName(new String[]{
                            typeName, initFuncName
                        }), null);
                    varDecl.setTheValue(invocation);
                  }
                }
              }
            }
          }
          Map<Property, VariableDeclaration> tcVars = new HashMap<Property, VariableDeclaration>();
          nsProps = contextProperties.get(TestContextPropertyKind.TCComposite);
          if (nsProps != null) {
            for (Property p : nsProps.values()) {
              VariableDeclaration varDecl = (VariableDeclaration) createVarOrTimerDeclAndInit(p);
              addStatementToFunction(decl, varDecl);                
              tcVars.put(p, varDecl);
            }
          }
          // config function
          Statement callConfigFunc = createConfigFunctionCall(tcVars.values());
          addStatementToFunction(decl, callConfigFunc);


          // invocation of test component function
          List<ConstantDeclaration> tcFunctions = new ArrayList<ConstantDeclaration>();
          for (int i = 0; i < methods.size(); i++) {
            Behavior behavior = (Behavior) methods.get(i);
            List<ConstantDeclaration> found = createFunctions(behavior);
            if(found.size()>0)
              tcFunctions.addAll(found);
          }
          for(ConstantDeclaration tcFunction : tcFunctions){
            // assume tcFunction requires parameters which are localPars + nonTCVars
            List<String> parValues = new ArrayList<String>();
            if(localPars != null){
              for(ParameterDeclaration p : localPars)
                parValues.add(p.getTheName().getTheName());
            }
            if(nonTCVars != null){
              for(VariableDeclaration v : nonTCVars)
                parValues.add(v.getTheName().getTheName());
            }
            Statement invocationTCFunc = createTCFunctionCall(tcFunction, tcVars, parValues); 
            if(invocationTCFunc != null)
              addStatementToFunction(decl, invocationTCFunc);
            // done operation for waiting termination of tc
            AltStatement altStatement = generateAltStatement();
            AltAlternative alt = generateAltAlternative();
            alt.setTheExpression(createDoneOperation(Reducer.ALL_COMPONENT));
            altStatement.getTheAlternatives().add(alt);
            if(altStatement != null)
              addStatementToFunction(decl, altStatement);

          }
        }
        return decl;
      }
      return null;
    }

    /**
     * Create tc function invocation.
     * @param tcFunction
     * @param tcVars
     * @param parValues
     * @return
     */
    private Statement createTCFunctionCall(ConstantDeclaration tcFunction, 
        Map<Property, VariableDeclaration> tcVars, List<String> parValues) {
      StartStatement tcFunctionCall = null;
      com.testingtech.muttcn.kernel.Expression runson = getFunctionRunson(tcFunction);
      if(runson != null && runson instanceof com.testingtech.muttcn.kernel.Identifier){
        String tcRef = ((com.testingtech.muttcn.kernel.Identifier)runson).getTheName().getTheName();
        Object[] keyObjects = tcVars.keySet().toArray();
        for(int i=0; i<keyObjects.length; i++){
          Property prop = (Property)keyObjects[i];
          Type propType = prop.getType();
          String tcFunctionName = tcFunction.getTheName().getTheName();
          if(propType != null /*&& tcRef.endsWith(componentTypeName)*/) {
            // type checking for <<test component>> stereotype type of 'runsOn' target
            List<Stereotype> stereotypeList = propType.getAppliedStereotypes() ;
            boolean hasTCStereotype = false;
            for (Stereotype st : stereotypeList) {
              if (st.getName().equals(UTPConsts.TEST_COMPONENT_NAME)) {
                hasTCStereotype = true;
                break;
              }
            }
            if ( ! hasTCStereotype){
              // TODO: add error handling here -> modeltransformation exception
              CorePlugin.log(
                  new UTPStereotypeException(propType, "TestComponent"));
            }
            else {
              // set the type and create statement for test case container component
              VariableDeclaration var = tcVars.get(prop);
              var.setTheType(generateIdentifier(tcRef));
              UnaryOperation createOperation = generateUnaryOperation(Reducer.CREATE);
              TupleValue tuple = generateTupleValue();
              tuple.getTheComponents().add(createIdentifier(tcRef));
              createOperation.setTheOperand(tuple);
              var.setTheValue(createOperation);     


              com.testingtech.muttcn.kernel.Expression operand = createIdentifier(var.getTheName().getTheName());
              tcFunctionCall = Reducer.stmtFac.createStartStatement();
              com.testingtech.muttcn.kernel.Expression value = createInvocation(
                  createIdentifier(tcFunctionName), parValues);
              tcFunctionCall.setTheOperand(operand);
              tcFunctionCall.setTheWith(value);
              break;
            }
          }     
        }
      }
      return tcFunctionCall;
    }

    /**
     * Create invocation of config function with given list of local variables.
     * @param localVars
     * @return
     */
    private Statement createConfigFunctionCall(Collection<VariableDeclaration> localVars) {
      ConstantDeclaration configFunc = getTestConfigFunction();
      List<String> parValues = new ArrayList<String>();
      //first parameter is system
      parValues.add("system");
      EList funcPars = createFunctionParameters(configFunc);
      for (int i = 0; i < funcPars.size(); i++) {
        ParameterDeclaration p = (ParameterDeclaration)funcPars.get(i);
        for (VariableDeclaration var : localVars) {
          if(p.getTheType().equals(var.getTheType())){
            parValues.add(var.getTheName().getTheName());
          }
        }
      }
      BinaryOperation op = createInvocation(createIdentifier(configFunc.getTheName().getTheName()), parValues);
      return createExecuteStatement(op);
    }

    /**
     * Transform operation to external function.
     * @param operation
     * @return
     */
    public EObject transform(Operation operation) {

      String name = NameMaker.createName(operation);
      return createExternalFunction(name, null);
    }

    public EObject doCreate(org.eclipse.uml2.uml.Element umlElm) {

      EObject result = null;
      if (umlElm instanceof Property &&
          isStereotypeApplied(umlElm, UTPConsts.SUT_NAME))
        result = transformSUT((Property) umlElm);
      else if(umlElm instanceof Operation &&
          isStereotypeApplied(umlElm, UTPConsts.TEST_CASE_NAME)
          // don't transform test cases set to 'private' in the model
          && ((Operation) umlElm).getVisibility() 
          != VisibilityKind.get(VisibilityKind.PRIVATE))
        result = transformTestcase((Operation) umlElm);
      else {
        if (umlElm instanceof Property) {
          result = transform((Property) umlElm);
        }else if(umlElm instanceof Operation)
          result = transform((Operation) umlElm);
        else if (umlElm instanceof Connector) {
          result = transform((Connector) umlElm);
        }else if (umlElm instanceof Lifeline) {
          result = transform((Lifeline) umlElm);
        }else if (umlElm instanceof StateMachine
            // don't transform test behavior (= tes case content)
            // set to 'private' in the model
            && ((StateMachine) umlElm).getVisibility() 
            != VisibilityKind.get(VisibilityKind.PRIVATE)) {
          result = transform((StateMachine) umlElm);
        }else if (umlElm instanceof Parameter) {
          result = transform((Parameter) umlElm);
        }else if (umlElm instanceof MessageOccurrenceSpecification) {
          result = transform((MessageOccurrenceSpecification) umlElm);
        }else if (umlElm instanceof OccurrenceSpecification) {
          result = transform((OccurrenceSpecification) umlElm);
        }else if (umlElm instanceof CombinedFragment) {
          result = transform((CombinedFragment) umlElm);
        }
      }
      if (result == null)
        result = super.doCreate(umlElm);
      return result;
    }

  }

  class UML2TTCN3Transformer extends
  NamespaceModuleCreator.UML2TTCN3Transformer {

    public UML2TTCN3Transformer(Ttcn3Creator ttcn3Creator) {

      super(ttcn3Creator);
    }


    @Override
    public Object caseCombinedFragment(CombinedFragment e) {
      ttcn3Creator.doCreate(e);
      return e;
    }


    @Override
    public Object caseConnector(Connector e) {
      if(tsiMain == null)
        tsiMain = createTSIMain(e);
      Namespace namespace = e.getNamespace();
      if (!(namespace instanceof org.eclipse.uml2.uml.Class)) {
        return super.caseConnector(e);
      }
      else {
        ttcn3Creator.doCreate(e);
        return e;
      }
    }

    public Object caseInteraction(Interaction e) {
      defaultCase(e);
      checkDefaultApplication(e);
      removeLocalParameters(e);
      return e;
    }

    public Object caseStateMachine(StateMachine e) {
      EObject ttcn3Elm = ttcn3Creator.doCreate(e);        
      if (ttcn3Elm != null && ttcn3Elm instanceof ConstantDeclaration) {
        elementMap.put(e, ttcn3Elm);
      }
      return e;
    }


    @Override
    public Object caseLifeline(Lifeline e) {
      EObject ttcn3Elm = ttcn3Creator.doCreate(e);        
      if (ttcn3Elm != null && ttcn3Elm instanceof ConstantDeclaration) {
        elementMap.put(e, ttcn3Elm);
      }
      return e;
    }


    @Override
    public Object caseRegion(Region e) {
      EObject ttcn3Elm = ttcn3Creator.doCreate(e);        
      if (ttcn3Elm != null && ttcn3Elm instanceof ConstantDeclaration) {
        elementMap.put(e, ttcn3Elm);
      }
      return e;
    }

    @Override
    public Object caseMessageOccurrenceSpecification(MessageOccurrenceSpecification e) {
      Message eventMessage = e.getMessage();
      // possibly add error handling here. one case where the message is always null is for the setverdict
      // second message occurrence specification error, which we delete from the model.
      if (eventMessage == null) {
        return e;
      }
      //TODO: add error handling here: if getSignature is null, the designer forgot to set an operation
      // in the operationEvent of the message.
      String signatureName = eventMessage.getSignature().getName();
      // if the message belongs to an operation which executes on the test component istself
      // (like 'setverdict', timer operations etc.), ignore the receive event if there is any.
      // we only need the (self)send event.
      // important: i make use of lazy evaluation here: isReceiveOp() can be null,
      // but never if it is not a self operation, so the order of the conditions needs
      // to be kept in this 'if'.
      if (isSelfOperation(signatureName) && isReceiveOperationEvent(e)){
        return e;
      }      
      ttcn3Creator.doCreate(e); 
      return e;
    }

    /**
     * this method checks if the operation of a call is an operation being executed on the test component.
     * @param signatureName
     * @return
     */
    private boolean isSelfOperation(String signatureName) {
      
      if (signatureName.equals(ARBITER_SETVERDICT_OPERATION_NAME) ||
          signatureName.equals(UTPConsts.TIMER_READ_OPERATION_NAME) ||
          signatureName.equals(UTPConsts.TIMER_RUNNING_OPERATION_NAME) ||
          signatureName.equals(UTPConsts.TIMER_START_OPERATION_NAME) ||
          signatureName.equals(UTPConsts.TIMER_STOP_OPERATION_NAME) ||
          signatureName.equals(UTPConsts.TIMER_TIMEOUT_OPERATION_NAME)) {        
        return true; 
      }
      return false;
    }
    
    private boolean isReceiveOperationEvent(MessageOccurrenceSpecification e) {

      // check if the 'ReceiveOperationEvent' interface is implemented
      Class[] interfaces = e.getEvent().getClass().getInterfaces();
      for (Class i : interfaces) {
        if (i.getSimpleName().equals(ReceiveOperationEvent.class.getSimpleName())) {
          return true;
        }
      }
      return false;
    }



    @Override
    public Object caseOccurrenceSpecification(OccurrenceSpecification e) {
      ttcn3Creator.doCreate(e);        
      return e;
    }


    @Override
    public Object caseOperation(Operation e) {
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
    public Object caseParameter(Parameter e) {
      EObject ttcn3Elm = ttcn3Creator.doCreate(e);   
      defaultCase(e);
      Operation operation = e.getOperation();
      if (operation != null) {
        EObject parent = (EObject) doSwitch(operation);
        if (ttcn3Elm instanceof ParameterDeclaration
            && parent instanceof ConstantDeclaration) {
          if (e.getDirection() == ParameterDirectionKind.RETURN_LITERAL) {
            setFunctionReturn((ConstantDeclaration) parent,
                ((ParameterDeclaration) ttcn3Elm).getTheType());
          }
          else {
            addParameterToFunction((ConstantDeclaration) parent,
                (ParameterDeclaration) ttcn3Elm);
          }
        }
      }
      return e;
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
   * Remove local parameters for given namespace.
   * @param e
   */
  private void removeLocalParameters(Namespace e) {
    localParameters.remove(e);
  }

  /**
   * Check if parameter p is a tsi parameter.
   * @param p
   * @return
   */
  private boolean isTSIParameter(ParameterDeclaration p) {
    if(p.getTheType().equals(createIdentifier(getTSIMainTypeName())))
      return true;
    return false;
  }

  /**
   * Check if property is connected, either directly or
   * over ports of the class type
   * @param p
   * @return
   */
  public boolean isConnected(Property p) {
    boolean result = false;
    if(p.getEnds().size() > 0)
      result = true;
    else{
      org.eclipse.uml2.uml.Type tp = p.getType();
      if(tp instanceof org.eclipse.uml2.uml.Class){
        EList ports = ((org.eclipse.uml2.uml.Class)tp).getOwnedPorts();
        for (int i = 0; i < ports.size(); i++) {
          org.eclipse.uml2.uml.Port port = (org.eclipse.uml2.uml.Port)ports.get(i);
          if(port.getEnds().size() > 0){
            result = true;
            break;
          }
        }
      }
    }
    return result;
  }

  /**
   * Get main test component type.
   * @return
   */
  protected TypeDeclaration getMTC() {
    NamedElementDeclaration decl = getTTCN3DeclarationForPredefined(this, TTmodelerConsts.DEFAULT_MTC_TYPE_NAME);
    if(decl != null && decl instanceof TypeDeclaration)
      return (TypeDeclaration)decl;
    return null;
  }

  /**
   * Get tsi main test component type.
   * @return
   */
  protected TypeDeclaration getTSIMain() {
    return tsiMain;
  }

  /**
   * Create done statement.
   * @return
   */
  private Done createDoneOperation(String port){
    Done done = Reducer.opFac.createDone();
    done.setTheOperand(createIdentifier(port));
    return done;
  }
}