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

package com.liferay.ant.jgit;

import java.io.IOException;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Repository;

/**
 * @author Shuyang Zhou
 */
public class DirCacheCachedRepositoryWrapper extends RepositoryWrapper {

	public DirCacheCachedRepositoryWrapper(Repository repository) {
		super(repository);
	}

	@Override
	public DirCache readDirCache()
		throws CorruptObjectException, IOException, NoWorkTreeException {

		if (_dirCacheReference != null) {
			DirCache dirCache = _dirCacheReference.get();

			if (dirCache != null) {
				return dirCache;
			}
		}

		DirCache dirCache = null;

		synchronized(DirCacheCachedRepositoryWrapper.class) {
			if (_dirCacheReference != null) {
				dirCache = _dirCacheReference.get();

				if (dirCache != null) {
					return dirCache;
				}
			}

			dirCache = repository.readDirCache();

			_dirCacheReference = new SoftReference<>(dirCache);
		}

		return dirCache;
	}

	private static volatile Reference<DirCache> _dirCacheReference;

}