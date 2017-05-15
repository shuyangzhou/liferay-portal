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

/**
 * <p>
 * This class is a wrapper for {@link Group}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Group
 * @generated
 */
@ProviderType
public class GroupWrapper implements Group, ModelWrapper<Group> {
	public GroupWrapper(Group group) {
		_group = group;
	}

	@Override
	public Class<?> getModelClass() {
		return Group.class;
	}

	@Override
	public String getModelClassName() {
		return Group.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("uuid", getUuid());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("creatorUserId", getCreatorUserId());
		attributes.put("classNameId", getClassNameId());
		attributes.put("classPK", getClassPK());
		attributes.put("parentGroupId", getParentGroupId());
		attributes.put("liveGroupId", getLiveGroupId());
		attributes.put("treePath", getTreePath());
		attributes.put("groupKey", getGroupKey());
		attributes.put("type", getType());
		attributes.put("typeSettings", getTypeSettings());
		attributes.put("manualMembership", getManualMembership());
		attributes.put("membershipRestriction", getMembershipRestriction());
		attributes.put("friendlyURL", getFriendlyURL());
		attributes.put("site", getSite());
		attributes.put("remoteStagingGroupCount", getRemoteStagingGroupCount());
		attributes.put("inheritContent", getInheritContent());
		attributes.put("active", getActive());
		attributes.put("defaultLanguageId", getDefaultLanguageId());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long creatorUserId = (Long)attributes.get("creatorUserId");

		if (creatorUserId != null) {
			setCreatorUserId(creatorUserId);
		}

		Long classNameId = (Long)attributes.get("classNameId");

		if (classNameId != null) {
			setClassNameId(classNameId);
		}

		Long classPK = (Long)attributes.get("classPK");

		if (classPK != null) {
			setClassPK(classPK);
		}

		Long parentGroupId = (Long)attributes.get("parentGroupId");

		if (parentGroupId != null) {
			setParentGroupId(parentGroupId);
		}

		Long liveGroupId = (Long)attributes.get("liveGroupId");

		if (liveGroupId != null) {
			setLiveGroupId(liveGroupId);
		}

		String treePath = (String)attributes.get("treePath");

		if (treePath != null) {
			setTreePath(treePath);
		}

		String groupKey = (String)attributes.get("groupKey");

		if (groupKey != null) {
			setGroupKey(groupKey);
		}

		Integer type = (Integer)attributes.get("type");

		if (type != null) {
			setType(type);
		}

		String typeSettings = (String)attributes.get("typeSettings");

		if (typeSettings != null) {
			setTypeSettings(typeSettings);
		}

		Boolean manualMembership = (Boolean)attributes.get("manualMembership");

		if (manualMembership != null) {
			setManualMembership(manualMembership);
		}

		Integer membershipRestriction = (Integer)attributes.get(
				"membershipRestriction");

		if (membershipRestriction != null) {
			setMembershipRestriction(membershipRestriction);
		}

		String friendlyURL = (String)attributes.get("friendlyURL");

		if (friendlyURL != null) {
			setFriendlyURL(friendlyURL);
		}

		Boolean site = (Boolean)attributes.get("site");

		if (site != null) {
			setSite(site);
		}

		Integer remoteStagingGroupCount = (Integer)attributes.get(
				"remoteStagingGroupCount");

		if (remoteStagingGroupCount != null) {
			setRemoteStagingGroupCount(remoteStagingGroupCount);
		}

		Boolean inheritContent = (Boolean)attributes.get("inheritContent");

		if (inheritContent != null) {
			setInheritContent(inheritContent);
		}

		Boolean active = (Boolean)attributes.get("active");

		if (active != null) {
			setActive(active);
		}

		String defaultLanguageId = (String)attributes.get("defaultLanguageId");

		if (defaultLanguageId != null) {
			setDefaultLanguageId(defaultLanguageId);
		}
	}

	@Override
	public CacheModel<Group> toCacheModel() {
		return _group.toCacheModel();
	}

	@Override
	public Group toEscapedModel() {
		return new GroupWrapper(_group.toEscapedModel());
	}

	@Override
	public Group toUnescapedModel() {
		return new GroupWrapper(_group.toUnescapedModel());
	}

