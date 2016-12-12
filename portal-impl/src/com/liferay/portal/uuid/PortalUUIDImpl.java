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

package com.liferay.portal.uuid;

import com.liferay.portal.kernel.security.SecureRandomUtil;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.uuid.PortalUUID;

import java.util.UUID;

/**
 * @author Brian Wing Shun Chan
 */
@DoPrivileged
public class PortalUUIDImpl implements PortalUUID {

	@Override
	public String fromJsSafeUuid(String jsSafeUuid) {
		return StringUtil.replace(
			jsSafeUuid, StringPool.DOUBLE_UNDERLINE, StringPool.DASH);
	}

	@Override
	public String generate() {
		long mostSigBits = SecureRandomUtil.nextLong();

		long leastSigBits = SecureRandomUtil.nextLong();

		char[] buffer = new char[16];

		StringBuilder sb = new StringBuilder(36);

		_appendDigits(sb, buffer, mostSigBits >> 32, 8);

		sb.append(CharPool.DASH);

		_appendDigits(sb, buffer, mostSigBits >> 16, 4);

		sb.append(StringPool.DASH);

		_appendDigits(sb, buffer, mostSigBits, 4);

		sb.append(StringPool.DASH);

		_appendDigits(sb, buffer, leastSigBits >> 48, 4);

		sb.append(StringPool.DASH);

		_appendDigits(sb, buffer, leastSigBits, 12);

		return sb.toString();
	}

	@Override
	public String generate(byte[] bytes) {
		return UUID.nameUUIDFromBytes(bytes).toString();
	}

	@Override
	public String toJsSafeUuid(String uuid) {
		return StringUtil.replace(
			uuid, CharPool.DASH, StringPool.DOUBLE_UNDERLINE);
	}

	private void _appendDigits(
		StringBuilder sb, char[] buffer, long val, int digits) {

		long high = 1L << (digits * 4);

		long hex = high | (val & (high - 1));

		int index = 16;

		do {
			buffer[--index] = _HEX_DIGITS[(int)(hex & 15)];

			hex >>>= 4;
		}
		while (hex != 0);

		index++;

		sb.append(buffer, index, 16 - index);
	}

	private static final char[] _HEX_DIGITS = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
		'e', 'f'
	};

}