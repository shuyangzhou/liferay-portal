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

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.security.InvalidParameterException;

/**
 * @author Brian Wing Shun Chan
 * @author Jorge Ferrer
 * @deprecated As of 7.0.0, with no direct replacement
 */
@Deprecated
public class PortletInstance {

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *				com.liferay.portal.kernel.util.PortalUtil#
	 *					getMaxPortletIdLength()}
	 */
	@Deprecated
	public static final int PORTLET_INSTANCE_KEY_MAX_LENGTH =
		255 - PortletInstance._INSTANCE_SEPARATOR.length() +
			PortletInstance._USER_SEPARATOR.length() + 39;

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public static PortletInstance fromPortletInstanceKey(
		String portletInstanceKey) {

		return new PortletInstance(
			_getPortletName(portletInstanceKey), _getUserId(portletInstanceKey),
			_getInstanceId(portletInstanceKey));
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public static PortletInstance fromPortletNameAndUserIdAndInstanceId(
		String portletName, String userIdAndInstanceId) {

		UserIdAndInstanceIdEncoder userIdAndInstanceIdEncoder =
			_buildUserIdAndInstanceIdEncoder(userIdAndInstanceId);

		return new PortletInstance(
			portletName, userIdAndInstanceIdEncoder.getUserId(),
			userIdAndInstanceIdEncoder.getInstanceId());
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public PortletInstance(String portletName) {
		this(portletName, StringUtil.randomString(12));
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public PortletInstance(String portletName, long userId) {
		this(portletName, userId, null);
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public PortletInstance(String portletName, long userId, String instanceId) {
		_validatePortletName(portletName);

		_portletName = portletName;
		_userId = userId;
		_instanceId = instanceId;
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public PortletInstance(String portletName, String instanceId) {
		this(portletName, 0, instanceId);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *				PortletConstants#getInstanceId(java.lang.String)}
	 */
	@Deprecated
	public String getInstanceId() {
		return _instanceId;
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *				PortletConstants#assemblePortletId(
	 *					java.lang.String, long, java.lang.String)}
	 */
	@Deprecated
	public String getPortletInstanceKey() {
		return PortletConstants.assemblePortletId(
			_portletName, _userId, _instanceId);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *				PortletConstants#getPortletName(java.lang.String)}
	 */
	@Deprecated
	public String getPortletName() {
		return _portletName;
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *				PortletConstants#getUserId(java.lang.String)}
	 */
	@Deprecated
	public long getUserId() {
		return _userId;
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public String getUserIdAndInstanceId() {
		UserIdAndInstanceIdEncoder userIdAndInstanceIdEncoder =
			new UserIdAndInstanceIdEncoder(_userId, _instanceId);

		return userIdAndInstanceIdEncoder.encode();
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *				PortletConstants#hasIdenticalRootPortletId(
	 *					java.lang.String, java.lang.String)}
	 */
	@Deprecated
	public boolean hasIdenticalPortletName(PortletInstance portletInstance) {
		return hasIdenticalPortletName(portletInstance.getPortletName());
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *				PortletConstants#hasIdenticalRootPortletId(
	 *					java.lang.String, java.lang.String)}
	 */
	@Deprecated
	public boolean hasIdenticalPortletName(String portletName) {
		return _portletName.equals(portletName);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *				PortletConstants#hasInstanceId(java.lang.String)}
	 */
	@Deprecated
	public boolean hasInstanceId() {
		return Validator.isNotNull(_instanceId);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *				PortletConstants#hasUserId(java.lang.String)}
	 */
	@Deprecated
	public boolean hasUserId() {
		if (_userId > 0) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return getPortletInstanceKey();
	}

	private static UserIdAndInstanceIdEncoder _buildUserIdAndInstanceIdEncoder(
		String userIdAndInstanceId) {

		if (userIdAndInstanceId == null) {
			throw new InvalidParameterException(
				"User ID and instance ID are null");
		}

		if (userIdAndInstanceId.isEmpty()) {
			return new UserIdAndInstanceIdEncoder(0, null);
		}

		int slashCount = StringUtil.count(userIdAndInstanceId, CharPool.SLASH);

		if (slashCount > 0) {
			throw new InvalidParameterException(
				"User ID and instance ID contain slashes");
		}

		int underlineCount = StringUtil.count(
			userIdAndInstanceId, CharPool.UNDERLINE);

		if (underlineCount > 1) {
			throw new InvalidParameterException(
				"User ID and instance ID has more than one underscore");
		}

		if (underlineCount == 1) {
			int index = userIdAndInstanceId.indexOf(CharPool.UNDERLINE);

			long userId = GetterUtil.getLong(
				userIdAndInstanceId.substring(0, index), -1);

			if (userId == -1) {
				throw new InvalidParameterException("User ID is not a number");
			}

			String instanceId = null;

			if (index < (userIdAndInstanceId.length() - 1)) {
				instanceId = userIdAndInstanceId.substring(index + 1);
			}

			return new UserIdAndInstanceIdEncoder(userId, instanceId);
		}

		return new UserIdAndInstanceIdEncoder(0, userIdAndInstanceId);
	}

	private static String _getInstanceId(String portletInstanceKey) {
		return PortletConstants.getInstanceId(portletInstanceKey);
	}

	private static String _getPortletName(String portletInstanceKey) {
		return PortletConstants.getRootPortletId(portletInstanceKey);
	}

	private static long _getUserId(String portletInstanceKey) {
		return PortletConstants.getUserId(portletInstanceKey);
	}

	private void _validatePortletName(String portletName) {
		for (String keyword : _PORTLET_NAME_KEYWORDS) {
			if (!portletName.contains(keyword)) {
				continue;
			}

			throw new InvalidParameterException(
				"The portletName '" + portletName +
					"' must not contain the keyword " + keyword);
		}
	}

	private static final String _INSTANCE_SEPARATOR = "_INSTANCE_";

	private static final String[] _PORTLET_NAME_KEYWORDS =
		{PortletInstance._INSTANCE_SEPARATOR, PortletInstance._USER_SEPARATOR};

	private static final String _USER_SEPARATOR = "_USER_";

	private final String _instanceId;
	private final String _portletName;
	private final long _userId;

	private static final class UserIdAndInstanceIdEncoder {

		public UserIdAndInstanceIdEncoder(long userId, String instanceId) {
			_userId = userId;
			_instanceId = instanceId;
		}

		public String encode() {
			if ((_userId <= 0) && Validator.isBlank(_instanceId)) {
				return null;
			}

			StringBundler sb = new StringBundler(3);

			if (_userId > 0) {
				sb.append(_userId);
				sb.append(StringPool.UNDERLINE);
			}

			if (_instanceId != null) {
				sb.append(_instanceId);
			}

			return sb.toString();
		}

		public String getInstanceId() {
			return _instanceId;
		}

		public long getUserId() {
			return _userId;
		}

		@Override
		public String toString() {
			return encode();
		}

		private String _instanceId;
		private long _userId;

	}

}