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

package com.liferay.portal.xsl;

import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateContextType;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.security.lang.PortalSecurityManagerThreadLocal;
import com.liferay.portal.security.pacl.PACLClassLoaderUtil;
import com.liferay.portal.security.pacl.PACLPolicy;
import com.liferay.portal.security.pacl.PACLPolicyManager;
import com.liferay.portal.template.RestrictedTemplate;
import com.liferay.portal.template.TemplateContextHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tina Tina
 */
public class XSLManager implements TemplateManager {

	public void destroy() {
		if (_restrictedHelperUtilities == null) {
			return;
		}

		_classLoaderHelperUtilities.clear();

		_classLoaderHelperUtilities = null;

		_restrictedHelperUtilities.clear();

		_restrictedHelperUtilities = null;

		_standardHelperUtilities.clear();

		_standardHelperUtilities = null;

		_templateContextHelper = null;
	}

	public void destroy(ClassLoader classLoader) {
		_classLoaderHelperUtilities.remove(classLoader);
	}

	public String getName() {
		return TemplateManager.XSL;
	}

	public Template getTemplate(
		TemplateResource templateResource,
		TemplateContextType templateContextType) {

		return getTemplate(templateResource, null, templateContextType);
	}

	public Template getTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource,
		TemplateContextType templateContextType) {

		if (!(templateResource instanceof XSLTemplateResource)) {
			throw new IllegalArgumentException(
				"Template resource is not an XSLTemplateResource");
		}

		XSLTemplateResource xslTemplateResource =
			(XSLTemplateResource)templateResource;

		if (templateContextType.equals(TemplateContextType.CLASS_LOADER)) {

			// This template will have all of its utilities initialized within
			// the class loader of the current thread

			ClassLoader contextClassLoader =
				PACLClassLoaderUtil.getContextClassLoader();

			PACLPolicy threadLocalPACLPolicy =
				PortalSecurityManagerThreadLocal.getPACLPolicy();

			PACLPolicy contextClassLoaderPACLPolicy =
				PACLPolicyManager.getPACLPolicy(contextClassLoader);

			try {
				PortalSecurityManagerThreadLocal.setPACLPolicy(
					contextClassLoaderPACLPolicy);

				Map<String, Object> helperUtilities =
					_classLoaderHelperUtilities.get(contextClassLoader);

				if (helperUtilities == null) {
					helperUtilities =
						_templateContextHelper.getHelperUtilities();

					_classLoaderHelperUtilities.put(
						contextClassLoader, helperUtilities);
				}

				return new PACLXSLTemplate(
					xslTemplateResource, errorTemplateResource, helperUtilities,
					_templateContextHelper, contextClassLoaderPACLPolicy);
			}
			finally {
				PortalSecurityManagerThreadLocal.setPACLPolicy(
					threadLocalPACLPolicy);
			}
		}
		else if (templateContextType.equals(TemplateContextType.EMPTY)) {
			return new XSLTemplate(
				xslTemplateResource, errorTemplateResource, null,
				_templateContextHelper);
		}
		else if (templateContextType.equals(TemplateContextType.RESTRICTED)) {
			return new RestrictedTemplate(
				new XSLTemplate(
					xslTemplateResource, errorTemplateResource,
					_restrictedHelperUtilities, _templateContextHelper),
				_templateContextHelper.getRestrictedVariables());
		}
		else if (templateContextType.equals(TemplateContextType.STANDARD)) {
			return new XSLTemplate(
				xslTemplateResource, errorTemplateResource,
				_standardHelperUtilities, _templateContextHelper);
		}

		return null;
	}

	public void init() throws TemplateException {
		if (_restrictedHelperUtilities != null) {
			return;
		}

		_restrictedHelperUtilities =
			_templateContextHelper.getRestrictedHelperUtilities();
		_standardHelperUtilities = _templateContextHelper.getHelperUtilities();
	}

	public void setTemplateContextHelper(
		TemplateContextHelper templateContextHelper) {

		_templateContextHelper = templateContextHelper;
	}

	private Map<ClassLoader, Map<String, Object>> _classLoaderHelperUtilities =
		new ConcurrentHashMap<ClassLoader, Map<String, Object>>();
	private Map<String, Object> _restrictedHelperUtilities;
	private Map<String, Object> _standardHelperUtilities;
	private TemplateContextHelper _templateContextHelper;

}