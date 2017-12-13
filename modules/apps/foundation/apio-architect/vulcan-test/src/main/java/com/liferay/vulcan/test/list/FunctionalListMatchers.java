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

package com.liferay.vulcan.test.list;

import com.liferay.vulcan.list.FunctionalList;
import com.liferay.vulcan.test.internal.list.IsAFunctionalList;

import org.hamcrest.Matcher;

/**
 * This class provides {@code Hamcrest} {@link Matcher}s that can be used for
 * testing a {@link FunctionalList}.
 *
 * <p>
 * This class shouldn't be instantiated.
 * </p>
 *
 * @author Alejandro Hernández
 * @review
 */
public final class FunctionalListMatchers {

	/**
	 * Returns a matcher that checks that this is a functional list that
	 * conforms with the matcher receive as parameter.
	 *
	 * @param  matcher the matcher used for checking the contents of this
	 *         functional interface
	 * @return a matcher that checks that this is a functional list that matches
	 *         the matcher receives as parameter
	 * @review
	 */
	public static <T> Matcher<FunctionalList<T>> aFunctionalListThat(
		Matcher<Iterable<? extends T>> matcher) {

		return new IsAFunctionalList<>(matcher);
	}

	private FunctionalListMatchers() {
		throw new UnsupportedOperationException();
	}

}