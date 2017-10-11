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

package com.liferay.portal.mobile.device.detection.fiftyonedegrees.data.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Matthew Tambara
 */
@ExtendedObjectClassDefinition(category = "foundation")
@Meta.OCD(
	id = "com.liferay.portal.mobile.device.detection.fiftyonedegrees.configuration.FiftyOneDegreesDataConfiguration",
	localization = "content/Language",
	name = "51degrees-device-detection-data-configuration-name"
)
public interface FiftyOneDegreesDataConfiguration {

	@Meta.AD(deflt = "META-INF/51Degrees-LiteV3.2.dat", required = false)
	public String fiftyOneDegreesDataFileName();

}