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
 * This class is a wrapper for {@link KaleoLog}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoLog
 * @generated
 */
@ProviderType
public class KaleoLogWrapper implements KaleoLog, ModelWrapper<KaleoLog> {
	public KaleoLogWrapper(KaleoLog kaleoLog) {
		_kaleoLog = kaleoLog;
	}

	@Override
	public Class<?> getModelClass() {
		return KaleoLog.class;
	}

	@Override
	public String getModelClassName() {
		return KaleoLog.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<KaleoLog, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<KaleoLog, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<KaleoLog, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<KaleoLog, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<KaleoLog, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<KaleoLog, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<KaleoLog, Object>> getAttributeGetters() {
		return _kaleoLog.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<KaleoLog, Object>> getAttributeSetters() {
		return _kaleoLog.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new KaleoLogWrapper((KaleoLog)_kaleoLog.clone());
	}

	@Override
	public int compareTo(KaleoLog kaleoLog) {
		return _kaleoLog.compareTo(kaleoLog);
	}

	/**
	* Returns the comment of this kaleo log.
	*
	* @return the comment of this kaleo log
	*/
	@Override
	public String getComment() {
		return _kaleoLog.getComment();
	}

	/**
	* Returns the company ID of this kaleo log.
	*
	* @return the company ID of this kaleo log
	*/
	@Override
	public long getCompanyId() {
		return _kaleoLog.getCompanyId();
	}

	/**
	* Returns the create date of this kaleo log.
	*
	* @return the create date of this kaleo log
	*/
	@Override
	public Date getCreateDate() {
		return _kaleoLog.getCreateDate();
	}

	/**
	* Returns the current assignee class name of this kaleo log.
	*
	* @return the current assignee class name of this kaleo log
	*/
	@Override
	public String getCurrentAssigneeClassName() {
		return _kaleoLog.getCurrentAssigneeClassName();
	}

	/**
	* Returns the current assignee class pk of this kaleo log.
	*
	* @return the current assignee class pk of this kaleo log
	*/
	@Override
	public long getCurrentAssigneeClassPK() {
		return _kaleoLog.getCurrentAssigneeClassPK();
	}

	/**
	* Returns the duration of this kaleo log.
	*
	* @return the duration of this kaleo log
	*/
	@Override
	public long getDuration() {
		return _kaleoLog.getDuration();
	}

	/**
	* Returns the end date of this kaleo log.
	*
	* @return the end date of this kaleo log
	*/
	@Override
	public Date getEndDate() {
		return _kaleoLog.getEndDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _kaleoLog.getExpandoBridge();
	}

	/**
	* Returns the group ID of this kaleo log.
	*
	* @return the group ID of this kaleo log
	*/
	@Override
	public long getGroupId() {
		return _kaleoLog.getGroupId();
	}

	/**
	* Returns the kaleo action description of this kaleo log.
	*
	* @return the kaleo action description of this kaleo log
	*/
	@Override
	public String getKaleoActionDescription() {
		return _kaleoLog.getKaleoActionDescription();
	}

	/**
	* Returns the kaleo action ID of this kaleo log.
	*
	* @return the kaleo action ID of this kaleo log
	*/
	@Override
	public long getKaleoActionId() {
		return _kaleoLog.getKaleoActionId();
	}

	/**
	* Returns the kaleo action name of this kaleo log.
	*
	* @return the kaleo action name of this kaleo log
	*/
	@Override
	public String getKaleoActionName() {
		return _kaleoLog.getKaleoActionName();
	}

	/**
	* Returns the kaleo class name of this kaleo log.
	*
	* @return the kaleo class name of this kaleo log
	*/
	@Override
	public String getKaleoClassName() {
		return _kaleoLog.getKaleoClassName();
	}

	/**
	* Returns the kaleo class pk of this kaleo log.
	*
	* @return the kaleo class pk of this kaleo log
	*/
	@Override
	public long getKaleoClassPK() {
		return _kaleoLog.getKaleoClassPK();
	}

	/**
	* Returns the kaleo definition version ID of this kaleo log.
	*
	* @return the kaleo definition version ID of this kaleo log
	*/
	@Override
	public long getKaleoDefinitionVersionId() {
		return _kaleoLog.getKaleoDefinitionVersionId();
	}

	/**
	* Returns the kaleo instance ID of this kaleo log.
	*
	* @return the kaleo instance ID of this kaleo log
	*/
	@Override
	public long getKaleoInstanceId() {
		return _kaleoLog.getKaleoInstanceId();
	}

	/**
	* Returns the kaleo instance token ID of this kaleo log.
	*
	* @return the kaleo instance token ID of this kaleo log
	*/
	@Override
	public long getKaleoInstanceTokenId() {
		return _kaleoLog.getKaleoInstanceTokenId();
	}

	/**
	* Returns the kaleo log ID of this kaleo log.
	*
	* @return the kaleo log ID of this kaleo log
	*/
	@Override
	public long getKaleoLogId() {
		return _kaleoLog.getKaleoLogId();
	}

	/**
	* Returns the kaleo node name of this kaleo log.
	*
	* @return the kaleo node name of this kaleo log
	*/
	@Override
	public String getKaleoNodeName() {
		return _kaleoLog.getKaleoNodeName();
	}

	/**
	* Returns the kaleo task instance token ID of this kaleo log.
	*
	* @return the kaleo task instance token ID of this kaleo log
	*/
	@Override
	public long getKaleoTaskInstanceTokenId() {
		return _kaleoLog.getKaleoTaskInstanceTokenId();
	}

	/**
	* Returns the modified date of this kaleo log.
	*
	* @return the modified date of this kaleo log
	*/
	@Override
	public Date getModifiedDate() {
		return _kaleoLog.getModifiedDate();
	}

	/**
	* Returns the previous assignee class name of this kaleo log.
	*
	* @return the previous assignee class name of this kaleo log
	*/
	@Override
	public String getPreviousAssigneeClassName() {
		return _kaleoLog.getPreviousAssigneeClassName();
	}

	/**
	* Returns the previous assignee class pk of this kaleo log.
	*
	* @return the previous assignee class pk of this kaleo log
	*/
	@Override
	public long getPreviousAssigneeClassPK() {
		return _kaleoLog.getPreviousAssigneeClassPK();
	}

	/**
	* Returns the previous kaleo node ID of this kaleo log.
	*
	* @return the previous kaleo node ID of this kaleo log
	*/
	@Override
	public long getPreviousKaleoNodeId() {
		return _kaleoLog.getPreviousKaleoNodeId();
	}

	/**
	* Returns the previous kaleo node name of this kaleo log.
	*
	* @return the previous kaleo node name of this kaleo log
	*/
	@Override
	public String getPreviousKaleoNodeName() {
		return _kaleoLog.getPreviousKaleoNodeName();
	}

	/**
	* Returns the primary key of this kaleo log.
	*
	* @return the primary key of this kaleo log
	*/
	@Override
	public long getPrimaryKey() {
		return _kaleoLog.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kaleoLog.getPrimaryKeyObj();
	}

	/**
	* Returns the start date of this kaleo log.
	*
	* @return the start date of this kaleo log
	*/
	@Override
	public Date getStartDate() {
		return _kaleoLog.getStartDate();
	}

	/**
	* Returns the terminal kaleo node of this kaleo log.
	*
	* @return the terminal kaleo node of this kaleo log
	*/
	@Override
	public boolean getTerminalKaleoNode() {
		return _kaleoLog.getTerminalKaleoNode();
	}

	/**
	* Returns the type of this kaleo log.
	*
	* @return the type of this kaleo log
	*/
	@Override
	public String getType() {
		return _kaleoLog.getType();
	}

	/**
	* Returns the user ID of this kaleo log.
	*
	* @return the user ID of this kaleo log
	*/
	@Override
	public long getUserId() {
		return _kaleoLog.getUserId();
	}

	/**
	* Returns the user name of this kaleo log.
	*
	* @return the user name of this kaleo log
	*/
	@Override
	public String getUserName() {
		return _kaleoLog.getUserName();
	}

	/**
	* Returns the user uuid of this kaleo log.
	*
	* @return the user uuid of this kaleo log
	*/
	@Override
	public String getUserUuid() {
		return _kaleoLog.getUserUuid();
	}

	/**
	* Returns the workflow context of this kaleo log.
	*
	* @return the workflow context of this kaleo log
	*/
	@Override
	public String getWorkflowContext() {
		return _kaleoLog.getWorkflowContext();
	}

	@Override
	public int hashCode() {
		return _kaleoLog.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _kaleoLog.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _kaleoLog.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _kaleoLog.isNew();
	}

	/**
	* Returns <code>true</code> if this kaleo log is terminal kaleo node.
	*
	* @return <code>true</code> if this kaleo log is terminal kaleo node; <code>false</code> otherwise
	*/
	@Override
	public boolean isTerminalKaleoNode() {
		return _kaleoLog.isTerminalKaleoNode();
	}

	@Override
	public void persist() {
		_kaleoLog.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_kaleoLog.setCachedModel(cachedModel);
	}

	/**
	* Sets the comment of this kaleo log.
	*
	* @param comment the comment of this kaleo log
	*/
	@Override
	public void setComment(String comment) {
		_kaleoLog.setComment(comment);
	}

	/**
	* Sets the company ID of this kaleo log.
	*
	* @param companyId the company ID of this kaleo log
	*/
	@Override
	public void setCompanyId(long companyId) {
		_kaleoLog.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this kaleo log.
	*
	* @param createDate the create date of this kaleo log
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_kaleoLog.setCreateDate(createDate);
	}

	/**
	* Sets the current assignee class name of this kaleo log.
	*
	* @param currentAssigneeClassName the current assignee class name of this kaleo log
	*/
	@Override
	public void setCurrentAssigneeClassName(String currentAssigneeClassName) {
		_kaleoLog.setCurrentAssigneeClassName(currentAssigneeClassName);
	}

	/**
	* Sets the current assignee class pk of this kaleo log.
	*
	* @param currentAssigneeClassPK the current assignee class pk of this kaleo log
	*/
	@Override
	public void setCurrentAssigneeClassPK(long currentAssigneeClassPK) {
		_kaleoLog.setCurrentAssigneeClassPK(currentAssigneeClassPK);
	}

	/**
	* Sets the duration of this kaleo log.
	*
	* @param duration the duration of this kaleo log
	*/
	@Override
	public void setDuration(long duration) {
		_kaleoLog.setDuration(duration);
	}

	/**
	* Sets the end date of this kaleo log.
	*
	* @param endDate the end date of this kaleo log
	*/
	@Override
	public void setEndDate(Date endDate) {
		_kaleoLog.setEndDate(endDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_kaleoLog.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_kaleoLog.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_kaleoLog.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this kaleo log.
	*
	* @param groupId the group ID of this kaleo log
	*/
	@Override
	public void setGroupId(long groupId) {
		_kaleoLog.setGroupId(groupId);
	}

	/**
	* Sets the kaleo action description of this kaleo log.
	*
	* @param kaleoActionDescription the kaleo action description of this kaleo log
	*/
	@Override
	public void setKaleoActionDescription(String kaleoActionDescription) {
		_kaleoLog.setKaleoActionDescription(kaleoActionDescription);
	}

	/**
	* Sets the kaleo action ID of this kaleo log.
	*
	* @param kaleoActionId the kaleo action ID of this kaleo log
	*/
	@Override
	public void setKaleoActionId(long kaleoActionId) {
		_kaleoLog.setKaleoActionId(kaleoActionId);
	}

	/**
	* Sets the kaleo action name of this kaleo log.
	*
	* @param kaleoActionName the kaleo action name of this kaleo log
	*/
	@Override
	public void setKaleoActionName(String kaleoActionName) {
		_kaleoLog.setKaleoActionName(kaleoActionName);
	}

	/**
	* Sets the kaleo class name of this kaleo log.
	*
	* @param kaleoClassName the kaleo class name of this kaleo log
	*/
	@Override
	public void setKaleoClassName(String kaleoClassName) {
		_kaleoLog.setKaleoClassName(kaleoClassName);
	}

	/**
	* Sets the kaleo class pk of this kaleo log.
	*
	* @param kaleoClassPK the kaleo class pk of this kaleo log
	*/
	@Override
	public void setKaleoClassPK(long kaleoClassPK) {
		_kaleoLog.setKaleoClassPK(kaleoClassPK);
	}

	/**
	* Sets the kaleo definition version ID of this kaleo log.
	*
	* @param kaleoDefinitionVersionId the kaleo definition version ID of this kaleo log
	*/
	@Override
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId) {
		_kaleoLog.setKaleoDefinitionVersionId(kaleoDefinitionVersionId);
	}

	/**
	* Sets the kaleo instance ID of this kaleo log.
	*
	* @param kaleoInstanceId the kaleo instance ID of this kaleo log
	*/
	@Override
	public void setKaleoInstanceId(long kaleoInstanceId) {
		_kaleoLog.setKaleoInstanceId(kaleoInstanceId);
	}

	/**
	* Sets the kaleo instance token ID of this kaleo log.
	*
	* @param kaleoInstanceTokenId the kaleo instance token ID of this kaleo log
	*/
	@Override
	public void setKaleoInstanceTokenId(long kaleoInstanceTokenId) {
		_kaleoLog.setKaleoInstanceTokenId(kaleoInstanceTokenId);
	}

	/**
	* Sets the kaleo log ID of this kaleo log.
	*
	* @param kaleoLogId the kaleo log ID of this kaleo log
	*/
	@Override
	public void setKaleoLogId(long kaleoLogId) {
		_kaleoLog.setKaleoLogId(kaleoLogId);
	}

	/**
	* Sets the kaleo node name of this kaleo log.
	*
	* @param kaleoNodeName the kaleo node name of this kaleo log
	*/
	@Override
	public void setKaleoNodeName(String kaleoNodeName) {
		_kaleoLog.setKaleoNodeName(kaleoNodeName);
	}

	/**
	* Sets the kaleo task instance token ID of this kaleo log.
	*
	* @param kaleoTaskInstanceTokenId the kaleo task instance token ID of this kaleo log
	*/
	@Override
	public void setKaleoTaskInstanceTokenId(long kaleoTaskInstanceTokenId) {
		_kaleoLog.setKaleoTaskInstanceTokenId(kaleoTaskInstanceTokenId);
	}

	/**
	* Sets the modified date of this kaleo log.
	*
	* @param modifiedDate the modified date of this kaleo log
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_kaleoLog.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_kaleoLog.setNew(n);
	}

	/**
	* Sets the previous assignee class name of this kaleo log.
	*
	* @param previousAssigneeClassName the previous assignee class name of this kaleo log
	*/
	@Override
	public void setPreviousAssigneeClassName(String previousAssigneeClassName) {
		_kaleoLog.setPreviousAssigneeClassName(previousAssigneeClassName);
	}

	/**
	* Sets the previous assignee class pk of this kaleo log.
	*
	* @param previousAssigneeClassPK the previous assignee class pk of this kaleo log
	*/
	@Override
	public void setPreviousAssigneeClassPK(long previousAssigneeClassPK) {
		_kaleoLog.setPreviousAssigneeClassPK(previousAssigneeClassPK);
	}

	/**
	* Sets the previous kaleo node ID of this kaleo log.
	*
	* @param previousKaleoNodeId the previous kaleo node ID of this kaleo log
	*/
	@Override
	public void setPreviousKaleoNodeId(long previousKaleoNodeId) {
		_kaleoLog.setPreviousKaleoNodeId(previousKaleoNodeId);
	}

	/**
	* Sets the previous kaleo node name of this kaleo log.
	*
	* @param previousKaleoNodeName the previous kaleo node name of this kaleo log
	*/
	@Override
	public void setPreviousKaleoNodeName(String previousKaleoNodeName) {
		_kaleoLog.setPreviousKaleoNodeName(previousKaleoNodeName);
	}

	/**
	* Sets the primary key of this kaleo log.
	*
	* @param primaryKey the primary key of this kaleo log
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_kaleoLog.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_kaleoLog.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the start date of this kaleo log.
	*
	* @param startDate the start date of this kaleo log
	*/
	@Override
	public void setStartDate(Date startDate) {
		_kaleoLog.setStartDate(startDate);
	}

	/**
	* Sets whether this kaleo log is terminal kaleo node.
	*
	* @param terminalKaleoNode the terminal kaleo node of this kaleo log
	*/
	@Override
	public void setTerminalKaleoNode(boolean terminalKaleoNode) {
		_kaleoLog.setTerminalKaleoNode(terminalKaleoNode);
	}

	/**
	* Sets the type of this kaleo log.
	*
	* @param type the type of this kaleo log
	*/
	@Override
	public void setType(String type) {
		_kaleoLog.setType(type);
	}

	/**
	* Sets the user ID of this kaleo log.
	*
	* @param userId the user ID of this kaleo log
	*/
	@Override
	public void setUserId(long userId) {
		_kaleoLog.setUserId(userId);
	}

	/**
	* Sets the user name of this kaleo log.
	*
	* @param userName the user name of this kaleo log
	*/
	@Override
	public void setUserName(String userName) {
		_kaleoLog.setUserName(userName);
	}

	/**
	* Sets the user uuid of this kaleo log.
	*
	* @param userUuid the user uuid of this kaleo log
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_kaleoLog.setUserUuid(userUuid);
	}

	/**
	* Sets the workflow context of this kaleo log.
	*
	* @param workflowContext the workflow context of this kaleo log
	*/
	@Override
	public void setWorkflowContext(String workflowContext) {
		_kaleoLog.setWorkflowContext(workflowContext);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<KaleoLog> toCacheModel() {
		return _kaleoLog.toCacheModel();
	}

	@Override
	public KaleoLog toEscapedModel() {
		return new KaleoLogWrapper(_kaleoLog.toEscapedModel());
	}

	@Override
	public String toString() {
		return _kaleoLog.toString();
	}

	@Override
	public KaleoLog toUnescapedModel() {
		return new KaleoLogWrapper(_kaleoLog.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _kaleoLog.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof KaleoLogWrapper)) {
			return false;
		}

		KaleoLogWrapper kaleoLogWrapper = (KaleoLogWrapper)obj;

		if (Objects.equals(_kaleoLog, kaleoLogWrapper._kaleoLog)) {
			return true;
		}

		return false;
	}

	@Override
	public KaleoLog getWrappedModel() {
		return _kaleoLog;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _kaleoLog.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _kaleoLog.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_kaleoLog.resetOriginalValues();
	}

	private final KaleoLog _kaleoLog;
}