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

package com.liferay.adaptive.media.image.internal.handler;

import com.liferay.adaptive.media.AdaptiveMedia;
import com.liferay.adaptive.media.AdaptiveMediaAttribute;
import com.liferay.adaptive.media.exception.AdaptiveMediaException;
import com.liferay.adaptive.media.exception.AdaptiveMediaRuntimeException;
import com.liferay.adaptive.media.finder.AdaptiveMediaQuery;
import com.liferay.adaptive.media.image.configuration.AdaptiveMediaImageConfigurationEntry;
import com.liferay.adaptive.media.image.configuration.AdaptiveMediaImageConfigurationHelper;
import com.liferay.adaptive.media.image.finder.AdaptiveMediaImageFinder;
import com.liferay.adaptive.media.image.finder.AdaptiveMediaImageQueryBuilder;
import com.liferay.adaptive.media.image.internal.configuration.AdaptiveMediaImageAttributeMapping;
import com.liferay.adaptive.media.image.internal.configuration.AdaptiveMediaImageConfigurationEntryImpl;
import com.liferay.adaptive.media.image.internal.finder.AdaptiveMediaImageQueryBuilderImpl;
import com.liferay.adaptive.media.image.internal.processor.AdaptiveMediaImage;
import com.liferay.adaptive.media.image.internal.util.Tuple;
import com.liferay.adaptive.media.image.processor.AdaptiveMediaImageAttribute;
import com.liferay.adaptive.media.image.processor.AdaptiveMediaImageProcessor;
import com.liferay.adaptive.media.processor.AdaptiveMediaAsyncProcessor;
import com.liferay.adaptive.media.processor.AdaptiveMediaAsyncProcessorLocator;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.InputStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Adolfo Pérez
 * @author Alejandro Tardín
 */
public class AdaptiveMediaImageRequestHandlerTest {

	@Before
	public void setUp() throws PortalException {
		_fileVersion = _getFileVersion();

		Mockito.doReturn(
			_adaptiveMediaAsyncProcessor
		).when(
			_adaptiveMediaAsyncProcessorLocator
		).locateForClass(
			FileVersion.class
		);

		_requestHandler.setAsyncProcessorLocator(
			_adaptiveMediaAsyncProcessorLocator);
		_requestHandler.setAdaptiveMediaImageFinder(_adaptiveMediaImageFinder);
		_requestHandler.setPathInterpreter(_pathInterpreter);
		_requestHandler.setAdaptiveMediaImageConfigurationHelper(
			_adaptiveMediaImageConfigurationHelper);
	}

	@Test(expected = AdaptiveMediaRuntimeException.class)
	public void testFinderFailsWithMediaProcessorException() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry =
			_createAdaptiveMediaImageConfigurationEntry(
				_fileVersion.getCompanyId(), 200, 500);

		HttpServletRequest request = _createRequestFor(
			_fileVersion, configurationEntry);

