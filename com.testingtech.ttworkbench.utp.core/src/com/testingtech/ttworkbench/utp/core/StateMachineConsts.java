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
package com.testingtech.ttworkbench.utp.core;

import org.eclipse.uml2.uml.PseudostateKind;

public final class StateMachineConsts {

  public static final String SM_FINAL_STATE_NAME = "FINAL"; //$NON-NLS-1$
  public static final String SM_UNNAMED_STATE_NAME = "UNNAMED_STATE_"; //$NON-NLS-1$
  public static final String SM_UNNAMED_CHOICE_NAME = "UNNAMED_CHOICE_"; //$NON-NLS-1$
  public static final String SM_INITIAL_STATE_NAME = PseudostateKind.INITIAL_LITERAL.getName();
  // reference constant for incoming transitions
  public static final int INCOMING = 0;
  // reference constant for outgoing transitions
  public static final int OUTGOING = 1;
  public static final String STATEMACHINE_FUNCTION_APPENDIX = "__statemachine"; //$NON-NLS-1$
  public static final String STATE_FUNCTION_APPENDIX = "__state"; //$NON-NLS-1$
  
  
  // keywords, later to turn into stereotypes
  
  public static final String REPLY_TRANSITION_KEYWORD = "reply"; //$NON-NLS-1$
  public static final String EXCEPTION_TRANSITION_KEYWORD = "exception"; //$NON-NLS-1$
  public static final String TIMEOUT_TRANSITION_KEYWORD = "timeout"; //$NON-NLS-1$
  public static final String RECEIVE_TRANSITION_KEYWORD = "receive"; //$NON-NLS-1$
  public static final String GETCALL_TRANSITION_KEYWORD = "getcall"; //$NON-NLS-1$
}
