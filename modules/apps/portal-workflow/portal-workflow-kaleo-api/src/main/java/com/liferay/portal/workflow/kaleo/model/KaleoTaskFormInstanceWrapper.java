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
 * This class is a wrapper for {@link KaleoTaskFormInstance}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoTaskFormInstance
 * @generated
 */
@ProviderType
public class KaleoTaskFormInstanceWrapper implements KaleoTaskFormInstance,
	ModelWrapper<KaleoTaskFormInstance> {
	public KaleoTaskFormInstanceWrapper(
		KaleoTaskFormInstance kaleoTaskFormInstance) {
		_kaleoTaskFormInstance = kaleoTaskFormInstance;
	}

	@Override
	public Class<?> getModelClass() {
		return KaleoTaskFormInstance.class;
	}

	@Override
	public String getModelClassName() {
		return KaleoTaskFormInstance.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<KaleoTaskFormInstance, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<KaleoTaskFormInstance, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<KaleoTaskFormInstance, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<KaleoTaskFormInstance, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<KaleoTaskFormInstance, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<KaleoTaskFormInstance, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<KaleoTaskFormInstance, Object>> getAttributeGetters() {
		return _kaleoTaskFormInstance.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<KaleoTaskFormInstance, Object>> getAttributeSetters() {
		return _kaleoTaskFormInstance.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new KaleoTaskFormInstanceWrapper((KaleoTaskFormInstance)_kaleoTaskFormInstance.clone());
	}

	@Override
	public int compareTo(KaleoTaskFormInstance kaleoTaskFormInstance) {
		return _kaleoTaskFormInstance.compareTo(kaleoTaskFormInstance);
	}

	/**
	* Returns the company ID of this kaleo task form instance.
	*
	* @return the company ID of this kaleo task form instance
	*/
	@Override
	public long getCompanyId() {
		return _kaleoTaskFormInstance.getCompanyId();
	}

	/**
	* Returns the create date of this kaleo task form instance.
	*
	* @return the create date of this kaleo task form instance
	*/
	@Override
	public Date getCreateDate() {
		return _kaleoTaskFormInstance.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _kaleoTaskFormInstance.getExpandoBridge();
	}

	/**
	* Returns the form value entry group ID of this kaleo task form instance.
	*
	* @return the form value entry group ID of this kaleo task form instance
	*/
	@Override
	public long getFormValueEntryGroupId() {
		return _kaleoTaskFormInstance.getFormValueEntryGroupId();
	}

	/**
	* Returns the form value entry ID of this kaleo task form instance.
	*
	* @return the form value entry ID of this kaleo task form instance
	*/
	@Override
	public long getFormValueEntryId() {
		return _kaleoTaskFormInstance.getFormValueEntryId();
	}

	/**
	* Returns the form value entry uuid of this kaleo task form instance.
	*
	* @return the form value entry uuid of this kaleo task form instance
	*/
	@Override
	public String getFormValueEntryUuid() {
		return _kaleoTaskFormInstance.getFormValueEntryUuid();
	}

	/**
	* Returns the form values of this kaleo task form instance.
	*
	* @return the form values of this kaleo task form instance
	*/
	@Override
	public String getFormValues() {
		return _kaleoTaskFormInstance.getFormValues();
	}

	/**
	* Returns the group ID of this kaleo task form instance.
	*
	* @return the group ID of this kaleo task form instance
	*/
	@Override
	public long getGroupId() {
		return _kaleoTaskFormInstance.getGroupId();
	}

	/**
	* Returns the kaleo definition version ID of this kaleo task form instance.
	*
	* @return the kaleo definition version ID of this kaleo task form instance
	*/
	@Override
	public long getKaleoDefinitionVersionId() {
		return _kaleoTaskFormInstance.getKaleoDefinitionVersionId();
	}

	/**
	* Returns the kaleo instance ID of this kaleo task form instance.
	*
	* @return the kaleo instance ID of this kaleo task form instance
	*/
	@Override
	public long getKaleoInstanceId() {
		return _kaleoTaskFormInstance.getKaleoInstanceId();
	}

	/**
	* Returns the kaleo task form ID of this kaleo task form instance.
	*
	* @return the kaleo task form ID of this kaleo task form instance
	*/
	@Override
	public long getKaleoTaskFormId() {
		return _kaleoTaskFormInstance.getKaleoTaskFormId();
	}

	/**
	* Returns the kaleo task form instance ID of this kaleo task form instance.
	*
	* @return the kaleo task form instance ID of this kaleo task form instance
	*/
	@Override
	public long getKaleoTaskFormInstanceId() {
		return _kaleoTaskFormInstance.getKaleoTaskFormInstanceId();
	}

	/**
	* Returns the kaleo task ID of this kaleo task form instance.
	*
	* @return the kaleo task ID of this kaleo task form instance
	*/
	@Override
	public long getKaleoTaskId() {
		return _kaleoTaskFormInstance.getKaleoTaskId();
	}

	/**
	* Returns the kaleo task instance token ID of this kaleo task form instance.
	*
	* @return the kaleo task instance token ID of this kaleo task form instance
	*/
	@Override
	public long getKaleoTaskInstanceTokenId() {
		return _kaleoTaskFormInstance.getKaleoTaskInstanceTokenId();
	}

	/**
	* Returns the metadata of this kaleo task form instance.
	*
	* @return the metadata of this kaleo task form instance
	*/
	@Override
	public String getMetadata() {
		return _kaleoTaskFormInstance.getMetadata();
	}

	/**
	* Returns the modified date of this kaleo task form instance.
	*
	* @return the modified date of this kaleo task form instance
	*/
	@Override
	public Date getModifiedDate() {
		return _kaleoTaskFormInstance.getModifiedDate();
	}

	/**
	* Returns the primary key of this kaleo task form instance.
	*
	* @return the primary key of this kaleo task form instance
	*/
	@Override
	public long getPrimaryKey() {
		return _kaleoTaskFormInstance.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kaleoTaskFormInstance.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this kaleo task form instance.
	*
	* @return the user ID of this kaleo task form instance
	*/
	@Override
	public long getUserId() {
		return _kaleoTaskFormInstance.getUserId();
	}

	/**
	* Returns the user name of this kaleo task form instance.
	*
	* @return the user name of this kaleo task form instance
	*/
	@Override
	public String getUserName() {
		return _kaleoTaskFormInstance.getUserName();
	}

	/**
	* Returns the user uuid of this kaleo task form instance.
	*
	* @return the user uuid of this kaleo task form instance
	*/
	@Override
	public String getUserUuid() {
		return _kaleoTaskFormInstance.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _kaleoTaskFormInstance.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _kaleoTaskFormInstance.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _kaleoTaskFormInstance.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _kaleoTaskFormInstance.isNew();
	}

	@Override
	public void persist() {
		_kaleoTaskFormInstance.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_kaleoTaskFormInstance.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this kaleo task form instance.
	*
	* @param companyId the company ID of this kaleo task form instance
	*/
	@Override
	public void setCompanyId(long companyId) {
		_kaleoTaskFormInstance.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this kaleo task form instance.
	*
	* @param createDate the create date of this kaleo task form instance
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_kaleoTaskFormInstance.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_kaleoTaskFormInstance.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_kaleoTaskFormInstance.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_kaleoTaskFormInstance.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the form value entry group ID of this kaleo task form instance.
	*
	* @param formValueEntryGroupId the form value entry group ID of this kaleo task form instance
	*/
	@Override
	public void setFormValueEntryGroupId(long formValueEntryGroupId) {
		_kaleoTaskFormInstance.setFormValueEntryGroupId(formValueEntryGroupId);
	}

	/**
	* Sets the form value entry ID of this kaleo task form instance.
	*
	* @param formValueEntryId the form value entry ID of this kaleo task form instance
	*/
	@Override
	public void setFormValueEntryId(long formValueEntryId) {
		_kaleoTaskFormInstance.setFormValueEntryId(formValueEntryId);
	}

	/**
	* Sets the form value entry uuid of this kaleo task form instance.
	*
	* @param formValueEntryUuid the form value entry uuid of this kaleo task form instance
	*/
	@Override
	public void setFormValueEntryUuid(String formValueEntryUuid) {
		_kaleoTaskFormInstance.setFormValueEntryUuid(formValueEntryUuid);
	}

	/**
	* Sets the form values of this kaleo task form instance.
	*
	* @param formValues the form values of this kaleo task form instance
	*/
	@Override
	public void setFormValues(String formValues) {
		_kaleoTaskFormInstance.setFormValues(formValues);
	}

	/**
	* Sets the group ID of this kaleo task form instance.
	*
	* @param groupId the group ID of this kaleo task form instance
	*/
	@Override
	public void setGroupId(long groupId) {
		_kaleoTaskFormInstance.setGroupId(groupId);
	}

	/**
	* Sets the kaleo definition version ID of this kaleo task form instance.
	*
	* @param kaleoDefinitionVersionId the kaleo definition version ID of this kaleo task form instance
	*/
	@Override
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId) {
		_kaleoTaskFormInstance.setKaleoDefinitionVersionId(kaleoDefinitionVersionId);
	}

	/**
	* Sets the kaleo instance ID of this kaleo task form instance.
	*
	* @param kaleoInstanceId the kaleo instance ID of this kaleo task form instance
	*/
	@Override
	public void setKaleoInstanceId(long kaleoInstanceId) {
		_kaleoTaskFormInstance.setKaleoInstanceId(kaleoInstanceId);
	}

	/**
	* Sets the kaleo task form ID of this kaleo task form instance.
	*
	* @param kaleoTaskFormId the kaleo task form ID of this kaleo task form instance
	*/
	@Override
	public void setKaleoTaskFormId(long kaleoTaskFormId) {
		_kaleoTaskFormInstance.setKaleoTaskFormId(kaleoTaskFormId);
	}

	/**
	* Sets the kaleo task form instance ID of this kaleo task form instance.
	*
	* @param kaleoTaskFormInstanceId the kaleo task form instance ID of this kaleo task form instance
	*/
	@Override
	public void setKaleoTaskFormInstanceId(long kaleoTaskFormInstanceId) {
		_kaleoTaskFormInstance.setKaleoTaskFormInstanceId(kaleoTaskFormInstanceId);
	}

	/**
	* Sets the kaleo task ID of this kaleo task form instance.
	*
	* @param kaleoTaskId the kaleo task ID of this kaleo task form instance
	*/
	@Override
	public void setKaleoTaskId(long kaleoTaskId) {
		_kaleoTaskFormInstance.setKaleoTaskId(kaleoTaskId);
	}

	/**
	* Sets the kaleo task instance token ID of this kaleo task form instance.
	*
	* @param kaleoTaskInstanceTokenId the kaleo task instance token ID of this kaleo task form instance
	*/
	@Override
	public void setKaleoTaskInstanceTokenId(long kaleoTaskInstanceTokenId) {
		_kaleoTaskFormInstance.setKaleoTaskInstanceTokenId(kaleoTaskInstanceTokenId);
	}

	/**
	* Sets the metadata of this kaleo task form instance.
	*
	* @param metadata the metadata of this kaleo task form instance
	*/
	@Override
	public void setMetadata(String metadata) {
		_kaleoTaskFormInstance.setMetadata(metadata);
	}

	/**
	* Sets the modified date of this kaleo task form instance.
	*
	* @param modifiedDate the modified date of this kaleo task form instance
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_kaleoTaskFormInstance.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_kaleoTaskFormInstance.setNew(n);
	}

	/**
	* Sets the primary key of this kaleo task form instance.
	*
	* @param primaryKey the primary key of this kaleo task form instance
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_kaleoTaskFormInstance.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_kaleoTaskFormInstance.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this kaleo task form instance.
	*
	* @param userId the user ID of this kaleo task form instance
	*/
	@Override
	public void setUserId(long userId) {
		_kaleoTaskFormInstance.setUserId(userId);
	}

	/**
	* Sets the user name of this kaleo task form instance.
	*
	* @param userName the user name of this kaleo task form instance
	*/
	@Override
	public void setUserName(String userName) {
		_kaleoTaskFormInstance.setUserName(userName);
	}

	/**
	* Sets the user uuid of this kaleo task form instance.
	*
	* @param userUuid the user uuid of this kaleo task form instance
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_kaleoTaskFormInstance.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<KaleoTaskFormInstance> toCacheModel() {
		return _kaleoTaskFormInstance.toCacheModel();
	}

	@Override
	public KaleoTaskFormInstance toEscapedModel() {
		return new KaleoTaskFormInstanceWrapper(_kaleoTaskFormInstance.toEscapedModel());
	}

	@Override
	public String toString() {
		return _kaleoTaskFormInstance.toString();
	}

	@Override
	public KaleoTaskFormInstance toUnescapedModel() {
		return new KaleoTaskFormInstanceWrapper(_kaleoTaskFormInstance.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _kaleoTaskFormInstance.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof KaleoTaskFormInstanceWrapper)) {
			return false;
		}

		KaleoTaskFormInstanceWrapper kaleoTaskFormInstanceWrapper = (KaleoTaskFormInstanceWrapper)obj;

		if (Objects.equals(_kaleoTaskFormInstance,
					kaleoTaskFormInstanceWrapper._kaleoTaskFormInstance)) {
			return true;
		}

		return false;
	}

	@Override
	public KaleoTaskFormInstance getWrappedModel() {
		return _kaleoTaskFormInstance;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _kaleoTaskFormInstance.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _kaleoTaskFormInstance.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_kaleoTaskFormInstance.resetOriginalValues();
	}

	private final KaleoTaskFormInstance _kaleoTaskFormInstance;
}