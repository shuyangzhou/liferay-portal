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

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author Preston Crary
 */
public class BoundedLinkedHashSet<E> extends LinkedHashSet<E> {

	public static final int UNBOUNDED = -1;

	public BoundedLinkedHashSet(int upperBound) {
		_upperBound = upperBound;
	}

	@Override
	public boolean add(E e) {
		if (isFull()) {
			return false;
		}

		return super.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		if (isFull()) {
			return false;
		}

		boolean modified = false;

		for (E e : collection) {
			if (add(e)) {
				modified = true;

				if (isFull()) {
					break;
				}
			}
		}

		return modified;
	}

	public int getUpperBound() {
		return _upperBound;
	}

	public boolean isFull() {
		return size() == _upperBound;
	}

	private final int _upperBound;

}