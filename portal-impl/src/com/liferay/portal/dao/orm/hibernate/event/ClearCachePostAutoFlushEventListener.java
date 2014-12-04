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

import org.hibernate.HibernateException;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.event.def.DefaultAutoFlushEventListener;

/**
 * @author Preston Crary
 */
public class ClearCachePostAutoFlushEventListener
	extends DefaultAutoFlushEventListener {

	public static final ClearCachePostAutoFlushEventListener INSTANCE =
		new ClearCachePostAutoFlushEventListener();

	@Override
	protected void postFlush(SessionImplementor sessionImplementor)
		throws HibernateException {

		super.postFlush(sessionImplementor);

		PersistenceContext persistenceContext =
			sessionImplementor.getPersistenceContext();

		persistenceContext.clear();
	}

}