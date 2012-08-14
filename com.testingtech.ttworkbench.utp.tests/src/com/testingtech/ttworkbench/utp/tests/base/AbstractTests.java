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

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.testingtech.ttworkbench.core.CorePlugin;
import com.testingtech.ttworkbench.core.ttcn3.ITTCN3Project;
import com.testingtech.ttworkbench.core.projectpackets.ProjectPacketOperation;
import com.testingtech.ttworkbench.metamodel.core.foreignmodel.ForeignModelTargetDescriptor;
import com.testingtech.ttworkbench.utp.ttcn3.legacy.UTP2TTCN3TransformationManager;
import com.testingtech.ttworkbench.utp.ea.Transformer;

public abstract class AbstractTests extends TestCase {

  public abstract String getProjectName();

  @Override
  protected void setUp() throws Exception {

    if (!TestUtil.resExists(getProjectName())) {
      // ok, if one of the needed folders exists
      ProjectPacketOperation.createProjectPackets(
          "com.testingtech.ttworkbench.utp", "test");
    }
    if (!TestUtil.resExists(getProjectName())) {

      fail("project not created");
    }
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {

    super.tearDown();
  }

  public final void eaTransformAndCheck(final String projectRelativeInputFile,
      final String projectRelativeOutputFolder, final String outputFile)
      throws Exception {

    final IProgressMonitor monitor = new NullProgressMonitor();
    final Transformer transformer = new Transformer();

    final String inputWorkspaceRelative = getProjectName() + "/"
                                          + projectRelativeInputFile;
    final String outputWorkspaceRelative = getOutputPrjName() + "/"
                                           + projectRelativeOutputFolder + "/"
                                           + outputFile;

    final Path outputPath = new Path(outputWorkspaceRelative);

    final IFile destination = ResourcesPlugin.getWorkspace().getRoot().getFile(
        outputPath);

    prepareOutputPath(getOutputPrjName(), projectRelativeOutputFolder, monitor);

    final IFile sourceFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
        new Path(inputWorkspaceRelative));
    final String source = sourceFile.getLocation().toOSString();
    boolean isTransformed = transformer.transform(monitor, source, destination);

    if (!isTransformed) {
      fail("ea to uml transformation failed");
    }
    try {
      destination.refreshLocal(IResource.DEPTH_ZERO, monitor);
    }
    catch (final CoreException e) {
      fail("refreshing failed");
    }

    // TODO workspace / project relative
    // TODO with or without slash
    final String checkMessage = TestUtil
        .checkMandatoryFile(outputWorkspaceRelative);
    if (checkMessage.length() > 0) {
      fail(checkMessage);
    }

    TestUtil.openEditor(outputWorkspaceRelative);

  }

  public final void umlTransformAndCheck(final String inputProject,
      final String outputProject, final String projectRelativeInputFile,
      final String projectRelativeOutputFolder, final String rootModuleName,
      final String[] expectedGeneratedTTCN3) throws Exception {

    final IProgressMonitor monitor = new NullProgressMonitor();

    final IPath outputPath = prepareOutputPath(outputProject,
        projectRelativeOutputFolder, monitor);

    final ForeignModelTargetDescriptor targetDescr = new ForeignModelTargetDescriptor(
        outputPath, rootModuleName);
    final UTP2TTCN3TransformationManager converter = new UTP2TTCN3TransformationManager(
        "platform:/resource" + inputProject + projectRelativeInputFile,
        targetDescr);

    converter.transform();
    converter.updateTarget();
    converter.dispose();

    ResourcesPlugin.getWorkspace().getRoot().getProject(inputProject)
        .refreshLocal(IResource.DEPTH_INFINITE, monitor);
    ResourcesPlugin.getWorkspace().getRoot().getProject(outputProject)
        .refreshLocal(IResource.DEPTH_INFINITE, monitor);

    final String checkMessage = TestUtil.checkMandatoryFilesInFolder(
        outputProject + projectRelativeOutputFolder, expectedGeneratedTTCN3);
    if (checkMessage.length() > 0) {
      fail(checkMessage);
    }

    // TODO open files
  }

  private IPath prepareOutputPath(final String outputProjectName,
      final String projectRelativeOutputFolder, final IProgressMonitor monitor)
      throws CoreException {

    final IPath outputPath = new Path(outputProjectName
                                      + projectRelativeOutputFolder);

    final IProject createdProject = createTTCN3Project(monitor,
        outputProjectName);
    checkTTCN3Folder(monitor, createdProject, projectRelativeOutputFolder);
    return outputPath;
  }

  protected IProject createTTCN3Project(
      final IProgressMonitor nullProgressMonitor, final String outputPrjName)
      throws CoreException {

    final IProject outputProject = ResourcesPlugin.getWorkspace().getRoot()
        .getProject(outputPrjName);

    if (!outputProject.exists()) {
      final IProjectDescription desc = outputProject.getWorkspace()
          .newProjectDescription(outputProject.getName());
      outputProject.create(desc, nullProgressMonitor);
    }
    if (!outputProject.isOpen()) {
      outputProject.open(nullProgressMonitor);
    }

    CorePlugin.addTTCN3Nature(outputProject);
    final ITTCN3Project ttcn3Project = (ITTCN3Project) outputProject
        .getAdapter(ITTCN3Project.class);
    if (ttcn3Project != null) {
      ttcn3Project.checkAndUpdate();
      outputProject.refreshLocal(IResource.DEPTH_INFINITE, nullProgressMonitor);
    }
    else {
      fail("output project could not be created");
    }
    return outputProject;
  }

  public String getOutputPrjName() {

    return getProjectName() + "-out";
  }

  private static void checkTTCN3Folder(final IProgressMonitor monitor,
      final IProject project, final String projectRelativePath)
      throws CoreException {

    final IFolder outputFolder = project.getFolder(projectRelativePath);

    if (!outputFolder.exists()) {
      outputFolder.create(true, true, monitor);
      final ITTCN3Project ttcn3Prj = (ITTCN3Project) outputFolder.getProject()
          .getAdapter(ITTCN3Project.class);
      if (ttcn3Prj != null) {
        ttcn3Prj.addSourceFolder(outputFolder);
      }
    }
  }

}
