/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.extender.internal.context;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.bean.BeanLocatorImpl;
import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.AggregateClassLoader;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.spring.configurator.ConfigurableApplicationContextConfigurator;
import com.liferay.portal.spring.extender.internal.InitialTablesCreator;
import com.liferay.portal.spring.extender.internal.bean.ApplicationContextServicePublisherUtil;
import com.liferay.portal.spring.extender.internal.loader.ModuleAggregareClassLoader;

import java.beans.Introspector;

import java.util.Dictionary;
import java.util.List;

import javax.sql.DataSource;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWiring;

import org.springframework.beans.CachedIntrospectionResults;

/**
 * @author Miguel Pastor
 */
public class ModuleApplicationContextRegistrator {

	public ModuleApplicationContextRegistrator(
			InitialTablesCreator initialTablesCreator,
			ConfigurableApplicationContextConfigurator
				configurableApplicationContextConfigurator,
			Bundle extendeeBundle, Bundle extenderBundle)
		throws Exception {

		_initialTablesCreator = initialTablesCreator;
		_configurableApplicationContextConfigurator =
			configurableApplicationContextConfigurator;
		_extendeeBundle = extendeeBundle;
		_extenderBundle = extenderBundle;

		BundleWiring extendeeBundleWiring = _extendeeBundle.adapt(
			BundleWiring.class);

		_extendeeClassLoader = extendeeBundleWiring.getClassLoader();

		_classLoader = new ModuleAggregareClassLoader(
			_extendeeClassLoader, _extendeeBundle.getSymbolicName());

		Dictionary<String, String> headers = _extendeeBundle.getHeaders(
			StringPool.BLANK);

		_moduleApplicationContext = new ModuleApplicationContext(
			_extendeeBundle, _extendeeClassLoader, _classLoader,
			StringUtil.split(
				headers.get("Liferay-Spring-Context"), CharPool.COMMA));

		_moduleApplicationContext.addBeanFactoryPostProcessor(
			beanFactory -> ModuleApplicationContext.registerDataSourceBean(
				beanFactory, _extendeeClassLoader));

		_moduleApplicationContext.addBeanFactoryPostProcessor(
			new ModuleBeanFactoryPostProcessor(
				_extendeeBundle.getBundleContext()));

		_configurableApplicationContextConfigurator.configure(
			_moduleApplicationContext);

		_registerDataSource();

		_createInitialTables();
	}

	public void stop() {
		ApplicationContextServicePublisherUtil.unregisterContext(
			_serviceRegistrations);

		if (_dataSourceServiceRegistration != null) {
			_dataSourceServiceRegistration.unregister();

			_dataSourceServiceRegistration = null;
		}

		_moduleApplicationContext.close();
	}

	protected void start() throws Exception {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		currentThread.setContextClassLoader(
			AggregateClassLoader.getAggregateClassLoader(
				PortalClassLoaderUtil.getClassLoader(), contextClassLoader));

		try {
			_moduleApplicationContext.refresh();

			_registerDataSource();

			_createInitialTables();

			BundleWiring bundleWiring = _extendeeBundle.adapt(
				BundleWiring.class);

			PortletBeanLocatorUtil.setBeanLocator(
				_extendeeBundle.getSymbolicName(),
				new BeanLocatorImpl(
					bundleWiring.getClassLoader(), _moduleApplicationContext));

			_serviceRegistrations =
				ApplicationContextServicePublisherUtil.registerContext(
					_moduleApplicationContext,
					_extendeeBundle.getBundleContext());
		}
		catch (Exception exception) {
			throw new Exception(
				"Unable to start " + _extendeeBundle.getSymbolicName(),
				exception);
		}
		finally {
			CachedIntrospectionResults.clearClassLoader(_classLoader);

			CachedIntrospectionResults.clearClassLoader(_extendeeClassLoader);

			BundleWiring extenderBundleWiring = _extenderBundle.adapt(
				BundleWiring.class);

			CachedIntrospectionResults.clearClassLoader(
				extenderBundleWiring.getClassLoader());

			Introspector.flushCaches();

			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	private void _createInitialTables() throws Exception {
		_initialTablesCreator.create(
			_extendeeBundle, _moduleApplicationContext.getDataSource());
	}

	private void _registerDataSource() {
		if (_dataSourceServiceRegistration == null) {
			BundleContext bundleContext = _extendeeBundle.getBundleContext();

			_dataSourceServiceRegistration = bundleContext.registerService(
				DataSource.class, _moduleApplicationContext.getDataSource(),
				MapUtil.singletonDictionary(
					"origin.bundle.symbolic.name",
					_extendeeBundle.getSymbolicName()));
		}
	}

	private final ClassLoader _classLoader;
	private final ConfigurableApplicationContextConfigurator
		_configurableApplicationContextConfigurator;
	private volatile ServiceRegistration<DataSource>
		_dataSourceServiceRegistration;
	private final Bundle _extendeeBundle;
	private final ClassLoader _extendeeClassLoader;
	private final Bundle _extenderBundle;
	private final InitialTablesCreator _initialTablesCreator;
	private final ModuleApplicationContext _moduleApplicationContext;
	private List<ServiceRegistration<?>> _serviceRegistrations;

}