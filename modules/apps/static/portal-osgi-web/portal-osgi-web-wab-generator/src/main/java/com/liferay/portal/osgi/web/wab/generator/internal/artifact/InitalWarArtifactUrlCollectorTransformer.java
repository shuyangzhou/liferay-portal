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

import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.osgi.web.wab.generator.internal.util.ManifestUtil;

import java.io.File;
import java.io.InputStream;

import java.net.URI;
import java.net.URL;

import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.felix.fileinstall.ArtifactUrlTransformer;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

/**
 * @author Matthew Tambara
 */
@Component(immediate = true, service = ArtifactUrlTransformer.class)
public class InitalWarArtifactUrlCollectorTransformer
	implements ArtifactUrlTransformer {

	public static Set<String> getLocations() {
		return Collections.unmodifiableSet(_locations);
	}

	@Override
	public boolean canHandle(File artifact) {
		String name = artifact.getName();

		if (name.endsWith(".war")) {
			try (ZipFile zipFile = new ZipFile(artifact);
				InputStream inputStream = zipFile.getInputStream(
					new ZipEntry(
						"WEB-INF/liferay-plugin-package.properties"))) {

				Properties properties = new Properties();

				properties.load(inputStream);

				if (!Boolean.valueOf(
						properties.getProperty("startup-required"))) {

					return false;
				}

				URI uri = artifact.toURI();

				URL url = uri.toURL();

				String path = url.getPath();

				int x = path.lastIndexOf('/');
				int y = path.lastIndexOf(".war");

				String symbolicName = path.substring(x + 1, y);

				Matcher matcher = _pattern.matcher(symbolicName);

				if (matcher.matches()) {
					symbolicName = matcher.group(1);
				}

				if (ManifestUtil.isValidOSGiBundle(url.getPath())) {
					_locations.add(url.toString());
				}

				String contextName = properties.getProperty(
					"servlet-context-name");

				if (Validator.isNull(contextName)) {
					contextName = symbolicName;
				}

				StringBundler sb = new StringBundler(7);

				sb.append(url.getPath());
				sb.append("?");
				sb.append(Constants.BUNDLE_SYMBOLICNAME);
				sb.append("=");
				sb.append(symbolicName);
				sb.append("&Web-ContextPath=/");
				sb.append(contextName);

				url = new URL("file", null, sb.toString());

				_locations.add(url.toString());
			}
			catch (Exception e) {
				ReflectionUtil.throwException(e);
			}
		}

		return false;
	}

	@Override
	public URL transform(URL url) throws Exception {
		throw new IllegalStateException(
			"This transformer should not handle anything");
	}

	private static final Set<String> _locations = new HashSet<>();
	private static final Pattern _pattern = Pattern.compile(
		"(.*?)(-\\d+\\.\\d+\\.\\d+\\.\\d+)?");

}