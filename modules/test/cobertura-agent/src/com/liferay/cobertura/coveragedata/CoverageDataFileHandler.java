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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import net.sourceforge.cobertura.coveragedata.HasBeenInstrumented;
import net.sourceforge.cobertura.util.ConfigurationUtil;

/**
 * @author Cristina González
 */
public abstract class CoverageDataFileHandler implements HasBeenInstrumented {

	public static File getDefaultDataFile() {
		if (defaultFile != null) {
			return defaultFile;
		}
		else {
			ConfigurationUtil configurationUtil = new ConfigurationUtil();

			defaultFile = new File(configurationUtil.getDatafile());

			return defaultFile;
		}
	}

	public static ProjectData loadCoverageData(File dataFile) {
		BufferedInputStream bufferedInputStream = null;

		try {
			bufferedInputStream = new BufferedInputStream(
				new FileInputStream(dataFile), 16384);

			ProjectData e = _loadCoverageData(bufferedInputStream);

			return e;
		}
		catch (IOException ioe) {
			throw new RuntimeException(
				"Cobertura: Error reading file " + dataFile.getAbsolutePath() +
					": " + ioe.getLocalizedMessage());
		}
		finally {
			if (bufferedInputStream != null) {
				try {
					bufferedInputStream.close();
				}
				catch (IOException ioe) {
					throw new RuntimeException(
						"Cobertura: Error closing file " +
							dataFile.getAbsolutePath() + ": " +
							ioe.getLocalizedMessage());
				}
			}
		}
	}

	public static void saveCoverageData(
		ProjectData projectData, File dataFile) {

		FileOutputStream fileOutputStream = null;

		try {
			File e = dataFile.getParentFile();

			if ((e != null) && !e.exists()) {
				e.mkdirs();
			}

			fileOutputStream = new FileOutputStream(dataFile);

			_saveCoverageData(projectData, fileOutputStream);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();

			throw new RuntimeException(
				"Cobertura: Error writing file " + dataFile.getAbsolutePath());
		}
		finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				}
				catch (IOException ioe) {
					ioe.printStackTrace();

					throw new RuntimeException(
						"Cobertura: Error closing file " +
							dataFile.getAbsolutePath());
				}
			}
		}
	}

	public CoverageDataFileHandler() {
	}

	private static ProjectData _loadCoverageData(InputStream dataFile)
		throws IOException {

		ObjectInputStream objectInputStream = null;

		try {
			objectInputStream = new ObjectInputStream(dataFile);

			return (ProjectData)objectInputStream.readObject();
		}
		catch (IOException ioe) {
			throw ioe;
		}
		catch (Exception e) {
			e.printStackTrace();

			throw new RuntimeException(
				"Cobertura: Error reading from object stream.");
		}
		finally {
			if (objectInputStream != null) {
				try {
					objectInputStream.close();
				}
				catch (IOException ioe) {
					ioe.printStackTrace();

					throw new RuntimeException(
						"Cobertura: Error closing object stream.");
				}
			}
		}
	}

	private static void _saveCoverageData(
		ProjectData projectData, OutputStream dataFile) {

		ObjectOutputStream objectOutputStream = null;

		try {
			objectOutputStream = new ObjectOutputStream(dataFile);
			objectOutputStream.writeObject(projectData);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();

			throw new RuntimeException(
				"Cobertura: Error writing to object stream.");
		}
		finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				}
				catch (IOException ioe) {
					ioe.printStackTrace();

					throw new RuntimeException(
						"Cobertura: Error closing object stream.");
				}
			}
		}
	}

	private static File defaultFile = null;

}