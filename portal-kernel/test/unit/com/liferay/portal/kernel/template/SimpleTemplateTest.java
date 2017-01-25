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

package com.liferay.portal.kernel.template;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class SimpleTemplateTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testConstructor() throws Exception {
		new SimpleTemplate(SimpleTemplateTest.class, "dependencies/test.tmpl");
	}

	@Test
	public void testSimpleTemplate() throws Exception {
		Map<String, String> context = Collections.singletonMap(
			"test", SimpleTemplateTest.class.getName());

		Assert.assertEquals(
			"This is the " + SimpleTemplateTest.class.getName() +
				" template content.",
			_processTemplate(_TEMPLATE_CONTENT, context));
	}

	@Test
	public void testSimpleTemplateEmptyContext() throws Exception {
		Assert.assertEquals(
			"This is the test template content.",
			_processTemplate(_TEMPLATE_CONTENT, Collections.emptyMap()));
	}

	@Test
	public void testSimpleTemplateInvalidTemplate() throws Exception {
		Assert.assertEquals(
			_INVALID_TEMPLATE_CONTENT,
			_processTemplate(
				_INVALID_TEMPLATE_CONTENT, Collections.emptyMap()));
	}

	private String _processTemplate(
			String templateContent, Map<String, String> context)
		throws Exception {

		try (UnsyncByteArrayInputStream unsyncByteArrayInputStream =
				new UnsyncByteArrayInputStream(templateContent.getBytes())) {

			SimpleTemplate simpleTemplate = new SimpleTemplate(
				unsyncByteArrayInputStream);

			UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

			simpleTemplate.processTemplate(unsyncStringWriter, context);

			return unsyncStringWriter.toString();
		}
	}

	private static final String _INVALID_TEMPLATE_CONTENT =
		"This is the ${test template content.";

	private static final String _TEMPLATE_CONTENT =
		"This is the ${test} template content.";

}