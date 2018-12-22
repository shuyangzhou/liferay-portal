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

import com.liferay.exportimport.kernel.lar.StagedModelType;

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
 * This class is a wrapper for {@link Team}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Team
 * @generated
 */
@ProviderType
public class TeamWrapper implements Team, ModelWrapper<Team> {
	public TeamWrapper(Team team) {
		_team = team;
	}

	@Override
	public Class<?> getModelClass() {
		return Team.class;
	}

	@Override
	public String getModelClassName() {
		return Team.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<Team, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<Team, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<Team, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<Team, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<Team, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<Team, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<Team, Object>> getAttributeGetters() {
		return _team.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<Team, Object>> getAttributeSetters() {
		return _team.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new TeamWrapper((Team)_team.clone());
	}

	@Override
	public int compareTo(Team team) {
		return _team.compareTo(team);
	}

	/**
	* Returns the company ID of this team.
	*
	* @return the company ID of this team
	*/
	@Override
	public long getCompanyId() {
		return _team.getCompanyId();
	}

	/**
	* Returns the create date of this team.
	*
	* @return the create date of this team
	*/
	@Override
	public Date getCreateDate() {
		return _team.getCreateDate();
	}

	/**
	* Returns the description of this team.
	*
	* @return the description of this team
	*/
	@Override
	public String getDescription() {
		return _team.getDescription();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _team.getExpandoBridge();
	}

	/**
	* Returns the group ID of this team.
	*
	* @return the group ID of this team
	*/
	@Override
	public long getGroupId() {
		return _team.getGroupId();
	}

	/**
	* Returns the last publish date of this team.
	*
	* @return the last publish date of this team
	*/
	@Override
	public Date getLastPublishDate() {
		return _team.getLastPublishDate();
	}

	/**
	* Returns the modified date of this team.
	*
	* @return the modified date of this team
	*/
	@Override
	public Date getModifiedDate() {
		return _team.getModifiedDate();
	}

	/**
	* Returns the mvcc version of this team.
	*
	* @return the mvcc version of this team
	*/
	@Override
	public long getMvccVersion() {
		return _team.getMvccVersion();
	}

	/**
	* Returns the name of this team.
	*
	* @return the name of this team
	*/
	@Override
	public String getName() {
		return _team.getName();
	}

	/**
	* Returns the primary key of this team.
	*
	* @return the primary key of this team
	*/
	@Override
	public long getPrimaryKey() {
		return _team.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _team.getPrimaryKeyObj();
	}

	@Override
	public Role getRole()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _team.getRole();
	}

	/**
	* Returns the team ID of this team.
	*
	* @return the team ID of this team
	*/
	@Override
	public long getTeamId() {
		return _team.getTeamId();
	}

	/**
	* Returns the user ID of this team.
	*
	* @return the user ID of this team
	*/
	@Override
	public long getUserId() {
		return _team.getUserId();
	}

	/**
	* Returns the user name of this team.
	*
	* @return the user name of this team
	*/
	@Override
	public String getUserName() {
		return _team.getUserName();
	}

	/**
	* Returns the user uuid of this team.
	*
	* @return the user uuid of this team
	*/
	@Override
	public String getUserUuid() {
		return _team.getUserUuid();
	}

	/**
	* Returns the uuid of this team.
	*
	* @return the uuid of this team
	*/
	@Override
	public String getUuid() {
		return _team.getUuid();
	}

	@Override
	public int hashCode() {
		return _team.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _team.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _team.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _team.isNew();
	}

	@Override
	public void persist() {
		_team.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_team.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this team.
	*
	* @param companyId the company ID of this team
	*/
	@Override
	public void setCompanyId(long companyId) {
		_team.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this team.
	*
	* @param createDate the create date of this team
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_team.setCreateDate(createDate);
	}

	/**
	* Sets the description of this team.
	*
	* @param description the description of this team
	*/
	@Override
	public void setDescription(String description) {
		_team.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_team.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_team.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_team.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this team.
	*
	* @param groupId the group ID of this team
	*/
	@Override
	public void setGroupId(long groupId) {
		_team.setGroupId(groupId);
	}

	/**
	* Sets the last publish date of this team.
	*
	* @param lastPublishDate the last publish date of this team
	*/
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_team.setLastPublishDate(lastPublishDate);
	}

	/**
	* Sets the modified date of this team.
	*
	* @param modifiedDate the modified date of this team
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_team.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the mvcc version of this team.
	*
	* @param mvccVersion the mvcc version of this team
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_team.setMvccVersion(mvccVersion);
	}

	/**
	* Sets the name of this team.
	*
	* @param name the name of this team
	*/
	@Override
	public void setName(String name) {
		_team.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_team.setNew(n);
	}

	/**
	* Sets the primary key of this team.
	*
	* @param primaryKey the primary key of this team
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_team.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_team.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the team ID of this team.
	*
	* @param teamId the team ID of this team
	*/
	@Override
	public void setTeamId(long teamId) {
		_team.setTeamId(teamId);
	}

	/**
	* Sets the user ID of this team.
	*
	* @param userId the user ID of this team
	*/
	@Override
	public void setUserId(long userId) {
		_team.setUserId(userId);
	}

	/**
	* Sets the user name of this team.
	*
	* @param userName the user name of this team
	*/
	@Override
	public void setUserName(String userName) {
		_team.setUserName(userName);
	}

	/**
	* Sets the user uuid of this team.
	*
	* @param userUuid the user uuid of this team
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_team.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this team.
	*
	* @param uuid the uuid of this team
	*/
	@Override
	public void setUuid(String uuid) {
		_team.setUuid(uuid);
	}

	@Override
	public CacheModel<Team> toCacheModel() {
		return _team.toCacheModel();
	}

	@Override
	public Team toEscapedModel() {
		return new TeamWrapper(_team.toEscapedModel());
	}

	@Override
	public String toString() {
		return _team.toString();
	}

	@Override
	public Team toUnescapedModel() {
		return new TeamWrapper(_team.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _team.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof TeamWrapper)) {
			return false;
		}

		TeamWrapper teamWrapper = (TeamWrapper)obj;

		if (Objects.equals(_team, teamWrapper._team)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _team.getStagedModelType();
	}

	@Override
	public Team getWrappedModel() {
		return _team;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _team.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _team.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_team.resetOriginalValues();
	}

	private final Team _team;
}