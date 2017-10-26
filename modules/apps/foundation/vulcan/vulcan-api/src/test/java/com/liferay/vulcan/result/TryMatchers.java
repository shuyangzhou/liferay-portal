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

package com.liferay.vulcan.result;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * This class provides {@code Hamcrest} {@link Matcher}s that can be used for
 * testing the {@link Try} monadic type.
 *
 * @author Alejandro Hernández
 * @review
 */
public class TryMatchers {

	/**
	 * Returns a matcher that checks that this try is a {@link Try.Failure}.
	 *
	 * @return a matcher that checks that this try is a failure
	 * @review
	 */
	public static <T> Matcher<Try<? extends T>> aFailTry() {
		return new FailTry<>();
	}

	/**
	 * Returns a matcher that checks that this try is a {@link Try.Success}.
	 *
	 * @return a matcher that checks that this try is a success
	 * @review
	 */
	public static <T> Matcher<Try<? extends T>> aSuccessTry() {
		return new SuccessTry<>();
	}

	/**
	 * Returns a matcher that checks that this try is a {@link Try.Success} with
	 * a certain value.
	 *
	 * @param  matcher the matcher used for checking the value
	 * @return a matcher that checks that this try is a success with a certain
	 *         value
	 * @review
	 */
	public static <T> Matcher<Try<? extends T>> aTryWithValue(
		final Matcher<T> matcher) {

		return new ValueTry<>(matcher);
	}

	private static class FailTry<T>
		extends TypeSafeDiagnosingMatcher<Try<? extends T>> {

		@Override
		public void describeTo(final Description description) {
			description.appendText("a Failure");
		}

		@Override
		protected boolean matchesSafely(
			final Try<? extends T> tTry, final Description description) {

			if (tTry.isFailure()) {
				return true;
			}
			else {
				description.appendText("was a Failure");

				return false;
			}
		}

	}

	private static class SuccessTry<T>
		extends TypeSafeDiagnosingMatcher<Try<? extends T>> {

		@Override
		public void describeTo(final Description description) {
			description.appendText("a Success");
		}

		@Override
		protected boolean matchesSafely(
			final Try<? extends T> tTry, final Description description) {

			if (tTry.isSuccess()) {
				return true;
			}
			else {
				description.appendText("was a Success");

				return false;
			}
		}

	}

	private static class ValueTry<T>
		extends TypeSafeDiagnosingMatcher<Try<? extends T>> {

		public ValueTry(final Matcher<T> matcher) {
			_matcher = matcher;
		}

		@Override
		public void describeTo(final Description description) {
			description.appendText(
				"a Try with a value that "
			).appendDescriptionOf(
				_matcher
			);
		}

		@Override
		protected boolean matchesSafely(
			final Try<? extends T> tTry, final Description description) {

			if (tTry.isSuccess()) {
				if (_matcher.matches(tTry.getUnchecked())) {
					return true;
				}
				else {
					description.appendText("was a Try whose value ");

					_matcher.describeMismatch(tTry.getUnchecked(), description);

					return false;
				}
			}
			else {
				description.appendText("was a failure");

				return false;
			}
		}

		private final Matcher<T> _matcher;

	}

}