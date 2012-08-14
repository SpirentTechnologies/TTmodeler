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

import static com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator.generateIdentifier;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.COMMENT_FORMAT_HTML;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.VALUE_REFERENCE;
import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.VARIABLE_REFERENCE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.ConnectableElement;
import org.eclipse.uml2.uml.Connector;
import org.eclipse.uml2.uml.ConnectorEnd;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.LiteralBoolean;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageSort;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.ReceiveOperationEvent;
import org.eclipse.uml2.uml.SendOperationEvent;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.TypedElement;
import org.eclipse.uml2.uml.ValueSpecification;

import com.testingtech.muttcn.auxiliary.ParameterDeclaration;
import com.testingtech.muttcn.auxiliary.SignatureParameterDeclaration;
import com.testingtech.muttcn.functionalabstractions.BehaviorValue;
import com.testingtech.muttcn.functionalabstractions.FunctionValue;
import com.testingtech.muttcn.kernel.Expression;
import com.testingtech.muttcn.kernel.Statement;
import com.testingtech.muttcn.kernel.Value;
import com.testingtech.muttcn.statements.ConstantDeclaration;
import com.testingtech.muttcn.statements.ExecuteStatement;
import com.testingtech.muttcn.statements.GroupDeclaration;
import com.testingtech.muttcn.statements.ModuleDeclaration;
import com.testingtech.muttcn.statements.NamedElementDeclaration;
import com.testingtech.muttcn.statements.StatementBlock;
import com.testingtech.muttcn.statements.TypeDeclaration;
import com.testingtech.muttcn.statements.ValuedElementDeclaration;
import com.testingtech.muttcn.types.AltstepBehaviorType;
import com.testingtech.muttcn.types.ComponentType;
import com.testingtech.muttcn.types.FunctionBehaviorType;
import com.testingtech.muttcn.types.FunctionType;
import com.testingtech.muttcn.types.ProductType;
import com.testingtech.muttcn.types.SignatureType;
import com.testingtech.muttcn.types.TestcaseBehaviorType;
import com.testingtech.muttcn.values.CharStringValue;
import com.testingtech.muttcn.values.ConstantValue;
import com.testingtech.muttcn.values.FloatValue;
import com.testingtech.muttcn.values.IntegerValue;
import com.testingtech.muttcn.values.ModuleValue;
import com.testingtech.muttcn.values.OctetStringValue;
import com.testingtech.ttcn.metamodel.Reducer;
import com.testingtech.ttworkbench.core.ide.EMFModuleView;
import com.testingtech.ttworkbench.core.ide.ModuleDeclarationKind;
import com.testingtech.ttworkbench.core.ttcn3.ITTCN3Project;
import com.testingtech.ttworkbench.metamodel.core.exception.ModelProcessingException;
import com.testingtech.ttworkbench.metamodel.muttcn.emfview.MuTTCNEMFModuleElementView;
import com.testingtech.ttworkbench.metamodel.muttcn.generator.DeclarationGenerator;
import com.testingtech.ttworkbench.metamodel.muttcn.generator.ValueGenerator;
import com.testingtech.ttworkbench.utp.core.TTmodelerConsts;
import com.testingtech.ttworkbench.utp.core.UMLConsts;
import com.testingtech.ttworkbench.utp.core.UTPConsts;
import com.testingtech.ttworkbench.utp.ttcn3.umlextractor.UMLExtractionUtil;

public abstract class TransformationUtil {

  public final static String PARAMETER_VALUE_EXCEPTION_MESSAGE = Messages.TransformationUtil_WrongValForPar;

  // file extension for ttcn3 file
  public static final String CL_SOURCE_FILE_EXTENSION = '.'+ITTCN3Project.TTCN3_MODULE_DEFAULT_EXTENSION;

  public static final String VALUE_TRUE = "true"; //$NON-NLS-1$

  public static final String VALUE_FALSE = "false"; //$NON-NLS-1$

  // port operation kind
  public static final int PORT_OP_KIND_UNDEF = 10;

  public static final int PORT_OP_KIND_CALL = 11;

  public static final int PORT_OP_KIND_REPLY = 12;

  public static final int PORT_OP_KIND_GETCALL = 13;

  public static final int PORT_OP_KIND_GETREPLY = 14;

  // connection kind
  public static final int CONNECTION_KIND_UNDEF = 0;

  public static final int CONNECTION_KIND_MAP = 1;

  public static final int CONNECTION_KIND_CONNECT = 2;

  /**
   * Interpreter of uml CconnectorEnd. Assum role and part are both specified
   * even if role refers to port of a conatining classifier. In case part is
   * empty, role refers to an instance of classifier. Should consider then if
   * both ends refer to test component or sut to determine to use default port
   * for map/connect statement.
   * 
   * @param end
   */
  public class ConnectorEndInterpreter {

