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

package com.liferay.portal.test.rule.callback;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistryUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.callback.BaseTestCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.runner.Description;

/**
 * @author Shuyang Zhou
 */
public class LeftoverDataTestCallback
	extends BaseTestCallback<Map<String, List<BaseModel<?>>>, Void> {

	public static final LeftoverDataTestCallback INSTANCE =
		new LeftoverDataTestCallback();

	@Override
	public void afterClass(
		Description description,
		Map<String, List<BaseModel<?>>> previousDataMap) {

		Map<String, List<BaseModel<?>>> dataMap = _captureDataMap();

		StringBundler sb = new StringBundler();

		for (Map.Entry<String, List<BaseModel<?>>> entry : dataMap.entrySet()) {
			String className = entry.getKey();

			List<BaseModel<?>> currentBaseModels = entry.getValue();

			List<BaseModel<?>> previsoutBaseModels = previousDataMap.remove(
				className);

			List<BaseModel<?>> leftoverBaseModels = new ArrayList<>(
				currentBaseModels);

			if (previsoutBaseModels != null) {
				leftoverBaseModels.removeAll(previsoutBaseModels);

				List<BaseModel<?>> overDeletedBaseModels = new ArrayList<>(
					previsoutBaseModels);

				overDeletedBaseModels.removeAll(currentBaseModels);

				if (!overDeletedBaseModels.isEmpty()) {
					sb.append(description.getClassName());
					sb.append(" caused overdeleted data for class :");
					sb.append(className);
					sb.append(" with data : [\n");

					for (BaseModel<?> baseModel : overDeletedBaseModels) {
						sb.append(StringPool.TAB);
						sb.append(baseModel);
						sb.append(",\n");
					}

					sb.setStringAt("\n]\n", sb.index() - 1);
				}
			}

			if (!leftoverBaseModels.isEmpty()) {
				sb.append(description.getClassName());
				sb.append(" caused leftover data for class :");
				sb.append(className);
				sb.append(" with data : [\n");

				for (BaseModel<?> baseModel : leftoverBaseModels) {
					sb.append(StringPool.TAB);
					sb.append(baseModel);
					sb.append(",\n");
				}

				sb.setStringAt("\n]\n", sb.index() - 1);
			}
		}

		Assert.assertTrue(sb.toString(), sb.index() == 0);
	}

	@Override
	public Map<String, List<BaseModel<?>>> beforeClass(
		Description description) {

		return _captureDataMap();
	}

	private static Map<String, List<BaseModel<?>>> _captureDataMap() {
		Map<String, List<BaseModel<?>>> dataMap = new HashMap<>();

		for (Map.Entry<String, PersistedModelLocalService> entry :
				_getPersistedModelLocalServices()) {

			PersistedModelLocalService persistedModelLocalService =
				entry.getValue();

			DynamicQuery dynamicQuery = ReflectionTestUtil.invoke(
				persistedModelLocalService, "dynamicQuery", new Class<?>[0]);

			List<BaseModel<?>> baseModels = ReflectionTestUtil.invoke(
				persistedModelLocalService, "dynamicQuery",
				new Class<?>[] {DynamicQuery.class}, dynamicQuery);

			if (!baseModels.isEmpty()) {
				dataMap.put(entry.getKey(), baseModels);
			}
		}

		return dataMap;
	}

	private static Set<Map.Entry<String, PersistedModelLocalService>>
		_getPersistedModelLocalServices() {

		PersistedModelLocalServiceRegistry persistedModelLocalServiceRegistry =
			PersistedModelLocalServiceRegistryUtil.
				getPersistedModelLocalServiceRegistry();

		Map<String, PersistedModelLocalService> persistedModelLocalServices =
			new HashMap<>(
				ReflectionTestUtil.getFieldValue(
					persistedModelLocalServiceRegistry,
					"_persistedModelLocalServices"));

		Set<String> classNames = persistedModelLocalServices.keySet();

		classNames.removeAll(_blackListClassNames);

		return persistedModelLocalServices.entrySet();
	}

	private LeftoverDataTestCallback() {
	}

	private static final List<String> _blackListClassNames = Arrays.asList(
		"com.liferay.document.library.sync.model.DLSyncEvent",
		"com.liferay.counter.kernel.model.Counter",
		"com.liferay.portal.kernel.model.ClassName",
		"com.liferay.portal.kernel.model.SystemEvent",
		"com.liferay.portal.security.audit.storage.model.AuditEvent",
		"com.liferay.sync.model.SyncDLObject");

}