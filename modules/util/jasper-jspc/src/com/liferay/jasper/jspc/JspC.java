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
package com.liferay.jasper.jspc;

import com.liferay.portal.kernel.util.ReflectionUtil;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Iterator;
import javax.servlet.ServletContext;

import org.apache.jasper.JasperException;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.jasper.compiler.TagPluginManager;
import org.apache.jasper.compiler.TldLocationsCache;
import org.apache.jasper.servlet.JspCServletContext;

/**
 * @author Shuyang Zhou
 */
public class JspC extends org.apache.jasper.JspC {

	public static void main(String[] args) {
		System.out.println("In Main");

		JspC jspc = new JspC();

		try {
			jspc.setArgs(args);

			jspc.execute();
		}
		catch (Exception e) {
			System.err.println(e);

			if (jspc.dieLevel != NO_DIE_LEVEL) {
				System.exit(jspc.dieLevel);
			}
		}
	}

	@Override
	public void scanFiles(File baseDir) throws JasperException {
		System.out.println("In Scan");

		super.scanFiles(baseDir);

		Iterator<String> iterator = pages.iterator();

		while (iterator.hasNext()) {
			String page = iterator.next();

			if (page.contains("/docroot/META-INF/custom_jsps/")) {
				iterator.remove();
			}
		}
	}

	@Override
	protected void initServletContext() {
		try {
			context = new JspCServletContext(new PrintWriter(System.out),
				new URL("file:" + uriRoot.replace('\\', '/') + '/'));
			tldLocationsCache = TldLocationsCache.getInstance(context);
		}
		catch (MalformedURLException me) {
			System.out.println("**" + me);
		}
		rctxt = new JspRuntimeContext(context, this);
		jspConfig = new JspConfig(context);

		tagPluginManager = new TagPluginManager(context);
	}

	public class JspConfig extends org.apache.jasper.compiler.JspConfig {

		public JspConfig(ServletContext ctxt) {
			super(ctxt);
		}

		public void initialize() {
			try {
				Class<?> clazz = this.getClass().getSuperclass();

				Field field = ReflectionUtil.getDeclaredField(
					clazz, "initialized");

				if (field.getBoolean(this)) {
					return;
				}

				Method init = ReflectionUtil.getDeclaredMethod(
					clazz, "init");

				init.invoke(this);

				JspProperty defaultProperty = new JspProperty(
					null, null, null, null, null, null, "true", null, null,
					null, "false");

				field = ReflectionUtil.getDeclaredField(
					clazz, "defaultJspProperty");

				field.setAccessible(true);

				field.set(this, defaultProperty);
			}
			catch (Exception e) {
				ReflectionUtil.throwException(e);
			}
		}

		@Override
		public boolean isJspPage(String uri) throws JasperException {
			initialize();

			return super.isJspPage(uri);
		}

		@Override
		public JspProperty findJspProperty(String uri) throws JasperException {
			initialize();

			return super.findJspProperty(uri);
		}

	}

}
