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

import java.io.File;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;

import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.eclipse.jgit.util.FS;

/**
 * @author Shuyang Zhou
 */
public class GitHasChangedSinceTask extends Task implements Condition {

	@Override
	public boolean eval() throws BuildException {
		if (_path == null) {
			throw new BuildException(
				"Path attribute is required", getLocation());
		}

		if (_since == null) {
			throw new BuildException(
				"Since attribute is required", getLocation());
		}

		File gitDir = PathUtil.getGitDir(_gitDir, getProject(), getLocation());

		String relativePath = PathUtil.toRelativePath(gitDir, _path);

		String cacheKey = relativePath.concat("#").concat(_since);

		if (_useCache) {
			Boolean changedSince = _changedSinceFlags.get(cacheKey);

			if (changedSince != null) {
				Project currentProject = getProject();

				currentProject.setNewProperty(
					_property, changedSince.toString());

				return changedSince;
			}
		}

		try (Repository repository = RepositoryCache.open(
				RepositoryCache.FileKey.exact(gitDir, FS.DETECTED))) {

			RevWalk revWalk = new RevWalk(repository);

			revWalk.setRetainBody(false);

			revWalk.markStart(
				revWalk.parseCommit(repository.resolve(Constants.HEAD)));
			revWalk.markUninteresting(
				revWalk.parseCommit(repository.resolve(_since)));

			revWalk.setTreeFilter(
				AndTreeFilter.create(
					PathFilter.create(relativePath), TreeFilter.ANY_DIFF
				));

			RevCommit revCommit = null;

			while ((revCommit = revWalk.next()) != null) {
				if (!hasIgnoreFile(repository, revCommit, relativePath)) {
					if (_useCache) {
						_changedSinceFlags.put(cacheKey, Boolean.TRUE);
					}

					return true;
				}
			}

			revWalk.dispose();

			if (_useCache) {
				_changedSinceFlags.put(cacheKey, Boolean.FALSE);
			}

			return false;
		}
		catch (Exception e) {
			throw new BuildException(
				"Unable to get head hash for path " + _path, e);
		}
	}

	@Override
	public void execute() throws BuildException {
		if (_property == null) {
			throw new BuildException(
				"Property attribute is required", getLocation());
		}

		if (eval()) {
			Project currentProject = getProject();

			if (_value == null) {
				currentProject.setNewProperty(_property, "true");
			}
			else {
				currentProject.setNewProperty(_property, _value);
			}
		}
	}

	public void setGitDir(File gitDir) {
		_gitDir = gitDir;
	}

	public void setIgnoreFileName(String ignoreFileName) {
		_ignoreFileName = ignoreFileName;
	}

	public void setPath(String path) {
		_path = path;
	}

	public void setProperty(String property) {
		_property = property;
	}

	public void setSince(String since) {
		_since = since;
	}

	public void setUseCache(boolean useCache) {
		_useCache = useCache;
	}

	public void setValue(String value) {
		_value = value;
	}

	protected boolean hasIgnoreFile(
			Repository repository, RevCommit revCommit, String relativePath)
		throws Exception {

		if (_ignoreFileName == null) {
			return false;
		}

		try (TreeWalk treeWalk = new TreeWalk(repository)) {
			treeWalk.addTree(revCommit.getTree());
			treeWalk.setRecursive(true);

			treeWalk.setFilter(
				AndTreeFilter.create(
					PathFilter.create(relativePath + "/" + _ignoreFileName),
					TreeFilter.ANY_DIFF));

			return treeWalk.next();
		}
	}

	private static final Map<String, Boolean> _changedSinceFlags =
		new ConcurrentHashMap<>();

	private File _gitDir;
	private String _ignoreFileName;
	private String _path;
	private String _property;
	private String _since;
	private boolean _useCache = true;
	private String _value;

}