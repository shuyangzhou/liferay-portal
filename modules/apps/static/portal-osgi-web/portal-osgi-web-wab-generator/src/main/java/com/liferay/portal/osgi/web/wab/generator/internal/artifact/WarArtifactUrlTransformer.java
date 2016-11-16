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

package com.liferay.portal.osgi.web.wab.generator.internal.artifact;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.osgi.web.wab.generator.internal.WabGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletContext;

import org.apache.felix.fileinstall.ArtifactUrlTransformer;

/**
 * @author Miguel Pastor
 * @author Raymond Augé
 */
public class WarArtifactUrlTransformer implements ArtifactUrlTransformer {

	public WarArtifactUrlTransformer(WabGenerator wabGenerator) {
		_wabGenerator = wabGenerator;
	}

	@Override
	public boolean canHandle(File artifact) {
		String name = artifact.getName();

		if (!name.endsWith(".war")) {
			return false;
		}

		if (_hasResources(artifact)) {
			return _isReadyForImport();
		}

		return true;
	}

	@Override
	public URL transform(URL artifact) throws Exception {
		return ArtifactURLUtil.transform(artifact);
	}

	private boolean _hasResources(File artifact) {
		try (ZipFile zipFile = new ZipFile(artifact)) {
			ZipEntry resourceDir = zipFile.getEntry(
				"WEB-INF/classes/resources-importer/");
			ZipEntry templateDir = zipFile.getEntry(
				"WEB-INF/classes/templates-importer/");

			if ((resourceDir != null) || (templateDir != null)) {
				return true;
			}

			ZipEntry pluginProperties = zipFile.getEntry(
				"WEB-INF/liferay-plugin-package.properties");

			if (pluginProperties == null) {
				return false;
			}

			try (InputStream inputStream = zipFile.getInputStream(
					pluginProperties)) {

				Properties properties = new Properties();

				properties.load(inputStream);

				String resourcesDir = properties.getProperty(
					"resources-importer-external-dir");

				return Validator.isNotNull(resourcesDir);
			}
		}
		catch (IOException ioe) {
			_log.error("Unable to check resources in " + artifact, ioe);
		}

		return false;
	}

	private boolean _isReadyForImport() {
		ServletContext servletContext = _wabGenerator.getServletContext();

		if (servletContext == null) {
			return false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		WarArtifactUrlTransformer.class);

	private final WabGenerator _wabGenerator;

}