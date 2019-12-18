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

package com.liferay.portal.search.elasticsearch7.internal.sidecar;

import com.liferay.petra.process.ProcessExecutor;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration;
import com.liferay.portal.search.elasticsearch7.configuration.OperationMode;
import com.liferay.portal.util.PropsValues;

import java.io.File;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tina Tian
 */
@Component(
	configurationPid = "com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration",
	immediate = true, service = {}
)
public class SidecarManager {

	@Activate
	protected void activate(ComponentContext componentContext)
		throws Exception {

		ElasticsearchConfiguration elasticsearchConfiguration =
			ConfigurableUtil.createConfigurable(
				ElasticsearchConfiguration.class,
				componentContext.getProperties());

		if (elasticsearchConfiguration.operationMode() !=
				OperationMode.SIDECAR) {

			return;
		}

		_sidecar = new Sidecar(
			_processExecutor,
			new SidecarConfig(
				new File(
					PropsValues.LIFERAY_HOME,
					elasticsearchConfiguration.sideCarHome()),
				_clusterExecutor));

		BundleContext bundleContext = componentContext.getBundleContext();

		_sidecarServiceRegistration = bundleContext.registerService(
			Sidecar.class, _sidecar, null);
	}

	@Deactivate
	protected void deactivate() {
		if (_sidecarServiceRegistration != null) {
			_sidecarServiceRegistration.unregister();

			_sidecar.stop();
		}
	}

	@Reference
	private ClusterExecutor _clusterExecutor;

	@Reference
	private ProcessExecutor _processExecutor;

	private Sidecar _sidecar;
	private ServiceRegistration<Sidecar> _sidecarServiceRegistration;

}