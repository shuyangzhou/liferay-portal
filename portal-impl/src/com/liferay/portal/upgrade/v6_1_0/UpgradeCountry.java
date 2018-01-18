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

package com.liferay.portal.upgrade.v6_1_0;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Kenneth Chang
 * @deprecated As of 7.0.0, with no direct replacement
 */
@Deprecated
public class UpgradeCountry extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		for (String name : _NAMES) {
			runSQL(
				"update Country set zipRequired = FALSE where name = '" + name +
					"'");
		}
	}

	private static final String[] _NAMES = {
		"Angola", "Antigua", "Aruba", "Bahamas", "Belize", "Benin", "Botswana",
		"Burkina Faso", "Burundi", "Central African Republic", "Comoros",
		"Republic of Congo", "Democratic Republic of Congo", "Cook Islands",
		"Djibouti", "Dominica", "Equatorial Guinea", "Eritrea", "Fiji Islands",
		"Gambia", "Ghana", "Grenada", "Guinea", "Guyana", "Ireland", "Kiribati",
		"North Korea", "Macau", "Malawi", "Mali", "Mauritania", "Mauritius",
		"Montserrat", "Nauru", "Niue", "Qatar", "Rwanda", "St. Kitts",
		"St. Lucia", "Sao Tome & Principe", "Seychelles", "Sierra Leone",
		"Solomon Islands", "Somalia", "Suriname", "Syria", "Tanzania", "Tonga",
		"Trinidad & Tobago", "Tuvalu", "Uganda", "United Arab Emirates",
		"Vanuatu", "Yemen", "Zimbabwe"
	};

}