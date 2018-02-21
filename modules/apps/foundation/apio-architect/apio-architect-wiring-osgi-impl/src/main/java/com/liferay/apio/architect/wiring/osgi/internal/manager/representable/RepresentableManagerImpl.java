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

package com.liferay.apio.architect.wiring.osgi.internal.manager.representable;

import static com.liferay.apio.architect.unsafe.Unsafe.unsafeCast;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.ManagerCache.INSTANCE;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.TypeArgumentProperties.KEY_PRINCIPAL_TYPE_ARGUMENT;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.util.ManagerUtil.getGenericClassFromPropertyOrElse;
import static com.liferay.apio.architect.wiring.osgi.internal.manager.util.ManagerUtil.getTypeParamOrFail;

import static org.osgi.service.component.annotations.ReferenceCardinality.MULTIPLE;
import static org.osgi.service.component.annotations.ReferencePolicyOption.GREEDY;

import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.logger.ApioLogger;
import com.liferay.apio.architect.related.RelatedCollection;
import com.liferay.apio.architect.representor.Representable;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.representor.Representor.Builder;
import com.liferay.apio.architect.unsafe.Unsafe;
import com.liferay.apio.architect.wiring.osgi.manager.representable.IdentifierClassManager;
import com.liferay.apio.architect.wiring.osgi.manager.representable.NameManager;
import com.liferay.apio.architect.wiring.osgi.manager.representable.RepresentableManager;
import com.liferay.osgi.service.tracker.collections.ServiceReferenceServiceTuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 */
@Component(immediate = true)
public class RepresentableManagerImpl
	implements NameManager, IdentifierClassManager, RepresentableManager {

	@Deactivate
	public void deactivate() {
		INSTANCE.clear();
	}

	@Override
	public <T extends Identifier> Optional<Class<T>> getIdentifierClassOptional(
		String name) {

		if (!INSTANCE.hasIdentifierClasses()) {
			_computeRepresentables();
		}

		Optional<Map<String, Class<Identifier>>> optional =
			INSTANCE.getIdentifierClassesOptional();

		return optional.map(
			map -> map.get(name)
		).map(
			Unsafe::unsafeCast
		);
	}

	@Override
	public Optional<String> getNameOptional(String className) {
		if (!INSTANCE.hasNames()) {
			_computeRepresentables();
		}

		Optional<Map<String, String>> optional = INSTANCE.getNamesOptional();

		return optional.map(map -> map.get(className));
	}

	@Override
	public <T, U> Optional<Representor<T, U>> getRepresentorOptional(
		String name) {

		if (!INSTANCE.hasRepresentors()) {
			_computeRepresentables();
		}

		Optional<Map<String, Representor>> optional =
			INSTANCE.getRepresentorsOptional();

		return optional.map(
			map -> map.get(name)
		).map(
			Unsafe::unsafeCast
		);
	}

	@Reference(cardinality = MULTIPLE, policyOption = GREEDY, unbind = "-")
	protected void setServiceReference(
		ServiceReference<Representable> serviceReference,
		Representable representable) {

		Class<Identifier> clazz = getGenericClassFromPropertyOrElse(
			serviceReference, KEY_PRINCIPAL_TYPE_ARGUMENT,
			() -> getTypeParamOrFail(representable, Representable.class, 2));

		TreeSet<ServiceReferenceServiceTuple<Representable, Representable>>
			serviceReferenceServiceTuples =
				_serviceReferenceServiceTuples.computeIfAbsent(
					clazz, name -> new TreeSet<>());

		ServiceReferenceServiceTuple<Representable, Representable>
			serviceReferenceServiceTuple = new ServiceReferenceServiceTuple<>(
				serviceReference, representable);

		serviceReferenceServiceTuples.add(serviceReferenceServiceTuple);
	}

	private void _computeRepresentables() {
		Map<String, Class<Identifier>> identifierClasses = new HashMap<>();
		Map<String, String> names = new HashMap<>();
		Map<String, List<RelatedCollection<?>>> relatedCollections =
			new HashMap<>();
		Map<String, Representor> representors = new HashMap<>();

		_serviceReferenceServiceTuples.forEach(
			(clazz, treeSet) -> {
				Representable representable = _getRepresentable(treeSet);

				String name = representable.getName();

				Set<Map.Entry<String, String>> entries = names.entrySet();

				Stream<Map.Entry<String, String>> stream = entries.stream();

				Optional<String> optional = stream.filter(
					entry -> Objects.equals(entry.getValue(), name)
				).map(
					Map.Entry::getKey
				).findFirst();

				if (optional.isPresent()) {
					_apioLogger.warning(
						_getDuplicateErrorMessage(clazz, name, optional.get()));

					return;
				}

				names.put(clazz.getName(), name);
				identifierClasses.put(name, clazz);
				representors.put(
					name,
					_getRepresentor(
						unsafeCast(representable), unsafeCast(clazz),
						relatedCollections));
			});

		INSTANCE.setIdentifierClasses(identifierClasses);
		INSTANCE.setNames(names);
		INSTANCE.setRepresentors(representors);
	}

	private String _getDuplicateErrorMessage(
		Class<Identifier> clazz, String name, String className) {

		StringBuilder stringBuilder = new StringBuilder();

		return stringBuilder.append(
			"Representable registered under "
		).append(
			clazz.getName()
		).append(
			" has name "
		).append(
			name
		).append(
			", but it's already in use by Representable "
		).append(
			"registered under "
		).append(
			className
		).toString();
	}

	private Representable _getRepresentable(
		TreeSet<ServiceReferenceServiceTuple<Representable, Representable>>
			treeSet) {

		ServiceReferenceServiceTuple<Representable, Representable>
			serviceReferenceServiceTuple = treeSet.first();

		return serviceReferenceServiceTuple.getService();
	}

	private <T, S, U extends Identifier<S>> Representor<T, S> _getRepresentor(
		Representable<T, S, U> representable, Class<U> clazz,
		Map<String, List<RelatedCollection<?>>> relatedCollections) {

		Supplier<List<RelatedCollection<?>>> relatedCollectionSupplier =
			() -> relatedCollections.get(clazz.getName());

		BiConsumer<Class<?>, RelatedCollection<?>> biConsumer =
			(identifierClass, relatedCollection) -> {
				List<RelatedCollection<?>> list =
					relatedCollections.computeIfAbsent(
						identifierClass.getName(), __ -> new ArrayList<>());

				list.add(relatedCollection);
			};

		Builder<T, S> builder = new Builder<>(
			clazz, biConsumer, relatedCollectionSupplier);

		return representable.representor(builder);
	}

	@Reference
	private ApioLogger _apioLogger;

	private final Map<Class<Identifier>, TreeSet<ServiceReferenceServiceTuple
		<Representable, Representable>>> _serviceReferenceServiceTuples =
			new HashMap<>();

}