    public String part = null;

    public String port = null;

    public Property prop = null;

    public ConnectorEndInterpreter(ConnectorEnd end) {

      if (end != null) {
        ConnectableElement roleElm = end.getRole();
        Property partElm = end.getPartWithPort();
        if (partElm == null) {
          // roleElm refers to property
          part = roleElm.getName();
          // default port
          port = TTmodelerConsts.DEFAULT_PORT_NAME;
        }
        else {
          // partElm refers to property, roleElm refers to port
          port = roleElm.getName();
          part = partElm.getName();
        }
        prop = getProperty(end);
      }
    }

    /**
     * Get applied stereo type for this prop.
     * 
     * @return
     */
    public String getStereoType() {

      if (prop != null) {
        if (isStereotypeApplied(prop, UTPConsts.SUT_NAME))
          return UTPConsts.SUT_NAME;
        else if (isStereotypeApplied(prop.getType(),
            UTPConsts.TEST_COMPONENT_NAME))
          return UTPConsts.TEST_COMPONENT_NAME;
      }
      return ""; //$NON-NLS-1$
    }
  }

  /**
   * Interpreter of uml Connector. Assum two ends of connection.
   * 
   * @author mli
   */
  public class ConnectorInterpreter {

    public ConnectorEndInterpreter source = null;

    public ConnectorEndInterpreter target = null;

    public int kind = CONNECTION_KIND_UNDEF;

    public boolean isValid = false;

    public ConnectorInterpreter(Connector conn) {

      EList ends = conn.getEnds();
      if (ends.size() == 2) {
        ConnectorEnd end1 = (ConnectorEnd) ends.get(0);
        ConnectorEnd end2 = (ConnectorEnd) ends.get(1);
        setIsValid(end1, end2);
        source = new ConnectorEndInterpreter(end1);
        target = new ConnectorEndInterpreter(end2);
        resolveKind(end1, end2);
      }
    }

    /**
     * Check validity of connector ends.
     * 
     * @param end1
     * @param end2
     */
    private void setIsValid(ConnectorEnd end1, ConnectorEnd end2) {

      isValid = (getIsValid(end1) && getIsValid(end2));
    }

    /**
     * Resolve kind of connection.
     * 
     * @return
     */
    public void resolveKind(ConnectorEnd end1, ConnectorEnd end2) {

      Property inst1 = source.prop;;
      Property inst2 = target.prop;
      if (inst1 != null && inst2 != null) {
        String inst1SType = source.getStereoType();
        String inst2SType = target.getStereoType();
        if ((inst1SType.equals(UTPConsts.SUT_NAME) && inst2SType
            .equals(UTPConsts.TEST_COMPONENT_NAME))
            || (inst2SType.equals(UTPConsts.SUT_NAME) && inst1SType
                .equals(UTPConsts.TEST_COMPONENT_NAME)))
          kind = CONNECTION_KIND_MAP;
        else if ((inst1SType.equals(UTPConsts.TEST_COMPONENT_NAME) && inst2SType
            .equals(UTPConsts.TEST_COMPONENT_NAME)))
          kind = CONNECTION_KIND_CONNECT;
      }
    }
  }

  private UTP2TTCN3TransformationManager manager;

  public TransformationUtil(UTP2TTCN3TransformationManager manager) {

    this.manager = manager;
  }

  public void dispose() {

    manager = null;
  }

  /**
   * Add given declaration decl to given module.
   * 
   * @param parent
   * @param decl
   */
  public static void addDeclaration2Module(ModuleDeclaration parent,
      NamedElementDeclaration decl) {

    ModuleValue value = parent.getTheModuleValue();
    if (value == null) {
      value = DeclarationGenerator
      .generateModuleValue((ModuleDeclaration) parent);
    }
    value.getTheDeclarations().add(decl);
  }

  public static void addDeclaration2Group(GroupDeclaration parent,
      NamedElementDeclaration decl) {

    parent.getTheDeclarations().add(decl);
  }

  /**
   * Add declaration to parent.
   * 
   * @param parent
   * @param decl
   */
  public static void addNamedElementDeclaration(EObject parent,
      NamedElementDeclaration decl) {

    if (parent instanceof GroupDeclaration) {
      addDeclaration2Group((GroupDeclaration) parent, decl);
    }
    else if (parent instanceof ModuleDeclaration) {
      addDeclaration2Module((ModuleDeclaration) parent, decl);
    }
  }

  /**
   * Adds a constant field declaration to a component type.
   * @param component
   * @param decl
   */
  public static void addDeclaration2ComponentType(
      ComponentType component, ValuedElementDeclaration decl) {

    component.getTheFieldDeclarations().add(Reducer.toFieldDeclaration(decl, false));
  }

