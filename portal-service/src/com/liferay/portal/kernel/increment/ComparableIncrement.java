/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.increment;

/**
 * @author László Csontos
 */
public abstract class ComparableIncrement<T>
	implements Increment<Comparable<T>> {

	public ComparableIncrement(Comparable<T> value, boolean increasing) {
		this.value = value;
		this.increasing = increasing;
	}

	@Override
	public void decrease(Comparable<T> delta) {
		doChange(delta, false, false);
	}

	@Override
	public ComparableIncrement<T> decreaseForNew(Comparable<T> delta) {
		return doChange(delta, false, true);
	}

	@Override
	public Comparable<T> getValue() {
		return value;
	}

	@Override
	public void increase(Comparable<T> delta) {
		doChange(delta, true, false);
	}

	@Override
	public ComparableIncrement<T> increaseForNew(Comparable<T> delta) {
		return doChange(delta, true, true);
	}

	@Override
	public void setValue(Comparable<T> value) {
		this.value = value;
	}

	protected abstract ComparableIncrement<T> createComparableIncrement();

	@SuppressWarnings("unchecked")
	protected ComparableIncrement<T> doChange(
		Comparable<T> delta, boolean doIncrease, boolean forNew) {

		ComparableIncrement<T> increment = this;

		if (forNew) {
			increment = createComparableIncrement();
		}

		if (delta == null) {
			return increment;
		}

		T value = (T)this.value;

		if ((doIncrease && increasing && (delta.compareTo(value) > 0)) ||
			(!doIncrease && !increasing && (delta.compareTo(value) < 0))) {

			increment.setValue(delta);
		}

		return increment;
	}

	protected boolean increasing;
	protected Comparable<T> value;

}