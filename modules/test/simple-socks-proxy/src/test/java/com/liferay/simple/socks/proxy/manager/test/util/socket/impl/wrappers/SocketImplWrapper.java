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

package com.liferay.simple.socks.proxy.manager.test.util.socket.impl.wrappers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

/**
 * @author Tom Wang
 */
public class SocketImplWrapper extends SocketImpl {

	@Override
	public Object getOption(int optID) {
		return null;
	}

	@Override
	public void setOption(int optID, Object value) throws SocketException {
	}

	@Override
	protected void accept(SocketImpl s) throws IOException {
	}

	@Override
	protected int available() throws IOException {
		return -1;
	}

	@Override
	protected void bind(InetAddress host, int port) throws IOException {
	}

	@Override
	protected void close() throws IOException {
	}

	@Override
	protected void connect(InetAddress address, int port) throws IOException {
	}

	@Override
	protected void connect(SocketAddress address, int timeout)
		throws IOException {
	}

	@Override
	protected void connect(String host, int port) throws IOException {
	}

	@Override
	protected void create(boolean stream) throws IOException {
	}

	@Override
	protected InputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	protected OutputStream getOutputStream() throws IOException {
		return null;
	}

	@Override
	protected void listen(int backlog) throws IOException {
	}

	@Override
	protected void sendUrgentData(int data) throws IOException {
	}

}