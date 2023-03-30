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

package com.liferay.oauth.client.persistence.service;

import com.liferay.oauth.client.persistence.model.OAuthClientEntry;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for OAuthClientEntry. This utility wraps
 * <code>com.liferay.oauth.client.persistence.service.impl.OAuthClientEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Brian Wing Shun Chan
 * @see OAuthClientEntryService
 * @generated
 */
public class OAuthClientEntryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.oauth.client.persistence.service.impl.OAuthClientEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static OAuthClientEntry addOAuthClientEntry(
			long userId, String authRequestParametersJSON,
			String authServerWellKnownURI, String infoJSON,
			String oidcUserInfoMapperJSON, String tokenRequestParametersJSON)
		throws PortalException {

		return getService().addOAuthClientEntry(
			userId, authRequestParametersJSON, authServerWellKnownURI, infoJSON,
			oidcUserInfoMapperJSON, tokenRequestParametersJSON);
	}

	public static OAuthClientEntry deleteOAuthClientEntry(
			long oAuthClientEntryId)
		throws PortalException {

		return getService().deleteOAuthClientEntry(oAuthClientEntryId);
	}

	public static OAuthClientEntry deleteOAuthClientEntry(
			long companyId, String authServerWellKnownURI, String clientId)
		throws PortalException {

		return getService().deleteOAuthClientEntry(
			companyId, authServerWellKnownURI, clientId);
	}

	public static List<OAuthClientEntry>
			getAuthServerWellKnownURISuffixOAuthClientEntries(
				long companyId, String authServerWellKnownURISuffix)
		throws PortalException {

		return getService().getAuthServerWellKnownURISuffixOAuthClientEntries(
			companyId, authServerWellKnownURISuffix);
	}

	public static List<OAuthClientEntry> getCompanyOAuthClientEntries(
		long companyId) {

		return getService().getCompanyOAuthClientEntries(companyId);
	}

	public static List<OAuthClientEntry> getCompanyOAuthClientEntries(
		long companyId, int start, int end) {

		return getService().getCompanyOAuthClientEntries(companyId, start, end);
	}

	public static OAuthClientEntry getOAuthClientEntry(
			long companyId, String authServerWellKnownURI, String clientId)
		throws PortalException {

		return getService().getOAuthClientEntry(
			companyId, authServerWellKnownURI, clientId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static List<OAuthClientEntry> getUserOAuthClientEntries(
		long userId) {

		return getService().getUserOAuthClientEntries(userId);
	}

	public static List<OAuthClientEntry> getUserOAuthClientEntries(
		long userId, int start, int end) {

		return getService().getUserOAuthClientEntries(userId, start, end);
	}

	public static OAuthClientEntry updateOAuthClientEntry(
			long oAuthClientEntryId, String authRequestParametersJSON,
			String authServerWellKnownURI, String infoJSON,
			String oidcUserInfoMapperJSON, String tokenRequestParametersJSON)
		throws PortalException {

		return getService().updateOAuthClientEntry(
			oAuthClientEntryId, authRequestParametersJSON,
			authServerWellKnownURI, infoJSON, oidcUserInfoMapperJSON,
			tokenRequestParametersJSON);
	}

	public static OAuthClientEntryService getService() {
		return _serviceDCLSingleton.getSingleton(
			OAuthClientEntryServiceUtil::_getService);
	}

	private static OAuthClientEntryService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			OAuthClientEntryServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<OAuthClientEntryService> serviceReference =
			bundleContext.getServiceReference(OAuthClientEntryService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<OAuthClientEntryService>
		_serviceDCLSingleton = new DCLSingleton<>();

}