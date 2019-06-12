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

package com.liferay.portal.search.elasticsearch6.internal.test.connection.remote;

import com.liferay.portal.kernel.test.util.PropsTestUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.search.elasticsearch6.configuration.OperationMode;
import com.liferay.portal.search.elasticsearch6.internal.connection.ElasticsearchClientResolver;
import com.liferay.portal.search.elasticsearch6.internal.connection.RemoteElasticsearchConnection;

import java.util.HashMap;

import org.elasticsearch.client.Client;

/**
 * @author André de Oliveira
 */
public class RemoteElasticsearchConnectionFixture implements ElasticsearchClientResolver {

	@Override
	public Client getClient() {
		return _remoteElasticsearchConnection.getClient();
	}

	public void connect() {
		_remoteElasticsearchConnection.connect();
	}

	RemoteElasticsearchConnection _remoteElasticsearchConnection = createRemoteElasticsearchConnection();

	protected RemoteElasticsearchConnection createRemoteElasticsearchConnection() {
		HashMap<String, Object> properties = new HashMap<>();

		properties.put("operationMode", OperationMode.REMOTE.name());

		RemoteElasticsearchConnection remoteElasticsearchConnection = new RemoteElasticsearchConnection() {
			{
				props = PropsTestUtil.setProps(
					new HashMap<String, Object>() {
						{
							put(
								PropsKeys.DNS_SECURITY_ADDRESS_TIMEOUT_SECONDS,
								String.valueOf(2));
							put(
								PropsKeys.DNS_SECURITY_THREAD_LIMIT,
								String.valueOf(10));
						}
					});

				activate(properties);
			}
		};

		return remoteElasticsearchConnection;
	}
}
