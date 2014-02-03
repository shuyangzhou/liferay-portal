/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.servlet.filters.aggregate;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.tools.SassToCssBuilder;

/**
 * @author Minhchau Dang
 */
public class SassFileAggregateContext extends FileAggregateContext {

	public SassFileAggregateContext(String docrootPath, String resourcePath) {

		super(docrootPath, resourcePath);
	}

	@Override
	public String getContent(String path) {
		String parsedContentPath = SassToCssBuilder.getCacheFileName(
			path, StringPool.BLANK);

		// TODO: Find a better way to parallelize

		for (int i = 0; i < 10; i++) {
			String parsedContent = super.getContent(parsedContentPath);

			if (parsedContent != null) {
				return parsedContent;
			}

			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {

			}
		}

		String unparsedContent = super.getContent(path);

		if (unparsedContent != null) {
			_cacheMiss = true;
		}

		return unparsedContent;
	}

	public boolean hasCacheMiss() {
		return _cacheMiss;
	}

	private boolean _cacheMiss = false;

}
