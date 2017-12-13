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

package com.liferay.apio.architect.test.internal.list;

import com.liferay.apio.architect.list.FunctionalList;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * A {@code Matcher} that checks if an element is a {@link FunctionalList}
 * containing some values.
 *
 * @author Alejandro Hernández
 */
public class IsAFunctionalList<T>
	extends TypeSafeDiagnosingMatcher<FunctionalList<T>> {

	public IsAFunctionalList(Matcher<Iterable<? extends T>> matcher) {
		_matcher = matcher;
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText(
			"a functional list that conforms with an "
		).appendDescriptionOf(
			_matcher
		);
	}

	@Override
	protected boolean matchesSafely(
		final FunctionalList<T> functionalList, final Description description) {

		T head = functionalList.head();

		Stream<T> stream = Stream.concat(
			Stream.of(head), functionalList.tailStream());

		List<T> list = stream.collect(Collectors.toList());

		if (_matcher.matches(list)) {
			return true;
		}

		description.appendText("was a functional list whose ");

		_matcher.describeMismatch(list, description);

		return false;
	}

	private final Matcher<Iterable<? extends T>> _matcher;

}