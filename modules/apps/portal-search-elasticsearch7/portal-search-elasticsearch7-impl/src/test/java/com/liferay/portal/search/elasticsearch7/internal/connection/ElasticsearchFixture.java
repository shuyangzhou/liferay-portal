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

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration;
import com.liferay.portal.util.FileImpl;

import java.io.File;
import java.io.IOException;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.client.ClusterClient;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.common.unit.TimeValue;

/**
 * @author André de Oliveira
 */
public class ElasticsearchFixture implements ElasticsearchClientResolver {

	public ElasticsearchFixture(Class clazz) {
		this(getSimpleName(clazz));
	}

	public ElasticsearchFixture(String subdirName) {
		this(subdirName, Collections.<String, Object>emptyMap());
	}

	public ElasticsearchFixture(
		String subdirName,
		Map<String, Object> elasticsearchConfigurationProperties) {

		_elasticsearchConfigurationProperties =
			createElasticsearchConfigurationProperties(
				elasticsearchConfigurationProperties);

		_tmpDirName = "tmp/" + subdirName;
	}

	public void createNode() throws Exception {
		deleteTmpDir();

		_embeddedElasticsearchConnection = createElasticsearchConnection();
	}

	public void destroyNode() throws Exception {
		if (_embeddedElasticsearchConnection != null) {
			_embeddedElasticsearchConnection.close();
		}

		deleteTmpDir();
	}

	public ClusterHealthResponse getClusterHealthResponse(
		HealthExpectations healthExpectations) {

		RestHighLevelClient restHighLevelClient = getRestHighLevelClient();

		ClusterClient clusterClient = restHighLevelClient.cluster();

		ClusterHealthRequest clusterHealthRequest = new ClusterHealthRequest();

		clusterHealthRequest.timeout(new TimeValue(10, TimeUnit.MINUTES));
		clusterHealthRequest.waitForActiveShards(
			healthExpectations.getActiveShards());
		clusterHealthRequest.waitForNodes(
			String.valueOf(healthExpectations.getNumberOfNodes()));
		clusterHealthRequest.waitForNoRelocatingShards(true);
		clusterHealthRequest.waitForStatus(healthExpectations.getStatus());

		try {
			return clusterClient.health(
				clusterHealthRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	public Map<String, Object> getElasticsearchConfigurationProperties() {
		return _elasticsearchConfigurationProperties;
	}

	public EmbeddedElasticsearchConnection
		getEmbeddedElasticsearchConnection() {

		return _embeddedElasticsearchConnection;
	}

	public GetIndexResponse getIndex(String... indices) {
		RestHighLevelClient restHighLevelClient = getRestHighLevelClient();

		IndicesClient indicesClient = restHighLevelClient.indices();

		GetIndexRequest getIndexRequest = new GetIndexRequest();

		getIndexRequest.indices(indices);

		try {
			return indicesClient.get(getIndexRequest, RequestOptions.DEFAULT);
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	@Override
	public RestHighLevelClient getRestHighLevelClient() {
		return _embeddedElasticsearchConnection.getRestHighLevelClient();
	}

	@Override
	public RestHighLevelClient getRestHighLevelClient(String connectionId) {
		return _embeddedElasticsearchConnection.getRestHighLevelClient();
	}

	@Override
	public RestHighLevelClient getRestHighLevelClient(
		String connectionId, boolean preferLocalCluster) {

		return _embeddedElasticsearchConnection.getRestHighLevelClient();
	}

	public void setUp() throws Exception {
		createNode();
	}

	public void tearDown() throws Exception {
		destroyNode();
	}

	public void waitForElasticsearchToStart() {
		getClusterHealthResponse(
			new HealthExpectations() {
				{
					setActivePrimaryShards(0);
					setActiveShards(0);
					setNumberOfDataNodes(1);
					setNumberOfNodes(1);
					setStatus(ClusterHealthStatus.GREEN);
					setUnassignedShards(0);
				}
			});
	}

	protected static String getSimpleName(Class clazz) {
		while (clazz.isAnonymousClass()) {
			clazz = clazz.getEnclosingClass();
		}

		return clazz.getSimpleName();
	}

	protected Map<String, Object> createElasticsearchConfigurationProperties(
		Map<String, Object> elasticsearchConfigurationProperties) {

		Map<String, Object> map = HashMapBuilder.<String, Object>put(
			"configurationPid", ElasticsearchConfiguration.class.getName()
		).put(
			"httpCORSAllowOrigin", "*"
		).put(
			"logExceptionsOnly", false
		).build();

		map.putAll(elasticsearchConfigurationProperties);

		return map;
	}

	protected EmbeddedElasticsearchConnection createElasticsearchConnection() {
		EmbeddedElasticsearchConnection embeddedElasticsearchConnection =
			new EmbeddedElasticsearchConnection(
				_tmpDirName, _tmpDirName + "/elasticSearch-tmpDir");

		embeddedElasticsearchConnection.activate(
			_elasticsearchConfigurationProperties);

		return embeddedElasticsearchConnection;
	}

	protected void deleteTmpDir() throws Exception {
		FileUtils.deleteDirectory(new File(_tmpDirName));
	}

	private final Map<String, Object> _elasticsearchConfigurationProperties;
	private EmbeddedElasticsearchConnection _embeddedElasticsearchConnection;
	private final String _tmpDirName;

}