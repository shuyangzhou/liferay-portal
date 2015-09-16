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

package com.liferay.portal.service;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author Matthew Tambara
 */
public class DIVERetryAdviceAcceptor implements RetryAdviceAcceptor {

	@Override
	public Boolean accept(Object returnValue, Throwable t) {
		if (t == null) {
			return false;
		}

		Throwable cause = t.getCause();

		while (t != cause) {
			if (t instanceof DataIntegrityViolationException) {
				return true;
			}

			if (cause == null) {
				return false;
			}

			t = cause;

			cause = t.getCause();
		}

		return false;
	}

}