/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.upgrade.release;

import com.liferay.portal.kernel.model.Release;

/**
 * @author Shuyang Zhou
 */
public interface ReleasePublisher {

	public void publish(Release release, boolean initialRelease);

	public void unpublish(Release release);

}