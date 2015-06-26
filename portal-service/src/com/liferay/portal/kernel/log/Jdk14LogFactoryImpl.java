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

package com.liferay.portal.kernel.log;

import com.liferay.portal.kernel.util.StringPool;

import java.io.IOException;
import java.io.InputStream;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Brian Wing Shun Chan
 */
public class Jdk14LogFactoryImpl implements LogFactory {

	public Jdk14LogFactoryImpl() {
		if (System.getProperty("java.util.logging.config.file") != null) {
			return;
		}

		try {
			Class<?> clazz = getClass();

			InputStream inputStream = clazz.getResourceAsStream(
				"/logging.properties");

			if (inputStream != null) {
				LogManager logManager = LogManager.getLogManager();

				logManager.readConfiguration(inputStream);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Logger logger = Logger.getLogger(StringPool.BLANK);

		try {
			FileHandler xmlFileHandler = new FileHandler(
				System.getProperty("user.dir") + "/unit-test-jdk-logs.xml",
				true);

			logger.addHandler(xmlFileHandler);

			FileHandler simpleFileHandler = new FileHandler(
				System.getProperty("user.dir") + "/unit-test-jdk-logs.log",
				true);

			simpleFileHandler.setFormatter(new SimpleFormatter());

			logger.addHandler(simpleFileHandler);
		}
		catch (IOException ioe) {
			logger.log(Level.SEVERE, "FileHandler addition failed", ioe);
		}
	}

	@Override
	public Log getLog(Class<?> c) {
		return getLog(c.getName());
	}

	@Override
	public Log getLog(String name) {
		return new Jdk14LogImpl(Logger.getLogger(name));
	}

	@Override
	public void setLevel(String name, String priority, boolean custom) {
	}

}