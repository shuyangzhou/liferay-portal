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

package com.liferay.apio.architect.wiring.osgi.internal.service.tracker.customizer;

import static com.liferay.apio.architect.wiring.osgi.internal.manager.ManagerCache.INSTANCE;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.TypeArgumentProperties.KEY_PRINCIPAL_TYPE_ARGUMENT;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.util.ManagerUtil.getGenericClassFromPropertyOrElse;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.util.ManagerUtil.getTypeParamOrFail;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Represents a {@code ServiceTrackerCustomizer} that transforms the service to
 * another type.
 *
 * @author Alejandro Hernández
 */
public abstract class TransformServiceTrackerCustomizer<T, U>
	implements ServiceTrackerCustomizer<T, U> {

	public TransformServiceTrackerCustomizer(Class<T> managedClass) {
		_managedClass = managedClass;
	}

	@Override
	public U addingService(ServiceReference<T> serviceReference) {
		Bundle bundle = FrameworkUtil.getBundle(
			TransformServiceTrackerCustomizer.class);

		BundleContext bundleContext = bundle.getBundleContext();

		T t = bundleContext.getService(serviceReference);

		Class<?> clazz = getGenericClassFromPropertyOrElse(
			serviceReference, KEY_PRINCIPAL_TYPE_ARGUMENT,
			() -> getTypeParamOrFail(
				t, _managedClass, getPrincipalTypeParamPosition()));

		U u = map(t, serviceReference, clazz);

		onAddedService(serviceReference, u);

		return u;
	}

	/**
	 * Returns the principal type parameter's position.
	 *
	 * @return the position
	 */
	public Integer getPrincipalTypeParamPosition() {
		return 2;
	}

	@Override
	public void modifiedService(ServiceReference<T> serviceReference, U u) {
	}

	@Override
	public void removedService(ServiceReference<T> serviceReference, U u) {
		Bundle bundle = FrameworkUtil.getBundle(
			TransformServiceTrackerCustomizer.class);

		BundleContext bundleContext = bundle.getBundleContext();

		bundleContext.ungetService(serviceReference);

		onRemovedService(serviceReference, u);
	}

	/**
	 * Returns the managed class.
	 *
	 * @return the managed class
	 */
	protected Class<T> getManagedClass() {
		return _managedClass;
	}

	/**
	 * Transforms a service of type {@code T} into a {@code U}.
	 *
	 * @param  t the service to transform
	 * @param  serviceReference the service reference
	 * @param  clazz the generic class
	 * @return the transformed service
	 */
	protected abstract U map(
		T t, ServiceReference<T> serviceReference, Class<?> clazz);

	/**
	 * Called when the service is being added.
	 *
	 * @param  serviceReference the service reference being added
	 * @param  u the service being added
	 * @review
	 */
	protected void onAddedService(ServiceReference<T> serviceReference, U u) {
		INSTANCE.clear();
	}

	/**
	 * Called when the service is being removed.
	 *
	 * @param serviceReference the service reference being removed
	 * @param u the service being removed
	 */
	protected void onRemovedService(ServiceReference<T> serviceReference, U u) {
		INSTANCE.clear();
	}

	private final Class<T> _managedClass;

}