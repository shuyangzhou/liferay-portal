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

package com.liferay.ant.process.ivy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * @author William Newbury
 * @author Shuyang Zhou
 */
public class IvyUpToDateTask extends Task {

	@Override
	public void execute() throws BuildException {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

		String name = runtimeMXBean.getName();

		Project currentProject = getProject();

		String sdkdir = currentProject.getProperty("sdk.dir");

		File sdkProcessIvyRecord = new File(sdkdir + "/sdk.process.ivy.record");

		if (sdkProcessIvyRecord.exists()) {
			try (BufferedReader reader = new BufferedReader(
					new FileReader(sdkProcessIvyRecord))) {

				if (name.equals(reader.readLine())) {
					currentProject.setProperty("sdk-ivy-processed", "true");

					return;
				}
			}
			catch (IOException ioe) {
			}
		}

		try (PrintWriter writer = new PrintWriter(sdkProcessIvyRecord)) {
			writer.print(name);
		}
		catch (FileNotFoundException fnfe) {
		}

		currentProject.setProperty("sdk-ivy-processed", null);
	}

}