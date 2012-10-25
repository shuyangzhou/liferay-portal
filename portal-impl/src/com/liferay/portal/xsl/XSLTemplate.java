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

import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.template.AbstractTemplate;
import com.liferay.portal.template.TemplateContextHelper;

import java.io.Writer;

import java.util.Locale;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author Tina Tian
 */
public class XSLTemplate extends AbstractTemplate {

	public XSLTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper) {

		super(
			templateResource, errorTemplateResource, templateContextHelper,
			TemplateManager.XSL, 0);

		if (!(templateResource instanceof XSLTemplateResource)) {
			throw new IllegalArgumentException(
				"Template resource is not an XSLTemplateResource");
		}

		XSLTemplateResource xslTemplateResource =
			(XSLTemplateResource)templateResource;

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
					templateResource.getTemplateId(),
				ex);
		}
	}

	public Object get(String key) {
		return _transformer.getParameter(key);
	}

	public void put(String key, Object value) {
		if (value == null) {
			return;
		}

		_transformer.setParameter(key, value);
		_errorTransformer.setParameter(key, value);
	}

	@Override
	protected void handleException(Exception exception, Writer writer)
		throws TemplateException {

		try {
			_errorTransformer.setParameter(
				"exception", _xslErrorListener.getMessageAndLocation());

			if (templateResource instanceof StringTemplateResource) {
				StringTemplateResource stringTemplateResource =
					(StringTemplateResource)templateResource;

				_errorTransformer.setParameter(
					"script", stringTemplateResource.getContent());
			}

			if (_xslErrorListener.getLocation() != null) {
				_errorTransformer.setParameter(
					"column", new Integer(_xslErrorListener.getColumnNumber()));
				_errorTransformer.setParameter(
					"line", new Integer(_xslErrorListener.getLineNumber()));
			}

			_errorTransformer.transform(_xmlSource, new StreamResult(writer));
		}
		catch (Exception ex) {
			throw new TemplateException(
				"Unable to process FreeMarker template " +
					errorTemplateResource.getTemplateId(),
				ex);
		}
	}

	@Override
	protected void processTemplate(
			TemplateResource templateResource, Writer writer)
		throws Exception {

		_transformer.transform(_xmlSource, new StreamResult(writer));
	}

	private Transformer _errorTransformer;
	private Transformer _transformer;
	private TransformerFactory _transformerFactory;
	private StreamSource _xmlSource;
	private XSLErrorListener _xslErrorListener;

}