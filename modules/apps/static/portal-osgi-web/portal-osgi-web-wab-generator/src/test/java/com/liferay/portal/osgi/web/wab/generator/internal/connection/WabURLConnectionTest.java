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

package com.liferay.portal.osgi.web.wab.generator.internal.connection;

import com.liferay.portal.kernel.security.xml.SecureXMLFactoryProviderUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.UnsecureSAXReaderUtil;
import com.liferay.portal.security.xml.SecureXMLFactoryProviderImpl;
import com.liferay.portal.util.FileImpl;
import com.liferay.portal.util.HttpImpl;
import com.liferay.portal.util.PropsImpl;
import com.liferay.portal.xml.SAXReaderImpl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.net.UnknownServiceException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Gregory Amerson
 */
public class WabURLConnectionTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		com.liferay.portal.kernel.util.PropsUtil.setProps(new PropsImpl());

		FileUtil fileUtil = new FileUtil();

		fileUtil.setFile(new FileImpl());

		HttpUtil httpUtil = new HttpUtil();

		httpUtil.setHttp(new HttpImpl());

		SAXReaderUtil saxReaderUtil = new SAXReaderUtil();

		SAXReaderImpl secureSAXReader = new SAXReaderImpl();

		secureSAXReader.setSecure(true);

		saxReaderUtil.setSAXReader(secureSAXReader);

		SecureXMLFactoryProviderUtil secureXMLFactoryProviderUtil =
			new SecureXMLFactoryProviderUtil();

		secureXMLFactoryProviderUtil.setSecureXMLFactoryProvider(
			new SecureXMLFactoryProviderImpl());

		UnsecureSAXReaderUtil unsecureSAXReaderUtil =
			new UnsecureSAXReaderUtil();

		SAXReaderImpl unsecureSAXReader = new SAXReaderImpl();

		unsecureSAXReaderUtil.setSAXReader(unsecureSAXReader);
		
		URL.setURLStreamHandlerFactory(new TestURLStreamHandlerFactory());
	}

	@Test(expected = UnknownServiceException.class)
	public void testWabURLConnectionRequiredParams() throws Exception {
		URL url = new URL(
			"webbundle:/path/to/foo?Web-ContextPath=foo&protocol=file");

		new WabURLConnection(null, null, url).getInputStream();
	}

	@Test(expected = UnknownServiceException.class)
	public void testWabURLConnectionRequiredParamsCompatibilityMode()
		throws Exception {

		File file = _getFile("classic-theme.autodeployed.war");

		URL url = new URL(
			"webbundle:" + file.toURI().toASCIIString() +
				"?Web-ContextPath=foo");

		new WabURLConnection(null, null, url).getInputStream();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWabURLConnectionRequiredParamsMissing() throws Exception {
		URL url = new URL("webbundle:/path/to/foo?Web-ContextPath=foo");

		new WabURLConnection(null, null, url).getInputStream();
	}

	private ClassLoader _getClassLoader() {
		Class<?> clazz = getClass();

		return clazz.getClassLoader();
	}

	private File _getFile(String fileName) throws URISyntaxException {
		ClassLoader classLoader = _getClassLoader();

		URL url = classLoader.getResource(fileName);

		if (!"file".equals(url.getProtocol())) {
			return null;
		}

		Path path = Paths.get(url.toURI());

		return path.toFile();
	}

	private static class TestURLStreamHandler extends URLStreamHandler {

		@Override
		protected URLConnection openConnection(URL url) throws IOException {
			return new URLConnection(url) {
				@Override
				public void connect() throws IOException {
				}
			};
		}

	}

	private static class TestURLStreamHandlerFactory
		implements URLStreamHandlerFactory {

		@Override
		public URLStreamHandler createURLStreamHandler(String protocol) {
			return new TestURLStreamHandler();
		}

	}

}