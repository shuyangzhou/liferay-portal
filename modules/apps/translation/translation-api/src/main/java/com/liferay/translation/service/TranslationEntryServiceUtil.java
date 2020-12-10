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

package com.liferay.translation.service;

/**
 * Provides the remote service utility for TranslationEntry. This utility wraps
 * <code>com.liferay.translation.service.impl.TranslationEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see TranslationEntryService
 * @generated
 */
public class TranslationEntryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.translation.service.impl.TranslationEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.translation.model.TranslationEntry
			addOrUpdateTranslationEntry(
				long groupId,
				com.liferay.info.item.InfoItemReference infoItemReference,
				java.lang.String content, java.lang.String contentType,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addOrUpdateTranslationEntry(
			groupId, infoItemReference, content, contentType, serviceContext);
	}

	public static com.liferay.translation.model.TranslationEntry
			addOrUpdateTranslationEntry(
				long groupId, java.lang.String languageId,
				com.liferay.info.item.InfoItemReference infoItemReference,
				com.liferay.info.item.InfoItemFieldValues infoItemFieldValues,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addOrUpdateTranslationEntry(
			groupId, languageId, infoItemReference, infoItemFieldValues,
			serviceContext);
	}

	public static com.liferay.translation.model.TranslationEntry
			deleteTranslationEntry(long translationEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteTranslationEntry(translationEntryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static TranslationEntryService getService() {
		return _translationEntryService;
	}

	private static volatile TranslationEntryService _translationEntryService;

}