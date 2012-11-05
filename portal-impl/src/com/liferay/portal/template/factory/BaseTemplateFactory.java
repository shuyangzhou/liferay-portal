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

package com.liferay.portal.template.factory;

import com.liferay.portal.kernel.mobile.device.Device;
import com.liferay.portal.kernel.mobile.device.UnknownDevice;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateContextType;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.templateparser.TemplateFactory;
import com.liferay.portal.kernel.templateparser.TemplateFactoryContext;
import com.liferay.portal.kernel.templateparser.TemplateNode;
import com.liferay.portal.kernel.templateparser.TransformException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.PwdGenerator;

import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 * @author Marcellus Tavares
 */
public abstract class BaseTemplateFactory implements TemplateFactory {

	public BaseTemplateFactory(
		String errorTemplateId, TemplateContextType templateContextType) {

		_errorTemplateId = errorTemplateId;
		_templateContextType = templateContextType;
	}

	public Template getTemplate(TemplateFactoryContext templateFactoryContext)
		throws TransformException {

		try {
			TemplateResource templateResource = getTemplateResource(
				templateFactoryContext);

			Template template = TemplateManagerUtil.getTemplate(
				templateFactoryContext.getLangType(), templateResource,
				getErrorTemplateResource(), _templateContextType);

			if (Validator.isNotNull(templateFactoryContext.getXML())) {
				Document document = SAXReaderUtil.read(
					templateFactoryContext.getXML());

				Element rootElement = document.getRootElement();

				List<TemplateNode> templateNodes = getTemplateNodes(
					rootElement, templateFactoryContext.getThemeDisplay());

				if (templateNodes != null) {
					for (TemplateNode templateNode : templateNodes) {
						template.put(templateNode.getName(), templateNode);
					}
				}

				Element requestElement = rootElement.element("request");

				template.put("request", insertRequestVariables(requestElement));
				template.put("xmlRequest", requestElement.asXML());
			}

			Map<String, Object> contextObjects =
				templateFactoryContext.getContextObjects();

			if (contextObjects != null) {
				for (String key : contextObjects.keySet()) {
					template.put(key, contextObjects.get(key));
				}
			}

			populateTemplateContext(template, templateFactoryContext);

			return template;
		}
		catch (Exception e) {
			if (e instanceof DocumentException) {
				throw new TransformException("Unable to read XML document", e);
			}
			else if (e instanceof IOException) {
				throw new TransformException("Error reading template", e);
			}
			else if (e instanceof TransformException) {
				throw (TransformException)e;
			}
			else {
				throw new TransformException("Unhandled exception", e);
			}
		}
	}

	protected Company getCompany(TemplateFactoryContext templateFactoryContext)
		throws Exception {

		ThemeDisplay themeDisplay = templateFactoryContext.getThemeDisplay();

		if (themeDisplay != null) {
			return themeDisplay.getCompany();
		}

		return CompanyLocalServiceUtil.getCompany(
			getCompanyId(templateFactoryContext));
	}

	protected long getCompanyGroupId(
		TemplateFactoryContext templateFactoryContext) {

		ThemeDisplay themeDisplay = templateFactoryContext.getThemeDisplay();

		if (themeDisplay != null) {
			return themeDisplay.getCompanyGroupId();
		}

		Map<String, String> tokens = templateFactoryContext.getTokens();

		if (tokens != null) {
			return GetterUtil.getLong(tokens.get("company_group_id"));
		}

		return 0;
	}

	protected long getCompanyId(TemplateFactoryContext templateFactoryContext) {
		ThemeDisplay themeDisplay = templateFactoryContext.getThemeDisplay();

		if (themeDisplay != null) {
			return themeDisplay.getCompanyId();
		}

		Map<String, String> tokens = templateFactoryContext.getTokens();

		if (tokens != null) {
			return GetterUtil.getLong(tokens.get("company_id"));
		}

		return 0;
	}

	protected Device getDevice(TemplateFactoryContext templateFactoryContext) {
		ThemeDisplay themeDisplay = templateFactoryContext.getThemeDisplay();

		if (themeDisplay != null) {
			return themeDisplay.getDevice();
		}

		return UnknownDevice.getInstance();
	}

	protected TemplateResource getErrorTemplateResource() {
		try {
			Class<?> clazz = getClass();

			ClassLoader classLoader = clazz.getClassLoader();

			URL url = classLoader.getResource(_errorTemplateId);

			return new URLTemplateResource(_errorTemplateId, url);
		}
		catch (Exception e) {
		}

		return null;
	}

	protected long getGroupId(TemplateFactoryContext templateFactoryContext) {
		ThemeDisplay themeDisplay = templateFactoryContext.getThemeDisplay();

		if (themeDisplay != null) {
			return themeDisplay.getScopeGroupId();
		}

		Map<String, String> tokens = templateFactoryContext.getTokens();

		if (tokens != null) {
			return GetterUtil.getLong(tokens.get("group_id"));
		}

		return 0;
	}

