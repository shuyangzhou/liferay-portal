/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.digital.signature.internal.http;

import com.liferay.digital.signature.configuration.DigitalSignatureConfiguration;
import com.liferay.digital.signature.configuration.DigitalSignatureConfigurationUtil;
import com.liferay.digital.signature.internal.web.cache.DSAccessTokenWebCacheItem;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(service = DSHttp.class)
public class DSHttp {

	public JSONObject get(long companyId, long groupId, String location) {
		try {
			return _invoke(companyId, groupId, location, Http.Method.GET, null);
		}
		catch (Exception exception) {
			return ReflectionUtil.throwException(exception);
		}
	}

	public byte[] getAsBytes(long companyId, long groupId, String location) {
		try {
			return _invokeAsBytes(
				companyId, groupId, location, Http.Method.GET, null);
		}
		catch (Exception exception) {
			return ReflectionUtil.throwException(exception);
		}
	}

	public JSONObject post(
		long companyId, long groupId, String location,
		JSONObject bodyJSONObject) {

		try {
			return _invoke(
				companyId, groupId, location, Http.Method.POST, bodyJSONObject);
		}
		catch (Exception exception) {
			return ReflectionUtil.throwException(exception);
		}
	}

	public JSONObject put(
		long companyId, long groupId, String location,
		JSONObject bodyJSONObject) {

		try {
			return _invoke(
				companyId, groupId, location, Http.Method.PUT, bodyJSONObject);
		}
		catch (Exception exception) {
			return ReflectionUtil.throwException(exception);
		}
	}

	private String _getDocuSignAccessToken(
			DigitalSignatureConfiguration digitalSignatureConfiguration)
		throws Exception {

		JSONObject jsonObject = DSAccessTokenWebCacheItem.get(
			digitalSignatureConfiguration.apiUsername(),
			digitalSignatureConfiguration.environment(),
			digitalSignatureConfiguration.integrationKey(),
			digitalSignatureConfiguration.rsaPrivateKey());

		if (Validator.isNotNull(jsonObject.getString("error"))) {
			_log.error(
				"[DSDIAG] Token grant failed, error " +
					jsonObject.getString("error") + ", description " +
						jsonObject.getString("error_description"));

			throw new PortalException(jsonObject.getString("error"));
		}

		String accessToken = jsonObject.getString("access_token");

		_log.error(
			StringBundler.concat(
				"[DSDIAG] Token grant, user ",
				digitalSignatureConfiguration.apiUsername(), ", environment ",
				digitalSignatureConfiguration.environment(),
				", integration key length ",
				_length(digitalSignatureConfiguration.integrationKey()),
				", private key length ",
				_length(digitalSignatureConfiguration.rsaPrivateKey()),
				", access token length ", _length(accessToken)));

		return accessToken;
	}

	private JSONObject _invoke(
			long companyId, long groupId, String location, Http.Method method,
			JSONObject bodyJSONObject)
		throws Exception {

		byte[] bytes = _invokeAsBytes(
			companyId, groupId, location, method, bodyJSONObject);

		if (bytes == null) {
			return _jsonFactory.createJSONObject();
		}

		return _jsonFactory.createJSONObject(new String(bytes));
	}

	private byte[] _invokeAsBytes(
			long companyId, long groupId, String location, Http.Method method,
			JSONObject bodyJSONObject)
		throws Exception {

		Http.Options options = new Http.Options();

		if (bodyJSONObject != null) {
			options.addHeader(
				HttpHeaders.CONTENT_TYPE, ContentTypes.APPLICATION_JSON);
		}

		DigitalSignatureConfiguration digitalSignatureConfiguration =
			DigitalSignatureConfigurationUtil.getDigitalSignatureConfiguration(
				companyId, groupId);

		options.addHeader(
			"Authorization",
			"Bearer " + _getDocuSignAccessToken(digitalSignatureConfiguration));

		if (bodyJSONObject != null) {
			options.setBody(
				bodyJSONObject.toString(), ContentTypes.APPLICATION_JSON,
				StringPool.UTF8);
		}

		options.setLocation(
			StringBundler.concat(
				digitalSignatureConfiguration.accountBaseURI(),
				"/restapi/v2.1/accounts/",
				digitalSignatureConfiguration.apiAccountId(), "/", location));
		options.setMethod(method);
		options.setTimeout(digitalSignatureConfiguration.httpTimeout());

		_log.error(
			StringBundler.concat(
				"[DSDIAG] Request ", String.valueOf(method), " ",
				options.getLocation(), ", account base URI ",
				digitalSignatureConfiguration.accountBaseURI(),
				", account id ",
				digitalSignatureConfiguration.apiAccountId(), ", timeout ",
				String.valueOf(
					digitalSignatureConfiguration.httpTimeout()),
				", body ",
				_truncate(
					(bodyJSONObject == null) ? null :
						bodyJSONObject.toString())));

		try {
			byte[] bytes = _http.URLtoByteArray(options);

			_log.error(
				StringBundler.concat(
					"[DSDIAG] Response ", String.valueOf(method), " ",
					options.getLocation(), ", code ",
					_responseCode(options), ", length ",
					String.valueOf((bytes == null) ? -1 : bytes.length),
					", headers ", _responseHeaders(options), ", body ",
					_truncate((bytes == null) ? null : new String(bytes))));

			return bytes;
		}
		catch (Exception exception) {
			_log.error(
				StringBundler.concat(
					"[DSDIAG] Failed ", String.valueOf(method), " ",
					options.getLocation(), ", code ", _responseCode(options),
					", headers ", _responseHeaders(options)),
				exception);

			throw exception;
		}
	}

	private String _responseCode(Http.Options options) {
		try {
			Http.Response response = options.getResponse();

			if (response == null) {
				return "none";
			}

			return String.valueOf(response.getResponseCode());
		}
		catch (Exception exception) {
			return "unavailable " + exception;
		}
	}

	private String _responseHeaders(Http.Options options) {
		try {
			Http.Response response = options.getResponse();

			if (response == null) {
				return "none";
			}

			return String.valueOf(response.getHeaders());
		}
		catch (Exception exception) {
			return "unavailable " + exception;
		}
	}

	private String _length(String value) {
		if (value == null) {
			return "null";
		}

		return String.valueOf(value.length());
	}

	private String _truncate(String value) {
		if (value == null) {
			return "null";
		}

		if (value.length() <= 800) {
			return value;
		}

		return value.substring(0, 800) + "...";
	}

	private static final Log _log = LogFactoryUtil.getLog(DSHttp.class);

	@Reference
	private Http _http;

	@Reference
	private JSONFactory _jsonFactory;

}