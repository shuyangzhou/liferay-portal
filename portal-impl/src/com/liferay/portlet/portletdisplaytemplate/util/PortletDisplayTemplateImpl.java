/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.portletdisplaytemplate.util;

import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mobile.device.Device;
import com.liferay.portal.kernel.mobile.device.UnknownDevice;
import com.liferay.portal.kernel.servlet.GenericServletWrapper;
import com.liferay.portal.kernel.servlet.PipingServletResponse;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateContextType;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.templateparser.TransformerListener;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.xsl.XSLTemplateResource;
import com.liferay.portlet.dynamicdatamapping.NoSuchTemplateException;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.taglib.util.VelocityTaglib;
import com.liferay.util.PwdGenerator;
import com.liferay.util.freemarker.FreeMarkerTaglibFactoryUtil;

import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;

import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.RenderRequest;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

/**
 * @author Eduardo Garcia
 * @author Juan Fernández
 * @author Brian Wing Shun Chan
 */
public class PortletDisplayTemplateImpl implements PortletDisplayTemplate {

	public DDMTemplate fetchDDMTemplate(long groupId, String displayStyle) {
		try {
			Group group = GroupLocalServiceUtil.getGroup(groupId);

			Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(
				group.getCompanyId());

			if (!displayStyle.startsWith("ddmTemplate_")) {
				return null;
			}

			String uuid = displayStyle.substring(12);

			if (Validator.isNull(uuid)) {
				return null;
			}

			try {
				return
					DDMTemplateLocalServiceUtil.getDDMTemplateByUuidAndGroupId(
						uuid, groupId);
			}
			catch (NoSuchTemplateException nste) {
			}

			try {
				return
					DDMTemplateLocalServiceUtil.getDDMTemplateByUuidAndGroupId(
						uuid, companyGroup.getGroupId());
			}
			catch (NoSuchTemplateException nste) {
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return null;
	}

	public long getDDMTemplateGroupId(ThemeDisplay themeDisplay) {
		try {
			Group scopeGroup = themeDisplay.getScopeGroup();

			if (scopeGroup.isLayout()) {
				scopeGroup = scopeGroup.getParentGroup();
			}

			if (scopeGroup.isStagingGroup()) {
				Group liveGroup = scopeGroup.getLiveGroup();

				if (!liveGroup.isStagedPortlet(
						PortletKeys.PORTLET_DISPLAY_TEMPLATES)) {

					return liveGroup.getGroupId();
				}
			}

			return scopeGroup.getGroupId();
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		return themeDisplay.getScopeGroupId();
	}

	public long getPortletDisplayTemplateDDMTemplateId(
		ThemeDisplay themeDisplay, String displayStyle) {

		long portletDisplayDDMTemplateId = 0;

		long portletDisplayDDMTemplateGroupId = getDDMTemplateGroupId(
			themeDisplay);

		if (displayStyle.startsWith("ddmTemplate_")) {
			DDMTemplate portletDisplayDDMTemplate = fetchDDMTemplate(
				portletDisplayDDMTemplateGroupId, displayStyle);

			if (portletDisplayDDMTemplate != null) {
				portletDisplayDDMTemplateId =
					portletDisplayDDMTemplate.getTemplateId();
			}
		}

		return portletDisplayDDMTemplateId;
	}

	public String renderDDMTemplate(
			PageContext pageContext, long ddmTemplateId, List<?> entries)
		throws Exception {

		Map<String, Object> contextObjects = new HashMap<String, Object>();

		return renderDDMTemplate(
			pageContext, ddmTemplateId, entries, contextObjects);
	}

	public String renderDDMTemplate(
			PageContext pageContext, long ddmTemplateId, List<?> entries,
			Map<String, Object> contextObjects)
		throws Exception {

		HttpServletRequest request =
			(HttpServletRequest)pageContext.getRequest();

		RenderRequest renderRequest = (RenderRequest)request.getAttribute(
			JavaConstants.JAVAX_PORTLET_REQUEST);

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long companyId = themeDisplay.getCompanyId();
		long groupId = themeDisplay.getCompanyGroupId();

		if (groupId <= 0) {
			groupId = themeDisplay.getScopeGroupId();
		}

		String templateId = _getTemplateId(
			companyId, groupId, String.valueOf(ddmTemplateId));

		DDMTemplate ddmTemplate = DDMTemplateLocalServiceUtil.getTemplate(
			ddmTemplateId);

		TemplateResource templateResource = new StringTemplateResource(
			templateId, ddmTemplate.getScript());

		String langType = ddmTemplate.getLanguage();

		String errorTemplateId = PropsUtil.get(
			PropsKeys.DYNAMIC_DATA_LISTS_ERROR_TEMPLATE, new Filter(langType));

		Template template = TemplateManagerUtil.getTemplate(
			langType, templateResource,
			_getErrorTemplateResource(errorTemplateId),
			TemplateContextType.STANDARD);

		template.prepare(request);

		for (String key : contextObjects.keySet()) {
			template.put(key, contextObjects.get(key));
		}

		if (langType.equals(TemplateConstants.LANG_TYPE_FTL)) {
			_addTaglibSupportFTL(template, pageContext);
		}
		else if (langType.equals(TemplateConstants.LANG_TYPE_VM)) {
			_addTaglibSupportVM(template, pageContext);
		}

		template.put(
			PortletDisplayTemplateConstants.DDM_TEMPLATE_ID, ddmTemplateId);
		template.put(PortletDisplayTemplateConstants.ENTRIES, entries);

		if (entries.size() == 1) {
			template.put(PortletDisplayTemplateConstants.ENTRY, entries.get(0));
		}

		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		template.processTemplate(unsyncStringWriter);

		return unsyncStringWriter.toString();
	}

	public String renderDDMTemplate(
			ThemeDisplay themeDisplay, Map<String, String> tokens,
			String viewMode, String languageId, String xml, String script,
			String langType)
		throws Exception {

		String errorTemplateId = PropsUtil.get(
			PropsKeys.DYNAMIC_DATA_LISTS_ERROR_TEMPLATE, new Filter(langType));

		List<TransformerListener> transformerListeners =
			_getTransformerListeners(
				PropsKeys.DYNAMIC_DATA_LISTS_TRANSFORMER_LISTENER);

		return doRenderTemplate(
			themeDisplay, tokens, viewMode, languageId, xml, script, langType,
			transformerListeners, errorTemplateId,
			TemplateContextType.STANDARD);
	}

	public String renderJournalTemplate(
			ThemeDisplay themeDisplay, Map<String, String> tokens,
			String viewMode, String languageId, String xml, String script,
			String langType)
		throws Exception {

		String errorTemplateId = PropsUtil.get(
			PropsKeys.JOURNAL_ERROR_TEMPLATE, new Filter(langType));

		List<TransformerListener> transformerListeners =
			_getTransformerListeners(PropsKeys.JOURNAL_TRANSFORMER_LISTENER);

		return doRenderTemplate(
			themeDisplay, tokens, viewMode, languageId, xml, script, langType,
			transformerListeners, errorTemplateId,
			TemplateContextType.RESTRICTED);
	}

	protected String doRenderTemplate(
			ThemeDisplay themeDisplay, Map<String, String> tokens,
			String viewMode, String languageId, String xml, String script,
			String langType, List<TransformerListener> transformerListeners,
			String errorTemplateId, TemplateContextType templateContextType)
		throws Exception {

		if (_log.isDebugEnabled()) {
			_log.debug("Language " + languageId);
		}

		if (Validator.isNull(viewMode)) {
			viewMode = Constants.VIEW;
		}

		if (_logTokens.isDebugEnabled()) {
			String tokensString = PropertiesUtil.list(tokens);

			_logTokens.debug(tokensString);
		}

		if (_logTransformBefore.isDebugEnabled()) {
			_logTransformBefore.debug(xml);
		}

		for (TransformerListener transformerListener : transformerListeners) {

			// Modify XML

			if (_logXmlBeforeListener.isDebugEnabled()) {
				_logXmlBeforeListener.debug(xml);
			}

			if (transformerListener != null) {
				xml = transformerListener.onXml(xml, languageId, tokens);

				if (_logXmlAfterListener.isDebugEnabled()) {
					_logXmlAfterListener.debug(xml);
				}
			}

			// Modify script

			if (_logScriptBeforeListener.isDebugEnabled()) {
				_logScriptBeforeListener.debug(script);
			}

			if (transformerListener != null) {
				script = transformerListener.onScript(
					script, xml, languageId, tokens);

				if (_logScriptAfterListener.isDebugEnabled()) {
					_logScriptAfterListener.debug(script);
				}
			}
		}

		// Transform

		String output = null;

		if (Validator.isNull(langType)) {
			output = LocalizationUtil.getLocalization(xml, languageId);
		}
		else {
			long companyId = 0;
			long groupId = 0;
			Company company = null;
			Device device = null;
			String templateId = StringPool.BLANK;
			Locale locale = LocaleUtil.fromLanguageId(languageId);

			if (themeDisplay == null) {
				if (tokens != null) {
					companyId = GetterUtil.getLong(tokens.get("company_id"));
					groupId = GetterUtil.getLong(tokens.get("group_id"));
				}

				company = CompanyLocalServiceUtil.getCompany(companyId);
				device = UnknownDevice.getInstance();
			}
			else {
				companyId = themeDisplay.getCompanyId();
				groupId = themeDisplay.getCompanyGroupId();
				company = themeDisplay.getCompany();
				device = themeDisplay.getDevice();

				if (groupId <= 0) {
					groupId = themeDisplay.getScopeGroupId();
				}
			}

			if (tokens != null) {
				templateId = tokens.get("template_id");
			}

			templateId = _getTemplateId(companyId, groupId, templateId);

			TemplateResource templateResource = null;

			if (langType.equals(TemplateConstants.LANG_TYPE_XSL)) {
				templateResource = new XSLTemplateResource(
					templateId, script, tokens, languageId, xml);
			}
			else {
				templateResource = new StringTemplateResource(
					templateId, script);
			}

			Template template = TemplateManagerUtil.getTemplate(
				langType, templateResource,
				_getErrorTemplateResource(errorTemplateId),
				templateContextType);

			template.prepare(themeDisplay, xml);

			template.put(
				"journalTemplatesPath",
				_getJournalTemplatesPath(companyId, groupId));
			template.put(
				"randomNamespace",
				PwdGenerator.getPassword(PwdGenerator.KEY3, 4) +
					StringPool.UNDERLINE);
			template.put("company", company);
			template.put("companyId", companyId);
			template.put("device", device);
			template.put("groupId", groupId);
			template.put("locale", locale);
			template.put(
				"permissionChecker",
				PermissionThreadLocal.getPermissionChecker());
			template.put("viewMode", viewMode);

			UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

			template.processTemplate(unsyncStringWriter);

			output = unsyncStringWriter.toString();
		}

		// Postprocess output

		for (TransformerListener transformerListener : transformerListeners) {

			// Modify output

			if (_logOutputBeforeListener.isDebugEnabled()) {
				_logOutputBeforeListener.debug(output);
			}

			output = transformerListener.onOutput(output, languageId, tokens);

			if (_logOutputAfterListener.isDebugEnabled()) {
				_logOutputAfterListener.debug(output);
			}
		}

		if (_logTransfromAfter.isDebugEnabled()) {
			_logTransfromAfter.debug(output);
		}

		return output;
	}

	private void _addTaglibSupportFTL(
			Template template, PageContext pageContext)
		throws Exception {

		// FreeMarker servlet application

		final Servlet servlet = (Servlet)pageContext.getPage();

		GenericServlet genericServlet = null;

		if (servlet instanceof GenericServlet) {
			genericServlet = (GenericServlet)servlet;
		}
		else {
			genericServlet = new GenericServletWrapper(servlet);

			genericServlet.init(pageContext.getServletConfig());
		}

		ServletContextHashModel servletContextHashModel =
			new ServletContextHashModel(
				genericServlet, ObjectWrapper.DEFAULT_WRAPPER);

		template.put(
			PortletDisplayTemplateConstants.FREEMARKER_SERVLET_APPLICATION,
			servletContextHashModel);

		// FreeMarker servlet request

		HttpServletRequest request =
			(HttpServletRequest)pageContext.getRequest();
		HttpServletResponse response =
			(HttpServletResponse)pageContext.getResponse();

		HttpRequestHashModel requestHashModel = new HttpRequestHashModel(
			request, response, ObjectWrapper.DEFAULT_WRAPPER);

		template.put(
			PortletDisplayTemplateConstants.FREEMARKER_SERVLET_REQUEST,
			requestHashModel);

		// Taglib Liferay hash

		TemplateHashModel taglibLiferayHash =
			FreeMarkerTaglibFactoryUtil.createTaglibFactory(
				pageContext.getServletContext());

		template.put(
			PortletDisplayTemplateConstants.TAGLIB_LIFERAY_HASH,
			taglibLiferayHash);
	}

	private void _addTaglibSupportVM(
		Template template, PageContext pageContext) {

		template.put(
			PortletDisplayTemplateConstants.TAGLIB_LIFERAY,
			_getVelocityTaglib(template, pageContext));
	}

	private TemplateResource _getErrorTemplateResource(String errorTemplateId) {
		try {
			Class<?> clazz = getClass();

			ClassLoader classLoader = clazz.getClassLoader();

			URL url = classLoader.getResource(errorTemplateId);

			return new URLTemplateResource(errorTemplateId, url);
		}
		catch (Exception e) {
		}

		return null;
	}

	private String _getJournalTemplatesPath(long companyId, long groupId) {
		StringBundler sb = new StringBundler(5);

		sb.append(TemplateConstants.JOURNAL_SEPARATOR);
		sb.append(StringPool.SLASH);
		sb.append(companyId);
		sb.append(StringPool.SLASH);
		sb.append(groupId);

		return sb.toString();
	}

	private String _getTemplateId(
		long companyId, long groupId, String templateId) {

		StringBundler sb = new StringBundler(5);

		sb.append(companyId);
		sb.append(StringPool.POUND);
		sb.append(groupId);
		sb.append(StringPool.POUND);
		sb.append(templateId);

		return sb.toString();
	}

	private List<TransformerListener> _getTransformerListeners(String key) {
		List<TransformerListener> transformerListeners =
			new ArrayList<TransformerListener>();

		String[] listeners = PropsUtil.getArray(key);

		for (int i = 0; i < listeners.length; i++) {
			TransformerListener listener = null;

			try {
				if (_log.isDebugEnabled()) {
					_log.debug("Instantiate listener " + listeners[i]);
				}

				ClassLoader classLoader =
					PortalClassLoaderUtil.getClassLoader();

				listener = (TransformerListener)InstanceFactory.newInstance(
					classLoader, listeners[i]);

				transformerListeners.add(listener);
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		return transformerListeners;
	}

	private VelocityTaglib _getVelocityTaglib(
		Template template, PageContext pageContext) {

		HttpServletRequest request =
			(HttpServletRequest)pageContext.getRequest();

		HttpSession session = request.getSession();

		ServletContext servletContext = session.getServletContext();

		HttpServletResponse response =
			(HttpServletResponse)pageContext.getResponse();

		VelocityTaglib velocityTaglib = new VelocityTaglib(
			servletContext, request,
			new PipingServletResponse(response, pageContext.getOut()),
			pageContext, template);

		return velocityTaglib;
	}

	private static Log _log = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class);

	private static Log _logOutputAfterListener = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class.getName() + ".OutputAfterListener");
	private static Log _logOutputBeforeListener = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class.getName() + ".OutputBeforeListener");
	private static Log _logScriptAfterListener = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class.getName() + ".ScriptAfterListener");
	private static Log _logScriptBeforeListener = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class.getName() + ".ScriptBeforeListener");
	private static Log _logTokens = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class.getName() + ".Tokens");
	private static Log _logTransformBefore = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class.getName() + ".TransformBefore");
	private static Log _logTransfromAfter = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class.getName() + ".TransformAfter");
	private static Log _logXmlAfterListener = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class.getName() + ".XmlAfterListener");
	private static Log _logXmlBeforeListener = LogFactoryUtil.getLog(
		PortletDisplayTemplateImpl.class.getName() + ".XmlBeforeListener");

}