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

import com.liferay.petra.process.ProcessCallable;
import com.liferay.petra.process.ProcessException;

import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.transport.BoundTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.http.HttpServerTransport;
import org.elasticsearch.node.Node;

/**
 * @author Tina Tian
 */
public class GetAddressProcessCallable implements ProcessCallable<String> {

	@Override
	public String call() throws ProcessException {
		try {
			Node node = ElasticsearchServerUtil.waitForStarted();

			Injector injector = node.injector();

			HttpServerTransport httpServerTransport = injector.getInstance(
				HttpServerTransport.class);

			BoundTransportAddress boundTransportAddress =
				httpServerTransport.boundAddress();

			TransportAddress publishAddress =
				boundTransportAddress.publishAddress();

			return publishAddress.toString();
		}
		catch (Exception e) {
			throw new ProcessException("Unable to get published address", e);
		}
	}

	private static final long serialVersionUID = 1L;

}