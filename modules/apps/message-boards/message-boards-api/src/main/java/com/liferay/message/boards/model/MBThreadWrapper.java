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

package com.liferay.message.boards.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.exportimport.kernel.lar.StagedModelType;

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
 * This class is a wrapper for {@link MBThread}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MBThread
 * @generated
 */
@ProviderType
public class MBThreadWrapper implements MBThread, ModelWrapper<MBThread> {
	public MBThreadWrapper(MBThread mbThread) {
		_mbThread = mbThread;
	}

	@Override
	public Class<?> getModelClass() {
		return MBThread.class;
	}

	@Override
	public String getModelClassName() {
		return MBThread.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<MBThread, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<MBThread, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<MBThread, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<MBThread, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<MBThread, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<MBThread, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<MBThread, Object>> getAttributeGetters() {
		return _mbThread.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<MBThread, Object>> getAttributeSetters() {
		return _mbThread.getAttributeSetters();
	}

	@Override
	public com.liferay.portal.kernel.repository.model.Folder addAttachmentsFolder()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _mbThread.addAttachmentsFolder();
	}

	@Override
	public Object clone() {
		return new MBThreadWrapper((MBThread)_mbThread.clone());
	}

	@Override
	public int compareTo(MBThread mbThread) {
		return _mbThread.compareTo(mbThread);
	}

	@Override
	public long getAttachmentsFolderId() {
		return _mbThread.getAttachmentsFolderId();
	}

	@Override
	public MBCategory getCategory()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _mbThread.getCategory();
	}

	/**
	* Returns the category ID of this message boards thread.
	*
	* @return the category ID of this message boards thread
	*/
	@Override
	public long getCategoryId() {
		return _mbThread.getCategoryId();
	}

	/**
	* Returns the company ID of this message boards thread.
	*
	* @return the company ID of this message boards thread
	*/
	@Override
	public long getCompanyId() {
		return _mbThread.getCompanyId();
	}

	/**
	* Returns the container model ID of this message boards thread.
	*
	* @return the container model ID of this message boards thread
	*/
	@Override
	public long getContainerModelId() {
		return _mbThread.getContainerModelId();
	}

	/**
	* Returns the container name of this message boards thread.
	*
	* @return the container name of this message boards thread
	*/
	@Override
	public String getContainerModelName() {
		return _mbThread.getContainerModelName();
	}

	/**
	* Returns the create date of this message boards thread.
	*
	* @return the create date of this message boards thread
	*/
	@Override
	public Date getCreateDate() {
		return _mbThread.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _mbThread.getExpandoBridge();
	}

	/**
	* Returns the group ID of this message boards thread.
	*
	* @return the group ID of this message boards thread
	*/
	@Override
	public long getGroupId() {
		return _mbThread.getGroupId();
	}

	/**
	* Returns the last post by user ID of this message boards thread.
	*
	* @return the last post by user ID of this message boards thread
	*/
	@Override
	public long getLastPostByUserId() {
		return _mbThread.getLastPostByUserId();
	}

	/**
	* Returns the last post by user uuid of this message boards thread.
	*
	* @return the last post by user uuid of this message boards thread
	*/
	@Override
	public String getLastPostByUserUuid() {
		return _mbThread.getLastPostByUserUuid();
	}

	/**
	* Returns the last post date of this message boards thread.
	*
	* @return the last post date of this message boards thread
	*/
	@Override
	public Date getLastPostDate() {
		return _mbThread.getLastPostDate();
	}

	/**
	* Returns the last publish date of this message boards thread.
	*
	* @return the last publish date of this message boards thread
	*/
	@Override
	public Date getLastPublishDate() {
		return _mbThread.getLastPublishDate();
	}

	@Override
	public com.liferay.portal.kernel.lock.Lock getLock() {
		return _mbThread.getLock();
	}

	/**
	* Returns the message count of this message boards thread.
	*
	* @return the message count of this message boards thread
	*/
	@Override
	public int getMessageCount() {
		return _mbThread.getMessageCount();
	}

	/**
	* Returns the modified date of this message boards thread.
	*
	* @return the modified date of this message boards thread
	*/
	@Override
	public Date getModifiedDate() {
		return _mbThread.getModifiedDate();
	}

