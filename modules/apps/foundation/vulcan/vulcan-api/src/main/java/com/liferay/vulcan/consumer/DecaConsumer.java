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
 * Represents an operation that accepts ten input arguments and returns no
 * result. This is the ten-arity specialization of {@link
 * java.util.function.Consumer}. Unlike most other functional interfaces, {@code
 * TriConsumer} is expected to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object, Object, Object,
 * Object, Object, Object, Object, Object, Object)}.
 *
 * @author Alejandro Hernández
 * @see    java.util.function.Consumer
 */
@FunctionalInterface
public interface DecaConsumer<A, B, C, D, E, F, G, H, I, J> {

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
	 * @param h the eighth function argument
	 * @param i the ninth function argument
	 * @param j the tenth function argument
	 */
	public void accept(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j);

	/**
	 * Returns a composed {@code DecaConsumer} that performs, in sequence, this
	 * operation followed by the {@code after} operation. If performing either
	 * operation throws an exception, it is relayed to the caller of the
	 * composed operation. If performing this operation throws an exception, the
	 * {@code after} operation will not be performed.
	 *
	 * @param  after the operation to perform after this operation
	 * @return a composed {@code UndecaConsumer} that performs in sequence this
	 *         operation followed by the {@code after} operation
	 */
	public default DecaConsumer<A, B, C, D, E, F, G, H, I, J> andThen(
		DecaConsumer<? super A, ? super B, ? super C, ? super D, ? super E,
			? super F, ? super G, ? super H, ? super I, ? super J> after) {

		Objects.requireNonNull(after);

		return (a, b, c, d, e, f, g, h, i, j) -> {
			accept(a, b, c, d, e, f, g, h, i, j);
			after.accept(a, b, c, d, e, f, g, h, i, j);
		};
	}

}