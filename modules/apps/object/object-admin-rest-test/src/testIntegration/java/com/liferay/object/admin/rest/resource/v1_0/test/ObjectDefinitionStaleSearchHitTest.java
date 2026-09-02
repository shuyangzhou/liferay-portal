/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.admin.rest.resource.v1_0.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.admin.rest.client.pagination.Page;
import com.liferay.object.admin.rest.client.pagination.Pagination;
import com.liferay.object.admin.rest.client.resource.v1_0.ObjectDefinitionResource;
import com.liferay.object.admin.rest.resource.v1_0.test.util.ObjectDefinitionTestUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.IndexWriterHelper;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsValues;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class ObjectDefinitionStaleSearchHitTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		Company company = CompanyLocalServiceUtil.getCompany(
			TestPropsValues.getCompanyId());

		User user = UserTestUtil.getAdminUser(company.getCompanyId());

		_objectDefinitionResource = ObjectDefinitionResource.builder(
		).authentication(
			user.getEmailAddress(), PropsValues.DEFAULT_ADMIN_PASSWORD
		).endpoint(
			company.getVirtualHostname(), PortalUtil.getPortalServerPort(false),
			"http"
		).locale(
			LocaleUtil.getDefault()
		).build();
	}

	@Test
	public void testGetObjectDefinitionsPageWithStaleIndexDocument()
		throws Exception {

		ObjectDefinition objectDefinition =
			ObjectDefinitionTestUtil.addCustomObjectDefinition();

		objectDefinition = _objectDefinitionLocalService.deleteObjectDefinition(
			objectDefinition);

		Indexer<ObjectDefinition> indexer =
			IndexerRegistryUtil.nullSafeGetIndexer(ObjectDefinition.class);

		indexer.reindex(objectDefinition);

		_indexWriterHelper.commit(objectDefinition.getCompanyId());

		List<Long> objectDefinitionIds = _getIndexedObjectDefinitionIds(
			indexer, objectDefinition);

		Assert.assertTrue(
			objectDefinitionIds.toString(),
			objectDefinitionIds.contains(
				objectDefinition.getObjectDefinitionId()));

		Page<com.liferay.object.admin.rest.client.dto.v1_0.ObjectDefinition>
			page = _objectDefinitionResource.getObjectDefinitionsPage(
				objectDefinition.getShortName(), null, null,
				Pagination.of(1, 20), null);

		for (com.liferay.object.admin.rest.client.dto.v1_0.ObjectDefinition
				item : page.getItems()) {

			Assert.assertNotEquals(
				String.valueOf(page.getItems()),
				Long.valueOf(objectDefinition.getObjectDefinitionId()),
				item.getId());
		}

		_indexWriterHelper.commit(objectDefinition.getCompanyId());

		objectDefinitionIds = _getIndexedObjectDefinitionIds(
			indexer, objectDefinition);

		Assert.assertFalse(
			objectDefinitionIds.toString(),
			objectDefinitionIds.contains(
				objectDefinition.getObjectDefinitionId()));
	}

	private List<Long> _getIndexedObjectDefinitionIds(
			Indexer<ObjectDefinition> indexer,
			ObjectDefinition objectDefinition)
		throws Exception {

		SearchContext searchContext = new SearchContext();

		searchContext.setAttribute(Field.NAME, objectDefinition.getShortName());
		searchContext.setCompanyId(objectDefinition.getCompanyId());
		searchContext.setEntryClassNames(
			new String[] {ObjectDefinition.class.getName()});
		searchContext.setKeywords(objectDefinition.getShortName());

		Hits hits = indexer.search(searchContext);

		List<Long> objectDefinitionIds = new ArrayList<>();

		for (Document document : hits.getDocs()) {
			objectDefinitionIds.add(
				GetterUtil.getLong(document.get(Field.ENTRY_CLASS_PK)));
		}

		return objectDefinitionIds;
	}

	@Inject
	private IndexWriterHelper _indexWriterHelper;

	@Inject
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	private ObjectDefinitionResource _objectDefinitionResource;

}