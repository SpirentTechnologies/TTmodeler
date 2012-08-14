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

import com.testingtech.muttcn.kernel.Expression;
import com.testingtech.muttcn.kernel.Statement;
import com.testingtech.muttcn.statements.ExecuteStatement;
import com.testingtech.muttcn.statements.StopStatement;
import com.testingtech.ttworkbench.utp.core.TTmodelerConsts;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.StopStatementConstructor;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.TimeoutStatementConstructor;

public class TimerOpCommonTransformer {

  protected String timerNameAsString;
  protected Expression timerNameAsExpression;

  protected Statement transformTimerTimeoutStatement() {
  
    ExecuteStatement timeoutStatement = new TimeoutStatementConstructor()
                                            .construct(timerNameAsString);
    return timeoutStatement;
  }

  protected Statement transformTimerStopStatement() {
  
    StopStatement timerStopStatement = new StopStatementConstructor().construct(
        timerNameAsString, TTmodelerConsts.TIMER_OPERAND_TYPE);
    return timerStopStatement;
  }

}
