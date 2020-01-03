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

import com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConnectionConfiguration;
import com.liferay.portal.search.elasticsearch7.internal.sidecar.Sidecar;
import com.liferay.portal.search.elasticsearch7.internal.util.ClassLoaderUtil;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author Michael C. Han
 */
public class RemoteElasticsearchConnection extends BaseElasticsearchConnection {

	public RemoteElasticsearchConnection(
		Sidecar sidecar,
		ElasticsearchConnectionConfiguration
			elasticsearchConnectionConfiguration) {

		_sidecar = sidecar;
		_elasticsearchConnectionConfiguration =
			elasticsearchConnectionConfiguration;
	}

	@Override
	public void close() {
		super.close();

		if (_sidecar != null) {
			_sidecar.stop();
		}
	}

	@Override
	public void connect() {
		if (_elasticsearchConnectionConfiguration.active()) {
			if (_sidecar != null) {
				_sidecar.start();
			}

			super.connect();
		}
	}

	@Override
	public String getConnectionId() {
		return _elasticsearchConnectionConfiguration.connectionId();
	}

	@Override
	public OperationMode getOperationMode() {
		return OperationMode.REMOTE;
	}

	protected void configureSecurity(RestClientBuilder restClientBuilder) {
		restClientBuilder.setHttpClientConfigCallback(
			httpClientBuilder -> {
				httpClientBuilder.setDefaultCredentialsProvider(
					createCredentialsProvider());

				if (_elasticsearchConnectionConfiguration.httpSSLEnabled()) {
					httpClientBuilder.setSSLContext(createSSLContext());
				}

				return httpClientBuilder;
			});
	}

	protected CredentialsProvider createCredentialsProvider() {
		CredentialsProvider credentialsProvider =
			new BasicCredentialsProvider();

		credentialsProvider.setCredentials(
			AuthScope.ANY,
			new UsernamePasswordCredentials(
				_elasticsearchConnectionConfiguration.username(),
				_elasticsearchConnectionConfiguration.password()));

		return credentialsProvider;
	}

	@Override
	protected RestHighLevelClient createRestHighLevelClient() {
		String[] networkHostAddresses;

		if (_sidecar == null) {
			networkHostAddresses =
				_elasticsearchConnectionConfiguration.networkHostAddresses();
		}
		else {
			try {
				networkHostAddresses = new String[] {
					_sidecar.getNetworkHostAddress()
				};
			}
			catch (Exception e) {
				throw new RuntimeException(
					"Unable to get network host address", e);
			}
		}

		HttpHost[] httpHosts = new HttpHost[networkHostAddresses.length];

		for (int i = 0; i < networkHostAddresses.length; i++) {
			httpHosts[i] = HttpHost.create(networkHostAddresses[i]);
		}

		RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);

		if (_elasticsearchConnectionConfiguration.authenticationEnabled()) {
			configureSecurity(restClientBuilder);
		}

		Class<? extends RemoteElasticsearchConnection> clazz = getClass();

		return ClassLoaderUtil.getWithContextClassLoader(
			() -> new RestHighLevelClient(restClientBuilder), clazz);
	}

	protected SSLContext createSSLContext() {
		try {
			Path path = Paths.get(
				_elasticsearchConnectionConfiguration.truststorePath());

			InputStream is = Files.newInputStream(path);

			KeyStore keyStore = KeyStore.getInstance(
				_elasticsearchConnectionConfiguration.truststoreType());
			String truststorePassword =
				_elasticsearchConnectionConfiguration.truststorePassword();

			keyStore.load(is, truststorePassword.toCharArray());

			SSLContextBuilder sslContextBuilder = SSLContexts.custom();

			sslContextBuilder.loadKeyMaterial(
				keyStore, truststorePassword.toCharArray());
			sslContextBuilder.loadTrustMaterial(keyStore, null);

			return sslContextBuilder.build();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final ElasticsearchConnectionConfiguration
		_elasticsearchConnectionConfiguration;
	private final Sidecar _sidecar;

}