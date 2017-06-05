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

package com.liferay.portal.editor.configuration;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;
import com.liferay.registry.util.StringPlus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Sergio González
 * @author Preston Crary
 */
public abstract class BaseEditorConfigurationProvider<T> {

	public BaseEditorConfigurationProvider(Class<T> editorContributorClass) {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			editorContributorClass,
			new EditorContributorServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	public void destroy() {
		_serviceTracker.close();
	}

	protected List<T> getContributors(
		String portletName, String editorConfigKey, String editorName) {

		List<T> contributors = new ArrayList<>();

		List<EditorContributorProvider<T>> editorContributorProviders =
			_editorContributorsProviders.get();

		for (EditorContributorProvider<T> editorContributorProvider :
				editorContributorProviders) {

			if (editorContributorProvider.matches(
					portletName, editorConfigKey, editorName)) {

				contributors.add(editorContributorProvider.get());
			}
		}

		return contributors;
	}

	private volatile AtomicReference<List<EditorContributorProvider<T>>>
		_editorContributorsProviders = new AtomicReference<>();
	private final ServiceTracker<T, ?> _serviceTracker;

	private static class EditorContributorProvider<T>
		implements Comparable<EditorContributorProvider<T>> {

		@Override
		public int compareTo(
			EditorContributorProvider<T> editorContributorProvider) {

			if (_priority < editorContributorProvider._priority) {
				return -1;
			}

			if (_priority > editorContributorProvider._priority) {
				return 1;
			}

			return editorContributorProvider._serviceRanking - _serviceRanking;
		}

		public T get() {
			return _editorContributor;
		}

		public boolean matches(
			String portletName, String editorConfigKey, String editorName) {

			if (_matches(portletName, _portletNames) &&
				_matches(editorConfigKey, _editorConfigKeys) &&
				_matches(editorName, _editorNames)) {

				return true;
			}

			return false;
		}

		private EditorContributorProvider(
			T editorContributor, List<String> portletNames,
			List<String> editorConfigKeys, List<String> editorNames,
			int serviceRanking) {

			_editorContributor = editorContributor;

			_portletNames = _toSet(portletNames);
			_editorConfigKeys = _toSet(editorConfigKeys);
			_editorNames = _toSet(editorNames);

			_serviceRanking = serviceRanking;

			_priority = _getPriority();
		}

		private int _getPriority() {
			if (_portletNames.isEmpty() && _editorConfigKeys.isEmpty() &&
				_editorNames.isEmpty()) {

				return 7;
			}

			if (_portletNames.isEmpty() && _editorConfigKeys.isEmpty()) {
				return 6;
			}

			if (!_portletNames.isEmpty() && _editorConfigKeys.isEmpty() &&
				_editorNames.isEmpty()) {

				return 5;
			}

			if (_portletNames.isEmpty() && _editorNames.isEmpty()) {
				return 4;
			}

			if (!_portletNames.isEmpty() && _editorConfigKeys.isEmpty()) {
				return 3;
			}

			if (_portletNames.isEmpty()) {
				return 2;
			}

			if (_editorNames.isEmpty()) {
				return 1;
			}

			return 0;
		}

		private boolean _matches(String name, Set<String> names) {
			if (names.isEmpty() || names.contains(name)) {
				return true;
			}

			return false;
		}

		private Set<String> _toSet(List<String> names) {
			if (names.isEmpty()) {
				return Collections.emptySet();
			}

			return new HashSet<>(names);
		}

		private final Set<String> _editorConfigKeys;
		private final T _editorContributor;
		private final Set<String> _editorNames;
		private final Set<String> _portletNames;
		private final int _priority;
		private final int _serviceRanking;

	}

	private class EditorContributorServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<T, T> {

		@Override
		public T addingService(ServiceReference<T> serviceReference) {
			Registry registry = RegistryUtil.getRegistry();

			T editorOptionsContributor = registry.getService(serviceReference);

			List<String> portletNames = StringPlus.asList(
				serviceReference.getProperty("javax.portlet.name"));
			List<String> editorConfigKeys = StringPlus.asList(
				serviceReference.getProperty("editor.config.key"));
			List<String> editorNames = StringPlus.asList(
				serviceReference.getProperty("editor.name"));

			int serviceRanking = GetterUtil.getInteger(
				serviceReference.getProperty("service.ranking"));

			EditorContributorProvider<T> editorContributorProvider =
				new EditorContributorProvider<>(
					editorOptionsContributor, portletNames, editorConfigKeys,
					editorNames, serviceRanking);

			while (true) {
				List<EditorContributorProvider<T>> editorContributorProviders =
					null;

				List<EditorContributorProvider<T>>
					oldEditorContributorProviders =
						_editorContributorsProviders.get();

				if (oldEditorContributorProviders == null) {
					editorContributorProviders = new ArrayList<>();
				}
				else {
					editorContributorProviders = new ArrayList<>(
						oldEditorContributorProviders);
				}

				int index = 0;

				while (index < editorContributorProviders.size()) {
					EditorContributorProvider<T> curEditorContributorProvider =
						editorContributorProviders.get(index);

					if (curEditorContributorProvider.compareTo(
							editorContributorProvider) <= 0) {

						break;
					}

					index++;
				}

				editorContributorProviders.add(
					index, editorContributorProvider);

				if (_editorContributorsProviders.compareAndSet(
						oldEditorContributorProviders,
						editorContributorProviders)) {

					return editorOptionsContributor;
				}
			}
		}

		@Override
		public void modifiedService(
			ServiceReference<T> serviceReference, T editorOptionsContributor) {

			removedService(serviceReference, editorOptionsContributor);

			addingService(serviceReference);
		}

		@Override
		public void removedService(
			ServiceReference<T> serviceReference, T editorContributor) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			while (true) {
				List<EditorContributorProvider<T>>
					oldEditorContributorProviders =
						_editorContributorsProviders.get();

				List<EditorContributorProvider<T>> editorContributorProviders =
					new ArrayList<>(oldEditorContributorProviders);

				Iterator<EditorContributorProvider<T>> iterator =
					editorContributorProviders.iterator();

				while (iterator.hasNext()) {
					EditorContributorProvider<T> editorContributorProvider =
						iterator.next();

					if (editorContributorProvider._editorContributor ==
							editorContributor) {

						iterator.remove();

						break;
					}
				}

				if (_editorContributorsProviders.compareAndSet(
						oldEditorContributorProviders,
						editorContributorProviders)) {

					return;
				}
			}
		}

	}

}