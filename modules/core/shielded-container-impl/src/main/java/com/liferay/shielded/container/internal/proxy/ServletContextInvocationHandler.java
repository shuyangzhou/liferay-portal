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

package com.liferay.shielded.container.internal.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * @author Shuyang Zhou
 */
public class ServletContextInvocationHandler implements InvocationHandler {

	public ServletContextInvocationHandler(
		ServletContext servletContext, ClassLoader classLoader) {

		_servletContext = servletContext;
		_classLoader = classLoader;
	}

	public FilterRegistration.Dynamic addFilter(
		String filterName, Class<? extends Filter> filterClass) {

		try {
			return _servletContext.addFilter(
				filterName,
				_createContextClassLoaderProxy(
					_createFilterProxy(new LazyInstanceSupplier<>(filterClass)),
					Filter.class));
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new RuntimeException(noSuchMethodException);
		}
	}

	public FilterRegistration.Dynamic addFilter(
		String filterName, Filter filter) {

		return _servletContext.addFilter(
			filterName,
			_createContextClassLoaderProxy(
				_createFilterProxy(() -> filter), Filter.class));
	}

	public FilterRegistration.Dynamic addFilter(
		String filterName, String filterClassName) {

		try {
			return addFilter(
				filterName,
				(Class<? extends Filter>)_classLoader.loadClass(
					filterClassName));
		}
		catch (ClassNotFoundException classNotFoundException) {
			throw new RuntimeException(classNotFoundException);
		}
	}

	public void addListener(Class<? extends EventListener> listenerClass) {
		try {
			Constructor<? extends EventListener> constructor =
				listenerClass.getConstructor();

			addListener(constructor.newInstance());
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	public void addListener(String listenerClassName) {
		try {
			addListener(
				(Class<? extends EventListener>)_classLoader.loadClass(
					listenerClassName));
		}
		catch (ClassNotFoundException classNotFoundException) {
			throw new RuntimeException(classNotFoundException);
		}
	}

	public <T extends EventListener> void addListener(T t) {
		_servletContext.addListener(_createContextClassLoaderProxy(t));
	}

	public ServletRegistration.Dynamic addServlet(
		String servletName, Class<? extends Servlet> servletClass) {

		try {
			return _servletContext.addServlet(
				servletName,
				_createContextClassLoaderProxy(
					_createServletProxy(
						new LazyInstanceSupplier<>(servletClass)),
					Servlet.class));
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new RuntimeException(noSuchMethodException);
		}
	}

	public ServletRegistration.Dynamic addServlet(
		String servletName, Servlet servlet) {

		return _servletContext.addServlet(
			servletName,
			_createContextClassLoaderProxy(
				_createServletProxy(() -> servlet), Servlet.class));
	}

	public ServletRegistration.Dynamic addServlet(
		String servletName, String servletClassName) {

		try {
			return addServlet(
				servletName,
				(Class<? extends Servlet>)_classLoader.loadClass(
					servletClassName));
		}
		catch (ClassNotFoundException classNotFoundException) {
			throw new RuntimeException(classNotFoundException);
		}
	}

	public Object getAttribute(String name) {
		return _servletContext.getAttribute(_encodeName(name));
	}

	public Enumeration<String> getAttributeNames() {
		List<String> names = new ArrayList<>();

		Enumeration<String> enumeration = _servletContext.getAttributeNames();

		while (enumeration.hasMoreElements()) {
			names.add(_decodeName(enumeration.nextElement()));
		}

		return Collections.enumeration(names);
	}

	public ClassLoader getClassLoader() {
		return _classLoader;
	}

	public String getInitParameter(String name) {
		return _servletContext.getInitParameter(_encodeName(name));
	}

	public Enumeration<String> getInitParameterNames() {
		List<String> names = new ArrayList<>();

		Enumeration<String> enumeration =
			_servletContext.getInitParameterNames();

		while (enumeration.hasMoreElements()) {
			names.add(_decodeName(enumeration.nextElement()));
		}

		return Collections.enumeration(names);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

		Method mappingMethod = _methodMappingsMap.get(method);

		if (mappingMethod == null) {
			return method.invoke(_servletContext, args);
		}

		return mappingMethod.invoke(this, args);
	}

	public void removeAttribute(String name) {
		_servletContext.removeAttribute(_encodeName(name));
	}

	public void setAttribute(String name, Object object) {
		_servletContext.setAttribute(_encodeName(name), object);
	}

	public boolean setInitParameter(String name, String value) {
		return _servletContext.setInitParameter(_encodeName(name), value);
	}

	public void setProxiedServletContext(ServletContext proxiedServletContext) {
		_proxiedServletContext = proxiedServletContext;
	}

	private <T> T _createContextClassLoaderProxy(T t, Class<?>... interfaces) {
		if (interfaces.length == 0) {
			Class<?> clazz = t.getClass();

			Set<Class<?>> interfaceClasses = new LinkedHashSet<>();

			while (clazz != null) {
				for (Class<?> interfaceClass : clazz.getInterfaces()) {
					interfaceClasses.add(interfaceClass);
				}

				clazz = clazz.getSuperclass();
			}

			interfaces = interfaceClasses.toArray(new Class<?>[0]);
		}

		return (T)Proxy.newProxyInstance(
			_classLoader, interfaces,
			new ContextClassLoaderInvocationHandler(_classLoader, t));
	}

	private Filter _createFilterProxy(
		Supplier<? extends Filter> filterSupplier) {

		return (Filter)Proxy.newProxyInstance(
			_classLoader, new Class<?>[] {Filter.class},
			new FilterInvocationHandler(
				filterSupplier, _proxiedServletContext));
	}

	private Servlet _createServletProxy(
		Supplier<? extends Servlet> servletSupplier) {

		return (Servlet)Proxy.newProxyInstance(
			_classLoader, new Class<?>[] {Servlet.class},
			new ServletInvocationHandler(
				servletSupplier, _proxiedServletContext));
	}

	private String _decodeName(String name) {
		if (name.startsWith(_LIFERAY_NAMESPACE)) {
			return name.substring(_LIFERAY_NAMESPACE.length());
		}

		return name;
	}

	private String _encodeName(String name) {
		if (name.startsWith(_APACHE_NAMESPACE)) {
			return _LIFERAY_NAMESPACE.concat(name);
		}

		return name;
	}

	private static final String _APACHE_NAMESPACE = "org.apache.";

	private static final String _LIFERAY_NAMESPACE = "com.liferay.";

	private static final Map<Method, Method> _methodMappingsMap =
		new HashMap<>();

	static {
		try {
			Method invokeMethod =
				ServletContextInvocationHandler.class.getDeclaredMethod(
					"invoke", Object.class, Method.class, Object[].class);
			Method setProxiedServletContextMethod =
				ServletContextInvocationHandler.class.getDeclaredMethod(
					"setProxiedServletContext", ServletContext.class);

			for (Method method :
					ServletContextInvocationHandler.class.
						getDeclaredMethods()) {

				if (!Modifier.isPublic(method.getModifiers()) ||
					method.equals(invokeMethod) ||
					method.equals(setProxiedServletContextMethod)) {

					continue;
				}

				_methodMappingsMap.put(
					ServletContext.class.getMethod(
						method.getName(), method.getParameterTypes()),
					method);
			}
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new ExceptionInInitializerError(noSuchMethodException);
		}
	}

	private final ClassLoader _classLoader;
	private ServletContext _proxiedServletContext;
	private final ServletContext _servletContext;

}