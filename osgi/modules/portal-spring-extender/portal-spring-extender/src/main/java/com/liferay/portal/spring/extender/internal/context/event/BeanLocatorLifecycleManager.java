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

package com.liferay.portal.spring.extender.internal.context.event;

import com.liferay.portal.bean.BeanLocatorImpl;
import com.liferay.portal.kernel.bean.BeanLocator;
import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.spring.extender.internal.classloader.BundleResolverClassLoader;

import org.eclipse.gemini.blueprint.context.event.OsgiBundleApplicationContextEvent;
import org.eclipse.gemini.blueprint.context.event.OsgiBundleApplicationContextListener;
import org.eclipse.gemini.blueprint.context.event.OsgiBundleContextClosedEvent;
import org.eclipse.gemini.blueprint.context.event.OsgiBundleContextRefreshedEvent;

import org.osgi.framework.Bundle;

/**
 * @author Miguel Pastor
 */
public class BeanLocatorLifecycleManager
	implements
		OsgiBundleApplicationContextListener
			<OsgiBundleApplicationContextEvent> {

	@Override
	public void onOsgiApplicationEvent(
		OsgiBundleApplicationContextEvent osgiBundleApplicationContextEvent) {

		Bundle bundle = osgiBundleApplicationContextEvent.getBundle();

		String symbolicName = bundle.getSymbolicName();

		if (osgiBundleApplicationContextEvent
				instanceof OsgiBundleContextRefreshedEvent) {

			PortletBeanLocatorUtil.setBeanLocator(
				symbolicName,
				_buildBeanLocator(osgiBundleApplicationContextEvent));
		}
		else if (osgiBundleApplicationContextEvent
					instanceof OsgiBundleContextClosedEvent) {

			OsgiBundleContextClosedEvent osgiBundleContextClosedEvent =
				(OsgiBundleContextClosedEvent)osgiBundleApplicationContextEvent;

			Throwable throwable =
				osgiBundleContextClosedEvent.getFailureCause();

			if (throwable != null) {
				PortletBeanLocatorUtil.setBeanLocator(symbolicName, null);
			}
		}
	}

	private BeanLocator _buildBeanLocator(
		OsgiBundleApplicationContextEvent osgiBundleApplicationContextEvent) {

		Bundle bundle = osgiBundleApplicationContextEvent.getBundle();

		ClassLoader classLoader = new BundleResolverClassLoader(bundle);

		return new BeanLocatorImpl(
			classLoader,
			osgiBundleApplicationContextEvent.getApplicationContext());
	}

}