	protected String getTemplateId(
		TemplateFactoryContext templateFactoryContext) {

		long companyGroupId = getCompanyGroupId(templateFactoryContext);

		String templateId = null;

		Map<String, String> tokens = templateFactoryContext.getTokens();
		Map<String, Object> contextObjects =
			templateFactoryContext.getContextObjects();

		if (tokens != null) {
			templateId = tokens.get("template_id");
		}

		if (Validator.isNull(templateId)) {
			if (contextObjects != null) {
				templateId = String.valueOf(contextObjects.get("template_id"));
			}
		}

		StringBundler sb = new StringBundler(5);

		sb.append(getCompanyId(templateFactoryContext));
		sb.append(StringPool.POUND);

		if (companyGroupId > 0) {
			sb.append(companyGroupId);
		}
		else {
			sb.append(getGroupId(templateFactoryContext));
		}

		sb.append(StringPool.POUND);
		sb.append(templateId);

		return sb.toString();
	}

	protected List<TemplateNode> getTemplateNodes(
			Element element, ThemeDisplay themeDisplay)
		throws Exception {

		List<TemplateNode> templateNodes = new ArrayList<TemplateNode>();

		Map<String, TemplateNode> prototypeTemplateNodes =
			new HashMap<String, TemplateNode>();

		List<Element> dynamicElementElements = element.elements(
			"dynamic-element");

		for (Element dynamicElementElement : dynamicElementElements) {
			Element dynamicContentElement = dynamicElementElement.element(
				"dynamic-content");

			String data = StringPool.BLANK;

			if (dynamicContentElement != null) {
				data = dynamicContentElement.getText();
			}

			String name = dynamicElementElement.attributeValue(
				"name", StringPool.BLANK);

			if (name.length() == 0) {
				throw new TransformException(
					"Element missing \"name\" attribute");
			}

			String type = dynamicElementElement.attributeValue(
				"type", StringPool.BLANK);

			TemplateNode templateNode = new TemplateNode(
				themeDisplay, name, stripCDATA(data), type);

			if (dynamicElementElement.element("dynamic-element") != null) {
				templateNode.appendChildren(
					getTemplateNodes(dynamicElementElement, themeDisplay));
			}
			else if ((dynamicContentElement != null) &&
					 (dynamicContentElement.element("option") != null)) {

				List<Element> optionElements = dynamicContentElement.elements(
					"option");

				for (Element optionElement : optionElements) {
					templateNode.appendOption(
						stripCDATA(optionElement.getText()));
				}
			}

			TemplateNode prototypeTemplateNode = prototypeTemplateNodes.get(
				name);

			if (prototypeTemplateNode == null) {
				prototypeTemplateNode = templateNode;

				prototypeTemplateNodes.put(name, prototypeTemplateNode);

				templateNodes.add(templateNode);
			}

			prototypeTemplateNode.appendSibling(templateNode);
		}

		return templateNodes;
	}

	protected abstract TemplateResource getTemplateResource(
			TemplateFactoryContext templateFactoryContext)
		throws Exception;

	protected Map<String, Object> insertRequestVariables(Element element) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (element == null) {
			return map;
		}

		for (Element childElement : element.elements()) {
			String name = childElement.getName();

			if (name.equals("attribute")) {
				Element nameElement = childElement.element("name");
				Element valueElement = childElement.element("value");

				map.put(nameElement.getText(), valueElement.getText());
			}
			else if (name.equals("parameter")) {
				Element nameElement = childElement.element("name");

				List<Element> valueElements = childElement.elements("value");

				if (valueElements.size() == 1) {
					Element valueElement = valueElements.get(0);

					map.put(nameElement.getText(), valueElement.getText());
				}
				else {
					List<String> values = new ArrayList<String>();

					for (Element valueElement : valueElements) {
						values.add(valueElement.getText());
					}

					map.put(nameElement.getText(), values);
				}
			}
			else if (childElement.elements().size() > 0) {
				map.put(name, insertRequestVariables(childElement));
			}
			else {
				map.put(name, childElement.getText());
			}
		}

		return map;
	}

	protected void populateTemplateContext(
			Template template, TemplateFactoryContext templateFactoryContext)
		throws Exception {

		template.put("company", getCompany(templateFactoryContext));
		template.put("companyId", getCompanyId(templateFactoryContext));
		template.put("device", getDevice(templateFactoryContext));
		template.put("groupId", getGroupId(templateFactoryContext));

		Locale locale = LocaleUtil.fromLanguageId(
			templateFactoryContext.getLanguageId());

		template.put("locale", locale);

		template.put(
			"permissionChecker", PermissionThreadLocal.getPermissionChecker());
		template.put("viewMode", templateFactoryContext.getViewMode());

		String randomNamespace =
			PwdGenerator.getPassword(PwdGenerator.KEY3, 4) +
				StringPool.UNDERLINE;

		template.put("randomNamespace", randomNamespace);
	}

	protected String stripCDATA(String s) {
		if (s.startsWith(StringPool.CDATA_OPEN) &&
			s.endsWith(StringPool.CDATA_CLOSE)) {

			s = s.substring(
				StringPool.CDATA_OPEN.length(),
				s.length() - StringPool.CDATA_CLOSE.length());
		}

		return s;
	}

	private String _errorTemplateId;
	private TemplateContextType _templateContextType;

}