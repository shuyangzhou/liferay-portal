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

package com.liferay.portal.kernel.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

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
 * This class is a wrapper for {@link WorkflowInstanceLink}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see WorkflowInstanceLink
 * @generated
 */
@ProviderType
public class WorkflowInstanceLinkWrapper implements WorkflowInstanceLink,
	ModelWrapper<WorkflowInstanceLink> {
	public WorkflowInstanceLinkWrapper(
		WorkflowInstanceLink workflowInstanceLink) {
		_workflowInstanceLink = workflowInstanceLink;
	}

	@Override
	public Class<?> getModelClass() {
		return WorkflowInstanceLink.class;
	}

	@Override
	public String getModelClassName() {
		return WorkflowInstanceLink.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<WorkflowInstanceLink, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<WorkflowInstanceLink, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<WorkflowInstanceLink, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<WorkflowInstanceLink, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<WorkflowInstanceLink, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<WorkflowInstanceLink, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<WorkflowInstanceLink, Object>> getAttributeGetters() {
		return _workflowInstanceLink.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<WorkflowInstanceLink, Object>> getAttributeSetters() {
		return _workflowInstanceLink.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new WorkflowInstanceLinkWrapper((WorkflowInstanceLink)_workflowInstanceLink.clone());
	}

	@Override
	public int compareTo(WorkflowInstanceLink workflowInstanceLink) {
		return _workflowInstanceLink.compareTo(workflowInstanceLink);
	}

	/**
	* Returns the fully qualified class name of this workflow instance link.
	*
	* @return the fully qualified class name of this workflow instance link
	*/
	@Override
	public String getClassName() {
		return _workflowInstanceLink.getClassName();
	}

	/**
	* Returns the class name ID of this workflow instance link.
	*
	* @return the class name ID of this workflow instance link
	*/
	@Override
	public long getClassNameId() {
		return _workflowInstanceLink.getClassNameId();
	}

	/**
	* Returns the class pk of this workflow instance link.
	*
	* @return the class pk of this workflow instance link
	*/
	@Override
	public long getClassPK() {
		return _workflowInstanceLink.getClassPK();
	}

	/**
	* Returns the company ID of this workflow instance link.
	*
	* @return the company ID of this workflow instance link
	*/
	@Override
	public long getCompanyId() {
		return _workflowInstanceLink.getCompanyId();
	}

	/**
	* Returns the create date of this workflow instance link.
	*
	* @return the create date of this workflow instance link
	*/
	@Override
	public Date getCreateDate() {
		return _workflowInstanceLink.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _workflowInstanceLink.getExpandoBridge();
	}

	/**
	* Returns the group ID of this workflow instance link.
	*
	* @return the group ID of this workflow instance link
	*/
	@Override
	public long getGroupId() {
		return _workflowInstanceLink.getGroupId();
	}

	/**
	* Returns the modified date of this workflow instance link.
	*
	* @return the modified date of this workflow instance link
	*/
	@Override
	public Date getModifiedDate() {
		return _workflowInstanceLink.getModifiedDate();
	}

	/**
	* Returns the mvcc version of this workflow instance link.
	*
	* @return the mvcc version of this workflow instance link
	*/
	@Override
	public long getMvccVersion() {
		return _workflowInstanceLink.getMvccVersion();
	}

	/**
	* Returns the primary key of this workflow instance link.
	*
	* @return the primary key of this workflow instance link
	*/
	@Override
	public long getPrimaryKey() {
		return _workflowInstanceLink.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _workflowInstanceLink.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this workflow instance link.
	*
	* @return the user ID of this workflow instance link
	*/
	@Override
	public long getUserId() {
		return _workflowInstanceLink.getUserId();
	}

	/**
	* Returns the user name of this workflow instance link.
	*
	* @return the user name of this workflow instance link
	*/
	@Override
	public String getUserName() {
		return _workflowInstanceLink.getUserName();
	}

	/**
	* Returns the user uuid of this workflow instance link.
	*
	* @return the user uuid of this workflow instance link
	*/
	@Override
	public String getUserUuid() {
		return _workflowInstanceLink.getUserUuid();
	}

	/**
	* Returns the workflow instance ID of this workflow instance link.
	*
	* @return the workflow instance ID of this workflow instance link
	*/
	@Override
	public long getWorkflowInstanceId() {
		return _workflowInstanceLink.getWorkflowInstanceId();
	}

	/**
	* Returns the workflow instance link ID of this workflow instance link.
	*
	* @return the workflow instance link ID of this workflow instance link
	*/
	@Override
	public long getWorkflowInstanceLinkId() {
		return _workflowInstanceLink.getWorkflowInstanceLinkId();
	}

	@Override
	public int hashCode() {
		return _workflowInstanceLink.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _workflowInstanceLink.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _workflowInstanceLink.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _workflowInstanceLink.isNew();
	}

	@Override
	public void persist() {
		_workflowInstanceLink.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_workflowInstanceLink.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_workflowInstanceLink.setClassName(className);
	}

	/**
	* Sets the class name ID of this workflow instance link.
	*
	* @param classNameId the class name ID of this workflow instance link
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_workflowInstanceLink.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this workflow instance link.
	*
	* @param classPK the class pk of this workflow instance link
	*/
	@Override
	public void setClassPK(long classPK) {
		_workflowInstanceLink.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this workflow instance link.
	*
	* @param companyId the company ID of this workflow instance link
	*/
	@Override
	public void setCompanyId(long companyId) {
		_workflowInstanceLink.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this workflow instance link.
	*
	* @param createDate the create date of this workflow instance link
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_workflowInstanceLink.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_workflowInstanceLink.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_workflowInstanceLink.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_workflowInstanceLink.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this workflow instance link.
	*
	* @param groupId the group ID of this workflow instance link
	*/
	@Override
	public void setGroupId(long groupId) {
		_workflowInstanceLink.setGroupId(groupId);
	}

	/**
	* Sets the modified date of this workflow instance link.
	*
	* @param modifiedDate the modified date of this workflow instance link
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_workflowInstanceLink.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the mvcc version of this workflow instance link.
	*
	* @param mvccVersion the mvcc version of this workflow instance link
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_workflowInstanceLink.setMvccVersion(mvccVersion);
	}

	@Override
	public void setNew(boolean n) {
		_workflowInstanceLink.setNew(n);
	}

	/**
	* Sets the primary key of this workflow instance link.
	*
	* @param primaryKey the primary key of this workflow instance link
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_workflowInstanceLink.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_workflowInstanceLink.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this workflow instance link.
	*
	* @param userId the user ID of this workflow instance link
	*/
	@Override
	public void setUserId(long userId) {
		_workflowInstanceLink.setUserId(userId);
	}

	/**
	* Sets the user name of this workflow instance link.
	*
	* @param userName the user name of this workflow instance link
	*/
	@Override
	public void setUserName(String userName) {
		_workflowInstanceLink.setUserName(userName);
	}

	/**
	* Sets the user uuid of this workflow instance link.
	*
	* @param userUuid the user uuid of this workflow instance link
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_workflowInstanceLink.setUserUuid(userUuid);
	}

	/**
	* Sets the workflow instance ID of this workflow instance link.
	*
	* @param workflowInstanceId the workflow instance ID of this workflow instance link
	*/
	@Override
	public void setWorkflowInstanceId(long workflowInstanceId) {
		_workflowInstanceLink.setWorkflowInstanceId(workflowInstanceId);
	}

	/**
	* Sets the workflow instance link ID of this workflow instance link.
	*
	* @param workflowInstanceLinkId the workflow instance link ID of this workflow instance link
	*/
	@Override
	public void setWorkflowInstanceLinkId(long workflowInstanceLinkId) {
		_workflowInstanceLink.setWorkflowInstanceLinkId(workflowInstanceLinkId);
	}

	@Override
	public CacheModel<WorkflowInstanceLink> toCacheModel() {
		return _workflowInstanceLink.toCacheModel();
	}

	@Override
	public WorkflowInstanceLink toEscapedModel() {
		return new WorkflowInstanceLinkWrapper(_workflowInstanceLink.toEscapedModel());
	}

	@Override
	public String toString() {
		return _workflowInstanceLink.toString();
	}

	@Override
	public WorkflowInstanceLink toUnescapedModel() {
		return new WorkflowInstanceLinkWrapper(_workflowInstanceLink.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _workflowInstanceLink.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof WorkflowInstanceLinkWrapper)) {
			return false;
		}

		WorkflowInstanceLinkWrapper workflowInstanceLinkWrapper = (WorkflowInstanceLinkWrapper)obj;

		if (Objects.equals(_workflowInstanceLink,
					workflowInstanceLinkWrapper._workflowInstanceLink)) {
			return true;
		}

		return false;
	}

	@Override
	public WorkflowInstanceLink getWrappedModel() {
		return _workflowInstanceLink;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _workflowInstanceLink.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _workflowInstanceLink.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_workflowInstanceLink.resetOriginalValues();
	}

	private final WorkflowInstanceLink _workflowInstanceLink;
}