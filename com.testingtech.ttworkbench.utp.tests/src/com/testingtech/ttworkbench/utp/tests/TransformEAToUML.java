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
package com.testingtech.ttworkbench.utp.tests;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.testingtech.ttworkbench.utp.ea.Transformer;

public class TransformEAToUML implements IObjectActionDelegate {

  private final class TransformJob extends Job {

    TransformJob(final String name) {

      super(name);
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {

      final String name = fileToProcess.getName();
      if (UTPTestsActivator.getDefault().isDebugging()) {
        System.out.println("processing " + name);
      }

      final String basename = name.substring(0, name
          .lastIndexOf('.' + fileToProcess.getFileExtension()));
      final IFile destination = fileToProcess.getParent().getFile(
          new Path(basename + ".uml"));

      if (UTPTestsActivator.getDefault().isDebugging()) {
        System.out.println("output file will be "
                           + destination.getFullPath().toOSString());
      }

      Transformer transformer;
      try {
        transformer = new Transformer();
      }
      catch (Exception e) {
        return new Status(IStatus.ERROR, UTPTestsActivator.PLUGIN_ID, 1,
            "transformation failed", e);
      }
      final String source = fileToProcess.getLocation().toOSString();
      final boolean isTransformed = transformer.transform(monitor, source,
          destination);

      try {
        destination.refreshLocal(IResource.DEPTH_ZERO, monitor);
      }
      catch (final CoreException e) {
        if (UTPTestsActivator.getDefault().isDebugging()) {
          System.err.println("refreshing failed with exception");
          e.printStackTrace(System.err);
        }
      }
      if (isTransformed) {
        return Status.OK_STATUS;
      }
      else {
        return new Status(IStatus.ERROR, UTPTestsActivator.PLUGIN_ID, 1,
            "transformation failed", null);
      }

    }
  }

  IFile fileToProcess;

  public TransformEAToUML() {

    // TODO Auto-generated constructor stub
  }

  public void setActivePart(final IAction action,
      final IWorkbenchPart targetPart) {

    // TODO Auto-generated method stub

  }

  public void run(final IAction action) {

    if (fileToProcess != null) {

      final TransformJob transformJob = new TransformJob("converting EA to UML2");
      transformJob.setUser(true);
      transformJob.schedule();

    }

  }

  public void selectionChanged(final IAction action, final ISelection selection) {

    fileToProcess = null;
    boolean thereIsAFileToProcess = false;

    if (selection instanceof IStructuredSelection) {
      final IStructuredSelection ssel = (IStructuredSelection) selection;
      final Iterator iter = ssel.iterator();

      while (iter.hasNext()) {
        final Object o = iter.next();

        if (o instanceof IFile) {
          fileToProcess = (IFile) o;
          thereIsAFileToProcess = true;
        }
        else if (o instanceof IAdaptable) {
          final IAdaptable adaptable = (IAdaptable) o;
          fileToProcess = (IFile) adaptable.getAdapter(IFile.class);
          thereIsAFileToProcess = true;
        }
      }
    }

    action.setEnabled(thereIsAFileToProcess);

  }

}
