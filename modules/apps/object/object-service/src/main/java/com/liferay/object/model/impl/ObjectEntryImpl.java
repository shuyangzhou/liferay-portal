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

package com.liferay.object.model.impl;

import com.liferay.object.service.ObjectEntryLocalServiceUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.cache.CacheField;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marco Leo
 * @author Brian Wing Shun Chan
 */
public class ObjectEntryImpl extends ObjectEntryBaseImpl {

	@Override
	public Map<String, Serializable> getValues() {

		// TODO Cache does not work for ObjectEntryLocalServiceTest#_getValues

		if (_values == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("Get values for object entry " + getObjectEntryId());
			}

			try {
				_values = ObjectEntryLocalServiceUtil.getValues(this);
			}
			catch (Exception exception) {
				_log.error(exception, exception);

				return new HashMap<>();
			}
		}
		else if (_log.isDebugEnabled()) {
			_log.debug(
				"Use cached values for object entry " + getObjectEntryId());
		}

		return _values;
	}

	@Override
	public void setValues(Map<String, Serializable> values) {
		_values = values;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectEntryImpl.class);

	@CacheField(propagateToInterface = true)
	private Map<String, Serializable> _values;

}