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

package com.liferay.adaptive.media.image.scaler;

import com.liferay.adaptive.media.image.configuration.AMImageConfigurationEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;

/**
 * Provides an interface that scales an image to fit the characteristics
 * specified by the image configuration entry.
 *
 * <p>
 * This interface should be implemented to allow Adaptive Media to work with new
 * type of images that are supported by the out of the box image scalers or to
 * replace one of the existing image scalers by a custom one.
 * </p>
 *
 * <p>
 * Implementations of this class need to be registed as an OSGi component. The
 * property <code>mime.type</code> defines the mime type or mime types that the
 * image scaler can handle. The special mime type <code>*</code> will be used as
 * a fallback in case there is no image scaler for a specific mime type.
 * </p>
 *
 * <p>
 * Image scalers can be replaced by registering an OSGi component with a higher
 * <code>service.ranking</code> property.
 * </p>
 *
 * @review
 *
 * @author Sergio González
 */
public interface AMImageScaler {

	/**
	 * Returns whether the image scaler is enabled to scale images.
	 *
	 * @return <code>true</code> if the image scaler is enabled to scale images
	 *
	 * @review
	 */
	public default boolean isEnabled() {
		return true;
	}

	/**
	 * Generates an scaled image for the file version that fits the
	 * characteristics specified by the image configuration entry.
	 *
	 * @param fileVersion the image file version that will be scaled
	 * @param amImageConfigurationEntry the image configuration entry
	 *
	 * @return the scaled image
	 *
	 * @review
	 */
	public AMImageScaledImage scaleImage(
		FileVersion fileVersion,
		AMImageConfigurationEntry amImageConfigurationEntry);

}