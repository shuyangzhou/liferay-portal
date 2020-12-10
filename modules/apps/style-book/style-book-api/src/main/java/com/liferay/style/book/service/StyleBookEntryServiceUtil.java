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

package com.liferay.style.book.service;

/**
 * Provides the remote service utility for StyleBookEntry. This utility wraps
 * <code>com.liferay.style.book.service.impl.StyleBookEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see StyleBookEntryService
 * @generated
 */
public class StyleBookEntryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.style.book.service.impl.StyleBookEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.style.book.model.StyleBookEntry addStyleBookEntry(
			long groupId, java.lang.String name,
			java.lang.String styleBookEntryKey,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addStyleBookEntry(
			groupId, name, styleBookEntryKey, serviceContext);
	}

	public static com.liferay.style.book.model.StyleBookEntry addStyleBookEntry(
			long groupId, java.lang.String frontendTokensValues,
			java.lang.String name, java.lang.String styleBookEntryKey,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addStyleBookEntry(
			groupId, frontendTokensValues, name, styleBookEntryKey,
			serviceContext);
	}

	public static com.liferay.style.book.model.StyleBookEntry
			copyStyleBookEntry(
				long groupId, long styleBookEntryId,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().copyStyleBookEntry(
			groupId, styleBookEntryId, serviceContext);
	}

	public static com.liferay.style.book.model.StyleBookEntry
			deleteStyleBookEntry(long styleBookEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteStyleBookEntry(styleBookEntryId);
	}

	public static com.liferay.style.book.model.StyleBookEntry
			deleteStyleBookEntry(
				com.liferay.style.book.model.StyleBookEntry styleBookEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteStyleBookEntry(styleBookEntry);
	}

	public static com.liferay.style.book.model.StyleBookEntry
			discardDraftStyleBookEntry(long styleBookEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().discardDraftStyleBookEntry(styleBookEntryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.style.book.model.StyleBookEntry publishDraft(
			long styleBookEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().publishDraft(styleBookEntryId);
	}

	public static com.liferay.style.book.model.StyleBookEntry
			updateDefaultStyleBookEntry(
				long styleBookEntryId, boolean defaultStyleBookEntry)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateDefaultStyleBookEntry(
			styleBookEntryId, defaultStyleBookEntry);
	}

	public static com.liferay.style.book.model.StyleBookEntry
			updateFrontendTokensValues(
				long styleBookEntryId, java.lang.String frontendTokensValues)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateFrontendTokensValues(
			styleBookEntryId, frontendTokensValues);
	}

	public static com.liferay.style.book.model.StyleBookEntry updateName(
			long styleBookEntryId, java.lang.String name)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateName(styleBookEntryId, name);
	}

	public static com.liferay.style.book.model.StyleBookEntry
			updatePreviewFileEntryId(
				long styleBookEntryId, long previewFileEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updatePreviewFileEntryId(
			styleBookEntryId, previewFileEntryId);
	}

	public static com.liferay.style.book.model.StyleBookEntry
			updateStyleBookEntry(
				long styleBookEntryId, java.lang.String frontendTokensValues,
				java.lang.String name)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateStyleBookEntry(
			styleBookEntryId, frontendTokensValues, name);
	}

	public static StyleBookEntryService getService() {
		return _styleBookEntryService;
	}

	private static volatile StyleBookEntryService _styleBookEntryService;

}