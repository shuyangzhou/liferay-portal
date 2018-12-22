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

package com.liferay.asset.kernel.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link AssetEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntry
 * @generated
 */
@ProviderType
public class AssetEntryWrapper implements AssetEntry, ModelWrapper<AssetEntry> {
	public AssetEntryWrapper(AssetEntry assetEntry) {
		_assetEntry = assetEntry;
	}

	@Override
	public Class<?> getModelClass() {
		return AssetEntry.class;
	}

	@Override
	public String getModelClassName() {
		return AssetEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<AssetEntry, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<AssetEntry, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<AssetEntry, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<AssetEntry, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<AssetEntry, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<AssetEntry, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<AssetEntry, Object>> getAttributeGetters() {
		return _assetEntry.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<AssetEntry, Object>> getAttributeSetters() {
		return _assetEntry.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new AssetEntryWrapper((AssetEntry)_assetEntry.clone());
	}

	@Override
	public int compareTo(AssetEntry assetEntry) {
		return _assetEntry.compareTo(assetEntry);
	}

	@Override
	public AssetRenderer<?> getAssetRenderer() {
		return _assetEntry.getAssetRenderer();
	}

	@Override
	public AssetRendererFactory<?> getAssetRendererFactory() {
		return _assetEntry.getAssetRendererFactory();
	}

	@Override
	public String[] getAvailableLanguageIds() {
		return _assetEntry.getAvailableLanguageIds();
	}

	@Override
	public java.util.List<AssetCategory> getCategories() {
		return _assetEntry.getCategories();
	}

	@Override
	public long[] getCategoryIds() {
		return _assetEntry.getCategoryIds();
	}

	/**
	* Returns the fully qualified class name of this asset entry.
	*
	* @return the fully qualified class name of this asset entry
	*/
	@Override
	public String getClassName() {
		return _assetEntry.getClassName();
	}

	/**
	* Returns the class name ID of this asset entry.
	*
	* @return the class name ID of this asset entry
	*/
	@Override
	public long getClassNameId() {
		return _assetEntry.getClassNameId();
	}

	/**
	* Returns the class pk of this asset entry.
	*
	* @return the class pk of this asset entry
	*/
	@Override
	public long getClassPK() {
		return _assetEntry.getClassPK();
	}

	/**
	* Returns the class type ID of this asset entry.
	*
	* @return the class type ID of this asset entry
	*/
	@Override
	public long getClassTypeId() {
		return _assetEntry.getClassTypeId();
	}

	/**
	* Returns the class uuid of this asset entry.
	*
	* @return the class uuid of this asset entry
	*/
	@Override
	public String getClassUuid() {
		return _assetEntry.getClassUuid();
	}

	/**
	* Returns the company ID of this asset entry.
	*
	* @return the company ID of this asset entry
	*/
	@Override
	public long getCompanyId() {
		return _assetEntry.getCompanyId();
	}

	/**
	* Returns the create date of this asset entry.
	*
	* @return the create date of this asset entry
	*/
	@Override
	public Date getCreateDate() {
		return _assetEntry.getCreateDate();
	}

	@Override
	public String getDefaultLanguageId() {
		return _assetEntry.getDefaultLanguageId();
	}

	/**
	* Returns the description of this asset entry.
	*
	* @return the description of this asset entry
	*/
	@Override
	public String getDescription() {
		return _assetEntry.getDescription();
	}

	/**
	* Returns the localized description of this asset entry in the language. Uses the default language if no localization exists for the requested language.
	*
	* @param locale the locale of the language
	* @return the localized description of this asset entry
	*/
	@Override
	public String getDescription(java.util.Locale locale) {
		return _assetEntry.getDescription(locale);
	}

	/**
	* Returns the localized description of this asset entry in the language, optionally using the default language if no localization exists for the requested language.
	*
	* @param locale the local of the language
	* @param useDefault whether to use the default language if no localization exists for the requested language
	* @return the localized description of this asset entry. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	*/
	@Override
	public String getDescription(java.util.Locale locale, boolean useDefault) {
		return _assetEntry.getDescription(locale, useDefault);
	}

	/**
	* Returns the localized description of this asset entry in the language. Uses the default language if no localization exists for the requested language.
	*
	* @param languageId the ID of the language
	* @return the localized description of this asset entry
	*/
	@Override
	public String getDescription(String languageId) {
		return _assetEntry.getDescription(languageId);
	}

	/**
	* Returns the localized description of this asset entry in the language, optionally using the default language if no localization exists for the requested language.
	*
	* @param languageId the ID of the language
	* @param useDefault whether to use the default language if no localization exists for the requested language
	* @return the localized description of this asset entry
	*/
	@Override
	public String getDescription(String languageId, boolean useDefault) {
		return _assetEntry.getDescription(languageId, useDefault);
	}

	@Override
	public String getDescriptionCurrentLanguageId() {
		return _assetEntry.getDescriptionCurrentLanguageId();
	}

	@Override
	public String getDescriptionCurrentValue() {
		return _assetEntry.getDescriptionCurrentValue();
	}

	/**
	* Returns a map of the locales and localized descriptions of this asset entry.
	*
	* @return the locales and localized descriptions of this asset entry
	*/
	@Override
	public Map<java.util.Locale, String> getDescriptionMap() {
		return _assetEntry.getDescriptionMap();
	}

	/**
	* Returns the end date of this asset entry.
	*
	* @return the end date of this asset entry
	*/
	@Override
	public Date getEndDate() {
		return _assetEntry.getEndDate();
	}

	/**
	* Returns the entry ID of this asset entry.
	*
	* @return the entry ID of this asset entry
	*/
	@Override
	public long getEntryId() {
		return _assetEntry.getEntryId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _assetEntry.getExpandoBridge();
	}

	/**
	* Returns the expiration date of this asset entry.
	*
	* @return the expiration date of this asset entry
	*/
	@Override
	public Date getExpirationDate() {
		return _assetEntry.getExpirationDate();
	}

	/**
	* Returns the group ID of this asset entry.
	*
	* @return the group ID of this asset entry
	*/
	@Override
	public long getGroupId() {
		return _assetEntry.getGroupId();
	}

	/**
	* Returns the height of this asset entry.
	*
	* @return the height of this asset entry
	*/
	@Override
	public int getHeight() {
		return _assetEntry.getHeight();
	}

	/**
	* Returns the layout uuid of this asset entry.
	*
	* @return the layout uuid of this asset entry
	*/
	@Override
	public String getLayoutUuid() {
		return _assetEntry.getLayoutUuid();
	}

	/**
	* Returns the listable of this asset entry.
	*
	* @return the listable of this asset entry
	*/
	@Override
	public boolean getListable() {
		return _assetEntry.getListable();
	}

	/**
	* Returns the mime type of this asset entry.
	*
	* @return the mime type of this asset entry
	*/
	@Override
	public String getMimeType() {
		return _assetEntry.getMimeType();
	}

	/**
	* Returns the modified date of this asset entry.
	*
	* @return the modified date of this asset entry
	*/
	@Override
	public Date getModifiedDate() {
		return _assetEntry.getModifiedDate();
	}

	/**
	* Returns the primary key of this asset entry.
	*
	* @return the primary key of this asset entry
	*/
	@Override
	public long getPrimaryKey() {
		return _assetEntry.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _assetEntry.getPrimaryKeyObj();
	}

	/**
	* Returns the priority of this asset entry.
	*
	* @return the priority of this asset entry
	*/
	@Override
	public double getPriority() {
		return _assetEntry.getPriority();
	}

	/**
	* Returns the publish date of this asset entry.
	*
	* @return the publish date of this asset entry
	*/
	@Override
	public Date getPublishDate() {
		return _assetEntry.getPublishDate();
	}

	/**
	* Returns the start date of this asset entry.
	*
	* @return the start date of this asset entry
	*/
	@Override
	public Date getStartDate() {
		return _assetEntry.getStartDate();
	}

	/**
	* Returns the summary of this asset entry.
	*
	* @return the summary of this asset entry
	*/
	@Override
	public String getSummary() {
		return _assetEntry.getSummary();
	}

	/**
	* Returns the localized summary of this asset entry in the language. Uses the default language if no localization exists for the requested language.
	*
	* @param locale the locale of the language
	* @return the localized summary of this asset entry
	*/
	@Override
	public String getSummary(java.util.Locale locale) {
		return _assetEntry.getSummary(locale);
	}

	/**
	* Returns the localized summary of this asset entry in the language, optionally using the default language if no localization exists for the requested language.
	*
	* @param locale the local of the language
	* @param useDefault whether to use the default language if no localization exists for the requested language
	* @return the localized summary of this asset entry. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	*/
	@Override
	public String getSummary(java.util.Locale locale, boolean useDefault) {
		return _assetEntry.getSummary(locale, useDefault);
	}

	/**
	* Returns the localized summary of this asset entry in the language. Uses the default language if no localization exists for the requested language.
	*
	* @param languageId the ID of the language
	* @return the localized summary of this asset entry
	*/
	@Override
	public String getSummary(String languageId) {
		return _assetEntry.getSummary(languageId);
	}

	/**
	* Returns the localized summary of this asset entry in the language, optionally using the default language if no localization exists for the requested language.
	*
	* @param languageId the ID of the language
	* @param useDefault whether to use the default language if no localization exists for the requested language
	* @return the localized summary of this asset entry
	*/
	@Override
	public String getSummary(String languageId, boolean useDefault) {
		return _assetEntry.getSummary(languageId, useDefault);
	}

	@Override
	public String getSummaryCurrentLanguageId() {
		return _assetEntry.getSummaryCurrentLanguageId();
	}

	@Override
	public String getSummaryCurrentValue() {
		return _assetEntry.getSummaryCurrentValue();
	}

	/**
	* Returns a map of the locales and localized summaries of this asset entry.
	*
	* @return the locales and localized summaries of this asset entry
	*/
	@Override
	public Map<java.util.Locale, String> getSummaryMap() {
		return _assetEntry.getSummaryMap();
	}

	@Override
	public String[] getTagNames() {
		return _assetEntry.getTagNames();
	}

	@Override
	public java.util.List<AssetTag> getTags() {
		return _assetEntry.getTags();
	}

	/**
	* Returns the title of this asset entry.
	*
	* @return the title of this asset entry
	*/
	@Override
	public String getTitle() {
		return _assetEntry.getTitle();
	}

	/**
	* Returns the localized title of this asset entry in the language. Uses the default language if no localization exists for the requested language.
	*
	* @param locale the locale of the language
	* @return the localized title of this asset entry
	*/
	@Override
	public String getTitle(java.util.Locale locale) {
		return _assetEntry.getTitle(locale);
	}

	/**
	* Returns the localized title of this asset entry in the language, optionally using the default language if no localization exists for the requested language.
	*
	* @param locale the local of the language
	* @param useDefault whether to use the default language if no localization exists for the requested language
	* @return the localized title of this asset entry. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	*/
	@Override
	public String getTitle(java.util.Locale locale, boolean useDefault) {
		return _assetEntry.getTitle(locale, useDefault);
	}

	/**
	* Returns the localized title of this asset entry in the language. Uses the default language if no localization exists for the requested language.
	*
	* @param languageId the ID of the language
	* @return the localized title of this asset entry
	*/
	@Override
	public String getTitle(String languageId) {
		return _assetEntry.getTitle(languageId);
	}

	/**
	* Returns the localized title of this asset entry in the language, optionally using the default language if no localization exists for the requested language.
	*
	* @param languageId the ID of the language
	* @param useDefault whether to use the default language if no localization exists for the requested language
	* @return the localized title of this asset entry
	*/
	@Override
	public String getTitle(String languageId, boolean useDefault) {
		return _assetEntry.getTitle(languageId, useDefault);
	}

	@Override
	public String getTitleCurrentLanguageId() {
		return _assetEntry.getTitleCurrentLanguageId();
	}

	@Override
	public String getTitleCurrentValue() {
		return _assetEntry.getTitleCurrentValue();
	}

	/**
	* Returns a map of the locales and localized titles of this asset entry.
	*
	* @return the locales and localized titles of this asset entry
	*/
	@Override
	public Map<java.util.Locale, String> getTitleMap() {
		return _assetEntry.getTitleMap();
	}

	/**
	* Returns the url of this asset entry.
	*
	* @return the url of this asset entry
	*/
	@Override
	public String getUrl() {
		return _assetEntry.getUrl();
	}

	/**
	* Returns the user ID of this asset entry.
	*
	* @return the user ID of this asset entry
	*/
	@Override
	public long getUserId() {
		return _assetEntry.getUserId();
	}

	/**
	* Returns the user name of this asset entry.
	*
	* @return the user name of this asset entry
	*/
	@Override
	public String getUserName() {
		return _assetEntry.getUserName();
	}

	/**
	* Returns the user uuid of this asset entry.
	*
	* @return the user uuid of this asset entry
	*/
	@Override
	public String getUserUuid() {
		return _assetEntry.getUserUuid();
	}

	/**
	* Returns the view count of this asset entry.
	*
	* @return the view count of this asset entry
	*/
	@Override
	public int getViewCount() {
		return _assetEntry.getViewCount();
	}

	/**
	* Returns the visible of this asset entry.
	*
	* @return the visible of this asset entry
	*/
	@Override
	public boolean getVisible() {
		return _assetEntry.getVisible();
	}

	/**
	* Returns the width of this asset entry.
	*
	* @return the width of this asset entry
	*/
	@Override
	public int getWidth() {
		return _assetEntry.getWidth();
	}

	@Override
	public int hashCode() {
		return _assetEntry.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _assetEntry.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _assetEntry.isEscapedModel();
	}

	/**
	* Returns <code>true</code> if this asset entry is listable.
	*
	* @return <code>true</code> if this asset entry is listable; <code>false</code> otherwise
	*/
	@Override
	public boolean isListable() {
		return _assetEntry.isListable();
	}

	@Override
	public boolean isNew() {
		return _assetEntry.isNew();
	}

	/**
	* Returns <code>true</code> if this asset entry is visible.
	*
	* @return <code>true</code> if this asset entry is visible; <code>false</code> otherwise
	*/
	@Override
	public boolean isVisible() {
		return _assetEntry.isVisible();
	}

	@Override
	public void persist() {
		_assetEntry.persist();
	}

	@Override
	public void prepareLocalizedFieldsForImport()
		throws com.liferay.portal.kernel.exception.LocaleException {
		_assetEntry.prepareLocalizedFieldsForImport();
	}

	@Override
	public void prepareLocalizedFieldsForImport(
		java.util.Locale defaultImportLocale)
		throws com.liferay.portal.kernel.exception.LocaleException {
		_assetEntry.prepareLocalizedFieldsForImport(defaultImportLocale);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_assetEntry.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_assetEntry.setClassName(className);
	}

	/**
	* Sets the class name ID of this asset entry.
	*
	* @param classNameId the class name ID of this asset entry
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_assetEntry.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this asset entry.
	*
	* @param classPK the class pk of this asset entry
	*/
	@Override
	public void setClassPK(long classPK) {
		_assetEntry.setClassPK(classPK);
	}

	/**
	* Sets the class type ID of this asset entry.
	*
	* @param classTypeId the class type ID of this asset entry
	*/
	@Override
	public void setClassTypeId(long classTypeId) {
		_assetEntry.setClassTypeId(classTypeId);
	}

	/**
	* Sets the class uuid of this asset entry.
	*
	* @param classUuid the class uuid of this asset entry
	*/
	@Override
	public void setClassUuid(String classUuid) {
		_assetEntry.setClassUuid(classUuid);
	}

	/**
	* Sets the company ID of this asset entry.
	*
	* @param companyId the company ID of this asset entry
	*/
	@Override
	public void setCompanyId(long companyId) {
		_assetEntry.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this asset entry.
	*
	* @param createDate the create date of this asset entry
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_assetEntry.setCreateDate(createDate);
	}

	/**
	* Sets the description of this asset entry.
	*
	* @param description the description of this asset entry
	*/
	@Override
	public void setDescription(String description) {
		_assetEntry.setDescription(description);
	}

	/**
	* Sets the localized description of this asset entry in the language.
	*
	* @param description the localized description of this asset entry
	* @param locale the locale of the language
	*/
	@Override
	public void setDescription(String description, java.util.Locale locale) {
		_assetEntry.setDescription(description, locale);
	}

	/**
	* Sets the localized description of this asset entry in the language, and sets the default locale.
	*
	* @param description the localized description of this asset entry
	* @param locale the locale of the language
	* @param defaultLocale the default locale
	*/
	@Override
	public void setDescription(String description, java.util.Locale locale,
		java.util.Locale defaultLocale) {
		_assetEntry.setDescription(description, locale, defaultLocale);
	}

	@Override
	public void setDescriptionCurrentLanguageId(String languageId) {
		_assetEntry.setDescriptionCurrentLanguageId(languageId);
	}

	/**
	* Sets the localized descriptions of this asset entry from the map of locales and localized descriptions.
	*
	* @param descriptionMap the locales and localized descriptions of this asset entry
	*/
	@Override
	public void setDescriptionMap(Map<java.util.Locale, String> descriptionMap) {
		_assetEntry.setDescriptionMap(descriptionMap);
	}

	/**
	* Sets the localized descriptions of this asset entry from the map of locales and localized descriptions, and sets the default locale.
	*
	* @param descriptionMap the locales and localized descriptions of this asset entry
	* @param defaultLocale the default locale
	*/
	@Override
	public void setDescriptionMap(
		Map<java.util.Locale, String> descriptionMap,
		java.util.Locale defaultLocale) {
		_assetEntry.setDescriptionMap(descriptionMap, defaultLocale);
	}

	/**
	* Sets the end date of this asset entry.
	*
	* @param endDate the end date of this asset entry
	*/
	@Override
	public void setEndDate(Date endDate) {
		_assetEntry.setEndDate(endDate);
	}

	/**
	* Sets the entry ID of this asset entry.
	*
	* @param entryId the entry ID of this asset entry
	*/
	@Override
	public void setEntryId(long entryId) {
		_assetEntry.setEntryId(entryId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_assetEntry.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_assetEntry.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_assetEntry.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the expiration date of this asset entry.
	*
	* @param expirationDate the expiration date of this asset entry
	*/
	@Override
	public void setExpirationDate(Date expirationDate) {
		_assetEntry.setExpirationDate(expirationDate);
	}

	/**
	* Sets the group ID of this asset entry.
	*
	* @param groupId the group ID of this asset entry
	*/
	@Override
	public void setGroupId(long groupId) {
		_assetEntry.setGroupId(groupId);
	}

	/**
	* Sets the height of this asset entry.
	*
	* @param height the height of this asset entry
	*/
	@Override
	public void setHeight(int height) {
		_assetEntry.setHeight(height);
	}

	/**
	* Sets the layout uuid of this asset entry.
	*
	* @param layoutUuid the layout uuid of this asset entry
	*/
	@Override
	public void setLayoutUuid(String layoutUuid) {
		_assetEntry.setLayoutUuid(layoutUuid);
	}

	/**
	* Sets whether this asset entry is listable.
	*
	* @param listable the listable of this asset entry
	*/
	@Override
	public void setListable(boolean listable) {
		_assetEntry.setListable(listable);
	}

	/**
	* Sets the mime type of this asset entry.
	*
	* @param mimeType the mime type of this asset entry
	*/
	@Override
	public void setMimeType(String mimeType) {
		_assetEntry.setMimeType(mimeType);
	}

	/**
	* Sets the modified date of this asset entry.
	*
	* @param modifiedDate the modified date of this asset entry
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_assetEntry.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_assetEntry.setNew(n);
	}

	/**
	* Sets the primary key of this asset entry.
	*
	* @param primaryKey the primary key of this asset entry
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_assetEntry.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_assetEntry.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the priority of this asset entry.
	*
	* @param priority the priority of this asset entry
	*/
	@Override
	public void setPriority(double priority) {
		_assetEntry.setPriority(priority);
	}

	/**
	* Sets the publish date of this asset entry.
	*
	* @param publishDate the publish date of this asset entry
	*/
	@Override
	public void setPublishDate(Date publishDate) {
		_assetEntry.setPublishDate(publishDate);
	}

	/**
	* Sets the start date of this asset entry.
	*
	* @param startDate the start date of this asset entry
	*/
	@Override
	public void setStartDate(Date startDate) {
		_assetEntry.setStartDate(startDate);
	}

	/**
	* Sets the summary of this asset entry.
	*
	* @param summary the summary of this asset entry
	*/
	@Override
	public void setSummary(String summary) {
		_assetEntry.setSummary(summary);
	}

	/**
	* Sets the localized summary of this asset entry in the language.
	*
	* @param summary the localized summary of this asset entry
	* @param locale the locale of the language
	*/
	@Override
	public void setSummary(String summary, java.util.Locale locale) {
		_assetEntry.setSummary(summary, locale);
	}

	/**
	* Sets the localized summary of this asset entry in the language, and sets the default locale.
	*
	* @param summary the localized summary of this asset entry
	* @param locale the locale of the language
	* @param defaultLocale the default locale
	*/
	@Override
	public void setSummary(String summary, java.util.Locale locale,
		java.util.Locale defaultLocale) {
		_assetEntry.setSummary(summary, locale, defaultLocale);
	}

	@Override
	public void setSummaryCurrentLanguageId(String languageId) {
		_assetEntry.setSummaryCurrentLanguageId(languageId);
	}

	/**
	* Sets the localized summaries of this asset entry from the map of locales and localized summaries.
	*
	* @param summaryMap the locales and localized summaries of this asset entry
	*/
	@Override
	public void setSummaryMap(Map<java.util.Locale, String> summaryMap) {
		_assetEntry.setSummaryMap(summaryMap);
	}

	/**
	* Sets the localized summaries of this asset entry from the map of locales and localized summaries, and sets the default locale.
	*
	* @param summaryMap the locales and localized summaries of this asset entry
	* @param defaultLocale the default locale
	*/
	@Override
	public void setSummaryMap(Map<java.util.Locale, String> summaryMap,
		java.util.Locale defaultLocale) {
		_assetEntry.setSummaryMap(summaryMap, defaultLocale);
	}

	/**
	* Sets the title of this asset entry.
	*
	* @param title the title of this asset entry
	*/
	@Override
	public void setTitle(String title) {
		_assetEntry.setTitle(title);
	}

	/**
	* Sets the localized title of this asset entry in the language.
	*
	* @param title the localized title of this asset entry
	* @param locale the locale of the language
	*/
	@Override
	public void setTitle(String title, java.util.Locale locale) {
		_assetEntry.setTitle(title, locale);
	}

	/**
	* Sets the localized title of this asset entry in the language, and sets the default locale.
	*
	* @param title the localized title of this asset entry
	* @param locale the locale of the language
	* @param defaultLocale the default locale
	*/
	@Override
	public void setTitle(String title, java.util.Locale locale,
		java.util.Locale defaultLocale) {
		_assetEntry.setTitle(title, locale, defaultLocale);
	}

	@Override
	public void setTitleCurrentLanguageId(String languageId) {
		_assetEntry.setTitleCurrentLanguageId(languageId);
	}

	/**
	* Sets the localized titles of this asset entry from the map of locales and localized titles.
	*
	* @param titleMap the locales and localized titles of this asset entry
	*/
	@Override
	public void setTitleMap(Map<java.util.Locale, String> titleMap) {
		_assetEntry.setTitleMap(titleMap);
	}

	/**
	* Sets the localized titles of this asset entry from the map of locales and localized titles, and sets the default locale.
	*
	* @param titleMap the locales and localized titles of this asset entry
	* @param defaultLocale the default locale
	*/
	@Override
	public void setTitleMap(Map<java.util.Locale, String> titleMap,
		java.util.Locale defaultLocale) {
		_assetEntry.setTitleMap(titleMap, defaultLocale);
	}

	/**
	* Sets the url of this asset entry.
	*
	* @param url the url of this asset entry
	*/
	@Override
	public void setUrl(String url) {
		_assetEntry.setUrl(url);
	}

	/**
	* Sets the user ID of this asset entry.
	*
	* @param userId the user ID of this asset entry
	*/
	@Override
	public void setUserId(long userId) {
		_assetEntry.setUserId(userId);
	}

	/**
	* Sets the user name of this asset entry.
	*
	* @param userName the user name of this asset entry
	*/
	@Override
	public void setUserName(String userName) {
		_assetEntry.setUserName(userName);
	}

	/**
	* Sets the user uuid of this asset entry.
	*
	* @param userUuid the user uuid of this asset entry
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_assetEntry.setUserUuid(userUuid);
	}

	/**
	* Sets the view count of this asset entry.
	*
	* @param viewCount the view count of this asset entry
	*/
	@Override
	public void setViewCount(int viewCount) {
		_assetEntry.setViewCount(viewCount);
	}

	/**
	* Sets whether this asset entry is visible.
	*
	* @param visible the visible of this asset entry
	*/
	@Override
	public void setVisible(boolean visible) {
		_assetEntry.setVisible(visible);
	}

	/**
	* Sets the width of this asset entry.
	*
	* @param width the width of this asset entry
	*/
	@Override
	public void setWidth(int width) {
		_assetEntry.setWidth(width);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<AssetEntry> toCacheModel() {
		return _assetEntry.toCacheModel();
	}

	@Override
	public AssetEntry toEscapedModel() {
		return new AssetEntryWrapper(_assetEntry.toEscapedModel());
	}

	@Override
	public String toString() {
		return _assetEntry.toString();
	}

	@Override
	public AssetEntry toUnescapedModel() {
		return new AssetEntryWrapper(_assetEntry.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _assetEntry.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AssetEntryWrapper)) {
			return false;
		}

		AssetEntryWrapper assetEntryWrapper = (AssetEntryWrapper)obj;

		if (Objects.equals(_assetEntry, assetEntryWrapper._assetEntry)) {
			return true;
		}

		return false;
	}

	@Override
	public AssetEntry getWrappedModel() {
		return _assetEntry;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _assetEntry.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _assetEntry.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_assetEntry.resetOriginalValues();
	}

	private final AssetEntry _assetEntry;
}