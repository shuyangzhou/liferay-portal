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
 * This class is a wrapper for {@link KaleoTaskAssignment}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoTaskAssignment
 * @generated
 */
@ProviderType
public class KaleoTaskAssignmentWrapper implements KaleoTaskAssignment,
	ModelWrapper<KaleoTaskAssignment> {
	public KaleoTaskAssignmentWrapper(KaleoTaskAssignment kaleoTaskAssignment) {
		_kaleoTaskAssignment = kaleoTaskAssignment;
	}

	@Override
	public Class<?> getModelClass() {
		return KaleoTaskAssignment.class;
	}

	@Override
	public String getModelClassName() {
		return KaleoTaskAssignment.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<KaleoTaskAssignment, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<KaleoTaskAssignment, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<KaleoTaskAssignment, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<KaleoTaskAssignment, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<KaleoTaskAssignment, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<KaleoTaskAssignment, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<KaleoTaskAssignment, Object>> getAttributeGetters() {
		return _kaleoTaskAssignment.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<KaleoTaskAssignment, Object>> getAttributeSetters() {
		return _kaleoTaskAssignment.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new KaleoTaskAssignmentWrapper((KaleoTaskAssignment)_kaleoTaskAssignment.clone());
	}

	@Override
	public int compareTo(KaleoTaskAssignment kaleoTaskAssignment) {
		return _kaleoTaskAssignment.compareTo(kaleoTaskAssignment);
	}

	/**
	* Returns the assignee action ID of this kaleo task assignment.
	*
	* @return the assignee action ID of this kaleo task assignment
	*/
	@Override
	public String getAssigneeActionId() {
		return _kaleoTaskAssignment.getAssigneeActionId();
	}

	/**
	* Returns the assignee class name of this kaleo task assignment.
	*
	* @return the assignee class name of this kaleo task assignment
	*/
	@Override
	public String getAssigneeClassName() {
		return _kaleoTaskAssignment.getAssigneeClassName();
	}

	/**
	* Returns the assignee class pk of this kaleo task assignment.
	*
	* @return the assignee class pk of this kaleo task assignment
	*/
	@Override
	public long getAssigneeClassPK() {
		return _kaleoTaskAssignment.getAssigneeClassPK();
	}

	/**
	* Returns the assignee script of this kaleo task assignment.
	*
	* @return the assignee script of this kaleo task assignment
	*/
	@Override
	public String getAssigneeScript() {
		return _kaleoTaskAssignment.getAssigneeScript();
	}

	/**
	* Returns the assignee script language of this kaleo task assignment.
	*
	* @return the assignee script language of this kaleo task assignment
	*/
	@Override
	public String getAssigneeScriptLanguage() {
		return _kaleoTaskAssignment.getAssigneeScriptLanguage();
	}

	/**
	* Returns the assignee script required contexts of this kaleo task assignment.
	*
	* @return the assignee script required contexts of this kaleo task assignment
	*/
	@Override
	public String getAssigneeScriptRequiredContexts() {
		return _kaleoTaskAssignment.getAssigneeScriptRequiredContexts();
	}

	/**
	* Returns the company ID of this kaleo task assignment.
	*
	* @return the company ID of this kaleo task assignment
	*/
	@Override
	public long getCompanyId() {
		return _kaleoTaskAssignment.getCompanyId();
	}

	/**
	* Returns the create date of this kaleo task assignment.
	*
	* @return the create date of this kaleo task assignment
	*/
	@Override
	public Date getCreateDate() {
		return _kaleoTaskAssignment.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _kaleoTaskAssignment.getExpandoBridge();
	}

	/**
	* Returns the group ID of this kaleo task assignment.
	*
	* @return the group ID of this kaleo task assignment
	*/
	@Override
	public long getGroupId() {
		return _kaleoTaskAssignment.getGroupId();
	}

	/**
	* Returns the kaleo class name of this kaleo task assignment.
	*
	* @return the kaleo class name of this kaleo task assignment
	*/
	@Override
	public String getKaleoClassName() {
		return _kaleoTaskAssignment.getKaleoClassName();
	}

	/**
	* Returns the kaleo class pk of this kaleo task assignment.
	*
	* @return the kaleo class pk of this kaleo task assignment
	*/
	@Override
	public long getKaleoClassPK() {
		return _kaleoTaskAssignment.getKaleoClassPK();
	}

	/**
	* Returns the kaleo definition version ID of this kaleo task assignment.
	*
	* @return the kaleo definition version ID of this kaleo task assignment
	*/
	@Override
	public long getKaleoDefinitionVersionId() {
		return _kaleoTaskAssignment.getKaleoDefinitionVersionId();
	}

	/**
	* Returns the kaleo node ID of this kaleo task assignment.
	*
	* @return the kaleo node ID of this kaleo task assignment
	*/
	@Override
	public long getKaleoNodeId() {
		return _kaleoTaskAssignment.getKaleoNodeId();
	}

	/**
	* Returns the kaleo task assignment ID of this kaleo task assignment.
	*
	* @return the kaleo task assignment ID of this kaleo task assignment
	*/
	@Override
	public long getKaleoTaskAssignmentId() {
		return _kaleoTaskAssignment.getKaleoTaskAssignmentId();
	}

	/**
	* Returns the modified date of this kaleo task assignment.
	*
	* @return the modified date of this kaleo task assignment
	*/
	@Override
	public Date getModifiedDate() {
		return _kaleoTaskAssignment.getModifiedDate();
	}

	/**
	* Returns the primary key of this kaleo task assignment.
	*
	* @return the primary key of this kaleo task assignment
	*/
	@Override
	public long getPrimaryKey() {
		return _kaleoTaskAssignment.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kaleoTaskAssignment.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this kaleo task assignment.
	*
	* @return the user ID of this kaleo task assignment
	*/
	@Override
	public long getUserId() {
		return _kaleoTaskAssignment.getUserId();
	}

	/**
	* Returns the user name of this kaleo task assignment.
	*
	* @return the user name of this kaleo task assignment
	*/
	@Override
	public String getUserName() {
		return _kaleoTaskAssignment.getUserName();
	}

	/**
	* Returns the user uuid of this kaleo task assignment.
	*
	* @return the user uuid of this kaleo task assignment
	*/
	@Override
	public String getUserUuid() {
		return _kaleoTaskAssignment.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _kaleoTaskAssignment.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _kaleoTaskAssignment.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _kaleoTaskAssignment.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _kaleoTaskAssignment.isNew();
	}

	@Override
	public void persist() {
		_kaleoTaskAssignment.persist();
	}

	/**
	* Sets the assignee action ID of this kaleo task assignment.
	*
	* @param assigneeActionId the assignee action ID of this kaleo task assignment
	*/
	@Override
	public void setAssigneeActionId(String assigneeActionId) {
		_kaleoTaskAssignment.setAssigneeActionId(assigneeActionId);
	}

	/**
	* Sets the assignee class name of this kaleo task assignment.
	*
	* @param assigneeClassName the assignee class name of this kaleo task assignment
	*/
	@Override
	public void setAssigneeClassName(String assigneeClassName) {
		_kaleoTaskAssignment.setAssigneeClassName(assigneeClassName);
	}

	/**
	* Sets the assignee class pk of this kaleo task assignment.
	*
	* @param assigneeClassPK the assignee class pk of this kaleo task assignment
	*/
	@Override
	public void setAssigneeClassPK(long assigneeClassPK) {
		_kaleoTaskAssignment.setAssigneeClassPK(assigneeClassPK);
	}

	/**
	* Sets the assignee script of this kaleo task assignment.
	*
	* @param assigneeScript the assignee script of this kaleo task assignment
	*/
	@Override
	public void setAssigneeScript(String assigneeScript) {
		_kaleoTaskAssignment.setAssigneeScript(assigneeScript);
	}

	/**
	* Sets the assignee script language of this kaleo task assignment.
	*
	* @param assigneeScriptLanguage the assignee script language of this kaleo task assignment
	*/
	@Override
	public void setAssigneeScriptLanguage(String assigneeScriptLanguage) {
		_kaleoTaskAssignment.setAssigneeScriptLanguage(assigneeScriptLanguage);
	}

	/**
	* Sets the assignee script required contexts of this kaleo task assignment.
	*
	* @param assigneeScriptRequiredContexts the assignee script required contexts of this kaleo task assignment
	*/
	@Override
	public void setAssigneeScriptRequiredContexts(
		String assigneeScriptRequiredContexts) {
		_kaleoTaskAssignment.setAssigneeScriptRequiredContexts(assigneeScriptRequiredContexts);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_kaleoTaskAssignment.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this kaleo task assignment.
	*
	* @param companyId the company ID of this kaleo task assignment
	*/
	@Override
	public void setCompanyId(long companyId) {
		_kaleoTaskAssignment.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this kaleo task assignment.
	*
	* @param createDate the create date of this kaleo task assignment
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_kaleoTaskAssignment.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_kaleoTaskAssignment.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_kaleoTaskAssignment.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_kaleoTaskAssignment.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this kaleo task assignment.
	*
	* @param groupId the group ID of this kaleo task assignment
	*/
	@Override
	public void setGroupId(long groupId) {
		_kaleoTaskAssignment.setGroupId(groupId);
	}

	/**
	* Sets the kaleo class name of this kaleo task assignment.
	*
	* @param kaleoClassName the kaleo class name of this kaleo task assignment
	*/
	@Override
	public void setKaleoClassName(String kaleoClassName) {
		_kaleoTaskAssignment.setKaleoClassName(kaleoClassName);
	}

	/**
	* Sets the kaleo class pk of this kaleo task assignment.
	*
	* @param kaleoClassPK the kaleo class pk of this kaleo task assignment
	*/
	@Override
	public void setKaleoClassPK(long kaleoClassPK) {
		_kaleoTaskAssignment.setKaleoClassPK(kaleoClassPK);
	}

	/**
	* Sets the kaleo definition version ID of this kaleo task assignment.
	*
	* @param kaleoDefinitionVersionId the kaleo definition version ID of this kaleo task assignment
	*/
	@Override
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId) {
		_kaleoTaskAssignment.setKaleoDefinitionVersionId(kaleoDefinitionVersionId);
	}

	/**
	* Sets the kaleo node ID of this kaleo task assignment.
	*
	* @param kaleoNodeId the kaleo node ID of this kaleo task assignment
	*/
	@Override
	public void setKaleoNodeId(long kaleoNodeId) {
		_kaleoTaskAssignment.setKaleoNodeId(kaleoNodeId);
	}

	/**
	* Sets the kaleo task assignment ID of this kaleo task assignment.
	*
	* @param kaleoTaskAssignmentId the kaleo task assignment ID of this kaleo task assignment
	*/
	@Override
	public void setKaleoTaskAssignmentId(long kaleoTaskAssignmentId) {
		_kaleoTaskAssignment.setKaleoTaskAssignmentId(kaleoTaskAssignmentId);
	}

	/**
	* Sets the modified date of this kaleo task assignment.
	*
	* @param modifiedDate the modified date of this kaleo task assignment
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_kaleoTaskAssignment.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_kaleoTaskAssignment.setNew(n);
	}

	/**
	* Sets the primary key of this kaleo task assignment.
	*
	* @param primaryKey the primary key of this kaleo task assignment
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_kaleoTaskAssignment.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_kaleoTaskAssignment.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this kaleo task assignment.
	*
	* @param userId the user ID of this kaleo task assignment
	*/
	@Override
	public void setUserId(long userId) {
		_kaleoTaskAssignment.setUserId(userId);
	}

	/**
	* Sets the user name of this kaleo task assignment.
	*
	* @param userName the user name of this kaleo task assignment
	*/
	@Override
	public void setUserName(String userName) {
		_kaleoTaskAssignment.setUserName(userName);
	}

	/**
	* Sets the user uuid of this kaleo task assignment.
	*
	* @param userUuid the user uuid of this kaleo task assignment
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_kaleoTaskAssignment.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<KaleoTaskAssignment> toCacheModel() {
		return _kaleoTaskAssignment.toCacheModel();
	}

	@Override
	public KaleoTaskAssignment toEscapedModel() {
		return new KaleoTaskAssignmentWrapper(_kaleoTaskAssignment.toEscapedModel());
	}

	@Override
	public String toString() {
		return _kaleoTaskAssignment.toString();
	}

	@Override
	public KaleoTaskAssignment toUnescapedModel() {
		return new KaleoTaskAssignmentWrapper(_kaleoTaskAssignment.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _kaleoTaskAssignment.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof KaleoTaskAssignmentWrapper)) {
			return false;
		}

		KaleoTaskAssignmentWrapper kaleoTaskAssignmentWrapper = (KaleoTaskAssignmentWrapper)obj;

		if (Objects.equals(_kaleoTaskAssignment,
					kaleoTaskAssignmentWrapper._kaleoTaskAssignment)) {
			return true;
		}

		return false;
	}

	@Override
	public KaleoTaskAssignment getWrappedModel() {
		return _kaleoTaskAssignment;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _kaleoTaskAssignment.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _kaleoTaskAssignment.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_kaleoTaskAssignment.resetOriginalValues();
	}

	private final KaleoTaskAssignment _kaleoTaskAssignment;
}