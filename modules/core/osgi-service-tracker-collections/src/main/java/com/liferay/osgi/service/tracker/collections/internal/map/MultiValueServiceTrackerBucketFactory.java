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

package com.liferay.osgi.service.tracker.collections.internal.map;

import com.liferay.osgi.service.tracker.collections.ServiceReferenceServiceTuple;
import com.liferay.osgi.service.tracker.collections.internal.ServiceReferenceServiceTupleComparator;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerBucket;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerBucketFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.osgi.framework.ServiceReference;

/**
 * @author Carlos Sierra Andrés
 */
public class MultiValueServiceTrackerBucketFactory<SR, TS>
	implements ServiceTrackerBucketFactory<SR, TS, List<TS>> {

	public MultiValueServiceTrackerBucketFactory() {
		_serviceReferenceServiceTupleComparator =
			new ServiceReferenceServiceTupleComparator<>(
				Collections.reverseOrder());
	}

	public MultiValueServiceTrackerBucketFactory(
		Comparator<ServiceReference<SR>> comparator) {

		_serviceReferenceServiceTupleComparator =
			new ServiceReferenceServiceTupleComparator<>(comparator);
	}

	@Override
	public ServiceTrackerBucket<SR, TS, List<TS>> create() {
		return new ListServiceTrackerBucket<>(
			_serviceReferenceServiceTupleComparator);
	}

	private final ServiceReferenceServiceTupleComparator<SR>
		_serviceReferenceServiceTupleComparator;

	private static class ListServiceTrackerBucket<SR, TS>
		implements ServiceTrackerBucket<SR, TS, List<TS>> {

		@Override
		public List<TS> getContent() {
			return _services;
		}

		@Override
		public synchronized boolean isDisposable() {
			return _serviceReferenceServiceTuples.isEmpty();
		}

		@Override
		public synchronized void remove(
			ServiceReferenceServiceTuple<SR, TS> serviceReferenceServiceTuple) {

			_serviceReferenceServiceTuples.remove(serviceReferenceServiceTuple);

			_rebuild();
		}

		@Override
		public synchronized void store(
			ServiceReferenceServiceTuple<SR, TS> serviceReferenceServiceTuple) {

			int index = Collections.binarySearch(
				_serviceReferenceServiceTuples, serviceReferenceServiceTuple,
				_serviceReferenceServiceTupleComparator);

			if (index < 0) {
				index = -index - 1;
			}

			_serviceReferenceServiceTuples.add(
				index, serviceReferenceServiceTuple);

			_rebuild();
		}

		private ListServiceTrackerBucket(
			ServiceReferenceServiceTupleComparator<SR>
				serviceReferenceServiceTupleComparator) {

			_serviceReferenceServiceTupleComparator =
				serviceReferenceServiceTupleComparator;
		}

		private void _rebuild() {
			_services = new ArrayList<>(_serviceReferenceServiceTuples.size());

			for (ServiceReferenceServiceTuple<SR, TS>
					serviceReferenceServiceTuple :
						_serviceReferenceServiceTuples) {

				_services.add(serviceReferenceServiceTuple.getService());
			}

			_services = Collections.unmodifiableList(_services);
		}

		private final ServiceReferenceServiceTupleComparator<SR>
			_serviceReferenceServiceTupleComparator;
		private final List<ServiceReferenceServiceTuple<SR, TS>>
			_serviceReferenceServiceTuples = new ArrayList<>();
		private List<TS> _services = new ArrayList<>();

	}

}