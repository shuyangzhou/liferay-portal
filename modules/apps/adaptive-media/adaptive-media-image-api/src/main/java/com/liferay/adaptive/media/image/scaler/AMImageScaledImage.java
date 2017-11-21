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

import java.io.InputStream;

/**
 * Represents an image scaled by Adaptive Media.
 *
 * @review
 *
 * @author Sergio González
 */
public interface AMImageScaledImage {

	/**
	 * Returns the image height.
	 *
	 * @return the image height
	 *
	 * @review
	 */
	public int getHeight();

	/**
	 * Returns an InputStream with the image data.
	 *
	 * @return an InputStream with the image data
	 *
	 * @review
	 */
	public InputStream getInputStream();

	/**
	 * Returns the size in bytes of this image.
	 *
	 * @return the size in bytes of this image
	 *
	 * @review
	 */
	public long getSize();

	/**
	 * Returns the image width.
	 *
	 * @return the image width
	 *
	 * @review
	 */
	public int getWidth();

}