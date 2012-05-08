/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.velocity;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.templateparser.TemplateContext;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.template.TemplateContextHelper;
import com.liferay.portal.template.TemplateResource;
import com.liferay.portal.template.TemplateResourceLoader;
import com.liferay.portal.template.TemplateResourceManager;
import com.liferay.portal.util.BaseTestCase;
import com.liferay.portal.util.PropsUtil;

import java.io.IOException;
import java.io.InputStream;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Tina Tian
 */
public class VelocityTemplateTest extends BaseTestCase {

	public static class MockTemplateLoader
		extends TemplateResourceLoader<String> {

		@Override
		public TemplateResource<String> findTemplateReource(String templateId)
			throws IOException {

			if (_TEMPLATE_FILE_NAME.equals(templateId)) {
				return new TemplateResource<String>(
					templateId, _TEMPLATE_FILE_NAME, this);
			}

			throw new IOException(
				"Unable to find template source " + templateId);
		}

		@Override
		public InputStream getInputStream(String resource) throws IOException {
			if (resource == _TEMPLATE_FILE_NAME) {
				return new UnsyncByteArrayInputStream(
					_TEST_TEMPLATE_CONTENT.getBytes(ENCODING));
			}

			throw new IOException(
				"Unable to get reader for template source " + resource);
		}

	}

	@Override
	public void setUp() throws Exception {
		_templateContextHelper = new MockTemplateContextHelper();

		_templateResourceManager = new TemplateResourceManager(
			TemplateManager.VELOCITY);

		_templateResourceManager.setResourceLoaders(
			new String[]{MockTemplateLoader.class.getName()});
		_templateResourceManager.setInterval(0);

		_velocityEngine = new VelocityEngine();

		ExtendedProperties extendedProperties = new FastExtendedProperties();

		extendedProperties.setProperty(
			VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS,
			PropsUtil.get(PropsKeys.VELOCITY_ENGINE_LOGGER));

		extendedProperties.setProperty(
			VelocityEngine.RUNTIME_LOG_LOGSYSTEM + ".log4j.category",
			PropsUtil.get(PropsKeys.VELOCITY_ENGINE_LOGGER_CATEGORY));

		_velocityEngine.setExtendedProperties(extendedProperties);

		_velocityEngine.init();
	}


	public void testGet() throws Exception {
		Template template = new VelocityTemplate(
			_TEMPLATE_FILE_NAME, null, null, null, null, _velocityEngine,
			_templateContextHelper, _templateResourceManager);

		template.put(_TEST_KEY, _TEST_VALUE);

		Object result = template.get(_TEST_KEY);

		assertNotNull(result);

		assertTrue(result instanceof String);

		String stringResult = (String)result;

		assertEquals(_TEST_VALUE, stringResult);
	}

	public void testPrepare() throws Exception {
		Template template = new VelocityTemplate(
			_TEMPLATE_FILE_NAME, null, null, null, null, _velocityEngine,
			_templateContextHelper, _templateResourceManager);

		template.put(_TEST_KEY, _TEST_VALUE);

		template.prepare(null);

		Object result = template.get(_TEST_VALUE);

		assertNotNull(result);

		assertTrue(result instanceof String);

		String stringResult = (String)result;

		assertEquals(_TEST_VALUE, stringResult);
	}

	public void testProcessTemplate1() throws Exception {
		Template template = new VelocityTemplate(
			_TEMPLATE_FILE_NAME, null, null, null, null, _velocityEngine,
			_templateContextHelper, _templateResourceManager);

		template.put(_TEST_KEY, _TEST_VALUE);

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		template.processTemplate(unsyncStringWriter);

		String result = unsyncStringWriter.toString();

		assertEquals(_TEST_VALUE, result);
	}

	public void testProcessTemplate2() throws Exception {
		Template template = new VelocityTemplate(
			_WRONG_TEMPLATE_ID, null, null, null, null, _velocityEngine,
			_templateContextHelper, _templateResourceManager);

		template.put(_TEST_KEY, _TEST_VALUE);

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		try {
			template.processTemplate(unsyncStringWriter);

			fail();
		}
		catch (Exception e) {
			if (e instanceof TemplateException) {
				String message = e.getMessage();

				assertTrue(message.contains(_WRONG_TEMPLATE_ID));

				return;
			}

			fail();
		}
	}

