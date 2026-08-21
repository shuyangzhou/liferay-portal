/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.field.util.ObjectFieldUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.test.util.ObjectDefinitionTestUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class ObjectDefinitionPortletIdCollisionTest {

	@ClassRule
	@Rule
	public static AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testRecycledClassNameReusesPortletId() throws Exception {

		// A custom object definition's class name ends in a random four
		// character token drawn from a space of sixty seven thousand six
		// hundred, and the generator only checks that no existing definition
		// holds it, so a deleted definition's token is handed out again. The
		// portlet ID is derived from that token, so the second definition
		// registers the portlet ID the first one used.

		String className =
			ObjectDefinitionConstants.
				CLASS_NAME_PREFIX_CUSTOM_OBJECT_DEFINITION + "Z9Z9";

		ObjectDefinition objectDefinition1 = _publish(className);

		String portletId = objectDefinition1.getPortletId();

		_objectDefinitionLocalService.deleteObjectDefinition(
			objectDefinition1.getObjectDefinitionId());

		List<LogEntry> logEntries = null;

		ObjectDefinition objectDefinition2 = null;

		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				_CLASS_NAME_PORTLET_TRACKER, LoggerTestUtil.ERROR)) {

			objectDefinition2 = _publish(className);

			logEntries = logCapture.getLogEntries();
		}

		try {
			Assert.assertEquals(portletId, objectDefinition2.getPortletId());

			Assert.assertTrue(
				"Portlet tracker logged " + logEntries, logEntries.isEmpty());
		}
		finally {
			_objectDefinitionLocalService.deleteObjectDefinition(
				objectDefinition2.getObjectDefinitionId());
		}
	}

	private ObjectDefinition _publish(String className) throws Exception {
		String name = ObjectDefinitionTestUtil.getRandomName();

		List<ObjectField> objectFields = Arrays.asList(
			ObjectFieldUtil.createObjectField(
				ObjectFieldConstants.BUSINESS_TYPE_TEXT,
				ObjectFieldConstants.DB_TYPE_STRING,
				RandomTestUtil.randomString(), StringUtil.randomId()));

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.addCustomObjectDefinition(
				null, TestPropsValues.getUserId(), 0, className, true, false,
				false, true, false, false, false, false, false, null,
				LocalizedMapUtil.getLocalizedMap(name), name, null, null,
				LocalizedMapUtil.getLocalizedMap(name), true,
				ObjectDefinitionConstants.SCOPE_COMPANY,
				ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT,
				Collections.emptyList(), objectFields, Collections.emptyList(),
				new ServiceContext());

		return _objectDefinitionLocalService.publishCustomObjectDefinition(
			TestPropsValues.getUserId(),
			objectDefinition.getObjectDefinitionId());
	}

	private static final String _CLASS_NAME_PORTLET_TRACKER =
		"com.liferay.portal.osgi.web.portlet.tracker.internal.osgi.util." +
			"tracker.PortletTracker";

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

}