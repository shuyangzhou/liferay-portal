/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.test.rule;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.ModelListenerRegistrationUtil;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AbstractTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;

import java.util.List;

import org.junit.runner.Description;

import org.osgi.framework.BundleContext;

/**
 * @author Shuyang Zhou
 */
public class PersistenceTestRule extends AbstractTestRule<Object, Object> {

	public static final PersistenceTestRule INSTANCE =
		new PersistenceTestRule();

	@Override
	public void afterMethod(
		Description description, Object modelListeners, Object target) {

		CacheRegistryUtil.setActive(true);

		ReflectionTestUtil.setFieldValue(
			ModelListenerRegistrationUtil.class, "_modelListeners",
			modelListeners);
	}

	@Override
	public Object beforeClass(Description description) {
		return null;
	}

	@Override
	public Object beforeMethod(Description description, Object target)
		throws Exception {

		Object modelListeners = ReflectionTestUtil.getAndSetFieldValue(
			ModelListenerRegistrationUtil.class, "_modelListeners",
			_excludedModelListeners);

		CacheRegistryUtil.setActive(false);

		UserTestUtil.setUser(TestPropsValues.getUser());

		return modelListeners;
	}

	@Override
	protected void afterClass(Description description, Object object) {
	}

	private PersistenceTestRule() {
	}

	private static final ServiceTrackerMap<String, List<ModelListener<?>>>
		_excludedModelListeners;

	static {
		BundleContext bundleContext = SystemBundleUtil.getBundleContext();

		_excludedModelListeners = ServiceTrackerMapFactory.openMultiValueMap(
			bundleContext,
			(Class<ModelListener<?>>)(Class<?>)ModelListener.class,
			"(persistence.test.rule.aware=true)",
			(serviceReference, emitter) -> {
				ModelListener<?> modelListener = bundleContext.getService(
					serviceReference);

				Class<?> modelClass = modelListener.getModelClass();

				if (modelClass != null) {
					emitter.emit(modelClass.getName());
				}
			},
			(serviceReference1, serviceReference2) -> {
				ModelListener<?> modelListener1 = bundleContext.getService(
					serviceReference1);

				Class<?> clazz1 = modelListener1.getClass();

				String name1 = clazz1.getName();

				ModelListener<?> modelListener2 = bundleContext.getService(
					serviceReference2);

				Class<?> clazz2 = modelListener2.getClass();

				String name2 = clazz2.getName();

				return name1.compareTo(name2);
			});
	}

}