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
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletContext;

import org.apache.felix.fileinstall.ArtifactUrlTransformer;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * @author Miguel Pastor
 * @author Raymond Augé
 */
public class WarArtifactUrlTransformer implements ArtifactUrlTransformer {

	public WarArtifactUrlTransformer(BundleContext bundleContext) {
		_bundleContext = bundleContext;
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
		StringBundler sb = new StringBundler(6);

		sb.append("(&(objectClass=");
		sb.append(ServletContext.class.getName());
		sb.append(
			")(bean.id=javax.servlet.ServletContext)(original.bean=true)");
		sb.append("(service.vendor=");
		sb.append(ReleaseInfo.getVendor());
		sb.append("))");

		try {
			ServiceReference<ServletContext>[] serviceReferences =
				(ServiceReference<ServletContext>[])
					_bundleContext.getServiceReferences(
						ServletContext.class.getName(), sb.toString());

			if ((serviceReferences == null) ||
				(serviceReferences.length == 0)) {

				return false;
			}

			for (ServiceReference<ServletContext> serviceReference :
					serviceReferences) {

				ServletContext servletContext = _bundleContext.getService(
					serviceReference);

				Object serverContainer = servletContext.getAttribute(
					"javax.websocket.server.ServerContainer");

				Boolean webSocketActive = (Boolean)serviceReference.getProperty(
					"websocket.active");

				if (((serverContainer == null) && (webSocketActive == null)) ||
					((serverContainer != null) && (webSocketActive != null) &&
					 webSocketActive)) {

					return true;
				}
			}
		}
		catch (InvalidSyntaxException ise) {
			_log.error(
				"Unable to check if it is ready to import resources", ise);
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		WarArtifactUrlTransformer.class);

	private final BundleContext _bundleContext;

}