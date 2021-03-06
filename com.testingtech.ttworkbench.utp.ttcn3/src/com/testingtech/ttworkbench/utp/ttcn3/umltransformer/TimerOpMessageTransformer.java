/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2001-2012.  All Rights Reserved.
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
package com.testingtech.ttworkbench.utp.ttcn3.umltransformer;

import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.COMMENT_FORMAT_HTML;

import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.ValueSpecification;

import com.testingtech.muttcn.kernel.Expression;
import com.testingtech.muttcn.kernel.Statement;
import com.testingtech.muttcn.operation.Read;
import com.testingtech.muttcn.operation.UnaryOperation;
import com.testingtech.muttcn.statements.AssignStatement;
import com.testingtech.muttcn.statements.StartStatement;
import com.testingtech.muttcn.values.FloatValue;
import com.testingtech.ttworkbench.utp.core.TTmodelerConsts;
import com.testingtech.ttworkbench.utp.core.UTPConsts;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.AssignStatementConstructor;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.ExpressionConstructor;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.ReadOpConstructor;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.RunningOpConstructor;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.StartStatementConstructor;
import com.testingtech.ttworkbench.utp.ttcn3.umlextractor.UMLExtractionUtil;

/**
 * this class is the transformer for sequence diagram messages calling
 * timer operations.
 * @author alo
 *
 */
public class TimerOpMessageTransformer extends TimerOpCommonTransformer implements UML2TTCN3Transformer<Message, Statement> {

  public Statement transform(Message timerOpMsg) {

    timerNameAsString = UMLExtractionUtil.getCommentFromElement(timerOpMsg, TTmodelerConsts.TIMER_REFERENCE, COMMENT_FORMAT_HTML);
    timerNameAsExpression = new ExpressionConstructor().construct(timerNameAsString);

    String timerOpName = timerOpMsg.getSignature().getName();

    // TODO: add error handling here: check if timeropname == null

    if (timerOpName.equals(UTPConsts.TIMER_START_OPERATION_NAME)) {
      return transformTimerStartStatement(timerOpMsg);
    }
    else if (timerOpName.equals(UTPConsts.TIMER_STOP_OPERATION_NAME)) {
      return transformTimerStopStatement();
    }
    else if (timerOpName.equals(UTPConsts.TIMER_READ_OPERATION_NAME)) {
      return transformTimerReadStatement(timerOpMsg);
    }
    else if (timerOpName.equals(UTPConsts.TIMER_RUNNING_OPERATION_NAME)) {
      return transformTimerRunningStatement(timerOpMsg);
    }
    else if (timerOpName.equals(UTPConsts.TIMER_TIMEOUT_OPERATION_NAME)) {
      return transformTimerTimeoutStatement();
    }

    //TODO: add error handling here: unknown timer operation
    // (shouldn't happen)
    return null;
  }


  private Statement transformTimerReadStatement(Message timerOpMsg) {

    // 1. declare fields for necessary ttcn-3 elements     
    AssignStatement readStatement;
    Read timerReadOp;
    Expression variableName;

    // 2. extract info from uml and construct ttcn-3 sub elements
    // of the resulting element 
    String varNameAsString = UMLExtractionUtil.getCommentFromElement(timerOpMsg,
        TTmodelerConsts.VARIABLE_REFERENCE, TTmodelerConsts.COMMENT_FORMAT_HTML);    

    variableName = new ExpressionConstructor().construct(varNameAsString);
    timerReadOp = new ReadOpConstructor().construct(timerNameAsExpression);

    // 3. construct the main element
    readStatement = new AssignStatementConstructor().construct(variableName, timerReadOp);

    return readStatement;    
  }

  private Statement transformTimerRunningStatement(Message timerOpMsg) {

    // 1. declare fields for necessary ttcn-3 elements 
    AssignStatement runningStatement;
    UnaryOperation timerRunningOp;

    // 2. extract info from uml and construct ttcn-3 sub elements
    // of the resulting element 
    
    // name of the variable which the running value shall be assigned to
    String booleanVarName = UMLExtractionUtil.getCommentFromElement(timerOpMsg, TTmodelerConsts.VARIABLE_REFERENCE, TTmodelerConsts.COMMENT_FORMAT_HTML);
    Expression booleanVarExpression = new ExpressionConstructor().construct(booleanVarName);
    
    timerRunningOp = new RunningOpConstructor().construct(timerNameAsString);

    // 3. construct the main element
    runningStatement = new AssignStatementConstructor().construct(booleanVarExpression,
        timerRunningOp);

    return runningStatement;
  }

  private Statement transformTimerStartStatement(Message timerOpMsg) {

    // 1. declare fields for necessary ttcn-3 elements 
    StartStatement startStatement;
    FloatValue duration = null;

    // if a duration value is present, resolve it
    duration = checkAndResolveDurationValue(timerOpMsg, duration);   

    // 3. construct the main element
    startStatement = new StartStatementConstructor().construct(timerNameAsExpression,
        duration);    
    return startStatement;
  }


  private FloatValue checkAndResolveDurationValue(Message timerOpMsg,
      FloatValue duration) {

    if ( ! timerOpMsg.getArguments().isEmpty()) {
      ValueSpecification durationValue = timerOpMsg.getArguments().get(0);
      duration = new TimerDurationTransformer().transform(durationValue);
    }
    return duration;
  }
}
