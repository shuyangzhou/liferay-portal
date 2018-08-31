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

package com.liferay.apio.architect.impl.wiring.osgi.manager.documentation.contributor;

import static com.liferay.apio.architect.documentation.contributor.CustomDocumentation.Builder;
import static com.liferay.apio.architect.impl.wiring.osgi.manager.cache.ManagerCache.INSTANCE;

import com.liferay.apio.architect.documentation.contributor.CustomDocumentation;
import com.liferay.apio.architect.documentation.contributor.CustomDocumentationContributor;
import com.liferay.apio.architect.impl.documentation.contributor.CustomDocumentationImpl.BuilderImpl;
import com.liferay.osgi.service.tracker.collections.internal.DefaultServiceTrackerCustomizer;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Víctor Galán
 */
@Component
public class CustomDocumentationManagerImpl
	implements CustomDocumentationManager {

	@Activate
	public void activate(BundleContext bundleContext) {
		_serviceTrackerList = _openServiceTrackerList(bundleContext);

		INSTANCE.clear();
	}

	@Deactivate
	public void deactivate() {
		_serviceTrackerList.close();

		INSTANCE.clear();
	}

	@Override
	public CustomDocumentation getCustomDocumentation() {
		return INSTANCE.getDocumentationContribution(
			this::_computeDocumentationContribution);
	}

	private void _computeDocumentationContribution() {
		Builder builder = new BuilderImpl();

		Iterable<CustomDocumentationContributor> iterable =
			() -> _serviceTrackerList.iterator();

		Stream<CustomDocumentationContributor> stream = StreamSupport.stream(
			iterable.spliterator(), false);

		stream.forEach(
			customDocumentationContributor ->
				customDocumentationContributor.customDocumentation(builder));

		CustomDocumentation customDocumentation = builder.build();

		INSTANCE.putDocumentationContribution(customDocumentation);
	}

	private ServiceTrackerList
		<CustomDocumentationContributor, CustomDocumentationContributor>
			_openServiceTrackerList(BundleContext bundleContext) {

		return ServiceTrackerListFactory.open(
			bundleContext, CustomDocumentationContributor.class, null,
			new DefaultServiceTrackerCustomizer<CustomDocumentationContributor>(
				bundleContext) {

				@Override
				public CustomDocumentationContributor addingService(
					ServiceReference<CustomDocumentationContributor>
						serviceReference) {

					INSTANCE.clear();

					return super.addingService(serviceReference);
				}

				@Override
				public void removedService(
					ServiceReference<CustomDocumentationContributor>
						serviceReference,
					CustomDocumentationContributor
						customDocumentationContributor) {

					INSTANCE.clear();

					super.removedService(
						serviceReference, customDocumentationContributor);
				}

			});
	}

	private ServiceTrackerList
		<CustomDocumentationContributor, CustomDocumentationContributor>
			_serviceTrackerList;

}