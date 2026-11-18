/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.db.partition;

/**
 * @author Luis Ortiz
 */
public interface DBPartitionCopyContributor {

	public void copy(long fromCompanyId, long toCompanyId) throws Exception;

}