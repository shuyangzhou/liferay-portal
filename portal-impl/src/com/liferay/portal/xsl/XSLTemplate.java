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

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.template.TemplateContextHelper;

import java.io.Writer;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Tina Tian
 */
public class XSLTemplate implements Template {

	public XSLTemplate(
		XSLTemplateResource xslTemplateResource,
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper) {

		if (xslTemplateResource == null) {
			throw new IllegalArgumentException("XSL template resource is null");
		}

		if (templateContextHelper == null) {
			throw new IllegalArgumentException(
				"Template context helper is null");
		}

		_templateContextHelper = templateContextHelper;
		_xslTemplateResource = xslTemplateResource;
		_errorTemplateResource = errorTemplateResource;

		Locale locale = LocaleUtil.fromLanguageId(
			xslTemplateResource.getLanguageId());

		_xslErrorListener = new XSLErrorListener(locale);

		_transformerFactory = TransformerFactory.newInstance();

		_transformerFactory.setErrorListener(_xslErrorListener);

		URIResolver uriResolver = new URIResolver(
			xslTemplateResource.getTokens(),
			xslTemplateResource.getLanguageId());

		_transformerFactory.setURIResolver(uriResolver);

		try {
			StreamSource scriptSource = new StreamSource(
				xslTemplateResource.getReader());

			_transformer = _transformerFactory.newTransformer(scriptSource);

			if (context != null) {
				for (Map.Entry<String, Object> entry : context.entrySet()) {
					_transformer.setParameter(entry.getKey(), entry.getValue());
				}
			}

			if (errorTemplateResource != null) {
				scriptSource = new StreamSource(
					errorTemplateResource.getReader());

				_errorTransformer = _transformerFactory.newTransformer(
					scriptSource);
			}

			_xmlSource = new StreamSource(xslTemplateResource.getXMLReader());
		}
		catch (Exception ex) {
			throw new IllegalStateException(
				"Unable to get transformer with script " +
					xslTemplateResource.getTemplateId(),
				ex);
		}
	}

	public Object get(String key) {
		return _transformer.getParameter(key);
	}

	public void prepare(HttpServletRequest request) {
		_templateContextHelper.prepare(this, request);
	}

	public boolean processTemplate(Writer writer) throws TemplateException {
		if (_errorTemplateResource == null) {
			try {
				_transformer.transform(_xmlSource, new StreamResult(writer));

				return true;
			}
			catch (Exception e) {
				throw new TemplateException(
					"Unable to process XSL template " +
						_xslTemplateResource.getTemplateId(),
					e);
			}
		}

		Writer oldWriter = (Writer)get(TemplateConstants.WRITER);

		try {
			UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

			put(TemplateConstants.WRITER, unsyncStringWriter);

			_transformer.transform(
				_xmlSource, new StreamResult(unsyncStringWriter));

			StringBundler sb = unsyncStringWriter.getStringBundler();

			sb.writeTo(writer);

			return true;
		}
		catch (Exception e) {
			put(TemplateConstants.WRITER, writer);

			try {
				_errorTransformer.setParameter(
					"exception", _xslErrorListener.getMessageAndLocation());

				if (_errorTemplateResource instanceof StringTemplateResource) {
					StringTemplateResource stringTemplateResource =
						(StringTemplateResource)_errorTemplateResource;

					_errorTransformer.setParameter(
						"script", stringTemplateResource.getContent());
				}

				if (_xslErrorListener.getLocation() != null) {
					_errorTransformer.setParameter(
						"column",
						new Integer(_xslErrorListener.getColumnNumber()));
					_errorTransformer.setParameter(
						"line", new Integer(_xslErrorListener.getLineNumber()));
				}

				_errorTransformer.transform(
					_xmlSource, new StreamResult(writer));
			}
			catch (Exception ex) {
				throw new TemplateException(
					"Unable to process XSL template " +
						_errorTemplateResource.getTemplateId(),
					ex);
			}

			return false;
		}
		finally {
			put(TemplateConstants.WRITER, oldWriter);
		}
	}

	public void put(String key, Object value) {
		if (value == null) {
			return;
		}

		_transformer.setParameter(key, value);
		_errorTransformer.setParameter(key, value);
	}

	private TemplateResource _errorTemplateResource;
	private Transformer _errorTransformer;
	private TemplateContextHelper _templateContextHelper;
	private Transformer _transformer;
	private TransformerFactory _transformerFactory;
	private StreamSource _xmlSource;
	private XSLErrorListener _xslErrorListener;
	private XSLTemplateResource _xslTemplateResource;

}