	/**
	* Returns the parent container model ID of this message boards thread.
	*
	* @return the parent container model ID of this message boards thread
	*/
	@Override
	public long getParentContainerModelId() {
		return _mbThread.getParentContainerModelId();
	}

	@Override
	public long[] getParticipantUserIds() {
		return _mbThread.getParticipantUserIds();
	}

	/**
	* Returns the primary key of this message boards thread.
	*
	* @return the primary key of this message boards thread
	*/
	@Override
	public long getPrimaryKey() {
		return _mbThread.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _mbThread.getPrimaryKeyObj();
	}

	/**
	* Returns the priority of this message boards thread.
	*
	* @return the priority of this message boards thread
	*/
	@Override
	public double getPriority() {
		return _mbThread.getPriority();
	}

	/**
	* Returns the question of this message boards thread.
	*
	* @return the question of this message boards thread
	*/
	@Override
	public boolean getQuestion() {
		return _mbThread.getQuestion();
	}

	/**
	* Returns the root message ID of this message boards thread.
	*
	* @return the root message ID of this message boards thread
	*/
	@Override
	public long getRootMessageId() {
		return _mbThread.getRootMessageId();
	}

	/**
	* Returns the root message user ID of this message boards thread.
	*
	* @return the root message user ID of this message boards thread
	*/
	@Override
	public long getRootMessageUserId() {
		return _mbThread.getRootMessageUserId();
	}

	/**
	* Returns the root message user uuid of this message boards thread.
	*
	* @return the root message user uuid of this message boards thread
	*/
	@Override
	public String getRootMessageUserUuid() {
		return _mbThread.getRootMessageUserUuid();
	}

	/**
	* Returns the status of this message boards thread.
	*
	* @return the status of this message boards thread
	*/
	@Override
	public int getStatus() {
		return _mbThread.getStatus();
	}

	/**
	* Returns the status by user ID of this message boards thread.
	*
	* @return the status by user ID of this message boards thread
	*/
	@Override
	public long getStatusByUserId() {
		return _mbThread.getStatusByUserId();
	}

	/**
	* Returns the status by user name of this message boards thread.
	*
	* @return the status by user name of this message boards thread
	*/
	@Override
	public String getStatusByUserName() {
		return _mbThread.getStatusByUserName();
	}

	/**
	* Returns the status by user uuid of this message boards thread.
	*
	* @return the status by user uuid of this message boards thread
	*/
	@Override
	public String getStatusByUserUuid() {
		return _mbThread.getStatusByUserUuid();
	}

	/**
	* Returns the status date of this message boards thread.
	*
	* @return the status date of this message boards thread
	*/
	@Override
	public Date getStatusDate() {
		return _mbThread.getStatusDate();
	}

	/**
	* Returns the thread ID of this message boards thread.
	*
	* @return the thread ID of this message boards thread
	*/
	@Override
	public long getThreadId() {
		return _mbThread.getThreadId();
	}

	/**
	* Returns the title of this message boards thread.
	*
	* @return the title of this message boards thread
	*/
	@Override
	public String getTitle() {
		return _mbThread.getTitle();
	}

