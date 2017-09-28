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

package com.liferay.petra.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * The String utility class.
 *
 * @author Brian Wing Shun Chan
 * @author Sandeep Soni
 * @author Ganesh Ram
 * @author Shuyang Zhou
 * @author Hugo Huijser
 */
public class StringUtil {

	/**
	 * Merges the elements of the boolean array into a string representing a
	 * comma delimited list of its values.
	 *
	 * @param  array the boolean values to merge
	 * @return a string representing a comma delimited list of the values of the
	 *         boolean array, an empty string if the array is empty, or
	 *         <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(boolean[] array) {
		return merge(array, StringPool.COMMA);
	}

	/**
	 * Merges the elements of the boolean array into a string representing a
	 * delimited list of its values.
	 *
	 * @param  array the boolean values to merge
	 * @param  delimiter the delimiter
	 * @return a string representing a comma delimited list of the values of the
	 *         boolean array, an empty string if the array is empty, or
	 *         <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(boolean[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		if (array.length == 0) {
			return StringPool.BLANK;
		}

		if (array.length == 1) {
			return String.valueOf(array[0]);
		}

		StringBundler sb = new StringBundler(2 * array.length - 1);

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				sb.append(delimiter);
			}

			sb.append(String.valueOf(array[i]));
		}

		return sb.toString();
	}

	/**
	 * Merges the elements of the character array into a string representing a
	 * comma delimited list of its values.
	 *
	 * @param  array the characters to merge
	 * @return a string representing a comma delimited list of the values of the
	 *         character array, an empty string if the array is empty, or
	 *         <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(char[] array) {
		return merge(array, StringPool.COMMA);
	}

	/**
	 * Merges the elements of the character array into a string representing a
	 * delimited list of its values.
	 *
	 * @param  array the characters to merge
	 * @param  delimiter the delimiter
	 * @return a string representing a delimited list of the values of the
	 *         character array, an empty string if the array is empty, or
	 *         <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(char[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		if (array.length == 0) {
			return StringPool.BLANK;
		}

		if (array.length == 1) {
			return String.valueOf(array[0]);
		}

		StringBundler sb = new StringBundler(2 * array.length - 1);

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				sb.append(delimiter);
			}

			sb.append(String.valueOf(array[i]));
		}

		return sb.toString();
	}

	/**
	 * Merges the elements of the collection by returning a string representing
	 * a comma delimited list of its values.
	 *
	 * @param  col the collection of objects
	 * @return the merged collection elements, or <code>null</code> if the
	 *         collection is <code>null</code>
	 */
	public static String merge(Collection<?> col) {
		return merge(col, StringPool.COMMA);
	}

	/**
	 * Merges the elements of the collection by returning a string representing
	 * a delimited list of its values.
	 *
	 * @param  col the collection of objects
	 * @param  delimiter the string whose last index in the string marks where
	 *         to begin the substring
	 * @return the merged collection elements, or <code>null</code> if the
	 *         collection is <code>null</code>
	 */
	public static String merge(Collection<?> col, String delimiter) {
		if (col == null) {
			return null;
		}

		if (col.isEmpty()) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(2 * col.size());

		for (Object object : col) {
			String objectString = String.valueOf(object);

			sb.append(objectString.trim());

			sb.append(delimiter);
		}

		sb.setIndex(sb.index() - 1);

		return sb.toString();
	}

	/**
	 * Merges the elements of an array of double-precision decimal numbers by
	 * returning a string representing a comma delimited list of its values.
	 *
	 * @param  array the doubles to merge
	 * @return a string representing a comma delimited list of the values of the
	 *         array of double-precision decimal numbers, an empty string if the
	 *         array is empty, or <code>null</code> if the array is
	 *         <code>null</code>
	 */
	public static String merge(double[] array) {
		return merge(array, StringPool.COMMA);
	}