  public static StatementBlock getBody(ConstantDeclaration func) {
    FunctionValue fValue = (FunctionValue) func.getTheValue();
    if (fValue == null) {
      fValue = DeclarationGenerator.generateFunctionValue();
      func.setTheValue(fValue);
    }
    Expression resultExpr = fValue.getTheResult();
    StatementBlock body = null;
    if (resultExpr instanceof BehaviorValue) {
      body = (StatementBlock) ((BehaviorValue) resultExpr).getTheBehaviorBody();
      if (body == null) {
        body = DeclarationGenerator.generateStatementBlock();
        ((BehaviorValue) resultExpr).setTheBehaviorBody(body);
      }
    }
    return body;
  }

  public static boolean isReplyMsg(Message msg) {

    MessageSort sort = msg.getMessageSort();
    if (sort.getValue() == MessageSort.REPLY) {
      return true;
    }
    return false;
  }

  public static int determineOperationKind(org.eclipse.uml2.uml.Event event,
      boolean isReply) {

    int operationKind = PORT_OP_KIND_UNDEF;
    // determine operation kind
    if (!isReply) {
      if (event instanceof SendOperationEvent)
        operationKind = PORT_OP_KIND_CALL;
      else if (event instanceof ReceiveOperationEvent)
        operationKind = PORT_OP_KIND_GETCALL;
      else if (event instanceof CallEvent)
        operationKind = PORT_OP_KIND_CALL;
    }
    else {
      // how events are associated to occurrence specifications is unclear in UML2 spec.
      // uml2 2.0 defines only SendOperationEvent. uml2 2.1 addes ReceiveOperationEvent
      if (event instanceof SendOperationEvent)
        operationKind = PORT_OP_KIND_REPLY;
      else if (event instanceof ReceiveOperationEvent)
        operationKind = PORT_OP_KIND_GETREPLY;
    }
    return operationKind;
  }

  /**
   * Add given statment st to given func.
   * 
   * @param func
   * @param st
   */
  public static void addStatementToFunction(ConstantDeclaration func, Statement st) {
    Statement body = getBody(func);
    FunctionValue fValue = (FunctionValue) func.getTheValue();
    if (fValue == null) {
      fValue = DeclarationGenerator.generateFunctionValue();
      func.setTheValue(fValue);
    }
    Expression resultExpr = fValue.getTheResult();
    if (body != null) {
      if (body instanceof StatementBlock) {
        StatementBlock statementBlock = (StatementBlock) body;
        addStatementToBlock(st, statementBlock);
      }
      else if (resultExpr instanceof BehaviorValue) {
        StatementBlock block = DeclarationGenerator.generateStatementBlock();
        addStatementToBlock(body, block);
        addStatementToBlock(st, block);
        ((BehaviorValue) resultExpr).setTheBehaviorBody(block);
      }
    }
  }

  public static void addStatementToBlock(Statement st,
      StatementBlock statementBlock) {

    statementBlock.getTheStatements().add(st);
  }

  /**
   * Add given parameter par to given function func.
   * 
   * @param func
   * @param par
   */
  public static void addParameterToFunction(ConstantDeclaration func,
      ParameterDeclaration par) {

    FunctionType fType = (FunctionType) func.getTheType();
    if (fType == null) {
      fType = DeclarationGenerator.generateFunctionType();
      func.setTheType(fType);
    }
    ProductType pType = (ProductType) fType.getTheFromType();
    if (pType == null) {
      pType = DeclarationGenerator.generateProductType();
      fType.setTheFromType(pType);
    }
    if (par.getTheType() != null)
      pType.getTheComponentTypes().add(par.getTheType());
    com.testingtech.muttcn.functionalabstractions.FunctionValue fValue = (com.testingtech.muttcn.functionalabstractions.FunctionValue) func
    .getTheValue();
    fValue.getTheParameters().add(par);

  }

  /**
   * Get parameter declarations from given function.
   * 
   * @param func
   * @return
   */
  public EList createFunctionParameters(ConstantDeclaration func) {

    EList result = new BasicEList();
    com.testingtech.muttcn.functionalabstractions.FunctionValue fValue = (com.testingtech.muttcn.functionalabstractions.FunctionValue) func
    .getTheValue();
    if (fValue != null) {
      result.addAll(fValue.getTheParameters());
    }
    return result;
  }

  /**
   * Add ttcn3Elem as parameter (or return type) to parent.
   * 
   * @param par
   * @param parent
   * @param kind
   */
  public static void addParameter(SignatureParameterDeclaration par,
      TypeDeclaration parent, ParameterDirectionKind kind) {

    SignatureType sv = (SignatureType) parent.getTheType();
    if (sv == null) {
      sv = DeclarationGenerator.generateSignatureType(parent);
    }
    if (kind == ParameterDirectionKind.RETURN_LITERAL) {
      sv.setTheType(par.getTheType());
    }
    else {
      SignatureParameterDeclaration sigpar = DeclarationGenerator
      .generateSignatureParameterDeclaration(par.getTheName().getTheName());
      sigpar.setTheType(par.getTheType());
      sigpar.setIsIn(par.getIsIn());
      sigpar.setIsOut(par.getIsOut());
      sv.getTheFieldDeclarations().add(sigpar);
    }
  }