	public void testProcessTemplate3() throws Exception {
		Template template = new VelocityTemplate(
			_WRONG_TEMPLATE_ID, _TEST_TEMPLATE_CONTENT, null, null, null,
			_velocityEngine, _templateContextHelper, _templateResourceManager);

		template.put(_TEST_KEY, _TEST_VALUE);

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		template.processTemplate(unsyncStringWriter);

		String result = unsyncStringWriter.toString();

		assertEquals(_TEST_VALUE, result);
	}

	public void testProcessTemplate4() throws Exception {
		Template template = new VelocityTemplate(
			_TEMPLATE_FILE_NAME, null, _WRONG_ERROR_TEMPLATE_ID, null, null,
			_velocityEngine, _templateContextHelper, _templateResourceManager);

		template.put(_TEST_KEY, _TEST_VALUE);

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		template.processTemplate(unsyncStringWriter);

		String result = unsyncStringWriter.toString();

		assertEquals(_TEST_VALUE, result);
	}

	public void testProcessTemplate5() throws Exception {
		Template template = new VelocityTemplate(
			_WRONG_TEMPLATE_ID, null, _TEMPLATE_FILE_NAME, null, null,
			_velocityEngine, _templateContextHelper, _templateResourceManager);

		template.put(_TEST_KEY, _TEST_VALUE);

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		template.processTemplate(unsyncStringWriter);

		String result = unsyncStringWriter.toString();

		assertEquals(_TEST_VALUE, result);
	}

	public void testProcessTemplate6() throws Exception {
		Template template = new VelocityTemplate(
			_WRONG_TEMPLATE_ID, null, _WRONG_ERROR_TEMPLATE_ID, null, null,
			_velocityEngine, _templateContextHelper, _templateResourceManager);

		template.put(_TEST_KEY, _TEST_VALUE);

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		try {
			template.processTemplate(unsyncStringWriter);

			fail();
		}
		catch (Exception e) {
			if (e instanceof TemplateException) {
				String message = e.getMessage();

				assertTrue(message.contains(_WRONG_ERROR_TEMPLATE_ID));

				return;
			}

			fail();
		}
	}

	public void testProcessTemplate7() throws Exception {
		Template template = new VelocityTemplate(
			_WRONG_TEMPLATE_ID, null, _WRONG_ERROR_TEMPLATE_ID,
			_TEST_TEMPLATE_CONTENT, null, _velocityEngine,
			_templateContextHelper, _templateResourceManager);

		template.put(_TEST_KEY, _TEST_VALUE);

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		template.processTemplate(unsyncStringWriter);

		String result = unsyncStringWriter.toString();

		assertEquals(_TEST_VALUE, result);
	}

	public void testProcessTemplate8() throws Exception {
		VelocityContext velocityContext = new VelocityContext();

		velocityContext.put(_TEST_KEY, _TEST_VALUE);

		Template template = new VelocityTemplate(
			_TEMPLATE_FILE_NAME, null, null, null, velocityContext,
			_velocityEngine, _templateContextHelper, _templateResourceManager);

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		template.processTemplate(unsyncStringWriter);

		String result = unsyncStringWriter.toString();

		assertEquals(_TEST_VALUE, result);
	}

	private static final String _TEMPLATE_FILE_NAME = "test.vm";

	private static final String _TEST_KEY = "TEST_KEY";

	private static final String _TEST_TEMPLATE_CONTENT = "$" + _TEST_KEY;

	private static final String _TEST_VALUE = "TEST_VALUE";

	private static final String _WRONG_ERROR_TEMPLATE_ID =
		"WRONG_ERROR_TEMPLATE_ID";

	private static final String _WRONG_TEMPLATE_ID = "WRONG_TEMPLATE_ID";

	private TemplateContextHelper _templateContextHelper;
	private TemplateResourceManager _templateResourceManager;
	private VelocityEngine _velocityEngine;

	private class MockTemplateContextHelper extends TemplateContextHelper {

		@Override
		public Map<String, Object> getHelperUtilities() {
			return Collections.emptyMap();
		}

		@Override
		public Map<String, Object> getRestrictedHelperUtilities() {
			return Collections.emptyMap();
		}

		@Override
		public Set<String> getRestrictedVariables() {
			return Collections.emptySet();
		}

		@Override
		public void prepare(
			TemplateContext templateContext, HttpServletRequest request) {

			String testValue = (String)templateContext.get(_TEST_KEY);

			templateContext.put(testValue, testValue);
		}

	}

}