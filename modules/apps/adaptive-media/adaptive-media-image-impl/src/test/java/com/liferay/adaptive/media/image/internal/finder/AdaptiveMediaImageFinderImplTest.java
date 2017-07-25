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

package com.liferay.adaptive.media.image.internal.finder;

import com.liferay.adaptive.media.AdaptiveMedia;
import com.liferay.adaptive.media.AdaptiveMediaAttribute;
import com.liferay.adaptive.media.exception.AdaptiveMediaRuntimeException;
import com.liferay.adaptive.media.finder.AdaptiveMediaQuery;
import com.liferay.adaptive.media.image.configuration.AdaptiveMediaImageConfigurationEntry;
import com.liferay.adaptive.media.image.configuration.AdaptiveMediaImageConfigurationHelper;
import com.liferay.adaptive.media.image.finder.AdaptiveMediaImageQueryBuilder;
import com.liferay.adaptive.media.image.internal.configuration.AdaptiveMediaImageConfigurationEntryImpl;
import com.liferay.adaptive.media.image.internal.util.ImageProcessor;
import com.liferay.adaptive.media.image.model.AdaptiveMediaImageEntry;
import com.liferay.adaptive.media.image.processor.AdaptiveMediaImageAttribute;
import com.liferay.adaptive.media.image.processor.AdaptiveMediaImageProcessor;
import com.liferay.adaptive.media.image.service.AdaptiveMediaImageEntryLocalService;
import com.liferay.adaptive.media.image.url.AdaptiveMediaImageURLFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Adolfo Pérez
 */
public class AdaptiveMediaImageFinderImplTest {

	@Before
	public void setUp() {
		_adaptiveMediaImageFinder.setAdaptiveMediaImageURLFactory(
			_adaptiveMediaImageURLFactory);
		_adaptiveMediaImageFinder.setAdaptiveMediaImageConfigurationHelper(
			_adaptiveMediaImageConfigurationHelper);
		_adaptiveMediaImageFinder.setImageProcessor(_imageProcessor);
		_adaptiveMediaImageFinder.setAdaptiveMediaImageEntryLocalService(
			_adaptiveMediaImageEntryLocalService);
	}

