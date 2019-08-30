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
import com.liferay.batch.engine.BatchItemWriter;
import com.liferay.batch.engine.BatchOperation;
import com.liferay.batch.engine.BatchTaskExecutor;
import com.liferay.batch.engine.BatchTaskExecutorFactory;
import com.liferay.batch.engine.model.BatchTask;
import com.liferay.batch.engine.service.BatchTaskLocalService;
import com.liferay.blogs.service.BlogsEntryLocalService;
import com.liferay.headless.delivery.dto.v1_0.BlogPosting;
import com.liferay.headless.delivery.resource.v1_0.BlogPostingResource;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;

import java.util.List;
import java.util.Locale;

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
import org.osgi.framework.ServiceRegistration;

/**
 * @author Ivica Cardic
 */
@RunWith(Arquillian.class)
public class BatchTaskExecutorTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(getClass());

		_bundleContext = bundle.getBundleContext();

		_batchItemWriterServiceRegistration = _bundleContext.registerService(
			BatchItemWriter.class, new BlogPostingBatchItemWriter(),
			new HashMapDictionary<String, String>() {
				{
					put("class.name", BlogPosting.class.getName());
					put("version", "v1.0");
				}
			});

		_company = CompanyTestUtil.addCompany();
		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addGroupAdminUser(_group);

		_blogPostingResource.setContextAcceptLanguage(_acceptLanguage);
		_blogPostingResource.setContextCompany(_company);

		PermissionThreadLocal.setPermissionChecker(
			PermissionCheckerFactoryUtil.create(_user));

		PrincipalThreadLocal.setName(_user.getUserId());

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCompanyId(_company.getCompanyId());

		ServiceContextThreadLocal.pushServiceContext(serviceContext);
	}

	@After
	public void tearDown() throws Exception {
		PermissionThreadLocal.setPermissionChecker(null);

		_batchItemWriterServiceRegistration.unregister();

		_batchTaskLocalService.deleteBatchTask(_batchTask.getBatchTaskId());
		_companyLocalService.deleteCompany(_company);
		_groupLocalService.deleteGroup(_group);
		_userLocalService.deleteUser(_user);
	}

	@Test
	public void testCompressedCSVFileImport() throws Exception {
		_importBlogPosting("BlogPosting.csv.zip", "CSV");
	}

	@Test
	public void testCompressedJSONFileImport() throws Exception {
		_importBlogPosting("BlogPosting.json.zip", "JSON");
	}

	@Test
	public void testCompressedXLSFileImport() throws Exception {
		_importBlogPosting("BlogPosting.xlsx.zip", "XLS");
	}

	@Test
	public void testCSVFileImport() throws Exception {
		_importBlogPosting("BlogPosting.csv", "CSV");
	}

	@Test
	public void testJSONFileImport() throws Exception {
		_importBlogPosting("BlogPosting.json", "JSON");
	}

	@Test
	public void testXLSXFileImport() throws Exception {
		_importBlogPosting("BlogPosting.xlsx", "XLS");
	}

	public class BlogPostingBatchItemWriter
		implements BatchItemWriter<BlogPosting> {

		@Override
		public void write(
				List<? extends BlogPosting> blogPostings,
				BatchOperation batchOperation)
			throws Exception {

			for (BlogPosting blogPosting : blogPostings) {
				_blogPostingResource.postSiteBlogPosting(
					_group.getGroupId(), blogPosting);
			}
		}

	}

	private FileEntry _addFileEntry(String fileName) throws Exception {
		return TempFileEntryUtil.addTempFileEntry(
			_group.getGroupId(), _user.getUserId(), "batch-import", fileName,
			BatchTaskExecutorTest.class.getResourceAsStream(
				"dependencies/" + fileName),
			MimeTypesUtil.getContentType(fileName));
	}

	private void _importBlogPosting(String fileName, String contentType)
		throws Exception {

		FileEntry fileEntry = _addFileEntry(fileName);

		_batchTask = _batchTaskLocalService.addBatchTask(
			fileEntry.getFileEntryId(), BlogPosting.class.getName(), "v1.0",
			contentType, BatchOperation.CREATE);

		BatchTaskExecutor batchTaskExecutor = _batchTaskExecutorFactory.create(
			BlogPosting.class);

		batchTaskExecutor.execute(_batchTask.getBatchTaskId());

		Assert.assertEquals(
			998, _blogsEntryLocalService.getBlogsEntriesCount());
	}

	private AcceptLanguage _acceptLanguage = new AcceptLanguage() {

		@Override
		public List<Locale> getLocales() {
			return null;
		}

		@Override
		public String getPreferredLanguageId() {
			return null;
		}

		@Override
		public Locale getPreferredLocale() {
			return LocaleUtil.getDefault();
		}

	};

	private ServiceRegistration<BatchItemWriter>
		_batchItemWriterServiceRegistration;
	private BatchTask _batchTask;

	@Inject
	private BatchTaskExecutorFactory _batchTaskExecutorFactory;

	@Inject
	private BatchTaskLocalService _batchTaskLocalService;

	@Inject
	private BlogPostingResource _blogPostingResource;

	@Inject
	private BlogsEntryLocalService _blogsEntryLocalService;

	private BundleContext _bundleContext;
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