	@Override
	public LayoutSet getPrivateLayoutSet() {
		return _group.getPrivateLayoutSet();
	}

	@Override
	public LayoutSet getPublicLayoutSet() {
		return _group.getPublicLayoutSet();
	}

	/**
	* Returns the active of this group.
	*
	* @return the active of this group
	*/
	@Override
	public boolean getActive() {
		return _group.getActive();
	}

	/**
	* Returns the inherit content of this group.
	*
	* @return the inherit content of this group
	*/
	@Override
	public boolean getInheritContent() {
		return _group.getInheritContent();
	}

	/**
	* Returns the manual membership of this group.
	*
	* @return the manual membership of this group
	*/
	@Override
	public boolean getManualMembership() {
		return _group.getManualMembership();
	}

	/**
	* Returns the site of this group.
	*
	* @return the site of this group
	*/
	@Override
	public boolean getSite() {
		return _group.getSite();
	}

	@Override
	public boolean hasAncestor(long groupId) {
		return _group.hasAncestor(groupId);
	}

	@Override
	public boolean hasLocalOrRemoteStagingGroup() {
		return _group.hasLocalOrRemoteStagingGroup();
	}

	@Override
	public boolean hasPrivateLayouts() {
		return _group.hasPrivateLayouts();
	}

	@Override
	public boolean hasPublicLayouts() {
		return _group.hasPublicLayouts();
	}

	@Override
	public boolean hasRemoteStagingGroup() {
		return _group.hasRemoteStagingGroup();
	}

	@Override
	public boolean hasStagingGroup() {
		return _group.hasStagingGroup();
	}

	/**
	* Returns <code>true</code> if this group is active.
	*
	* @return <code>true</code> if this group is active; <code>false</code> otherwise
	*/
	@Override
	public boolean isActive() {
		return _group.isActive();
	}

	@Override
	public boolean isCachedModel() {
		return _group.isCachedModel();
	}

	/**
	* @deprecated As of 7.0.0, replaced by {@link #hasAncestor}
	*/
	@Deprecated
	@Override
	public boolean isChild(long groupId) {
		return _group.isChild(groupId);
	}

	@Override
	public boolean isCompany() {
		return _group.isCompany();
	}

	@Override
	public boolean isCompanyStagingGroup() {
		return _group.isCompanyStagingGroup();
	}

	@Override
	public boolean isControlPanel() {
		return _group.isControlPanel();
	}

	@Override
	public boolean isEscapedModel() {
		return _group.isEscapedModel();
	}

	@Override
	public boolean isGuest() {
		return _group.isGuest();
	}

	@Override
	public boolean isInStagingPortlet(java.lang.String portletId) {
		return _group.isInStagingPortlet(portletId);
	}

	/**
	* Returns <code>true</code> if this group is inherit content.
	*
	* @return <code>true</code> if this group is inherit content; <code>false</code> otherwise
	*/
	@Override
	public boolean isInheritContent() {
		return _group.isInheritContent();
	}

	@Override
	public boolean isLayout() {
		return _group.isLayout();
	}

	@Override
	public boolean isLayoutPrototype() {
		return _group.isLayoutPrototype();
	}

	@Override
	public boolean isLayoutSetPrototype() {
		return _group.isLayoutSetPrototype();
	}

	@Override
	public boolean isLimitedToParentSiteMembers() {
		return _group.isLimitedToParentSiteMembers();
	}

	/**
	* Returns <code>true</code> if this group is manual membership.
	*
	* @return <code>true</code> if this group is manual membership; <code>false</code> otherwise
	*/
	@Override
	public boolean isManualMembership() {
		return _group.isManualMembership();
	}

	@Override
	public boolean isNew() {
		return _group.isNew();
	}

	@Override
	public boolean isOrganization() {
		return _group.isOrganization();
	}

	@Override
	public boolean isRegularSite() {
		return _group.isRegularSite();
	}

	@Override
	public boolean isRoot() {
		return _group.isRoot();
	}

