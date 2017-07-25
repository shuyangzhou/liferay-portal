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
import com.liferay.blogs.web.constants.BlogsPortletKeys;
import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.criteria.file.criterion.FileItemSelectorCriterion;
import com.liferay.item.selector.criteria.image.criterion.ImageItemSelectorCriterion;
import com.liferay.item.selector.criteria.upload.criterion.UploadItemSelectorCriterion;
import com.liferay.portal.kernel.editor.configuration.BaseEditorConfigContributor;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	property = {
		"editor.config.key=contentEditor", "editor.name=alloyeditor",
		"editor.name=ckeditor", "javax.portlet.name=" + BlogsPortletKeys.BLOGS,
		"javax.portlet.name=" + BlogsPortletKeys.BLOGS_ADMIN,
		"service.ranking:Integer=100"
	},
	service = EditorConfigContributor.class
)
public class StaticAdaptiveMediaBlogsEditorConfigContributor
	extends BaseEditorConfigContributor {

	@Override
	public void populateConfigJSONObject(
		JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
		ThemeDisplay themeDisplay,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

		String extraPlugins = jsonObject.getString("extraPlugins");

		if (StringUtil.contains(extraPlugins, "adaptivemedia")) {
			return;
		}

		String itemSelectorURL = jsonObject.getString(
			"filebrowserImageBrowseLinkUrl");

		if (Validator.isNull(itemSelectorURL)) {
			return;
		}

		List<ItemSelectorCriterion> itemSelectorCriteria =
			_itemSelector.getItemSelectorCriteria(itemSelectorURL);

		boolean adaptiveMediaImageURLItemSelectorReturnTypeAdded = false;

		for (ItemSelectorCriterion itemSelectorCriterion :
				itemSelectorCriteria) {

			if (itemSelectorCriterion instanceof BlogsItemSelectorCriterion ||
				itemSelectorCriterion instanceof FileItemSelectorCriterion ||
				itemSelectorCriterion instanceof ImageItemSelectorCriterion ||
				itemSelectorCriterion instanceof UploadItemSelectorCriterion) {

				addAdaptiveMediaImageURLItemSelectorReturnType(
					itemSelectorCriterion);

				adaptiveMediaImageURLItemSelectorReturnTypeAdded = true;
			}
		}

		if (!adaptiveMediaImageURLItemSelectorReturnTypeAdded) {
			return;
		}

		if (Validator.isNotNull(extraPlugins)) {
			extraPlugins = extraPlugins + ",adaptivemedia";
		}
		else {
			extraPlugins = "adaptivemedia";
		}

		jsonObject.put("extraPlugins", extraPlugins);

		String itemSelectedEventName = _itemSelector.getItemSelectedEventName(
			itemSelectorURL);

		PortletURL itemSelectorPortletURL = _itemSelector.getItemSelectorURL(
			requestBackedPortletURLFactory, itemSelectedEventName,
			itemSelectorCriteria.toArray(
				new ItemSelectorCriterion[itemSelectorCriteria.size()]));

		jsonObject.put(
			"filebrowserImageBrowseLinkUrl", itemSelectorPortletURL.toString());
		jsonObject.put(
			"filebrowserImageBrowseUrl", itemSelectorPortletURL.toString());

		_allowTagRule(jsonObject, _PICTURE_TAG_RULE);
		_allowTagRule(jsonObject, _IMG_TAG_RULE);
	}

	@Reference(unbind = "-")
	public void setItemSelector(ItemSelector itemSelector) {
		_itemSelector = itemSelector;
	}

	protected void addAdaptiveMediaImageURLItemSelectorReturnType(
		ItemSelectorCriterion itemSelectorCriterion) {

		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			new ArrayList<>();

		desiredItemSelectorReturnTypes.add(
			new AdaptiveMediaImageURLItemSelectorReturnType());
		desiredItemSelectorReturnTypes.addAll(
			itemSelectorCriterion.getDesiredItemSelectorReturnTypes());

		itemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);
	}

	private void _allowTagRule(JSONObject jsonObject, String tagRule) {
		String allowedContent = jsonObject.getString("allowedContent");

		if (Validator.isNotNull(allowedContent) &&
			!allowedContent.equals(Boolean.TRUE.toString())) {

			allowedContent += StringPool.SPACE + tagRule;

			jsonObject.put("allowedContent", allowedContent);
		}
	}

	private static final String _IMG_TAG_RULE = "img[*](*){*};";

	private static final String _PICTURE_TAG_RULE =
		"picture[*](*); source[*](*);";

	private ItemSelector _itemSelector;

}