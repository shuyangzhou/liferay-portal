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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link ClassName}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ClassName
 * @generated
 */
@ProviderType
public class ClassNameWrapper implements ClassName, ModelWrapper<ClassName> {
	public ClassNameWrapper(ClassName className) {
		_className = className;
	}

	@Override
	public Class<?> getModelClass() {
		return ClassName.class;
	}

	@Override
	public String getModelClassName() {
		return ClassName.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<ClassName, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<ClassName, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<ClassName, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<ClassName, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<ClassName, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<ClassName, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<ClassName, Object>> getAttributeGetters() {
		return _className.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<ClassName, Object>> getAttributeSetters() {
		return _className.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new ClassNameWrapper((ClassName)_className.clone());
	}

	@Override
	public int compareTo(ClassName className) {
		return _className.compareTo(className);
	}

	/**
	* Returns the fully qualified class name of this class name.
	*
	* @return the fully qualified class name of this class name
	*/
	@Override
	public String getClassName() {
		return _className.getClassName();
	}

	/**
	* Returns the class name ID of this class name.
	*
	* @return the class name ID of this class name
	*/
	@Override
	public long getClassNameId() {
		return _className.getClassNameId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _className.getExpandoBridge();
	}

	/**
	* Returns the mvcc version of this class name.
	*
	* @return the mvcc version of this class name
	*/
	@Override
	public long getMvccVersion() {
		return _className.getMvccVersion();
	}

	/**
	* Returns the primary key of this class name.
	*
	* @return the primary key of this class name
	*/
	@Override
	public long getPrimaryKey() {
		return _className.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _className.getPrimaryKeyObj();
	}

	/**
	* Returns the value of this class name.
	*
	* @return the value of this class name
	*/
	@Override
	public String getValue() {
		return _className.getValue();
	}

	@Override
	public int hashCode() {
		return _className.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _className.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _className.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _className.isNew();
	}

	@Override
	public void persist() {
		_className.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_className.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_className.setClassName(className);
	}

	/**
	* Sets the class name ID of this class name.
	*
	* @param classNameId the class name ID of this class name
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_className.setClassNameId(classNameId);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_className.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_className.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_className.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the mvcc version of this class name.
	*
	* @param mvccVersion the mvcc version of this class name
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_className.setMvccVersion(mvccVersion);
	}

	@Override
	public void setNew(boolean n) {
		_className.setNew(n);
	}

	/**
	* Sets the primary key of this class name.
	*
	* @param primaryKey the primary key of this class name
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_className.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_className.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the value of this class name.
	*
	* @param value the value of this class name
	*/
	@Override
	public void setValue(String value) {
		_className.setValue(value);
	}

	@Override
	public CacheModel<ClassName> toCacheModel() {
		return _className.toCacheModel();
	}

	@Override
	public ClassName toEscapedModel() {
		return new ClassNameWrapper(_className.toEscapedModel());
	}

	@Override
	public String toString() {
		return _className.toString();
	}

	@Override
	public ClassName toUnescapedModel() {
		return new ClassNameWrapper(_className.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _className.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ClassNameWrapper)) {
			return false;
		}

		ClassNameWrapper classNameWrapper = (ClassNameWrapper)obj;

		if (Objects.equals(_className, classNameWrapper._className)) {
			return true;
		}

		return false;
	}

	@Override
	public ClassName getWrappedModel() {
		return _className;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _className.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _className.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_className.resetOriginalValues();
	}

	private final ClassName _className;
}