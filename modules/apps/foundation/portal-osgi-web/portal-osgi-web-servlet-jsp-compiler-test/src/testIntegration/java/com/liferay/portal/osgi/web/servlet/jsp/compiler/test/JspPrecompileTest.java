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

package com.liferay.portal.osgi.web.servlet.jsp.compiler.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTemplate;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.test.LayoutTestUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import javax.portlet.Portlet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Matthew Tambara
 */
@RunWith(Arquillian.class)
public class JspPrecompileTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testPrecompiledJsp() throws Exception {
		Bundle testBundle = FrameworkUtil.getBundle(JspPrecompileTest.class);

		final BundleContext bundleContext = testBundle.getBundleContext();

		Bundle bundle = bundleContext.installBundle(
			JspPrecompilePortlet.PORTLET_NAME, _createTestBundle());

		bundle.start();

		Class clazz = getClass();

		ClassLoader classloader = clazz.getClassLoader();

		StringBundler path = new StringBundler(11);

		path.append(PropsUtil.get(PropsKeys.LIFERAY_HOME));
		path.append(StringPool.SLASH);
		path.append("work");
		path.append(StringPool.SLASH);
		path.append(bundle.getSymbolicName());
		path.append(StringPool.DASH);
		path.append(bundle.getVersion());
		path.append(StringPool.SLASH);
		path.append(
			StringUtil.replace(_JSP_PACKAGE, CharPool.PERIOD, CharPool.SLASH));
		path.append(StringPool.SLASH);
		path.append("precompile_jsp.class");

		File file = new File(path.toString());

		File parentFile = file.getParentFile();

		parentFile.mkdirs();

		file.createNewFile();

		try (InputStream inputStream = classloader.getResourceAsStream(
				"precompile_jsp.class");
			OutputStream outputStream = new FileOutputStream(file)) {

			StreamUtil.transfer(inputStream, outputStream);
		}

		_group = GroupTestUtil.addGroup();

		Layout layout = LayoutTestUtil.addLayout(_group);

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		LayoutTemplate layoutTemplate = layoutTypePortlet.getLayoutTemplate();

		List<String> columns = layoutTemplate.getColumns();

		String columnId = columns.get(0);

		layoutTypePortlet.addPortletId(
			TestPropsValues.getUserId(), JspPrecompilePortlet.PORTLET_NAME,
			columnId, -1, false);

		LayoutLocalServiceUtil.updateLayout(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			layout.getTypeSettings());

		StringBundler address = new StringBundler(7);

		address.append("http://localhost:8080/web");
		address.append(_group.getFriendlyURL());
		address.append(StringPool.QUESTION);
		address.append(JspPrecompilePortlet.JSP_PARAMETER);
		address.append(StringPool.EQUAL);
		address.append(StringPool.SLASH);
		address.append(_RUNTIME_COMPILE_JSP);

