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

	private static File defaultFile = null;

	public CoverageDataFileHandler() {
	}

	public static File getDefaultDataFile() {
		if(defaultFile != null) {
			return defaultFile;
		}
		else {
			ConfigurationUtil config = new ConfigurationUtil();

			defaultFile = new File(config.getDatafile());

			return defaultFile;
		}
	}

	public static ProjectData loadCoverageData(File dataFile) {
		BufferedInputStream is = null;

		Object e1;

		try {
			is = new BufferedInputStream(new FileInputStream(dataFile), 16384);

			ProjectData e = loadCoverageData((InputStream)is);

			return e;
		}
		catch (IOException var13) {
			System.err.println("Cobertura: Error reading file " + dataFile.getAbsolutePath() + ": " + var13.getLocalizedMessage());

			e1 = null;
		}
		finally {
			if(is != null) {
				try {
					is.close();
				}
				catch (IOException var12) {
					System.err.println("Cobertura: Error closing file " + dataFile.getAbsolutePath() + ": " + var12.getLocalizedMessage());
				}
			}

		}

		return (ProjectData)e1;
	}

	private static ProjectData loadCoverageData(InputStream dataFile) throws IOException {
		ObjectInputStream objects = null;

		ProjectData var3;

		try {
			objects = new ObjectInputStream(dataFile);

			ProjectData e = (ProjectData)objects.readObject();

			System.out.println("Cobertura: Loaded information on " + e.getNumberOfClasses() + " classes.");

			var3 = e;

			return var3;
		}
		catch (IOException var14) {
			throw var14;
		}
		catch (Exception var15) {
			System.err.println("Cobertura: Error reading from object stream.");

			var15.printStackTrace();

			var3 = null;
		}
		finally {
			if(objects != null) {
				try {
					objects.close();
				}
				catch (IOException var13) {
					System.err.println("Cobertura: Error closing object stream.");

					var13.printStackTrace();
				}
			}

		}

		return var3;
	}

	public static void saveCoverageData(ProjectData projectData, File dataFile) {
		FileOutputStream os = null;

		try {
			File e = dataFile.getParentFile();

			if(e != null && !e.exists()) {
				e.mkdirs();
			}

			os = new FileOutputStream(dataFile);

			saveCoverageData(projectData, (OutputStream) os);
		}
		catch (IOException var12) {
			System.err.println("Cobertura: Error writing file " + dataFile.getAbsolutePath());

			var12.printStackTrace();
		}
		finally {
			if(os != null) {
				try {
					os.close();
				}
				catch (IOException var11) {
					System.err.println("Cobertura: Error closing file " + dataFile.getAbsolutePath());

					var11.printStackTrace();
				}
			}

		}

	}

	private static void saveCoverageData(ProjectData projectData, OutputStream dataFile) {
		ObjectOutputStream objects = null;

		try {
			objects = new ObjectOutputStream(dataFile);
			objects.writeObject(projectData);
			System.out.println("Cobertura: Saved information on " + projectData.getNumberOfClasses() + " classes.");
		}
		catch (IOException var12) {
			System.err.println("Cobertura: Error writing to object stream.");

			var12.printStackTrace();
		}
		finally {
			if(objects != null) {
				try {
					objects.close();
				}
				catch (IOException var11) {
					System.err.println("Cobertura: Error closing object stream.");

					var11.printStackTrace();
				}
			}

		}

	}
}