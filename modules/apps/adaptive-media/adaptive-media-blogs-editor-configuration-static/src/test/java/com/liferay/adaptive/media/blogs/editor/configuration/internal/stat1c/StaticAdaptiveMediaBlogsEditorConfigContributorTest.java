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

package com.liferay.adaptive.media.blogs.editor.configuration.internal.stat1c;

import com.liferay.adaptive.media.image.item.selector.AdaptiveMediaImageURLItemSelectorReturnType;
import com.liferay.blogs.item.selector.criterion.BlogsItemSelectorCriterion;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.criteria.URLItemSelectorReturnType;
import com.liferay.item.selector.criteria.file.criterion.FileItemSelectorCriterion;
import com.liferay.item.selector.criteria.image.criterion.ImageItemSelectorCriterion;
import com.liferay.item.selector.criteria.upload.criterion.UploadItemSelectorCriterion;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletURL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.powermock.api.mockito.PowerMockito;

import org.skyscreamer.jsonassert.JSONAssert;

/**
 * @author Alejandro Tardín
 */
public class StaticAdaptiveMediaBlogsEditorConfigContributorTest
	extends PowerMockito {

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		JSONFactoryUtil jsonFactoryUtil = new JSONFactoryUtil();

		jsonFactoryUtil.setJSONFactory(new JSONFactoryImpl());

		_inputEditorTaglibAttributes.put(
			"liferay-ui:input-editor:name", "testEditor");
	}

	@Test
	public void testAddAdaptiveMediaImageURLItemSelectorReturnType()
		throws Exception {

		BlogsItemSelectorCriterion blogsItemSelectorCriterion =
			new BlogsItemSelectorCriterion();

		blogsItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			Collections.<ItemSelectorReturnType>singletonList(
				new URLItemSelectorReturnType()));

		StaticAdaptiveMediaBlogsEditorConfigContributor
			staticAdaptiveMediaBlogsEditorConfigContributor =
				new StaticAdaptiveMediaBlogsEditorConfigContributor();

		staticAdaptiveMediaBlogsEditorConfigContributor.
			addAdaptiveMediaImageURLItemSelectorReturnType(
				blogsItemSelectorCriterion);

		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			blogsItemSelectorCriterion.getDesiredItemSelectorReturnTypes();

		Assert.assertEquals(
			desiredItemSelectorReturnTypes.toString(), 2,
			desiredItemSelectorReturnTypes.size());
		Assert.assertTrue(
			desiredItemSelectorReturnTypes.get(0) instanceof
				AdaptiveMediaImageURLItemSelectorReturnType);
		Assert.assertTrue(
			desiredItemSelectorReturnTypes.get(1) instanceof
				URLItemSelectorReturnType);
	}

	@Test
	public void testAddsPictureAndSourceAndImageAreAddedToAllowedContentForBlogs()
		throws Exception {

		_testAddsPictureAndImgTagToAllowedContent(
			_getItemSelectorCriterion(BlogsItemSelectorCriterion.class));
	}

	@Test
	public void testAddsPictureAndSourceAndImageAreAddedToAllowedContentForImage()
		throws Exception {

		_testAddsPictureAndImgTagToAllowedContent(
			_getItemSelectorCriterion(ImageItemSelectorCriterion.class));
	}

	@Test
	public void testAddsPictureAndSourceAndImgAreAddedToAllowedContentForFile()
		throws Exception {

		_testAddsPictureAndImgTagToAllowedContent(
			_getItemSelectorCriterion(FileItemSelectorCriterion.class));
	}

	@Test
	public void testAddsPictureAndSourceAreAddedToAllowedContentForUpload()
		throws Exception {

		_testSetsPictureAndImgTagToAllowedContent(
			_getItemSelectorCriterion(UploadItemSelectorCriterion.class));
	}

	@Test
	public void testAnyExtraPluginsArePreserved() throws Exception {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("extraPlugins", "testPlugin");
		jsonObject.put("filebrowserImageBrowseLinkUrl", "browseLinkUrl");

		Mockito.doReturn(
			Arrays.asList(
				_getItemSelectorCriterion(ImageItemSelectorCriterion.class))
		).when(
			_itemSelector
		).getItemSelectorCriteria(
			Mockito.anyString()
		);

		PortletURL portletURL = Mockito.mock(PortletURL.class);

		Mockito.doReturn(
			portletURL
		).when(
			_itemSelector
		).getItemSelectorURL(
			Mockito.any(RequestBackedPortletURLFactory.class),
			Mockito.anyString(), Mockito.any(ItemSelectorCriterion[].class)
		);

		StaticAdaptiveMediaBlogsEditorConfigContributor
			staticAdaptiveMediaBlogsEditorConfigContributor =
				new StaticAdaptiveMediaBlogsEditorConfigContributor();

		staticAdaptiveMediaBlogsEditorConfigContributor.setItemSelector(
			_itemSelector);

		staticAdaptiveMediaBlogsEditorConfigContributor.
			populateConfigJSONObject(
				jsonObject, _inputEditorTaglibAttributes, _themeDisplay,
				_requestBackedPortletURLFactory);

		Assert.assertEquals(
			"testPlugin,adaptivemedia", jsonObject.get("extraPlugins"));
	}

	@Test
	public void testDoesNothingForUnsupportedItemSelectorCriterion()
		throws Exception {

		JSONObject originalJSONObject = JSONFactoryUtil.createJSONObject();

		originalJSONObject.put("allowedContent", "a[*](*); div(*);");
		originalJSONObject.put(
			"filebrowserImageBrowseLinkUrl",
			"blogsItemSelectorCriterionFileEntryItemSelectorReturnType");

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		JSONObject expectedJSONObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		PortletURL itemSelectorPortletURL = mock(PortletURL.class);

		when(
			itemSelectorPortletURL.toString()
		).thenReturn(
			"itemSelectorPortletURL"
		);

		when(
			_itemSelector.getItemSelectorURL(
				Mockito.any(RequestBackedPortletURLFactory.class),
				Mockito.anyString(), Mockito.any(ItemSelectorCriterion.class))
		).thenReturn(
			itemSelectorPortletURL
		);

		when(
			_itemSelector.getItemSelectedEventName(Mockito.anyString())
		).thenReturn(
			"selectedEventName"
		);

		when(
			_itemSelector.getItemSelectorCriteria(
				"blogsItemSelectorCriterionFileEntryItemSelectorReturnType")
		).thenReturn(
			Collections.<ItemSelectorCriterion>emptyList()
		);

		StaticAdaptiveMediaBlogsEditorConfigContributor
			staticAdaptiveMediaBlogsEditorConfigContributor =
				new StaticAdaptiveMediaBlogsEditorConfigContributor();

		staticAdaptiveMediaBlogsEditorConfigContributor.setItemSelector(
			_itemSelector);

		staticAdaptiveMediaBlogsEditorConfigContributor.
			populateConfigJSONObject(
				jsonObject, _inputEditorTaglibAttributes, _themeDisplay,
				_requestBackedPortletURLFactory);

		JSONAssert.assertEquals(
			expectedJSONObject.toJSONString(), jsonObject.toJSONString(), true);
	}

	@Test
	public void testDoesNothingIfTheAdaptiveMediaPluginHasAlreadyBeenAdded()
		throws Exception {

		PortletURL itemSelectorPortletURL = mock(PortletURL.class);

		when(
			itemSelectorPortletURL.toString()
		).thenReturn(
			"itemSelectorPortletURL"
		);

		when(
			_itemSelector.getItemSelectorURL(
				Mockito.any(RequestBackedPortletURLFactory.class),
				Mockito.anyString(), Mockito.any(ItemSelectorCriterion.class))
		).thenReturn(
			itemSelectorPortletURL
		);

		when(
			_itemSelector.getItemSelectedEventName(Mockito.anyString())
		).thenReturn(
			"selectedEventName"
		);

		ItemSelectorCriterion itemSelectorCriterion = _getItemSelectorCriterion(
			BlogsItemSelectorCriterion.class);

		when(
			_itemSelector.getItemSelectorCriteria(
				"blogsItemSelectorCriterionFileEntryItemSelectorReturnType")
		).thenReturn(
			Arrays.asList(itemSelectorCriterion)
		);

		JSONObject originalJSONObject = JSONFactoryUtil.createJSONObject();

		originalJSONObject.put("allowedContent", "a[*](*); div(*);");
		originalJSONObject.put("extraPlugins", "adaptivemedia");
		originalJSONObject.put(
			"filebrowserImageBrowseLinkUrl",
			"blogsItemSelectorCriterionFileEntryItemSelectorReturnType");

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		JSONObject expectedJSONObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		StaticAdaptiveMediaBlogsEditorConfigContributor
			staticAdaptiveMediaBlogsEditorConfigContributor =
				new StaticAdaptiveMediaBlogsEditorConfigContributor();

		staticAdaptiveMediaBlogsEditorConfigContributor.setItemSelector(
			_itemSelector);

		staticAdaptiveMediaBlogsEditorConfigContributor.
			populateConfigJSONObject(
				jsonObject, _inputEditorTaglibAttributes, _themeDisplay,
				_requestBackedPortletURLFactory);

		JSONAssert.assertEquals(
			expectedJSONObject.toJSONString(), jsonObject.toJSONString(), true);
	}

	@Test
	public void testDoesNothingIfThereIsNotFileBrowserImageBrowseLinkUrl()
		throws Exception {

		JSONObject originalJSONObject = JSONFactoryUtil.createJSONObject();

		originalJSONObject.put("allowedContent", "a[*](*); div(*);");

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		JSONObject expectedJSONObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		StaticAdaptiveMediaBlogsEditorConfigContributor
			staticAdaptiveMediaBlogsEditorConfigContributor =
				new StaticAdaptiveMediaBlogsEditorConfigContributor();

		staticAdaptiveMediaBlogsEditorConfigContributor.
			populateConfigJSONObject(
				jsonObject, _inputEditorTaglibAttributes, _themeDisplay,
				_requestBackedPortletURLFactory);

		JSONAssert.assertEquals(
			expectedJSONObject.toJSONString(), jsonObject.toJSONString(), true);
	}

	@Test
	public void testSetsPictureAndSourceAndImgAreAddedToAllowedContentForBlogs()
		throws Exception {

		_testSetsPictureAndImgTagToAllowedContent(
			_getItemSelectorCriterion(BlogsItemSelectorCriterion.class));
	}

	@Test
	public void testSetsPictureAndSourceAndImgAreAddedToAllowedContentForFile()
		throws Exception {

		_testSetsPictureAndImgTagToAllowedContent(
			_getItemSelectorCriterion(FileItemSelectorCriterion.class));
	}

	@Test
	public void testSetsPictureAndSourceAndImgAreAddedToAllowedContentForImage()
		throws Exception {

		_testSetsPictureAndImgTagToAllowedContent(
			_getItemSelectorCriterion(ImageItemSelectorCriterion.class));
	}

	@Test
	public void testSetsPictureAndSourceAreAddedToAllowedContentForUpload()
		throws Exception {

		_testSetsPictureAndImgTagToAllowedContent(
			_getItemSelectorCriterion(UploadItemSelectorCriterion.class));
	}

	private ItemSelectorCriterion _getItemSelectorCriterion(
			Class<? extends ItemSelectorCriterion> itemSelectorCriterionClass)
		throws IllegalAccessException, InstantiationException {

		ItemSelectorCriterion itemSelectorCriterion =
			itemSelectorCriterionClass.newInstance();

		itemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			new ArrayList<ItemSelectorReturnType>());

		return itemSelectorCriterion;
	}

	private void _testAddsPictureAndImgTagToAllowedContent(
			ItemSelectorCriterion itemSelectorCriterion)
		throws Exception {

		PortletURL itemSelectorPortletURL = mock(PortletURL.class);

		when(
			itemSelectorPortletURL.toString()
		).thenReturn(
			"itemSelectorPortletURL"
		);

		when(
			_itemSelector.getItemSelectorURL(
				Mockito.any(RequestBackedPortletURLFactory.class),
				Mockito.anyString(), Mockito.any(ItemSelectorCriterion.class))
		).thenReturn(
			itemSelectorPortletURL
		);

		when(
			_itemSelector.getItemSelectedEventName(Mockito.anyString())
		).thenReturn(
			"selectedEventName"
		);

		when(
			_itemSelector.getItemSelectorCriteria(
				"blogsItemSelectorCriterionFileEntryItemSelectorReturnType")
		).thenReturn(
			Arrays.asList(itemSelectorCriterion)
		);

		JSONObject originalJSONObject = JSONFactoryUtil.createJSONObject();

		originalJSONObject.put("allowedContent", "a[*](*); div(*);");
		originalJSONObject.put(
			"filebrowserImageBrowseLinkUrl",
			"blogsItemSelectorCriterionFileEntryItemSelectorReturnType");

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		JSONObject expectedJSONObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		expectedJSONObject.put(
			"allowedContent",
			"a[*](*); div(*); picture[*](*); source[*](*); img[*](*){*};");
		expectedJSONObject.put("extraPlugins", "adaptivemedia");
		expectedJSONObject.put(
			"filebrowserImageBrowseLinkUrl", itemSelectorPortletURL.toString());
		expectedJSONObject.put(
			"filebrowserImageBrowseUrl", itemSelectorPortletURL.toString());

		StaticAdaptiveMediaBlogsEditorConfigContributor
			staticAdaptiveMediaBlogsEditorConfigContributor =
				new StaticAdaptiveMediaBlogsEditorConfigContributor();

		staticAdaptiveMediaBlogsEditorConfigContributor.setItemSelector(
			_itemSelector);

		staticAdaptiveMediaBlogsEditorConfigContributor.
			populateConfigJSONObject(
				jsonObject, _inputEditorTaglibAttributes, _themeDisplay,
				_requestBackedPortletURLFactory);

		JSONAssert.assertEquals(
			expectedJSONObject.toJSONString(), jsonObject.toJSONString(), true);
	}

	private void _testSetsPictureAndImgTagToAllowedContent(
			ItemSelectorCriterion itemSelectorCriterion)
		throws Exception {

		PortletURL itemSelectorPortletURL = mock(PortletURL.class);

		when(
			itemSelectorPortletURL.toString()
		).thenReturn(
			"itemSelectorPortletURL"
		);

		when(
			_itemSelector.getItemSelectorURL(
				Mockito.any(RequestBackedPortletURLFactory.class),
				Mockito.anyString(), Mockito.any(ItemSelectorCriterion.class))
		).thenReturn(
			itemSelectorPortletURL
		);

		when(
			_itemSelector.getItemSelectedEventName(Mockito.anyString())
		).thenReturn(
			"selectedEventName"
		);

		when(
			_itemSelector.getItemSelectorCriteria(
				"blogsItemSelectorCriterionFileEntryItemSelectorReturnType")
		).thenReturn(
			Arrays.asList(itemSelectorCriterion)
		);

		JSONObject originalJSONObject = JSONFactoryUtil.createJSONObject();

		originalJSONObject.put(
			"filebrowserImageBrowseLinkUrl",
			"blogsItemSelectorCriterionFileEntryItemSelectorReturnType");

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			originalJSONObject.toJSONString());

		JSONObject expectedJSONObject = JSONFactoryUtil.createJSONObject();

		expectedJSONObject.put("extraPlugins", "adaptivemedia");
		expectedJSONObject.put(
			"filebrowserImageBrowseLinkUrl", itemSelectorPortletURL.toString());
		expectedJSONObject.put(
			"filebrowserImageBrowseUrl", itemSelectorPortletURL.toString());

		StaticAdaptiveMediaBlogsEditorConfigContributor
			staticAdaptiveMediaBlogsEditorConfigContributor =
				new StaticAdaptiveMediaBlogsEditorConfigContributor();

		staticAdaptiveMediaBlogsEditorConfigContributor.setItemSelector(
			_itemSelector);

		staticAdaptiveMediaBlogsEditorConfigContributor.
			populateConfigJSONObject(
				jsonObject, _inputEditorTaglibAttributes, _themeDisplay,
				_requestBackedPortletURLFactory);

		JSONAssert.assertEquals(
			expectedJSONObject.toJSONString(), jsonObject.toJSONString(), true);
	}

	private final Map<String, Object> _inputEditorTaglibAttributes =
		new HashMap<>();

	@Mock
	private ItemSelector _itemSelector;

	@Mock
	private RequestBackedPortletURLFactory _requestBackedPortletURLFactory;

	@Mock
	private ThemeDisplay _themeDisplay;

}