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

package com.liferay.portal.freemarker;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.template.TemplateContextHelper;
import com.liferay.portal.template.TemplateResource;
import com.liferay.portal.template.TemplateResourceLoader;
import com.liferay.portal.template.TemplateResourceManager;

import freemarker.core.Environment;
import freemarker.core.ParseException;

import freemarker.template.Configuration;

import java.io.Reader;
import java.io.Writer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mika Koivisto
 * @author Tina Tian
 */
public class FreeMarkerTemplate implements Template {

	public FreeMarkerTemplate(
		String templateId, String templateContent, String errorTemplateId,
		String errorTemplateContent, Map<String, Object> context,
		Configuration configuration,
		TemplateContextHelper templateContextHelper,
		TemplateResourceManager templateResourceManager,
		Map<String, String> autoImportLibraries) {

		_templateId = templateId;
		_templateContent = templateContent;

		if (errorTemplateId != null) {
			_errorTemplateId = errorTemplateId;
			_errorTemplateContent = errorTemplateContent;
			_hasErrorTemplate = true;
		}

		_context = new ConcurrentHashMap<String, Object>();

		if (context != null) {
			for (Map.Entry<String, Object> entry : context.entrySet()) {
				_doPut(entry.getKey(), entry.getValue());
			}
		}

		_configuration = configuration;
		_templateContextHelper = templateContextHelper;
		_templateResourceManager = templateResourceManager;
		_autoImportLibraries = autoImportLibraries;
	}

	public Object get(String key) {
		return _context.get(key);
	}

	public void prepare(HttpServletRequest request) throws TemplateException {
		_templateContextHelper.prepare(this, request);
	}

	public boolean processTemplate(Writer writer) throws TemplateException {
		if (!_hasErrorTemplate) {
			try {
				_processTemplate(_templateId, _templateContent, writer);

				return true;
			}
			catch (Exception e) {
				_log.error(
					"Unable to process FreeMarker template " + _templateId);

				throw new TemplateException(e);
			}
		}

		try {
			UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

			_processTemplate(_templateId, _templateContent, unsyncStringWriter);

			StringBundler sb = unsyncStringWriter.getStringBundler();

			sb.writeTo(writer);

			return true;
		}
		catch (Exception e1) {
			if ((e1 instanceof ParseException) ||
				(e1 instanceof freemarker.template.TemplateException)) {

				put("exception", e1.getMessage());
				put("script", _templateContent);

				if (e1 instanceof ParseException) {
					ParseException pe = (ParseException)e1;

					put("column", pe.getColumnNumber());
					put("line", pe.getLineNumber());
				}

				try {
					_processTemplate(
						_errorTemplateId, _errorTemplateContent, writer);
				}
				catch (Exception e2) {
					_log.error(
						"Unable to process FreeMarker template " +
							_errorTemplateId);

					throw new TemplateException(e2);
				}
			}
			else {
				_log.error(
					"Unable to process FreeMarker template " + _templateId);

				throw new TemplateException(e1);
			}
		}

		return false;
	}

	public void put(String key, Object value) {
		_doPut(key, value);
	}

	private void _doPut(String key, Object value) {
		if (value == null) {
			return;
		}

		_context.put(key, value);
	}

	private void _processTemplate(
			String templateId, String templateContent, Writer writer)
		throws Exception {

		Set<Reader> readers = new HashSet<Reader>();

		try {
			TemplateResource<?> templateResource =
				_templateResourceManager.findTemplateResource(
					templateId, templateContent);

			if (templateResource == null) {
				throw new Exception(
					"Unable to find template resource with templateId " +
						templateId + ", templateContent " + templateContent);
			}

			Reader reader = templateResource.getReader();

			readers.add(reader);

			freemarker.template.Template template =
				new freemarker.template.Template(
					templateResource.getName(), reader, _configuration,
					TemplateResourceLoader.ENCODING);

			Environment environment = template.createProcessingEnvironment(
				_context, writer, null);

			importLibrary(environment, readers);

			environment.process();
		}
		finally {
			for (Reader reader : readers) {
				reader.close();
			}
		}
	}

	private void importLibrary(Environment environment, Set<Reader> readers)
		throws Exception {

		if (_autoImportLibraries == null) {
			return;
		}

		for (Map.Entry<String, String> entry :
			_autoImportLibraries.entrySet()) {

			String key = entry.getKey();
			String value = entry.getValue();

			TemplateResource<?> templateResource =
				_templateResourceManager.findTemplateResource(value);

			Reader reader = templateResource.getReader();

			readers.add(reader);

			freemarker.template.Template importTemplate =
				new freemarker.template.Template(
					templateResource.getName(), reader, _configuration,
					TemplateResourceLoader.ENCODING);

			environment.importLib(importTemplate, key);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(FreeMarkerTemplate.class);

	private Map<String, String> _autoImportLibraries;
	private Configuration _configuration;
	private Map<String, Object> _context;
	private String _errorTemplateContent;
	private String _errorTemplateId;
	private boolean _hasErrorTemplate;
	private String _templateContent;
	private TemplateContextHelper _templateContextHelper;
	private String _templateId;
	private TemplateResourceManager _templateResourceManager;

}