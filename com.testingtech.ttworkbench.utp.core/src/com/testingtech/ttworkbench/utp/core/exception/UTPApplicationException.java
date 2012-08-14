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
package com.testingtech.ttworkbench.utp.core.exception;

import java.util.ArrayList;
import java.util.List;

import com.testingtech.ttworkbench.metamodel.core.exception.ModelProcessingException;


public class UTPApplicationException extends ModelProcessingException{

  /**
   * 
   */
  private static final long serialVersionUID = -8390694543758849252L;

  private List<String> pkgs = new ArrayList<String>();
  
  private static final String MESSAGE_STARTER = Messages.UTPApplicationException_Msg0;

  public UTPApplicationException(String pkg) {
    pkgs.add(pkg);
  }

  public UTPApplicationException(List<String> pkgs) {
    this.pkgs.addAll(pkgs);
  }
  
  @Override
  public String getMessage() {
    int len = pkgs.size();
    if(len==1){
      msg = MESSAGE_STARTER + pkgs.get(0);
    }else if(len>1){
      msg = MESSAGE_STARTER;
      for(int i=0; i<len; i++){
        String m = pkgs.get(i);
        msg += m + '\n';
      }
    }
    return msg;    
  }

  public List<String> getPackages(){
    return pkgs;
  }
  
}
