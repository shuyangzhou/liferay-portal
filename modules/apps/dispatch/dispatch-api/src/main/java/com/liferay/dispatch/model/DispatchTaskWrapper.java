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

package com.liferay.dispatch.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link DispatchTask}.
 * </p>
 *
 * @author Matija Petanjek
 * @see DispatchTask
 * @generated
 */
public class DispatchTaskWrapper
	extends BaseModelWrapper<DispatchTask>
	implements DispatchTask, ModelWrapper<DispatchTask> {

	public DispatchTaskWrapper(DispatchTask dispatchTask) {
		super(dispatchTask);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("dispatchTaskId", getDispatchTaskId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("active", isActive());
		attributes.put("name", getName());
		attributes.put("system", isSystem());
		attributes.put("type", getType());
		attributes.put("typeSettings", getTypeSettings());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long dispatchTaskId = (Long)attributes.get("dispatchTaskId");

		if (dispatchTaskId != null) {
			setDispatchTaskId(dispatchTaskId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Boolean active = (Boolean)attributes.get("active");

		if (active != null) {
			setActive(active);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		Boolean system = (Boolean)attributes.get("system");

		if (system != null) {
			setSystem(system);
		}

		String type = (String)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		String typeSettings = (String)attributes.get("typeSettings");

		if (typeSettings != null) {
			setTypeSettings(typeSettings);
		}
	}

	/**
	 * Returns the active of this dispatch task.
	 *
	 * @return the active of this dispatch task
	 */
	@Override
	public boolean getActive() {
		return model.getActive();
	}

	/**
	 * Returns the company ID of this dispatch task.
	 *
	 * @return the company ID of this dispatch task
	 */
	@Override
	public long getCompanyId() {
		return model.getCompanyId();
	}

	/**
	 * Returns the create date of this dispatch task.
	 *
	 * @return the create date of this dispatch task
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the dispatch task ID of this dispatch task.
	 *
	 * @return the dispatch task ID of this dispatch task
	 */
	@Override
	public long getDispatchTaskId() {
		return model.getDispatchTaskId();
	}

	/**
	 * Returns the modified date of this dispatch task.
	 *
	 * @return the modified date of this dispatch task
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the mvcc version of this dispatch task.
	 *
	 * @return the mvcc version of this dispatch task
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the name of this dispatch task.
	 *
	 * @return the name of this dispatch task
	 */
	@Override
	public String getName() {
		return model.getName();
	}

	/**
	 * Returns the primary key of this dispatch task.
	 *
	 * @return the primary key of this dispatch task
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the system of this dispatch task.
	 *
	 * @return the system of this dispatch task
	 */
	@Override
	public boolean getSystem() {
		return model.getSystem();
	}

	/**
	 * Returns the type of this dispatch task.
	 *
	 * @return the type of this dispatch task
	 */
	@Override
	public String getType() {
		return model.getType();
	}

	/**
	 * Returns the type settings of this dispatch task.
	 *
	 * @return the type settings of this dispatch task
	 */
	@Override
	public String getTypeSettings() {
		return model.getTypeSettings();
	}

	/**
	 * Returns the user ID of this dispatch task.
	 *
	 * @return the user ID of this dispatch task
	 */
	@Override
	public long getUserId() {
		return model.getUserId();
	}

	/**
	 * Returns the user name of this dispatch task.
	 *
	 * @return the user name of this dispatch task
	 */
	@Override
	public String getUserName() {
		return model.getUserName();
	}

	/**
	 * Returns the user uuid of this dispatch task.
	 *
	 * @return the user uuid of this dispatch task
	 */
	@Override
	public String getUserUuid() {
		return model.getUserUuid();
	}

	/**
	 * Returns <code>true</code> if this dispatch task is active.
	 *
	 * @return <code>true</code> if this dispatch task is active; <code>false</code> otherwise
	 */
	@Override
	public boolean isActive() {
		return model.isActive();
	}

	/**
	 * Returns <code>true</code> if this dispatch task is system.
	 *
	 * @return <code>true</code> if this dispatch task is system; <code>false</code> otherwise
	 */
	@Override
	public boolean isSystem() {
		return model.isSystem();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets whether this dispatch task is active.
	 *
	 * @param active the active of this dispatch task
	 */
	@Override
	public void setActive(boolean active) {
		model.setActive(active);
	}

	/**
	 * Sets the company ID of this dispatch task.
	 *
	 * @param companyId the company ID of this dispatch task
	 */
	@Override
	public void setCompanyId(long companyId) {
		model.setCompanyId(companyId);
	}

	/**
	 * Sets the create date of this dispatch task.
	 *
	 * @param createDate the create date of this dispatch task
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the dispatch task ID of this dispatch task.
	 *
	 * @param dispatchTaskId the dispatch task ID of this dispatch task
	 */
	@Override
	public void setDispatchTaskId(long dispatchTaskId) {
		model.setDispatchTaskId(dispatchTaskId);
	}

	/**
	 * Sets the modified date of this dispatch task.
	 *
	 * @param modifiedDate the modified date of this dispatch task
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the mvcc version of this dispatch task.
	 *
	 * @param mvccVersion the mvcc version of this dispatch task
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the name of this dispatch task.
	 *
	 * @param name the name of this dispatch task
	 */
	@Override
	public void setName(String name) {
		model.setName(name);
	}

	/**
	 * Sets the primary key of this dispatch task.
	 *
	 * @param primaryKey the primary key of this dispatch task
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets whether this dispatch task is system.
	 *
	 * @param system the system of this dispatch task
	 */
	@Override
	public void setSystem(boolean system) {
		model.setSystem(system);
	}

	/**
	 * Sets the type of this dispatch task.
	 *
	 * @param type the type of this dispatch task
	 */
	@Override
	public void setType(String type) {
		model.setType(type);
	}

	/**
	 * Sets the type settings of this dispatch task.
	 *
	 * @param typeSettings the type settings of this dispatch task
	 */
	@Override
	public void setTypeSettings(String typeSettings) {
		model.setTypeSettings(typeSettings);
	}

	/**
	 * Sets the user ID of this dispatch task.
	 *
	 * @param userId the user ID of this dispatch task
	 */
	@Override
	public void setUserId(long userId) {
		model.setUserId(userId);
	}

	/**
	 * Sets the user name of this dispatch task.
	 *
	 * @param userName the user name of this dispatch task
	 */
	@Override
	public void setUserName(String userName) {
		model.setUserName(userName);
	}

	/**
	 * Sets the user uuid of this dispatch task.
	 *
	 * @param userUuid the user uuid of this dispatch task
	 */
	@Override
	public void setUserUuid(String userUuid) {
		model.setUserUuid(userUuid);
	}

	@Override
	protected DispatchTaskWrapper wrap(DispatchTask dispatchTask) {
		return new DispatchTaskWrapper(dispatchTask);
	}

}