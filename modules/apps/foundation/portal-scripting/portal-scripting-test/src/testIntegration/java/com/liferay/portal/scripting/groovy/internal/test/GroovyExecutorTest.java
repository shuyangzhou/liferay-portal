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

package com.liferay.portal.scripting.groovy.internal.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.scripting.ScriptingExecutor;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.io.IOException;
import java.io.InputStream;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Inácio Nery
 */
@RunWith(Arquillian.class)
public class GroovyExecutorTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		ScriptingExecutor[] scriptingExecutors;

		scriptingExecutors = registry.getServices(
			"com.liferay.portal.kernel.scripting.ScriptingExecutor",
			"(scripting.language=groovy)");

		_scriptingExecutor = scriptingExecutors[0];
	}

	@Test
	public void testBindingInputVariables() throws Exception {
		Map<String, Object> inputObjects = new HashMap<>();

		inputObjects.put("variable", "string");

		Set<String> outputNames = Collections.emptySet();

		execute(inputObjects, outputNames, "binding-input");
	}

	@Test(expected = RuntimeException.class)
	public void testRuntimeError() throws Exception {
		Map<String, Object> inputObjects = Collections.emptyMap();
		Set<String> outputNames = Collections.emptySet();

		execute(inputObjects, outputNames, "runtime-error");
	}

	@Test
	public void testSimpleScript() throws Exception {
		Map<String, Object> inputObjects = Collections.emptyMap();
		Set<String> outputNames = Collections.emptySet();

		execute(inputObjects, outputNames, "simple");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSyntaxError() throws Exception {
		Map<String, Object> inputObjects = Collections.emptyMap();
		Set<String> outputNames = Collections.emptySet();

		execute(inputObjects, outputNames, "syntax-error");
	}

	protected Map<String, Object> execute(
			Map<String, Object> inputObjects, Set<String> outputNames,
			String fileName)
		throws Exception {

		String script = getScript(fileName + ".groovy");

		return _scriptingExecutor.eval(null, inputObjects, outputNames, script);
	}

	protected String getScript(String name) throws IOException {
		Class<?> clazz = getClass();

		InputStream inputStream = clazz.getResourceAsStream(
			"dependencies/" + name);

		return StringUtil.read(inputStream);
	}

	private ScriptingExecutor _scriptingExecutor;

}