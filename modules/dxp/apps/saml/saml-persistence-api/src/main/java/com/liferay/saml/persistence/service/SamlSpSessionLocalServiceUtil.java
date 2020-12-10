/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.saml.persistence.service;

/**
 * Provides the local service utility for SamlSpSession. This utility wraps
 * <code>com.liferay.saml.persistence.service.impl.SamlSpSessionLocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Mika Koivisto
 * @see SamlSpSessionLocalService
 * @generated
 */
public class SamlSpSessionLocalServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.saml.persistence.service.impl.SamlSpSessionLocalServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * Adds the saml sp session to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SamlSpSessionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param samlSpSession the saml sp session
	 * @return the saml sp session that was added
	 */
	public static com.liferay.saml.persistence.model.SamlSpSession
		addSamlSpSession(
			com.liferay.saml.persistence.model.SamlSpSession samlSpSession) {

		return getService().addSamlSpSession(samlSpSession);
	}

	public static com.liferay.saml.persistence.model.SamlSpSession
			addSamlSpSession(
				java.lang.String samlIdpEntityId,
				java.lang.String samlSpSessionKey,
				java.lang.String assertionXml, java.lang.String jSessionId,
				java.lang.String nameIdFormat,
				java.lang.String nameIdNameQualifier,
				java.lang.String nameIdSPNameQualifier,
				java.lang.String nameIdValue, java.lang.String sessionIndex,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addSamlSpSession(
			samlIdpEntityId, samlSpSessionKey, assertionXml, jSessionId,
			nameIdFormat, nameIdNameQualifier, nameIdSPNameQualifier,
			nameIdValue, sessionIndex, serviceContext);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			createPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().createPersistedModel(primaryKeyObj);
	}

	/**
	 * Creates a new saml sp session with the primary key. Does not add the saml sp session to the database.
	 *
	 * @param samlSpSessionId the primary key for the new saml sp session
	 * @return the new saml sp session
	 */
	public static com.liferay.saml.persistence.model.SamlSpSession
		createSamlSpSession(long samlSpSessionId) {

		return getService().createSamlSpSession(samlSpSessionId);
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			deletePersistedModel(
				com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deletePersistedModel(persistedModel);
	}

	/**
	 * Deletes the saml sp session with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SamlSpSessionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param samlSpSessionId the primary key of the saml sp session
	 * @return the saml sp session that was removed
	 * @throws PortalException if a saml sp session with the primary key could not be found
	 */
	public static com.liferay.saml.persistence.model.SamlSpSession
			deleteSamlSpSession(long samlSpSessionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().deleteSamlSpSession(samlSpSessionId);
	}

	/**
	 * Deletes the saml sp session from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SamlSpSessionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param samlSpSession the saml sp session
	 * @return the saml sp session that was removed
	 */
	public static com.liferay.saml.persistence.model.SamlSpSession
		deleteSamlSpSession(
			com.liferay.saml.persistence.model.SamlSpSession samlSpSession) {

		return getService().deleteSamlSpSession(samlSpSession);
	}

	public static <T> T dslQuery(
		com.liferay.petra.sql.dsl.query.DSLQuery dslQuery) {

		return getService().dslQuery(dslQuery);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery
		dynamicQuery() {

		return getService().dynamicQuery();
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.saml.persistence.model.impl.SamlSpSessionModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {

		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.saml.persistence.model.impl.SamlSpSessionModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {

		return getService().dynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {

		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {

		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.saml.persistence.model.SamlSpSession
		fetchSamlSpSession(long samlSpSessionId) {

		return getService().fetchSamlSpSession(samlSpSessionId);
	}

	public static com.liferay.saml.persistence.model.SamlSpSession
		fetchSamlSpSessionByJSessionId(java.lang.String jSessionId) {

		return getService().fetchSamlSpSessionByJSessionId(jSessionId);
	}

	public static com.liferay.saml.persistence.model.SamlSpSession
		fetchSamlSpSessionBySamlSpSessionKey(
			java.lang.String samlSpSessionKey) {

		return getService().fetchSamlSpSessionBySamlSpSessionKey(
			samlSpSessionKey);
	}

	public static com.liferay.saml.persistence.model.SamlSpSession
		fetchSamlSpSessionBySessionIndex(java.lang.String sessionIndex) {

		return getService().fetchSamlSpSessionBySessionIndex(sessionIndex);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery
		getActionableDynamicQuery() {

		return getService().getActionableDynamicQuery();
	}

	public static
		com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery
			getIndexableActionableDynamicQuery() {

		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	/**
	 * @throws PortalException
	 */
	public static com.liferay.portal.kernel.model.PersistedModel
			getPersistedModel(java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	 * Returns the saml sp session with the primary key.
	 *
	 * @param samlSpSessionId the primary key of the saml sp session
	 * @return the saml sp session
	 * @throws PortalException if a saml sp session with the primary key could not be found
	 */
	public static com.liferay.saml.persistence.model.SamlSpSession
			getSamlSpSession(long samlSpSessionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getSamlSpSession(samlSpSessionId);
	}

	public static com.liferay.saml.persistence.model.SamlSpSession
			getSamlSpSessionByJSessionId(java.lang.String jSessionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getSamlSpSessionByJSessionId(jSessionId);
	}

	public static com.liferay.saml.persistence.model.SamlSpSession
			getSamlSpSessionBySamlSpSessionKey(
				java.lang.String samlSpSessionKey)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getSamlSpSessionBySamlSpSessionKey(
			samlSpSessionKey);
	}

	public static com.liferay.saml.persistence.model.SamlSpSession
			getSamlSpSessionBySessionIndex(java.lang.String sessionIndex)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().getSamlSpSessionBySessionIndex(sessionIndex);
	}

	/**
	 * Returns a range of all the saml sp sessions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.saml.persistence.model.impl.SamlSpSessionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of saml sp sessions
	 * @param end the upper bound of the range of saml sp sessions (not inclusive)
	 * @return the range of saml sp sessions
	 */
	public static java.util.List
		<com.liferay.saml.persistence.model.SamlSpSession> getSamlSpSessions(
			int start, int end) {

		return getService().getSamlSpSessions(start, end);
	}

	public static java.util.List
		<com.liferay.saml.persistence.model.SamlSpSession> getSamlSpSessions(
			java.lang.String nameIdValue) {

		return getService().getSamlSpSessions(nameIdValue);
	}

	/**
	 * Returns the number of saml sp sessions.
	 *
	 * @return the number of saml sp sessions
	 */
	public static int getSamlSpSessionsCount() {
		return getService().getSamlSpSessionsCount();
	}

	public static com.liferay.saml.persistence.model.SamlSpSession
			updateSamlSpSession(
				long samlSpSessionId, java.lang.String jSessionId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateSamlSpSession(samlSpSessionId, jSessionId);
	}

	public static com.liferay.saml.persistence.model.SamlSpSession
			updateSamlSpSession(
				long samlSpSessionId, java.lang.String samlIdpEntityId,
				java.lang.String samlSpSessionKey,
				java.lang.String assertionXml, java.lang.String jSessionId,
				java.lang.String nameIdFormat,
				java.lang.String nameIdNameQualifier,
				java.lang.String nameIdSPNameQualifier,
				java.lang.String nameIdValue, java.lang.String sessionIndex,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateSamlSpSession(
			samlSpSessionId, samlIdpEntityId, samlSpSessionKey, assertionXml,
			jSessionId, nameIdFormat, nameIdNameQualifier,
			nameIdSPNameQualifier, nameIdValue, sessionIndex, serviceContext);
	}

	/**
	 * Updates the saml sp session in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect SamlSpSessionLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param samlSpSession the saml sp session
	 * @return the saml sp session that was updated
	 */
	public static com.liferay.saml.persistence.model.SamlSpSession
		updateSamlSpSession(
			com.liferay.saml.persistence.model.SamlSpSession samlSpSession) {

		return getService().updateSamlSpSession(samlSpSession);
	}

	public static SamlSpSessionLocalService getService() {
		return _samlSpSessionLocalService;
	}

	private static volatile SamlSpSessionLocalService
		_samlSpSessionLocalService;

}