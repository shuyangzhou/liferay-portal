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

package com.liferay.wiki.engine.impl;

import com.liferay.wiki.engine.WikiEngine;
import com.liferay.wiki.internal.util.WikiCacheHelper;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Iván Zaera
 */
@Component(immediate = true, service = WikiEngineTracker.class)
public class WikiEngineTracker {

	public Collection<String> getFormats() {
		return _wikiEngineMap.keySet();
	}

	public WikiEngine getWikiEngine(String format) {
		return _wikiEngineMap.get(format);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = new ServiceTracker<>(
			bundleContext, WikiEngine.class,
			new WikiEngineServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	private BundleContext _bundleContext;
	private ServiceTracker<WikiEngine, WikiEngine> _serviceTracker;

	@Reference
	private WikiCacheHelper _wikiCacheHelper;

	private final Map<String, WikiEngine> _wikiEngineMap =
		new ConcurrentHashMap<>();

	private class WikiEngineServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<WikiEngine, WikiEngine> {

		@Override
		public WikiEngine addingService(
			ServiceReference<WikiEngine> serviceReference) {

			WikiEngine wikiEngine = _bundleContext.getService(serviceReference);

			_wikiEngineMap.put(wikiEngine.getFormat(), wikiEngine);

			_wikiCacheHelper.clearCache();

			return wikiEngine;
		}

		@Override
		public void modifiedService(
			ServiceReference<WikiEngine> serviceReference,
			WikiEngine wikiEngine) {

			removedService(serviceReference, wikiEngine);
			addingService(serviceReference);
		}

		@Override
		public void removedService(
			ServiceReference<WikiEngine> serviceReference,
			WikiEngine wikiEngine) {

			_bundleContext.ungetService(serviceReference);

			_wikiEngineMap.remove(wikiEngine.getFormat());
		}

	}

}