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

package com.liferay.mail.reader.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link Attachment}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Attachment
 * @generated
 */
@ProviderType
public class AttachmentWrapper implements Attachment, ModelWrapper<Attachment> {
	public AttachmentWrapper(Attachment attachment) {
		_attachment = attachment;
	}

	@Override
	public Class<?> getModelClass() {
		return Attachment.class;
	}

	@Override
	public String getModelClassName() {
		return Attachment.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<Attachment, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<Attachment, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<Attachment, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<Attachment, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<Attachment, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<Attachment, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<Attachment, Object>> getAttributeGetters() {
		return _attachment.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<Attachment, Object>> getAttributeSetters() {
		return _attachment.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new AttachmentWrapper((Attachment)_attachment.clone());
	}

	@Override
	public int compareTo(Attachment attachment) {
		return _attachment.compareTo(attachment);
	}

	/**
	* Returns the account ID of this attachment.
	*
	* @return the account ID of this attachment
	*/
	@Override
	public long getAccountId() {
		return _attachment.getAccountId();
	}

	/**
	* Returns the attachment ID of this attachment.
	*
	* @return the attachment ID of this attachment
	*/
	@Override
	public long getAttachmentId() {
		return _attachment.getAttachmentId();
	}

	/**
	* Returns the company ID of this attachment.
	*
	* @return the company ID of this attachment
	*/
	@Override
	public long getCompanyId() {
		return _attachment.getCompanyId();
	}

	/**
	* Returns the content path of this attachment.
	*
	* @return the content path of this attachment
	*/
	@Override
	public String getContentPath() {
		return _attachment.getContentPath();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _attachment.getExpandoBridge();
	}

	/**
	* Returns the file name of this attachment.
	*
	* @return the file name of this attachment
	*/
	@Override
	public String getFileName() {
		return _attachment.getFileName();
	}

	/**
	* Returns the folder ID of this attachment.
	*
	* @return the folder ID of this attachment
	*/
	@Override
	public long getFolderId() {
		return _attachment.getFolderId();
	}

	/**
	* Returns the message ID of this attachment.
	*
	* @return the message ID of this attachment
	*/
	@Override
	public long getMessageId() {
		return _attachment.getMessageId();
	}

	/**
	* Returns the primary key of this attachment.
	*
	* @return the primary key of this attachment
	*/
	@Override
	public long getPrimaryKey() {
		return _attachment.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _attachment.getPrimaryKeyObj();
	}

	/**
	* Returns the size of this attachment.
	*
	* @return the size of this attachment
	*/
	@Override
	public long getSize() {
		return _attachment.getSize();
	}

	/**
	* Returns the user ID of this attachment.
	*
	* @return the user ID of this attachment
	*/
	@Override
	public long getUserId() {
		return _attachment.getUserId();
	}

	/**
	* Returns the user uuid of this attachment.
	*
	* @return the user uuid of this attachment
	*/
	@Override
	public String getUserUuid() {
		return _attachment.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _attachment.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _attachment.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _attachment.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _attachment.isNew();
	}

	@Override
	public void persist() {
		_attachment.persist();
	}

	/**
	* Sets the account ID of this attachment.
	*
	* @param accountId the account ID of this attachment
	*/
	@Override
	public void setAccountId(long accountId) {
		_attachment.setAccountId(accountId);
	}

	/**
	* Sets the attachment ID of this attachment.
	*
	* @param attachmentId the attachment ID of this attachment
	*/
	@Override
	public void setAttachmentId(long attachmentId) {
		_attachment.setAttachmentId(attachmentId);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_attachment.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this attachment.
	*
	* @param companyId the company ID of this attachment
	*/
	@Override
	public void setCompanyId(long companyId) {
		_attachment.setCompanyId(companyId);
	}

	/**
	* Sets the content path of this attachment.
	*
	* @param contentPath the content path of this attachment
	*/
	@Override
	public void setContentPath(String contentPath) {
		_attachment.setContentPath(contentPath);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_attachment.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_attachment.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_attachment.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the file name of this attachment.
	*
	* @param fileName the file name of this attachment
	*/
	@Override
	public void setFileName(String fileName) {
		_attachment.setFileName(fileName);
	}

	/**
	* Sets the folder ID of this attachment.
	*
	* @param folderId the folder ID of this attachment
	*/
	@Override
	public void setFolderId(long folderId) {
		_attachment.setFolderId(folderId);
	}

	/**
	* Sets the message ID of this attachment.
	*
	* @param messageId the message ID of this attachment
	*/
	@Override
	public void setMessageId(long messageId) {
		_attachment.setMessageId(messageId);
	}

	@Override
	public void setNew(boolean n) {
		_attachment.setNew(n);
	}

	/**
	* Sets the primary key of this attachment.
	*
	* @param primaryKey the primary key of this attachment
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_attachment.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_attachment.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the size of this attachment.
	*
	* @param size the size of this attachment
	*/
	@Override
	public void setSize(long size) {
		_attachment.setSize(size);
	}

	/**
	* Sets the user ID of this attachment.
	*
	* @param userId the user ID of this attachment
	*/
	@Override
	public void setUserId(long userId) {
		_attachment.setUserId(userId);
	}

	/**
	* Sets the user uuid of this attachment.
	*
	* @param userUuid the user uuid of this attachment
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_attachment.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<Attachment> toCacheModel() {
		return _attachment.toCacheModel();
	}

	@Override
	public Attachment toEscapedModel() {
		return new AttachmentWrapper(_attachment.toEscapedModel());
	}

	@Override
	public String toString() {
		return _attachment.toString();
	}

	@Override
	public Attachment toUnescapedModel() {
		return new AttachmentWrapper(_attachment.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _attachment.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AttachmentWrapper)) {
			return false;
		}

		AttachmentWrapper attachmentWrapper = (AttachmentWrapper)obj;

		if (Objects.equals(_attachment, attachmentWrapper._attachment)) {
			return true;
		}

		return false;
	}

	@Override
	public Attachment getWrappedModel() {
		return _attachment;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _attachment.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _attachment.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_attachment.resetOriginalValues();
	}

	private final Attachment _attachment;
}