  /**
   * Check if umlElm applied given stereotype.
   * 
   * @param umlElm
   * @param stereoTypetName
   * @return
   */
  public static boolean isStereotypeApplied(org.eclipse.uml2.uml.Element umlElm,
      String stereoTypeName) {

    if (umlElm == null)
      return false;
    List stTypes = stereoTypeDefinitions(umlElm.getAppliedStereotypes());
    for (int i = 0; i < stTypes.size(); i++) {
      Object ste = stTypes.get(i);
      if (ste instanceof EClass
          && TransformationUtil.matchType((EClass) ste, stereoTypeName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Create a ttcn3 basic type for given name;
   * 
   * @param name
   * @return
   */
  public static Expression createBasicType(
      String name) {

    return DeclarationGenerator.generateType(name);
  }

  /**
   * Create identifier for given name.
   * 
   * @param name
   * @return
   */
  public static Expression createIdentifier(
      String name) {

    return DeclarationGenerator.generateIdentifier(name);
  }

  /**
   * Create external function with given name.
   * 
   * @param name
   * @return
   */
  public static ConstantDeclaration createExternalFunction(String name,
      Expression rType) {

    ConstantDeclaration decl = DeclarationGenerator
    .generateConstantDeclaration(name);
    FunctionType fType = DeclarationGenerator.generateFunctionType();
    fType.setTheToType(DeclarationGenerator.generateFunctionBehaviorType());
    decl.setTheType(fType);
    if (rType != null)
      setFunctionReturn(decl, rType);
    FunctionValue fValue = DeclarationGenerator.generateFunctionValue();
    fValue.setTheResult(DeclarationGenerator.generateFunctionBehaviorValue());
    decl.setTheValue(fValue);
    return decl;
  }

  /**
   * Return execute statement with op as operand.
   * 
   * @param op
   * @return
   */
  public ExecuteStatement createExecuteStatement(
      Expression op) {

    ExecuteStatement ex = Reducer.stmtFac.createExecuteStatement();
    ex.setTheOperand(op);
    return ex;
  }

  /**
   * Check if function declaration contains parameter.
   * 
   * @param functionDecl
   * @param varName
   * @return
   */
  public static boolean functionContainsParameter(ConstantDeclaration func,
      String parName) {

    boolean result = false;
    com.testingtech.muttcn.functionalabstractions.FunctionValue fValue = (com.testingtech.muttcn.functionalabstractions.FunctionValue) func
    .getTheValue();
    if (fValue != null) {
      EList pars = fValue.getTheParameters();
      for (int i = 0; i < pars.size(); i++) {
        ParameterDeclaration par = (ParameterDeclaration) pars.get(i);
        if (par.getTheName().getTheName().equals(parName))
          return true;
      }
    }
    return result;
  }

  /**
   * Check if connection end is valid according to semantics definition
   * 
   * @param end1
   * @return
   */
  public boolean getIsValid(ConnectorEnd end1) {

    boolean result = true;
    org.eclipse.uml2.uml.Property prop = end1.getPartWithPort();
    org.eclipse.uml2.uml.ConnectableElement role = end1.getRole();

    // If a connector end is attached to a port of the containing classifier,
    // partWithPort will be empty.

    // If a connector end references both a role and a partWithPort,
    // then the role must be a port that is defined by the type of the
    // partWithPort.
    if (prop != null && role != null) {
      if (!(role instanceof org.eclipse.uml2.uml.Port))
        result = false;
      else {
        org.eclipse.uml2.uml.Type roleType = ((org.eclipse.uml2.uml.Port) role)
        .getClass_();
        org.eclipse.uml2.uml.Type propType = prop.getType();
        if (roleType != null && propType != null)
          if (!(roleType.equals(propType)))
            result = false;
      }
      // The property held in self.partWithPort must not be a Port.
      if (prop instanceof org.eclipse.uml2.uml.Port)
        result = false;
    }
    else
      result = false;
    return result;
  }

  /**
   * Get ttcn3 element for given utp predefined type.
   * 
   * @param qualifiedName
   * @return
   */
  public static Expression getPredefinedType(
      String qualifiedName) {

    Expression eType = null;

    return eType;
  }

  /**
   * Get property referred by the given connector end.
   * 
   * @param end
   * @return
   */
  private Property getProperty(ConnectorEnd end) {

    if (end != null) {
      ConnectableElement roleElm = end.getRole();
      Property partElm = end.getPartWithPort();
      if (partElm == null && roleElm instanceof Property) {
        // roleElm refers to property
        return (Property) roleElm;
      }
      else {
        // partElm refers to property, roleElm refers to port
        return partElm;
      }
    }
    return null;
  }

  /**
   * Get namespace of given umlType.
   * 
   * @param creator
   * @param umlType
   * @return
   */
  public static Namespace getNamespace(GeneralModuleCreator creator,
      org.eclipse.uml2.uml.Type umlType) {

    Namespace ns = umlType.getPackage();
    if (umlType instanceof Interface
        || (umlType instanceof Class && !isStereotypeApplied(umlType,
            UTPConsts.TEST_COMPONENT_NAME))) {
      ns = (Namespace) umlType;
    }
    return ns;
  }

  private static final Map<String, String> UML2TTCN = new Hashtable<String, String>();

  private static final void mapUml2Ttcn(String umlType, String ttcnType) {
    UML2TTCN.put(umlType, ttcnType);
  }
  static {
    mapUml2Ttcn("UMLPrimitiveTypes::Boolean", "boolean"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("UMLPrimitiveTypes::Integer", "integer"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("UMLPrimitiveTypes::String", "charstring"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("UMLPrimitiveTypes::UnlimitedNatural", "any"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("JavaPrimitiveTypes::boolean", "boolean"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("JavaPrimitiveTypes::byte", "octetstring"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("JavaPrimitiveTypes::char", "char"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("JavaPrimitiveTypes::double", "float"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("JavaPrimitiveTypes::float", "float"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("JavaPrimitiveTypes::int", "integer"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("JavaPrimitiveTypes::long", "integer"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("JavaPrimitiveTypes::short", "integer"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("TTCN3Predefined::Types::Float", "float"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("TTCN3Predefined::Types::Integer", "integer"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("TTCN3Predefined::Types::Boolean", "boolean"); //$NON-NLS-1$ //$NON-NLS-2$
    mapUml2Ttcn("TTCN3Predefined::Types::Charstring", "charstring"); //$NON-NLS-1$ //$NON-NLS-2$
    
  }

  /**
   * Get ttcn3 element for given uml and utp primitive type
   * 
   * @param type
   * @return
   */
  public static Expression getPrimitiveType(String qualifiedName) {

    Expression eType = null;
    String ttcnType = UML2TTCN.get(qualifiedName);
    if (ttcnType != null) {
      eType = createBasicType(ttcnType);
    }
    return eType;
  }

  /**
   * Get ttcn3 declaration for given typeName in given emfView.
   * 
   * @param emfView
   * @param typeName
   * @return
   */
  public static NamedElementDeclaration getTTCN3Declaration(EMFModuleView emfView, String typeName) {

    if (emfView != null) {
      EList found = emfView.findDeclaration(null, typeName,
          ModuleDeclarationKind.ANY_LITERAL, true);
      if (found != null && found.size() > 0) {
        EObject obj = (EObject) ((MuTTCNEMFModuleElementView) found.get(0)).getObject();
        if (obj instanceof NamedElementDeclaration) {
          return (NamedElementDeclaration) obj;
        }
      }
    }
    return null;
  }

  /**
   * @param input
   * @return
   */
  public List inverseOrder(List input) {

    List output = new ArrayList();
    if (input.size() > 0) {
      for (int i = input.size() - 1; i >= 0; i--) {
        output.add(input.get(i));
      }
    }
    return output;
  }

  /**
   * Check if given string is an empty string.
   * 
   * @param string
   * @return
   */
  public static boolean isEmpty(String string) {

    return string == null || string.length() == 0;
  }

  /**
   * Get value from given value specification
   * 
   * @param arg
   * @return
   */
  public static Expression transformValueSpecification(ValueSpecification arg) {

    // value type defined by the type of value specification
    Expression result = null;
    if (arg instanceof LiteralString) {
      String val = ((LiteralString) arg).getValue();
      CharStringValue charStringValue = Reducer.valueFac.createCharStringValue();
      result = getValue(charStringValue, val);
    }
    else if (arg instanceof LiteralInteger) {
      int val = ((LiteralInteger) arg).getValue();
      IntegerValue integerValue = Reducer.valueFac.createIntegerValue();
      result = getValue(integerValue, val);
    }
    else if (arg instanceof LiteralBoolean) {
      boolean val = ((LiteralBoolean) arg).isValue();
      String stringVal = VALUE_FALSE;
      if (val)
        stringVal = VALUE_TRUE;
      result = DeclarationGenerator.generateConstantValue(stringVal);
    }
    else if (arg instanceof org.eclipse.uml2.uml.Expression) {
      // symbol as value
      String val = ((org.eclipse.uml2.uml.Expression) arg).getSymbol();
      result = createIdentifier(val);
    }
    return result;
  }

  public static Expression transformElementWithAssignmentComment(Element elemWithAssignmentComment) {

    Expression assignmentExpression = null;

    // look for a comment which assigns an attribute/field to this parameter
    // and add the uniquifying appendix to it
    String basicParameterName = UMLExtractionUtil.getCommentFromElement(elemWithAssignmentComment,
        VARIABLE_REFERENCE, COMMENT_FORMAT_HTML);
    if (basicParameterName != null) {
      String assignedVariableName = createUniqueName(basicParameterName,
          TTmodelerConsts.VARIABLE);
      assignmentExpression = generateIdentifier(assignedVariableName);
    }
    // if no variable comment exists, look for value comment
    else {
      String assignedValue = UMLExtractionUtil.getCommentFromElement(elemWithAssignmentComment, TTmodelerConsts
          .VALUE_REFERENCE, COMMENT_FORMAT_HTML); 
      if (elemWithAssignmentComment instanceof TypedElement) {
        String typeName = ((TypedElement) elemWithAssignmentComment).getType().getName();
        if (typeName.equals(UMLConsts.UML_PRIMITIVE_TYPE_INTEGER_NAME)) {
          assignmentExpression = ValueGenerator
              .generateIntegerValue(assignedValue);
        } else if (typeName.equals(UMLConsts.UML_PRIMITIVE_TYPE_BOOLEAN_NAME)) {
          assignmentExpression = ValueGenerator
              .generateBooleanValue(assignedValue);
        } else if (typeName.equals(UMLConsts.UML_PRIMITIVE_TYPE_STRING_NAME)) {
          assignmentExpression = ValueGenerator
              .generateCharStringValue("\"" + assignedValue + "\""); //$NON-NLS-2$
        }
      }
    }
    return assignmentExpression;
  }



  /**
   * Create value of basic ttcn-3 type for given uml type.
   * 
   * @param argType
   * @return
   */
  public static Expression getBasicTypeValue(org.eclipse.uml2.uml.Type argType) {

    Expression value = null;
    String qualifiedName = argType.getQualifiedName();
    if ("UMLPrimitiveTypes::Boolean".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createConstantValue();
    }
    else if ("UMLPrimitiveTypes::Integer".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createIntegerValue();
    }
    else if ("UMLPrimitiveTypes::String".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createCharStringValue();
    }
    else if ("UMLPrimitiveTypes::UnlimitedNatural".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createIntegerValue();
    }
    else if ("JavaPrimitiveTypes::boolean".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createConstantValue();
    }
    else if ("JavaPrimitiveTypes::byte".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createOctetStringValue();
    }
    else if ("JavaPrimitiveTypes::char".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createCharStringValue();
    }
    else if ("JavaPrimitiveTypes::double".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createFloatValue();
    }
    else if ("JavaPrimitiveTypes::float".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createFloatValue();
    }
    else if ("JavaPrimitiveTypes::int".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createIntegerValue();
    }
    else if ("JavaPrimitiveTypes::long".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createIntegerValue();
    }
    else if ("JavaPrimitiveTypes::short".equals(qualifiedName)) { //$NON-NLS-1$
      value = Reducer.valueFac.createIntegerValue();
    }
    return value;
  }

  /**
   * Set content of given value.
   * 
   * @param value
   * @param content
   * @return
   */
  public static Expression getValue(
      Expression value, int content) {

    if (value == null)
      return null;
    if (value instanceof IntegerValue) {
      ((IntegerValue) value).setTheNumber(new BigInteger(""+content)); //$NON-NLS-1$
    }
    return value;
  }

  /**
   * Set content of given value.
   * 
   * @param value
   * @param content
   */
  public static Expression getValue(
      Expression value, String content) {

    if (value == null)
      return null;
    if (value instanceof ConstantValue)
      value.setTheName(Reducer.makeName(content));
    else if (value instanceof CharStringValue) {
      ((CharStringValue) value).setTheContent(content);
    }
    else if (value instanceof OctetStringValue) {
      ((OctetStringValue) value).setTheContent(content);
    }
    else if (value instanceof IntegerValue) {
      BigInteger num = null;
      try {
        num = new BigInteger(content);
      }
      catch (NumberFormatException ex) {
        return null;
      }
      if (num != null)
        ((IntegerValue) value).setTheNumber(num);
    }
    if (value instanceof FloatValue) {
      BigDecimal num = null;
      try {
        num = new BigDecimal(content);
      }
      catch (NumberFormatException ex) {
        return null;
      }
      if (num != null)
        ((FloatValue) value).setTheNumber(num);
    }
    return value;
  }

  /**
   * Create charstring value for given string.
   * 
   * @param arg
   * @return
   */
  public static Value getValue(String arg) {

    return DeclarationGenerator.generateCharStringValue(arg);
  }

  /**
   * Make module with given creator.
   * 
   * @param genCreator
   * @return
   */
  public EMFModuleView makeModule(NamespaceModuleCreator genCreator) {

    try {
      genCreator.create();
      manager.getTransformers().put(genCreator.getModuleName(), genCreator);
    }
    catch (ModelProcessingException ex) {
      manager.reportError(ex);
    }
    return manager.getEmfModuleView(genCreator.getModuleName());
  }

  /**
   * Create test component for given uml type, component type name.
   * 
   * @param umlType
   * @param compTypeName
   * @return
   */
  public static TypeDeclaration createTestComponent(
      org.eclipse.uml2.uml.Type umlType, String compTypeName) {

    TypeDeclaration compType = null;
    compType = createTestComponent(compTypeName);
    // TODO: default port
    return compType;
  }

  public static TypeDeclaration createTestComponent(String compTypeName) {

    ComponentType sutComp = DeclarationGenerator.generateComponentType(null);
    TypeDeclaration compType = DeclarationGenerator
    .generateTypeDeclaration(compTypeName);
    compType.setTheType(sutComp);
    return compType;
  }

  /**
   * Create parameter.
   * 
   * @param typeRef
   * @param name
   * @param direction
   * @return
   */
  public static ParameterDeclaration createParameterDeclaration(
      Expression typeRef, String name, ParameterDirectionKind direction) {

    ParameterDeclaration decl = DeclarationGenerator
    .generateParameterDeclaration(name);
    decl.setTheType(typeRef);
    if (direction == ParameterDirectionKind.IN_LITERAL
        || direction == ParameterDirectionKind.INOUT_LITERAL) {
      decl.setIsIn(Boolean.TRUE);
    }
    if (direction == ParameterDirectionKind.OUT_LITERAL
        || direction == ParameterDirectionKind.INOUT_LITERAL) {
      decl.setIsOut(Boolean.TRUE);
    }
    return decl;
  }

  /**
   * Create parameter.
   * 
   * @param typeRef
   * @param name
   * @param direction
   * @return
   */
  public static SignatureParameterDeclaration createSignatureParameterDeclaration(
      Expression typeRef, String name,
      ParameterDirectionKind direction) {

    SignatureParameterDeclaration decl = DeclarationGenerator
    .generateSignatureParameterDeclaration(name);
    decl.setTheType(typeRef);
    if (direction == ParameterDirectionKind.IN_LITERAL
        || direction == ParameterDirectionKind.INOUT_LITERAL) {
      decl.setIsIn(Boolean.TRUE);
    }
    if (direction == ParameterDirectionKind.OUT_LITERAL
        || direction == ParameterDirectionKind.INOUT_LITERAL) {
      decl.setIsOut(Boolean.TRUE);
    }
    return decl;
  }

  /**
   * Make a qualified name with dotted notation for given input. Assuming input
   * includes local names of scopes in the order, e.g. "module, recordType,
   * field".
   * 
   * @param input
   * @return
   */
  public static Expression getQualifiedName(String[] input) {

    Expression result = null;
    if (input.length == 1)
      result = createIdentifier(input[0]);
    else if (input.length > 1) {
      Expression operand = null;
      for (int i = 0; i + 1 < input.length; i++) {
        String scope = input[i];
        String local = input[i + 1];
        if (operand == null) {
          operand = Reducer.makeFullName(Reducer.makeName(scope), Reducer.makeName(local));
        }
        else {
          operand = Reducer.makeFullName(operand, Reducer.makeName(local));
        }
      }
      result = operand;
    }
    return result;
  }

  /**
   * If type of class cl matches the given name, return true, otherwise return
   * false;
   * 
   * @param cl
   * @param name
   * @return
   */
  public static boolean matchType(EClass cl, String name) {

    if (cl.getName().equals(name))
      return true;
    return false;
  }

  /**
   * Add given return type rType to function func.
   * 
   * @param func
   * @param rType
   */
  public static void setFunctionReturn(ConstantDeclaration func, Expression rType) {

    FunctionType fType = (FunctionType) func.getTheType();
    if (fType == null) {
      fType = DeclarationGenerator.generateFunctionType();
      func.setTheType(fType);
    }
    Expression toTypeExpr = fType.getTheToType();
    if (toTypeExpr instanceof FunctionBehaviorType) {
      FunctionBehaviorType bType = (FunctionBehaviorType) toTypeExpr;
      bType.setTheToType(rType);
    }
    else if (toTypeExpr instanceof TestcaseBehaviorType) {
      TestcaseBehaviorType bType = (TestcaseBehaviorType) toTypeExpr;
      bType.setTheToType(rType);
    }
    else if (toTypeExpr instanceof AltstepBehaviorType) {
      AltstepBehaviorType bType = (AltstepBehaviorType) toTypeExpr;
      bType.setTheToType(rType);
    }
  }

  /**
   * Set runs on delcaration for function or altstep.
   * 
   * @param func
   * @param runson
   */
  public static void setFunctionRunson(ConstantDeclaration func,
      Expression runson) {

    FunctionType fType = (FunctionType) func.getTheType();
    if (fType == null) {
      fType = DeclarationGenerator.generateFunctionType();
      func.setTheType(fType);
    }
    Expression toTypeExpr = fType.getTheToType();
    if (toTypeExpr instanceof FunctionBehaviorType) {
      FunctionBehaviorType bType = (FunctionBehaviorType) toTypeExpr;
      bType.setTheFromType(runson);
    }
    else if (toTypeExpr instanceof AltstepBehaviorType) {
      AltstepBehaviorType bType = (AltstepBehaviorType) toTypeExpr;
      bType.setTheFromType(runson);
    }
    else if (toTypeExpr instanceof TestcaseBehaviorType) {
      TestcaseBehaviorType bType = (TestcaseBehaviorType) toTypeExpr;
      bType.setTheFromType(runson);
    }
  }

  /**
   * Get runs on of given function.
   * 
   * @param func
   * @return
   */
  public Expression getFunctionRunson(ConstantDeclaration func) {

    Expression runson = null;
    FunctionType fType = (FunctionType) func.getTheType();
    if (fType != null) {
      Expression toTypeExpr = fType
      .getTheToType();
      if (toTypeExpr instanceof FunctionBehaviorType) {
        FunctionBehaviorType bType = (FunctionBehaviorType) toTypeExpr;
        runson = bType.getTheFromType();
      }
      else if (toTypeExpr instanceof AltstepBehaviorType) {
        AltstepBehaviorType bType = (AltstepBehaviorType) toTypeExpr;
        runson = bType.getTheFromType();
      }
      else if (toTypeExpr instanceof TestcaseBehaviorType) {
        TestcaseBehaviorType bType = (TestcaseBehaviorType) toTypeExpr;
        runson = bType.getTheFromType();
      }
    }
    return runson;
  }

  public static String getVarOrValueCommentFromElement(Element element, boolean commentInputFormat) {

    String comment = UMLExtractionUtil.getCommentFromElement(element,
        VALUE_REFERENCE, commentInputFormat);
    if (comment == null) {
      comment = UMLExtractionUtil.getCommentFromElement(element,
          VARIABLE_REFERENCE, commentInputFormat);
    }

      return comment;
  }  

  // already copied in new extractionutil, remove during legacy switch to new design!
  /**
   * this method checks whether a uml element has a certain keyword.
   * @param e
   * @param keyword
   * @return
   */
  public static boolean hasKeyword (NamedElement e, String keyword) {

    for (EAnnotation ea : e.getEAnnotations()) {
      if (ea.getDetails().containsKey(keyword)) {
        return true;
      }
    }
    return false;
  }

  /**
   * this method checks whether a uml element has any keyword.
   * @param e
   * @return
   */
  public static boolean hasAnyKeyword (NamedElement e) {

    EList<EAnnotation> annotations = e.getEAnnotations();
    // no annotations
    if (annotations.isEmpty()) {
      return false;
    }

    for (EAnnotation ea : annotations) {
      // empty annotations
      if (ea.getDetails().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  public static String createUniqueName(String name, int type) {

    if (type == TTmodelerConsts.TIMER) {
      name = name + TTmodelerConsts.UNIQUE_TIMER_NAME_APPENDIX;
    }
    else if (type == TTmodelerConsts.VARIABLE) {
      name = name + TTmodelerConsts.UNIQUE_VAR_NAME_APPENDIX;
    }

    return name;      
  }


  /**
   * Set system declaration for given test case.
   * 
   * @param func
   * @param sys
   */
  public static void setTestcaseSystem(ConstantDeclaration func, Expression sys) {

    FunctionType fType = (FunctionType) func.getTheType();
    if (fType == null) {
      fType = DeclarationGenerator.generateFunctionType();
      func.setTheType(fType);
    }
    Expression toTypeExpr = fType.getTheToType();
    if (toTypeExpr instanceof TestcaseBehaviorType) {
      TestcaseBehaviorType bType = (TestcaseBehaviorType) toTypeExpr;
      bType.setTheSystemType(sys);
    }
  }

  /**
   * Get EClass (definition) for given stereoType.
   * 
   * @param st
   * @return
   */
  public static EClass stereoTypeDefinition(EObject stereoType) {

    if (!(stereoType instanceof Stereotype))
      return null;
    Stereotype st = (Stereotype) stereoType;
    return st.getDefinition();
  }

  public static List stereoTypeDefinitions(EList stereoTypes) {

    List result = new ArrayList();
    for (int i = 0; i < stereoTypes.size(); i++) {
      EClass cl = stereoTypeDefinition((EObject) stereoTypes.get(i));
      if (cl != null)
        result.add(cl);
    }
    return result;
  }

}
