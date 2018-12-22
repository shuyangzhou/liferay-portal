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
 * This class is a wrapper for {@link WorkflowDefinitionLink}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see WorkflowDefinitionLink
 * @generated
 */
@ProviderType
public class WorkflowDefinitionLinkWrapper implements WorkflowDefinitionLink,
	ModelWrapper<WorkflowDefinitionLink> {
	public WorkflowDefinitionLinkWrapper(
		WorkflowDefinitionLink workflowDefinitionLink) {
		_workflowDefinitionLink = workflowDefinitionLink;
	}

	@Override
	public Class<?> getModelClass() {
		return WorkflowDefinitionLink.class;
	}

	@Override
	public String getModelClassName() {
		return WorkflowDefinitionLink.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<WorkflowDefinitionLink, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<WorkflowDefinitionLink, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<WorkflowDefinitionLink, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<WorkflowDefinitionLink, Object>> attributeSetters =
			getAttributeSetters();

		for (Map.Entry<String, BiConsumer<WorkflowDefinitionLink, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<WorkflowDefinitionLink, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<WorkflowDefinitionLink, Object>> getAttributeGetters() {
		return _workflowDefinitionLink.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<WorkflowDefinitionLink, Object>> getAttributeSetters() {
		return _workflowDefinitionLink.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new WorkflowDefinitionLinkWrapper((WorkflowDefinitionLink)_workflowDefinitionLink.clone());
	}

	@Override
	public int compareTo(WorkflowDefinitionLink workflowDefinitionLink) {
		return _workflowDefinitionLink.compareTo(workflowDefinitionLink);
	}

	/**
	* Returns the fully qualified class name of this workflow definition link.
	*
	* @return the fully qualified class name of this workflow definition link
	*/
	@Override
	public String getClassName() {
		return _workflowDefinitionLink.getClassName();
	}

	/**
	* Returns the class name ID of this workflow definition link.
	*
	* @return the class name ID of this workflow definition link
	*/
	@Override
	public long getClassNameId() {
		return _workflowDefinitionLink.getClassNameId();
	}

	/**
	* Returns the class pk of this workflow definition link.
	*
	* @return the class pk of this workflow definition link
	*/
	@Override
	public long getClassPK() {
		return _workflowDefinitionLink.getClassPK();
	}

	/**
	* Returns the company ID of this workflow definition link.
	*
	* @return the company ID of this workflow definition link
	*/
	@Override
	public long getCompanyId() {
		return _workflowDefinitionLink.getCompanyId();
	}

	/**
	* Returns the create date of this workflow definition link.
	*
	* @return the create date of this workflow definition link
	*/
	@Override
	public Date getCreateDate() {
		return _workflowDefinitionLink.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _workflowDefinitionLink.getExpandoBridge();
	}

	/**
	* Returns the group ID of this workflow definition link.
	*
	* @return the group ID of this workflow definition link
	*/
	@Override
	public long getGroupId() {
		return _workflowDefinitionLink.getGroupId();
	}

	/**
	* Returns the modified date of this workflow definition link.
	*
	* @return the modified date of this workflow definition link
	*/
	@Override
	public Date getModifiedDate() {
		return _workflowDefinitionLink.getModifiedDate();
	}

	/**
	* Returns the mvcc version of this workflow definition link.
	*
	* @return the mvcc version of this workflow definition link
	*/
	@Override
	public long getMvccVersion() {
		return _workflowDefinitionLink.getMvccVersion();
	}

	/**
	* Returns the primary key of this workflow definition link.
	*
	* @return the primary key of this workflow definition link
	*/
	@Override
	public long getPrimaryKey() {
		return _workflowDefinitionLink.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _workflowDefinitionLink.getPrimaryKeyObj();
	}

	/**
	* Returns the type pk of this workflow definition link.
	*
	* @return the type pk of this workflow definition link
	*/
	@Override
	public long getTypePK() {
		return _workflowDefinitionLink.getTypePK();
	}

	/**
	* Returns the user ID of this workflow definition link.
	*
	* @return the user ID of this workflow definition link
	*/
	@Override
	public long getUserId() {
		return _workflowDefinitionLink.getUserId();
	}

	/**
	* Returns the user name of this workflow definition link.
	*
	* @return the user name of this workflow definition link
	*/
	@Override
	public String getUserName() {
		return _workflowDefinitionLink.getUserName();
	}

	/**
	* Returns the user uuid of this workflow definition link.
	*
	* @return the user uuid of this workflow definition link
	*/
	@Override
	public String getUserUuid() {
		return _workflowDefinitionLink.getUserUuid();
	}

	/**
	* Returns the workflow definition link ID of this workflow definition link.
	*
	* @return the workflow definition link ID of this workflow definition link
	*/
	@Override
	public long getWorkflowDefinitionLinkId() {
		return _workflowDefinitionLink.getWorkflowDefinitionLinkId();
	}

	/**
	* Returns the workflow definition name of this workflow definition link.
	*
	* @return the workflow definition name of this workflow definition link
	*/
	@Override
	public String getWorkflowDefinitionName() {
		return _workflowDefinitionLink.getWorkflowDefinitionName();
	}

	/**
	* Returns the workflow definition version of this workflow definition link.
	*
	* @return the workflow definition version of this workflow definition link
	*/
	@Override
	public int getWorkflowDefinitionVersion() {
		return _workflowDefinitionLink.getWorkflowDefinitionVersion();
	}

	@Override
	public int hashCode() {
		return _workflowDefinitionLink.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _workflowDefinitionLink.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _workflowDefinitionLink.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _workflowDefinitionLink.isNew();
	}

	@Override
	public void persist() {
		_workflowDefinitionLink.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_workflowDefinitionLink.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_workflowDefinitionLink.setClassName(className);
	}

	/**
	* Sets the class name ID of this workflow definition link.
	*
	* @param classNameId the class name ID of this workflow definition link
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_workflowDefinitionLink.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this workflow definition link.
	*
	* @param classPK the class pk of this workflow definition link
	*/
	@Override
	public void setClassPK(long classPK) {
		_workflowDefinitionLink.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this workflow definition link.
	*
	* @param companyId the company ID of this workflow definition link
	*/
	@Override
	public void setCompanyId(long companyId) {
		_workflowDefinitionLink.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this workflow definition link.
	*
	* @param createDate the create date of this workflow definition link
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_workflowDefinitionLink.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_workflowDefinitionLink.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_workflowDefinitionLink.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_workflowDefinitionLink.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this workflow definition link.
	*
	* @param groupId the group ID of this workflow definition link
	*/
	@Override
	public void setGroupId(long groupId) {
		_workflowDefinitionLink.setGroupId(groupId);
	}

	/**
	* Sets the modified date of this workflow definition link.
	*
	* @param modifiedDate the modified date of this workflow definition link
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_workflowDefinitionLink.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the mvcc version of this workflow definition link.
	*
	* @param mvccVersion the mvcc version of this workflow definition link
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_workflowDefinitionLink.setMvccVersion(mvccVersion);
	}

	@Override
	public void setNew(boolean n) {
		_workflowDefinitionLink.setNew(n);
	}

	/**
	* Sets the primary key of this workflow definition link.
	*
	* @param primaryKey the primary key of this workflow definition link
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_workflowDefinitionLink.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_workflowDefinitionLink.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the type pk of this workflow definition link.
	*
	* @param typePK the type pk of this workflow definition link
	*/
	@Override
	public void setTypePK(long typePK) {
		_workflowDefinitionLink.setTypePK(typePK);
	}

	/**
	* Sets the user ID of this workflow definition link.
	*
	* @param userId the user ID of this workflow definition link
	*/
	@Override
	public void setUserId(long userId) {
		_workflowDefinitionLink.setUserId(userId);
	}

	/**
	* Sets the user name of this workflow definition link.
	*
	* @param userName the user name of this workflow definition link
	*/
	@Override
	public void setUserName(String userName) {
		_workflowDefinitionLink.setUserName(userName);
	}

	/**
	* Sets the user uuid of this workflow definition link.
	*
	* @param userUuid the user uuid of this workflow definition link
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_workflowDefinitionLink.setUserUuid(userUuid);
	}

	/**
	* Sets the workflow definition link ID of this workflow definition link.
	*
	* @param workflowDefinitionLinkId the workflow definition link ID of this workflow definition link
	*/
	@Override
	public void setWorkflowDefinitionLinkId(long workflowDefinitionLinkId) {
		_workflowDefinitionLink.setWorkflowDefinitionLinkId(workflowDefinitionLinkId);
	}

	/**
	* Sets the workflow definition name of this workflow definition link.
	*
	* @param workflowDefinitionName the workflow definition name of this workflow definition link
	*/
	@Override
	public void setWorkflowDefinitionName(String workflowDefinitionName) {
		_workflowDefinitionLink.setWorkflowDefinitionName(workflowDefinitionName);
	}

	/**
	* Sets the workflow definition version of this workflow definition link.
	*
	* @param workflowDefinitionVersion the workflow definition version of this workflow definition link
	*/
	@Override
	public void setWorkflowDefinitionVersion(int workflowDefinitionVersion) {
		_workflowDefinitionLink.setWorkflowDefinitionVersion(workflowDefinitionVersion);
	}

	@Override
	public CacheModel<WorkflowDefinitionLink> toCacheModel() {
		return _workflowDefinitionLink.toCacheModel();
	}

	@Override
	public WorkflowDefinitionLink toEscapedModel() {
		return new WorkflowDefinitionLinkWrapper(_workflowDefinitionLink.toEscapedModel());
	}

	@Override
	public String toString() {
		return _workflowDefinitionLink.toString();
	}

	@Override
	public WorkflowDefinitionLink toUnescapedModel() {
		return new WorkflowDefinitionLinkWrapper(_workflowDefinitionLink.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _workflowDefinitionLink.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof WorkflowDefinitionLinkWrapper)) {
			return false;
		}

		WorkflowDefinitionLinkWrapper workflowDefinitionLinkWrapper = (WorkflowDefinitionLinkWrapper)obj;

		if (Objects.equals(_workflowDefinitionLink,
					workflowDefinitionLinkWrapper._workflowDefinitionLink)) {
			return true;
		}

		return false;
	}

	@Override
	public WorkflowDefinitionLink getWrappedModel() {
		return _workflowDefinitionLink;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _workflowDefinitionLink.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _workflowDefinitionLink.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_workflowDefinitionLink.resetOriginalValues();
	}

	private final WorkflowDefinitionLink _workflowDefinitionLink;
}