	/**
	* Returns the trash entry created when this message boards thread was moved to the Recycle Bin. The trash entry may belong to one of the ancestors of this message boards thread.
	*
	* @return the trash entry created when this message boards thread was moved to the Recycle Bin
	*/
	@Override
	public com.liferay.trash.kernel.model.TrashEntry getTrashEntry()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _mbThread.getTrashEntry();
	}

	/**
	* Returns the class primary key of the trash entry for this message boards thread.
	*
	* @return the class primary key of the trash entry for this message boards thread
	*/
	@Override
	public long getTrashEntryClassPK() {
		return _mbThread.getTrashEntryClassPK();
	}

	/**
	* Returns the trash handler for this message boards thread.
	*
	* @return the trash handler for this message boards thread
	* @deprecated As of Judson (7.1.x), with no direct replacement
	*/
	@Deprecated
	@Override
	public com.liferay.portal.kernel.trash.TrashHandler getTrashHandler() {
		return _mbThread.getTrashHandler();
	}

	/**
	* Returns the user ID of this message boards thread.
	*
	* @return the user ID of this message boards thread
	*/
	@Override
	public long getUserId() {
		return _mbThread.getUserId();
	}

	/**
	* Returns the user name of this message boards thread.
	*
	* @return the user name of this message boards thread
	*/
	@Override
	public String getUserName() {
		return _mbThread.getUserName();
	}

	/**
	* Returns the user uuid of this message boards thread.
	*
	* @return the user uuid of this message boards thread
	*/
	@Override
	public String getUserUuid() {
		return _mbThread.getUserUuid();
	}

	/**
	* Returns the uuid of this message boards thread.
	*
	* @return the uuid of this message boards thread
	*/
	@Override
	public String getUuid() {
		return _mbThread.getUuid();
	}

	/**
	* Returns the view count of this message boards thread.
	*
	* @return the view count of this message boards thread
	*/
	@Override
	public int getViewCount() {
		return _mbThread.getViewCount();
	}

	@Override
	public int hashCode() {
		return _mbThread.hashCode();
	}

	@Override
	public boolean hasLock(long userId) {
		return _mbThread.hasLock(userId);
	}

	/**
	* Returns <code>true</code> if this message boards thread is approved.
	*
	* @return <code>true</code> if this message boards thread is approved; <code>false</code> otherwise
	*/
	@Override
	public boolean isApproved() {
		return _mbThread.isApproved();
	}

	@Override
	public boolean isCachedModel() {
		return _mbThread.isCachedModel();
	}

	/**
	* Returns <code>true</code> if this message boards thread is denied.
	*
	* @return <code>true</code> if this message boards thread is denied; <code>false</code> otherwise
	*/
	@Override
	public boolean isDenied() {
		return _mbThread.isDenied();
	}

	/**
	* Returns <code>true</code> if this message boards thread is a draft.
	*
	* @return <code>true</code> if this message boards thread is a draft; <code>false</code> otherwise
	*/
	@Override
	public boolean isDraft() {
		return _mbThread.isDraft();
	}

	@Override
	public boolean isEscapedModel() {
		return _mbThread.isEscapedModel();
	}

	/**
	* Returns <code>true</code> if this message boards thread is expired.
	*
	* @return <code>true</code> if this message boards thread is expired; <code>false</code> otherwise
	*/
	@Override
	public boolean isExpired() {
		return _mbThread.isExpired();
	}

	/**
	* Returns <code>true</code> if this message boards thread is inactive.
	*
	* @return <code>true</code> if this message boards thread is inactive; <code>false</code> otherwise
	*/
	@Override
	public boolean isInactive() {
		return _mbThread.isInactive();
	}

	/**
	* Returns <code>true</code> if this message boards thread is incomplete.
	*
	* @return <code>true</code> if this message boards thread is incomplete; <code>false</code> otherwise
	*/
	@Override
	public boolean isIncomplete() {
		return _mbThread.isIncomplete();
	}

	/**
	* Returns <code>true</code> if this message boards thread is in the Recycle Bin.
	*
	* @return <code>true</code> if this message boards thread is in the Recycle Bin; <code>false</code> otherwise
	*/
	@Override
	public boolean isInTrash() {
		return _mbThread.isInTrash();
	}

	/**
	* Returns <code>true</code> if the parent of this message boards thread is in the Recycle Bin.
	*
	* @return <code>true</code> if the parent of this message boards thread is in the Recycle Bin; <code>false</code> otherwise
	*/
	@Override
	public boolean isInTrashContainer() {
		return _mbThread.isInTrashContainer();
	}

	@Override
	public boolean isInTrashExplicitly() {
		return _mbThread.isInTrashExplicitly();
	}

	@Override
	public boolean isInTrashImplicitly() {
		return _mbThread.isInTrashImplicitly();
	}

	@Override
	public boolean isLocked() {
		return _mbThread.isLocked();
	}

	@Override
	public boolean isNew() {
		return _mbThread.isNew();
	}

	/**
	* Returns <code>true</code> if this message boards thread is pending.
	*
	* @return <code>true</code> if this message boards thread is pending; <code>false</code> otherwise
	*/
	@Override
	public boolean isPending() {
		return _mbThread.isPending();
	}

	/**
	* Returns <code>true</code> if this message boards thread is question.
	*
	* @return <code>true</code> if this message boards thread is question; <code>false</code> otherwise
	*/
	@Override
	public boolean isQuestion() {
		return _mbThread.isQuestion();
	}

	/**
	* Returns <code>true</code> if this message boards thread is scheduled.
	*
	* @return <code>true</code> if this message boards thread is scheduled; <code>false</code> otherwise
	*/
	@Override
	public boolean isScheduled() {
		return _mbThread.isScheduled();
	}

	@Override
	public void persist() {
		_mbThread.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_mbThread.setCachedModel(cachedModel);
	}

	/**
	* Sets the category ID of this message boards thread.
	*
	* @param categoryId the category ID of this message boards thread
	*/
	@Override
	public void setCategoryId(long categoryId) {
		_mbThread.setCategoryId(categoryId);
	}

	/**
	* Sets the company ID of this message boards thread.
	*
	* @param companyId the company ID of this message boards thread
	*/
	@Override
	public void setCompanyId(long companyId) {
		_mbThread.setCompanyId(companyId);
	}

	/**
	* Sets the container model ID of this message boards thread.
	*
	* @param containerModelId the container model ID of this message boards thread
	*/
	@Override
	public void setContainerModelId(long containerModelId) {
		_mbThread.setContainerModelId(containerModelId);
	}

	/**
	* Sets the create date of this message boards thread.
	*
	* @param createDate the create date of this message boards thread
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_mbThread.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_mbThread.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_mbThread.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_mbThread.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this message boards thread.
	*
	* @param groupId the group ID of this message boards thread
	*/
	@Override
	public void setGroupId(long groupId) {
		_mbThread.setGroupId(groupId);
	}

	/**
	* Sets the last post by user ID of this message boards thread.
	*
	* @param lastPostByUserId the last post by user ID of this message boards thread
	*/
	@Override
	public void setLastPostByUserId(long lastPostByUserId) {
		_mbThread.setLastPostByUserId(lastPostByUserId);
	}

	/**
	* Sets the last post by user uuid of this message boards thread.
	*
	* @param lastPostByUserUuid the last post by user uuid of this message boards thread
	*/
	@Override
	public void setLastPostByUserUuid(String lastPostByUserUuid) {
		_mbThread.setLastPostByUserUuid(lastPostByUserUuid);
	}

	/**
	* Sets the last post date of this message boards thread.
	*
	* @param lastPostDate the last post date of this message boards thread
	*/
	@Override
	public void setLastPostDate(Date lastPostDate) {
		_mbThread.setLastPostDate(lastPostDate);
	}

	/**
	* Sets the last publish date of this message boards thread.
	*
	* @param lastPublishDate the last publish date of this message boards thread
	*/
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_mbThread.setLastPublishDate(lastPublishDate);
	}

	/**
	* Sets the message count of this message boards thread.
	*
	* @param messageCount the message count of this message boards thread
	*/
	@Override
	public void setMessageCount(int messageCount) {
		_mbThread.setMessageCount(messageCount);
	}

	/**
	* Sets the modified date of this message boards thread.
	*
	* @param modifiedDate the modified date of this message boards thread
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_mbThread.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_mbThread.setNew(n);
	}

	/**
	* Sets the parent container model ID of this message boards thread.
	*
	* @param parentContainerModelId the parent container model ID of this message boards thread
	*/
	@Override
	public void setParentContainerModelId(long parentContainerModelId) {
		_mbThread.setParentContainerModelId(parentContainerModelId);
	}

	/**
	* Sets the primary key of this message boards thread.
	*
	* @param primaryKey the primary key of this message boards thread
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_mbThread.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_mbThread.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the priority of this message boards thread.
	*
	* @param priority the priority of this message boards thread
	*/
	@Override
	public void setPriority(double priority) {
		_mbThread.setPriority(priority);
	}

	/**
	* Sets whether this message boards thread is question.
	*
	* @param question the question of this message boards thread
	*/
	@Override
	public void setQuestion(boolean question) {
		_mbThread.setQuestion(question);
	}

	/**
	* Sets the root message ID of this message boards thread.
	*
	* @param rootMessageId the root message ID of this message boards thread
	*/
	@Override
	public void setRootMessageId(long rootMessageId) {
		_mbThread.setRootMessageId(rootMessageId);
	}

	/**
	* Sets the root message user ID of this message boards thread.
	*
	* @param rootMessageUserId the root message user ID of this message boards thread
	*/
	@Override
	public void setRootMessageUserId(long rootMessageUserId) {
		_mbThread.setRootMessageUserId(rootMessageUserId);
	}

	/**
	* Sets the root message user uuid of this message boards thread.
	*
	* @param rootMessageUserUuid the root message user uuid of this message boards thread
	*/
	@Override
	public void setRootMessageUserUuid(String rootMessageUserUuid) {
		_mbThread.setRootMessageUserUuid(rootMessageUserUuid);
	}

	/**
	* Sets the status of this message boards thread.
	*
	* @param status the status of this message boards thread
	*/
	@Override
	public void setStatus(int status) {
		_mbThread.setStatus(status);
	}

	/**
	* Sets the status by user ID of this message boards thread.
	*
	* @param statusByUserId the status by user ID of this message boards thread
	*/
	@Override
	public void setStatusByUserId(long statusByUserId) {
		_mbThread.setStatusByUserId(statusByUserId);
	}

	/**
	* Sets the status by user name of this message boards thread.
	*
	* @param statusByUserName the status by user name of this message boards thread
	*/
	@Override
	public void setStatusByUserName(String statusByUserName) {
		_mbThread.setStatusByUserName(statusByUserName);
	}

	/**
	* Sets the status by user uuid of this message boards thread.
	*
	* @param statusByUserUuid the status by user uuid of this message boards thread
	*/
	@Override
	public void setStatusByUserUuid(String statusByUserUuid) {
		_mbThread.setStatusByUserUuid(statusByUserUuid);
	}

	/**
	* Sets the status date of this message boards thread.
	*
	* @param statusDate the status date of this message boards thread
	*/
	@Override
	public void setStatusDate(Date statusDate) {
		_mbThread.setStatusDate(statusDate);
	}

	/**
	* Sets the thread ID of this message boards thread.
	*
	* @param threadId the thread ID of this message boards thread
	*/
	@Override
	public void setThreadId(long threadId) {
		_mbThread.setThreadId(threadId);
	}

	/**
	* Sets the title of this message boards thread.
	*
	* @param title the title of this message boards thread
	*/
	@Override
	public void setTitle(String title) {
		_mbThread.setTitle(title);
	}

	/**
	* Sets the user ID of this message boards thread.
	*
	* @param userId the user ID of this message boards thread
	*/
	@Override
	public void setUserId(long userId) {
		_mbThread.setUserId(userId);
	}

	/**
	* Sets the user name of this message boards thread.
	*
	* @param userName the user name of this message boards thread
	*/
	@Override
	public void setUserName(String userName) {
		_mbThread.setUserName(userName);
	}

	/**
	* Sets the user uuid of this message boards thread.
	*
	* @param userUuid the user uuid of this message boards thread
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_mbThread.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this message boards thread.
	*
	* @param uuid the uuid of this message boards thread
	*/
	@Override
	public void setUuid(String uuid) {
		_mbThread.setUuid(uuid);
	}

	/**
	* Sets the view count of this message boards thread.
	*
	* @param viewCount the view count of this message boards thread
	*/
	@Override
	public void setViewCount(int viewCount) {
		_mbThread.setViewCount(viewCount);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<MBThread> toCacheModel() {
		return _mbThread.toCacheModel();
	}

	@Override
	public MBThread toEscapedModel() {
		return new MBThreadWrapper(_mbThread.toEscapedModel());
	}

	@Override
	public String toString() {
		return _mbThread.toString();
	}

	@Override
	public MBThread toUnescapedModel() {
		return new MBThreadWrapper(_mbThread.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _mbThread.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof MBThreadWrapper)) {
			return false;
		}

		MBThreadWrapper mbThreadWrapper = (MBThreadWrapper)obj;

		if (Objects.equals(_mbThread, mbThreadWrapper._mbThread)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _mbThread.getStagedModelType();
	}

	@Override
	public MBThread getWrappedModel() {
		return _mbThread;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _mbThread.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _mbThread.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_mbThread.resetOriginalValues();
	}

	private final MBThread _mbThread;
}