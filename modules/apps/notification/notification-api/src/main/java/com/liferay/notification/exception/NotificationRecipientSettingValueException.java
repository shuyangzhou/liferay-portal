/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.notification.exception;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Murilo Stodolni
 */
public class NotificationRecipientSettingValueException
	extends PortalException {

	public String getMessageKey() {
		return _messageKey;
	}

	public static class FromMustNotBeNull
		extends NotificationRecipientSettingValueException {

		public FromMustNotBeNull() {
			super("From is null", "from-is-required");
		}

	}

	public static class FromNameMustNotBeNull
		extends NotificationRecipientSettingValueException {

		public FromNameMustNotBeNull() {
			super("From name is null", "from-name-is-required");
		}

	}

	public static class RoleMustExist
		extends NotificationRecipientSettingValueException {

		public RoleMustExist(
			String externalReferenceCode, Throwable throwable) {

			super(
				"Unable to find role with external reference code " +
					externalReferenceCode,
				"the-role-recipient-does-not-exist", throwable);
		}

	}

	public static class ToMustNotBeNull
		extends NotificationRecipientSettingValueException {

		public ToMustNotBeNull() {
			super("To is null", "to-is-required");
		}

	}

	public static class UserGroupMustExist
		extends NotificationRecipientSettingValueException {

		public UserGroupMustExist(
			String externalReferenceCode, Throwable throwable) {

			super(
				"Unable to find user group with external reference code " +
					externalReferenceCode,
				"the-user-group-recipient-does-not-exist", throwable);
		}

	}

	private NotificationRecipientSettingValueException(
		String message, String messageKey) {

		super(message);

		_messageKey = messageKey;
	}

	private NotificationRecipientSettingValueException(
		String message, String messageKey, Throwable throwable) {

		super(message, throwable);

		_messageKey = messageKey;
	}

	private final String _messageKey;

}