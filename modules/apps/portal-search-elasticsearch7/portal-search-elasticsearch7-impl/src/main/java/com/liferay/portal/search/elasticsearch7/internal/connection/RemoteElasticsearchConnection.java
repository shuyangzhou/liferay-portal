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

import com.liferay.petra.process.ProcessExecutor;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration;
import com.liferay.portal.search.elasticsearch7.internal.sidecar.Sidecar;
import com.liferay.portal.search.elasticsearch7.internal.sidecar.SidecarConfig;
import com.liferay.portal.search.elasticsearch7.internal.util.ClassLoaderUtil;
import com.liferay.portal.util.PropsValues;

import java.io.File;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.KeyStore;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

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

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	configurationPid = "com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration",
	immediate = true, property = "operation.mode=REMOTE",
	service = ElasticsearchConnection.class
)
public class RemoteElasticsearchConnection extends BaseElasticsearchConnection {

	@Override
	public OperationMode getOperationMode() {
		return OperationMode.REMOTE;
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		elasticsearchConfiguration = ConfigurableUtil.createConfigurable(
			ElasticsearchConfiguration.class, properties);

		if ((elasticsearchConfiguration.operationMode() !=
				com.liferay.portal.search.elasticsearch7.configuration.
					OperationMode.REMOTE) ||
			!Arrays.equals(
				elasticsearchConfiguration.networkHostAddresses(),
				new String[] {"http://localhost:9200"}) ||
			!Objects.equals(
				elasticsearchConfiguration.clusterName(),
				"LiferayElasticsearchCluster")) {

			return;
		}

		String sidecarHome = elasticsearchConfiguration.sidecarHome();

		File sidecarHomeFolder = new File(
			PropsValues.LIFERAY_HOME, sidecarHome);

		if (!sidecarHomeFolder.exists()) {
			sidecarHomeFolder = new File(sidecarHome);

			if (!sidecarHomeFolder.exists()) {
				throw new IllegalStateException(
					"Sidecar home " + sidecarHome + " does not exist");
			}
		}

		_sidecar = new Sidecar(
			_processExecutor,
			new SidecarConfig(
				sidecarHomeFolder,
				elasticsearchConfiguration.sidecarHeartbeatInterval(),
				elasticsearchConfiguration.sidecarJVMOptions(),
				_clusterExecutor));

		_sidecar.start();
	}

	protected void configureSecurity(RestClientBuilder restClientBuilder) {
		restClientBuilder.setHttpClientConfigCallback(
			httpClientBuilder -> {
				httpClientBuilder.setDefaultCredentialsProvider(
					createCredentialsProvider());

				if (elasticsearchConfiguration.httpSSLEnabled()) {
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
				elasticsearchConfiguration.username(),
				elasticsearchConfiguration.password()));

		return credentialsProvider;
	}

	@Override
	protected RestHighLevelClient createRestHighLevelClient() {
		String[] networkHostAddresses;

		if (_sidecar == null) {
			networkHostAddresses =
				elasticsearchConfiguration.networkHostAddresses();
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

		if (elasticsearchConfiguration.authenticationEnabled()) {
			configureSecurity(restClientBuilder);
		}

		Class<? extends RemoteElasticsearchConnection> clazz = getClass();

		return ClassLoaderUtil.getWithContextClassLoader(
			() -> new RestHighLevelClient(restClientBuilder), clazz);
	}

	protected SSLContext createSSLContext() {
		try {
			KeyStore keyStore = KeyStore.getInstance(
				elasticsearchConfiguration.truststoreType());
			String truststorePath = elasticsearchConfiguration.truststorePath();
			String truststorePassword =
				elasticsearchConfiguration.truststorePassword();

			Path path = Paths.get(truststorePath);

			InputStream is = Files.newInputStream(path);

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

	@Deactivate
	protected void deactivate(Map<String, Object> properties) {
		close();

		if (_sidecar != null) {
			_sidecar.stop();
		}
	}

	@Modified
	protected synchronized void modified(Map<String, Object> properties) {
		deactivate(properties);

		activate(properties);

		if (elasticsearchConfiguration.operationMode() ==
				com.liferay.portal.search.elasticsearch7.configuration.
					OperationMode.REMOTE) {

			connect();
		}
	}

	@Reference
	protected Props props;

	@Reference
	private ClusterExecutor _clusterExecutor;

	@Reference
	private ProcessExecutor _processExecutor;

	private volatile Sidecar _sidecar;

}