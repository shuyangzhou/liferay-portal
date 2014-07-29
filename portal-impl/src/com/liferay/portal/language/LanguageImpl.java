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

package com.liferay.portal.language;

import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheMapSynchronizeUtil;
import com.liferay.portal.kernel.cache.PortalCacheMapSynchronizeUtil.Synchronizer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageWrapper;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.CookieKeys;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CompanyConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;

import java.io.Serializable;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Various translation related functionality. Features not documented in every
 * single method include: You can disable translation in total by configuring
 * <code>translations.disabled=true</code> in portal.properties.
 * 
 * If a translation for a given key does not exist, these methods typically 
 * return the requested key as the translation.
 * 
 * Parameter substitution follows the standard Java Resourcebundle notion of 
 * index based substitution - e.g. substitutable characters have to be named as
 * {0}, {1} etc., in any order or cardinality.
 * 
 * Depending on the context passed into these functions, the lookup might be
 * limited to the portal's resource bundle (e.g. when only a locale is passed),
 * or extended to include an individual portlet's resource bundle (e.g. when a
 * request object is passed) - a portlet's resource bundle will override the 
 * portal's resources in that case. 

 * @author Brian Wing Shun Chan
 * @author Andrius Vitkauskas
 * @author Eduardo Lundgren
 */
@DoPrivileged
public class LanguageImpl implements Language, Serializable {

	/**
	 * Translate a given key (pattern), using the current request's locale
	 * or - if current request locale is not available - the server's default
	 * locale. Substitute placeholders <code>{0}</code> with argument.
	 * 
	 * @param request is used to determine the current locale
	 * @param pattern key to look up in the current locale's resource file. 
	 *        Follows standard Java resource spec
	 * @param argument single argument to be substituted in pattern. Will be 
	 *        translated if possible
	 * @return translated pattern, with argument substituted for placeholder
	 *         in pattern.
	 */
	@Override
	public String format(
		HttpServletRequest request, String pattern, LanguageWrapper argument) {

		return format(request, pattern, new LanguageWrapper[] {argument}, true);
	}

	/**
	 * Translate a given key (pattern), using the current request's locale
	 * or - if current request locale is not available - the server's default
	 * locale. Substitute placeholders <code>{0}</code> with argument.
	 * 
	 * @param request is used to determine the current locale
	 * @param pattern key to look up in the current locale's resource file. 
	 *        Follows standard Java resource spec
	 * @param argument single argument to be substituted in pattern. 
	 * @param translateArguments determine if arguments should be substituted 
	 *        as is (false) or translated (true)
	 * @return translated pattern, with argument substituted for placeholder
	 *         in pattern.
	 */
	@Override
	public String format(
		HttpServletRequest request, String pattern, LanguageWrapper argument,
		boolean translateArguments) {

		return format(
			request, pattern, new LanguageWrapper[] {argument},
			translateArguments);
	}

	/**
	 * Translate a given key (pattern), using the current request's locale
	 * or - if current request locale is not available - the server's default
	 * locale. Substitute placeholders like <code>{0}</code> with arguments.
	 * 
	 * @param request is used to determine the current locale
	 * @param pattern key to look up in the current locale's resource file. 
	 *        Follows standard Java resource spec
	 * @param arguments arguments to be substituted in pattern. Will be 
	 *        translated if possible
	 * @return translated pattern, with argument substituted for placeholder
	 *         in pattern.
	 */
	@Override
	public String format(
		HttpServletRequest request, String pattern,
		LanguageWrapper[] arguments) {

		return format(request, pattern, arguments, true);
	}

