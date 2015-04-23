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

package com.liferay.cobertura.coveragedata;

import java.io.File;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.cobertura.coveragedata.ClassData;
import net.sourceforge.cobertura.coveragedata.CoverageData;
import net.sourceforge.cobertura.coveragedata.HasBeenInstrumented;
import net.sourceforge.cobertura.coveragedata.LineData;
import net.sourceforge.cobertura.coveragedata.PackageData;
import net.sourceforge.cobertura.coveragedata.SaveTimer;
import net.sourceforge.cobertura.coveragedata.SourceFileData;
import net.sourceforge.cobertura.util.FileLocker;

/**
 * @author Cristina González
 */
public class ProjectData extends CoverageDataContainer
	implements HasBeenInstrumented {

	public static void initialize() {
		if (System.getProperty("catalina.home") != null) {
			saveGlobalProjectData();

			ClassData.class.toString();
			CoverageData.class.toString();
			CoverageDataContainer.class.toString();
			FileLocker.class.toString();
			HasBeenInstrumented.class.toString();
			LineData.class.toString();
			PackageData.class.toString();
			SourceFileData.class.toString();
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new SaveTimer()));
	}

	public static void saveGlobalProjectData() {
		ProjectData projectDataToSave = new ProjectData();

		TouchCollector.applyTouchesOnProjectData(projectDataToSave);

		File dataFile = CoverageDataFileHandler.getDefaultDataFile();

		synchronized (dataFile.getPath().intern() ) {
			FileLocker fileLocker = new FileLocker(dataFile);

			try {

				// Read the old data, merge our current data into it, then
				// write a new ser file.

				if (fileLocker.lock()) {
					ProjectData datafileProjectData =
						loadCoverageDataFromDatafile(dataFile);

					if (datafileProjectData == null) {
						datafileProjectData = projectDataToSave;
					}
					else {
						datafileProjectData.merge(projectDataToSave);
					}

					CoverageDataFileHandler.saveCoverageData(
						datafileProjectData, dataFile);
				}
			}
			finally {

				// Release the file lock

				fileLocker.release();
			}
		}
	}

	public void addClassData(ClassData classData) {
		getLock().lock();

		try {
			String packageName = classData.getPackageName();

			PackageData packageData =
				(PackageData)getChildren().get(packageName);

			if (packageData == null) {
				packageData = new PackageData(packageName);

				getChildren().put(packageName, packageData);
			}

			packageData.addClassData(classData);

			_classes.put(classData.getName(), classData);
		}
		finally {
			getLock().unlock();
		}
	}

	public ClassData getClassData(String name) {
		getLock().lock();

		try {
			return (ClassData)_classes.get(name);
		}
		finally {
			getLock().unlock();
		}
	}

	public Collection getClasses() {
		getLock().lock();

		try {
			return _classes.values();
		}
		finally {
			getLock().unlock();
		}
	}

	public int getNumberOfClasses() {
		getLock().lock();

		try {
			return _classes.size();
		}
		finally {
			getLock().unlock();
		}
	}

	public int getNumberOfSourceFiles() {
		return getSourceFiles().size();
	}

	public ClassData getOrCreateClassData(String name) {
		getLock().lock();

		try {
			ClassData classData = (ClassData)_classes.get(name);

			if (classData == null) {
				classData = new ClassData(name);

				addClassData(classData);
			}

			return classData;
		}
		finally {
			getLock().unlock();
		}
	}

	public SortedSet getPackages() {
		getLock().lock();

		try {
			return new TreeSet(getChildren().values());
		}
		finally {
			getLock().unlock();
		}
	}

	public Collection getSourceFiles() {
		SortedSet sourceFileDatas = new TreeSet();

		getLock().lock();

		try {
			for (CoverageData coverageData : getChildren().values()) {
				sourceFileDatas.addAll(
					((PackageData)coverageData).getSourceFiles());
			}
		}
		finally {
			getLock().unlock();
		}

		return sourceFileDatas;
	}

	public SortedSet getSubPackages(String packageName) {
		SortedSet subPackages = new TreeSet();

		getLock().lock();

		try {
			for (CoverageData coverageData : getChildren().values()) {
				PackageData packageData = (PackageData)coverageData;

				if (packageData.getName().startsWith(packageName + ".") ||
					packageData.getName().equals(packageName) ||
					packageName.equals("")) {

					subPackages.add(packageData);
				}
			}
		}
		finally {
			getLock().unlock();
		}

		return subPackages;
	}

	public void merge(CoverageData coverageData) {
		if (coverageData == null) {
			return;
		}

		ProjectData projectData = (ProjectData)coverageData;

		getBothLocks(projectData);

		try {
			super.merge(coverageData);

			for (Object key : projectData._classes.keySet()) {
				if (!_classes.containsKey(key)) {
					_classes.put(key, projectData._classes.get(key));
				}
			}
		}
		finally {
			getLock().unlock();
			projectData.getLock().unlock();
		}
	}

	private static ProjectData loadCoverageDataFromDatafile(File dataFile) {
		ProjectData projectData = null;

		// Read projectData from the serialized file.

		if (dataFile.isFile()) {
			projectData = CoverageDataFileHandler.loadCoverageData(dataFile);
		}

		return projectData;
	}

	private static final long serialVersionUID = 6;

	private final Map _classes = new HashMap();

}