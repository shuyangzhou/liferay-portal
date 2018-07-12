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

package com.liferay.arquillian.extension.docker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jboss.osgi.vfs.VFSUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Tambara
 */
public class SimpleHTTPServer {

	/**
	 * Constructs an HTTP server, which will run on a randomly selected port on the wildcard address.
	 */
	public SimpleHTTPServer() throws IOException {
		this(null, InetAddress.getLocalHost().getCanonicalHostName(), 0);
	}

	/**
	 * Constructs an HTTP server _running on a specified address/port.
	 *
	 * @param bindAddress the address to bind to.
	 * @param port the port to bind to.
	 */
	public SimpleHTTPServer(InetAddress bindAddress, int port)
		throws IOException {

		this(bindAddress, bindAddress.getCanonicalHostName(), port);
	}

	/**
	 * Register a stream for serving.
	 *
	 * @param stream the URL to obtain the stream contents, which will be opened each time the stream is served.
	 * @return an HTTP URL that can be used to access the contents of the provided stream.
	 */
	public URL serve(URL stream) {
		final String token = UUID.randomUUID().toString();

		_streams.put(token, stream);

		try {
			return new URL(
				String.format(
					"http://%s:%d/%s", _canonicalHostName,
					this._serverSocket.getLocalPort(), token));
		}
		catch (MalformedURLException murle) {
			throw new IllegalStateException(
				"HTTP url could not be parsed.", murle);
		}
	}

	/**
	 * Closes this server, closing the server _socket and all in-progress client connections.
	 */
	public void shutdown() {
		_running = false;

		try {
			_serverSocket.close();
		}
		catch (IOException ioe) {
		}

		final ArrayList<ClientConnection> runningClients;

		synchronized (this) {
			runningClients = new ArrayList<>(_clients);
			_clients.clear();
		}

		for (ClientConnection client : runningClients) {
			client.shutdown();
		}
	}

	/**
	 * Starts listening for client connections.
	 */
	public void start() {
		Thread srv = new Thread("Simple HTTP Server") {

			@Override
			public void run() {
				_serve();
			}

		};

		srv.setDaemon(true);
		srv.start();
	}

	private SimpleHTTPServer(
			InetAddress bindAddress, String canonicalHostname, int port)
		throws IOException {

		_canonicalHostName = canonicalHostname;

		_serverSocket = new ServerSocket();

		_serverSocket.bind(new InetSocketAddress(bindAddress, port));
	}

	private synchronized void _onClientConnect(ClientConnection client) {
		if (!_running) {
			client.shutdown();
		}
		else {
			_clients.add(client);
		}
	}

	private synchronized void _onClientDisconnect(ClientConnection client) {
		_clients.remove(client);
	}

	private void _runError(String message, Throwable t) {
		if (_running) {
			_logger.error(message, t);
		}
	}

	private void _serve() {
		try {
			while (_running) {
				ClientConnection client = new ClientConnection(
					_serverSocket.accept());

				_onClientConnect(client);

				client.start();
			}
		}
		catch (Exception e) {
			_runError("Error accepting connection", e);
		}
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		SimpleHTTPServer.class.getPackage().getName());

	private final String _canonicalHostName;
	private final List<ClientConnection> _clients = new ArrayList<>();
	private volatile boolean _running = true;
	private final ServerSocket _serverSocket;
	private final Map<String, URL> _streams = Collections.synchronizedMap(
		new HashMap<String, URL>());

	private class ClientConnection extends Thread {

		public ClientConnection(Socket socket) {
			setDaemon(true);

			_socket = socket;
		}

		public void run() {
			try {
				final BufferedReader input = new BufferedReader(
					new InputStreamReader(
						_socket.getInputStream(), "US-ASCII"));

				final DataOutputStream output = new DataOutputStream(
					_socket.getOutputStream());

				try {
					final String line = input.readLine();

					//if(_logger.isDebugEnabled()) {
					//	_logger.debug("Incoming request [{}]", line);
					//}

					if ((line == null) || (line.length() < 1)) {
						return;
					}

					final URL streamUrl = _getRequestedFile(line);

					if (streamUrl != null) {
						//_logger.debug("For [{}] serving {}", line, streamUrl);
						_writeResponse(output, "200 OK");
						VFSUtils.copyStream(streamUrl.openStream(), output);
					}
					else {
						_writeResponse(output, "404 Not Found");
						//_logger.warn("For [{}] no file found", line);
					}
				}
				catch (Exception e) {
					_writeResponse(output, "500 Server Error");
					_runError("Error serving file", e);
				}
				finally {
					output.flush();
				}
			}
			catch (Exception e) {
				_runError("Error setting up file serving thread", e);
			}
			finally {
				shutdown();
			}
		}

		public synchronized void shutdown() {
			if (_socket != null) {
				try {
					_socket.close();
				}
				catch (IOException ioe) {
				}

				_socket = null;
			}

			_onClientDisconnect(this);
		}

		private URL _getRequestedFile(String requestLine) {
			if (requestLine.startsWith("GET")) {
				String[] parts = requestLine.split(" ");

				if ((parts.length >= 2) && parts[1].startsWith("/")) {
					String token = parts[1].substring(1);

					return _streams.get(token);
				}
			}

			return null;
		}

		private void _writeResponse(
				DataOutputStream output, String responseLine)
			throws IOException {

			output.writeBytes("HTTP/1.0 ");
			output.writeBytes(responseLine);
			output.writeBytes("\r\n\r\n");
		}

		private Socket _socket;

	}

}