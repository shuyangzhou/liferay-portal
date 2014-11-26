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

package com.liferay.arquillian.extension.persistence.internal.observer;

import com.liferay.arquillian.extension.persistence.internal.annotation.PersistenceTest;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.model.ModelListenerRegistrationUtil;
import com.liferay.portal.tools.DBUpgrader;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.jboss.arquillian.test.spi.event.suite.Before;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;

/**
 * @author Cristina González
 */
public class PersistenceTestObserver {

	public void afterTest(@Observes EventContext<After> eventContext)
		throws Throwable {

		Object instance = ReflectionTestUtil.getFieldValue(
			ModelListenerRegistrationUtil.class, "_instance");

		CacheRegistryUtil.setActive(true);

		ReflectionTestUtil.setFieldValue(
			instance, "_modelListeners", modelListeners);
	}

	public void beforeClass(@Observes BeforeClass beforeClass) {
		TestClass testClass = beforeClass.getTestClass();

		if (testClass.getAnnotation(PersistenceTest.class) != null) {
			try {
				DBUpgrader.upgrade();

				TemplateManagerUtil.init();
			}
			catch (Throwable t) {
				throw new RuntimeException(t);
			}
			finally {
				CacheRegistryUtil.setActive(true);
			}
		}
	}

	public void beforeTest(@Observes EventContext<Before> eventContext)
		throws Throwable {

		Object instance = ReflectionTestUtil.getFieldValue(
			ModelListenerRegistrationUtil.class, "_instance");

		modelListeners = ReflectionTestUtil.getFieldValue(
			instance, "_modelListeners");

		ReflectionTestUtil.setFieldValue(
			instance, "_modelListeners",
			new ConcurrentHashMap<Class<?>, List<ModelListener<?>>>());

		CacheRegistryUtil.setActive(false);
	}

	private Object modelListeners;

}