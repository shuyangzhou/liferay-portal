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

package com.liferay.portal.monitoring.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.monitoring.configuration.MonitoringConfiguration;
import com.liferay.portal.monitoring.internal.jmx.MonitoringConfigurationManager;
import com.liferay.portal.monitoring.internal.messaging.MonitoringMessageListener;
import com.liferay.portal.monitoring.internal.messaging.MonitoringMessagingConfigurator;
import com.liferay.portal.monitoring.internal.portlet.DefaultPortletMonitoringControl;
import com.liferay.portal.monitoring.internal.portlet.MonitoringInvokerPortletFactoryImpl;
import com.liferay.portal.monitoring.internal.servlet.filter.MonitoringFilter;
import com.liferay.portal.monitoring.internal.servlet.taglib.MonitoringBottomDynamicInclude;
import com.liferay.portal.monitoring.internal.servlet.taglib.MonitoringTopHeadDynamicInclude;
import com.liferay.portal.monitoring.internal.statistics.jmx.ActionRequestPortletManager;
import com.liferay.portal.monitoring.internal.statistics.jmx.EventRequestPortletManager;
import com.liferay.portal.monitoring.internal.statistics.jmx.PortalManager;
import com.liferay.portal.monitoring.internal.statistics.jmx.RenderRequestPortletManager;
import com.liferay.portal.monitoring.internal.statistics.jmx.ResourceRequestPortletManager;
import com.liferay.portal.monitoring.internal.statistics.jmx.ServiceManager;
import com.liferay.portal.monitoring.internal.statistics.portal.ServerSummaryStatistics;
import com.liferay.portal.monitoring.internal.statistics.portlet.ActionRequestSummaryStatistics;
import com.liferay.portal.monitoring.internal.statistics.portlet.EventRequestSummaryStatistics;
import com.liferay.portal.monitoring.internal.statistics.portlet.RenderRequestSummaryStatistics;
import com.liferay.portal.monitoring.internal.statistics.portlet.ResourceRequestSummaryStatistics;

import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Shuyang Zhou
 */
@Component(
	configurationPid = "com.liferay.portal.monitoring.configuration.MonitoringConfiguration",
	immediate = true
)
public class MonitoringGateKeeper {

	@Activate
	@Modified
	protected void activate(
			ComponentContext componentContext, Map<String, Object> properties)
		throws ClassNotFoundException {

		MonitoringConfiguration monitoringConfiguration =
			ConfigurableUtil.createConfigurable(
				MonitoringConfiguration.class, properties);

		if (monitoringConfiguration.monitorEnabled()) {
			for (String service : _services) {
				componentContext.enableComponent(service);
			}
		}
		else {
			for (String service : _services) {
				componentContext.disableComponent(service);
			}
		}
	}

	private static final String[] _services = {
		ActionRequestPortletManager.class.getName(),
		ActionRequestSummaryStatistics.class.getName(),
		com.liferay.portal.monitoring.internal.statistics.portal.
			ServerStatistics.class.getName(),
		com.liferay.portal.monitoring.internal.statistics.portlet.
			ServerStatistics.class.getName(),
		com.liferay.portal.monitoring.internal.statistics.service.
			ServerStatistics.class.getName(),
		DataSampleFactoryImpl.class.getName(),
		DefaultPortletMonitoringControl.class.getName(),
		EventRequestPortletManager.class.getName(),
		EventRequestSummaryStatistics.class.getName(),
		MonitoringBottomDynamicInclude.class.getName(),
		MonitoringConfigurationManager.class.getName(),
		MonitoringFilter.class.getName(),
		MonitoringInvokerPortletFactoryImpl.class.getName(),
		MonitoringMessageListener.class.getName(),
		MonitoringMessagingConfigurator.class.getName(),
		MonitoringTopHeadDynamicInclude.class.getName(),
		PortalManager.class.getName(),
		RenderRequestPortletManager.class.getName(),
		RenderRequestSummaryStatistics.class.getName(),
		ResourceRequestPortletManager.class.getName(),
		ResourceRequestSummaryStatistics.class.getName(),
		ServerSummaryStatistics.class.getName(), ServiceManager.class.getName()
	};

}