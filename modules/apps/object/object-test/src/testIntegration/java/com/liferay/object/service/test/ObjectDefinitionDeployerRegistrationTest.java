/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.constants.ObjectRelationshipConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.related.models.ObjectRelatedModelsProvider;
import com.liferay.object.related.models.ObjectRelatedModelsProviderRegistryUtil;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.test.util.ObjectDefinitionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class ObjectDefinitionDeployerRegistrationTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_objectDefinition = ObjectDefinitionTestUtil.publishObjectDefinition();
	}

	@After
	public void tearDown() throws Exception {
		if (_objectDefinition != null) {
			_objectDefinitionLocalService.deleteObjectDefinition(
				_objectDefinition.getObjectDefinitionId());
		}
	}

	@Test
	public void testDeployAfterDeployInactive() throws Exception {
		_objectDefinitionLocalService.deployInactiveObjectDefinition(
			_objectDefinition);

		_objectDefinitionLocalService.deployObjectDefinition(_objectDefinition);

		for (String objectRelationshipType : _OBJECT_RELATIONSHIP_TYPES) {
			List<ServiceReference<?>> serviceReferences = _getServiceReferences(
				objectRelationshipType);

			Assert.assertEquals(
				serviceReferences.toString(), 1, serviceReferences.size());
		}
	}

	@Test
	public void testUndeployAfterDeployInactive() throws Exception {
		_objectDefinitionLocalService.deployInactiveObjectDefinition(
			_objectDefinition);

		_objectDefinitionLocalService.undeployObjectDefinition(
			_objectDefinition);

		for (String objectRelationshipType : _OBJECT_RELATIONSHIP_TYPES) {
			List<ServiceReference<?>> serviceReferences = _getServiceReferences(
				objectRelationshipType);

			Assert.assertTrue(
				serviceReferences.toString(), serviceReferences.isEmpty());
		}
	}

	private List<ServiceReference<?>> _getServiceReferences(
			String objectRelationshipType)
		throws Exception {

		List<ServiceReference<?>> serviceReferences = new ArrayList<>();

		Bundle bundle = FrameworkUtil.getBundle(
			ObjectDefinitionDeployerRegistrationTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		ServiceReference<?>[] allServiceReferences =
			bundleContext.getAllServiceReferences(
				ObjectRelatedModelsProvider.class.getName(), null);

		if (allServiceReferences == null) {
			return serviceReferences;
		}

		String externalReferenceCode =
			_objectDefinition.getExternalReferenceCode();

		for (ServiceReference<?> serviceReference : allServiceReferences) {
			if (externalReferenceCode.equals(
					serviceReference.getProperty(
						ObjectRelatedModelsProviderRegistryUtil.
							KEY_OBJECT_DEFINITION_ERC)) &&
				objectRelationshipType.equals(
					serviceReference.getProperty(
						ObjectRelatedModelsProviderRegistryUtil.
							KEY_RELATIONSHIP_TYPE))) {

				serviceReferences.add(serviceReference);
			}
		}

		return serviceReferences;
	}

	private static final String[] _OBJECT_RELATIONSHIP_TYPES = {
		ObjectRelationshipConstants.TYPE_MANY_TO_MANY,
		ObjectRelationshipConstants.TYPE_ONE_TO_MANY,
		ObjectRelationshipConstants.TYPE_ONE_TO_ONE
	};

	private ObjectDefinition _objectDefinition;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

}