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

package com.liferay.portal.lpkg.deployer.internal;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.URLCodec;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.osgi.framework.Bundle;

/**
 * @author Eric Yan
 */
public class LPKGInnerWarBundleUtil {

	protected static String generateLPKGURL(
		Bundle lpkgBundle, String servletContextName,
		String portalProfileNames) {

		StringBundler sb = new StringBundler(10);

		sb.append("lpkg:/");
		sb.append(URLCodec.encodeURL(lpkgBundle.getSymbolicName()));
		sb.append(StringPool.DASH);
		sb.append(lpkgBundle.getVersion());
		sb.append(StringPool.SLASH);
		sb.append(servletContextName);
		sb.append(".war");

		if (Validator.isNotNull(portalProfileNames)) {
			sb.append(StringPool.QUESTION);
			sb.append("liferay-portal-profile-names=");
			sb.append(portalProfileNames);
		}

		return sb.toString();
	}

	protected static String[] readServletContextNameAndPortalProfileNames(
			URL warURL)
		throws IOException {

		String pathString = warURL.getPath();

		String servletContextName = pathString.substring(
			pathString.lastIndexOf('/') + 1, pathString.lastIndexOf(".war"));

		int index = servletContextName.lastIndexOf('-');

		if (index >= 0) {
			servletContextName = servletContextName.substring(0, index);
		}

		String portalProfileNames = null;

		Path tempFilePath = Files.createTempFile(null, null);

		try (InputStream inputStream1 = warURL.openStream()) {
			Files.copy(
				inputStream1, tempFilePath,
				StandardCopyOption.REPLACE_EXISTING);

			try (ZipFile zipFile = new ZipFile(tempFilePath.toFile());
				InputStream inputStream2 = zipFile.getInputStream(
					new ZipEntry(
						"WEB-INF/liferay-plugin-package.properties"))) {

				if (inputStream2 != null) {
					Properties properties = new Properties();

					properties.load(inputStream2);

					String configuredServletContextName =
						properties.getProperty("servlet-context-name");

					if (configuredServletContextName != null) {
						servletContextName = configuredServletContextName;
					}

					portalProfileNames = properties.getProperty(
						"liferay-portal-profile-names");
				}
			}
		}
		finally {
			Files.delete(tempFilePath);
		}

		return new String[] {servletContextName, portalProfileNames};
	}

}