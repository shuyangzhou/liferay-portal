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

package com.liferay.vulcan.consumer;

import java.util.Objects;

/**
 * Represents an operation that accepts seven input arguments and returns no
 * result. This is the seven-arity specialization of {@link
 * java.util.function.Consumer}. Unlike most other functional interfaces, {@code
 * TriConsumer} is expected to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object, Object, Object,
 * Object, Object, Object)}.
 *
 * @author Alejandro Hernández
 * @see    java.util.function.Consumer
 */
@FunctionalInterface
public interface HeptaConsumer<A, B, C, D, E, F, G> {

	/**
	 * Performs this operation on the given arguments.
	 *
	 * @param a the first function argument
	 * @param b the second function argument
	 * @param c the third function argument
	 * @param d the fourth function argument
	 * @param e the fifth function argument
	 * @param f the sixth function argument
	 * @param g the seventh function argument
	 */
	public void accept(A a, B b, C c, D d, E e, F f, G g);

	/**
	 * Returns a composed {@code HeptaConsumer} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation throws an exception, it is relayed to the caller of the
	 * composed operation. If performing this operation throws an exception, the
	 * {@code after} operation will not be performed.
	 *
	 * @param  after the operation to perform after this operation
	 * @return a composed {@code HeptaConsumer} that performs in sequence this
	 *         operation followed by the {@code after} operation
	 */
	public default HeptaConsumer<A, B, C, D, E, F, G> andThen(
		HeptaConsumer<? super A, ? super B, ? super C, ? super D, ? super E,
			? super F, ? super G> after) {

		Objects.requireNonNull(after);

		return (a, b, c, d, e, f, g) -> {
			accept(a, b, c, d, e, f, g);
			after.accept(a, b, c, d, e, f, g);
		};
	}

}