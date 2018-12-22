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

package com.liferay.portal.background.task.model;

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
 * This class is a wrapper for {@link BackgroundTask}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see BackgroundTask
 * @generated
 */
@ProviderType
public class BackgroundTaskWrapper implements BackgroundTask,
	ModelWrapper<BackgroundTask> {
	public BackgroundTaskWrapper(BackgroundTask backgroundTask) {
		_backgroundTask = backgroundTask;
	}

	@Override
	public Class<?> getModelClass() {
		return BackgroundTask.class;
	}

	@Override
	public String getModelClassName() {
		return BackgroundTask.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<BackgroundTask, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<BackgroundTask, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<BackgroundTask, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<BackgroundTask, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<BackgroundTask, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<BackgroundTask, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<BackgroundTask, Object>> getAttributeGetters() {
		return _backgroundTask.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<BackgroundTask, Object>> getAttributeSetters() {
		return _backgroundTask.getAttributeSetters();
	}

	@Override
	public void addAttachment(long userId, String fileName, java.io.File file)
		throws com.liferay.portal.kernel.exception.PortalException {
		_backgroundTask.addAttachment(userId, fileName, file);
	}

	@Override
	public void addAttachment(long userId, String fileName,
		java.io.InputStream inputStream)
		throws com.liferay.portal.kernel.exception.PortalException {
		_backgroundTask.addAttachment(userId, fileName, inputStream);
	}

	@Override
	public com.liferay.portal.kernel.repository.model.Folder addAttachmentsFolder()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _backgroundTask.addAttachmentsFolder();
	}

	@Override
	public Object clone() {
		return new BackgroundTaskWrapper((BackgroundTask)_backgroundTask.clone());
	}

	@Override
	public int compareTo(BackgroundTask backgroundTask) {
		return _backgroundTask.compareTo(backgroundTask);
	}

	@Override
	public java.util.List<com.liferay.portal.kernel.repository.model.FileEntry> getAttachmentsFileEntries()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _backgroundTask.getAttachmentsFileEntries();
	}

	@Override
	public java.util.List<com.liferay.portal.kernel.repository.model.FileEntry> getAttachmentsFileEntries(
		int start, int end)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _backgroundTask.getAttachmentsFileEntries(start, end);
	}

	@Override
	public int getAttachmentsFileEntriesCount()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _backgroundTask.getAttachmentsFileEntriesCount();
	}

	@Override
	public long getAttachmentsFolderId() {
		return _backgroundTask.getAttachmentsFolderId();
	}

	/**
	* Returns the background task ID of this background task.
	*
	* @return the background task ID of this background task
	*/
	@Override
	public long getBackgroundTaskId() {
		return _backgroundTask.getBackgroundTaskId();
	}

	/**
	* Returns the company ID of this background task.
	*
	* @return the company ID of this background task
	*/
	@Override
	public long getCompanyId() {
		return _backgroundTask.getCompanyId();
	}

	/**
	* Returns the completed of this background task.
	*
	* @return the completed of this background task
	*/
	@Override
	public boolean getCompleted() {
		return _backgroundTask.getCompleted();
	}

	/**
	* Returns the completion date of this background task.
	*
	* @return the completion date of this background task
	*/
	@Override
	public Date getCompletionDate() {
		return _backgroundTask.getCompletionDate();
	}

	/**
	* Returns the create date of this background task.
	*
	* @return the create date of this background task
	*/
	@Override
	public Date getCreateDate() {
		return _backgroundTask.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _backgroundTask.getExpandoBridge();
	}

	/**
	* Returns the group ID of this background task.
	*
	* @return the group ID of this background task
	*/
	@Override
	public long getGroupId() {
		return _backgroundTask.getGroupId();
	}

	/**
	* Returns the modified date of this background task.
	*
	* @return the modified date of this background task
	*/
	@Override
	public Date getModifiedDate() {
		return _backgroundTask.getModifiedDate();
	}

	/**
	* Returns the mvcc version of this background task.
	*
	* @return the mvcc version of this background task
	*/
	@Override
	public long getMvccVersion() {
		return _backgroundTask.getMvccVersion();
	}

	/**
	* Returns the name of this background task.
	*
	* @return the name of this background task
	*/
	@Override
	public String getName() {
		return _backgroundTask.getName();
	}

	/**
	* Returns the primary key of this background task.
	*
	* @return the primary key of this background task
	*/
	@Override
	public long getPrimaryKey() {
		return _backgroundTask.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _backgroundTask.getPrimaryKeyObj();
	}

	/**
	* Returns the servlet context names of this background task.
	*
	* @return the servlet context names of this background task
	*/
	@Override
	public String getServletContextNames() {
		return _backgroundTask.getServletContextNames();
	}

	/**
	* Returns the status of this background task.
	*
	* @return the status of this background task
	*/
	@Override
	public int getStatus() {
		return _backgroundTask.getStatus();
	}

	@Override
	public String getStatusLabel() {
		return _backgroundTask.getStatusLabel();
	}

	/**
	* Returns the status message of this background task.
	*
	* @return the status message of this background task
	*/
	@Override
	public String getStatusMessage() {
		return _backgroundTask.getStatusMessage();
	}

	/**
	* Returns the task context map of this background task.
	*
	* @return the task context map of this background task
	*/
	@Override
	public Map<String, Serializable> getTaskContextMap() {
		return _backgroundTask.getTaskContextMap();
	}

	/**
	* Returns the task executor class name of this background task.
	*
	* @return the task executor class name of this background task
	*/
	@Override
	public String getTaskExecutorClassName() {
		return _backgroundTask.getTaskExecutorClassName();
	}

	/**
	* Returns the user ID of this background task.
	*
	* @return the user ID of this background task
	*/
	@Override
	public long getUserId() {
		return _backgroundTask.getUserId();
	}

	/**
	* Returns the user name of this background task.
	*
	* @return the user name of this background task
	*/
	@Override
	public String getUserName() {
		return _backgroundTask.getUserName();
	}

	/**
	* Returns the user uuid of this background task.
	*
	* @return the user uuid of this background task
	*/
	@Override
	public String getUserUuid() {
		return _backgroundTask.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _backgroundTask.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _backgroundTask.isCachedModel();
	}

	/**
	* Returns <code>true</code> if this background task is completed.
	*
	* @return <code>true</code> if this background task is completed; <code>false</code> otherwise
	*/
	@Override
	public boolean isCompleted() {
		return _backgroundTask.isCompleted();
	}

	@Override
	public boolean isEscapedModel() {
		return _backgroundTask.isEscapedModel();
	}

	@Override
	public boolean isInProgress() {
		return _backgroundTask.isInProgress();
	}

	@Override
	public boolean isNew() {
		return _backgroundTask.isNew();
	}

	@Override
	public void persist() {
		_backgroundTask.persist();
	}

	/**
	* Sets the background task ID of this background task.
	*
	* @param backgroundTaskId the background task ID of this background task
	*/
	@Override
	public void setBackgroundTaskId(long backgroundTaskId) {
		_backgroundTask.setBackgroundTaskId(backgroundTaskId);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_backgroundTask.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this background task.
	*
	* @param companyId the company ID of this background task
	*/
	@Override
	public void setCompanyId(long companyId) {
		_backgroundTask.setCompanyId(companyId);
	}

	/**
	* Sets whether this background task is completed.
	*
	* @param completed the completed of this background task
	*/
	@Override
	public void setCompleted(boolean completed) {
		_backgroundTask.setCompleted(completed);
	}

	/**
	* Sets the completion date of this background task.
	*
	* @param completionDate the completion date of this background task
	*/
	@Override
	public void setCompletionDate(Date completionDate) {
		_backgroundTask.setCompletionDate(completionDate);
	}

	/**
	* Sets the create date of this background task.
	*
	* @param createDate the create date of this background task
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_backgroundTask.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_backgroundTask.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_backgroundTask.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_backgroundTask.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this background task.
	*
	* @param groupId the group ID of this background task
	*/
	@Override
	public void setGroupId(long groupId) {
		_backgroundTask.setGroupId(groupId);
	}

	/**
	* Sets the modified date of this background task.
	*
	* @param modifiedDate the modified date of this background task
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_backgroundTask.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the mvcc version of this background task.
	*
	* @param mvccVersion the mvcc version of this background task
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_backgroundTask.setMvccVersion(mvccVersion);
	}

	/**
	* Sets the name of this background task.
	*
	* @param name the name of this background task
	*/
	@Override
	public void setName(String name) {
		_backgroundTask.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_backgroundTask.setNew(n);
	}

	/**
	* Sets the primary key of this background task.
	*
	* @param primaryKey the primary key of this background task
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_backgroundTask.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_backgroundTask.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the servlet context names of this background task.
	*
	* @param servletContextNames the servlet context names of this background task
	*/
	@Override
	public void setServletContextNames(String servletContextNames) {
		_backgroundTask.setServletContextNames(servletContextNames);
	}

	/**
	* Sets the status of this background task.
	*
	* @param status the status of this background task
	*/
	@Override
	public void setStatus(int status) {
		_backgroundTask.setStatus(status);
	}

	/**
	* Sets the status message of this background task.
	*
	* @param statusMessage the status message of this background task
	*/
	@Override
	public void setStatusMessage(String statusMessage) {
		_backgroundTask.setStatusMessage(statusMessage);
	}

	/**
	* Sets the task context map of this background task.
	*
	* @param taskContextMap the task context map of this background task
	*/
	@Override
	public void setTaskContextMap(Map<String, Serializable> taskContextMap) {
		_backgroundTask.setTaskContextMap(taskContextMap);
	}

	/**
	* Sets the task executor class name of this background task.
	*
	* @param taskExecutorClassName the task executor class name of this background task
	*/
	@Override
	public void setTaskExecutorClassName(String taskExecutorClassName) {
		_backgroundTask.setTaskExecutorClassName(taskExecutorClassName);
	}

	/**
	* Sets the user ID of this background task.
	*
	* @param userId the user ID of this background task
	*/
	@Override
	public void setUserId(long userId) {
		_backgroundTask.setUserId(userId);
	}

	/**
	* Sets the user name of this background task.
	*
	* @param userName the user name of this background task
	*/
	@Override
	public void setUserName(String userName) {
		_backgroundTask.setUserName(userName);
	}

	/**
	* Sets the user uuid of this background task.
	*
	* @param userUuid the user uuid of this background task
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_backgroundTask.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<BackgroundTask> toCacheModel() {
		return _backgroundTask.toCacheModel();
	}

	@Override
	public BackgroundTask toEscapedModel() {
		return new BackgroundTaskWrapper(_backgroundTask.toEscapedModel());
	}

	@Override
	public String toString() {
		return _backgroundTask.toString();
	}

	@Override
	public BackgroundTask toUnescapedModel() {
		return new BackgroundTaskWrapper(_backgroundTask.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _backgroundTask.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BackgroundTaskWrapper)) {
			return false;
		}

		BackgroundTaskWrapper backgroundTaskWrapper = (BackgroundTaskWrapper)obj;

		if (Objects.equals(_backgroundTask,
					backgroundTaskWrapper._backgroundTask)) {
			return true;
		}

		return false;
	}

	@Override
	public BackgroundTask getWrappedModel() {
		return _backgroundTask;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _backgroundTask.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _backgroundTask.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_backgroundTask.resetOriginalValues();
	}

	private final BackgroundTask _backgroundTask;
}