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

package com.liferay.portal.search.elasticsearch7.internal.connection;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration;
import com.liferay.portal.search.elasticsearch7.internal.sidecar.Sidecar;
import com.liferay.portal.search.elasticsearch7.internal.util.ClassLoaderUtil;

import java.util.Map;

import org.apache.http.HttpHost;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tina Tian
 */
@Component(
	configurationPid = "com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration",
	immediate = true, property = "operation.mode=SIDECAR",
	service = ElasticsearchConnection.class
)
public class SidecarElasticsearchConnection
	extends BaseElasticsearchConnection {

	@Override
	public OperationMode getOperationMode() {
		return OperationMode.SIDECAR;
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		elasticsearchConfiguration = ConfigurableUtil.createConfigurable(
			ElasticsearchConfiguration.class, properties);
	}

	@Override
	protected RestHighLevelClient createRestHighLevelClient() {
		return ClassLoaderUtil.getWithContextClassLoader(
			() -> new RestHighLevelClient(
				RestClient.builder(
					HttpHost.create(_sidecar.getNetworkHostAddress()))),
			getClass());
	}

	@Deactivate
	protected void deactivate() {
		close();
	}

	@Modified
	protected synchronized void modified(Map<String, Object> properties) {
		elasticsearchConfiguration = ConfigurableUtil.createConfigurable(
			ElasticsearchConfiguration.class, properties);

		if (isConnected()) {
			close();
		}

		if (!isConnected() &&
			(elasticsearchConfiguration.operationMode() ==
				com.liferay.portal.search.elasticsearch7.configuration.
					OperationMode.SIDECAR)) {

			connect();
		}
	}

	@Reference
	private Sidecar _sidecar;

}