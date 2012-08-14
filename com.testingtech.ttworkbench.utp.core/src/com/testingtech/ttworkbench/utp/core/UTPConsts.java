/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2003-2012.  All Rights Reserved.
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

import UTPPredefined.Verdict;

import com.testingtech.utp.DataPartition;
import com.testingtech.utp.DataPool;
import com.testingtech.utp.DataSelector;
import com.testingtech.utp.Default;
import com.testingtech.utp.DefaultApplication;
import com.testingtech.utp.SUT;
import com.testingtech.utp.TestCase;
import com.testingtech.utp.TestComponent;
import com.testingtech.utp.TestContext;
import com.testingtech.utp.UTPPackage;

@SuppressWarnings(value={"nls"})
public final class UTPConsts {

  public static final String PROFILE_NAME = UTPPackage.eNAME;

  public static final String TEST_CONTEXT_NAME = TestContext.class.getSimpleName();

  public static final String TEST_CASE_NAME = TestCase.class.getSimpleName();

  public static final String TEST_COMPONENT_NAME = TestComponent.class.getSimpleName();

  public static final String SUT_NAME = SUT.class.getSimpleName();

  public static final String DEFAULT_NAME = Default.class.getSimpleName();

  public static final String DEFAULT_APPLICATION_NAME = DefaultApplication.class.getSimpleName();

  public static final String DATA_PARTITION_NAME = DataPartition.class.getSimpleName();

  public static final String DATA_POOL_NAME = DataPool.class.getSimpleName();

  public static final String DATA_SELECTOR_NAME = DataSelector.class.getSimpleName();

  public static final String LANGUAGE_TAG = "uml"; //$NON-NLS-1$

  public static final String UTP_PREDEFINED_NAME = "UTPPredefined"; //$NON-NLS-1$

  public static final String UTP_PREDEFINED_Verdict_NAME = Verdict.class.getSimpleName();
  
  public static final String UTP_TIMER_INTERFACE_NAME = "Timer"; //$NON-NLS-1$
  
  public static final String UTP_ARBITER_INTERFACE_NAME = "Arbiter"; //$NON-NLS-1$
  
  public static final String UTP_ARBITER_SETVERDICT_METHOD_NAME = "setVerdict"; //$NON-NLS-1$
  
  public static final String UTP_ARBITER_SETVERDICT_PARAMETER_NAME = "v"; //$NON-NLS-1$

  /**
   * name of module including predefined elements
   */
  public static final String PREDEFINED_MODULE_NAME = "UTPAUX"; //$NON-NLS-1$

  public static final String TIMER_READ_OPERATION_NAME = "read";

  public static final int TIMER_READ_OPERATION = 0;

  public static final String TIMER_START_OPERATION_NAME = "start";

  public static final int TIMER_START_OPERATION = 1;

  public static final String TIMER_STOP_OPERATION_NAME = "stop";

  public static final int TIMER_STOP_OPERATION = 2;

  public static final String TIMER_RUNNING_OPERATION_NAME = "running";

  public static final int TIMER_RUNNING_OPERATION = 3;

  public static final String TIMER_TIMEOUT_OPERATION_NAME = "timeout";

  public static final int TIMER_TIMEOUT_OPERATION = 4;

  /**
   * parameter name of the timer start() signature
   */
  public static final String DURATION = "duration";

  /**
   * parameter name of the timer isRunning() signature
   */
  public static final String IS_RUNNING = "isRunning";

  /**
   * parameter name of the timer read() signature
   */
  public static final String ELAPSED_TIME = "elapsedTime";
}
