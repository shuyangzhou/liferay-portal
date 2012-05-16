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

package com.liferay.portal.velocity;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.template.TemplateContextHelper;
import com.liferay.portal.template.TemplateResource;
import com.liferay.portal.template.TemplateResourceManager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;

/**
 * @author Tina Tian
 */
public class VelocityTemplate implements Template {

	public VelocityTemplate(
		String templateId, String templateContent, String errorTemplateId,
		String errorTemplateContent, VelocityContext velocityContext,
		VelocityEngine velocityEngine,
		TemplateContextHelper templateContextHelper,
		TemplateResourceManager templateResourceManager) {

		_templateId = templateId;
		_templateContent = templateContent;

		if (errorTemplateId != null) {
			_errorTemplateId = errorTemplateId;
			_errorTemplateContent = errorTemplateContent;
			_hasErrorTemplate = true;
		}

		if (velocityContext == null) {
			_velocityContext = new VelocityContext();
		}
		else {
			_velocityContext = new VelocityContext(velocityContext);
		}

		_velocityEngine = velocityEngine;
		_templateContextHelper = templateContextHelper;
		_templateResourceManager = templateResourceManager;
	}

	public Object get(String key) {
		return _velocityContext.get(key);
	}

	public void prepare(HttpServletRequest request) throws TemplateException {
		_templateContextHelper.prepare(this, request);
	}

	public boolean processTemplate(Writer writer) throws TemplateException {
		if (!_hasErrorTemplate) {
			try {
				return _processTemplate(_templateId, _templateContent, writer);
			}
			catch (Exception e) {
				_log.error(
					"Unable to process Velocity template " + _templateId);

				throw new TemplateException(e);
			}
		}

		try {
			UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

			boolean result = _processTemplate(
				_templateId, _templateContent, unsyncStringWriter);

			StringBundler sb = unsyncStringWriter.getStringBundler();

			sb.writeTo(writer);

			return result;
		}
		catch (Exception e1) {
			put("exception", e1.getMessage());
			put("script", _templateContent);

			if (e1 instanceof ParseErrorException) {
				ParseErrorException pee = (ParseErrorException)e1;

				put("column", pee.getColumnNumber());
				put("line", pee.getLineNumber());
			}

			try {
				 _processTemplate(
					_errorTemplateId, _errorTemplateContent, writer);

				return false;
			}
			catch (Exception e2) {
				_log.error(
					"Unable to process Velocity template " + _errorTemplateId);

				throw new TemplateException(e2);
			}
		}
	}

	public void put(String key, Object value) {
		if (value == null) {
			return;
		}

		_velocityContext.put(key, value);
	}

	private boolean _processTemplate(
			String templateId, String templateContent, Writer writer)
		throws Exception {

		Reader reader = null;

		try {
			TemplateResource templateResource =
				_templateResourceManager.findTemplateResource(
					templateId, templateContent);

			if (templateResource == null) {
				throw new Exception(
					"Unable to find template resource with templateId " +
						templateId + ", template content" + templateContent);
			}

			InputStream inputStream = _templateResourceManager.getInputStream(
				templateResource);

			if (inputStream == null) {
				throw new Exception(
					"Unable to find template resource with templateId " +
						templateId + ", templateContent " + templateContent);
			}

			reader = new InputStreamReader(inputStream);

			return _velocityEngine.evaluate(
				_velocityContext, writer, templateId, reader);
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(VelocityTemplate.class);

	private String _errorTemplateContent;
	private String _errorTemplateId;
	private boolean _hasErrorTemplate;
	private String _templateContent;
	private TemplateContextHelper _templateContextHelper;
	private String _templateId;
	private TemplateResourceManager _templateResourceManager;
	private VelocityContext _velocityContext;
	private VelocityEngine _velocityEngine;

}