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

package com.liferay.portlet;

import java.io.IOException;

import javax.portlet.PortletAsyncContext;
import javax.portlet.PortletAsyncListener;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

/**
 * @author Dante Wang
 */
public class PortletAsyncListenerAdapter implements AsyncListener {

	public PortletAsyncListenerAdapter(
		PortletAsyncContext portletAsyncContext) {

		// TODO

		throw new UnsupportedOperationException();
	}

	public void addListener(PortletAsyncListener portletAsyncListener)
		throws IllegalStateException {

		// TODO

		throw new UnsupportedOperationException();
	}

	public void addListener(
			PortletAsyncListener portletAsyncListener,
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws IllegalStateException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void onComplete(AsyncEvent asyncEvent) throws IOException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void onError(AsyncEvent asyncEvent) throws IOException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void onStartAsync(AsyncEvent asyncEvent) throws IOException {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public void onTimeout(AsyncEvent asyncEvent) throws IOException {

		// TODO

		throw new UnsupportedOperationException();
	}

}