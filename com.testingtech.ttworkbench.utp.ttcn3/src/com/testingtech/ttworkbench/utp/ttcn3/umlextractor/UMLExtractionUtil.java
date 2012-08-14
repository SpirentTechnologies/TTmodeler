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
package com.testingtech.ttworkbench.utp.ttcn3.umlextractor;

import static com.testingtech.ttworkbench.utp.core.TTmodelerConsts.COMMENT_FORMAT_PLAINTEXT;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;

public class UMLExtractionUtil {

  public static String getCommentFromElement(Element element, String keyword, boolean commentInputFormat) {
  
    String comment = null;
    
    for (Comment c : element.getOwnedComments()) {
      for (EAnnotation ea : c.getEAnnotations()) {
        if (ea.getDetails().containsKey(keyword)) {
          comment =  c.getBody().trim();
          // catch cases in which the user forgot to fill the comment body
          if (comment.length() == 0) {
            // TODO: add error handling here: comment is empty
          }
          else {
            if (commentInputFormat == COMMENT_FORMAT_PLAINTEXT) {
              return comment;
            }
            else {
              String strippedComment = stripHTMLfromComment(comment);
              if (strippedComment == null || strippedComment.length() == 0) {
                // TODO: add error handling here: (stripped) comment is empty
              }
              return strippedComment;
            }
          }
        }
      }
    }
    
    // TODO: add error handling here
    // if the method hasn't returned until here, either the comment keyword was misspelled,
    // or no respective comment was present. 
    return comment;
  }

  @SuppressWarnings("nls")
  private static String stripHTMLfromComment(String comment) {
  
    Scanner commentStripper = new Scanner(comment);
    Pattern htmlTagPattern = Pattern.compile("<\\S*\\s*\\S*>\\s*");
    commentStripper.useDelimiter(htmlTagPattern);
    while (commentStripper.hasNext()) {
      String nextToken = commentStripper.next();
      if (nextToken.length() != 0) {
        return nextToken.trim();
      }
    }
    return null;
  }
  
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
  
  public static <T extends NamedElement> T getElementbyKeyword(String keyword,
      List<T> list) {

    for (T element : list) {
      if (hasKeyword(element, keyword)) {
        return element;
      }
    }
  // TODO: add error handling here: no element with respective keyword present,
  // or keyword misspelled
    return null;
  }

  public static <T extends NamedElement> T getElementbyName(String name,
      List<T> list) {

    for (T element : list) {
      if (element.getName().equals(name)) {
        return element;
      }
    }
  // TODO: add error handling here: no element with respective named present,
  // or name misspelled
    return null;
  }
}
