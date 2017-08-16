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
import com.liferay.portal.kernel.util.StringPool;

import org.osgi.framework.Bundle;

/**
 * @author Matthew Tambara
 */
public class LPKGUtil {

	public static String generateBundleLocation(
		Bundle bundle, String fileType, String innerBundle,
		String lpkgDeployerDir) {

		StringBundler sb = new StringBundler(7);

		sb.append(fileType);
		sb.append(":file:");
		sb.append(lpkgDeployerDir);
		sb.append(StringPool.SLASH);
		sb.append(bundle.getSymbolicName());
		sb.append(".lpkg!");
		sb.append(innerBundle);

		return sb.toString();
	}

}