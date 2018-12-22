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
 * This class is a wrapper for {@link KaleoAction}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoAction
 * @generated
 */
@ProviderType
public class KaleoActionWrapper implements KaleoAction,
	ModelWrapper<KaleoAction> {
	public KaleoActionWrapper(KaleoAction kaleoAction) {
		_kaleoAction = kaleoAction;
	}

	@Override
	public Class<?> getModelClass() {
		return KaleoAction.class;
	}

	@Override
	public String getModelClassName() {
		return KaleoAction.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<KaleoAction, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<KaleoAction, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<KaleoAction, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<KaleoAction, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<KaleoAction, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<KaleoAction, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<KaleoAction, Object>> getAttributeGetters() {
		return _kaleoAction.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<KaleoAction, Object>> getAttributeSetters() {
		return _kaleoAction.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new KaleoActionWrapper((KaleoAction)_kaleoAction.clone());
	}

	@Override
	public int compareTo(KaleoAction kaleoAction) {
		return _kaleoAction.compareTo(kaleoAction);
	}

	/**
	* Returns the company ID of this kaleo action.
	*
	* @return the company ID of this kaleo action
	*/
	@Override
	public long getCompanyId() {
		return _kaleoAction.getCompanyId();
	}

	/**
	* Returns the create date of this kaleo action.
	*
	* @return the create date of this kaleo action
	*/
	@Override
	public Date getCreateDate() {
		return _kaleoAction.getCreateDate();
	}

	/**
	* Returns the description of this kaleo action.
	*
	* @return the description of this kaleo action
	*/
	@Override
	public String getDescription() {
		return _kaleoAction.getDescription();
	}

	/**
	* Returns the execution type of this kaleo action.
	*
	* @return the execution type of this kaleo action
	*/
	@Override
	public String getExecutionType() {
		return _kaleoAction.getExecutionType();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _kaleoAction.getExpandoBridge();
	}

	/**
	* Returns the group ID of this kaleo action.
	*
	* @return the group ID of this kaleo action
	*/
	@Override
	public long getGroupId() {
		return _kaleoAction.getGroupId();
	}

	/**
	* Returns the kaleo action ID of this kaleo action.
	*
	* @return the kaleo action ID of this kaleo action
	*/
	@Override
	public long getKaleoActionId() {
		return _kaleoAction.getKaleoActionId();
	}

	/**
	* Returns the kaleo class name of this kaleo action.
	*
	* @return the kaleo class name of this kaleo action
	*/
	@Override
	public String getKaleoClassName() {
		return _kaleoAction.getKaleoClassName();
	}

	/**
	* Returns the kaleo class pk of this kaleo action.
	*
	* @return the kaleo class pk of this kaleo action
	*/
	@Override
	public long getKaleoClassPK() {
		return _kaleoAction.getKaleoClassPK();
	}

	/**
	* Returns the kaleo definition version ID of this kaleo action.
	*
	* @return the kaleo definition version ID of this kaleo action
	*/
	@Override
	public long getKaleoDefinitionVersionId() {
		return _kaleoAction.getKaleoDefinitionVersionId();
	}

	/**
	* Returns the kaleo node name of this kaleo action.
	*
	* @return the kaleo node name of this kaleo action
	*/
	@Override
	public String getKaleoNodeName() {
		return _kaleoAction.getKaleoNodeName();
	}

	/**
	* Returns the modified date of this kaleo action.
	*
	* @return the modified date of this kaleo action
	*/
	@Override
	public Date getModifiedDate() {
		return _kaleoAction.getModifiedDate();
	}

	/**
	* Returns the name of this kaleo action.
	*
	* @return the name of this kaleo action
	*/
	@Override
	public String getName() {
		return _kaleoAction.getName();
	}

	/**
	* Returns the primary key of this kaleo action.
	*
	* @return the primary key of this kaleo action
	*/
	@Override
	public long getPrimaryKey() {
		return _kaleoAction.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kaleoAction.getPrimaryKeyObj();
	}

	/**
	* Returns the priority of this kaleo action.
	*
	* @return the priority of this kaleo action
	*/
	@Override
	public int getPriority() {
		return _kaleoAction.getPriority();
	}

	/**
	* Returns the script of this kaleo action.
	*
	* @return the script of this kaleo action
	*/
	@Override
	public String getScript() {
		return _kaleoAction.getScript();
	}

	/**
	* Returns the script language of this kaleo action.
	*
	* @return the script language of this kaleo action
	*/
	@Override
	public String getScriptLanguage() {
		return _kaleoAction.getScriptLanguage();
	}

	/**
	* Returns the script required contexts of this kaleo action.
	*
	* @return the script required contexts of this kaleo action
	*/
	@Override
	public String getScriptRequiredContexts() {
		return _kaleoAction.getScriptRequiredContexts();
	}

	/**
	* Returns the user ID of this kaleo action.
	*
	* @return the user ID of this kaleo action
	*/
	@Override
	public long getUserId() {
		return _kaleoAction.getUserId();
	}

	/**
	* Returns the user name of this kaleo action.
	*
	* @return the user name of this kaleo action
	*/
	@Override
	public String getUserName() {
		return _kaleoAction.getUserName();
	}

	/**
	* Returns the user uuid of this kaleo action.
	*
	* @return the user uuid of this kaleo action
	*/
	@Override
	public String getUserUuid() {
		return _kaleoAction.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _kaleoAction.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _kaleoAction.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _kaleoAction.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _kaleoAction.isNew();
	}

	@Override
	public void persist() {
		_kaleoAction.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_kaleoAction.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this kaleo action.
	*
	* @param companyId the company ID of this kaleo action
	*/
	@Override
	public void setCompanyId(long companyId) {
		_kaleoAction.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this kaleo action.
	*
	* @param createDate the create date of this kaleo action
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_kaleoAction.setCreateDate(createDate);
	}

	/**
	* Sets the description of this kaleo action.
	*
	* @param description the description of this kaleo action
	*/
	@Override
	public void setDescription(String description) {
		_kaleoAction.setDescription(description);
	}

	/**
	* Sets the execution type of this kaleo action.
	*
	* @param executionType the execution type of this kaleo action
	*/
	@Override
	public void setExecutionType(String executionType) {
		_kaleoAction.setExecutionType(executionType);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_kaleoAction.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_kaleoAction.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_kaleoAction.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this kaleo action.
	*
	* @param groupId the group ID of this kaleo action
	*/
	@Override
	public void setGroupId(long groupId) {
		_kaleoAction.setGroupId(groupId);
	}

	/**
	* Sets the kaleo action ID of this kaleo action.
	*
	* @param kaleoActionId the kaleo action ID of this kaleo action
	*/
	@Override
	public void setKaleoActionId(long kaleoActionId) {
		_kaleoAction.setKaleoActionId(kaleoActionId);
	}

	/**
	* Sets the kaleo class name of this kaleo action.
	*
	* @param kaleoClassName the kaleo class name of this kaleo action
	*/
	@Override
	public void setKaleoClassName(String kaleoClassName) {
		_kaleoAction.setKaleoClassName(kaleoClassName);
	}

	/**
	* Sets the kaleo class pk of this kaleo action.
	*
	* @param kaleoClassPK the kaleo class pk of this kaleo action
	*/
	@Override
	public void setKaleoClassPK(long kaleoClassPK) {
		_kaleoAction.setKaleoClassPK(kaleoClassPK);
	}

	/**
	* Sets the kaleo definition version ID of this kaleo action.
	*
	* @param kaleoDefinitionVersionId the kaleo definition version ID of this kaleo action
	*/
	@Override
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId) {
		_kaleoAction.setKaleoDefinitionVersionId(kaleoDefinitionVersionId);
	}

	/**
	* Sets the kaleo node name of this kaleo action.
	*
	* @param kaleoNodeName the kaleo node name of this kaleo action
	*/
	@Override
	public void setKaleoNodeName(String kaleoNodeName) {
		_kaleoAction.setKaleoNodeName(kaleoNodeName);
	}

	/**
	* Sets the modified date of this kaleo action.
	*
	* @param modifiedDate the modified date of this kaleo action
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_kaleoAction.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this kaleo action.
	*
	* @param name the name of this kaleo action
	*/
	@Override
	public void setName(String name) {
		_kaleoAction.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_kaleoAction.setNew(n);
	}

	/**
	* Sets the primary key of this kaleo action.
	*
	* @param primaryKey the primary key of this kaleo action
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_kaleoAction.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_kaleoAction.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the priority of this kaleo action.
	*
	* @param priority the priority of this kaleo action
	*/
	@Override
	public void setPriority(int priority) {
		_kaleoAction.setPriority(priority);
	}

	/**
	* Sets the script of this kaleo action.
	*
	* @param script the script of this kaleo action
	*/
	@Override
	public void setScript(String script) {
		_kaleoAction.setScript(script);
	}

	/**
	* Sets the script language of this kaleo action.
	*
	* @param scriptLanguage the script language of this kaleo action
	*/
	@Override
	public void setScriptLanguage(String scriptLanguage) {
		_kaleoAction.setScriptLanguage(scriptLanguage);
	}

	/**
	* Sets the script required contexts of this kaleo action.
	*
	* @param scriptRequiredContexts the script required contexts of this kaleo action
	*/
	@Override
	public void setScriptRequiredContexts(String scriptRequiredContexts) {
		_kaleoAction.setScriptRequiredContexts(scriptRequiredContexts);
	}

	/**
	* Sets the user ID of this kaleo action.
	*
	* @param userId the user ID of this kaleo action
	*/
	@Override
	public void setUserId(long userId) {
		_kaleoAction.setUserId(userId);
	}

	/**
	* Sets the user name of this kaleo action.
	*
	* @param userName the user name of this kaleo action
	*/
	@Override
	public void setUserName(String userName) {
		_kaleoAction.setUserName(userName);
	}

	/**
	* Sets the user uuid of this kaleo action.
	*
	* @param userUuid the user uuid of this kaleo action
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_kaleoAction.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<KaleoAction> toCacheModel() {
		return _kaleoAction.toCacheModel();
	}

	@Override
	public KaleoAction toEscapedModel() {
		return new KaleoActionWrapper(_kaleoAction.toEscapedModel());
	}

	@Override
	public String toString() {
		return _kaleoAction.toString();
	}

	@Override
	public KaleoAction toUnescapedModel() {
		return new KaleoActionWrapper(_kaleoAction.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _kaleoAction.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof KaleoActionWrapper)) {
			return false;
		}

		KaleoActionWrapper kaleoActionWrapper = (KaleoActionWrapper)obj;

		if (Objects.equals(_kaleoAction, kaleoActionWrapper._kaleoAction)) {
			return true;
		}

		return false;
	}

	@Override
	public KaleoAction getWrappedModel() {
		return _kaleoAction;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _kaleoAction.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _kaleoAction.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_kaleoAction.resetOriginalValues();
	}

	private final KaleoAction _kaleoAction;
}