		URL url = new URL(address.toString());

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					"com.liferay.portal.osgi.web.servlet.jsp.compiler." +
						"internal.JspCompiler",
					Level.DEBUG)) {

			try (InputStream inputStream = url.openStream()) {
				_verifyCompiledJsp(inputStream, "Runtime Compiled");
			}

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			StringBundler sb = new StringBundler(4);

			sb.append("Compiling JSP: ");
			sb.append(_JSP_PACKAGE);
			sb.append(StringPool.PERIOD);
			sb.append(
				StringUtil.replace(
					_RUNTIME_COMPILE_JSP, CharPool.PERIOD, CharPool.UNDERLINE));

			if (!_containsLog(loggingEvents, sb.toString())) {
				Assert.fail("No JSP compiled at runtime");
			}

			loggingEvents.clear();

			address.setIndex(address.index() - 1);

			address.append(_PRECOMPILE_JSP);

			url = new URL(address.toString());

			try (InputStream inputStream = url.openStream()) {
				_verifyCompiledJsp(inputStream, "Precompiled");
			}

			sb.setIndex(sb.index() - 1);

			sb.append(
				StringUtil.replace(
					_PRECOMPILE_JSP, CharPool.PERIOD, CharPool.UNDERLINE));

			if (_containsLog(loggingEvents, sb.toString())) {
				Assert.fail("JSP was compiled at runtime");
			}
		}
		finally {
			FileUtil.deltree(parentFile);
		}
	}

	private String _buildImportPackageString(Class<?>... classes) {
		StringBundler sb = new StringBundler(classes.length * 2);

		for (Class<?> clazz : classes) {
			Package pkg = clazz.getPackage();

			sb.append(pkg.getName());
			sb.append(StringPool.COMMA);
		}

		int index = sb.index();

		if (index > 0) {
			sb.setIndex(index - 1);
		}

		return sb.toString();
	}

	private boolean _containsLog(
		List<LoggingEvent> loggingEvents, String expected) {

		List<String> strings = new ArrayList<>();

		for (LoggingEvent loggingEvent : loggingEvents) {
			String message = loggingEvent.getRenderedMessage();

			if (message.equals(expected)) {
				return true;
			}
		}

		return false;
	}

	private InputStream _createTestBundle() throws Exception {
		try (UnsyncByteArrayOutputStream unsyncByteArrayOutputStream =
				new UnsyncByteArrayOutputStream()) {

			try (JarOutputStream jarOutputStream = new JarOutputStream(
					unsyncByteArrayOutputStream)) {

				Manifest manifest = new Manifest();

				Attributes attributes = manifest.getMainAttributes();

				attributes.putValue(
					Constants.BUNDLE_ACTIVATOR,
					JspPrecompileBundleActivator.class.getName());
				attributes.putValue(Constants.BUNDLE_MANIFESTVERSION, "2");
				attributes.putValue(
					Constants.BUNDLE_SYMBOLICNAME,
					"com.liferay.portal.osgi.web.servlet.jsp.compiler.test." +
						"bundle");
				attributes.putValue(Constants.BUNDLE_VERSION, "1.0.0");
				attributes.putValue(
					Constants.IMPORT_PACKAGE,
					_buildImportPackageString(
						MVCPortlet.class, PortalUtil.class, Portlet.class,
						HttpServletRequest.class, BundleActivator.class));
				attributes.putValue("Manifest-Version", "2");

				jarOutputStream.putNextEntry(
					new ZipEntry(JarFile.MANIFEST_NAME));

				manifest.write(jarOutputStream);

				jarOutputStream.closeEntry();

				_writeClasses(
					jarOutputStream, JspPrecompilePortlet.class,
					JspPrecompileBundleActivator.class);

				_writeResources(
					jarOutputStream, _RUNTIME_COMPILE_JSP, _PRECOMPILE_JSP);

			}

			return new UnsyncByteArrayInputStream(
				unsyncByteArrayOutputStream.unsafeGetByteArray(), 0,
				unsyncByteArrayOutputStream.size());
		}
	}

	private void _verifyCompiledJsp(InputStream inputStream, String string)
		throws IOException {

		try (InputStreamReader inputStreamReader = new InputStreamReader(
				inputStream);
			UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(inputStreamReader)) {

			String line = null;

			while (true) {
				line = unsyncBufferedReader.readLine();

				if (line == null) {
					Assert.fail();
				}

				if (line.contains(string)) {
					return;
				}
			}
		}
	}

	private void _writeClasses(
			JarOutputStream jarOutputStream, Class<?>... classes)
		throws IOException {

		Class clzz = getClass();

		ClassLoader classLoader = clzz.getClassLoader();

		for (Class<?> clazz : classes) {
			String className = clazz.getName();

			String path = StringUtil.replace(
				className, CharPool.PERIOD, CharPool.SLASH);

			String resourcePath = path.concat(".class");

			jarOutputStream.putNextEntry(new ZipEntry(resourcePath));

			StreamUtil.transfer(
				classLoader.getResourceAsStream(resourcePath), jarOutputStream,
				false);

			jarOutputStream.closeEntry();
		}
	}

	private void _writeResources(
			JarOutputStream jarOutputStream, String... strings)
		throws IOException {

		Class clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		for (String string : strings) {
			String path = "META-INF/resources/" + string;

			jarOutputStream.putNextEntry(new ZipEntry(path));

			StreamUtil.transfer(
				classLoader.getResourceAsStream(path), jarOutputStream, false);

			jarOutputStream.closeEntry();
		}
	}

	private static final String _JSP_PACKAGE = "org.apache.jsp";

	private static final String _PRECOMPILE_JSP = "precompile.jsp";

	private static final String _RUNTIME_COMPILE_JSP = "runtime.jsp";

	@DeleteAfterTestRun
	private Group _group;

}