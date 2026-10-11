/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.jenkins.results.parser.monitor;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author Calum Ragan
 */
public class SMTPEndpointReader {

	public static Socket connect(String host, int port, int timeoutMillis)
		throws IOException {

		return _smtpEndpointReader.doConnect(host, port, timeoutMillis);
	}

	public static void setInstance(SMTPEndpointReader smtpEndpointReader) {
		_smtpEndpointReader = smtpEndpointReader;
	}

	public static SSLSocket startTLS(String host, int port, Socket socket)
		throws IOException {

		return _smtpEndpointReader.doStartTLS(host, port, socket);
	}

	protected Socket doConnect(String host, int port, int timeoutMillis)
		throws IOException {

		Socket socket = new Socket();

		try {
			socket.connect(new InetSocketAddress(host, port), timeoutMillis);
		}
		catch (IOException ioException) {
			socket.close();

			throw ioException;
		}

		return socket;
	}

	protected SSLSocket doStartTLS(String host, int port, Socket socket)
		throws IOException {

		SSLSocketFactory sslSocketFactory =
			(SSLSocketFactory)SSLSocketFactory.getDefault();

		SSLSocket sslSocket = (SSLSocket)sslSocketFactory.createSocket(
			socket, host, port, true);

		SSLParameters sslParameters = sslSocket.getSSLParameters();

		sslParameters.setEndpointIdentificationAlgorithm("HTTPS");

		sslSocket.setSSLParameters(sslParameters);

		sslSocket.startHandshake();

		return sslSocket;
	}

	private static volatile SMTPEndpointReader _smtpEndpointReader =
		new SMTPEndpointReader();

}