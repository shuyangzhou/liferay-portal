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

package com.liferay.simple.socks.proxy.configuration;

import aQute.bnd.annotation.metatype.Meta;

import java.util.List;

/**
 * @author Tom Wang
 */
public interface SocksProxyConfiguration {

	@Meta.AD(deflt = "127.0.0.1, 127.0.0.2", required = false)
	public List<String> allowedHosts();

	@Meta.AD(deflt = "4096", required = false)
	public int bufferSize();

	@Meta.AD(deflt = "200", required = false)
	public int listenerTimeout();

	@Meta.AD(deflt = "8888", required = false)
	public int listeningPort();

	@Meta.AD(deflt = "200", required = false)
	public int serverTimeout();

	@Meta.AD(deflt = "10", required = false)
	public int socketTimeout();

}