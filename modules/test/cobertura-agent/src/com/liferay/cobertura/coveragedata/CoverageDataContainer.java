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

/**
 * @author Cristina González
 */
public abstract class CoverageDataContainer
	implements CoverageData, Serializable {

	public CoverageDataContainer() {
		_initLock();
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		CoverageDataContainer coverageDataContainer =
			(CoverageDataContainer)obj;

		lock.lock();

		try {
			return children.equals(coverageDataContainer.children);
		}
		finally {
			lock.unlock();
		}
	}

	public double getBranchCoverageRate() {
		int number = 0;

		int numberCovered = 0;

		lock.lock();

		try {
			for (CoverageData coverageContainer : children.values()) {
				number += coverageContainer.getNumberOfValidBranches();

				numberCovered += coverageContainer.getNumberOfCoveredBranches();
			}
		}
		finally {
			lock.unlock();
		}

		if (number == 0) {

			// no branches, therefore 100% branch coverage.

			return 1d;
		}

		return (double)numberCovered / number;
	}

	public CoverageData getChild(String name) {
		lock.lock();

		try {
			return (CoverageData)children.get(name);
		}
		finally {
			lock.unlock();
		}
	}

	public double getLineCoverageRate() {
		int number = 0;

		int numberCovered = 0;

		lock.lock();

		try {
			for (CoverageData coverageContainer : children.values()) {
				number += coverageContainer.getNumberOfValidLines();

				numberCovered += coverageContainer.getNumberOfCoveredLines();
			}
		}
		finally {
			lock.unlock();
		}

		if (number == 0) {

			// no lines, therefore 100% line coverage.

			return 1d;
		}

		return (double)numberCovered / number;
	}

	public int getNumberOfChildren() {
		lock.lock();

		try {
			return children.size();
		}
		finally {
			lock.unlock();
		}
	}

	public int getNumberOfCoveredBranches() {
		int number = 0;

		lock.lock();

		try {
			for (CoverageData coverageContainer : children.values()) {
				number += coverageContainer.getNumberOfCoveredBranches();
			}
		}
		finally {
			lock.unlock();
		}

		return number;
	}

	public int getNumberOfCoveredLines() {
		int number = 0;

		lock.lock();

		try {
			for (CoverageData coverageContainer : children.values()) {
				number += coverageContainer.getNumberOfCoveredLines();
			}
		}
		finally {
			lock.unlock();
		}

		return number;
	}

	public int getNumberOfValidBranches() {
		int number = 0;

		lock.lock();

		try {
			for (CoverageData coverageContainer : children.values()) {
				number += coverageContainer.getNumberOfValidBranches();
			}
		}
		finally {
			lock.unlock();
		}

		return number;
	}

	public int getNumberOfValidLines() {
		int number = 0;

		lock.lock();

		try {
			for (CoverageData coverageContainer : children.values()) {
				number += coverageContainer.getNumberOfValidLines();
			}
		}
		finally {
			lock.unlock();
		}

		return number;
	}

	public int hashCode() {
		lock.lock();

		try {
			return children.size();
		}
		finally {
			lock.unlock();
		}
	}

	public void merge(CoverageData coverageData) {
		CoverageDataContainer container = (CoverageDataContainer)coverageData;

		getBothLocks(container);

		try {
			Map<Object, CoverageData> containerChildren = container.children;

			for (Object key : containerChildren.keySet()) {
				CoverageData newChild = containerChildren.get(key);

				CoverageData existingChild = containerChildren.get(key);

				if (existingChild != null) {
					existingChild.merge(newChild);
				}
				else {
					children.put(key, newChild);
				}
			}
		}
		finally {
			lock.unlock();

			container.lock.unlock();
		}
	}

	protected void getBothLocks(CoverageDataContainer other) {
		boolean myLock = false;

		boolean otherLock = false;

		while (!myLock || !otherLock) {
			try {
				myLock = lock.tryLock();
				otherLock = other.lock.tryLock();
			}
			finally {
				if (!myLock || !otherLock) {
					if (myLock) {
						lock.unlock();
					}

					if (otherLock) {
						other.lock.unlock();
					}

					Thread.yield();
				}
			}
		}
	}

	protected Map<Object, CoverageData> children = new HashMap<>();
	protected transient Lock lock;

	private void _initLock() {
		lock = new ReentrantLock();
	}

	private void readObject(ObjectInputStream in)
		throws ClassNotFoundException, IOException {

		in.defaultReadObject();

		_initLock();
	}

	private static final long serialVersionUID = 2;

}