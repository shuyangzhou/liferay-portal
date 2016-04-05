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

package com.liferay.portal.kernel.util;

/**
 * @author Preston Crary
 */
public class JDK6StringWrapper implements CharSequence {

	public JDK6StringWrapper(String parentString) {
		_length = parentString.length();
		_offset = 0;
		_parentString = parentString;
	}

	@Override
	public char charAt(int index) {
		return _parentString.charAt(index + _offset);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof CharSequence)) {
			return false;
		}

		CharSequence charSequence = (CharSequence)object;

		if (_length != charSequence.length()) {
			return false;
		}

		for (int i = 0; i < _length; i++) {
			if (charAt(i) != charSequence.charAt(i)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		if ((_hash == 0) && (0 < length())) {
			int hash = 0;

			for (int i = 0; i < length(); i++) {
				hash = HashUtil.hash(hash, charAt(i));
			}

			_hash = hash;
		}

		return _hash;
	}

	public int indexOf(int ch) {
		return indexOf(ch, 0);
	}

	public int indexOf(int ch, int fromIndex) {
		final int max = length();

		if (fromIndex < 0) {
			fromIndex = 0;
		}
		else if (fromIndex >= max) {
			return -1;
		}

		for (int i = fromIndex; i < max; i++) {
			if (charAt(i) == ch) {
				return i;
			}
		}

		return -1;
	}

	public int indexOf(String str) {
		return indexOf(str, 0);
	}

	public int indexOf(String str, int fromIndex) {
		return _indexOf(
			this, _offset, length(), str, 0, str.length(), fromIndex);
	}

	public int lastIndexOf(int ch) {
		return lastIndexOf(ch, length() - 1);
	}

	public int lastIndexOf(int ch, int fromIndex) {
		int i = Math.min(fromIndex, length() - 1);

		for (; i >= 0; i--) {
			if (charAt(i) == ch) {
				return i;
			}
		}

		return -1;
	}

	public int lastIndexOf(CharSequence str) {
		return lastIndexOf(str, length());
	}

	public int lastIndexOf(CharSequence str, int fromIndex) {
		return _lastIndexOf(
			this, _offset, length(), str, _offset, str.length(), fromIndex);
	}

	@Override
	public int length() {
		return _length;
	}

	public JDK6StringWrapper subSequence(int beginIndex) {
		if (beginIndex < 0) {
			throw new StringIndexOutOfBoundsException(beginIndex);
		}

		int subLen = length() - beginIndex;

		if (subLen < 0) {
			throw new StringIndexOutOfBoundsException(subLen);
		}

		if (beginIndex == 0) {
			return this;
		}

		return new JDK6StringWrapper(beginIndex + _offset, subLen, _parentString);
	}

	public JDK6StringWrapper trim() {
		int len = length();
		int st = 0;

		while ((st < len) && (charAt(st) <= ' ')) {
			st++;
		}

		while ((st < len) && (charAt(len - 1) <= ' ')) {
			len--;
		}

		if ((st > 0) || (len < length())) {
			return subSequence(st, len);
		}

		return this;
	}

	@Override
	public JDK6StringWrapper subSequence(int beginIndex, int endIndex) {
		if (beginIndex < 0) {
			throw new StringIndexOutOfBoundsException(beginIndex);
		}

		if (length() < endIndex) {
			throw new StringIndexOutOfBoundsException(endIndex);
		}

		int subLen = endIndex - beginIndex;

		if (subLen < 0) {
			throw new StringIndexOutOfBoundsException(subLen);
		}

		if ((beginIndex == 0) && (endIndex == length())) {
			return this;
		}

		return new JDK6StringWrapper(beginIndex + _offset, subLen, _parentString);
	}

	@Override
	public String toString() {
		return _parentString.substring(_offset, _length);
	}

	private static int _indexOf(
		CharSequence source, int sourceOffset, int sourceCount,
		CharSequence target, int targetOffset, int targetCount, int fromIndex) {

		if (fromIndex >= sourceCount) {
			return (targetCount == 0 ? sourceCount : -1);
		}

		if (fromIndex < 0) {
			fromIndex = 0;
		}

		if (targetCount == 0) {
			return fromIndex;
		}

		char first = target.charAt(targetOffset);
		int max = sourceOffset + (sourceCount - targetCount);

		for (int i = sourceOffset + fromIndex; i <= max; i++) {
			if (source.charAt(i) != first) {
				while (++i <= max && source.charAt(i) != first);
			}

			if (i <= max) {
				int j = i + 1;
				int end = j + targetCount - 1;

				for (int k = targetOffset + 1; (j < end) &&
						(source.charAt(j) == target.charAt(k)); j++, k++);

				if (j == end) {
					return i - sourceOffset;
				}
			}
		}

		return -1;
	}

	private static int _lastIndexOf(
		CharSequence source, int sourceOffset, int sourceCount,
		CharSequence target, int targetOffset, int targetCount, int fromIndex) {

		int rightIndex = sourceCount - targetCount;

		if (fromIndex < 0) {
			return -1;
		}

		if (fromIndex > rightIndex) {
			fromIndex = rightIndex;
		}

		if (targetCount == 0) {
			return fromIndex;
		}

		int strLastIndex = targetOffset + targetCount - 1;
		char strLastChar = target.charAt(strLastIndex);
		int min = sourceOffset + targetCount - 1;
		int i = min + fromIndex;

		startSearchForLastChar:
		while (true) {
			while ((i >= min) && (source.charAt(i) != strLastChar)) {
				i--;
			}

			if (i < min) {
				return -1;
			}

			int j = i - 1;
			int start = j - (targetCount - 1);
			int k = strLastIndex - 1;

			while (j > start) {
				if (source.charAt(j--) != target.charAt(k--)) {
					i--;

					continue startSearchForLastChar;
				}
			}

			return start - sourceOffset + 1;
		}
	}

	private JDK6StringWrapper(int length, int offset, String parentString) {
		_length = length;
		_offset = offset;
		_parentString = parentString;
	}

	private int _hash;
	private final int _length;
	private final int _offset;
	private final String _parentString;

}