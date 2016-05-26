/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.osgi.web.wab.generator.internal;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 */
@Component(immediate = true)
public class FileInstallRestarter {

	@Activate
	public void activate(BundleContext bundleContext) throws BundleException {
		for (Bundle bundle : bundleContext.getBundles()) {
			if ("org.apache.felix.fileinstall".equals(
					bundle.getSymbolicName())) {

				bundle.stop(Bundle.STOP_TRANSIENT);

				bundle.start(Bundle.START_TRANSIENT);

				break;
			}
		}
	}

	@Reference
	public void setWabGenerator(
		com.liferay.portal.osgi.web.wab.generator.WabGenerator wabGenerator) {
	}

}