		Mockito.when(
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				Mockito.any(Function.class))
		).thenThrow(
			AdaptiveMediaException.class
		);

		_requestHandler.handleRequest(request);
	}

	@Test(expected = AdaptiveMediaRuntimeException.class)
	public void testFinderFailsWithPortalException() throws Exception {
		AdaptiveMediaImageConfigurationEntry configurationEntry =
			_createAdaptiveMediaImageConfigurationEntry(
				_fileVersion.getCompanyId(), 200, 500);

		HttpServletRequest request = _createRequestFor(
			_fileVersion, configurationEntry);

		Mockito.when(
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				Mockito.any(Function.class))
		).thenThrow(
			PortalException.class
		);

		_requestHandler.handleRequest(request);
	}

	@Test
	public void testInvalidPath() throws Exception {
		Mockito.when(
			_pathInterpreter.interpretPath(Mockito.anyString())
		).thenReturn(
			Optional.empty()
		);

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		Optional<AdaptiveMedia<AdaptiveMediaImageProcessor>>
			adaptiveMediaOptional = _requestHandler.handleRequest(request);

		Assert.assertFalse(adaptiveMediaOptional.isPresent());
	}

	@Test(expected = NullPointerException.class)
	public void testNullRequest() throws Exception {
		_requestHandler.handleRequest(null);
	}

	@Test
	public void testPathInterpreterFailure() throws Exception {
		Mockito.when(
			_pathInterpreter.interpretPath(Mockito.anyString())
		).thenThrow(
			AdaptiveMediaRuntimeException.class
		);

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		Optional<AdaptiveMedia<AdaptiveMediaImageProcessor>>
			adaptiveMediaOptional = _requestHandler.handleRequest(request);

		Assert.assertFalse(adaptiveMediaOptional.isPresent());
	}

	@Test
	public void testReturnsTheClosestMatchByWidthIfNoExactMatchPresentAndRunsTheProcess()
		throws Exception {

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			_createAdaptiveMediaImageConfigurationEntry(
				_fileVersion.getCompanyId(), 200, 500);

		AdaptiveMediaImageConfigurationEntry closestConfigurationEntry =
			_createAdaptiveMediaImageConfigurationEntry(
				_fileVersion.getCompanyId(), 201, 501);

		AdaptiveMediaImageConfigurationEntry fartherConfigurationEntry =
			_createAdaptiveMediaImageConfigurationEntry(
				_fileVersion.getCompanyId(), 301, 501);

		AdaptiveMediaImageConfigurationEntry farthestConfigurationEntry =
			_createAdaptiveMediaImageConfigurationEntry(
				_fileVersion.getCompanyId(), 401, 501);

		AdaptiveMedia<AdaptiveMediaImageProcessor> closestAdaptiveMedia =
			_createAdaptiveMedia(_fileVersion, closestConfigurationEntry);

		AdaptiveMedia<AdaptiveMediaImageProcessor> fartherAdaptiveMedia =
			_createAdaptiveMedia(_fileVersion, fartherConfigurationEntry);

		AdaptiveMedia<AdaptiveMediaImageProcessor> farthestAdaptiveMedia =
			_createAdaptiveMedia(_fileVersion, farthestConfigurationEntry);

		_mockClosestMatch(
			_fileVersion, configurationEntry,
			Arrays.asList(
				farthestAdaptiveMedia, closestAdaptiveMedia,
				fartherAdaptiveMedia));

		HttpServletRequest request = _createRequestFor(
			_fileVersion, configurationEntry);

		Assert.assertEquals(
			Optional.of(closestAdaptiveMedia),
			_requestHandler.handleRequest(request));

		Mockito.verify(
			_adaptiveMediaAsyncProcessor
		).triggerProcess(
			_fileVersion, String.valueOf(_fileVersion.getFileVersionId())
		);
	}

	@Test
	public void testReturnsTheExactMatchIfPresentAndDoesNotRunTheProcess()
		throws Exception {

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			_createAdaptiveMediaImageConfigurationEntry(
				_fileVersion.getCompanyId(), 200, 500);

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia =
			_createAdaptiveMedia(_fileVersion, configurationEntry);

		_mockExactMatch(_fileVersion, configurationEntry, adaptiveMedia);

		HttpServletRequest request = _createRequestFor(
			_fileVersion, configurationEntry);

		Assert.assertEquals(
			Optional.of(adaptiveMedia), _requestHandler.handleRequest(request));

		Mockito.verify(
			_adaptiveMediaAsyncProcessor, Mockito.never()
		).triggerProcess(
			_fileVersion, String.valueOf(_fileVersion.getFileVersionId())
		);
	}

	@Test
	public void testReturnsTheRealImageIfThereAreNoAdaptiveMediasAndRunsTheProcess()
		throws Exception {

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			_createAdaptiveMediaImageConfigurationEntry(
				_fileVersion.getCompanyId(), 200, 500);

		HttpServletRequest request = _createRequestFor(
			_fileVersion, configurationEntry);

		Mockito.when(
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				Mockito.any(Function.class))
		).thenAnswer(
			invocation -> Stream.empty()
		);

		Optional<AdaptiveMedia<AdaptiveMediaImageProcessor>>
			adaptiveMediaOptional = _requestHandler.handleRequest(request);

		Assert.assertTrue(adaptiveMediaOptional.isPresent());

		AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia =
			adaptiveMediaOptional.get();

		Assert.assertEquals(
			_fileVersion.getContentStream(false),
			adaptiveMedia.getInputStream());

		Assert.assertEquals(
			Optional.of(_fileVersion.getFileName()),
			adaptiveMedia.getValueOptional(
				AdaptiveMediaAttribute.getFileName()));

		Assert.assertEquals(
			Optional.of(_fileVersion.getMimeType()),
			adaptiveMedia.getValueOptional(
				AdaptiveMediaAttribute.getContentType()));

		Optional<Integer> contentLength = adaptiveMedia.getValueOptional(
			AdaptiveMediaAttribute.getContentLength());

		Assert.assertEquals(_fileVersion.getSize(), (long)contentLength.get());

		Mockito.verify(
			_adaptiveMediaAsyncProcessor
		).triggerProcess(
			_fileVersion, String.valueOf(_fileVersion.getFileVersionId())
		);
	}

	private AdaptiveMedia<AdaptiveMediaImageProcessor> _createAdaptiveMedia(
			FileVersion fileVersion,
			AdaptiveMediaImageConfigurationEntry configurationEntry)
		throws Exception {

		Map<String, String> properties = new HashMap<>();

		AdaptiveMediaAttribute<Object, String> configurationUuid =
			AdaptiveMediaAttribute.getConfigurationUuid();

		properties.put(
			configurationUuid.getName(), configurationEntry.getUUID());

		AdaptiveMediaAttribute<Object, String> fileName =
			AdaptiveMediaAttribute.getFileName();

		properties.put(fileName.getName(), fileVersion.getFileName());

		AdaptiveMediaAttribute<Object, String> contentType =
			AdaptiveMediaAttribute.getContentType();

		properties.put(contentType.getName(), fileVersion.getMimeType());

		AdaptiveMediaAttribute<Object, Integer> contentLength =
			AdaptiveMediaAttribute.getContentLength();

		properties.put(
			contentLength.getName(), String.valueOf(fileVersion.getSize()));

		Map<String, String> configurationEntryProperties =
			configurationEntry.getProperties();

		properties.put(
			AdaptiveMediaImageAttribute.IMAGE_WIDTH.getName(),
			configurationEntryProperties.get("max-width"));
		properties.put(
			AdaptiveMediaImageAttribute.IMAGE_HEIGHT.getName(),
			configurationEntryProperties.get("max-height"));

		return new AdaptiveMediaImage(
			() -> {
				try {
					return fileVersion.getContentStream(false);
				}
				catch (PortalException pe) {
					throw new AdaptiveMediaRuntimeException(pe);
				}
			},
			AdaptiveMediaImageAttributeMapping.fromProperties(properties),
			null);
	}

	private AdaptiveMediaImageConfigurationEntry
		_createAdaptiveMediaImageConfigurationEntry(
			long companyId, int width, int height) {

		String uuid = "testUuid" + Math.random();

		final Map<String, String> properties = new HashMap<>();

		properties.put("configuration-uuid", uuid);
		properties.put("max-height", String.valueOf(height));
		properties.put("max-width", String.valueOf(width));

		AdaptiveMediaImageConfigurationEntryImpl configurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				uuid, uuid, properties);

		Mockito.when(
			_adaptiveMediaImageConfigurationHelper.
				getAdaptiveMediaImageConfigurationEntry(
					companyId, configurationEntry.getUUID())
		).thenReturn(
			Optional.of(configurationEntry)
		);

		return configurationEntry;
	}

	private HttpServletRequest _createRequestFor(
		FileVersion fileVersion,
		AdaptiveMediaImageConfigurationEntry configurationEntry) {

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

		Mockito.when(
			request.getPathInfo()
		).thenReturn(
			"pathInfo"
		);

		Map<String, String> pathProperties = new HashMap<>();

		pathProperties.put("configuration-uuid", configurationEntry.getUUID());

		Mockito.when(
			_pathInterpreter.interpretPath(request.getPathInfo())
		).thenReturn(
			Optional.of(Tuple.of(fileVersion, pathProperties))
		);

		return request;
	}

	private FileVersion _getFileVersion() throws PortalException {
		FileVersion fileVersion = Mockito.mock(FileVersion.class);

		Mockito.when(
			fileVersion.getCompanyId()
		).thenReturn(
			1234L
		);

		Mockito.when(
			fileVersion.getContentStream(false)
		).thenReturn(
			Mockito.mock(InputStream.class)
		);

		Mockito.when(
			fileVersion.getMimeType()
		).thenReturn(
			"image/jpg"
		);

		Mockito.when(
			fileVersion.getFileName()
		).thenReturn(
			"fileName"
		);

		Mockito.when(
			fileVersion.getSize()
		).thenReturn(
			2048L
		);

		return fileVersion;
	}

	private void _mockClosestMatch(
			FileVersion fileVersion,
			AdaptiveMediaImageConfigurationEntry configurationEntry,
			List<AdaptiveMedia<AdaptiveMediaImageProcessor>> adaptiveMediaList)
		throws Exception {

		Mockito.when(
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				Mockito.any(Function.class))
		).thenAnswer(
			invocation -> {
				Function<AdaptiveMediaImageQueryBuilder, AdaptiveMediaQuery>
					function = invocation.getArgumentAt(0, Function.class);

				AdaptiveMediaImageQueryBuilderImpl queryBuilder =
					new AdaptiveMediaImageQueryBuilderImpl();

				AdaptiveMediaQuery adaptiveMediaQuery = function.apply(
					queryBuilder);

				Map<AdaptiveMediaAttribute<AdaptiveMediaImageProcessor, ?>,
					Object> attributes = queryBuilder.getAttributes();

				Object queryBuilderWidth = attributes.get(
					AdaptiveMediaImageAttribute.IMAGE_WIDTH);

				Object queryBuilderHeight = attributes.get(
					AdaptiveMediaImageAttribute.IMAGE_HEIGHT);

				Map<String, String> properties =
					configurationEntry.getProperties();

				int configurationWidth = GetterUtil.getInteger(
					properties.get("max-width"));

				int configurationHeight = GetterUtil.getInteger(
					properties.get("max-height"));

				if (AdaptiveMediaImageQueryBuilderImpl.QUERY.equals(
						adaptiveMediaQuery) &&
					fileVersion.equals(queryBuilder.getFileVersion()) &&
					(queryBuilder.getConfigurationUuid() == null) &&
					queryBuilderWidth.equals(configurationWidth) &&
					queryBuilderHeight.equals(configurationHeight)) {

					return adaptiveMediaList.stream();
				}

				return Stream.empty();
			}
		);
	}

	private void _mockExactMatch(
			FileVersion fileVersion,
			AdaptiveMediaImageConfigurationEntry configurationEntry,
			AdaptiveMedia<AdaptiveMediaImageProcessor> adaptiveMedia)
		throws Exception {

		Mockito.when(
			_adaptiveMediaImageFinder.getAdaptiveMediaStream(
				Mockito.any(Function.class))
		).thenAnswer(
			invocation -> {
				Function<AdaptiveMediaImageQueryBuilder, AdaptiveMediaQuery>
					function = invocation.getArgumentAt(0, Function.class);

				AdaptiveMediaImageQueryBuilderImpl queryBuilder =
					new AdaptiveMediaImageQueryBuilderImpl();

				AdaptiveMediaQuery adaptiveMediaQuery = function.apply(
					queryBuilder);

				if (!AdaptiveMediaImageQueryBuilderImpl.QUERY.equals(
						adaptiveMediaQuery)) {

					return Stream.empty();
				}

				if (fileVersion.equals(queryBuilder.getFileVersion())) {
					Predicate<AdaptiveMediaImageConfigurationEntry>
						configurationEntryFilter =
							queryBuilder.getConfigurationEntryFilter();

					if (configurationEntryFilter.test(configurationEntry)) {
						return Stream.of(adaptiveMedia);
					}
				}

				return Stream.empty();
			}
		);
	}

	private final AdaptiveMediaAsyncProcessor<FileVersion, ?>
		_adaptiveMediaAsyncProcessor = Mockito.mock(
			AdaptiveMediaAsyncProcessor.class);
	private final AdaptiveMediaAsyncProcessorLocator
		_adaptiveMediaAsyncProcessorLocator = Mockito.mock(
			AdaptiveMediaAsyncProcessorLocator.class);
	private final AdaptiveMediaImageConfigurationHelper
		_adaptiveMediaImageConfigurationHelper = Mockito.mock(
			AdaptiveMediaImageConfigurationHelper.class);
	private final AdaptiveMediaImageFinder _adaptiveMediaImageFinder =
		Mockito.mock(AdaptiveMediaImageFinder.class);
	private FileVersion _fileVersion;
	private final PathInterpreter _pathInterpreter = Mockito.mock(
		PathInterpreter.class);
	private final AdaptiveMediaImageRequestHandler _requestHandler =
		new AdaptiveMediaImageRequestHandler();

}