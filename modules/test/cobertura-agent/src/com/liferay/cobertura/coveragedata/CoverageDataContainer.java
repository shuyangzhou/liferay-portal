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

package com.liferay.cobertura.coveragedata;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sourceforge.cobertura.coveragedata.CoverageData;
import net.sourceforge.cobertura.coveragedata.HasBeenInstrumented;

/**
 * @author Cristina González
 */
public abstract class CoverageDataContainer
	implements CoverageData, HasBeenInstrumented, Serializable {

	public CoverageDataContainer() {
		_initLock();
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		CoverageDataContainer coverageDataContainer =
			(CoverageDataContainer)obj;

		_lock.lock();

		try {
			return getChildren().equals(coverageDataContainer.getChildren());
		}
		finally {
			_lock.unlock();
		}
	}

	public double getBranchCoverageRate() {
		int number = 0;

		int numberCovered = 0;

		_lock.lock();

		try {
			for (CoverageData coverageContainer : getChildren().values()) {
				number += coverageContainer.getNumberOfValidBranches();

				numberCovered += coverageContainer.getNumberOfCoveredBranches();
			}
		}
		finally {
			_lock.unlock();
		}

		if (number == 0) {

			// no branches, therefore 100% branch coverage.

			return 1d;
		}

		return (double)numberCovered / number;
	}

	public CoverageData getChild(String name) {
		_lock.lock();

		try {
			return (CoverageData)getChildren().get(name);
		}
		finally {
			_lock.unlock();
		}
	}

	public double getLineCoverageRate() {
		int number = 0;

		int numberCovered = 0;

		_lock.lock();

		try {
			for (CoverageData coverageContainer : getChildren().values()) {
				number += coverageContainer.getNumberOfValidLines();

				numberCovered += coverageContainer.getNumberOfCoveredLines();
			}
		}
		finally {
			_lock.unlock();
		}

		if (number == 0) {

			// no lines, therefore 100% line coverage.

			return 1d;
		}

		return (double)numberCovered / number;
	}

	public int getNumberOfChildren() {
		_lock.lock();

		try {
			return getChildren().size();
		}
		finally {
			_lock.unlock();
		}
	}

	public int getNumberOfCoveredBranches() {
		int number = 0;

		_lock.lock();

		try {
			for (CoverageData coverageContainer : getChildren().values()) {
				number += coverageContainer.getNumberOfCoveredBranches();
			}
		}
		finally {
			_lock.unlock();
		}

		return number;
	}

	public int getNumberOfCoveredLines() {
		int number = 0;

		_lock.lock();

		try {
			for (CoverageData coverageContainer : getChildren().values()) {
				number += coverageContainer.getNumberOfCoveredLines();
			}
		}
		finally {
			_lock.unlock();
		}

		return number;
	}

	public int getNumberOfValidBranches() {
		int number = 0;

		_lock.lock();

		try {
			for (CoverageData coverageContainer : getChildren().values()) {
				number += coverageContainer.getNumberOfValidBranches();
			}
		}
		finally {
			_lock.unlock();
		}

		return number;
	}

	public int getNumberOfValidLines() {
		int number = 0;

		_lock.lock();

		try {
			for (CoverageData coverageContainer : getChildren().values()) {
				number += coverageContainer.getNumberOfValidLines();
			}
		}
		finally {
			_lock.unlock();
		}

		return number;
	}

	public int hashCode() {
		_lock.lock();

		try {
			return getChildren().size();
		}
		finally {
			_lock.unlock();
		}
	}

	public void merge(CoverageData coverageData) {
		CoverageDataContainer container = (CoverageDataContainer)coverageData;

		getBothLocks(container);

		try {
			for (Object object : getChildren().keySet()) {
				CoverageData newChild = container.getChildren().get(object);

				CoverageData existingChild = getChildren().get(object);

				if (existingChild != null) {
					existingChild.merge(newChild);
				}
				else {
					getChildren().put(object, newChild);
				}
			}
		}
		finally {
			_lock.unlock();

			container._lock.unlock();
		}
	}

	protected void getBothLocks(CoverageDataContainer other) {
		boolean myLock = false;

		boolean otherLock = false;

		while (!myLock || !otherLock) {
			try {
				myLock = _lock.tryLock();
				otherLock = other._lock.tryLock();
			}
			finally {
				if (!myLock || !otherLock) {
					if (myLock) {
						_lock.unlock();
					}

					if (otherLock) {
						other._lock.unlock();
					}

					Thread.yield();
				}
			}
		}
	}

	protected Map<Object, CoverageData> getChildren() {
		return _children;
	}

	protected Lock getLock() {
		return _lock;
	}

	private void _initLock() {
		_lock = new ReentrantLock();
	}

	private void readObject(ObjectInputStream in)
		throws ClassNotFoundException, IOException {

		in.defaultReadObject();

		_initLock();
	}

	private static final long serialVersionUID = 2;

	private final Map<Object, CoverageData> _children = new HashMap<>();
	private transient Lock _lock;

}