	@Test(expected = PortalException.class)
	public void testFileEntryGetFileVersionFails() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				new HashMap<>());

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileEntry.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Collections.singleton(configurationEntry)
		);

		Mockito.when(
			_fileEntry.getFileVersion()
		).thenThrow(
			PortalException.class
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.allForFileEntry(
					_fileEntry
				).done());

		adaptiveMediaStream.count();
	}

	@Test
	public void testFileEntryGetMediaWithNoAttributes() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				new HashMap<>());

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Collections.singleton(configurationEntry)
		);

		Mockito.when(
			_fileEntry.getFileVersion()
		).thenReturn(
			_fileVersion
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry = _mockImage(
			800, 900, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.allForFileEntry(
					_fileEntry
				).done());

		Assert.assertEquals(1, adaptiveMediaStream.count());
	}

	@Test
	public void testGetMediaAttributes() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "200"));

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Collections.singleton(configurationEntry)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry imageEntry = _mockImage(99, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			imageEntry
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.allForVersion(
					_fileVersion
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		Assert.assertEquals(
			adaptiveMediaList.toString(), 1, adaptiveMediaList.size());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia =
			adaptiveMediaList.get(0);

		Assert.assertEquals(
			adaptiveMedia.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_HEIGHT),
			Optional.of(99));

		Assert.assertEquals(
			adaptiveMedia.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_WIDTH),
			Optional.of(199));
	}

	@Test
	public void testGetMediaAttributesOrderByAsc() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry1 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "200"));
		AdaptiveMediaImageConfigurationEntry configurationEntry2 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "800"));
		AdaptiveMediaImageConfigurationEntry configurationEntry3 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "400"));

		List<AdaptiveMediaImageConfigurationEntry> configurationEntries =
			Arrays.asList(
				configurationEntry1, configurationEntry2, configurationEntry3);

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			configurationEntries
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry1 = _mockImage(
			99, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry1.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry1
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry2 = _mockImage(
			99, 799, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry2.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry2
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry3 = _mockImage(
			99, 399, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry3.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry3
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.allForVersion(
					_fileVersion
				).orderBy(
					AdaptiveMediaImageAttribute.IMAGE_WIDTH,
					AdaptiveMediaImageQueryBuilder.SortOrder.ASC
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		Assert.assertEquals(
			adaptiveMediaList.toString(), 3, adaptiveMediaList.size());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia1 =
			adaptiveMediaList.get(0);

		Assert.assertEquals(
			adaptiveMedia1.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_WIDTH),
			Optional.of(199));

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia2 =
			adaptiveMediaList.get(1);

		Assert.assertEquals(
			adaptiveMedia2.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_WIDTH),
			Optional.of(399));

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia3 =
			adaptiveMediaList.get(2);

		Assert.assertEquals(
			adaptiveMedia3.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_WIDTH),
			Optional.of(799));
	}

	@Test
	public void testGetMediaAttributesOrderByDesc() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry1 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "200"));
		AdaptiveMediaImageConfigurationEntry configurationEntry2 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "800"));
		AdaptiveMediaImageConfigurationEntry configurationEntry3 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "400"));

		List<AdaptiveMediaImageConfigurationEntry> configurationEntries =
			Arrays.asList(
				configurationEntry1, configurationEntry2, configurationEntry3);

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			configurationEntries
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry1 = _mockImage(
			99, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry1.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry1
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry2 = _mockImage(
			99, 799, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry2.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry2
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry3 = _mockImage(
			99, 399, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry3.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry3
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.allForVersion(
					_fileVersion
				).orderBy(
					AdaptiveMediaImageAttribute.IMAGE_WIDTH,
					AdaptiveMediaImageQueryBuilder.SortOrder.DESC
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		Assert.assertEquals(
			adaptiveMediaList.toString(), 3, adaptiveMediaList.size());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia1 =
			adaptiveMediaList.get(0);

		Assert.assertEquals(
			adaptiveMedia1.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_WIDTH),
			Optional.of(799));

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia2 =
			adaptiveMediaList.get(1);

		Assert.assertEquals(
			adaptiveMedia2.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_WIDTH),
			Optional.of(399));

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia3 =
			adaptiveMediaList.get(2);

		Assert.assertEquals(
			adaptiveMedia3.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_WIDTH),
			Optional.of(199));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetMediaAttributesWithNonBuilderQuery() throws Exception {
		_adaptiveMediaImageFinder.getAdaptiveMediaStream(queryBuilder ->
			new AdaptiveMediaQuery<FileVersion, AdaptiveMediaImageProcessor>() {
			});
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetMediaAttributesWithNullQuery() throws Exception {
		_adaptiveMediaImageFinder.getAdaptiveMediaStream(queryBuilder -> null);
	}

	@Test(expected = AdaptiveMediaRuntimeException.InvalidConfiguration.class)
	public void testGetMediaConfigurationError() throws Exception {
		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					Mockito.anyLong(), Mockito.any(Predicate.class))
		).thenThrow(
			AdaptiveMediaRuntimeException.InvalidConfiguration.class
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		_adaptiveMediaImageFinder.getAdaptiveMediaStream(
			queryBuilder -> queryBuilder.allForVersion(
				_fileVersion
			).done());
	}

	@Test
	public void testGetMediaInputStream() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				Collections.emptyMap());

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Collections.singleton(configurationEntry)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry = _mockImage(
			800, 900, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		InputStream inputStream = Mockito.mock(InputStream.class);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.
				getAdaptiveMediaImageEntryContentStream(
					configurationEntry, _fileVersion)
		).thenReturn(
			inputStream
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.allForVersion(
					_fileVersion
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		Assert.assertEquals(
			adaptiveMediaList.toString(), 1, adaptiveMediaList.size());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia =
			adaptiveMediaList.get(0);

		Assert.assertSame(inputStream, adaptiveMedia.getInputStream());
	}

	@Test
	public void testGetMediaMissingAttribute() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100"));

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Collections.singleton(configurationEntry)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry = _mockImage(
			99, 1000, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.allForVersion(
					_fileVersion
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		Assert.assertEquals(
			adaptiveMediaList.toString(), 1, adaptiveMediaList.size());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia =
			adaptiveMediaList.get(0);

		Assert.assertEquals(
			adaptiveMedia.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_HEIGHT),
			Optional.of(99));

		Assert.assertEquals(
			adaptiveMedia.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_WIDTH),
			Optional.of(1000));
	}

	@Test
	public void testGetMediaQueryWith100Height() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry1 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "200"));

		AdaptiveMediaImageConfigurationEntry configurationEntry2 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "200", "max-width", "200"));

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry1, configurationEntry2)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry1 = _mockImage(
			99, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry1.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry1
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry2 = _mockImage(
			199, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry2.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry2
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.forVersion(
					_fileVersion
				).with(
					AdaptiveMediaImageAttribute.IMAGE_HEIGHT, 100
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia0 =
			adaptiveMediaList.get(0);

		Optional<Integer> adaptiveMedia0HeightOptional =
			adaptiveMedia0.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_HEIGHT);

		Assert.assertEquals(99, (int)adaptiveMedia0HeightOptional.get());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia1 =
			adaptiveMediaList.get(1);

		Optional<Integer> adaptiveMedia1HeightOptional =
			adaptiveMedia1.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_HEIGHT);

		Assert.assertEquals(199, (int)adaptiveMedia1HeightOptional.get());
	}

	@Test
	public void testGetMediaQueryWith200Height() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry1 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "200"));

		AdaptiveMediaImageConfigurationEntry configurationEntry2 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "200", "max-width", "200"));

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry1, configurationEntry2)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry1 = _mockImage(
			99, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry1.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry1
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry2 = _mockImage(
			199, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry2.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry2
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.forVersion(
					_fileVersion
				).with(
					AdaptiveMediaImageAttribute.IMAGE_HEIGHT, 200
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia0 =
			adaptiveMediaList.get(0);

		Optional<Integer> adaptiveMedia0HeightOptional =
			adaptiveMedia0.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_HEIGHT);

		Assert.assertEquals(199, (int)adaptiveMedia0HeightOptional.get());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia1 =
			adaptiveMediaList.get(1);

		Optional<Integer> adaptiveMedia1HeightOptional =
			adaptiveMedia1.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_HEIGHT);

		Assert.assertEquals(99, (int)adaptiveMedia1HeightOptional.get());
	}

	@Test
	public void testGetMediaQueryWith200HeightAspectRatio() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry1 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "200"));

		AdaptiveMediaImageConfigurationEntry configurationEntry2 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "200", "max-width", "100"));

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry1, configurationEntry2)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry1 = _mockImage(
			99, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry1.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry1
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry2 = _mockImage(
			55, 99, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry2.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry2
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.forVersion(
					_fileVersion
				).with(
					AdaptiveMediaImageAttribute.IMAGE_HEIGHT, 200
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia0 =
			adaptiveMediaList.get(0);

		Optional<Integer> adaptiveMedia0HeightOptional =
			adaptiveMedia0.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_HEIGHT);

		Assert.assertEquals(99, (int)adaptiveMedia0HeightOptional.get());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia1 =
			adaptiveMediaList.get(1);

		Optional<Integer> adaptiveMedia1HeightOptional =
			adaptiveMedia1.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_HEIGHT);

		Assert.assertEquals(55, (int)adaptiveMedia1HeightOptional.get());
	}

	@Test
	public void testGetMediaQueryWithConfigurationAttribute() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry1 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), "small",
				MapUtil.fromArray("max-height", "100", "max-width", "200"));

		AdaptiveMediaImageConfigurationEntry configurationEntry2 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), "medium",
				MapUtil.fromArray("max-height", "200", "max-width", "200"));

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			anyConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ANY;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					anyConfigurationStatus.getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry1, configurationEntry2)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry1 = _mockImage(
			99, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry1.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry1
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry2 = _mockImage(
			199, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry2.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry2
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.forVersion(
					_fileVersion
				).forConfiguration(
					"small"
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		Assert.assertEquals(
			adaptiveMediaList.toString(), 1, adaptiveMediaList.size());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia0 =
			adaptiveMediaList.get(0);

		Optional<String> adaptiveMedia0Optional =
			adaptiveMedia0.getValueOptional(
				AdaptiveMediaAttribute.getConfigurationUuid());

		Assert.assertEquals("small", adaptiveMedia0Optional.get());
	}

	@Test
	public void testGetMediaQueryWithConfigurationStatusAttributeForConfiguration()
		throws Exception {

		AdaptiveMediaImageConfigurationEntry configurationEntry1 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), "small",
				MapUtil.fromArray("max-height", "100", "max-width", "200"));

		AdaptiveMediaImageConfigurationEntry configurationEntry2 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(), "medium",
				MapUtil.fromArray("max-height", "200", "max-width", "200"),
				false);

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ANY.
						getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry1, configurationEntry2)
		);

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					AdaptiveMediaImageQueryBuilder.ConfigurationStatus.DISABLED.
						getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry2)
		);

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED.
						getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry1)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry1 = _mockImage(
			99, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry1.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry1
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry2 = _mockImage(
			199, 199, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry2.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry2
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(queryBuilder ->
				queryBuilder.forVersion(
					_fileVersion
				).withConfigurationStatus(
					AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED
				).forConfiguration(
					"small"
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		Assert.assertEquals(
			adaptiveMediaList.toString(), 1, adaptiveMediaList.size());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia0 =
			adaptiveMediaList.get(0);

		Optional<String> adaptiveMedia0Optional =
			adaptiveMedia0.getValueOptional(
				AdaptiveMediaAttribute.getConfigurationUuid());

		Assert.assertEquals("small", adaptiveMedia0Optional.get());

		adaptiveMediaStream = _adaptiveMediaImageFinder.getAdaptiveMediaStream(
			queryBuilder -> queryBuilder.forVersion(
				_fileVersion
			).withConfigurationStatus(
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ANY
			).forConfiguration(
				"small"
			).done());

		adaptiveMediaList = adaptiveMediaStream.collect(Collectors.toList());

		Assert.assertEquals(
			adaptiveMediaList.toString(), 1, adaptiveMediaList.size());

		adaptiveMedia0 = adaptiveMediaList.get(0);

		adaptiveMedia0Optional = adaptiveMedia0.getValueOptional(
			AdaptiveMediaAttribute.getConfigurationUuid());

		Assert.assertEquals("small", adaptiveMedia0Optional.get());

		adaptiveMediaStream = _adaptiveMediaImageFinder.getAdaptiveMediaStream(
			queryBuilder -> queryBuilder.forVersion(
				_fileVersion
			).withConfigurationStatus(
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.DISABLED
			).forConfiguration(
				"small"
			).done());

		adaptiveMediaList = adaptiveMediaStream.collect(Collectors.toList());

		Assert.assertEquals(
			adaptiveMediaList.toString(), 0, adaptiveMediaList.size());
	}

	@Test
	public void testGetMediaQueryWithConfigurationStatusAttributeWithWidth()
		throws Exception {

		AdaptiveMediaImageConfigurationEntry configurationEntry1 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), "1",
				MapUtil.fromArray("max-height", "100"));

		AdaptiveMediaImageConfigurationEntry configurationEntry2 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(), "2",
				MapUtil.fromArray("max-height", "200"), false);

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry1)
		);

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			disabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.DISABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					disabledConfigurationStatus.getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry2)
		);

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			allConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ANY;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					allConfigurationStatus.getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry1, configurationEntry2)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry1 = _mockImage(
			100, 1000, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry1.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry1
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry2 = _mockImage(
			200, 1000, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry2.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry2
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.forVersion(
					_fileVersion
				).withConfigurationStatus(
					enabledConfigurationStatus
				).with(
					AdaptiveMediaImageAttribute.IMAGE_WIDTH, 100
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia0 =
			adaptiveMediaList.get(0);

		Optional<String> adaptiveMedia0ConfigurationUuidOptional =
			adaptiveMedia0.getValueOptional(
				AdaptiveMediaAttribute.getConfigurationUuid());

		Assert.assertEquals("1", adaptiveMedia0ConfigurationUuidOptional.get());

		adaptiveMediaStream = _adaptiveMediaImageFinder.getAdaptiveMediaStream(
			queryBuilder -> queryBuilder.forVersion(
				_fileVersion
			).withConfigurationStatus(
				disabledConfigurationStatus
			).with(
				AdaptiveMediaImageAttribute.IMAGE_WIDTH, 100
			).done());

		adaptiveMediaList = adaptiveMediaStream.collect(Collectors.toList());

		adaptiveMedia0 = adaptiveMediaList.get(0);

		adaptiveMedia0ConfigurationUuidOptional =
			adaptiveMedia0.getValueOptional(
				AdaptiveMediaAttribute.getConfigurationUuid());

		Assert.assertEquals("2", adaptiveMedia0ConfigurationUuidOptional.get());

		adaptiveMediaStream = _adaptiveMediaImageFinder.getAdaptiveMediaStream(
			queryBuilder -> queryBuilder.forVersion(
				_fileVersion
			).withConfigurationStatus(
				allConfigurationStatus
			).with(
				AdaptiveMediaImageAttribute.IMAGE_WIDTH, 100
			).done());

		adaptiveMediaList = adaptiveMediaStream.collect(Collectors.toList());

		adaptiveMedia0 = adaptiveMediaList.get(0);

		adaptiveMedia0ConfigurationUuidOptional =
			adaptiveMedia0.getValueOptional(
				AdaptiveMediaAttribute.getConfigurationUuid());

		Assert.assertEquals("1", adaptiveMedia0ConfigurationUuidOptional.get());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia1 =
			adaptiveMediaList.get(1);

		Optional<String> adaptiveMedia1ConfigurationUuidOptional =
			adaptiveMedia1.getValueOptional(
				AdaptiveMediaAttribute.getConfigurationUuid());

		Assert.assertEquals("2", adaptiveMedia1ConfigurationUuidOptional.get());
	}

	@Test
	public void testGetMediaQueryWithNoMatchingAttributes() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry1 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100"));

		AdaptiveMediaImageConfigurationEntry configurationEntry2 =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "200"));

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Arrays.asList(configurationEntry1, configurationEntry2)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry1 = _mockImage(
			99, 1000, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry1.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry1
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry2 = _mockImage(
			199, 1000, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry2.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry2
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.forVersion(
					_fileVersion
				).with(
					AdaptiveMediaImageAttribute.IMAGE_WIDTH, 100
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia0 =
			adaptiveMediaList.get(0);

		Optional<Integer> adaptiveMedia0HeightOptional =
			adaptiveMedia0.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_HEIGHT);

		Assert.assertEquals(99, (int)adaptiveMedia0HeightOptional.get());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia1 =
			adaptiveMediaList.get(1);

		Optional<Integer> adaptiveMedia1HeightOptional =
			adaptiveMedia1.getValueOptional(
				AdaptiveMediaImageAttribute.IMAGE_HEIGHT);

		Assert.assertEquals(199, (int)adaptiveMedia1HeightOptional.get());
	}

	@Test
	public void testGetMediaWhenNotSupported() throws Exception {
		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			false
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.allForVersion(
					_fileVersion
				).done());

		Object[] adaptiveMediaArray = adaptiveMediaStream.toArray();

		Assert.assertEquals(
			Arrays.toString(adaptiveMediaArray), 0, adaptiveMediaArray.length);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetMediaWithNullFunction() throws Exception {
		_adaptiveMediaImageFinder.getAdaptiveMediaStream(null);
	}

	@Test
	public void testMediaLazilyDelegatesOnStorageInputStream()
		throws Exception {

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				StringUtil.randomString(), StringUtil.randomString(),
				MapUtil.fromArray("max-height", "100", "max-width", "200"));

		AdaptiveMediaImageQueryBuilder.ConfigurationStatus
			enabledConfigurationStatus =
				AdaptiveMediaImageQueryBuilder.ConfigurationStatus.ENABLED;

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntries(
					_fileVersion.getCompanyId(),
					enabledConfigurationStatus.getPredicate())
		).thenReturn(
			Collections.singleton(configurationEntry)
		);

		Mockito.when(
			_fileVersion.getFileName()
		).thenReturn(
			StringUtil.randomString()
		);

		Mockito.when(
			_fileVersion.getMimeType()
		).thenReturn(
			"image/jpeg"
		);

		AdaptiveMediaImageEntry adaptiveMediaImageEntry = _mockImage(
			99, 99, 1000L);

		Mockito.when(
			_adaptiveMediaImageEntryLocalService.fetchAdaptiveMediaImageEntry(
				configurationEntry.getUUID(), _fileVersion.getFileVersionId())
		).thenReturn(
			adaptiveMediaImageEntry
		);

		Mockito.when(
			_imageProcessor.isMimeTypeSupported(Mockito.anyString())
		).thenReturn(
			true
		);

		Stream<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaStream =
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				queryBuilder -> queryBuilder.allForVersion(
					_fileVersion
				).done());

		List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList =
			adaptiveMediaStream.collect(Collectors.toList());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia =
			adaptiveMediaList.get(0);

		adaptiveMedia.getInputStream();

		Mockito.verify(
			_adaptiveMediaImageEntryLocalService
		).getAdaptiveMediaImageEntryContentStream(
			configurationEntry, _fileVersion
		);
	}

	private AdaptiveMediaImageEntry _mockImage(
		int height, int width, long size) {

		AdaptiveMediaImageEntry adaptiveMediaImageEntry = Mockito.mock(
			AdaptiveMediaImageEntry.class);

		Mockito.when(
			adaptiveMediaImageEntry.getHeight()
		).thenReturn(
			height
		);

		Mockito.when(
			adaptiveMediaImageEntry.getWidth()
		).thenReturn(
			width
		);

		Mockito.when(
			adaptiveMediaImageEntry.getSize()
		).thenReturn(
			size
		);

		return adaptiveMediaImageEntry;
	}

	private final AdaptiveMediaImageConfigurationHelper
		_adaptiveMediaImageConfigurationHelper = Mockito.mock(
			AdaptiveMediaImageConfigurationHelper.class);
	private final AdaptiveMediaImageEntryLocalService
		_adaptiveMediaImageEntryLocalService = Mockito.mock(
			AdaptiveMediaImageEntryLocalService.class);
	private final AdaptiveMediaImageFinderImpl _adaptiveMediaImageFinder =
		new AdaptiveMediaImageFinderImpl();
	private final AdaptiveMediaImageURLFactory _adaptiveMediaImageURLFactory =
		Mockito.mock(AdaptiveMediaImageURLFactory.class);
	private final FileEntry _fileEntry = Mockito.mock(FileEntry.class);
	private final FileVersion _fileVersion = Mockito.mock(FileVersion.class);
	private final ImageProcessor _imageProcessor = Mockito.mock(
		ImageProcessor.class);

}