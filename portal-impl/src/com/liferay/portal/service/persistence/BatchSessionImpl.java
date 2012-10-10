/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.service.persistence;

import com.liferay.portal.kernel.dao.orm.ORMException;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InitialThreadLocal;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.util.PropsValues;

/**
 * @author Raymond Augé
 * @author Brian Wing Shun Chan
 */
public class BatchSessionImpl implements BatchSession {

	public void delete(Session session, BaseModel<?> model)
		throws ORMException {

		if (model.isCachedModel() || isEnabled()) {
			Object staleObject = session.get(
				model.getClass(), model.getPrimaryKeyObj());

			if (staleObject != null) {
				session.evict(staleObject);
			}
		}

		session.delete(model);

		if (!isEnabled()) {
			session.flush();

			return;
		}

		if ((PropsValues.HIBERNATE_JDBC_BATCH_SIZE == 0) ||
			((_counter.get() % PropsValues.HIBERNATE_JDBC_BATCH_SIZE) == 0)) {

			session.flush();
		}

		_counter.set(_counter.get() + 1);
	}

	public boolean isEnabled() {
		if (!PropsValues.SPRING_HIBERNATE_SESSION_DELEGATED) {
			StringBundler sb = new StringBundler(6);

			sb.append("Batch processing must not be enabled when ");
			sb.append(StringPool.OPEN_CURLY_BRACE);
			sb.append(PropsKeys.SPRING_HIBERNATE_SESSION_DELEGATED);
			sb.append(StringPool.EQUAL);
			sb.append(Boolean.FALSE.toString());
			sb.append(StringPool.CLOSE_CURLY_BRACE);

			_log.warn(sb.toString());

			return false;
		}

		return _enabled.get();
	}

	public void setEnabled(boolean enabled) {
		_enabled.set(enabled);
	}

	public void update(Session session, BaseModel<?> model, boolean merge)
		throws ORMException {

		if (merge || model.isCachedModel()) {
			session.merge(model);
		}
		else {
			if (model.isNew()) {
				session.save(model);
			}
			else {
				boolean contains = false;

				if (isEnabled()) {
					Object obj = session.get(
						model.getClass(), model.getPrimaryKeyObj());

					if ((obj != null) && obj.equals(model)) {
						contains = true;
					}
				}

				if (!contains && !session.contains(model)) {
					session.saveOrUpdate(model);
				}
			}
		}

		if (!isEnabled()) {
			session.flush();

			return;
		}

		if ((PropsValues.HIBERNATE_JDBC_BATCH_SIZE == 0) ||
			((_counter.get() % PropsValues.HIBERNATE_JDBC_BATCH_SIZE) == 0)) {

			session.flush();
		}

		_counter.set(_counter.get() + 1);
	}

	private static Log _log = LogFactoryUtil.getLog(BatchSessionImpl.class);

	private static final long _INITIAL_COUNTER = 1;

	private static ThreadLocal<Long> _counter = new InitialThreadLocal<Long>(
		BatchSessionImpl.class + "._counter", _INITIAL_COUNTER);
	private static ThreadLocal<Boolean> _enabled =
		new InitialThreadLocal<Boolean>(
			BatchSessionImpl.class + "._enabled", false);

}