/*******************************************************************************
 * Copyright (c) 2012, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.osgi.container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleRevisions;

/**
 * An implementation of {@link BundleRevisions} which represent a 
 * {@link Module} installed in a {@link ModuleContainer container}.
 * The ModuleRevisions provides a bridge between the revisions, the 
 * module and the container they are associated with.  The 
 * ModuleRevisions holds the information about the installation of
 * a module in a container such as the module id and location.
 * @since 3.10
 */
public final class ModuleRevisions implements BundleRevisions {
	private final Object monitor = new Object();
	private final Module module;
	private final ModuleContainer container;
	/* @GuardedBy("monitor") */
	private final List<ModuleRevision> revisions = new ArrayList<>(1);
	/* @GuardedBy("monitor") */
	private boolean uninstalled = false;
	/* @GuardedBy("monitor") */
	private ModuleRevision uninstalledCurrent;

	ModuleRevisions(Module module, ModuleContainer container) {
		this.module = module;
		this.container = container;

		ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

		readLock = readWriteLock.readLock();
		writeLock = readWriteLock.writeLock();
	}

	public Module getModule() {
		return module;
	}

	ModuleContainer getContainer() {
		return container;
	}

	@Override
	public Bundle getBundle() {
		return module.getBundle();
	}

	@Override
	public List<BundleRevision> getRevisions() {
		readLock.lock();

		try {
			return new ArrayList<BundleRevision>(revisions);
		}
		finally {
			readLock.unlock();
		}
	}

	/**
	 * Same as {@link ModuleRevisions#getRevisions()} except it
	 * returns a list of {@link ModuleRevision}.
	 * @return the list of module revisions
	 */
	public List<ModuleRevision> getModuleRevisions() {
		readLock.lock();

		try {
			return new ArrayList<>(revisions);
		}
		finally {
			readLock.unlock();
		}
	}

	/**
	 * Returns the current {@link ModuleRevision revision} associated with this revisions.
	 * 
	 * @return the current {@link ModuleRevision revision} associated with this revisions
	 *     or {@code null} if the current revision does not exist.
	 */
	ModuleRevision getCurrentRevision() {
		readLock.lock();

		try {
			if (uninstalled) {
				return uninstalledCurrent;
			}
			if (revisions.isEmpty()) {
				return null;
			}
			return revisions.get(0);
		}
		finally {
			readLock.unlock();
		}
	}

	ModuleRevision addRevision(ModuleRevision revision) {
		writeLock.lock();

		try {
			revisions.add(0, revision);
		}
		finally {
			writeLock.unlock();
		}

		return revision;
	}

	boolean removeRevision(ModuleRevision revision) {
		writeLock.lock();

		try {
			return revisions.remove(revision);
		} finally {
			module.cleanup(revision);

			writeLock.unlock();
		}
	}

	boolean isUninstalled() {
		readLock.lock();

		try {
			return uninstalled;
		}
		finally {
			readLock.unlock();
		}
	}

	void uninstall() {
		writeLock.lock();

		try {
			uninstalled = true;
			// save off the current revision
			if (revisions.isEmpty()) {
				throw new IllegalStateException("Revisions is empty on uninstall!"); //$NON-NLS-1$
			}
			uninstalledCurrent = revisions.get(0);
		}
		finally {
			writeLock.unlock();
		}
	}

	public String toString() {
		return "moduleID=" + module.getId(); //$NON-NLS-1$
	}

	private final Lock readLock;
	private final Lock writeLock;

}
/* @generated */