	/**
	 * Translate a given key (pattern), using the current request's locale
	 * or - if current request locale is not available - the server's default
	 * locale. Substitute placeholders like <code>{0}</code> with arguments.
	 * 
	 * @param request is used to determine the current locale
	 * @param pattern key to look up in the current locale's resource file. 
	 *        Follows standard Java resource spec
	 * @param arguments the arguments to be substituted in pattern. 
	 * @param translateArguments determine if arguments should be substituted 
	 *        as is (false) or translated (true)
	 * @return translated pattern, with arguments substituted for placeholders
	 *         in pattern.
	 */
	@Override
	public String format(
		HttpServletRequest request, String pattern, LanguageWrapper[] arguments,
		boolean translateArguments) {

		if (PropsValues.TRANSLATIONS_DISABLED) {
			return pattern;
		}

		String value = null;

		try {
			pattern = get(request, pattern);

			if (ArrayUtil.isNotEmpty(arguments)) {
				pattern = _escapePattern(pattern);

				Object[] formattedArguments = new Object[arguments.length];

				for (int i = 0; i < arguments.length; i++) {
					if (translateArguments) {
						formattedArguments[i] =
							arguments[i].getBefore() +
							get(request, arguments[i].getText()) +
							arguments[i].getAfter();
					}
					else {
						formattedArguments[i] =
							arguments[i].getBefore() +
							arguments[i].getText() +
							arguments[i].getAfter();
					}
				}

				value = MessageFormat.format(pattern, formattedArguments);
			}
			else {
				value = pattern;
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return value;
	}

	/**
	 * Translate a given key (pattern), using the current request's locale
	 * or - if current request locale is not available - the server's default
	 * locale. Substitute placeholders <code>{0}</code> with argument.
	 * 
	 * @param request is used to determine the current locale
	 * @param pattern key to look up in the current locale's resource file. 
	 *        Follows standard Java resource spec
	 * @param argument single argument to be substituted in pattern. Will be 
	 *        translated if possible
	 * @return translated pattern, with argument substituted for placeholder
	 *         in pattern.
	 */
	@Override
	public String format(
		HttpServletRequest request, String pattern, Object argument) {

		return format(request, pattern, new Object[] {argument}, true);
	}

	/**
	 * Translate a given key (pattern), using the current request's locale
	 * or - if current request locale is not available - the server's default
	 * locale. Substitute placeholders <code>{0}</code> with argument.
	 * 
	 * @param request is used to determine the current locale
	 * @param pattern key to look up in the current locale's resource file. 
	 *        Follows standard Java resource spec
	 * @param argument single argument to be substituted in pattern. 
	 * @param translateArguments determine if arguments should be substituted 
	 *        as is (false) or translated (true)
	 * @return translated pattern, with argument substituted for placeholder
	 *         in pattern.
	 */
	@Override
	public String format(
		HttpServletRequest request, String pattern, Object argument,
		boolean translateArguments) {

		return format(
			request, pattern, new Object[] {argument}, translateArguments);
	}

	/**
	 * Translate a given key (pattern), using the current request's locale
	 * or - if current request locale is not available - the server's default
	 * locale. Substitute placeholders like <code>{0}</code> with arguments.
	 * 
	 * @param request is used to determine the current locale
	 * @param pattern key to look up in the current locale's resource file. 
	 *        Follows standard Java resource spec
	 * @param arguments arguments to be substituted in pattern. Will be 
	 *        translated if possible
	 * @return translated pattern, with argument substituted for placeholder
	 *         in pattern.
	 */
	@Override
	public String format(
		HttpServletRequest request, String pattern, Object[] arguments) {

		return format(request, pattern, arguments, true);
	}

	/**
	 * Translate a given key (pattern), using the current request's locale
	 * or - if current request locale is not available - the server's default
	 * locale. Substitute placeholders like <code>{0}</code> with arguments.
	 * 
	 * @param request is used to determine the current locale
	 * @param pattern key to look up in the current locale's resource file. 
	 *        Follows standard Java resource spec
	 * @param arguments arguments to be substituted in pattern. 
	 * @param translateArguments determine if all arguments should be 
	 *        substituted as-is (false) or translated (true)
	 * @return translated pattern, with arguments substituted for placeholders
	 *         in pattern.
	 */
	@Override
	public String format(
		HttpServletRequest request, String pattern, Object[] arguments,
		boolean translateArguments) {

		if (PropsValues.TRANSLATIONS_DISABLED) {
			return pattern;
		}

		String value = null;

		try {
			pattern = get(request, pattern);

			if (ArrayUtil.isNotEmpty(arguments)) {
				pattern = _escapePattern(pattern);

				Object[] formattedArguments = new Object[arguments.length];

				for (int i = 0; i < arguments.length; i++) {
					if (translateArguments) {
						formattedArguments[i] = get(
							request, arguments[i].toString());
					}
					else {
						formattedArguments[i] = arguments[i];
					}
				}

				value = MessageFormat.format(pattern, formattedArguments);
			}
			else {
				value = pattern;
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return value;
	}

	/**
	 * Translate a given key (pattern) to the given locale. Uses the server default
	 * locale if requested one is not available. Substitute placeholders like 
	 * <code>{0}</code> with arguments.
	 * 
	 * @param locale language/locale to translate to
	 * @param pattern key to look up in the current locale's resource file. 
	 *        Follows standard Java resource spec
	 * @param arguments arguments to be substituted in pattern. Will be 
	 *        translated if possible
	 * @return translated pattern, with arguments substituted for placeholders
	 *         in pattern.
	 */
	@Override
	public String format(
		Locale locale, String pattern, List<Object> arguments) {

		return format(locale, pattern, arguments.toArray(), true);
	}

	/**
	 * Translate a given key (pattern) to the given locale. Uses the server default
	 * locale if requested one is not available. Substitute placeholders 
	 * <code>{0}</code> with argument.
	 * 
	 * @param locale language/locale to translate to
	 * @param pattern key to look up in the current locale's resource file. 
	 *        Follows standard Java resource spec
	 * @param argument argument to be substituted in pattern. Will be 
	 *        translated if possible
	 * @return translated pattern, with argument substituted for placeholders
	 *         in pattern.
	 */
	@Override
	public String format(Locale locale, String pattern, Object argument) {
		return format(locale, pattern, new Object[] {argument}, true);
	}

	/**
	 * @see {@link #format(HttpServletRequest, String, Object, boolean)}
	 */
	@Override
	public String format(
		Locale locale, String pattern, Object argument,
		boolean translateArguments) {

		return format(
			locale, pattern, new Object[] {argument}, translateArguments);
	}

	/**
	 * @see {@link #format(HttpServletRequest, String, Object[])}
	 */
	@Override
	public String format(Locale locale, String pattern, Object[] arguments) {
		return format(locale, pattern, arguments, true);
	}

	/**
	 * @see {@link #format(HttpServletRequest, String, Object[], boolean)}
	 */
	@Override
	public String format(
		Locale locale, String pattern, Object[] arguments,
		boolean translateArguments) {

		if (PropsValues.TRANSLATIONS_DISABLED) {
			return pattern;
		}

		String value = null;

		try {
			pattern = get(locale, pattern);

			if (ArrayUtil.isNotEmpty(arguments)) {
				pattern = _escapePattern(pattern);

				Object[] formattedArguments = new Object[arguments.length];

				for (int i = 0; i < arguments.length; i++) {
					if (translateArguments) {
						formattedArguments[i] = get(
							locale, arguments[i].toString());
					}
					else {
						formattedArguments[i] = arguments[i];
					}
				}

				value = MessageFormat.format(pattern, formattedArguments);
			}
			else {
				value = pattern;
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return value;
	}

	/**
	 * @see {@link #format(HttpServletRequest, String, Object)}
	 */
	@Override
	public String format(
		ResourceBundle resourceBundle, String pattern, Object argument) {

		return format(resourceBundle, pattern, new Object[] {argument}, true);
	}

	/**
	 * @see {@link #format(HttpServletRequest, String, Object, boolean)}
	 */
	@Override
	public String format(
		ResourceBundle resourceBundle, String pattern, Object argument,
		boolean translateArguments) {

		return format(
			resourceBundle, pattern, new Object[] {argument},
			translateArguments);
	}

	/**
	 * @see {@link #format(HttpServletRequest, String, Object[])}
	 */
	@Override
	public String format(
		ResourceBundle resourceBundle, String pattern, Object[] arguments) {

		return format(resourceBundle, pattern, arguments, true);
	}

	/**
	 * @see {@link #format(HttpServletRequest, String, Object[], boolean)}
	 */
	@Override
	public String format(
		ResourceBundle resourceBundle, String pattern, Object[] arguments,
		boolean translateArguments) {

		if (PropsValues.TRANSLATIONS_DISABLED) {
			return pattern;
		}

		String value = null;

		try {
			pattern = get(resourceBundle, pattern);

			if (ArrayUtil.isNotEmpty(arguments)) {
				pattern = _escapePattern(pattern);

				Object[] formattedArguments = new Object[arguments.length];

				for (int i = 0; i < arguments.length; i++) {
					if (translateArguments) {
						formattedArguments[i] = get(
							resourceBundle, arguments[i].toString());
					}
					else {
						formattedArguments[i] = arguments[i];
					}
				}

				value = MessageFormat.format(pattern, formattedArguments);
			}
			else {
				value = pattern;
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return value;
	}

	/**
	 * Retrieve the translation for the given key from portlet configuration
	 * (determined by request) or - if not set - from portal's resource bundle
	 * @param request determining the context and requested locale
	 * @param key translation key
	 * @return translation - if exists - or key otherwise.
	 */
	@Override
	public String get(HttpServletRequest request, String key) {
		return get(request, key, key);
	}

	/**
	 * Retrieve the translation for the given key from portlet configuration
	 * (determined by request) or - if not set - from portal's resource bundle
	 * 
	 * @param request determining the context and requested locale
	 * @param key translation key
	 * @param defaultValue default in case there's no matching translatoin
	 * @return translation - if exists - or defaultValue otherwise.
	 */
	@Override
	public String get(
		HttpServletRequest request, String key, String defaultValue) {

		if ((request == null) || (key == null)) {
			return defaultValue;
		}

		PortletConfig portletConfig = (PortletConfig)request.getAttribute(
			JavaConstants.JAVAX_PORTLET_CONFIG);

		Locale locale = _getLocale(request);

		if (portletConfig == null) {
			return get(locale, key, defaultValue);
		}

		ResourceBundle resourceBundle = portletConfig.getResourceBundle(locale);

		if (resourceBundle.containsKey(key)) {
			return _get(resourceBundle, key);
		}

		return get(locale, key, defaultValue);
	}

	/**
	 * @see {@link #get(HttpServletRequest, String)},
	 * this method only looks up portal resources, no portlet resources.
	 */
	@Override
	public String get(Locale locale, String key) {
		return get(locale, key, key);
	}

	/**
	 * @see {@link #get(HttpServletRequest, String, String)},
 	 * this method only looks up portal resources, no portlet resources.
	 */
	@Override
	public String get(Locale locale, String key, String defaultValue) {
		if (PropsValues.TRANSLATIONS_DISABLED) {
			return key;
		}

		if ((locale == null) || (key == null)) {
			return defaultValue;
		}

		String value = LanguageResources.getMessage(locale, key);

		if (value != null) {
			return LanguageResources.fixValue(value);
		}

		if (value == null) {
			if ((key.length() > 0) &&
				(key.charAt(key.length() - 1) == CharPool.CLOSE_BRACKET)) {

				int pos = key.lastIndexOf(CharPool.OPEN_BRACKET);

				if (pos != -1) {
					key = key.substring(0, pos);

					return get(locale, key, defaultValue);
				}
			}
		}

		return defaultValue;
	}

	/**
	 * @see {@link #get(HttpServletRequest, String)}
	 */
	@Override
	public String get(ResourceBundle resourceBundle, String key) {
		return get(resourceBundle, key, key);
	}

	/**
	 * @see {@link #get(HttpServletRequest, String, String)}
	 */
	@Override
	public String get(
		ResourceBundle resourceBundle, String key, String defaultValue) {

		String value = _get(resourceBundle, key);

		if (value != null) {
			return value;
		}

		return defaultValue;
	}

	/**
	 * retrieves the locales configured for the portal. Configuration is done
	 * in portal.properties with keys "locales" and "locales.enabled"
	 */
	@Override
	public Locale[] getAvailableLocales() {
		return _getInstance()._locales;
	}

	@Override
	public Locale[] getAvailableLocales(long groupId) {
		if (groupId <= 0) {
			return getAvailableLocales();
		}

		try {
			if (isInheritLocales(groupId)) {
				return getAvailableLocales();
			}
		}
		catch (Exception e) {
		}

		Locale[] locales = _groupLocalesMap.get(groupId);

		if (locales != null) {
			return locales;
		}

		_initGroupLocales(groupId);

		return _groupLocalesMap.get(groupId);
	}

	@Override
	public String getBCP47LanguageId(HttpServletRequest request) {
		Locale locale = PortalUtil.getLocale(request);

		return getBCP47LanguageId(locale);
	}

	@Override
	public String getBCP47LanguageId(Locale locale) {
		return LocaleUtil.toBCP47LanguageId(locale);
	}

	@Override
	public String getBCP47LanguageId(PortletRequest portletRequest) {
		Locale locale = PortalUtil.getLocale(portletRequest);

		return getBCP47LanguageId(locale);
	}

	@Override
	public String getCharset(Locale locale) {
		return _getInstance()._getCharset();
	}

	/**
	 * retrieve the languageId that the given request should be served with.
	 * LanguageId might be just a language or a specific variant, e.g. "en" 
	 * or "en_GB"
	 * 
	 * @param request
	 * @return
	 */
	@Override
	public String getLanguageId(HttpServletRequest request) {
		String languageId = ParamUtil.getString(request, "languageId");

		if (Validator.isNotNull(languageId)) {
			if (_localesMap.containsKey(languageId) ||
				_charEncodings.containsKey(languageId)) {

				return languageId;
			}
		}

		Locale locale = PortalUtil.getLocale(request);

		return getLanguageId(locale);
	}

	/**
	 * retrieve the languageId of the given Locale
	 * LanguageId might be just a language or a specific variant, e.g. "en" 
	 * or "en_GB"
	 * 
	 * @param locale
	 * @return
	 */
	@Override
	public String getLanguageId(Locale locale) {
		return LocaleUtil.toLanguageId(locale);
	}

	/**
	 * retrieve the languageId that the given PortletRequest should be served with.
	 * LanguageId might be just a language or a specific variant, e.g. "en" 
	 * or "en_GB"
	 * 
	 * @param locale
	 * @return
	 */
	@Override
	public String getLanguageId(PortletRequest portletRequest) {
		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			portletRequest);

		return getLanguageId(request);
	}

	/**
	 * retrieve the Locale for the given languageCode (e.g. "en" or "en_GB")
	 * @param languageCode
	 * @return
	 */
	@Override
	public Locale getLocale(String languageCode) {
		return _getInstance()._getLocale(languageCode);
	}

	/**
	 * retrieve the supported locales. These are defined as the available locales
	 * (configured in portal.properties as "locales") without the ones configured
	 * as beta resources ("locales.beta" in portal.properties)
	 */
	@Override
	public Locale[] getSupportedLocales() {
		List<Locale> supportedLocales = new ArrayList<>();

		Locale[] locales = getAvailableLocales();

		for (Locale locale : locales) {
			if (!isBetaLocale(locale)) {
				supportedLocales.add(locale);
			}
		}

		return supportedLocales.toArray(new Locale[supportedLocales.size()]);
	}

	/**
	 * @see {@link #getTimeDescription(Locale, long)}
	 */
	@Override
	public String getTimeDescription(
		HttpServletRequest request, long milliseconds) {

		return getTimeDescription(request, milliseconds, false);
	}

	/**
	 * @see {@link #getTimeDescription(Locale, long, boolean)}
	 */
	@Override
	public String getTimeDescription(
		HttpServletRequest request, long milliseconds, boolean approximate) {

		String description = Time.getDescription(milliseconds, approximate);

		String value = null;

		try {
			int pos = description.indexOf(CharPool.SPACE);

			String x = description.substring(0, pos);

			value = x.concat(StringPool.SPACE).concat(
				get(
					request,
					StringUtil.toLowerCase(
						description.substring(pos + 1, description.length()))));
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return value;
	}

	/**
	 * @see {@link #getTimeDescription(Locale, long)}
	 */
	@Override
	public String getTimeDescription(
		HttpServletRequest request, Long milliseconds) {

		return getTimeDescription(request, milliseconds.longValue());
	}

	/**
	 * Retrieve a localized description of the given timespan in the largest unit
	 * possible - e.g. 1000ms will be localized to english as "1 second", while 1001ms 
	 * will be "1001 milliseconds". Similarly 86400000ms will result in "1 day", while
	 * 86401000ms will be "86401 seconds". Follows english grammar rules for plural. 
	 * 
	 * @param locale determines the requested language
	 * @param milliseconds the timespan to describe
	 * @return exact description of the timespan as explained 
	 */
	@Override
	public String getTimeDescription(Locale locale, long milliseconds) {
		return getTimeDescription(locale, milliseconds, false);
	}

	/**
	 * <p>
	 * Retrieve a localized description. For accurate output (e.g. 
	 * approximate==false), @see {@link #getTimeDescription(HttpServletRequest, long)}.
	 * </p><p>
	 * Approximate description rounds the time to the largest possible unit and
	 * ignores the rest, e.g. 1000-1999ms will be returned as "1 second", 
	 * 86400000-172799999ms as "1 day".
	 * </p>
	 * <p>
	 * follows english grammar rules for plural
	 * </p>
	 * @param locale determines the requested language
	 * @param milliseconds time span to be described
	 * @param approximate determines exact or approximate description
	 */
	@Override
	public String getTimeDescription(
		Locale locale, long milliseconds, boolean approximate) {

		String description = Time.getDescription(milliseconds, approximate);

		String value = null;

		try {
			int pos = description.indexOf(CharPool.SPACE);

			String x = description.substring(0, pos);

			value = x.concat(StringPool.SPACE).concat(
				get(
					locale,
					StringUtil.toLowerCase(
						description.substring(pos + 1, description.length()))));
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return value;
	}

	/**
	 * @see {@link #getTimeDescription(Locale, long)}
	 */
	@Override
	public String getTimeDescription(Locale locale, Long milliseconds) {
		return getTimeDescription(locale, milliseconds.longValue());
	}

	@Override
	public void init() {
		_instances.clear();
	}

	/**
	 * determines if given language code (e.g. "en", "en_GB") is configured
	 * to be available. @see {@link #getAvailableLocales()} for configuration
	 * options.
	 * 
	 * @param languageCode
	 * @return
	 */
	@Override
	public boolean isAvailableLanguageCode(String languageCode) {
		return _getInstance()._localesMap.containsKey(languageCode);
	}

	/**
	 * determines if given locale is configured to be available. 
	 * @see {@link #getAvailableLocales()} for configuration options.
	 * 
	 * @param locale
	 * @return
	 */
	@Override
	public boolean isAvailableLocale(Locale locale) {
		return _getInstance()._localesSet.contains(locale);
	}

	/**
	 * determines if given locale is configured to be available in the given group 
	 */
	@Override
	public boolean isAvailableLocale(long groupId, Locale locale) {
		if (groupId <= 0) {
			return isAvailableLocale(locale);
		}

		try {
			if (isInheritLocales(groupId)) {
				return isAvailableLocale(locale);
			}
		}
		catch (Exception e) {
		}

		Set<Locale> localesSet = _groupLocalesSet.get(groupId);

		if (localesSet != null) {
			return localesSet.contains(locale);
		}

		_initGroupLocales(groupId);

		localesSet = _groupLocalesSet.get(groupId);

		return localesSet.contains(locale);
	}

	/**
	 * determines if given language code is configured to be available in the given group 
	 */
	@Override
	public boolean isAvailableLocale(long groupId, String languageId) {
		Locale[] locales = getAvailableLocales(groupId);

		for (Locale locale : locales) {
			if (languageId.equals(locale.toString())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * determines if given languageId is configured to be available
	 */
	@Override
	public boolean isAvailableLocale(String languageId) {
		Locale[] locales = getAvailableLocales();

		for (Locale locale : locales) {
			if (languageId.equals(locale.toString())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * determines if given locale is configured to be among the beta languages
	 * @see {@link #getSupportedLocales()} 
	 */
	@Override
	public boolean isBetaLocale(Locale locale) {
		return _getInstance()._localesBetaSet.contains(locale);
	}

	@Override
	public boolean isDuplicateLanguageCode(String languageCode) {
		return _getInstance()._duplicateLanguageCodes.contains(languageCode);
	}

	@Override
	public boolean isInheritLocales(long groupId) throws PortalException {
		Group group = GroupLocalServiceUtil.getGroup(groupId);

		Group liveGroup = group;

		if (group.isStagingGroup()) {
			liveGroup = group.getLiveGroup();
		}

		if (!group.isSite() || group.isCompany()) {
			return true;
		}

		return GetterUtil.getBoolean(
			liveGroup.getTypeSettingsProperty("inheritLocales"), true);
	}

	@Override
	public String process(
		ResourceBundle resourceBundle, Locale locale, String content) {

		StringBundler sb = new StringBundler();

		Matcher matcher = _pattern.matcher(content);

		int x = 0;

		while (matcher.find()) {
			int y = matcher.start(0);

			String key = matcher.group(1);

			sb.append(content.substring(x, y));
			sb.append(StringPool.APOSTROPHE);

			String value = get(resourceBundle, key);

			sb.append(HtmlUtil.escapeJS(value));
			sb.append(StringPool.APOSTROPHE);

			x = matcher.end(0);
		}

		sb.append(content.substring(x));

		return sb.toString();
	}

	@Override
	public void resetAvailableGroupLocales(long groupId) {
		_resetAvailableGroupLocales(groupId);
	}

	@Override
	public void resetAvailableLocales(long companyId) {
		_resetAvailableLocales(companyId);
	}

	@Override
	public void updateCookie(
		HttpServletRequest request, HttpServletResponse response,
		Locale locale) {

		String languageId = LocaleUtil.toLanguageId(locale);

		Cookie languageIdCookie = new Cookie(
			CookieKeys.GUEST_LANGUAGE_ID, languageId);

		languageIdCookie.setPath(StringPool.SLASH);
		languageIdCookie.setMaxAge(CookieKeys.MAX_AGE);

		CookieKeys.addCookie(request, response, languageIdCookie);
	}

	private static LanguageImpl _getInstance() {
		Long companyId = CompanyThreadLocal.getCompanyId();

		LanguageImpl instance = _instances.get(companyId);

		if (instance == null) {
			instance = new LanguageImpl(companyId);

			_instances.put(companyId, instance);
		}

		return instance;
	}

	private LanguageImpl() {
		this(CompanyConstants.SYSTEM);
	}

	private LanguageImpl(long companyId) {
		String[] languageIds = PropsValues.LOCALES;

		if (companyId != CompanyConstants.SYSTEM) {
			try {
				languageIds = PrefsPropsUtil.getStringArray(
					companyId, PropsKeys.LOCALES, StringPool.COMMA,
					PropsValues.LOCALES_ENABLED);
			}
			catch (SystemException se) {
				languageIds = PropsValues.LOCALES_ENABLED;
			}
		}

		_charEncodings = new HashMap<>();
		_duplicateLanguageCodes = new HashSet<>();
		_locales = new Locale[languageIds.length];
		_localesMap = new HashMap<>(languageIds.length);
		_localesSet = new HashSet<>(languageIds.length);

		for (int i = 0; i < languageIds.length; i++) {
			String languageId = languageIds[i];

			Locale locale = LocaleUtil.fromLanguageId(languageId, false);

			_charEncodings.put(locale.toString(), StringPool.UTF8);

			String language = languageId;

			int pos = languageId.indexOf(CharPool.UNDERLINE);

			if (pos > 0) {
				language = languageId.substring(0, pos);
			}

			if (_localesMap.containsKey(language)) {
				_duplicateLanguageCodes.add(language);
			}

			_locales[i] = locale;

			if (!_localesMap.containsKey(language)) {
				_localesMap.put(language, locale);
			}

			_localesSet.add(locale);
		}

		String[] localesBetaArray = PropsValues.LOCALES_BETA;

		_localesBetaSet = new HashSet<>(localesBetaArray.length);

		for (String languageId : localesBetaArray) {
			Locale locale = LocaleUtil.fromLanguageId(languageId, false);

			_localesBetaSet.add(locale);
		}
	}

	private String _escapePattern(String pattern) {
		return StringUtil.replace(
			pattern, StringPool.APOSTROPHE, StringPool.DOUBLE_APOSTROPHE);
	}

	private String _get(ResourceBundle resourceBundle, String key) {
		if (PropsValues.TRANSLATIONS_DISABLED) {
			return key;
		}

		if ((resourceBundle == null) || (key == null)) {
			return null;
		}

		String value = ResourceBundleUtil.getString(resourceBundle, key);

		if (value != null) {
			return LanguageResources.fixValue(value);
		}

		if ((key.length() > 0) &&
			(key.charAt(key.length() - 1) == CharPool.CLOSE_BRACKET)) {

			int pos = key.lastIndexOf(CharPool.OPEN_BRACKET);

			if (pos != -1) {
				key = key.substring(0, pos);

				return _get(resourceBundle, key);
			}
		}

		return null;
	}

	private String _getCharset() {
		return StringPool.UTF8;
	}

	private Locale _getLocale(HttpServletRequest request) {
		Locale locale = null;

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay != null) {
			locale = themeDisplay.getLocale();
		}
		else {
			locale = request.getLocale();

			if (!isAvailableLocale(locale)) {
				locale = LocaleUtil.getDefault();
			}
		}

		return locale;
	}

	private Locale _getLocale(String languageCode) {
		return _localesMap.get(languageCode);
	}

	private void _initGroupLocales(long groupId) {
		String[] languageIds = null;

		try {
			Group group = GroupLocalServiceUtil.getGroup(groupId);

			UnicodeProperties typeSettingsProperties =
				group.getTypeSettingsProperties();

			languageIds = StringUtil.split(
				typeSettingsProperties.getProperty(PropsKeys.LOCALES));
		}
		catch (Exception e) {
			languageIds = PropsValues.LOCALES_ENABLED;
		}

		Locale[] locales = new Locale[languageIds.length];
		Map<String, Locale> localesMap = new HashMap<>(languageIds.length);
		Set<Locale> localesSet = new HashSet<>(languageIds.length);

		for (int i = 0; i < languageIds.length; i++) {
			String languageId = languageIds[i];

			Locale locale = LocaleUtil.fromLanguageId(languageId, false);

			String language = languageId;

			int pos = languageId.indexOf(CharPool.UNDERLINE);

			if (pos > 0) {
				language = languageId.substring(0, pos);
			}

			locales[i] = locale;

			if (!localesMap.containsKey(language)) {
				localesMap.put(language, locale);
			}

			localesSet.add(locale);
		}

		_groupLocalesMap.put(groupId, locales);
		_groupLocalesSet.put(groupId, localesSet);
	}

	private void _resetAvailableGroupLocales(long groupId) {
		_groupLocalesMap.remove(groupId);
		_groupLocalesSet.remove(groupId);
	}

	private void _resetAvailableLocales(long companyId) {
		_portalCache.remove(companyId);
	}

	private static Log _log = LogFactoryUtil.getLog(LanguageImpl.class);

	private static Map<Long, LanguageImpl> _instances =
		new ConcurrentHashMap<>();
	private static Pattern _pattern = Pattern.compile(
		"Liferay\\.Language\\.get\\([\"']([^)]+)[\"']\\)");
	private static PortalCache<Long, Serializable> _portalCache =
		MultiVMPoolUtil.getCache(LanguageImpl.class.getName());

	static {
		PortalCacheMapSynchronizeUtil.<Long, Serializable>synchronize(
			_portalCache, _instances,
			new Synchronizer<Long, Serializable>() {

				@Override
				public void onSynchronize(
					Map<? extends Long, ? extends Serializable> map, Long key,
					Serializable value, int timeToLive) {

					_instances.remove(key);
				}

			});
	}

	private Map<String, String> _charEncodings;
	private Set<String> _duplicateLanguageCodes;
	private Map<Long, Locale[]> _groupLocalesMap = new HashMap<>();
	private Map<Long, Set<Locale>> _groupLocalesSet = new HashMap<>();
	private Locale[] _locales;
	private Set<Locale> _localesBetaSet;
	private Map<String, Locale> _localesMap;
	private Set<Locale> _localesSet;

}