	@Override
	public boolean isShowSite(
		com.liferay.portal.kernel.security.permission.PermissionChecker permissionChecker,
		boolean privateSite)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _group.isShowSite(permissionChecker, privateSite);
	}

	/**
	* Returns <code>true</code> if this group is site.
	*
	* @return <code>true</code> if this group is site; <code>false</code> otherwise
	*/
	@Override
	public boolean isSite() {
		return _group.isSite();
	}

	@Override
	public boolean isStaged() {
		return _group.isStaged();
	}

	@Override
	public boolean isStagedPortlet(java.lang.String portletId) {
		return _group.isStagedPortlet(portletId);
	}

	@Override
	public boolean isStagedRemotely() {
		return _group.isStagedRemotely();
	}

	@Override
	public boolean isStagingGroup() {
		return _group.isStagingGroup();
	}

	@Override
	public boolean isUser() {
		return _group.isUser();
	}

	@Override
	public boolean isUserGroup() {
		return _group.isUserGroup();
	}

	@Override
	public boolean isUserPersonalSite() {
		return _group.isUserPersonalSite();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _group.getExpandoBridge();
	}

	@Override
	public Group getLiveGroup() {
		return _group.getLiveGroup();
	}

	@Override
	public Group getParentGroup() {
		return _group.getParentGroup();
	}

	@Override
	public Group getStagingGroup() {
		return _group.getStagingGroup();
	}

	@Override
	public com.liferay.portal.kernel.util.UnicodeProperties getParentLiveGroupTypeSettingsProperties() {
		return _group.getParentLiveGroupTypeSettingsProperties();
	}

	@Override
	public com.liferay.portal.kernel.util.UnicodeProperties getTypeSettingsProperties() {
		return _group.getTypeSettingsProperties();
	}

	@Override
	public int compareTo(Group group) {
		return _group.compareTo(group);
	}

	@Override
	public int getChildrenWithLayoutsCount(boolean site) {
		return _group.getChildrenWithLayoutsCount(site);
	}

	/**
	* Returns the membership restriction of this group.
	*
	* @return the membership restriction of this group
	*/
	@Override
	public int getMembershipRestriction() {
		return _group.getMembershipRestriction();
	}

	@Override
	public int getPrivateLayoutsPageCount() {
		return _group.getPrivateLayoutsPageCount();
	}

	@Override
	public int getPublicLayoutsPageCount() {
		return _group.getPublicLayoutsPageCount();
	}

	/**
	* Returns the remote staging group count of this group.
	*
	* @return the remote staging group count of this group
	*/
	@Override
	public int getRemoteStagingGroupCount() {
		return _group.getRemoteStagingGroupCount();
	}

	/**
	* Returns the type of this group.
	*
	* @return the type of this group
	*/
	@Override
	public int getType() {
		return _group.getType();
	}

	@Override
	public int hashCode() {
		return _group.hashCode();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _group.getPrimaryKeyObj();
	}

	@Override
	public java.lang.Object clone() {
		return new GroupWrapper((Group)_group.clone());
	}

	@Override
	public java.lang.String buildTreePath()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _group.buildTreePath();
	}

	/**
	* Returns the fully qualified class name of this group.
	*
	* @return the fully qualified class name of this group
	*/
	@Override
	public java.lang.String getClassName() {
		return _group.getClassName();
	}

	/**
	* Returns the creator user uuid of this group.
	*
	* @return the creator user uuid of this group
	*/
	@Override
	public java.lang.String getCreatorUserUuid() {
		return _group.getCreatorUserUuid();
	}

	/**
	* Returns the default language ID of this group.
	*
	* @return the default language ID of this group
	*/
	@Override
	public java.lang.String getDefaultLanguageId() {
		return _group.getDefaultLanguageId();
	}

	@Override
	public java.lang.String getDescription() {
		return _group.getDescription();
	}

	@Override
	public java.lang.String getDescription(java.lang.String languageId) {
		return _group.getDescription(languageId);
	}

	@Override
	public java.lang.String getDescription(java.lang.String languageId,
		boolean useDefault) {
		return _group.getDescription(languageId, useDefault);
	}

	@Override
	public java.lang.String getDescriptionMapAsXML() {
		return _group.getDescriptionMapAsXML();
	}

	@Override
	public java.lang.String getDescriptiveName()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _group.getDescriptiveName();
	}

	@Override
	public java.lang.String getDescriptiveName(java.util.Locale locale)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _group.getDescriptiveName(locale);
	}

	@Override
	public java.lang.String getDisplayURL(
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay) {
		return _group.getDisplayURL(themeDisplay);
	}

	@Override
	public java.lang.String getDisplayURL(
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay,
		boolean privateLayout) {
		return _group.getDisplayURL(themeDisplay, privateLayout);
	}

	/**
	* Returns the friendly url of this group.
	*
	* @return the friendly url of this group
	*/
	@Override
	public java.lang.String getFriendlyURL() {
		return _group.getFriendlyURL();
	}

	/**
	* Returns the group key of this group.
	*
	* @return the group key of this group
	*/
	@Override
	public java.lang.String getGroupKey() {
		return _group.getGroupKey();
	}

	@Override
	public java.lang.String getIconCssClass() {
		return _group.getIconCssClass();
	}

	@Override
	public java.lang.String getIconURL(
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay) {
		return _group.getIconURL(themeDisplay);
	}

	@Override
	public java.lang.String getLayoutRootNodeName(boolean privateLayout,
		java.util.Locale locale) {
		return _group.getLayoutRootNodeName(privateLayout, locale);
	}

	@Override
	public java.lang.String getLiveParentTypeSettingsProperty(
		java.lang.String key) {
		return _group.getLiveParentTypeSettingsProperty(key);
	}

	@Override
	public java.lang.String getLogoURL(
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay,
		boolean useDefault) {
		return _group.getLogoURL(themeDisplay, useDefault);
	}

	@Override
	public java.lang.String getName() {
		return _group.getName();
	}

	@Override
	public java.lang.String getName(java.lang.String languageId) {
		return _group.getName(languageId);
	}

	@Override
	public java.lang.String getName(java.lang.String languageId,
		boolean useDefault) {
		return _group.getName(languageId, useDefault);
	}

	@Override
	public java.lang.String getNameMapAsXML() {
		return _group.getNameMapAsXML();
	}

	@Override
	public java.lang.String getPathFriendlyURL(boolean privateLayout,
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay) {
		return _group.getPathFriendlyURL(privateLayout, themeDisplay);
	}

	@Override
	public java.lang.String getScopeDescriptiveName(
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _group.getScopeDescriptiveName(themeDisplay);
	}

	@Override
	public java.lang.String getScopeLabel(
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay) {
		return _group.getScopeLabel(themeDisplay);
	}

	/**
	* Returns the tree path of this group.
	*
	* @return the tree path of this group
	*/
	@Override
	public java.lang.String getTreePath() {
		return _group.getTreePath();
	}

	@Override
	public java.lang.String getTypeLabel() {
		return _group.getTypeLabel();
	}

	/**
	* Returns the type settings of this group.
	*
	* @return the type settings of this group
	*/
	@Override
	public java.lang.String getTypeSettings() {
		return _group.getTypeSettings();
	}

	@Override
	public java.lang.String getTypeSettingsProperty(java.lang.String key) {
		return _group.getTypeSettingsProperty(key);
	}

	@Override
	public java.lang.String getUnambiguousName(java.lang.String name,
		java.util.Locale locale) {
		return _group.getUnambiguousName(name, locale);
	}

	/**
	* Returns the uuid of this group.
	*
	* @return the uuid of this group
	*/
	@Override
	public java.lang.String getUuid() {
		return _group.getUuid();
	}

	@Override
	public java.lang.String toString() {
		return _group.toString();
	}

	@Override
	public java.lang.String toXmlString() {
		return _group.toXmlString();
	}

	@Override
	public java.lang.String[] getAvailableLanguageIds() {
		return _group.getAvailableLanguageIds();
	}

	@Override
	public java.util.List<Group> getAncestors() {
		return _group.getAncestors();
	}

	@Override
	public java.util.List<Group> getChildren(boolean site) {
		return _group.getChildren(site);
	}

	/**
	* @deprecated As of 7.0.0, replaced by {@link
	#getChildrenWithLayouts(boolean, int, int,
	OrderByComparator)}
	*/
	@Deprecated
	@Override
	public java.util.List<Group> getChildrenWithLayouts(boolean site,
		int start, int end) {
		return _group.getChildrenWithLayouts(site, start, end);
	}

	@Override
	public java.util.List<Group> getChildrenWithLayouts(boolean site,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Group> obc) {
		return _group.getChildrenWithLayouts(site, start, end, obc);
	}

	@Override
	public java.util.List<Group> getDescendants(boolean site) {
		return _group.getDescendants(site);
	}

	@Override
	public Map<java.lang.String, java.lang.String> getLanguageIdToDescriptionMap() {
		return _group.getLanguageIdToDescriptionMap();
	}

	@Override
	public Map<java.lang.String, java.lang.String> getLanguageIdToNameMap() {
		return _group.getLanguageIdToNameMap();
	}

	/**
	* Returns the class name ID of this group.
	*
	* @return the class name ID of this group
	*/
	@Override
	public long getClassNameId() {
		return _group.getClassNameId();
	}

	/**
	* Returns the class pk of this group.
	*
	* @return the class pk of this group
	*/
	@Override
	public long getClassPK() {
		return _group.getClassPK();
	}

	/**
	* Returns the company ID of this group.
	*
	* @return the company ID of this group
	*/
	@Override
	public long getCompanyId() {
		return _group.getCompanyId();
	}

	/**
	* Returns the creator user ID of this group.
	*
	* @return the creator user ID of this group
	*/
	@Override
	public long getCreatorUserId() {
		return _group.getCreatorUserId();
	}

	@Override
	public long getDefaultPrivatePlid() {
		return _group.getDefaultPrivatePlid();
	}

	@Override
	public long getDefaultPublicPlid() {
		return _group.getDefaultPublicPlid();
	}

	/**
	* Returns the group ID of this group.
	*
	* @return the group ID of this group
	*/
	@Override
	public long getGroupId() {
		return _group.getGroupId();
	}

	/**
	* Returns the live group ID of this group.
	*
	* @return the live group ID of this group
	*/
	@Override
	public long getLiveGroupId() {
		return _group.getLiveGroupId();
	}

	/**
	* Returns the mvcc version of this group.
	*
	* @return the mvcc version of this group
	*/
	@Override
	public long getMvccVersion() {
		return _group.getMvccVersion();
	}

	@Override
	public long getOrganizationId() {
		return _group.getOrganizationId();
	}

	/**
	* Returns the parent group ID of this group.
	*
	* @return the parent group ID of this group
	*/
	@Override
	public long getParentGroupId() {
		return _group.getParentGroupId();
	}

	/**
	* Returns the primary key of this group.
	*
	* @return the primary key of this group
	*/
	@Override
	public long getPrimaryKey() {
		return _group.getPrimaryKey();
	}

	@Override
	public long getRemoteLiveGroupId() {
		return _group.getRemoteLiveGroupId();
	}

	@Override
	public void clearStagingGroup() {
		_group.clearStagingGroup();
	}

	@Override
	public void persist() {
		_group.persist();
	}

	/**
	* Sets whether this group is active.
	*
	* @param active the active of this group
	*/
	@Override
	public void setActive(boolean active) {
		_group.setActive(active);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_group.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(java.lang.String className) {
		_group.setClassName(className);
	}

	/**
	* Sets the class name ID of this group.
	*
	* @param classNameId the class name ID of this group
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_group.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this group.
	*
	* @param classPK the class pk of this group
	*/
	@Override
	public void setClassPK(long classPK) {
		_group.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this group.
	*
	* @param companyId the company ID of this group
	*/
	@Override
	public void setCompanyId(long companyId) {
		_group.setCompanyId(companyId);
	}

	/**
	* Sets the creator user ID of this group.
	*
	* @param creatorUserId the creator user ID of this group
	*/
	@Override
	public void setCreatorUserId(long creatorUserId) {
		_group.setCreatorUserId(creatorUserId);
	}

	/**
	* Sets the creator user uuid of this group.
	*
	* @param creatorUserUuid the creator user uuid of this group
	*/
	@Override
	public void setCreatorUserUuid(java.lang.String creatorUserUuid) {
		_group.setCreatorUserUuid(creatorUserUuid);
	}

	/**
	* Sets the default language ID of this group.
	*
	* @param defaultLanguageId the default language ID of this group
	*/
	@Override
	public void setDefaultLanguageId(java.lang.String defaultLanguageId) {
		_group.setDefaultLanguageId(defaultLanguageId);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_group.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_group.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_group.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the friendly url of this group.
	*
	* @param friendlyURL the friendly url of this group
	*/
	@Override
	public void setFriendlyURL(java.lang.String friendlyURL) {
		_group.setFriendlyURL(friendlyURL);
	}

	/**
	* Sets the group ID of this group.
	*
	* @param groupId the group ID of this group
	*/
	@Override
	public void setGroupId(long groupId) {
		_group.setGroupId(groupId);
	}

	/**
	* Sets the group key of this group.
	*
	* @param groupKey the group key of this group
	*/
	@Override
	public void setGroupKey(java.lang.String groupKey) {
		_group.setGroupKey(groupKey);
	}

	/**
	* Sets whether this group is inherit content.
	*
	* @param inheritContent the inherit content of this group
	*/
	@Override
	public void setInheritContent(boolean inheritContent) {
		_group.setInheritContent(inheritContent);
	}

	/**
	* Sets the live group ID of this group.
	*
	* @param liveGroupId the live group ID of this group
	*/
	@Override
	public void setLiveGroupId(long liveGroupId) {
		_group.setLiveGroupId(liveGroupId);
	}

	/**
	* Sets whether this group is manual membership.
	*
	* @param manualMembership the manual membership of this group
	*/
	@Override
	public void setManualMembership(boolean manualMembership) {
		_group.setManualMembership(manualMembership);
	}

	/**
	* Sets the membership restriction of this group.
	*
	* @param membershipRestriction the membership restriction of this group
	*/
	@Override
	public void setMembershipRestriction(int membershipRestriction) {
		_group.setMembershipRestriction(membershipRestriction);
	}

	/**
	* Sets the mvcc version of this group.
	*
	* @param mvccVersion the mvcc version of this group
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_group.setMvccVersion(mvccVersion);
	}

	@Override
	public void setNew(boolean n) {
		_group.setNew(n);
	}

	/**
	* Sets the parent group ID of this group.
	*
	* @param parentGroupId the parent group ID of this group
	*/
	@Override
	public void setParentGroupId(long parentGroupId) {
		_group.setParentGroupId(parentGroupId);
	}

	/**
	* Sets the primary key of this group.
	*
	* @param primaryKey the primary key of this group
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_group.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_group.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the remote staging group count of this group.
	*
	* @param remoteStagingGroupCount the remote staging group count of this group
	*/
	@Override
	public void setRemoteStagingGroupCount(int remoteStagingGroupCount) {
		_group.setRemoteStagingGroupCount(remoteStagingGroupCount);
	}

	/**
	* Sets whether this group is site.
	*
	* @param site the site of this group
	*/
	@Override
	public void setSite(boolean site) {
		_group.setSite(site);
	}

	/**
	* Sets the tree path of this group.
	*
	* @param treePath the tree path of this group
	*/
	@Override
	public void setTreePath(java.lang.String treePath) {
		_group.setTreePath(treePath);
	}

	/**
	* Sets the type of this group.
	*
	* @param type the type of this group
	*/
	@Override
	public void setType(int type) {
		_group.setType(type);
	}

	/**
	* Sets the type settings of this group.
	*
	* @param typeSettings the type settings of this group
	*/
	@Override
	public void setTypeSettings(java.lang.String typeSettings) {
		_group.setTypeSettings(typeSettings);
	}

	@Override
	public void setTypeSettingsProperties(
		com.liferay.portal.kernel.util.UnicodeProperties typeSettingsProperties) {
		_group.setTypeSettingsProperties(typeSettingsProperties);
	}

	/**
	* Sets the uuid of this group.
	*
	* @param uuid the uuid of this group
	*/
	@Override
	public void setUuid(java.lang.String uuid) {
		_group.setUuid(uuid);
	}

	@Override
	public void updateTreePath(java.lang.String treePath) {
		_group.updateTreePath(treePath);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof GroupWrapper)) {
			return false;
		}

		GroupWrapper groupWrapper = (GroupWrapper)obj;

		if (Objects.equals(_group, groupWrapper._group)) {
			return true;
		}

		return false;
	}

	@Override
	public Group getWrappedModel() {
		return _group;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _group.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _group.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_group.resetOriginalValues();
	}

	private final Group _group;
}