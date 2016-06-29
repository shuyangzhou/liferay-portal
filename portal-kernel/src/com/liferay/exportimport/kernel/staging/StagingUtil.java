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

package com.liferay.exportimport.kernel.staging;

import aQute.bnd.annotation.ProviderType;

import com.liferay.exportimport.kernel.lar.MissingReference;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutRevision;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ServiceRetriever;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.workflow.WorkflowTask;
import com.liferay.portal.kernel.xml.Element;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Raymond Augé
 */
@ProviderType
public class StagingUtil {

	public static String buildRemoteURL(
		String remoteAddress, int remotePort, String remotePathContext,
		boolean secureConnection) {

		return _getStaging().buildRemoteURL(
			remoteAddress, remotePort, remotePathContext, secureConnection);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #getRemoteSiteURL(Group,
	 *             boolean)}
	 */
	@Deprecated
	public static String buildRemoteURL(
		String remoteAddress, int remotePort, String remotePathContext,
		boolean secureConnection, long remoteGroupId, boolean privateLayout) {

		return _getStaging().buildRemoteURL(
			remoteAddress, remotePort, remotePathContext, secureConnection,
			remoteGroupId, privateLayout);
	}

	public static String buildRemoteURL(
		UnicodeProperties typeSettingsProperties) {

		return _getStaging().buildRemoteURL(typeSettingsProperties);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             com.liferay.exportimport.kernel.service.StagingLocalServiceUtil#
	 *             checkDefaultLayoutSetBranches(long, Group, boolean, boolean,
	 *             boolean, ServiceContext)}
	 */
	@Deprecated
	public static void checkDefaultLayoutSetBranches(
			long userId, Group liveGroup, boolean branchingPublic,
			boolean branchingPrivate, boolean remote,
			ServiceContext serviceContext)
		throws PortalException {

		_getStaging().checkDefaultLayoutSetBranches(
			userId, liveGroup, branchingPublic, branchingPrivate, remote,
			serviceContext);
	}

	public static long copyFromLive(PortletRequest portletRequest)
		throws PortalException {

		return _getStaging().copyFromLive(portletRequest);
	}

	public static long copyFromLive(
			PortletRequest portletRequest, Portlet portlet)
		throws PortalException {

		return _getStaging().copyFromLive(portletRequest, portlet);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #publishPortlet(long, long,
	 *             long, long, long, String, Map)}
	 */
	@Deprecated
	public static long copyPortlet(
			PortletRequest portletRequest, long sourceGroupId,
			long targetGroupId, long sourcePlid, long targetPlid,
			String portletId)
		throws PortalException {

		return _getStaging().copyPortlet(
			portletRequest, sourceGroupId, targetGroupId, sourcePlid,
			targetPlid, portletId);
	}

	public static long copyRemoteLayouts(
			ExportImportConfiguration exportImportConfiguration)
		throws PortalException {

		return _getStaging().copyRemoteLayouts(exportImportConfiguration);
	}

	public static long copyRemoteLayouts(long exportImportConfigurationId)
		throws PortalException {

		return _getStaging().copyRemoteLayouts(exportImportConfigurationId);
	}

	public static long copyRemoteLayouts(
			long sourceGroupId, boolean privateLayout,
			Map<Long, Boolean> layoutIdMap, Map<String, String[]> parameterMap,
			String remoteAddress, int remotePort, String remotePathContext,
			boolean secureConnection, long remoteGroupId,
			boolean remotePrivateLayout)
		throws PortalException {

		return _getStaging().copyRemoteLayouts(
			sourceGroupId, privateLayout, layoutIdMap, parameterMap,
			remoteAddress, remotePort, remotePathContext, secureConnection,
			remoteGroupId, remotePrivateLayout);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #copyRemoteLayouts(long,
	 *             boolean, Map, Map, String, int, String, boolean, long,
	 *             boolean)}
	 */
	@Deprecated
	public static long copyRemoteLayouts(
			long sourceGroupId, boolean privateLayout,
			Map<Long, Boolean> layoutIdMap, Map<String, String[]> parameterMap,
			String remoteAddress, int remotePort, String remotePathContext,
			boolean secureConnection, long remoteGroupId,
			boolean remotePrivateLayout, Date startDate, Date endDate)
		throws PortalException {

		return _getStaging().copyRemoteLayouts(
			sourceGroupId, privateLayout, layoutIdMap, parameterMap,
			remoteAddress, remotePort, remotePathContext, secureConnection,
			remoteGroupId, remotePrivateLayout, startDate, endDate);
	}

	public static long copyRemoteLayouts(
			long sourceGroupId, boolean privateLayout,
			Map<Long, Boolean> layoutIdMap, String name,
			Map<String, String[]> parameterMap, String remoteAddress,
			int remotePort, String remotePathContext, boolean secureConnection,
			long remoteGroupId, boolean remotePrivateLayout)
		throws PortalException {

		return _getStaging().copyRemoteLayouts(
			sourceGroupId, privateLayout, layoutIdMap, name, parameterMap,
			remoteAddress, remotePort, remotePathContext, secureConnection,
			remoteGroupId, remotePrivateLayout);
	}

	public static void deleteLastImportSettings(
			Group liveGroup, boolean privateLayout)
		throws PortalException {

		_getStaging().deleteLastImportSettings(liveGroup, privateLayout);
	}

	public static void deleteRecentLayoutRevisionId(
		HttpServletRequest request, long layoutSetBranchId, long plid) {

		_getStaging().deleteRecentLayoutRevisionId(
			request, layoutSetBranchId, plid);
	}

	public static void deleteRecentLayoutRevisionId(
		long userId, long layoutSetBranchId, long plid) {

		_getStaging().deleteRecentLayoutRevisionId(
			userId, layoutSetBranchId, plid);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             #deleteRecentLayoutRevisionId(long, long, long)}
	 */
	@Deprecated
	public static void deleteRecentLayoutRevisionId(
		User user, long layoutSetBranchId, long plid) {

		_getStaging().deleteRecentLayoutRevisionId(
			user, layoutSetBranchId, plid);
	}

	public static JSONArray getErrorMessagesJSONArray(
		Locale locale, Map<String, MissingReference> missingReferences) {

		return _getStaging().getErrorMessagesJSONArray(
			locale, missingReferences);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             #getErrorMessagesJSONArray(Locale, Map<String,
	 *             MissingReference>)}
	 */
	@Deprecated
	public static JSONArray getErrorMessagesJSONArray(
		Locale locale, Map<String, MissingReference> missingReferences,
		Map<String, Serializable> contextMap) {

		return _getStaging().getErrorMessagesJSONArray(
			locale, missingReferences, contextMap);
	}

	public static JSONObject getExceptionMessagesJSONObject(
		Locale locale, Exception e,
		ExportImportConfiguration exportImportConfiguration) {

		return _getStaging().getExceptionMessagesJSONObject(
			locale, e, exportImportConfiguration);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             #getExceptionMessagesJSONObject(Locale, Exception,
	 *             ExportImportConfiguration)}
	 */
	@Deprecated
	public static JSONObject getExceptionMessagesJSONObject(
		Locale locale, Exception e, Map<String, Serializable> contextMap) {

		return _getStaging().getExceptionMessagesJSONObject(
			locale, e, contextMap);
	}

	public static Group getLiveGroup(long groupId) {
		return _getStaging().getLiveGroup(groupId);
	}

	public static long getLiveGroupId(long groupId) {
		return _getStaging().getLiveGroupId(groupId);
	}

	/**
	 * @deprecated As of 7.0.0, moved to {@link
	 *             com.liferay.exportimport.kernel.lar.ExportImportHelperUtil#getMissingParentLayouts(
	 *             Layout, long)}
	 */
	@Deprecated
	public static List<Layout> getMissingParentLayouts(
			Layout layout, long liveGroupId)
		throws Exception {

		return _getStaging().getMissingParentLayouts(layout, liveGroupId);
	}

	public static long getRecentLayoutRevisionId(
			HttpServletRequest request, long layoutSetBranchId, long plid)
		throws PortalException {

		return _getStaging().getRecentLayoutRevisionId(
			request, layoutSetBranchId, plid);
	}

	public static long getRecentLayoutRevisionId(
			User user, long layoutSetBranchId, long plid)
		throws PortalException {

		return _getStaging().getRecentLayoutRevisionId(
			user, layoutSetBranchId, plid);
	}

	public static long getRecentLayoutSetBranchId(
		HttpServletRequest request, long layoutSetId) {

		return _getStaging().getRecentLayoutSetBranchId(request, layoutSetId);
	}

	public static long getRecentLayoutSetBranchId(User user, long layoutSetId) {
		return _getStaging().getRecentLayoutSetBranchId(user, layoutSetId);
	}

	public static String getRemoteSiteURL(
			Group stagingGroup, boolean privateLayout)
		throws PortalException {

		return _getStaging().getRemoteSiteURL(stagingGroup, privateLayout);
	}

	public static String getSchedulerGroupName(
		String destinationName, long groupId) {

		return _getStaging().getSchedulerGroupName(destinationName, groupId);
	}

	public static String getStagedPortletId(String portletId) {
		return _getStaging().getStagedPortletId(portletId);
	}

	public static long[] getStagingAndLiveGroupIds(long groupId)
		throws PortalException {

		return _getStaging().getStagingAndLiveGroupIds(groupId);
	}

	public static Group getStagingGroup(long groupId) {
		return _getStaging().getStagingGroup(groupId);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             com.liferay.exportimport.kernel.configuration.ExportImportConfigurationParameterMapFactory#buildParameterMap(
	 *             )}
	 */
	@Deprecated
	public static Map<String, String[]> getStagingParameters() {
		return _getStaging().getStagingParameters();
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             com.liferay.exportimport.kernel.configuration.ExportImportConfigurationParameterMapFactory#buildParameterMap(
	 *             PortletRequest)}
	 */
	@Deprecated
	public static Map<String, String[]> getStagingParameters(
		PortletRequest portletRequest) {

		return _getStaging().getStagingParameters(portletRequest);
	}

	public static JSONArray getWarningMessagesJSONArray(
		Locale locale, Map<String, MissingReference> missingReferences) {

		return _getStaging().getWarningMessagesJSONArray(
			locale, missingReferences);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             #getWarningMessagesJSONArray(Locale, Map<String,
	 *             MissingReference>)}
	 */
	@Deprecated
	public static JSONArray getWarningMessagesJSONArray(
		Locale locale, Map<String, MissingReference> missingReferences,
		Map<String, Serializable> contextMap) {

		return _getStaging().getWarningMessagesJSONArray(
			locale, missingReferences, contextMap);
	}

	public static WorkflowTask getWorkflowTask(
			long userId, LayoutRevision layoutRevision)
		throws PortalException {

		return _getStaging().getWorkflowTask(userId, layoutRevision);
	}

	public static boolean hasWorkflowTask(
			long userId, LayoutRevision layoutRevision)
		throws PortalException {

		return _getStaging().hasWorkflowTask(userId, layoutRevision);
	}

	public static boolean isGroupAccessible(Group group, Group fromGroup) {
		return _getStaging().isGroupAccessible(group, fromGroup);
	}

	public static boolean isGroupAccessible(long groupId, long fromGroupId)
		throws PortalException {

		return _getStaging().isGroupAccessible(groupId, fromGroupId);
	}

	public static boolean isIncomplete(Layout layout, long layoutSetBranchId) {
		return _getStaging().isIncomplete(layout, layoutSetBranchId);
	}

	/**
	 * @deprecated As of 7.0.0, see {@link
	 *             com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor#getIsolationLevel(
	 *             )}
	 */
	@Deprecated
	public static void lockGroup(long userId, long groupId)
		throws PortalException {

		_getStaging().lockGroup(userId, groupId);
	}

	public static long publishLayout(
			long userId, long plid, long liveGroupId, boolean includeChildren)
		throws PortalException {

		return _getStaging().publishLayout(
			userId, plid, liveGroupId, includeChildren);
	}

	public static long publishLayouts(
			long userId, ExportImportConfiguration exportImportConfiguration)
		throws PortalException {

		return _getStaging().publishLayouts(userId, exportImportConfiguration);
	}

	public static long publishLayouts(
			long userId, long exportImportConfigurationId)
		throws PortalException {

		return _getStaging().publishLayouts(
			userId, exportImportConfigurationId);
	}

	public static long publishLayouts(
			long userId, long sourceGroupId, long targetGroupId,
			boolean privateLayout, long[] layoutIds,
			Map<String, String[]> parameterMap)
		throws PortalException {

		return _getStaging().publishLayouts(
			userId, sourceGroupId, targetGroupId, privateLayout, layoutIds,
			parameterMap);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #publishLayouts(long, long,
	 *             long, boolean, long[], Map)}
	 */
	@Deprecated
	public static long publishLayouts(
			long userId, long sourceGroupId, long targetGroupId,
			boolean privateLayout, long[] layoutIds,
			Map<String, String[]> parameterMap, Date startDate, Date endDate)
		throws PortalException {

		return _getStaging().publishLayouts(
			userId, sourceGroupId, targetGroupId, privateLayout, layoutIds,
			parameterMap, startDate, endDate);
	}

	public static long publishLayouts(
			long userId, long sourceGroupId, long targetGroupId,
			boolean privateLayout, long[] layoutIds, String name,
			Map<String, String[]> parameterMap)
		throws PortalException {

		return _getStaging().publishLayouts(
			userId, sourceGroupId, targetGroupId, privateLayout, layoutIds,
			name, parameterMap);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #publishLayouts(long, long,
	 *             long, boolean, long[], Map)}
	 */
	@Deprecated
	public static long publishLayouts(
			long userId, long sourceGroupId, long targetGroupId,
			boolean privateLayout, Map<Long, Boolean> layoutIdMap,
			Map<String, String[]> parameterMap, Date startDate, Date endDate)
		throws PortalException {

		return _getStaging().publishLayouts(
			userId, sourceGroupId, targetGroupId, privateLayout, layoutIdMap,
			parameterMap, startDate, endDate);
	}

	public static long publishLayouts(
			long userId, long sourceGroupId, long targetGroupId,
			boolean privateLayout, Map<String, String[]> parameterMap)
		throws PortalException {

		return _getStaging().publishLayouts(
			userId, sourceGroupId, targetGroupId, privateLayout, parameterMap);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #publishLayouts(long, long,
	 *             long, boolean, Map)}
	 */
	@Deprecated
	public static long publishLayouts(
			long userId, long sourceGroupId, long targetGroupId,
			boolean privateLayout, Map<String, String[]> parameterMap,
			Date startDate, Date endDate)
		throws PortalException {

		return _getStaging().publishLayouts(
			userId, sourceGroupId, targetGroupId, privateLayout, parameterMap,
			startDate, endDate);
	}

	public static long publishPortlet(
			long userId, ExportImportConfiguration exportImportConfiguration)
		throws PortalException {

		return _getStaging().publishPortlet(userId, exportImportConfiguration);
	}

	public static long publishPortlet(
			long userId, long exportImportConfigurationId)
		throws PortalException {

		return _getStaging().publishPortlet(
			userId, exportImportConfigurationId);
	}

	public static long publishPortlet(
			long userId, long sourceGroupId, long targetGroupId,
			long sourcePlid, long targetPlid, String portletId,
			Map<String, String[]> parameterMap)
		throws PortalException {

		return _getStaging().publishPortlet(
			userId, sourceGroupId, targetGroupId, sourcePlid, targetPlid,
			portletId, parameterMap);
	}

	public static long publishToLive(PortletRequest portletRequest)
		throws PortalException {

		return _getStaging().publishToLive(portletRequest);
	}

	public static long publishToLive(
			PortletRequest portletRequest, Portlet portlet)
		throws PortalException {

		return _getStaging().publishToLive(portletRequest, portlet);
	}

	public static long publishToRemote(PortletRequest portletRequest)
		throws PortalException {

		return _getStaging().publishToRemote(portletRequest);
	}

	public static void scheduleCopyFromLive(PortletRequest portletRequest)
		throws PortalException {

		_getStaging().scheduleCopyFromLive(portletRequest);
	}

	public static void schedulePublishToLive(PortletRequest portletRequest)
		throws PortalException {

		_getStaging().schedulePublishToLive(portletRequest);
	}

	public static void schedulePublishToRemote(PortletRequest portletRequest)
		throws PortalException {

		_getStaging().schedulePublishToRemote(portletRequest);
	}

	public static void setRecentLayoutBranchId(
			HttpServletRequest request, long layoutSetBranchId, long plid,
			long layoutBranchId)
		throws PortalException {

		_getStaging().setRecentLayoutBranchId(
			request, layoutSetBranchId, plid, layoutBranchId);
	}

	public static void setRecentLayoutBranchId(
			User user, long layoutSetBranchId, long plid, long layoutBranchId)
		throws PortalException {

		_getStaging().setRecentLayoutBranchId(
			user, layoutSetBranchId, plid, layoutBranchId);
	}

	public static void setRecentLayoutRevisionId(
			HttpServletRequest request, long layoutSetBranchId, long plid,
			long layoutRevisionId)
		throws PortalException {

		_getStaging().setRecentLayoutRevisionId(
			request, layoutSetBranchId, plid, layoutRevisionId);
	}

	public static void setRecentLayoutRevisionId(
			User user, long layoutSetBranchId, long plid, long layoutRevisionId)
		throws PortalException {

		_getStaging().setRecentLayoutRevisionId(
			user, layoutSetBranchId, plid, layoutRevisionId);
	}

	public static void setRecentLayoutSetBranchId(
			HttpServletRequest request, long layoutSetId,
			long layoutSetBranchId)
		throws PortalException {

		_getStaging().setRecentLayoutSetBranchId(
			request, layoutSetId, layoutSetBranchId);
	}

	public static void setRecentLayoutSetBranchId(
			User user, long layoutSetId, long layoutSetBranchId)
		throws PortalException {

		_getStaging().setRecentLayoutSetBranchId(
			user, layoutSetId, layoutSetBranchId);
	}

	public static String stripProtocolFromRemoteAddress(String remoteAddress) {
		return _getStaging().stripProtocolFromRemoteAddress(remoteAddress);
	}

	/**
	 * @deprecated As of 7.0.0, see {@link
	 *             com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor#getIsolationLevel(
	 *             )}
	 */
	@Deprecated
	public static void unlockGroup(long groupId) {
		_getStaging().unlockGroup(groupId);
	}

	public static void unscheduleCopyFromLive(PortletRequest portletRequest)
		throws PortalException {

		_getStaging().unscheduleCopyFromLive(portletRequest);
	}

	public static void unschedulePublishToLive(PortletRequest portletRequest)
		throws PortalException {

		_getStaging().unschedulePublishToLive(portletRequest);
	}

	public static void unschedulePublishToRemote(PortletRequest portletRequest)
		throws PortalException {

		_getStaging().unschedulePublishToRemote(portletRequest);
	}

	public static void updateLastImportSettings(
			Element layoutElement, Layout layout,
			PortletDataContext portletDataContext)
		throws PortalException {

		_getStaging().updateLastImportSettings(
			layoutElement, layout, portletDataContext);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             com.liferay.exportimport.kernel.lar.ExportImportDateUtil#updateLastPublishDate(
	 *             long, boolean, com.liferay.portal.kernel.util.DateRange,
	 *             Date)}
	 */
	@Deprecated
	public static void updateLastPublishDate(
			long sourceGroupId, boolean privateLayout, Date lastPublishDate)
		throws PortalException {

		_getStaging().updateLastPublishDate(
			sourceGroupId, privateLayout, lastPublishDate);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             com.liferay.exportimport.kernel.lar.ExportImportDateUtil#updateLastPublishDate(
	 *             String, PortletPreferences,
	 *             com.liferay.portal.kernel.util.DateRange, Date)}
	 */
	@Deprecated
	public static void updateLastPublishDate(
			String portletId, PortletPreferences portletPreferences,
			Date lastPublishDate)
		throws PortalException {

		_getStaging().updateLastPublishDate(
			portletId, portletPreferences, lastPublishDate);
	}

	public static void updateStaging(
			PortletRequest portletRequest, Group liveGroup)
		throws PortalException {

		_getStaging().updateStaging(portletRequest, liveGroup);
	}

	public static void validateRemote(
			long groupId, String remoteAddress, int remotePort,
			String remotePathContext, boolean secureConnection,
			long remoteGroupId)
		throws PortalException {

		_getStaging().validateRemote(
			groupId, remoteAddress, remotePort, remotePathContext,
			secureConnection, remoteGroupId);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #validateRemote(long, String,
	 *             int, String, boolean, long)}
	 */
	@Deprecated
	public static void validateRemote(
			String remoteAddress, int remotePort, String remotePathContext,
			boolean secureConnection, long remoteGroupId)
		throws PortalException {

		_getStaging().validateRemote(
			remoteAddress, remotePort, remotePathContext, secureConnection,
			remoteGroupId);
	}

	private static Staging _getStaging() {
		return _serviceRetriever.getService();
	}

	private static final ServiceRetriever<Staging> _serviceRetriever =
		new ServiceRetriever<>(Staging.class);

}