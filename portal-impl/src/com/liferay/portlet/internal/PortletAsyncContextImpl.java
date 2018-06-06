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

package com.liferay.portlet.internal;

import com.liferay.portal.kernel.portlet.LiferayPortletAsyncContext;

import javax.portlet.PortletAsyncListener;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;

/**
 * @author Neil Griffin
 */
public class PortletAsyncContextImpl implements LiferayPortletAsyncContext {

	@Override
	public void addListener(AsyncListener asyncListener) {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void addListener(PortletAsyncListener portletAsyncListener)
		throws IllegalStateException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void addListener(
			PortletAsyncListener portletAsyncListener,
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IllegalStateException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void complete() throws IllegalStateException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends PortletAsyncListener> T createPortletAsyncListener(
			Class<T> listenerClass)
		throws PortletException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void dispatch() throws IllegalStateException {
		throw new UnsupportedOperationException();

		// TODO

	}

	@Override
	public void dispatch(String path) throws IllegalStateException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void doStart() {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public ResourceRequest getResourceRequest() throws IllegalStateException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public ResourceResponse getResourceResponse() throws IllegalStateException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public long getTimeout() {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasOriginalRequestAndResponse() {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCalledDispatch() {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void removeListener(AsyncListener asyncListener) {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void reset(AsyncContext asyncContext) {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void setTimeout(long timeout) {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void start(Runnable runnable) throws IllegalStateException {

		// TODO

		throw new UnsupportedOperationException();
	}

}