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

package com.liferay.batch.engine.internal.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.batch.engine.BatchEngineTaskContentType;
import com.liferay.batch.engine.BatchEngineTaskExecutor;
import com.liferay.batch.engine.BatchEngineTaskOperation;
import com.liferay.batch.engine.model.BatchEngineTask;
import com.liferay.batch.engine.service.BatchEngineTaskLocalService;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.headless.delivery.dto.v1_0.BlogPosting;
import com.liferay.petra.io.StreamUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Ivica Cardic
 */
@RunWith(Arquillian.class)
public class BatchEngineTaskExecutorTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_company = CompanyTestUtil.addCompany();
		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addGroupAdminUser(_group);
	}

	@After
	public void tearDown() throws Exception {
		if (_batchEngineTask != null) {
			_batchEngineTaskLocalService.deleteBatchEngineTask(
				_batchEngineTask.getBatchEngineTaskId());
		}

		_blogsEntryLocalService.deleteEntries(_group.getGroupId());
		_companyLocalService.deleteCompany(_company);
		_groupLocalService.deleteGroup(_group);
		_userLocalService.deleteUser(_user);
	}

	@Test
	public void testCreateBlogPostingsFromJSONFile() throws Exception {
		String fileContent = StreamUtil.toString(
			BatchEngineTaskExecutorTest.class.getResourceAsStream(
				"dependencies/BlogPostings_CREATE.json"),
			"UTF-8");

		fileContent = fileContent.replaceAll(
			"SITE_ID", String.valueOf(_group.getGroupId()));

		_importBlogPosting(
			BatchEngineTaskOperation.CREATE,
			fileContent.getBytes(StandardCharsets.UTF_8), "JSON");

		Assert.assertEquals(
			998, _blogsEntryLocalService.getBlogsEntriesCount());
	}

	@Test
	public void testDeleteBlogPostingsFromJSONFile() throws Exception {
		List<BlogsEntry> blogsEntries = _createBlogsEntries();

		Assert.assertEquals(
			blogsEntries.size(),
			_blogsEntryLocalService.getBlogsEntriesCount());

		String content = _getBlogPostingsJSONDeleteContent(blogsEntries);

		_importBlogPosting(
			BatchEngineTaskOperation.DELETE,
			content.getBytes(StandardCharsets.UTF_8), "JSON");

		Assert.assertEquals(0, _blogsEntryLocalService.getBlogsEntriesCount());
	}

	private List<BlogsEntry> _createBlogsEntries() throws PortalException {
		List<BlogsEntry> blogsEntries = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			blogsEntries.add(
				_blogsEntryLocalService.addEntry(
					_user.getUserId(), "title" + i, "content" + i, new Date(),
					ServiceContextTestUtil.getServiceContext(
						_company.getCompanyId(), _group.getGroupId(),
						_user.getUserId())));
		}

		return blogsEntries;
	}

	private String _getBlogPostingsJSONDeleteContent(
		List<BlogsEntry> blogsEntries) {

		StringBundler sb = new StringBundler();

		sb.append("[");

		for (int i = 0; i < blogsEntries.size(); i++) {
			BlogsEntry blogsEntry = blogsEntries.get(i);

			sb.append("{\"id\":");
			sb.append(blogsEntry.getEntryId());
			sb.append(",\"siteId\":");
			sb.append(_group.getGroupId());
			sb.append("}");

			if (i < (blogsEntries.size() - 1)) {
				sb.append(",");
			}
		}

		sb.append("]");

		return sb.toString();
	}

	private void _importBlogPosting(
		BatchEngineTaskOperation batchEngineTaskOperation, byte[] content,
		String contentType) {

		_batchEngineTask = _batchEngineTaskLocalService.addBatchEngineTask(
			_company.getCompanyId(), _user.getUserId(),
			BatchEngineTaskContentType.valueOf(contentType),
			batchEngineTaskOperation, 100, BlogPosting.class.getName(), content,
			"v1.0");

		_batchEngineTaskExecutor.execute(_batchEngineTask);
	}

	private BatchEngineTask _batchEngineTask;

	@Inject
	private BatchEngineTaskExecutor _batchEngineTaskExecutor;

	@Inject
	private BatchEngineTaskLocalService _batchEngineTaskLocalService;

	@Inject
	private BlogsEntryLocalService _blogsEntryLocalService;

	private Company _company;

	@Inject
	private CompanyLocalService _companyLocalService;

	private Group _group;

	@Inject
	private GroupLocalService _groupLocalService;

	private User _user;

	@Inject
	private UserLocalService _userLocalService;

}