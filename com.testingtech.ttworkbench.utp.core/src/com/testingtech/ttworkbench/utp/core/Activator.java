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

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import com.testingtech.ttworkbench.core.ui.AbstractLicensedPlugin;
import com.testingtech.ttworkbench.core.ui.PluginLicenseHandler;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractLicensedPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.testingtech.ttworkbench.utp.core"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private PluginLicenseHandler licenseHandler = null;

	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

  /**
   * @see com.testingtech.ttworkbench.core.ui.AbstractLicensedPlugin#getDefaultInstance()
   */
  public Plugin getDefaultInstance() {
    return getDefault();
  }

  /**
   * @see com.testingtech.ttworkbench.core.ui.AbstractLicensedPlugin#getPluginLicenseHandler()
   */
  public PluginLicenseHandler getPluginLicenseHandler() {
    if (licenseHandler == null) {
      licenseHandler = new PluginLicenseHandler("1.0", "UTP-EP", this, this.getBundle().getSymbolicName()); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    return licenseHandler;
  }

  /**
   * Returns the path where the license file should be located.
   * 
   * @return  the path where the license file should be located
   */
  public String getLicensePath() {
    return getPluginLicenseHandler().getLicensePath();
  }


}
