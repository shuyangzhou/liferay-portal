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

package com.liferay.portal.bootstrap;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.util.PropsValues;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

import org.eclipse.osgi.container.ModuleReadHook;

/**
 * @author Matthew Tambara
 */
public class ModuleReadHookImpl implements ModuleReadHook {

	@Override
	public void process(long bundleId, String location) {
		Path path = Paths.get(
			PropsValues.LIFERAY_HOME, "osgi/state/org.eclipse.osgi",
			String.valueOf(bundleId), "0/bundleFile");

		File file = path.toFile();

		if ((bundleId == 0) || location.startsWith("webbundle") ||
			file.exists()) {

			return;
		}

		try (InputStream inputStream = _getInputStream(location)) {
			if (inputStream == null) {
				if (_log.isWarnEnabled()) {
					_log.warn("Unable to find file " + location);
				}

				return;
			}

			try {
				Files.copy(inputStream, path);
			}
			catch (IOException ioe) {
				StringBundler sb = new StringBundler(4);

				sb.append("Unable to copy from ");
				sb.append(location);
				sb.append(" to ");
				sb.append(path.toString());

				_log.error(sb.toString(), ioe);
			}
		}
		catch (IOException ioe) {
			_log.error("Unable to get inputStream from " + location, ioe);
		}
	}

	private InputStream _getInputStream(String location) throws IOException {
		InputStream inputStream = null;

		if (location.startsWith("file")) {
			URL url = new URL(location);

			File file = new File(url.getPath());

			inputStream = new FileInputStream(file);
		}
		else {
			Matcher matcher = _pattern.matcher(location);

			if (matcher.matches()) {
				ZipFile zipFile = new ZipFile(matcher.group(2));

				inputStream = zipFile.getInputStream(
					zipFile.getEntry(matcher.group(1)));
			}
		}

		return inputStream;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ModuleReadHookImpl.class);

	private static final Pattern _pattern = Pattern.compile(
		"((?:.*)\\.jar).*lpkgPath=([^&]*)");

}