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

package com.liferay.arquillian.extension.junit.bridge.remote.instanceproducer;

import com.liferay.arquillian.extension.junit.bridge.remote.processor.service.BundleActivatorsManager;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;

/**
 * @author Cristina González
 */
public class OSGiAllInBundleInstanceProducer {

	public void createInstanceProducer(
		@Observes ArquillianDescriptor arquillianDescriptor) {

		ServiceLoader serviceLoader = _serviceLoaderInstance.get();

		_bundleActivatorsManagerInstanceProducer.set(
			serviceLoader.onlyOne(BundleActivatorsManager.class));
	}

	@ApplicationScoped
	@Inject
	private InstanceProducer<BundleActivatorsManager>
		_bundleActivatorsManagerInstanceProducer;

	@Inject
	private Instance<ServiceLoader> _serviceLoaderInstance;

}