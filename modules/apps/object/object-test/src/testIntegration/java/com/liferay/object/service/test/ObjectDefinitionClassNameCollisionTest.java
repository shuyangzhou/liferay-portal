/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.exception.ObjectDefinitionClassNameException;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.test.util.ObjectDefinitionTestUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * DEBUG do not merge. Proves, on CI, why the object tests reported a duplicate
 * class name: the test utilities used to supply a four character class name
 * drawn blindly from 67600 names, and the batch engine units hold some of those
 * names for good in the company the tests run in. The service rejects a
 * supplied duplicate before reaching the code that would draw again.
 *
 * This cannot run locally: the integration tests get their own company, and a
 * bundle built by hand has no batch engine data in it, so nothing is there to
 * collide with. On CI the company carries those definitions, which is where the
 * reported failure came from.
 *
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class ObjectDefinitionClassNameCollisionTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Test
	public void testReportTheClassNamesTheCompanyAlreadyHolds()
		throws Exception {

		List<String> classNames = _getFourCharacterClassNames();

		throw new IllegalStateException(
			StringBundler.concat(
				"CLASSNAMEDIAG companyId=", TestPropsValues.getCompanyId(),
				" fourCharacterClassNames=", classNames.size(), " sample=",
				classNames));
	}

	@Test
	public void testSuppliedDuplicateClassNameIsRejected() throws Exception {
		List<String> classNames = _getFourCharacterClassNames();

		Assert.assertFalse(
			StringBundler.concat(
				"The company holds no four character class name to collide ",
				"with, so this run cannot prove the collision. companyId ",
				TestPropsValues.getCompanyId()),
			classNames.isEmpty());

		// The draw the test utilities used to make, on the unlucky outcome.

		try {
			_addModifiableSystemObjectDefinition(classNames.get(0));

			Assert.fail(
				StringBundler.concat(
					"Adding a modifiable system object definition under the ",
					"taken class name ", classNames.get(0), " was accepted"));
		}
		catch (ObjectDefinitionClassNameException.MustNotBeDuplicate
					objectDefinitionClassNameException) {

			Assert.assertTrue(
				objectDefinitionClassNameException.getMessage(),
				objectDefinitionClassNameException.getMessage(
				).contains(
					classNames.get(0)
				));
		}

		// The name the test utilities ask for now cannot equal any of them.

		String className = ObjectDefinitionTestUtil.getRandomClassName();

		Assert.assertFalse(className, classNames.contains(className));

		ObjectDefinition objectDefinition =
			_addModifiableSystemObjectDefinition(className);

		try {
			Assert.assertEquals(className, objectDefinition.getClassName());
		}
		finally {
			_objectDefinitionLocalService.deleteObjectDefinition(
				objectDefinition);
		}
	}

	private ObjectDefinition _addModifiableSystemObjectDefinition(
			String className)
		throws Exception {

		return _objectDefinitionLocalService.addSystemObjectDefinition(
			null, TestPropsValues.getUserId(), 0, className, null, true, false,
			true, false, true, false, false, false, false, false, null,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			true, ObjectDefinitionTestUtil.getRandomName(), null, null, null,
			null,
			LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString()),
			false, ObjectDefinitionConstants.SCOPE_COMPANY, null, 1,
			WorkflowConstants.STATUS_DRAFT, Collections.emptyList(),
			Collections.emptyList(), Collections.emptyList());
	}

	private List<String> _getFourCharacterClassNames() throws Exception {
		List<String> classNames = new ArrayList<>();

		for (ObjectDefinition objectDefinition :
				_objectDefinitionLocalService.getObjectDefinitions(
					TestPropsValues.getCompanyId(),
					WorkflowConstants.STATUS_ANY)) {

			String className = objectDefinition.getClassName();

			if ((className != null) &&
				_pattern.matcher(
					className
				).matches()) {

				classNames.add(className);
			}
		}

		return classNames;
	}

	private static final Pattern _pattern = Pattern.compile(
		"com\\.liferay\\.object\\.model\\.ObjectDefinition#[a-zA-Z]\\d" +
			"[a-zA-Z]\\d");

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

}