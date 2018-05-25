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

package com.liferay.apio.architect.wiring.osgi.internal.registrar.resource;

import static com.liferay.apio.architect.wiring.osgi.internal.manager.TypeArgumentProperties.KEY_IDENTIFIER_CLASS;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.util.ManagerUtil.createServiceTracker;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.util.ManagerUtil.getTypeParamTry;

import static org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL;
import static org.osgi.service.component.annotations.ReferencePolicyOption.GREEDY;

import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.logger.ApioLogger;
import com.liferay.apio.architect.representor.Representable;
import com.liferay.apio.architect.resource.CollectionResource;
import com.liferay.apio.architect.router.CollectionRouter;
import com.liferay.apio.architect.router.ItemRouter;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * Allow developers to register resources as {@link CollectionResource}, instead
 * of implementing a register for each of the enclosing interfaces separately.
 *
 * @author Alejandro Hernández
 */
@Component(immediate = true)
public class CollectionResourceRegistrar {

	@Activate
	protected void activate(BundleContext bundleContext) {
		String[] classes = {
			CollectionRouter.class.getName(), ItemRouter.class.getName(),
			Representable.class.getName()
		};

		_serviceTracker = createServiceTracker(
			bundleContext, CollectionResource.class, classes,
			(properties, service) -> {
				Try<Class<Object>> classTry = getTypeParamTry(
					service, CollectionResource.class, 2);

				classTry.voidFold(
					__ -> _warning(
						"Unable to get generic identifier class from " +
							service.getClass()),
					clazz -> properties.put(KEY_IDENTIFIER_CLASS, clazz));
			});

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	private void _warning(String message) {
		if (_apioLogger != null) {
			_apioLogger.warning(message);
		}
	}

	@Reference(cardinality = OPTIONAL, policyOption = GREEDY)
	private ApioLogger _apioLogger;

	private ServiceTracker<CollectionResource, ServiceRegistration<?>>
		_serviceTracker;

}