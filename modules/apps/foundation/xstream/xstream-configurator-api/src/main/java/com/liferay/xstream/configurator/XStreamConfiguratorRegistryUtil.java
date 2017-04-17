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

package com.liferay.xstream.configurator;

import aQute.bnd.annotation.ProviderType;

import com.liferay.exportimport.kernel.xstream.XStreamAlias;
import com.liferay.exportimport.kernel.xstream.XStreamAliasRegistryUtil;
import com.liferay.exportimport.kernel.xstream.XStreamConverter;
import com.liferay.exportimport.kernel.xstream.XStreamType;
import com.liferay.exportimport.xstream.ConverterAdapter;
import com.liferay.exportimport.xstream.XStreamStagedModelTypeHierarchyPermission;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.concurrent.ConcurrentHashSet;
import com.liferay.portal.kernel.util.AggregateClassLoader;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Mate Thurzo
 */
@Component(enabled = true, immediate = true)
@ProviderType
public class XStreamConfiguratorRegistryUtil {

	public static ClassLoader getConfiguratorsClassLoader(
		ClassLoader masterClassLoader) {

		Set<ClassLoader> classLoaders = new HashSet<>();

		Set<XStreamConfigurator> xStreamConfigurators =
			_instance._getXStreamConfigurators();

		for (XStreamConfigurator xStreamConfigurator : xStreamConfigurators) {
			Class<?> clazz = xStreamConfigurator.getClass();

			classLoaders.add(clazz.getClassLoader());
		}

		// Temporary code to fetch class loaders from the old framework too

		Map<Class<?>, String> aliases = XStreamAliasRegistryUtil.getAliases();

		if (MapUtil.isNotEmpty(aliases)) {
			for (Class<?> clazz : aliases.keySet()) {
				classLoaders.add(clazz.getClassLoader());
			}
		}

		return AggregateClassLoader.getAggregateClassLoader(
			masterClassLoader,
			classLoaders.toArray(new ClassLoader[classLoaders.size()]));
	}

	public static XStream getXStream() {
		XStream xStream = _xStream;

		if (xStream == null) {
			xStream = _buildXStream();

			_xStream = xStream;
		}

		return xStream;
	}

	public static Set<XStreamConfigurator> getXStreamConfigurators() {
		return _instance._getXStreamConfigurators();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.open(
			_bundleContext, XStreamConfigurator.class,
			new XStreamConfiguratorServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private static XStream _buildXStream() {
		XStream xStream = new XStream(
			null, new XppDriver(),
			new ClassLoaderReference(
				getConfiguratorsClassLoader(XStream.class.getClassLoader())));

		xStream.omitField(HashMap.class, "cache_bitmask");

		if (_xStreamConfigurators.isEmpty()) {
			return xStream;
		}

		List<String> allowedTypeNames = new ArrayList<>();

		for (XStreamConfigurator xStreamConfigurator : _xStreamConfigurators) {
			List<XStreamAlias> xStreamAliases =
				xStreamConfigurator.getXStreamAliases();

			if (ListUtil.isNotEmpty(xStreamAliases)) {
				for (XStreamAlias xStreamAlias : xStreamAliases) {
					xStream.alias(
						xStreamAlias.getName(), xStreamAlias.getClazz());
				}
			}

			List<XStreamConverter> xStreamConverters =
				xStreamConfigurator.getXStreamConverters();

			if (ListUtil.isNotEmpty(xStreamConverters)) {
				for (XStreamConverter xStreamConverter : xStreamConverters) {
					xStream.registerConverter(
						new ConverterAdapter(xStreamConverter),
						XStream.PRIORITY_VERY_HIGH);
				}
			}

			List<XStreamType> xStreamTypes =
				xStreamConfigurator.getAllowedXStreamTypes();

			if (ListUtil.isNotEmpty(xStreamTypes)) {
				for (XStreamType xStreamType : xStreamTypes) {
					allowedTypeNames.add(xStreamType.getTypeExpression());
				}
			}
		}

		// For default permissions, first wipe than add default

		xStream.addPermission(NoTypePermission.NONE);

		// Add permissions

		xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xStream.addPermission(
			XStreamStagedModelTypeHierarchyPermission.STAGED_MODELS);

		xStream.allowTypes(_XSTREAM_DEFAULT_ALLOWED_TYPES);

		xStream.allowTypeHierarchy(List.class);
		xStream.allowTypeHierarchy(Map.class);
		xStream.allowTypeHierarchy(Timestamp.class);
		xStream.allowTypeHierarchy(Set.class);

		xStream.allowTypes(allowedTypeNames.toArray(new String[0]));

		xStream.allowTypesByWildcard(
			new String[] {
				"com.thoughtworks.xstream.mapper.DynamicProxyMapper*"
			});

		return xStream;
	}

	private Set<XStreamConfigurator> _getXStreamConfigurators() {
		return _xStreamConfigurators;
	}

	private static final Class<?>[] _XSTREAM_DEFAULT_ALLOWED_TYPES = {
		boolean[].class, byte[].class, Date.class, Date[].class, double[].class,
		float[].class, int[].class, Locale.class, long[].class, Number.class,
		Number[].class, short[].class, String.class, String[].class
	};

	private static final XStreamConfiguratorRegistryUtil _instance =
		new XStreamConfiguratorRegistryUtil();

	private static transient XStream _xStream;
	private static final Set<XStreamConfigurator> _xStreamConfigurators =
		new ConcurrentHashSet<>();

	private BundleContext _bundleContext;
	private ServiceTracker<XStreamConfigurator, XStreamConfigurator>
		_serviceTracker;

	private class XStreamConfiguratorServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<XStreamConfigurator, XStreamConfigurator> {

		@Override
		public XStreamConfigurator addingService(
			ServiceReference<XStreamConfigurator> serviceReference) {

			XStreamConfigurator xStreamConfigurator = _bundleContext.getService(
				serviceReference);

			_xStreamConfigurators.add(xStreamConfigurator);

			_xStream = null;

			return xStreamConfigurator;
		}

		@Override
		public void modifiedService(
			ServiceReference<XStreamConfigurator> serviceReference,
			XStreamConfigurator xStreamConfigurator) {

			removedService(serviceReference, xStreamConfigurator);

			addingService(serviceReference);
		}

		@Override
		public void removedService(
			ServiceReference<XStreamConfigurator> serviceReference,
			XStreamConfigurator xStreamConfigurator) {

			_bundleContext.ungetService(serviceReference);

			_xStreamConfigurators.remove(xStreamConfigurator);

			_xStream = null;
		}

	}

}