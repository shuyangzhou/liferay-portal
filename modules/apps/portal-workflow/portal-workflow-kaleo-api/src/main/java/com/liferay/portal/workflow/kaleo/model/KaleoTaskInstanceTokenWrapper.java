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
 * This class is a wrapper for {@link KaleoTaskInstanceToken}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoTaskInstanceToken
 * @generated
 */
@ProviderType
public class KaleoTaskInstanceTokenWrapper implements KaleoTaskInstanceToken,
	ModelWrapper<KaleoTaskInstanceToken> {
	public KaleoTaskInstanceTokenWrapper(
		KaleoTaskInstanceToken kaleoTaskInstanceToken) {
		_kaleoTaskInstanceToken = kaleoTaskInstanceToken;
	}

	@Override
	public Class<?> getModelClass() {
		return KaleoTaskInstanceToken.class;
	}

	@Override
	public String getModelClassName() {
		return KaleoTaskInstanceToken.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<KaleoTaskInstanceToken, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<KaleoTaskInstanceToken, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<KaleoTaskInstanceToken, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<KaleoTaskInstanceToken, Object>> attributeSetters =
			getAttributeSetters();

		for (Map.Entry<String, BiConsumer<KaleoTaskInstanceToken, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<KaleoTaskInstanceToken, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<KaleoTaskInstanceToken, Object>> getAttributeGetters() {
		return _kaleoTaskInstanceToken.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<KaleoTaskInstanceToken, Object>> getAttributeSetters() {
		return _kaleoTaskInstanceToken.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new KaleoTaskInstanceTokenWrapper((KaleoTaskInstanceToken)_kaleoTaskInstanceToken.clone());
	}

	@Override
	public int compareTo(KaleoTaskInstanceToken kaleoTaskInstanceToken) {
		return _kaleoTaskInstanceToken.compareTo(kaleoTaskInstanceToken);
	}

	/**
	* Returns the class name of this kaleo task instance token.
	*
	* @return the class name of this kaleo task instance token
	*/
	@Override
	public String getClassName() {
		return _kaleoTaskInstanceToken.getClassName();
	}

	/**
	* Returns the class pk of this kaleo task instance token.
	*
	* @return the class pk of this kaleo task instance token
	*/
	@Override
	public long getClassPK() {
		return _kaleoTaskInstanceToken.getClassPK();
	}

	/**
	* Returns the company ID of this kaleo task instance token.
	*
	* @return the company ID of this kaleo task instance token
	*/
	@Override
	public long getCompanyId() {
		return _kaleoTaskInstanceToken.getCompanyId();
	}

	/**
	* Returns the completed of this kaleo task instance token.
	*
	* @return the completed of this kaleo task instance token
	*/
	@Override
	public boolean getCompleted() {
		return _kaleoTaskInstanceToken.getCompleted();
	}

	/**
	* Returns the completion date of this kaleo task instance token.
	*
	* @return the completion date of this kaleo task instance token
	*/
	@Override
	public Date getCompletionDate() {
		return _kaleoTaskInstanceToken.getCompletionDate();
	}

	/**
	* Returns the completion user ID of this kaleo task instance token.
	*
	* @return the completion user ID of this kaleo task instance token
	*/
	@Override
	public long getCompletionUserId() {
		return _kaleoTaskInstanceToken.getCompletionUserId();
	}

	/**
	* Returns the completion user uuid of this kaleo task instance token.
	*
	* @return the completion user uuid of this kaleo task instance token
	*/
	@Override
	public String getCompletionUserUuid() {
		return _kaleoTaskInstanceToken.getCompletionUserUuid();
	}

	/**
	* Returns the create date of this kaleo task instance token.
	*
	* @return the create date of this kaleo task instance token
	*/
	@Override
	public Date getCreateDate() {
		return _kaleoTaskInstanceToken.getCreateDate();
	}

	/**
	* Returns the due date of this kaleo task instance token.
	*
	* @return the due date of this kaleo task instance token
	*/
	@Override
	public Date getDueDate() {
		return _kaleoTaskInstanceToken.getDueDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _kaleoTaskInstanceToken.getExpandoBridge();
	}

	@Override
	public KaleoTaskAssignmentInstance getFirstKaleoTaskAssignmentInstance() {
		return _kaleoTaskInstanceToken.getFirstKaleoTaskAssignmentInstance();
	}

	/**
	* Returns the group ID of this kaleo task instance token.
	*
	* @return the group ID of this kaleo task instance token
	*/
	@Override
	public long getGroupId() {
		return _kaleoTaskInstanceToken.getGroupId();
	}

	/**
	* Returns the kaleo definition version ID of this kaleo task instance token.
	*
	* @return the kaleo definition version ID of this kaleo task instance token
	*/
	@Override
	public long getKaleoDefinitionVersionId() {
		return _kaleoTaskInstanceToken.getKaleoDefinitionVersionId();
	}

	/**
	* Returns the kaleo instance ID of this kaleo task instance token.
	*
	* @return the kaleo instance ID of this kaleo task instance token
	*/
	@Override
	public long getKaleoInstanceId() {
		return _kaleoTaskInstanceToken.getKaleoInstanceId();
	}

	@Override
	public KaleoInstanceToken getKaleoInstanceToken()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoTaskInstanceToken.getKaleoInstanceToken();
	}

	/**
	* Returns the kaleo instance token ID of this kaleo task instance token.
	*
	* @return the kaleo instance token ID of this kaleo task instance token
	*/
	@Override
	public long getKaleoInstanceTokenId() {
		return _kaleoTaskInstanceToken.getKaleoInstanceTokenId();
	}

	@Override
	public KaleoTask getKaleoTask()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kaleoTaskInstanceToken.getKaleoTask();
	}

	@Override
	public java.util.List<KaleoTaskAssignmentInstance> getKaleoTaskAssignmentInstances() {
		return _kaleoTaskInstanceToken.getKaleoTaskAssignmentInstances();
	}

	/**
	* Returns the kaleo task ID of this kaleo task instance token.
	*
	* @return the kaleo task ID of this kaleo task instance token
	*/
	@Override
	public long getKaleoTaskId() {
		return _kaleoTaskInstanceToken.getKaleoTaskId();
	}

	/**
	* Returns the kaleo task instance token ID of this kaleo task instance token.
	*
	* @return the kaleo task instance token ID of this kaleo task instance token
	*/
	@Override
	public long getKaleoTaskInstanceTokenId() {
		return _kaleoTaskInstanceToken.getKaleoTaskInstanceTokenId();
	}

	/**
	* Returns the kaleo task name of this kaleo task instance token.
	*
	* @return the kaleo task name of this kaleo task instance token
	*/
	@Override
	public String getKaleoTaskName() {
		return _kaleoTaskInstanceToken.getKaleoTaskName();
	}

	/**
	* Returns the modified date of this kaleo task instance token.
	*
	* @return the modified date of this kaleo task instance token
	*/
	@Override
	public Date getModifiedDate() {
		return _kaleoTaskInstanceToken.getModifiedDate();
	}

	/**
	* Returns the primary key of this kaleo task instance token.
	*
	* @return the primary key of this kaleo task instance token
	*/
	@Override
	public long getPrimaryKey() {
		return _kaleoTaskInstanceToken.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kaleoTaskInstanceToken.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this kaleo task instance token.
	*
	* @return the user ID of this kaleo task instance token
	*/
	@Override
	public long getUserId() {
		return _kaleoTaskInstanceToken.getUserId();
	}

	/**
	* Returns the user name of this kaleo task instance token.
	*
	* @return the user name of this kaleo task instance token
	*/
	@Override
	public String getUserName() {
		return _kaleoTaskInstanceToken.getUserName();
	}

	/**
	* Returns the user uuid of this kaleo task instance token.
	*
	* @return the user uuid of this kaleo task instance token
	*/
	@Override
	public String getUserUuid() {
		return _kaleoTaskInstanceToken.getUserUuid();
	}

	/**
	* Returns the workflow context of this kaleo task instance token.
	*
	* @return the workflow context of this kaleo task instance token
	*/
	@Override
	public String getWorkflowContext() {
		return _kaleoTaskInstanceToken.getWorkflowContext();
	}

	@Override
	public int hashCode() {
		return _kaleoTaskInstanceToken.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _kaleoTaskInstanceToken.isCachedModel();
	}

	/**
	* Returns <code>true</code> if this kaleo task instance token is completed.
	*
	* @return <code>true</code> if this kaleo task instance token is completed; <code>false</code> otherwise
	*/
	@Override
	public boolean isCompleted() {
		return _kaleoTaskInstanceToken.isCompleted();
	}

	@Override
	public boolean isEscapedModel() {
		return _kaleoTaskInstanceToken.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _kaleoTaskInstanceToken.isNew();
	}

	@Override
	public void persist() {
		_kaleoTaskInstanceToken.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_kaleoTaskInstanceToken.setCachedModel(cachedModel);
	}

	/**
	* Sets the class name of this kaleo task instance token.
	*
	* @param className the class name of this kaleo task instance token
	*/
	@Override
	public void setClassName(String className) {
		_kaleoTaskInstanceToken.setClassName(className);
	}

	/**
	* Sets the class pk of this kaleo task instance token.
	*
	* @param classPK the class pk of this kaleo task instance token
	*/
	@Override
	public void setClassPK(long classPK) {
		_kaleoTaskInstanceToken.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this kaleo task instance token.
	*
	* @param companyId the company ID of this kaleo task instance token
	*/
	@Override
	public void setCompanyId(long companyId) {
		_kaleoTaskInstanceToken.setCompanyId(companyId);
	}

	/**
	* Sets whether this kaleo task instance token is completed.
	*
	* @param completed the completed of this kaleo task instance token
	*/
	@Override
	public void setCompleted(boolean completed) {
		_kaleoTaskInstanceToken.setCompleted(completed);
	}

	/**
	* Sets the completion date of this kaleo task instance token.
	*
	* @param completionDate the completion date of this kaleo task instance token
	*/
	@Override
	public void setCompletionDate(Date completionDate) {
		_kaleoTaskInstanceToken.setCompletionDate(completionDate);
	}

	/**
	* Sets the completion user ID of this kaleo task instance token.
	*
	* @param completionUserId the completion user ID of this kaleo task instance token
	*/
	@Override
	public void setCompletionUserId(long completionUserId) {
		_kaleoTaskInstanceToken.setCompletionUserId(completionUserId);
	}

	/**
	* Sets the completion user uuid of this kaleo task instance token.
	*
	* @param completionUserUuid the completion user uuid of this kaleo task instance token
	*/
	@Override
	public void setCompletionUserUuid(String completionUserUuid) {
		_kaleoTaskInstanceToken.setCompletionUserUuid(completionUserUuid);
	}

	/**
	* Sets the create date of this kaleo task instance token.
	*
	* @param createDate the create date of this kaleo task instance token
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_kaleoTaskInstanceToken.setCreateDate(createDate);
	}

	/**
	* Sets the due date of this kaleo task instance token.
	*
	* @param dueDate the due date of this kaleo task instance token
	*/
	@Override
	public void setDueDate(Date dueDate) {
		_kaleoTaskInstanceToken.setDueDate(dueDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_kaleoTaskInstanceToken.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_kaleoTaskInstanceToken.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_kaleoTaskInstanceToken.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this kaleo task instance token.
	*
	* @param groupId the group ID of this kaleo task instance token
	*/
	@Override
	public void setGroupId(long groupId) {
		_kaleoTaskInstanceToken.setGroupId(groupId);
	}

	/**
	* Sets the kaleo definition version ID of this kaleo task instance token.
	*
	* @param kaleoDefinitionVersionId the kaleo definition version ID of this kaleo task instance token
	*/
	@Override
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId) {
		_kaleoTaskInstanceToken.setKaleoDefinitionVersionId(kaleoDefinitionVersionId);
	}

	/**
	* Sets the kaleo instance ID of this kaleo task instance token.
	*
	* @param kaleoInstanceId the kaleo instance ID of this kaleo task instance token
	*/
	@Override
	public void setKaleoInstanceId(long kaleoInstanceId) {
		_kaleoTaskInstanceToken.setKaleoInstanceId(kaleoInstanceId);
	}

	/**
	* Sets the kaleo instance token ID of this kaleo task instance token.
	*
	* @param kaleoInstanceTokenId the kaleo instance token ID of this kaleo task instance token
	*/
	@Override
	public void setKaleoInstanceTokenId(long kaleoInstanceTokenId) {
		_kaleoTaskInstanceToken.setKaleoInstanceTokenId(kaleoInstanceTokenId);
	}

	/**
	* Sets the kaleo task ID of this kaleo task instance token.
	*
	* @param kaleoTaskId the kaleo task ID of this kaleo task instance token
	*/
	@Override
	public void setKaleoTaskId(long kaleoTaskId) {
		_kaleoTaskInstanceToken.setKaleoTaskId(kaleoTaskId);
	}

	/**
	* Sets the kaleo task instance token ID of this kaleo task instance token.
	*
	* @param kaleoTaskInstanceTokenId the kaleo task instance token ID of this kaleo task instance token
	*/
	@Override
	public void setKaleoTaskInstanceTokenId(long kaleoTaskInstanceTokenId) {
		_kaleoTaskInstanceToken.setKaleoTaskInstanceTokenId(kaleoTaskInstanceTokenId);
	}

	/**
	* Sets the kaleo task name of this kaleo task instance token.
	*
	* @param kaleoTaskName the kaleo task name of this kaleo task instance token
	*/
	@Override
	public void setKaleoTaskName(String kaleoTaskName) {
		_kaleoTaskInstanceToken.setKaleoTaskName(kaleoTaskName);
	}

	/**
	* Sets the modified date of this kaleo task instance token.
	*
	* @param modifiedDate the modified date of this kaleo task instance token
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_kaleoTaskInstanceToken.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_kaleoTaskInstanceToken.setNew(n);
	}

	/**
	* Sets the primary key of this kaleo task instance token.
	*
	* @param primaryKey the primary key of this kaleo task instance token
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_kaleoTaskInstanceToken.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_kaleoTaskInstanceToken.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this kaleo task instance token.
	*
	* @param userId the user ID of this kaleo task instance token
	*/
	@Override
	public void setUserId(long userId) {
		_kaleoTaskInstanceToken.setUserId(userId);
	}

	/**
	* Sets the user name of this kaleo task instance token.
	*
	* @param userName the user name of this kaleo task instance token
	*/
	@Override
	public void setUserName(String userName) {
		_kaleoTaskInstanceToken.setUserName(userName);
	}

	/**
	* Sets the user uuid of this kaleo task instance token.
	*
	* @param userUuid the user uuid of this kaleo task instance token
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_kaleoTaskInstanceToken.setUserUuid(userUuid);
	}

	/**
	* Sets the workflow context of this kaleo task instance token.
	*
	* @param workflowContext the workflow context of this kaleo task instance token
	*/
	@Override
	public void setWorkflowContext(String workflowContext) {
		_kaleoTaskInstanceToken.setWorkflowContext(workflowContext);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<KaleoTaskInstanceToken> toCacheModel() {
		return _kaleoTaskInstanceToken.toCacheModel();
	}

	@Override
	public KaleoTaskInstanceToken toEscapedModel() {
		return new KaleoTaskInstanceTokenWrapper(_kaleoTaskInstanceToken.toEscapedModel());
	}

	@Override
	public String toString() {
		return _kaleoTaskInstanceToken.toString();
	}

	@Override
	public KaleoTaskInstanceToken toUnescapedModel() {
		return new KaleoTaskInstanceTokenWrapper(_kaleoTaskInstanceToken.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _kaleoTaskInstanceToken.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof KaleoTaskInstanceTokenWrapper)) {
			return false;
		}

		KaleoTaskInstanceTokenWrapper kaleoTaskInstanceTokenWrapper = (KaleoTaskInstanceTokenWrapper)obj;

		if (Objects.equals(_kaleoTaskInstanceToken,
					kaleoTaskInstanceTokenWrapper._kaleoTaskInstanceToken)) {
			return true;
		}

		return false;
	}

	@Override
	public KaleoTaskInstanceToken getWrappedModel() {
		return _kaleoTaskInstanceToken;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _kaleoTaskInstanceToken.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _kaleoTaskInstanceToken.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_kaleoTaskInstanceToken.resetOriginalValues();
	}

	private final KaleoTaskInstanceToken _kaleoTaskInstanceToken;
}