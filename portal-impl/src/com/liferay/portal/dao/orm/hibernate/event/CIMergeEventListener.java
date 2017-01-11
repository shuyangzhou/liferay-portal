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

package com.liferay.portal.dao.orm.hibernate.event;

import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.io.unsync.UnsyncPrintWriter;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.file.Files;

import org.hibernate.HibernateException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.event.MergeEvent;
import org.hibernate.event.def.DefaultMergeEventListener;

/**
 * @author Tom Wang
 * @author Preston Crary
 */
public class CIMergeEventListener extends DefaultMergeEventListener {

	public static final CIMergeEventListener INSTANCE =
		new CIMergeEventListener();

	@Override
	public void onMerge(MergeEvent event) throws HibernateException {
		try {
			super.onMerge(event);

			_logEvent(event);
		}
		catch (StaleObjectStateException sose) {
			_findStaleObjectStateExceptionCause(event, sose);
		}
	}

	private CIMergeEventListener() {
		try {
			_tempFile = File.createTempFile("mvcc-log", null);

			_tempFile.deleteOnExit();
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private void _findStaleObjectStateExceptionCause(
		MergeEvent event, StaleObjectStateException sose) {

		Object object = event.getEntity();

		if (!(object instanceof MVCCModel)) {
			throw sose;
		}

		MVCCModel mvccModel = (MVCCModel)object;

		BaseModel<?> baseModel = (BaseModel<?>)object;

		StringBundler sb = new StringBundler(64);

		sb.append("{entityName=");
		sb.append(event.getEntityName());
		sb.append(", primaryKey=");
		sb.append(baseModel.getPrimaryKeyObj());
		sb.append(", previousMvccVersion=");
		sb.append(mvccModel.getMvccVersion());
		sb.append("}");

		String search = sb.toString();

		sb.setIndex(0);

		try (InputStreamReader inputStreamReader = new InputStreamReader(
				Files.newInputStream(_tempFile.toPath()));
			UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(inputStreamReader)) {

			String line = null;

			while ((line = unsyncBufferedReader.readLine()) != null) {
				if (line.equals(search)) {
					sb.append("This update caused an MVCC conflict: \n");

					sb.append(search);
					sb.append("\n");

					while ((line = unsyncBufferedReader.readLine()) != null) {
						if (line.startsWith("{entityName=")) {
							throw new RuntimeException(sb.toString(), sose);
						}

						sb.append(line);
						sb.append("\n");
					}

					throw new RuntimeException(sb.toString(), sose);
				}
			}
		}
		catch (IOException ioe) {
			sose.addSuppressed(ioe);
		}

		throw sose;
	}

	private void _logEvent(MergeEvent event) {
		Object object = event.getEntity();

		if (!(object instanceof MVCCModel)) {
			return;
		}

		MVCCModel mvccModel = (MVCCModel)object;

		BaseModel<?> baseModel = (BaseModel<?>)object;

		try (UnsyncPrintWriter unsyncPrintWriter = new UnsyncPrintWriter(
				new FileOutputStream(_tempFile, true))) {

			unsyncPrintWriter.write("{entityName=");
			unsyncPrintWriter.write(event.getEntityName());
			unsyncPrintWriter.write(", primaryKey=");
			unsyncPrintWriter.write(
				String.valueOf(baseModel.getPrimaryKeyObj()));
			unsyncPrintWriter.write(", previousMvccVersion=");
			unsyncPrintWriter.write(
				String.valueOf(mvccModel.getMvccVersion() - 1));
			unsyncPrintWriter.write("}\n");

			StaleObjectStateException sose = new StaleObjectStateException(
				event.getEntityName(), baseModel.getPrimaryKeyObj());

			sose.printStackTrace(unsyncPrintWriter);

			unsyncPrintWriter.flush();
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private final File _tempFile;

}