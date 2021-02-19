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

package com.liferay.portal.tools.service.builder.test.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link GenericMethodsEntryLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see GenericMethodsEntryLocalService
 * @generated
 */
public class GenericMethodsEntryLocalServiceWrapper
	implements GenericMethodsEntryLocalService,
			   ServiceWrapper<GenericMethodsEntryLocalService> {

	public GenericMethodsEntryLocalServiceWrapper(
		GenericMethodsEntryLocalService genericMethodsEntryLocalService) {

		_genericMethodsEntryLocalService = genericMethodsEntryLocalService;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _genericMethodsEntryLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public <T> void typeParameter(java.util.function.Consumer<T> consumer)
		throws Exception {

		_genericMethodsEntryLocalService.typeParameter(consumer);
	}

	@Override
	public <E extends Exception> void typeParameterAndBound(
			java.util.function.BiConsumer<String, E> biConsumer)
		throws E {

		_genericMethodsEntryLocalService.typeParameterAndBound(biConsumer);
	}

	@Override
	public <T, E extends Exception> java.util.List<T> typeParametersAndBound(
		java.util.function.BiFunction<Long, T, E> biFunction,
		java.util.function.BiConsumer<Long, E> biConsumer) {

		return _genericMethodsEntryLocalService.typeParametersAndBound(
			biFunction, biConsumer);
	}

	@Override
	public <N extends Number, E extends Exception> java.util.List<N>
		typeParametersAndBounds(
			java.util.function.BiFunction<Long, N, E> biFunction,
			java.util.function.BiConsumer<Long, N> biConsumer) {

		return _genericMethodsEntryLocalService.typeParametersAndBounds(
			biFunction, biConsumer);
	}

	@Override
	public
		<N extends Number & ObjIntConsumer, E extends Exception & Serializable>
			java.util.List<N> typeParametersAndMultipleBounds(
				java.util.function.BiFunction<Long, N, E> biFunction,
				java.util.function.BiConsumer<Long, N> biConsumer) {

		return _genericMethodsEntryLocalService.typeParametersAndMultipleBounds(
			biFunction, biConsumer);
	}

	@Override
	public GenericMethodsEntryLocalService getWrappedService() {
		return _genericMethodsEntryLocalService;
	}

	@Override
	public void setWrappedService(
		GenericMethodsEntryLocalService genericMethodsEntryLocalService) {

		_genericMethodsEntryLocalService = genericMethodsEntryLocalService;
	}

	private GenericMethodsEntryLocalService _genericMethodsEntryLocalService;

}