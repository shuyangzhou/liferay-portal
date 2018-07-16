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

package com.liferay.document.library.opener.uad.test;

import com.liferay.document.library.opener.model.DLOpenerFileEntryReference;

import java.util.List;

import org.junit.Assume;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	immediate = true, service = DLOpenerFileEntryReferenceUADTestHelper.class
)
public class DLOpenerFileEntryReferenceUADTestHelper {

	public DLOpenerFileEntryReference addDLOpenerFileEntryReference(long userId)
		throws Exception {

		Assume.assumeTrue(false);

		return null;
	}

	public void cleanUpDependencies(
			List<DLOpenerFileEntryReference> dlOpenerFileEntryReferences)
		throws Exception {
	}

}