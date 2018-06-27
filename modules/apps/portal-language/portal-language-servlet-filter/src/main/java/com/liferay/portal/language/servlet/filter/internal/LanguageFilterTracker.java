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

package com.liferay.portal.language.servlet.filter.internal;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.util.AggregateResourceBundle;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleLoaderUtil;
import com.liferay.portal.kernel.util.StringBundler;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true)
public class LanguageFilterTracker {

	@Activate
	protected void activate(final BundleContext bundleContext) {
		_serviceTracker = new ServiceTracker<>(
			bundleContext, ServletContextHelper.class,
			new ServletContextHelperServiceTrackerCustomizer(bundleContext));

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	private ServiceTracker<ServletContextHelper, ServiceTracker<?, ?>>
		_serviceTracker;

	private static class ResourceBundleLoaderServiceTrackerCustomizer
		implements
			ServiceTrackerCustomizer
				<ResourceBundleLoader, TrackedServletContextHelper> {

		public ResourceBundleLoaderServiceTrackerCustomizer(
			BundleContext bundleContext, String symbolicName,
			Object contextName) {

			_bundleContext = bundleContext;

			_properties = new HashMap<>();

			_properties.put("service.ranking", Integer.MIN_VALUE);

			_properties.put("servlet.context.name", contextName);

			_filterString = StringBundler.concat(
				"(&(resource.bundle.base.name=*)(|(bundle.symbolic.name=",
				symbolicName, ")(&(servlet.context.name=",
				String.valueOf(contextName),
				")(service.bundleid=0)))(objectClass=",
				ResourceBundleLoader.class.getName(), "))");

			_contextName = contextName;
		}

		@Override
		public TrackedServletContextHelper addingService(
			ServiceReference<ResourceBundleLoader> serviceReference) {

			List<ServiceRegistration<?>> serviceRegistrations =
				new ArrayList<>();

			Dictionary<String, Object> properties = new Hashtable<>(
				_properties);

			properties.put(
				"resource.bundle.base.name",
				serviceReference.getProperty("resource.bundle.base.name"));

			ResourceBundleLoader resourceBundleLoader =
				_bundleContext.getService(serviceReference);

			serviceRegistrations.add(
				_bundleContext.registerService(
					ResourceBundleLoader.class, resourceBundleLoader,
					properties));

			ServiceTracker<ResourceBundleLoader, ResourceBundleLoader>
				serviceTracker = ServiceTrackerFactory.open(
					_bundleContext, _filterString);

			Filter filter = new LanguageFilter(
				new ServiceTrackerResourceBundleLoader(serviceTracker));

			Dictionary<String, Object> filterProperties = new Hashtable<>();

			filterProperties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
				_contextName);
			filterProperties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER,
				new String[] {
					DispatcherType.ASYNC.toString(),
					DispatcherType.FORWARD.toString(),
					DispatcherType.INCLUDE.toString(),
					DispatcherType.REQUEST.toString()
				});
			filterProperties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_NAME,
				LanguageFilter.class.getName());
			filterProperties.put(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN,
				new String[] {"*.css", "*.js"});

			serviceRegistrations.add(
				_bundleContext.registerService(
					Filter.class, filter, filterProperties));

			return new TrackedServletContextHelper(
				serviceTracker, serviceRegistrations);
		}

		@Override
		public void modifiedService(
			ServiceReference<ResourceBundleLoader> serviceReference,
			TrackedServletContextHelper service) {

			removedService(serviceReference, service);

			addingService(serviceReference);
		}

		@Override
		public void removedService(
			ServiceReference<ResourceBundleLoader> serviceReference,
			TrackedServletContextHelper trackedServletContextHelper) {

			_bundleContext.ungetService(serviceReference);

			trackedServletContextHelper.clean();
		}

		private final BundleContext _bundleContext;
		private final Object _contextName;
		private final String _filterString;
		private final Map<String, Object> _properties;

	}

	private static class ServiceTrackerResourceBundleLoader
		implements ResourceBundleLoader {

		public ServiceTrackerResourceBundleLoader(
			ServiceTracker<ResourceBundleLoader, ResourceBundleLoader>
				serviceTracker) {

			_serviceTracker = serviceTracker;
		}

		@Override
		public ResourceBundle loadResourceBundle(Locale locale) {
			ResourceBundleLoader resourceBundleLoader =
				_serviceTracker.getService();

			ResourceBundleLoader portalResourceBundleLoader =
				ResourceBundleLoaderUtil.getPortalResourceBundleLoader();

			if (resourceBundleLoader != null) {
				ResourceBundle resourceBundle =
					resourceBundleLoader.loadResourceBundle(locale);

				if (resourceBundle != null) {
					return new AggregateResourceBundle(
						resourceBundle,
						portalResourceBundleLoader.loadResourceBundle(locale));
				}
			}

			return portalResourceBundleLoader.loadResourceBundle(locale);
		}

		/**
		 * @deprecated As of Judson, replaced by {@link
		 *             #loadResourceBundle(Locale)}
		 */
		@Deprecated
		public ResourceBundle loadResourceBundle(String languageId) {
			return loadResourceBundle(LocaleUtil.fromLanguageId(languageId));
		}

		private final ServiceTracker<ResourceBundleLoader, ResourceBundleLoader>
			_serviceTracker;

	}

	private static class ServletContextHelperServiceTrackerCustomizer
		implements
			ServiceTrackerCustomizer
				<ServletContextHelper, ServiceTracker<?, ?>> {

		public ServletContextHelperServiceTrackerCustomizer(
			BundleContext bundleContext) {

			_bundleContext = bundleContext;
		}

		@Override
		public ServiceTracker<?, ?> addingService(
			ServiceReference<ServletContextHelper> serviceReference) {

			Bundle bundle = serviceReference.getBundle();

			Object contextName = serviceReference.getProperty(
				HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME);

			return ServiceTrackerFactory.open(
				bundle.getBundleContext(),
				StringBundler.concat(
					"(&(resource.bundle.base.name=*)(bundle.symbolic.name=",
					bundle.getSymbolicName(), ")(objectClass=",
					ResourceBundleLoader.class.getName(), "))"),
				new ResourceBundleLoaderServiceTrackerCustomizer(
					_bundleContext, bundle.getSymbolicName(), contextName));
		}

		@Override
		public void modifiedService(
			ServiceReference<ServletContextHelper> serviceReference,
			ServiceTracker<?, ?> serviceTracker) {

			removedService(serviceReference, serviceTracker);

			addingService(serviceReference);
		}

		@Override
		public void removedService(
			ServiceReference<ServletContextHelper> serviceReference,
			ServiceTracker<?, ?> serviceTracker) {

			serviceTracker.close();
		}

		private final BundleContext _bundleContext;

	}

	private static class TrackedServletContextHelper {

		public TrackedServletContextHelper(
			ServiceTracker<?, ?> serviceTracker,
			List<ServiceRegistration<?>> serviceRegistrations) {

			_serviceTracker = serviceTracker;
			_serviceRegistrations = serviceRegistrations;
		}

		public void clean() {
			_serviceTracker.close();

			for (ServiceRegistration<?> serviceRegistration :
					_serviceRegistrations) {

				serviceRegistration.unregister();
			}
		}

		private final List<ServiceRegistration<?>> _serviceRegistrations;
		private ServiceTracker<?, ?> _serviceTracker;

	}

}