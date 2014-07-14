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

package com.liferay.portal.search.elasticsearch.connection;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;

/**
 * @author Michael C. Han
 */
public class ElasticsearchConnectionManager {

	public void afterPropertiesSet() {
		_elasticsearchConnection.initialize();
	}

	public AdminClient getAdminClient() {
		Client client = getClient();

		return client.admin();
	}

	public Client getClient() {
		if (_elasticsearchConnection == null) {
			throw new IllegalStateException(
				"Elasticsearch connection not initialized");
		}

		return _elasticsearchConnection.getClient();
	}

	public ClusterAdminClient getClusterAdminClient() {
		AdminClient adminClient = getAdminClient();

		return adminClient.cluster();
	}

	public ClusterHealthResponse getClusterHealthResponse() {
		return _elasticsearchConnection.getClusterHealthResponse();
	}

	public ElasticsearchConnection getElasticsearchConnection() {
		return _elasticsearchConnection;
	}

	public void setElasticsearchConnection(
		ElasticsearchConnection elasticsearchConnection) {

		_elasticsearchConnection = elasticsearchConnection;
	}

	private ElasticsearchConnection _elasticsearchConnection;

}