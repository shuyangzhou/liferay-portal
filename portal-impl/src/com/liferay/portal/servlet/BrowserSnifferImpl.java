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

package com.liferay.portal.servlet;

import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portal.kernel.servlet.BrowserSniffer;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;

/**
 * See http://www.zytrax.com/tech/web/browser_ids.htm for examples.
 *
 * @author Eduardo Lundgren
 * @author Nate Cavanaugh
 */
@DoPrivileged
public class BrowserSnifferImpl implements BrowserSniffer {

	@Override
	public boolean acceptsGzip(HttpServletRequest request) {
		String acceptEncoding = request.getHeader(HttpHeaders.ACCEPT_ENCODING);

		if ((acceptEncoding != null) && acceptEncoding.contains("gzip")) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public String getBrowserId(HttpServletRequest request) {
		String userAgent = getUserAgent(request);

		if (isIe(userAgent)) {
			return BROWSER_ID_IE;
		}
		else if (_isFirefox(userAgent)) {
			return BROWSER_ID_FIREFOX;
		}
		else {
			return BROWSER_ID_OTHER;
		}
	}

	@Override
	public String getJavaScriptBrowser(HttpServletRequest request) {
		String version = getVersion(request);

		String majorVersion = version;

		if (version.isEmpty()) {
			majorVersion = "0";
		}

		String userAgent = getUserAgent(request);

		StringBundler sb = new StringBundler(41);

		sb.append("{acceptsGzip: function() {return ");
		sb.append(acceptsGzip(request));
		sb.append(";}, getMajorVersion: function() {return ");
		sb.append(majorVersion);
		sb.append(";}, getRevision: function() {return '");
		sb.append(getRevision(request));
		sb.append("';}, getVersion: function() {return '");
		sb.append(version);
		sb.append("';}, isAir: function() {return ");
		sb.append(_isAir(userAgent));
		sb.append(";}, isChrome: function() {return ");
		sb.append(_isChrome(userAgent));
		sb.append(";}, isFirefox: function() {return ");
		sb.append(_isFirefox(userAgent));
		sb.append(";}, isGecko: function() {return ");
		sb.append(_isGecko(userAgent));
		sb.append(";}, isIe: function() {return ");
		sb.append(isIe(userAgent));
		sb.append(";}, isIphone: function() {return ");
		sb.append(_isIphone(userAgent));
		sb.append(";}, isLinux: function() {return ");
		sb.append(_isLinux(userAgent));
		sb.append(";}, isMac: function() {return ");
		sb.append(_isMac(userAgent));
		sb.append(";}, isMobile: function() {return ");
		sb.append(_isMobile(userAgent));
		sb.append(";}, isMozilla: function() {return ");
		sb.append(_isMozilla(userAgent));
		sb.append(";}, isOpera: function() {return ");
		sb.append(_isOpera(userAgent));
		sb.append(";}, isRtf: function() {return ");
		sb.append(_isRtf(userAgent, version));
		sb.append(";}, isSafari: function() {return ");
		sb.append(_isSafari(userAgent));
		sb.append(";}, isSun: function() {return ");
		sb.append(_isSun(userAgent));
		sb.append(";}, isWebKit: function() {return ");
		sb.append(_isWebKit(userAgent));
		sb.append(";}, isWindows: function() {return ");
		sb.append(_isWindows(userAgent));
		sb.append(";}};");

		return sb.toString();
	}

	@Override
	public float getMajorVersion(HttpServletRequest request) {
		return GetterUtil.getFloat(getVersion(request));
	}

	@Override
	public String getRevision(HttpServletRequest request) {
		String revision = (String)request.getAttribute(
			WebKeys.BROWSER_SNIFFER_REVISION);

		if (revision != null) {
			return revision;
		}

		revision = parseVersion(
			getUserAgent(request), revisionLeadings, revisionSeparators);

		request.setAttribute(WebKeys.BROWSER_SNIFFER_REVISION, revision);

		return revision;
	}

	@Override
	public String getVersion(HttpServletRequest request) {
		String version = (String)request.getAttribute(
			WebKeys.BROWSER_SNIFFER_VERSION);

		if (version != null) {
			return version;
		}

		String userAgent = getUserAgent(request);

		version = parseVersion(userAgent, versionLeadings, versionSeparators);

		if (version.isEmpty()) {
			version = parseVersion(
				userAgent, revisionLeadings, revisionSeparators);
		}

		request.setAttribute(WebKeys.BROWSER_SNIFFER_VERSION, version);

		return version;
	}

	@Override
	public boolean isAir(HttpServletRequest request) {
		return _isAir(getUserAgent(request));
	}

	@Override
	public boolean isAndroid(HttpServletRequest request) {
		return _isAndroid(getUserAgent(request));
	}

	@Override
	public boolean isChrome(HttpServletRequest request) {
		return _isChrome(getUserAgent(request));
	}

	@Override
	public boolean isFirefox(HttpServletRequest request) {
		return _isFirefox(getUserAgent(request));
	}

	@Override
	public boolean isGecko(HttpServletRequest request) {
		return _isGecko(getUserAgent(request));
	}

	@Override
	public boolean isIe(HttpServletRequest request) {
		return isIe(getUserAgent(request));
	}

	@Override
	public boolean isIeOnWin32(HttpServletRequest request) {
		String userAgent = getUserAgent(request);

		if (isIe(userAgent) &&
			!(userAgent.contains("wow64") || userAgent.contains("win64"))) {

			return true;
		}

		return false;
	}

	@Override
	public boolean isIeOnWin64(HttpServletRequest request) {
		String userAgent = getUserAgent(request);

		if (isIe(userAgent) &&
			(userAgent.contains("wow64") || userAgent.contains("win64"))) {

			return true;
		}

		return false;
	}

	@Override
	public boolean isIphone(HttpServletRequest request) {
		return _isIphone(getUserAgent(request));
	}

	@Override
	public boolean isLinux(HttpServletRequest request) {
		return _isLinux(getUserAgent(request));
	}

	@Override
	public boolean isMac(HttpServletRequest request) {
		return _isMac(getUserAgent(request));
	}

	@Override
	public boolean isMobile(HttpServletRequest request) {
		return _isMobile(getUserAgent(request));
	}

	@Override
	public boolean isMozilla(HttpServletRequest request) {
		return _isMozilla(getUserAgent(request));
	}

	@Override
	public boolean isOpera(HttpServletRequest request) {
		return _isOpera(getUserAgent(request));
	}

	@Override
	public boolean isRtf(HttpServletRequest request) {
		return _isRtf(getUserAgent(request), getVersion(request));
	}

	@Override
	public boolean isSafari(HttpServletRequest request) {
		String userAgent = getUserAgent(request);

		if (isWebKit(request) && userAgent.contains("safari")) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isSun(HttpServletRequest request) {
		return _isSun(getUserAgent(request));
	}

	@Override
	public boolean isWebKit(HttpServletRequest request) {
		return _isWebKit(getUserAgent(request));
	}

	@Override
	public boolean isWindows(HttpServletRequest request) {
		return _isWindows(getUserAgent(request));
	}

	protected static String parseVersion(
		String userAgent, String[] leadings, char[] separators) {

		leading:
		for (String leading : leadings) {
			int index = 0;

			version:
			while (true) {
				index = userAgent.indexOf(leading, index);

				if ((index < 0) ||
					(((index += leading.length()) + 2) > userAgent.length())) {

					continue leading;
				}

				char c1 = userAgent.charAt(index);
				char c2 = userAgent.charAt(++index);

				if (((c2 >= '0') && (c2 <= '9')) || (c2 == '.')) {
					for (char separator : separators) {
						if (c1 == separator) {
							break version;
						}
					}
				}
			}

			// Major

			int majorStart = index;
			int majorEnd = index + 1;

			for (int i = majorStart; i < userAgent.length(); i++) {
				char c = userAgent.charAt(i);

				if ((c < '0') || (c > '9')) {
					majorEnd = i;

					break;
				}
			}

			String major = userAgent.substring(majorStart, majorEnd);

			if (userAgent.charAt(majorEnd) != '.') {
				return major;
			}

			// Minor

			int minorStart = majorEnd + 1;
			int minorEnd = userAgent.length();

			for (int i = minorStart; i < userAgent.length(); i++) {
				char c = userAgent.charAt(i);

				if ((c < '0') || (c > '9')) {
					minorEnd = i;

					break;
				}
			}

			String minor = userAgent.substring(minorStart, minorEnd);

			return major.concat(StringPool.PERIOD).concat(minor);
		}

		return StringPool.BLANK;
	}

	protected String getAccept(HttpServletRequest request) {
		String accept = StringPool.BLANK;

		if (request == null) {
			return accept;
		}

		accept = String.valueOf(request.getAttribute(HttpHeaders.ACCEPT));

		if (Validator.isNotNull(accept)) {
			return accept;
		}

		accept = request.getHeader(HttpHeaders.ACCEPT);

		if (accept != null) {
			accept = StringUtil.toLowerCase(accept);
		}
		else {
			accept = StringPool.BLANK;
		}

		request.setAttribute(HttpHeaders.ACCEPT, accept);

		return accept;
	}

	protected String getUserAgent(HttpServletRequest request) {
		if (request == null) {
			return StringPool.BLANK;
		}

		Object userAgentObject = request.getAttribute(HttpHeaders.USER_AGENT);

		if (userAgentObject != null) {
			return userAgentObject.toString();
		}

		String userAgent = request.getHeader(HttpHeaders.USER_AGENT);

		if (userAgent != null) {
			userAgent = StringUtil.toLowerCase(userAgent);
		}
		else {
			userAgent = StringPool.BLANK;
		}

		request.setAttribute(HttpHeaders.USER_AGENT, userAgent);

		return userAgent;
	}

	protected boolean isIe(String userAgent) {
		if ((userAgent.contains("msie") || userAgent.contains("trident")) &&
			!userAgent.contains("opera")) {

			return true;
		}

		return false;
	}

	protected static String[] revisionLeadings = {"rv", "it", "ra", "ie"};
	protected static char[] revisionSeparators =
		{CharPool.BACK_SLASH, CharPool.COLON, CharPool.SLASH, CharPool.SPACE};
	protected static String[] versionLeadings =
		{"version", "firefox", "minefield", "chrome"};
	protected static char[] versionSeparators =
		{CharPool.BACK_SLASH, CharPool.SLASH};

	private String _getVersion(HttpServletRequest request, String userAgent) {
		String version = (String)request.getAttribute(
			WebKeys.BROWSER_SNIFFER_VERSION);

		if (version != null) {
			return version;
		}

		version = parseVersion(userAgent, versionLeadings, versionSeparators);

		if (version.isEmpty()) {
			version = parseVersion(
				userAgent, revisionLeadings, revisionSeparators);
		}

		request.setAttribute(WebKeys.BROWSER_SNIFFER_VERSION, version);

		return version;
	}

	private boolean _isAir(String userAgent) {
		if (userAgent.contains("adobeair")) {
			return true;
		}

		return false;
	}

	private boolean _isAndroid(String userAgent) {
		if (userAgent.contains("android")) {
			return true;
		}

		return false;
	}

	private boolean _isChrome(String userAgent) {
		if (userAgent.contains("chrome")) {
			return true;
		}

		return false;
	}

	private boolean _isFirefox(String userAgent) {
		if (!_isMozilla(userAgent)) {
			return false;
		}

		for (String firefoxAlias : _FIREFOX_ALIASES) {
			if (userAgent.contains(firefoxAlias)) {
				return true;
			}
		}

		return false;
	}

	private boolean _isGecko(String userAgent) {
		if (userAgent.contains("gecko")) {
			return true;
		}

		return false;
	}

	private boolean _isIphone(String userAgent) {
		if (userAgent.contains("iphone")) {
			return true;
		}

		return false;
	}

	private boolean _isLinux(String userAgent) {
		if (userAgent.contains("linux")) {
			return true;
		}

		return false;
	}

	private boolean _isMac(String userAgent) {
		if (userAgent.contains("mac")) {
			return true;
		}

		return false;
	}

	private boolean _isMobile(String userAgent) {
		if (userAgent.contains("mobile") ||
			(userAgent.contains("android") && userAgent.contains("nexus"))) {

			return true;
		}

		return false;
	}

	private boolean _isMozilla(String userAgent) {
		if (userAgent.contains("compatible")) {
			return false;
		}

		if (userAgent.contains("webkit")) {
			return false;
		}

		if (userAgent.contains("mozilla")) {
			return true;
		}

		return false;
	}

	private boolean _isOpera(String userAgent) {
		if (userAgent.contains("opera")) {
			return true;
		}

		return false;
	}

	private boolean _isRtf(String userAgent, String version) {
		if (_isAndroid(userAgent)) {
			return true;
		}

		if (_isChrome(userAgent)) {
			return true;
		}

		float majorVersion = GetterUtil.getFloat(version);

		if (isIe(userAgent) && (majorVersion >= 5.5)) {
			return true;
		}

		if (_isMozilla(userAgent) && (majorVersion >= 1.3)) {
			return true;
		}

		if (_isOpera(userAgent)) {
			if (_isMobile(userAgent) && (majorVersion >= 10.0)) {
				return true;
			}
			else if (!_isMobile(userAgent)) {
				return true;
			}
		}

		if (_isSafari(userAgent)) {
			if (_isMobile(userAgent) && (majorVersion >= 5.0)) {
				return true;
			}
			else if (!_isMobile(userAgent) && (majorVersion >= 3.0)) {
				return true;
			}
		}

		return false;
	}

	private boolean _isSafari(String userAgent) {
		if (_isWebKit(userAgent) && userAgent.contains("safari")) {
			return true;
		}

		return false;
	}

	private boolean _isSun(String userAgent) {
		if (userAgent.contains("sunos")) {
			return true;
		}

		return false;
	}

	private boolean _isWebKit(String userAgent) {
		for (String webKitAlias : _WEBKIT_ALIASES) {
			if (userAgent.contains(webKitAlias)) {
				return true;
			}
		}

		return false;
	}

	private boolean _isWindows(String userAgent) {
		for (String windowsAlias : _WINDOWS_ALIASES) {
			if (userAgent.contains(windowsAlias)) {
				return true;
			}
		}

		return false;
	}

	private static final String[] _FIREFOX_ALIASES = {
		"firefox", "minefield", "granparadiso", "bonecho", "firebird",
		"phoenix", "camino"
	};

	private static final String[] _WEBKIT_ALIASES = {"khtml", "applewebkit"};

	private static final String[] _WINDOWS_ALIASES = {
		"windows", "win32", "16bit"
	};

}