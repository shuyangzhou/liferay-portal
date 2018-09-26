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

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.URLCodec;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.osgi.framework.Bundle;

/**
 * @author Matthew Tambara
 */
public class LPKGInnerBundleLocationUtil {

	public static String generateInnerBundleLocation(
		Bundle lpkgBundle, String path) {

		String location = path.concat("?lpkgPath=");

		return location.concat(lpkgBundle.getLocation());
	}

	public static URL generateInnerBundleURL(File lpkgBundleFile, String path)
		throws MalformedURLException {

		StringBundler sb = new StringBundler(4);

		sb.append("jar:");

		URI uri = lpkgBundleFile.toURI();

		sb.append(URLCodec.decodeURL(uri.toString()));

		sb.append("!/");
		sb.append(path);

		return new URL(sb.toString());
	}

}