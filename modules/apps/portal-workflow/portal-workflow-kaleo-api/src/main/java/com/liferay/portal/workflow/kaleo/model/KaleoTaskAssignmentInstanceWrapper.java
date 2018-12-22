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

package com.liferay.portal.workflow.kaleo.model;

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
 * This class is a wrapper for {@link KaleoTaskAssignmentInstance}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoTaskAssignmentInstance
 * @generated
 */
@ProviderType
public class KaleoTaskAssignmentInstanceWrapper
	implements KaleoTaskAssignmentInstance,
		ModelWrapper<KaleoTaskAssignmentInstance> {
	public KaleoTaskAssignmentInstanceWrapper(
		KaleoTaskAssignmentInstance kaleoTaskAssignmentInstance) {
		_kaleoTaskAssignmentInstance = kaleoTaskAssignmentInstance;
	}

	@Override
	public Class<?> getModelClass() {
		return KaleoTaskAssignmentInstance.class;
	}

	@Override
	public String getModelClassName() {
		return KaleoTaskAssignmentInstance.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<KaleoTaskAssignmentInstance, Object>> attributeGetters =
			getAttributeGetters();

		for (Map.Entry<String, Function<KaleoTaskAssignmentInstance, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<KaleoTaskAssignmentInstance, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<KaleoTaskAssignmentInstance, Object>> attributeSetters =
			getAttributeSetters();

		for (Map.Entry<String, BiConsumer<KaleoTaskAssignmentInstance, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<KaleoTaskAssignmentInstance, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<KaleoTaskAssignmentInstance, Object>> getAttributeGetters() {
		return _kaleoTaskAssignmentInstance.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<KaleoTaskAssignmentInstance, Object>> getAttributeSetters() {
		return _kaleoTaskAssignmentInstance.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new KaleoTaskAssignmentInstanceWrapper((KaleoTaskAssignmentInstance)_kaleoTaskAssignmentInstance.clone());
	}

	@Override
	public int compareTo(
		KaleoTaskAssignmentInstance kaleoTaskAssignmentInstance) {
		return _kaleoTaskAssignmentInstance.compareTo(kaleoTaskAssignmentInstance);
	}

	/**
	* Returns the assignee class name of this kaleo task assignment instance.
	*
	* @return the assignee class name of this kaleo task assignment instance
	*/
	@Override
	public String getAssigneeClassName() {
		return _kaleoTaskAssignmentInstance.getAssigneeClassName();
	}

	/**
	* Returns the assignee class pk of this kaleo task assignment instance.
	*
	* @return the assignee class pk of this kaleo task assignment instance
	*/
	@Override
	public long getAssigneeClassPK() {
		return _kaleoTaskAssignmentInstance.getAssigneeClassPK();
	}

	/**
	* Returns the company ID of this kaleo task assignment instance.
	*
	* @return the company ID of this kaleo task assignment instance
	*/
	@Override
	public long getCompanyId() {
		return _kaleoTaskAssignmentInstance.getCompanyId();
	}

	/**
	* Returns the completed of this kaleo task assignment instance.
	*
	* @return the completed of this kaleo task assignment instance
	*/
	@Override
	public boolean getCompleted() {
		return _kaleoTaskAssignmentInstance.getCompleted();
	}

	/**
	* Returns the completion date of this kaleo task assignment instance.
	*
	* @return the completion date of this kaleo task assignment instance
	*/
	@Override
	public Date getCompletionDate() {
		return _kaleoTaskAssignmentInstance.getCompletionDate();
	}

	/**
	* Returns the create date of this kaleo task assignment instance.
	*
	* @return the create date of this kaleo task assignment instance
	*/
	@Override
	public Date getCreateDate() {
		return _kaleoTaskAssignmentInstance.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _kaleoTaskAssignmentInstance.getExpandoBridge();
	}

	/**
	* Returns the group ID of this kaleo task assignment instance.
	*
	* @return the group ID of this kaleo task assignment instance
	*/
	@Override
	public long getGroupId() {
		return _kaleoTaskAssignmentInstance.getGroupId();
	}

	/**
	* Returns the kaleo definition version ID of this kaleo task assignment instance.
	*
	* @return the kaleo definition version ID of this kaleo task assignment instance
	*/
	@Override
	public long getKaleoDefinitionVersionId() {
		return _kaleoTaskAssignmentInstance.getKaleoDefinitionVersionId();
	}

	/**
	* Returns the kaleo instance ID of this kaleo task assignment instance.
	*
	* @return the kaleo instance ID of this kaleo task assignment instance
	*/
	@Override
	public long getKaleoInstanceId() {
		return _kaleoTaskAssignmentInstance.getKaleoInstanceId();
	}

	/**
	* Returns the kaleo instance token ID of this kaleo task assignment instance.
	*
	* @return the kaleo instance token ID of this kaleo task assignment instance
	*/
	@Override
	public long getKaleoInstanceTokenId() {
		return _kaleoTaskAssignmentInstance.getKaleoInstanceTokenId();
	}

	/**
	* Returns the kaleo task assignment instance ID of this kaleo task assignment instance.
	*
	* @return the kaleo task assignment instance ID of this kaleo task assignment instance
	*/
	@Override
	public long getKaleoTaskAssignmentInstanceId() {
		return _kaleoTaskAssignmentInstance.getKaleoTaskAssignmentInstanceId();
	}

	/**
	* Returns the kaleo task ID of this kaleo task assignment instance.
	*
	* @return the kaleo task ID of this kaleo task assignment instance
	*/
	@Override
	public long getKaleoTaskId() {
		return _kaleoTaskAssignmentInstance.getKaleoTaskId();
	}

	/**
	* Returns the kaleo task instance token ID of this kaleo task assignment instance.
	*
	* @return the kaleo task instance token ID of this kaleo task assignment instance
	*/
	@Override
	public long getKaleoTaskInstanceTokenId() {
		return _kaleoTaskAssignmentInstance.getKaleoTaskInstanceTokenId();
	}

	/**
	* Returns the kaleo task name of this kaleo task assignment instance.
	*
	* @return the kaleo task name of this kaleo task assignment instance
	*/
	@Override
	public String getKaleoTaskName() {
		return _kaleoTaskAssignmentInstance.getKaleoTaskName();
	}

	/**
	* Returns the modified date of this kaleo task assignment instance.
	*
	* @return the modified date of this kaleo task assignment instance
	*/
	@Override
	public Date getModifiedDate() {
		return _kaleoTaskAssignmentInstance.getModifiedDate();
	}

	/**
	* Returns the primary key of this kaleo task assignment instance.
	*
	* @return the primary key of this kaleo task assignment instance
	*/
	@Override
	public long getPrimaryKey() {
		return _kaleoTaskAssignmentInstance.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kaleoTaskAssignmentInstance.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this kaleo task assignment instance.
	*
	* @return the user ID of this kaleo task assignment instance
	*/
	@Override
	public long getUserId() {
		return _kaleoTaskAssignmentInstance.getUserId();
	}

	/**
	* Returns the user name of this kaleo task assignment instance.
	*
	* @return the user name of this kaleo task assignment instance
	*/
	@Override
	public String getUserName() {
		return _kaleoTaskAssignmentInstance.getUserName();
	}

	/**
	* Returns the user uuid of this kaleo task assignment instance.
	*
	* @return the user uuid of this kaleo task assignment instance
	*/
	@Override
	public String getUserUuid() {
		return _kaleoTaskAssignmentInstance.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _kaleoTaskAssignmentInstance.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _kaleoTaskAssignmentInstance.isCachedModel();
	}

	/**
	* Returns <code>true</code> if this kaleo task assignment instance is completed.
	*
	* @return <code>true</code> if this kaleo task assignment instance is completed; <code>false</code> otherwise
	*/
	@Override
	public boolean isCompleted() {
		return _kaleoTaskAssignmentInstance.isCompleted();
	}

	@Override
	public boolean isEscapedModel() {
		return _kaleoTaskAssignmentInstance.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _kaleoTaskAssignmentInstance.isNew();
	}

	@Override
	public void persist() {
		_kaleoTaskAssignmentInstance.persist();
	}

	/**
	* Sets the assignee class name of this kaleo task assignment instance.
	*
	* @param assigneeClassName the assignee class name of this kaleo task assignment instance
	*/
	@Override
	public void setAssigneeClassName(String assigneeClassName) {
		_kaleoTaskAssignmentInstance.setAssigneeClassName(assigneeClassName);
	}

	/**
	* Sets the assignee class pk of this kaleo task assignment instance.
	*
	* @param assigneeClassPK the assignee class pk of this kaleo task assignment instance
	*/
	@Override
	public void setAssigneeClassPK(long assigneeClassPK) {
		_kaleoTaskAssignmentInstance.setAssigneeClassPK(assigneeClassPK);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_kaleoTaskAssignmentInstance.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this kaleo task assignment instance.
	*
	* @param companyId the company ID of this kaleo task assignment instance
	*/
	@Override
	public void setCompanyId(long companyId) {
		_kaleoTaskAssignmentInstance.setCompanyId(companyId);
	}

	/**
	* Sets whether this kaleo task assignment instance is completed.
	*
	* @param completed the completed of this kaleo task assignment instance
	*/
	@Override
	public void setCompleted(boolean completed) {
		_kaleoTaskAssignmentInstance.setCompleted(completed);
	}

	/**
	* Sets the completion date of this kaleo task assignment instance.
	*
	* @param completionDate the completion date of this kaleo task assignment instance
	*/
	@Override
	public void setCompletionDate(Date completionDate) {
		_kaleoTaskAssignmentInstance.setCompletionDate(completionDate);
	}

	/**
	* Sets the create date of this kaleo task assignment instance.
	*
	* @param createDate the create date of this kaleo task assignment instance
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_kaleoTaskAssignmentInstance.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_kaleoTaskAssignmentInstance.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_kaleoTaskAssignmentInstance.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_kaleoTaskAssignmentInstance.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this kaleo task assignment instance.
	*
	* @param groupId the group ID of this kaleo task assignment instance
	*/
	@Override
	public void setGroupId(long groupId) {
		_kaleoTaskAssignmentInstance.setGroupId(groupId);
	}

	/**
	* Sets the kaleo definition version ID of this kaleo task assignment instance.
	*
	* @param kaleoDefinitionVersionId the kaleo definition version ID of this kaleo task assignment instance
	*/
	@Override
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId) {
		_kaleoTaskAssignmentInstance.setKaleoDefinitionVersionId(kaleoDefinitionVersionId);
	}

	/**
	* Sets the kaleo instance ID of this kaleo task assignment instance.
	*
	* @param kaleoInstanceId the kaleo instance ID of this kaleo task assignment instance
	*/
	@Override
	public void setKaleoInstanceId(long kaleoInstanceId) {
		_kaleoTaskAssignmentInstance.setKaleoInstanceId(kaleoInstanceId);
	}

	/**
	* Sets the kaleo instance token ID of this kaleo task assignment instance.
	*
	* @param kaleoInstanceTokenId the kaleo instance token ID of this kaleo task assignment instance
	*/
	@Override
	public void setKaleoInstanceTokenId(long kaleoInstanceTokenId) {
		_kaleoTaskAssignmentInstance.setKaleoInstanceTokenId(kaleoInstanceTokenId);
	}

	/**
	* Sets the kaleo task assignment instance ID of this kaleo task assignment instance.
	*
	* @param kaleoTaskAssignmentInstanceId the kaleo task assignment instance ID of this kaleo task assignment instance
	*/
	@Override
	public void setKaleoTaskAssignmentInstanceId(
		long kaleoTaskAssignmentInstanceId) {
		_kaleoTaskAssignmentInstance.setKaleoTaskAssignmentInstanceId(kaleoTaskAssignmentInstanceId);
	}

	/**
	* Sets the kaleo task ID of this kaleo task assignment instance.
	*
	* @param kaleoTaskId the kaleo task ID of this kaleo task assignment instance
	*/
	@Override
	public void setKaleoTaskId(long kaleoTaskId) {
		_kaleoTaskAssignmentInstance.setKaleoTaskId(kaleoTaskId);
	}

	/**
	* Sets the kaleo task instance token ID of this kaleo task assignment instance.
	*
	* @param kaleoTaskInstanceTokenId the kaleo task instance token ID of this kaleo task assignment instance
	*/
	@Override
	public void setKaleoTaskInstanceTokenId(long kaleoTaskInstanceTokenId) {
		_kaleoTaskAssignmentInstance.setKaleoTaskInstanceTokenId(kaleoTaskInstanceTokenId);
	}

	/**
	* Sets the kaleo task name of this kaleo task assignment instance.
	*
	* @param kaleoTaskName the kaleo task name of this kaleo task assignment instance
	*/
	@Override
	public void setKaleoTaskName(String kaleoTaskName) {
		_kaleoTaskAssignmentInstance.setKaleoTaskName(kaleoTaskName);
	}

	/**
	* Sets the modified date of this kaleo task assignment instance.
	*
	* @param modifiedDate the modified date of this kaleo task assignment instance
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_kaleoTaskAssignmentInstance.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_kaleoTaskAssignmentInstance.setNew(n);
	}

	/**
	* Sets the primary key of this kaleo task assignment instance.
	*
	* @param primaryKey the primary key of this kaleo task assignment instance
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_kaleoTaskAssignmentInstance.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_kaleoTaskAssignmentInstance.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this kaleo task assignment instance.
	*
	* @param userId the user ID of this kaleo task assignment instance
	*/
	@Override
	public void setUserId(long userId) {
		_kaleoTaskAssignmentInstance.setUserId(userId);
	}

	/**
	* Sets the user name of this kaleo task assignment instance.
	*
	* @param userName the user name of this kaleo task assignment instance
	*/
	@Override
	public void setUserName(String userName) {
		_kaleoTaskAssignmentInstance.setUserName(userName);
	}

	/**
	* Sets the user uuid of this kaleo task assignment instance.
	*
	* @param userUuid the user uuid of this kaleo task assignment instance
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_kaleoTaskAssignmentInstance.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<KaleoTaskAssignmentInstance> toCacheModel() {
		return _kaleoTaskAssignmentInstance.toCacheModel();
	}

	@Override
	public KaleoTaskAssignmentInstance toEscapedModel() {
		return new KaleoTaskAssignmentInstanceWrapper(_kaleoTaskAssignmentInstance.toEscapedModel());
	}

	@Override
	public String toString() {
		return _kaleoTaskAssignmentInstance.toString();
	}

	@Override
	public KaleoTaskAssignmentInstance toUnescapedModel() {
		return new KaleoTaskAssignmentInstanceWrapper(_kaleoTaskAssignmentInstance.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _kaleoTaskAssignmentInstance.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof KaleoTaskAssignmentInstanceWrapper)) {
			return false;
		}

		KaleoTaskAssignmentInstanceWrapper kaleoTaskAssignmentInstanceWrapper = (KaleoTaskAssignmentInstanceWrapper)obj;

		if (Objects.equals(_kaleoTaskAssignmentInstance,
					kaleoTaskAssignmentInstanceWrapper._kaleoTaskAssignmentInstance)) {
			return true;
		}

		return false;
	}

	@Override
	public KaleoTaskAssignmentInstance getWrappedModel() {
		return _kaleoTaskAssignmentInstance;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _kaleoTaskAssignmentInstance.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _kaleoTaskAssignmentInstance.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_kaleoTaskAssignmentInstance.resetOriginalValues();
	}

	private final KaleoTaskAssignmentInstance _kaleoTaskAssignmentInstance;
}