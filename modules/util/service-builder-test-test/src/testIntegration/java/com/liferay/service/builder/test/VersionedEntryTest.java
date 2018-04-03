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

package com.liferay.service.builder.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.service.version.VersionService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.service.builder.test.model.VersionedEntry;
import com.liferay.service.builder.test.model.VersionedEntryVersion;
import com.liferay.service.builder.test.service.VersionedEntryLocalService;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Preston Crary
 */
@RunWith(Arquillian.class)
public class VersionedEntryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testCreate() {
		VersionedEntry versionedEntry = _versionService.create();

		Assert.assertTrue(versionedEntry.isDraft());
		Assert.assertTrue(versionedEntry.isNew());
		Assert.assertEquals(
			versionedEntry.getHeadId(), versionedEntry.getPrimaryKey());

		Assert.assertNull(
			_versionService.fetchDraft(versionedEntry.getPrimaryKey()));
	}

	@Test
	public void testDelete() throws Exception {
		_versionedEntry = _versionService.create();

		_versionedEntry = _versionService.publish(_versionedEntry);

		VersionedEntry draftVersionedEntry = _versionService.getDraft(
			_versionedEntry);

		draftVersionedEntry = _versionService.update(draftVersionedEntry);

		Assert.assertNotNull(draftVersionedEntry);
		Assert.assertNotEquals(draftVersionedEntry, _versionedEntry);

		List<VersionedEntryVersion> versionedEntryVersions =
			_versionService.delete(_versionedEntry);

		Assert.assertEquals(
			versionedEntryVersions.toString(), 1,
			versionedEntryVersions.size());

		versionedEntryVersions = _versionService.getVersions(_versionedEntry);

		Assert.assertEquals(
			versionedEntryVersions.toString(), 0,
			versionedEntryVersions.size());

		_versionedEntry = _versionService.fetchPublished(
			_versionedEntry.getVersionedEntryId());

		Assert.assertNull(_versionedEntry);

		draftVersionedEntry = _versionService.fetchDraft(
			draftVersionedEntry.getHeadId());

		Assert.assertNull(draftVersionedEntry);
	}

	@Test
	public void testDeleteDraft() throws Exception {
		_versionedEntry = _versionService.create();

		VersionedEntry draftVersionedEntry = _versionService.getDraft(
			_versionedEntry);

		draftVersionedEntry = _versionService.update(draftVersionedEntry);

		Assert.assertEquals(draftVersionedEntry, _versionedEntry);

		Assert.assertTrue(_versionedEntry.isDraft());

		List<VersionedEntryVersion> versionedEntryVersions =
			_versionService.delete(_versionedEntry);

		Assert.assertSame(Collections.emptyList(), versionedEntryVersions);

		Assert.assertNull(
			_versionService.fetchDraft(_versionedEntry.getPrimaryKey()));
	}

	@Test
	public void testDeleteLatestVersion() throws Exception {
		_versionedEntry = _versionService.create();

		_versionedEntry = _versionService.publish(_versionedEntry);

		VersionedEntryVersion versionedEntryVersion =
			_versionService.fetchLatestVersion(_versionedEntry);

		try {
			_versionService.deleteVersion(versionedEntryVersion);

			Assert.fail();
		}
		catch (IllegalArgumentException iae) {
			Assert.assertEquals(
				"Cannot delete latest version 1, revert to go back to a " +
					"previous version",
				iae.getMessage());
		}
	}

	@Test
	public void testDeleteVersion() throws Exception {
		_versionedEntry = _versionService.create();

		_versionedEntry = _versionService.publish(_versionedEntry);

		VersionedEntry draftVersionedEntry = _versionService.getDraft(
			_versionedEntry);

		_versionedEntry = _versionService.publish(draftVersionedEntry);

		VersionedEntryVersion versionedEntryVersion =
			_versionService.getVersion(_versionedEntry, 1);

		Assert.assertEquals(
			versionedEntryVersion,
			_versionService.deleteVersion(versionedEntryVersion));
	}

	@Test
	public void testFetchDraft() throws Exception {
		_versionedEntry = _versionService.create();

		_versionedEntry.setContent(RandomTestUtil.randomString());

		_versionedEntry = _versionService.update(_versionedEntry);

		Assert.assertTrue(_versionedEntry.isDraft());

		Assert.assertSame(
			_versionedEntry, _versionService.fetchDraft(_versionedEntry));

		_versionedEntry = _versionService.publish(_versionedEntry);

		Assert.assertNull(_versionService.fetchDraft(_versionedEntry));

		VersionedEntry draftVersionedEntry = _versionService.getDraft(
			_versionedEntry);

		Assert.assertNotNull(draftVersionedEntry);
		Assert.assertNotEquals(draftVersionedEntry, _versionedEntry);

		Assert.assertEquals(
			draftVersionedEntry.getContent(), _versionedEntry.getContent());
	}

	@Test
	public void testFetchPublished() throws Exception {
		_versionedEntry = _versionService.create();

		_versionedEntry = _versionService.update(_versionedEntry);

		Assert.assertTrue(_versionedEntry.isDraft());

		Assert.assertNull(_versionService.fetchPublished(_versionedEntry));
		Assert.assertNull(
			_versionService.fetchPublished(_versionedEntry.getPrimaryKey()));

		_versionedEntry = _versionService.publish(_versionedEntry);

		Assert.assertEquals(
			_versionedEntry, _versionService.fetchPublished(_versionedEntry));

		VersionedEntry draftVersionedEntry = _versionService.getDraft(
			_versionedEntry);

		Assert.assertEquals(
			_versionedEntry,
			_versionService.fetchPublished(draftVersionedEntry));
		Assert.assertEquals(
			_versionedEntry,
			_versionService.fetchPublished(draftVersionedEntry.getHeadId()));
	}

	@Test
	public void testGetDraft() throws Exception {
		_versionedEntry = _versionService.create();

		_versionedEntry = _versionService.update(_versionedEntry);

		VersionedEntry draftVersionedEntry = _versionService.getDraft(
			_versionedEntry.getPrimaryKey());

		Assert.assertEquals(_versionedEntry, draftVersionedEntry);

		_versionedEntry = _versionService.publish(draftVersionedEntry);

		draftVersionedEntry = _versionService.getDraft(
			_versionedEntry.getPrimaryKey());

		Assert.assertNotEquals(_versionedEntry, draftVersionedEntry);
	}

	@Test
	public void testGetVersions() throws Exception {
		_versionedEntry = _versionService.create();

		String content1 = RandomTestUtil.randomString();

		_versionedEntry.setContent(content1);

		_versionedEntry = _versionService.publish(_versionedEntry);

		VersionedEntry draftVersionedEntry = _versionService.getDraft(
			_versionedEntry);

		String content2 = RandomTestUtil.randomString();

		draftVersionedEntry.setContent(content2);

		_versionedEntry = _versionService.publish(draftVersionedEntry);

		List<VersionedEntryVersion> versionedEntryVersions =
			_versionService.getVersions(draftVersionedEntry);

		Assert.assertEquals(
			versionedEntryVersions.toString(), 2,
			versionedEntryVersions.size());

		VersionedEntryVersion versionedEntryVersion2 =
			versionedEntryVersions.get(0);

		Assert.assertEquals(2, versionedEntryVersion2.getVersion());
		Assert.assertEquals(content2, versionedEntryVersion2.getContent());

		VersionedEntryVersion versionedEntryVersion1 =
			versionedEntryVersions.get(1);

		Assert.assertEquals(1, versionedEntryVersion1.getVersion());
		Assert.assertEquals(content1, versionedEntryVersion1.getContent());
	}

	@Test
	public void testPublish() throws Exception {
		_versionedEntry = _versionService.create();

		Assert.assertTrue(
			_versionedEntry.toString(), _versionedEntry.isDraft());

		_versionedEntry = _versionService.publish(_versionedEntry);

		Assert.assertFalse(
			_versionedEntry.toString(), _versionedEntry.isDraft());

		VersionedEntryVersion versionedEntryVersion =
			_versionService.fetchLatestVersion(_versionedEntry);

		Assert.assertNotNull(versionedEntryVersion);

		Assert.assertEquals(1, versionedEntryVersion.getVersion());

		try {
			_versionService.publish(_versionedEntry);

			Assert.fail();
		}
		catch (IllegalArgumentException iae) {
			Assert.assertEquals(
				"Can only publish drafts " + _versionedEntry.getPrimaryKey(),
				iae.getMessage());
		}
	}

	@Test
	public void testRevert() throws Exception {
		_versionedEntry = _versionService.create();

		String content1 = RandomTestUtil.randomString();

		_versionedEntry.setContent(content1);

		_versionService.update(_versionedEntry);

		List<VersionedEntryVersion> versionedEntryVersions =
			_versionService.getVersions(_versionedEntry);

		Assert.assertSame(Collections.emptyList(), versionedEntryVersions);

		_versionedEntry = _versionService.publish(_versionedEntry);

		Assert.assertEquals(content1, _versionedEntry.getContent());

		VersionedEntry draftVersionedEntry = _versionService.getDraft(
			_versionedEntry);

		String content2 = RandomTestUtil.randomString();

		draftVersionedEntry.setContent(content2);

		_versionedEntry = _versionService.publish(draftVersionedEntry);

		Assert.assertEquals(content2, _versionedEntry.getContent());

		_versionedEntry = _versionService.revert(_versionedEntry, 1);

		Assert.assertEquals(content1, _versionedEntry.getContent());

		versionedEntryVersions = _versionService.getVersions(_versionedEntry);

		Assert.assertEquals(
			versionedEntryVersions.toString(), 3,
			versionedEntryVersions.size());

		VersionedEntryVersion versionedEntryVersion3 =
			versionedEntryVersions.get(0);
		VersionedEntryVersion versionedEntryVersion2 =
			versionedEntryVersions.get(1);
		VersionedEntryVersion versionedEntryVersion1 =
			versionedEntryVersions.get(2);

		Assert.assertEquals(3, versionedEntryVersion3.getVersion());
		Assert.assertEquals(content1, versionedEntryVersion3.getContent());

		Assert.assertEquals(2, versionedEntryVersion2.getVersion());
		Assert.assertEquals(content2, versionedEntryVersion2.getContent());

		Assert.assertEquals(1, versionedEntryVersion1.getVersion());
		Assert.assertEquals(content1, versionedEntryVersion1.getContent());

		_versionedEntry = _versionService.revert(draftVersionedEntry, 1);

		versionedEntryVersions = _versionService.getVersions(_versionedEntry);

		Assert.assertEquals(
			versionedEntryVersions.toString(), 4,
			versionedEntryVersions.size());

		VersionedEntryVersion versionedEntryVersion4 =
			versionedEntryVersions.get(0);
		versionedEntryVersion3 = versionedEntryVersions.get(1);
		versionedEntryVersion2 = versionedEntryVersions.get(2);
		versionedEntryVersion1 = versionedEntryVersions.get(3);

		Assert.assertEquals(4, versionedEntryVersion4.getVersion());
		Assert.assertEquals(content1, versionedEntryVersion4.getContent());

		Assert.assertEquals(3, versionedEntryVersion3.getVersion());
		Assert.assertEquals(content1, versionedEntryVersion3.getContent());

		Assert.assertEquals(2, versionedEntryVersion2.getVersion());
		Assert.assertEquals(content2, versionedEntryVersion2.getContent());

		Assert.assertEquals(1, versionedEntryVersion1.getVersion());
		Assert.assertEquals(content1, versionedEntryVersion1.getContent());
	}

	@Test
	public void testSameService() throws Exception {
		Assert.assertSame(_versionedEntryLocalService, _versionService);
	}

	@Test
	public void testUpdate() throws Exception {
		_versionedEntry = _versionService.create();

		_versionedEntry = _versionService.publish(_versionedEntry);

		_versionService.update(_versionedEntry);

		try {
			_versionService.update(_versionedEntry);

			Assert.fail();
		}
		catch (IllegalArgumentException iae) {
			Assert.assertEquals(
				"Draft already exists for " + _versionedEntry.getPrimaryKey(),
				iae.getMessage());
		}
	}

	@Inject
	private static VersionedEntryLocalService _versionedEntryLocalService;

	@Inject(
		filter = "model.class.name=com.liferay.service.builder.test.model.VersionedEntry"
	)
	private static VersionService<VersionedEntry, VersionedEntryVersion>
		_versionService;

	@DeleteAfterTestRun
	private VersionedEntry _versionedEntry;

}