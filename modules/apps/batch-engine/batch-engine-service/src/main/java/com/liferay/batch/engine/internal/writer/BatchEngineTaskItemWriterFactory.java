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

package com.liferay.batch.engine.internal.writer;

import com.liferay.batch.engine.BatchEngineTaskField;
import com.liferay.batch.engine.BatchEngineTaskMethod;
import com.liferay.batch.engine.BatchEngineTaskOperation;
import com.liferay.batch.engine.internal.BatchEngineTaskResourceRegistry;
import com.liferay.batch.engine.model.BatchEngineTask;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import java.util.Objects;

import javax.ws.rs.PathParam;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ivica cardic
 */
@Component(service = BatchEngineTaskItemWriterFactory.class)
public class BatchEngineTaskItemWriterFactory {

	@Activate
	public void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	public <T> BatchEngineTaskItemWriter<T> create(
			BatchEngineTask batchEngineTask)
		throws Exception {

		BatchEngineTaskOperation batchEngineTaskOperation =
			BatchEngineTaskOperation.valueOf(batchEngineTask.getOperation());

		ServiceReference<Object> serviceReference =
			_batchEngineTaskResourceRegistry.getServiceReference(
				batchEngineTaskOperation, batchEngineTask.getClassName(),
				batchEngineTask.getVersion());

		if (serviceReference == null) {
			StringBundler sb = new StringBundler(4);

			sb.append("No resource available for batchEngineTaskOperation ");
			sb.append(batchEngineTask.getOperation());
			sb.append(" and className ");
			sb.append(batchEngineTask.getClassName());

			throw new IllegalStateException(sb.toString());
		}

		Object resource = _bundleContext.getService(serviceReference);

		Class<?> resourceClass = resource.getClass();

		Method resourceMethod = _getResourceMethod(
			batchEngineTaskOperation, batchEngineTask.getClassName(),
			resourceClass);

		Class<?> parentResourceClass = resourceClass.getSuperclass();

		return new BatchEngineTaskItemWriter<>(
			_companyLocalService.getCompany(batchEngineTask.getCompanyId()),
			resourceMethod,
			_getMethodParameterNames(
				parentResourceClass.getMethod(
					resourceMethod.getName(),
					resourceMethod.getParameterTypes()),
				resourceMethod),
			_bundleContext.getServiceObjects(serviceReference),
			_userLocalService.getUser(batchEngineTask.getUserId()));
	}

	private String[] _getMethodParameterNames(
		Method parentResourceMethod, Method resourceMethod) {

		Parameter[] parentResourceMethodParameters =
			parentResourceMethod.getParameters();

		Parameter[] resourceMethodParameters = resourceMethod.getParameters();

		String[] parameterNames = new String[resourceMethodParameters.length];

		for (int i = 0; i < resourceMethodParameters.length; i++) {
			Parameter parameter = resourceMethodParameters[i];

			BatchEngineTaskField batchEngineTaskField = parameter.getAnnotation(
				BatchEngineTaskField.class);

			if (batchEngineTaskField == null) {
				parameter = parentResourceMethodParameters[i];

				PathParam pathParam = parameter.getAnnotation(PathParam.class);

				if (pathParam != null) {
					parameterNames[i] = pathParam.value();
				}
			}
			else {
				parameterNames[i] = batchEngineTaskField.value();
			}
		}

		return parameterNames;
	}

	private Method _getResourceMethod(
		BatchEngineTaskOperation batchEngineTaskOperation, String itemClassName,
		Class<?> resourceClass) {

		for (Method method : resourceClass.getMethods()) {
			BatchEngineTaskMethod batchEngineTaskMethod =
				method.getDeclaredAnnotation(BatchEngineTaskMethod.class);

			if (batchEngineTaskMethod == null) {
				continue;
			}

			Class<?> itemClass = batchEngineTaskMethod.itemClass();

			if (Objects.equals(itemClass.getName(), itemClassName) &&
				(batchEngineTaskMethod.batchEngineTaskOperation() ==
					batchEngineTaskOperation)) {

				return method;
			}
		}

		StringBundler sb = new StringBundler(4);

		sb.append("No resource method available for batchEngineTaskOperation ");
		sb.append(batchEngineTaskOperation);
		sb.append(" and className ");
		sb.append(itemClassName);

		throw new IllegalStateException(sb.toString());
	}

	@Reference
	private BatchEngineTaskResourceRegistry _batchEngineTaskResourceRegistry;

	private BundleContext _bundleContext;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private UserLocalService _userLocalService;

}