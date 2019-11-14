/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.search.tuning.rankings.web.internal.results.builder;

import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.document.library.configuration.DLConfiguration;
import com.liferay.document.library.kernel.model.DLFileEntryConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.security.permission.ResourceActions;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.document.Document;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author André de Oliveira
 * @author Bryan Engler
 */
public class RankingJSONBuilder {

	public RankingJSONBuilder(
		DLAppLocalService dlAppLocalService, ResourceActions resourceActions) {

		_dlAppLocalService = dlAppLocalService;
		_dlConfiguration = ConfigurableUtil.createConfigurable(
			DLConfiguration.class, new HashMap<String, Object>());
		_resourceActions = resourceActions;
	}

	public JSONObject build() {
		return JSONUtil.put(
			"author", getAuthor()
		).put(
			"clicks", _document.getString("clicks")
		).put(
			"description", _document.getString(Field.DESCRIPTION)
		).put(
			"hidden", _hidden
		).put(
			"icon", getIcon()
		).put(
			"id", _document.getString(Field.UID)
		).put(
			"pinned", _pinned
		).put(
			"title", getTitle()
		).put(
			"type", getType(_locale)
		);
	}

	public RankingJSONBuilder document(Document document) {
		_document = document;

		return this;
	}

	public RankingJSONBuilder hidden(boolean hidden) {
		_hidden = hidden;

		return this;
	}

	public RankingJSONBuilder locale(Locale locale) {
		_locale = locale;

		return this;
	}

	public RankingJSONBuilder pinned(boolean pinned) {
		_pinned = pinned;

		return this;
	}

	protected String getAuthor() {
		if (_isUser()) {
			return _document.getString("screenName");
		}

		return _document.getString(Field.USER_NAME);
	}

	protected String getIcon() {
		if (_isFileEntry()) {
			long entryClassPK = _document.getLong(Field.ENTRY_CLASS_PK);

			try {
				FileEntry fileEntry = _dlAppLocalService.getFileEntry(
					entryClassPK);

				return _getIconFileMimeType(fileEntry.getMimeType());
			}
			catch (PortalException pe) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to get file entry for " + entryClassPK, pe);
				}

				return "document-default";
			}
		}

		AssetRendererFactory<?> assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				_document.getString(Field.ENTRY_CLASS_NAME));

		if (assetRendererFactory != null) {
			return assetRendererFactory.getIconCssClass();
		}

		return null;
	}

	protected String getTitle() {
		String title = _document.getString(Field.TITLE + "_en_US");

		if (!Validator.isBlank(title)) {
			return title;
		}

		title = _document.getString(Field.TITLE);

		if (!Validator.isBlank(title)) {
			return title;
		}

		if (_isUser()) {
			return _document.getString("fullName");
		}

		return _document.getString("name");
	}

	protected String getType(Locale locale) {
		String entryClassName = _document.getString(Field.ENTRY_CLASS_NAME);

		return _resourceActions.getModelResource(locale, entryClassName);
	}

	private boolean _containsMimeType(String[] mimeTypes, String mimeType) {
		for (String curMimeType : mimeTypes) {
			int pos = curMimeType.indexOf("/");

			if (pos != -1) {
				if (mimeType.equals(curMimeType)) {
					return true;
				}
			}
			else {
				if (mimeType.startsWith(curMimeType)) {
					return true;
				}
			}
		}

		return false;
	}

	private String _getIconFileMimeType(String mimeType) {
		if (_containsMimeType(_dlConfiguration.codeFileMimeTypes(), mimeType)) {
			return "document-code";
		}
		else if (_containsMimeType(
					_dlConfiguration.compressedFileMimeTypes(), mimeType)) {

			return "document-compressed";
		}
		else if (_containsMimeType(
					_dlConfiguration.multimediaFileMimeTypes(), mimeType)) {

			if (mimeType.startsWith("image")) {
				return "document-image";
			}

			return "document-multimedia";
		}
		else if (_containsMimeType(
					_dlConfiguration.presentationFileMimeTypes(), mimeType)) {

			return "document-presentation";
		}
		else if (_containsMimeType(
					_dlConfiguration.spreadSheetFileMimeTypes(), mimeType)) {

			return "document-table";
		}
		else if (_containsMimeType(
					_dlConfiguration.textFileMimeTypes(), mimeType)) {

			return "document-text";
		}
		else if (_containsMimeType(
					_dlConfiguration.vectorialFileMimeTypes(), mimeType)) {

			return "document-pdf";
		}

		return "document-default";
	}

	private boolean _isFileEntry() {
		String entryClassName = _document.getString(Field.ENTRY_CLASS_NAME);

		return entryClassName.equals(DLFileEntryConstants.getClassName());
	}

	private boolean _isUser() {
		String entryClassName = _document.getString(Field.ENTRY_CLASS_NAME);

		return entryClassName.equals(User.class.getName());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RankingJSONBuilder.class);

	private final DLAppLocalService _dlAppLocalService;
	private final DLConfiguration _dlConfiguration;
	private Document _document;
	private boolean _hidden;
	private Locale _locale;
	private boolean _pinned;
	private final ResourceActions _resourceActions;

}