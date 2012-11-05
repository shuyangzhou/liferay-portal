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

package com.liferay.portlet.journal.util;

import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateContextType;
import com.liferay.portal.kernel.templateparser.BaseTransformer;
import com.liferay.portal.kernel.templateparser.TemplateFactory;
import com.liferay.portal.kernel.templateparser.TemplateFactoryContext;
import com.liferay.portal.kernel.templateparser.TransformException;
import com.liferay.portal.kernel.templateparser.TransformerListener;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portlet.dynamicdatalists.util.DDLTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Marcellus Tavares
 * @author Tina Tian
 */
public class JournalTransformer extends BaseTransformer {

	@Override
	protected Template getTemplate(
			TemplateFactoryContext templateFactoryContext)
		throws TransformException {

		String langType = templateFactoryContext.getLangType();

		String className = PropsUtil.get(
			PropsKeys.JOURNAL_TEMPLATE_LANGUAGE_FACTORY, new Filter(langType));
		String errorTemplate = PropsUtil.get(
			PropsKeys.JOURNAL_ERROR_TEMPLATE, new Filter(langType));

		Tuple tuple = new Tuple(
			className, TemplateContextType.RESTRICTED, errorTemplate);

		TemplateFactory templateFactory = _templateFactories.get(tuple);

		if (templateFactory == null) {
			try {
				templateFactory = (TemplateFactory)InstanceFactory.newInstance(
					PortalClassLoaderUtil.getClassLoader(), className,
					new Class[]{String.class, TemplateContextType.class},
					new Object[]{errorTemplate, TemplateContextType.STANDARD});

				_templateFactories.put(tuple, templateFactory);
			}
			catch (Exception e) {
				throw new TransformException(e);
			}
		}

		Template template = templateFactory.getTemplate(templateFactoryContext);

		template.put(
			"journalTemplatesPath",
			_getJournalTemplatesPath(templateFactoryContext.getThemeDisplay()));

		return template;
	}

	@Override
	protected List<TransformerListener> getTransformerListeners() {
		if (_transformerListeners != null) {
			return Collections.unmodifiableList(_transformerListeners);
		}

		List<TransformerListener> transformerListeners =
			new ArrayList<TransformerListener>();

		String[] classNames = PropsUtil.getArray(
			PropsKeys.JOURNAL_TRANSFORMER_LISTENER);

		ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();

		for (String className : classNames) {
			TransformerListener transformerListener;

			try {
				transformerListener =
					(TransformerListener)InstanceFactory.newInstance(
						classLoader, className);

				transformerListeners.add(transformerListener);
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		_transformerListeners = transformerListeners;

		return Collections.unmodifiableList(transformerListeners);
	}

	private String _getJournalTemplatesPath(ThemeDisplay themeDisplay) {
		long companyId = 0;
		long groupId = 0;

		if (themeDisplay != null) {
			companyId = themeDisplay.getCompanyId();
			groupId = themeDisplay.getScopeGroupId();
		}

		StringBundler sb = new StringBundler(5);

		sb.append(TemplateConstants.JOURNAL_SEPARATOR);
		sb.append(StringPool.SLASH);
		sb.append(companyId);
		sb.append(StringPool.SLASH);
		sb.append(groupId);

		return sb.toString();
	}

	private static Log _log = LogFactoryUtil.getLog(
		DDLTransformer.class.getName());

	private static Map<Tuple, TemplateFactory> _templateFactories =
		new ConcurrentHashMap<Tuple, TemplateFactory>();
	private static List<TransformerListener> _transformerListeners;

}