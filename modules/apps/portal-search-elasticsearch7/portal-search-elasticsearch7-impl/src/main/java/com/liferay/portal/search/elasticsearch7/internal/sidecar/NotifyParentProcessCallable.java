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

package com.liferay.portal.search.elasticsearch7.internal.sidecar;

import com.liferay.petra.process.ProcessCallable;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.Serializable;

/**
 * @author Tina Tian
 */
public class NotifyParentProcessCallable
	implements ProcessCallable<Serializable> {

	public NotifyParentProcessCallable(String message) {
		_message = message;
	}

	@Override
	public Serializable call() {
		Log log = LogFactoryUtil.getLog(NotifyParentProcessCallable.class);

		if (log.isInfoEnabled()) {
			log.info(_message);
		}

		return null;
	}

	private static final long serialVersionUID = 1L;

	private final String _message;

}