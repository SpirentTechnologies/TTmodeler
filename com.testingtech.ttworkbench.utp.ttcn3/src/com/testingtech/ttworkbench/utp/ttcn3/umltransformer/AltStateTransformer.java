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

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;

import com.testingtech.muttcn.statements.AltAlternative;
import com.testingtech.muttcn.statements.AltStatement;
import com.testingtech.ttcn.metamodel.Reducer;
import com.testingtech.ttworkbench.utp.ttcn3.constructor.AltStatementConstructor;
import com.testingtech.ttworkbench.utp.ttcn3.umlextractor.StateExtractor;

public class AltStateTransformer implements
    UML2TTCN3Transformer<State, AltStatement> {

  public AltStatement transform(State altState) {

    AltStatement altStatement;
    
    ArrayList<AltAlternative> alternatives = new ArrayList<AltAlternative>();
    StateExtractor myState = new StateExtractor(altState);
   
    for (Transition altTransition : myState.outTransitions) {
      AltAlternative alternative = new AltTransitionTransformer(altTransition).transform(altTransition);
      alternatives.add(alternative);
    }
    
    altStatement = new AltStatementConstructor().construct(alternatives);
    return altStatement;
  }

}