	/**
	 * Merges the elements of an array of double-precision decimal numbers by
	 * returning a string representing a delimited list of its values.
	 *
	 * @param  array the doubles to merge
	 * @param  delimiter the delimiter
	 * @return a string representing a delimited list of the values of the array
	 *         of double-precision decimal numbers, an empty string if the array
	 *         is empty, or <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(double[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		if (array.length == 0) {
			return StringPool.BLANK;
		}

		if (array.length == 1) {
			return String.valueOf(array[0]);
		}

		StringBundler sb = new StringBundler(2 * array.length - 1);

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				sb.append(delimiter);
			}

			sb.append(String.valueOf(array[i]));
		}

		return sb.toString();
	}

	/**
	 * Merges the elements of an array of decimal numbers into a string
	 * representing a comma delimited list of its values.
	 *
	 * @param  array the floats to merge
	 * @return a string representing a comma delimited list of the values of the
	 *         array of decimal numbers, an empty string if the array is empty,
	 *         or <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(float[] array) {
		return merge(array, StringPool.COMMA);
	}

	/**
	 * Merges the elements of an array of decimal numbers into a string
	 * representing a delimited list of its values.
	 *
	 * @param  array the floats to merge
	 * @param  delimiter the delimiter
	 * @return a string representing a delimited list of the values of the array
	 *         of decimal numbers, an empty string if the array is empty, or
	 *         <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(float[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		if (array.length == 0) {
			return StringPool.BLANK;
		}

		if (array.length == 1) {
			return String.valueOf(array[0]);
		}

		StringBundler sb = new StringBundler(2 * array.length - 1);

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				sb.append(delimiter);
			}

			sb.append(String.valueOf(array[i]));
		}

		return sb.toString();
	}

	/**
	 * Merges the elements of an array of integers into a string representing a
	 * comma delimited list of its values.
	 *
	 * @param  array the integers to merge
	 * @return a string representing a comma delimited list of the values of the
	 *         array of integers, an empty string if the array is empty, or
	 *         <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(int[] array) {
		return merge(array, StringPool.COMMA);
	}

	/**
	 * Merges the elements of an array of integers into a string representing a
	 * delimited list of its values.
	 *
	 * @param  array the integers to merge
	 * @param  delimiter the delimiter
	 * @return a string representing a delimited list of the values of the array
	 *         of integers, an empty string if the array is empty, or
	 *         <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(int[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		if (array.length == 0) {
			return StringPool.BLANK;
		}

		if (array.length == 1) {
			return String.valueOf(array[0]);
		}

		StringBundler sb = new StringBundler(2 * array.length - 1);

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				sb.append(delimiter);
			}

			sb.append(String.valueOf(array[i]));
		}

		return sb.toString();
	}

	/**
	 * Merges the elements of an array of long integers by returning a string
	 * representing a comma delimited list of its values.
	 *
	 * @param  array the long integers to merge
	 * @return a string representing a comma delimited list of the values of the
	 *         array of long integers, an empty string if the array is empty, or
	 *         <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(long[] array) {
		return merge(array, StringPool.COMMA);
	}

	/**
	 * Merges the elements of an array of long integers by returning a string
	 * representing a delimited list of its values.
	 *
	 * @param  array the long integers to merge
	 * @param  delimiter the delimiter
	 * @return a string representing a delimited list of the values of the array
	 *         of long integers, an empty string if the array is empty, or
	 *         <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(long[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		if (array.length == 0) {
			return StringPool.BLANK;
		}

		if (array.length == 1) {
			return String.valueOf(array[0]);
		}

		StringBundler sb = new StringBundler(2 * array.length - 1);

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				sb.append(delimiter);
			}

			sb.append(String.valueOf(array[i]));
		}

		return sb.toString();
	}

	/**
	 * Merges the elements of an array of objects into a string representing a
	 * comma delimited list of the objects.
	 *
	 * @param  array the objects to merge
	 * @return a string representing a comma delimited list of the objects, an
	 *         empty string if the array is empty, or <code>null</code> if the
	 *         array is <code>null</code>
	 */
	public static String merge(Object[] array) {
		return merge(array, StringPool.COMMA);
	}

	/**
	 * Merges the elements of an array of objects into a string representing a
	 * delimited list of the objects.
	 *
	 * @param  array the objects to merge
	 * @param  delimiter the delimiter
	 * @return a string representing a delimited list of the objects, an empty
	 *         string if the array is empty, or <code>null</code> if the array
	 *         is <code>null</code>
	 */
	public static String merge(Object[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		if (array.length == 0) {
			return StringPool.BLANK;
		}

		if (array.length == 1) {
			return String.valueOf(array[0]);
		}

		StringBundler sb = new StringBundler(2 * array.length - 1);

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				sb.append(delimiter);
			}

			sb.append(String.valueOf(array[i]).trim());
		}

