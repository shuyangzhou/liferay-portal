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

package com.liferay.portal.monitoring.internal.portlet;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.monitoring.DataSampleFactory;
import com.liferay.portal.kernel.monitoring.PortletMonitoringControl;
import com.liferay.portal.kernel.portlet.InvokerFilterContainer;
import com.liferay.portal.kernel.portlet.InvokerPortlet;
import com.liferay.portal.kernel.portlet.InvokerPortletFactory;
import com.liferay.portal.monitoring.configuration.MonitoringConfiguration;

import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 * @author Philip Jones
 */
@Component(
	configurationPid = "com.liferay.portal.monitoring.configuration.MonitoringConfiguration",
	immediate = true, property = {Constants.SERVICE_RANKING + "=100"},
	service = InvokerPortletFactory.class
)
public class MonitoringInvokerPortletFactoryImpl
	implements InvokerPortletFactory {

	@Override
	public InvokerPortlet create(
			com.liferay.portal.kernel.model.Portlet portletModel,
			Portlet portlet, PortletConfig portletConfig,
			PortletContext portletContext,
			InvokerFilterContainer invokerFilterContainer,
			boolean checkAuthToken, boolean facesPortlet, boolean strutsPortlet,
			boolean strutsBridgePortlet)
		throws PortletException {

		InvokerPortlet invokerPortlet = _invokerPortletFactory.create(
			portletModel, portlet, portletConfig, portletContext,
			invokerFilterContainer, checkAuthToken, facesPortlet, strutsPortlet,
			strutsBridgePortlet);

		if (_monitorEnabled) {
			invokerPortlet = new MonitoringInvokerPortlet(
				invokerPortlet, _dataSampleFactory, _portletMonitoringControl);
		}

		return invokerPortlet;
	}

	@Override
	public InvokerPortlet create(
			com.liferay.portal.kernel.model.Portlet portletModel,
			Portlet portlet, PortletContext portletContext,
			InvokerFilterContainer invokerFilterContainer)
		throws PortletException {

		InvokerPortlet invokerPortlet = _invokerPortletFactory.create(
			portletModel, portlet, portletContext, invokerFilterContainer);

		if (_monitorEnabled) {
			invokerPortlet = new MonitoringInvokerPortlet(
				invokerPortlet, _dataSampleFactory, _portletMonitoringControl);
		}

		return invokerPortlet;
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		MonitoringConfiguration monitoringConfiguration =
			ConfigurableUtil.createConfigurable(
				MonitoringConfiguration.class, properties);

		_monitorEnabled = monitoringConfiguration.monitorEnabled();
	}

	@Reference
	private DataSampleFactory _dataSampleFactory;

	@Reference(target = "(" + Constants.SERVICE_RANKING + "=1)")
	private InvokerPortletFactory _invokerPortletFactory;

	private boolean _monitorEnabled;

	@Reference
	private PortletMonitoringControl _portletMonitoringControl;

}