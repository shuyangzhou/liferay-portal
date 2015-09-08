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

package com.liferay.portal.kernel.scheduler;

import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.StringBundler;

/**
 * @author Tina Tian
 */
public class IntervalTriggerContent
	implements TriggerContent<ObjectValuePair<Integer, TimeUnit>> {

	public IntervalTriggerContent(int interval, TimeUnit timeUnit) {
		_objectValuePair = new ObjectValuePair<>(interval, timeUnit);
	}

	@Override
	public ObjectValuePair<Integer, TimeUnit> getTriggerContent() {
		return _objectValuePair;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(5);

		sb.append("{interval=");
		sb.append(_objectValuePair.getKey());
		sb.append(", timeUnit=");
		sb.append(_objectValuePair.getValue());
		sb.append("}");

		return sb.toString();
	}

	private final ObjectValuePair<Integer, TimeUnit> _objectValuePair;

}