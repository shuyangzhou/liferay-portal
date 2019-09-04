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

package com.liferay.taglib.servlet;

import java.io.IOException;
import java.io.Writer;

import java.util.Enumeration;

import javax.el.ELContext;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.ErrorData;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * @author Matthew Tambara
 */
public class AutoClosePageContextWrapper extends PageContext {

	public AutoClosePageContextWrapper(PageContext pageContext) {
		_pageContext = pageContext;
	}

	@Override
	public Object findAttribute(String name) {
		return _pageContext.findAttribute(name);
	}

	@Override
	public void forward(String relativeUrlPath)
		throws IOException, ServletException {

		_pageContext.forward(relativeUrlPath);
	}

	@Override
	public Object getAttribute(String name) {
		return _pageContext.getAttribute(name);
	}

	@Override
	public Object getAttribute(String name, int scope) {
		return _pageContext.getAttribute(name, scope);
	}

	@Override
	public Enumeration<String> getAttributeNamesInScope(int scope) {
		return _pageContext.getAttributeNamesInScope(scope);
	}

	@Override
	public int getAttributesScope(String name) {
		return _pageContext.getAttributesScope(name);
	}

	@Override
	public ELContext getELContext() {
		return _pageContext.getELContext();
	}

	@Override
	public ErrorData getErrorData() {
		return _pageContext.getErrorData();
	}

	@Override
	public Exception getException() {
		return _pageContext.getException();
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	@Override
	public ExpressionEvaluator getExpressionEvaluator() {
		return _pageContext.getExpressionEvaluator();
	}

	@Override
	public JspWriter getOut() {
		return _pageContext.getOut();
	}

	@Override
	public Object getPage() {
		return _pageContext.getPage();
	}

	@Override
	public ServletRequest getRequest() {
		return _pageContext.getRequest();
	}

	@Override
	public ServletResponse getResponse() {
		return _pageContext.getResponse();
	}

	@Override
	public ServletConfig getServletConfig() {
		return _pageContext.getServletConfig();
	}

	@Override
	public ServletContext getServletContext() {
		return _pageContext.getServletContext();
	}

	@Override
	public HttpSession getSession() {
		return _pageContext.getSession();
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	@Override
	public VariableResolver getVariableResolver() {
		return _pageContext.getVariableResolver();
	}

	public PageContext getWrappedPageContext() {
		return _pageContext;
	}

	@Override
	public void handlePageException(Exception e)
		throws IOException, ServletException {

		_pageContext.handlePageException(e);
	}

	@Override
	public void handlePageException(Throwable t)
		throws IOException, ServletException {

		_pageContext.handlePageException(t);
	}

	@Override
	public void include(String relativeUrlPath)
		throws IOException, ServletException {

		_pageContext.include(relativeUrlPath);
	}

	@Override
	public void include(String relativeUrlPath, boolean flush)
		throws IOException, ServletException {

		_pageContext.include(relativeUrlPath, flush);
	}

	@Override
	public void initialize(
			Servlet servlet, ServletRequest servletRequest,
			ServletResponse servletResponse, String errorPageURL,
			boolean needsSession, int bufferSize, boolean autoFlush)
		throws IllegalArgumentException, IllegalStateException, IOException {

		_pageContext.initialize(
			servlet, servletRequest, servletResponse, errorPageURL,
			needsSession, bufferSize, autoFlush);
	}

	@Override
	public JspWriter popBody() {
		return _pageContext.popBody();
	}

	@Override
	public BodyContent pushBody() {
		return _pageContext.pushBody();
	}

	@Override
	public JspWriter pushBody(Writer writer) {
		return _pageContext.pushBody(new PipingJspWriter(writer));
	}

	public void registerCloseCallback(Runnable runnable) {
		_runnable = runnable;
	}

	@Override
	public void release() {
		_pageContext.release();

		if (_runnable != null) {
			_runnable.run();
		}
	}

	@Override
	public void removeAttribute(String name) {
		_pageContext.removeAttribute(name);
	}

	@Override
	public void removeAttribute(String name, int scope) {
		_pageContext.removeAttribute(name, scope);
	}

	@Override
	public void setAttribute(String name, Object value) {
		_pageContext.setAttribute(name, value);
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		_pageContext.setAttribute(name, value, scope);
	}

	private final PageContext _pageContext;
	private Runnable _runnable;

}