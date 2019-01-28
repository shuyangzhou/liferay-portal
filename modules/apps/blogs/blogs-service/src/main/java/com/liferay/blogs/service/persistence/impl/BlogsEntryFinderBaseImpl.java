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

package com.liferay.blogs.service.persistence.impl;

import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.service.persistence.BlogsEntryPersistence;

import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;

import org.osgi.service.component.annotations.Reference;

import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public abstract class BlogsEntryFinderBaseImpl extends BasePersistenceImpl<BlogsEntry> {
	public BlogsEntryFinderBaseImpl() {
		setModelClass(BlogsEntry.class);
	}

	@Override
	public Set<String> getBadColumnNames() {
		return blogsEntryPersistence.getBadColumnNames();
	}

	@Reference
	protected BlogsEntryPersistence blogsEntryPersistence;
}