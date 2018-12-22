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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link Message}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Message
 * @generated
 */
@ProviderType
public class MessageWrapper implements Message, ModelWrapper<Message> {
	public MessageWrapper(Message message) {
		_message = message;
	}

	@Override
	public Class<?> getModelClass() {
		return Message.class;
	}

	@Override
	public String getModelClassName() {
		return Message.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<Message, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<Message, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<Message, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<Message, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<Message, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<Message, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<Message, Object>> getAttributeGetters() {
		return _message.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<Message, Object>> getAttributeSetters() {
		return _message.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new MessageWrapper((Message)_message.clone());
	}

	@Override
	public int compareTo(Message message) {
		return _message.compareTo(message);
	}

	/**
	* Returns the account ID of this message.
	*
	* @return the account ID of this message
	*/
	@Override
	public long getAccountId() {
		return _message.getAccountId();
	}

	/**
	* Returns the bcc of this message.
	*
	* @return the bcc of this message
	*/
	@Override
	public String getBcc() {
		return _message.getBcc();
	}

	/**
	* Returns the body of this message.
	*
	* @return the body of this message
	*/
	@Override
	public String getBody() {
		return _message.getBody();
	}

	/**
	* Returns the cc of this message.
	*
	* @return the cc of this message
	*/
	@Override
	public String getCc() {
		return _message.getCc();
	}

	/**
	* Returns the company ID of this message.
	*
	* @return the company ID of this message
	*/
	@Override
	public long getCompanyId() {
		return _message.getCompanyId();
	}

	/**
	* Returns the content type of this message.
	*
	* @return the content type of this message
	*/
	@Override
	public String getContentType() {
		return _message.getContentType();
	}

	/**
	* Returns the create date of this message.
	*
	* @return the create date of this message
	*/
	@Override
	public Date getCreateDate() {
		return _message.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _message.getExpandoBridge();
	}

	/**
	* Returns the flags of this message.
	*
	* @return the flags of this message
	*/
	@Override
	public String getFlags() {
		return _message.getFlags();
	}

	/**
	* Returns the folder ID of this message.
	*
	* @return the folder ID of this message
	*/
	@Override
	public long getFolderId() {
		return _message.getFolderId();
	}

	@Override
	public long getGroupId()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _message.getGroupId();
	}

	/**
	* Returns the message ID of this message.
	*
	* @return the message ID of this message
	*/
	@Override
	public long getMessageId() {
		return _message.getMessageId();
	}

	/**
	* Returns the modified date of this message.
	*
	* @return the modified date of this message
	*/
	@Override
	public Date getModifiedDate() {
		return _message.getModifiedDate();
	}

	/**
	* Returns the preview of this message.
	*
	* @return the preview of this message
	*/
	@Override
	public String getPreview() {
		return _message.getPreview();
	}

	/**
	* Returns the primary key of this message.
	*
	* @return the primary key of this message
	*/
	@Override
	public long getPrimaryKey() {
		return _message.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _message.getPrimaryKeyObj();
	}

	/**
	* Returns the remote message ID of this message.
	*
	* @return the remote message ID of this message
	*/
	@Override
	public long getRemoteMessageId() {
		return _message.getRemoteMessageId();
	}

	/**
	* Returns the sender of this message.
	*
	* @return the sender of this message
	*/
	@Override
	public String getSender() {
		return _message.getSender();
	}

	/**
	* Returns the sent date of this message.
	*
	* @return the sent date of this message
	*/
	@Override
	public Date getSentDate() {
		return _message.getSentDate();
	}

	/**
	* Returns the size of this message.
	*
	* @return the size of this message
	*/
	@Override
	public long getSize() {
		return _message.getSize();
	}

	/**
	* Returns the subject of this message.
	*
	* @return the subject of this message
	*/
	@Override
	public String getSubject() {
		return _message.getSubject();
	}

	/**
	* Returns the to of this message.
	*
	* @return the to of this message
	*/
	@Override
	public String getTo() {
		return _message.getTo();
	}

	/**
	* Returns the user ID of this message.
	*
	* @return the user ID of this message
	*/
	@Override
	public long getUserId() {
		return _message.getUserId();
	}

	/**
	* Returns the user name of this message.
	*
	* @return the user name of this message
	*/
	@Override
	public String getUserName() {
		return _message.getUserName();
	}

	/**
	* Returns the user uuid of this message.
	*
	* @return the user uuid of this message
	*/
	@Override
	public String getUserUuid() {
		return _message.getUserUuid();
	}

	@Override
	public boolean hasAttachments() {
		return _message.hasAttachments();
	}

	@Override
	public boolean hasFlag(int flag) {
		return _message.hasFlag(flag);
	}

	@Override
	public int hashCode() {
		return _message.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _message.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _message.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _message.isNew();
	}

	@Override
	public void persist() {
		_message.persist();
	}

	/**
	* Sets the account ID of this message.
	*
	* @param accountId the account ID of this message
	*/
	@Override
	public void setAccountId(long accountId) {
		_message.setAccountId(accountId);
	}

	/**
	* Sets the bcc of this message.
	*
	* @param bcc the bcc of this message
	*/
	@Override
	public void setBcc(String bcc) {
		_message.setBcc(bcc);
	}

	/**
	* Sets the body of this message.
	*
	* @param body the body of this message
	*/
	@Override
	public void setBody(String body) {
		_message.setBody(body);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_message.setCachedModel(cachedModel);
	}

	/**
	* Sets the cc of this message.
	*
	* @param cc the cc of this message
	*/
	@Override
	public void setCc(String cc) {
		_message.setCc(cc);
	}

	/**
	* Sets the company ID of this message.
	*
	* @param companyId the company ID of this message
	*/
	@Override
	public void setCompanyId(long companyId) {
		_message.setCompanyId(companyId);
	}

	/**
	* Sets the content type of this message.
	*
	* @param contentType the content type of this message
	*/
	@Override
	public void setContentType(String contentType) {
		_message.setContentType(contentType);
	}

	/**
	* Sets the create date of this message.
	*
	* @param createDate the create date of this message
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_message.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_message.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_message.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_message.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the flags of this message.
	*
	* @param flags the flags of this message
	*/
	@Override
	public void setFlags(String flags) {
		_message.setFlags(flags);
	}

	/**
	* Sets the folder ID of this message.
	*
	* @param folderId the folder ID of this message
	*/
	@Override
	public void setFolderId(long folderId) {
		_message.setFolderId(folderId);
	}

	/**
	* Sets the message ID of this message.
	*
	* @param messageId the message ID of this message
	*/
	@Override
	public void setMessageId(long messageId) {
		_message.setMessageId(messageId);
	}

	/**
	* Sets the modified date of this message.
	*
	* @param modifiedDate the modified date of this message
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_message.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_message.setNew(n);
	}

	/**
	* Sets the preview of this message.
	*
	* @param preview the preview of this message
	*/
	@Override
	public void setPreview(String preview) {
		_message.setPreview(preview);
	}

	/**
	* Sets the primary key of this message.
	*
	* @param primaryKey the primary key of this message
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_message.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_message.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the remote message ID of this message.
	*
	* @param remoteMessageId the remote message ID of this message
	*/
	@Override
	public void setRemoteMessageId(long remoteMessageId) {
		_message.setRemoteMessageId(remoteMessageId);
	}

	/**
	* Sets the sender of this message.
	*
	* @param sender the sender of this message
	*/
	@Override
	public void setSender(String sender) {
		_message.setSender(sender);
	}

	/**
	* Sets the sent date of this message.
	*
	* @param sentDate the sent date of this message
	*/
	@Override
	public void setSentDate(Date sentDate) {
		_message.setSentDate(sentDate);
	}

	/**
	* Sets the size of this message.
	*
	* @param size the size of this message
	*/
	@Override
	public void setSize(long size) {
		_message.setSize(size);
	}

	/**
	* Sets the subject of this message.
	*
	* @param subject the subject of this message
	*/
	@Override
	public void setSubject(String subject) {
		_message.setSubject(subject);
	}

	/**
	* Sets the to of this message.
	*
	* @param to the to of this message
	*/
	@Override
	public void setTo(String to) {
		_message.setTo(to);
	}

	/**
	* Sets the user ID of this message.
	*
	* @param userId the user ID of this message
	*/
	@Override
	public void setUserId(long userId) {
		_message.setUserId(userId);
	}

	/**
	* Sets the user name of this message.
	*
	* @param userName the user name of this message
	*/
	@Override
	public void setUserName(String userName) {
		_message.setUserName(userName);
	}

	/**
	* Sets the user uuid of this message.
	*
	* @param userUuid the user uuid of this message
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_message.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<Message> toCacheModel() {
		return _message.toCacheModel();
	}

	@Override
	public Message toEscapedModel() {
		return new MessageWrapper(_message.toEscapedModel());
	}

	@Override
	public String toString() {
		return _message.toString();
	}

	@Override
	public Message toUnescapedModel() {
		return new MessageWrapper(_message.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _message.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof MessageWrapper)) {
			return false;
		}

		MessageWrapper messageWrapper = (MessageWrapper)obj;

		if (Objects.equals(_message, messageWrapper._message)) {
			return true;
		}

		return false;
	}

	@Override
	public Message getWrappedModel() {
		return _message;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _message.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _message.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_message.resetOriginalValues();
	}

	private final Message _message;
}