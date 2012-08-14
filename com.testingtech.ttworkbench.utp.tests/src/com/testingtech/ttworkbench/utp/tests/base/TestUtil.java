/*
 * ----------------------------------------------------------------------------
 *  (C) Copyright Testing Technologies, 2009-2012.  All Rights Reserved.
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
package com.testingtech.ttworkbench.utp.tests.base;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

import com.testingtech.ttworkbench.core.ui.SWTUtil;

public class TestUtil {

  public static boolean resExists(final IPath workspaceRelativePath) {

    final IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
        workspaceRelativePath);
    return res != null && res.exists();
  }

  public static boolean resExists(final String workspaceRelativePath) {

    final IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(
        workspaceRelativePath);
    return res != null && res.exists();
  }

  /**
   * @param files
   *          a String array with workspace relative paths of files
   * @return a message about missing files or an empty string in good case
   */
  public static String checkMandatoryFiles(final String[] files) {

    final String message = "";
    final List<String> missingFiles = new ArrayList<String>(files.length);
    for (final String string : files) {
      if (!TestUtil.resExists(string)) {
        missingFiles.add(string);
      }
    }

    if (missingFiles.size() > 0) {
      final StringBuffer buf = new StringBuffer();
      for (final String string : missingFiles) {

        buf.append(string);
        if (buf.length() > 0) {
          buf.append(", ");
        }
      }

      return "missing files: " + buf.toString();
    }
    return message;
  }

  public static String checkMandatoryFile(final String file) {

    return checkMandatoryFiles(new String[]{
      file
    });
  }

  public static void printFolderContents(final String workspaceRelativeFolder)
      throws CoreException {

    final IResource container = ResourcesPlugin.getWorkspace().getRoot()
        .findMember(workspaceRelativeFolder);
    printContainerContents(container);
  }

  private static void printContainerContents(final IResource container)
      throws CoreException {

    if (container != null && container.getType() == IResource.FOLDER) {
      container.refreshLocal(IResource.DEPTH_INFINITE, null);
      System.out.println("contents of folder \"" + container + "\"");
      final IResource[] members = ((IFolder) container).members();
      for (final IResource resource : members) {
        System.out.println(resource.getName());
      }
    }
  }

  public static void printFolderContents(final IPath workspaceRelativeFolder)
      throws CoreException {

    final IResource container = ResourcesPlugin.getWorkspace().getRoot()
        .findMember(workspaceRelativeFolder);
    printContainerContents(container);
  }

  public static void openEditor(final String workspaceRelativeFilePath)
      throws PartInitException {

    final IFile res = (IFile) ResourcesPlugin.getWorkspace().getRoot()
        .findMember(workspaceRelativeFilePath);
    IDE.openEditor(SWTUtil.getActivePage(), res);
  }

  public static String checkMandatoryFilesInFolder(
      final String projectRelativeOutputFolder, final String[] fileNames) {

    final String[] filesToCheck = new String[fileNames.length];
    for (int i = 0; i < fileNames.length; i++) {
      filesToCheck[i] = projectRelativeOutputFolder + '/' + fileNames[i];
    }
    return checkMandatoryFiles(filesToCheck);
  }
}