		return sb.toString();
	}

	/**
	 * Merges the elements of an array of short integers by returning a string
	 * representing a comma delimited list of its values.
	 *
	 * @param  array the short integers to merge
	 * @return a string representing a comma delimited list of the values of the
	 *         array of short integers, an empty string if the array is empty,
	 *         or <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(short[] array) {
		return merge(array, StringPool.COMMA);
	}

	/**
	 * Merges the elements of an array of short integers by returning a string
	 * representing a delimited list of its values.
	 *
	 * @param  array the short integers to merge
	 * @param  delimiter the delimiter
	 * @return a string representing a delimited list of the values of the array
	 *         of short integers, an empty string if the array is empty, or
	 *         <code>null</code> if the array is <code>null</code>
	 */
	public static String merge(short[] array, String delimiter) {
		if (array == null) {
			return null;
		}

		if (array.length == 0) {
			return StringPool.BLANK;
		}

		if (array.length == 1) {
			return String.valueOf(array[0]);
		}

		StringBundler sb = new StringBundler(2 * array.length - 1);

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				sb.append(delimiter);
			}

			sb.append(String.valueOf(array[i]));
		}

		return sb.toString();
	}

	/**
	 * Replaces all occurrences of the character with the new character.
	 *
	 * @param  s the original string
	 * @param  oldSub the character to be searched for and replaced in the
	 *         original string
	 * @param  newSub the character with which to replace the
	 *         <code>oldSub</code> character
	 * @return a string representing the original string with all occurrences of
	 *         the <code>oldSub</code> character replaced with the
	 *         <code>newSub</code> character, or <code>null</code> if the
	 *         original string is <code>null</code>
	 */
	public static String replace(String s, char oldSub, char newSub) {
		if (s == null) {
			return null;
		}

		return s.replace(oldSub, newSub);
	}

	/**
	 * Replaces all occurrences of the character with the new string.
	 *
	 * @param  s the original string
	 * @param  oldSub the character to be searched for and replaced in the
	 *         original string
	 * @param  newSub the string with which to replace the <code>oldSub</code>
	 *         character
	 * @return a string representing the original string with all occurrences of
	 *         the <code>oldSub</code> character replaced with the string
	 *         <code>newSub</code>, or <code>null</code> if the original string
	 *         is <code>null</code>
	 */
	public static String replace(String s, char oldSub, String newSub) {
		if ((s == null) || (newSub == null)) {
			return null;
		}

		int index = s.indexOf(oldSub);

		if (index == -1) {
			return s;
		}

		int previousIndex = index;

		StringBundler sb = new StringBundler();

		if (previousIndex != 0) {
			sb.append(s.substring(0, previousIndex));
		}

		sb.append(newSub);

		while ((index = s.indexOf(oldSub, previousIndex + 1)) != -1) {
			sb.append(s.substring(previousIndex + 1, index));
			sb.append(newSub);

			previousIndex = index;
		}

		index = previousIndex + 1;

		if (index < s.length()) {
			sb.append(s.substring(index));
		}

		return sb.toString();
	}

	public static String replace(String s, char[] oldSubs, char[] newSubs) {
		if ((s == null) || (oldSubs == null) || (newSubs == null)) {
			return null;
		}

		if (oldSubs.length != newSubs.length) {
			return s;
		}

		StringBuilder sb = new StringBuilder(s.length());

		sb.append(s);

		boolean modified = false;

		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);

			for (int j = 0; j < oldSubs.length; j++) {
				if (c == oldSubs[j]) {
					sb.setCharAt(i, newSubs[j]);

					modified = true;

					break;
				}
			}
		}

		if (modified) {
			return sb.toString();
		}

		return s;
	}

	public static String replace(String s, char[] oldSubs, String[] newSubs) {
		if ((s == null) || (oldSubs == null) || (newSubs == null)) {
			return null;
		}

		if (oldSubs.length != newSubs.length) {
			return s;
		}

		StringBundler sb = null;

		int lastReplacementIndex = 0;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			for (int j = 0; j < oldSubs.length; j++) {
				if (c == oldSubs[j]) {
					if (sb == null) {
						sb = new StringBundler();
					}

					if (i > lastReplacementIndex) {
						sb.append(s.substring(lastReplacementIndex, i));
					}

					sb.append(newSubs[j]);

					lastReplacementIndex = i + 1;

					break;
				}
			}
		}

		if (sb == null) {
			return s;
		}

		if (lastReplacementIndex < s.length()) {
			sb.append(s.substring(lastReplacementIndex));
		}

		return sb.toString();
	}

	/**
	 * Replaces all occurrences of the string with the new string.
	 *
	 * @param  s the original string
	 * @param  oldSub the string to be searched for and replaced in the original
	 *         string
	 * @param  newSub the string with which to replace the <code>oldSub</code>
	 *         string
	 * @return a string representing the original string with all occurrences of
	 *         the <code>oldSub</code> string replaced with the string
	 *         <code>newSub</code>, or <code>null</code> if the original string
	 *         is <code>null</code>
	 */
	public static String replace(String s, String oldSub, String newSub) {
		return replace(s, oldSub, newSub, 0);
	}

	/**
	 * Replaces all occurrences of the string with the new string, starting from
	 * the specified index.
	 *
	 * @param  s the original string
	 * @param  oldSub the string to be searched for and replaced in the original
	 *         string
	 * @param  newSub the string with which to replace the <code>oldSub</code>
	 *         string
	 * @param  fromIndex the index of the original string from which to begin
	 *         searching
	 * @return a string representing the original string with all occurrences of
	 *         the <code>oldSub</code> string occurring after the specified
	 *         index replaced with the string <code>newSub</code>, or
	 *         <code>null</code> if the original string is <code>null</code>
	 */
	public static String replace(
		String s, String oldSub, String newSub, int fromIndex) {

		if (s == null) {
			return null;
		}

		if ((oldSub == null) || oldSub.equals(StringPool.BLANK)) {
			return s;
		}

		if (newSub == null) {
			newSub = StringPool.BLANK;
		}

		int y = s.indexOf(oldSub, fromIndex);

		if (y >= 0) {
			StringBundler sb = new StringBundler();

			int length = oldSub.length();
			int x = 0;

			while (x <= y) {
				sb.append(s.substring(x, y));
				sb.append(newSub);

				x = y + length;

				y = s.indexOf(oldSub, x);
			}

			sb.append(s.substring(x));

			return sb.toString();
		}
		else {
			return s;
		}
	}

	/**
	 * Replaces all occurrences of the keywords found in the substring, defined
	 * by the beginning and ending strings, with the new values.
	 *
	 * <p>
	 * For example, with the following initialized variables:
	 * </p>
	 *
	 * <p>
	 * <pre>
	 * <code>
	 * String s = "http://www.example-url/${userId}";
	 * String begin = "${";
	 * String end = "}";
	 * Map<String, String> values =  new HashMap&#60;String, String&#62;();
	 * values.put("userId", "jbloggs");
	 * </code>
	 * </pre>
	 * </p>
	 *
	 * <p>
	 * <code>replace(s, begin, end, values)</code> returns
	 * <code>"http://www.example-url/jbloggs"</code>
	 * </p>
	 *
	 * @param  s the original string
	 * @param  begin the string preceding the substring to be modified. This
	 *         string is excluded from the result.
	 * @param  end the string following the substring to be modified. This
	 *         string is excluded from the result.
	 * @param  values the key-value map values
	 * @return a string representing the original string with all occurrences of
	 *         the of the keywords found in the substring, replaced with the new
	 *         values. <code>null</code> is returned if the original string, the
	 *         beginning string, the ending string, or the key-map values are
	 *         <code>null</code>.
	 */
	public static String replace(
		String s, String begin, String end, Map<String, String> values) {

		StringBundler sb = replaceToStringBundler(s, begin, end, values);

		return sb.toString();
	}

	/**
	 * Replaces all occurrences of the elements of the string array with the
	 * corresponding elements of the new string array.
	 *
	 * @param  s the original string
	 * @param  oldSubs the strings to be searched for and replaced in the
	 *         original string
	 * @param  newSubs the strings with which to replace the
	 *         <code>oldSubs</code> strings
	 * @return a string representing the original string with all occurrences of
	 *         the <code>oldSubs</code> strings replaced with the corresponding
	 *         <code>newSubs</code> strings, or <code>null</code> if the
	 *         original string, the <code>oldSubs</code> array, or the
	 *         <code>newSubs</code> is <code>null</code>
	 */
	public static String replace(String s, String[] oldSubs, String[] newSubs) {
		if ((s == null) || (oldSubs == null) || (newSubs == null)) {
			return null;
		}

		if (oldSubs.length != newSubs.length) {
			return s;
		}

		for (int i = 0; i < oldSubs.length; i++) {
			s = replace(s, oldSubs[i], newSubs[i]);
		}

		return s;
	}

	/**
	 * Replaces all occurrences of the keywords found in the substring, defined
	 * by the beginning and ending strings, with the new values. The result is
	 * returned as a {@link StringBundler}.
	 *
	 * <p>
	 * For example, with the following initialized variables:
	 * </p>
	 *
	 * <p>
	 * <pre>
	 * <code>
	 * String s = "http://www.example-url/${userId}";
	 * String begin = "${";
	 * String end = "}";
	 * Map<String, String> values =  new HashMap&#60;String, String&#62;();
	 * values.put("userId", "jbloggs");
	 * </code>
	 * </pre>
	 * </p>
	 *
	 * <p>
	 * <code>StringBundler sb = replaceToStringBundler(s, begin, end,
	 * values)</code> <code>sb.toString()</code> returns
	 * <code>"http://www.example-url/jbloggs"</code>
	 * </p>
	 *
	 * @param  s the original string
	 * @param  begin the string preceding the substring to be modified. This
	 *         string is excluded from the result.
	 * @param  end the string following the substring to be modified. This
	 *         string is excluded from the result.
	 * @param  values the key-value map values
	 * @return a string bundler representing the original string with all
	 *         occurrences of the keywords found in the substring, replaced with
	 *         the new values. <code>null</code> is returned if the original
	 *         string, the beginning string, the ending string, or the key-map
	 *         values are <code>null</code>.
	 * @see    #replace(String, String, String, Map)
	 */
	public static StringBundler replaceToStringBundler(
		String s, String begin, String end, Map<String, String> values) {

		if (Validator.isBlank(s) || Validator.isBlank(begin) ||
			Validator.isBlank(end) || MapUtil.isEmpty(values)) {

			return new StringBundler(s);
		}

		StringBundler sb = new StringBundler(values.size() * 2 + 1);

		int pos = 0;

		while (true) {
			int x = s.indexOf(begin, pos);

			int y = s.indexOf(end, x + begin.length());

			if ((x == -1) || (y == -1)) {
				sb.append(s.substring(pos));

				break;
			}
			else {
				sb.append(s.substring(pos, x));

				String oldValue = s.substring(x + begin.length(), y);

				String newValue = values.get(oldValue);

				if (newValue == null) {
					newValue = oldValue;
				}

				sb.append(newValue);

				pos = y + end.length();
			}
		}

		return sb;
	}

	/**
	 * Splits string <code>s</code> around comma characters.
	 *
	 * <p>
	 * Examples:
	 * </p>
	 *
	 * <p>
	 * <pre>
	 * <code>
	 * split("Alice,Bob,Charlie") returns {"Alice", "Bob", "Charlie"}
	 * split("Alice, Bob, Charlie") returns {"Alice", " Bob", " Charlie"}
	 * </code>
	 * </pre>
	 * </p>
	 *
	 * @param  s the string to split
	 * @return the array of strings resulting from splitting string
	 *         <code>s</code> around comma characters, or an empty string array
	 *         if <code>s</code> is <code>null</code> or <code>s</code> is empty
	 */
	public static String[] split(String s) {
		return split(s, CharPool.COMMA);
	}

	/**
	 * Splits the string <code>s</code> around the specified delimiter.
	 *
	 * <p>
	 * Example:
	 * </p>
	 *
	 * <p>
	 * <pre>
	 * <code>
	 * splitLines("First;Second;Third", ';') returns {"First","Second","Third"}
	 * </code>
	 * </pre>
	 * </p>
	 *
	 * @param  s the string to split
	 * @param  delimiter the delimiter
	 * @return the array of strings resulting from splitting string
	 *         <code>s</code> around the specified delimiter character, or an
	 *         empty string array if <code>s</code> is <code>null</code> or if
	 *         <code>s</code> is empty
	 */
	public static String[] split(String s, char delimiter) {
		if (Validator.isNull(s)) {
			return _emptyStringArray;
		}

		s = s.trim();

		if (s.length() == 0) {
			return _emptyStringArray;
		}

		List<String> nodeValues = new ArrayList<>();

		_split(nodeValues, s, 0, delimiter);

		return nodeValues.toArray(new String[nodeValues.size()]);
	}

	/**
	 * Splits the string <code>s</code> around the specified delimiter string.
	 *
	 * <p>
	 * Example:
	 * </p>
	 *
	 * <p>
	 * <pre>
	 * <code>
	 * splitLines("oneandtwoandthreeandfour", "and") returns {"one","two","three","four"}
	 * </code>
	 * </pre>
	 * </p>
	 *
	 * @param  s the string to split
	 * @param  delimiter the delimiter
	 * @return the array of strings resulting from splitting string
	 *         <code>s</code> around the specified delimiter string, or an empty
	 *         string array if <code>s</code> is <code>null</code> or equals the
	 *         delimiter
	 */
	public static String[] split(String s, String delimiter) {
		if (Validator.isNull(s) || (delimiter == null) ||
			delimiter.equals(StringPool.BLANK)) {

			return _emptyStringArray;
		}

		s = s.trim();

		if (s.equals(delimiter)) {
			return _emptyStringArray;
		}

		if (delimiter.length() == 1) {
			return split(s, delimiter.charAt(0));
		}

		List<String> nodeValues = new ArrayList<>();

		int offset = 0;

		int pos = s.indexOf(delimiter, offset);

		while (pos != -1) {
			nodeValues.add(s.substring(offset, pos));

			offset = pos + delimiter.length();

			pos = s.indexOf(delimiter, offset);
		}

		if (offset < s.length()) {
			nodeValues.add(s.substring(offset));
		}

		return nodeValues.toArray(new String[nodeValues.size()]);
	}

	/**
	 * Returns a string representing the hexidecimal character code of the
	 * integer.
	 *
	 * <p>
	 * Examples:
	 * </p>
	 *
	 * <p>
	 * <pre>
	 * <code>
	 * toHexString(10) returns "a"
	 * toHexString(15) returns "f"
	 * toHexString(10995) returns "2af3"
	 * </code>
	 * </pre>
	 * </p>
	 *
	 * @param  i the integer to convert
	 * @return a string representing the hexidecimal character code of the
	 *         integer
	 */
	public static String toHexString(int i) {
		char[] buffer = new char[8];

		int index = 8;

		do {
			buffer[--index] = HEX_DIGITS[i & 15];

			i >>>= 4;
		}
		while (i != 0);

		return new String(buffer, index, 8 - index);
	}

	/**
	 * Returns a string representing the hexidecimal character code of the long
	 * integer.
	 *
	 * <p>
	 * Example:
	 * </p>
	 *
	 * <p>
	 * <pre>
	 * <code>
	 * toHexString(12345678910L) returns "2dfdc1c3e"
	 * </code>
	 * </pre>
	 * </p>
	 *
	 * @param  l the long integer to convert
	 * @return a string representing the hexidecimal character code of the long
	 *         integer
	 */
	public static String toHexString(long l) {
		char[] buffer = new char[16];

		int index = 16;

		do {
			buffer[--index] = HEX_DIGITS[(int)(l & 15)];

			l >>>= 4;
		}
		while (l != 0);

		return new String(buffer, index, 16 - index);
	}

	/**
	 * Returns a string representing the hexidecimal character code of the
	 * <code>Integer</code> or <code>Long</code> object type. If the object is
	 * not an instance of these types, the object's original value is returned.
	 *
	 * @param  obj the object to convert
	 * @return a string representing the hexidecimal character code of the
	 *         object
	 */
	public static String toHexString(Object obj) {
		if (obj instanceof Integer) {
			return toHexString(((Integer)obj).intValue());
		}
		else if (obj instanceof Long) {
			return toHexString(((Long)obj).longValue());
		}
		else {
			return String.valueOf(obj);
		}
	}

	protected static final char[] HEX_DIGITS = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
		'e', 'f'
	};

	private static void _split(
		Collection<String> values, String s, int offset, char delimiter) {

		int pos = s.indexOf(delimiter, offset);

		while (pos != -1) {
			values.add(s.substring(offset, pos));

			offset = pos + 1;

			pos = s.indexOf(delimiter, offset);
		}

		if (offset < s.length()) {
			values.add(s.substring(offset));
		}
	}

	private static final String[] _emptyStringArray = new String[0];

}