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

package com.liferay.portal.dao.orm.hibernate.event;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.HibernateException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.event.MergeEvent;
import org.hibernate.event.def.DefaultMergeEventListener;

/**
 * @author Tom Wang
 * @author Preston Crary
 */
public class CIMergeEventListener extends DefaultMergeEventListener {

	public static final CIMergeEventListener INSTANCE =
		new CIMergeEventListener();

	@Override
	public void onMerge(MergeEvent event) throws HibernateException {
		try {
			super.onMerge(event);

			_logEvent(event);
		}
		catch (StaleObjectStateException sose) {
			_throwStaleObjectStateExceptionCause(event, sose);
		}
	}

	private CIMergeEventListener() {
	}

	private String _getKey(BaseModel<?> baseModel) {
		MVCCModel mvccModel = (MVCCModel)baseModel;

		StringBundler sb = new StringBundler(5);

		sb.append(baseModel.getModelClassName());
		sb.append(StringPool.DASH);
		sb.append(baseModel.getPrimaryKeyObj());
		sb.append(StringPool.DASH);
		sb.append(mvccModel.getMvccVersion());

		return sb.toString();
	}

	private void _logEvent(MergeEvent event) {
		Object object = event.getOriginal();

		if (!(object instanceof MVCCModel)) {
			return;
		}

		BaseModel<?> baseModel = (BaseModel<?>)object;

		String key = _getKey(baseModel);

		_previousUpdates.put(
			key,
			new StaleObjectStateException(
				baseModel.getModelClassName(), baseModel.getPrimaryKeyObj()));
	}

	private void _throwStaleObjectStateExceptionCause(
		MergeEvent event, StaleObjectStateException sose) {

		Object object = event.getOriginal();

		if (!(object instanceof MVCCModel)) {
			throw sose;
		}

		String key = _getKey((BaseModel<?>)object);

		StaleObjectStateException cause = _previousUpdates.get(key);

		sose.initCause(cause);

		throw sose;
	}

	private final Map<String, StaleObjectStateException> _previousUpdates =
		new ConcurrentHashMap<>();

}