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

/**
 * This interface is responsible for tracking the image scalers and return the
 * one that will be used for a specific mime type.
 *
 * <p>
 * Every image scaler registered in OSGi with the property
 * <code>mime.type</code> is tracked. Multiple image scalers can be registered
 * for the same mime type, but only one will be returned by the tracker.
 * </p>
 *
 * <p>
 * The image scalers are grouped by the mime.type and they are ordered based on
 * the <code>service.ranking</code> property. The image scaler will return the
 * first image scaler of the group whose method
 * {@link AMImageScaler#isEnabled()} returns true.
 * </p>
 *
 * <p>
 * If there is no enabled image scaler or there are not image scalers registered
 * for the specific mime type, it will search for the image scaler with higher
 * service ranking that is registered using <code>*</code> as mime type and
 * whose method {@link AMImageScaler#isEnabled()} returns true.
 * </p>
 *
 * <p>
 * If there are not enabled image scalers registered for the <code>*</code> mime
 * type it will return <code>null</code>.
 * </p>
 *
 * @review
 *
 * @author Sergio González
 */
public interface AMImageScalerTracker {

	/**
	 * Returns the image scaler enabled for the specific mime type.
	 *
	 * @param  mimeType the mime type of the image that needs to handle the
	 *         image scaler
	 * @return the image scaler
	 *
	 * @review
	 */
	public AMImageScaler getAMImageScaler(String mimeType);

}