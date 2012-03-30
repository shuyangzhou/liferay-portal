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

import com.liferay.portal.freemarker.JournalTemplateLoader;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.template.engine.TemplateEngine;
import com.liferay.portal.kernel.template.engine.TemplateEngineContext;
import com.liferay.portal.kernel.template.engine.TemplateEngineException;
import com.liferay.portal.kernel.template.engine.TemplateEngineUtil;
import com.liferay.portal.kernel.templateparser.TemplateContext;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.util.PropsValues;
import com.liferay.util.ContentUtil;

import freemarker.core.ParseException;

/**
 * @author Mika Koivisto
 */
public class FreeMarkerTemplateParser extends VelocityTemplateParser {

	@Override
	protected String getErrorTemplateContent() {
		return ContentUtil.get(PropsValues.JOURNAL_ERROR_TEMPLATE_FREEMARKER);
	}

	@Override
	protected String getErrorTemplateId() {
		return PropsValues.JOURNAL_ERROR_TEMPLATE_FREEMARKER;
	}

	@Override
	protected String getJournalTemplatesPath() {
		StringBundler sb = new StringBundler(5);

		sb.append(JournalTemplateLoader.JOURNAL_SEPARATOR);
		sb.append(StringPool.SLASH);
		sb.append(getCompanyId());
		sb.append(StringPool.SLASH);
		sb.append(getGroupId());

		return sb.toString();
	}

	@Override
	protected TemplateContext getTemplateContext() throws Exception {
		return TemplateEngineUtil.getWrappedRestrictedToolsContext(
			TemplateEngine.FREE_MARKER);
	}

	@Override
	protected boolean mergeTemplate(
			TemplateContext templateContext,
			UnsyncStringWriter unsyncStringWriter)
		throws Exception {

		TemplateEngineContext freeMarkerContext =
			(TemplateEngineContext)templateContext;

		try {
			return TemplateEngineUtil.mergeTemplate(
				TemplateEngine.FREE_MARKER, getTemplateId(), getScript(),
				freeMarkerContext, unsyncStringWriter);
		}
		catch (Exception e) {
			if (e instanceof TemplateEngineException) {
				String errorTemplateId = getErrorTemplateId();
				String errorTemplateContent = getErrorTemplateContent();

				Throwable throwable = e.getCause();

				freeMarkerContext.put("exception", throwable.getMessage());
				freeMarkerContext.put("script", getScript());

				if (throwable instanceof ParseException) {
					ParseException pe = (ParseException)throwable;

					freeMarkerContext.put("column", pe.getColumnNumber());
					freeMarkerContext.put("line", pe.getLineNumber());
				}

				unsyncStringWriter.reset();

				return TemplateEngineUtil.mergeTemplate(
					TemplateEngine.FREE_MARKER, errorTemplateId,
					errorTemplateContent, freeMarkerContext,
					unsyncStringWriter);
			}
			else {
				throw e;
			}
		}
	}

}