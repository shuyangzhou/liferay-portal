/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.process;

import com.liferay.portal.kernel.io.unsync.UnsyncBufferedInputStream;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author Shuyang Zhou
 */
public class ProcessExecutor {

	public static <T extends Serializable> T execute(
			ProcessCallable<T> processCallable, String classPath,
			boolean loggable)
		throws ProcessException {

		return execute(
			processCallable, classPath, System.out, System.err, loggable);
	}

	public static <T extends Serializable> T execute(
			ProcessCallable<T> processCallable, String classPath,
			OutputStream outputStream, OutputStream errorStream,
			boolean loggable)
		throws ProcessException {

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(
				"java", "-cp", classPath, ProcessExecutor.class.getName(),
				Boolean.toString(loggable));

			Process process = processBuilder.start();

			_writeObject(process.getOutputStream(), processCallable, true);

			if (loggable) {
				try {
					int b = -1;

					while ((b = process.getInputStream().read()) != -1) {
						outputStream.write(b);
					}
				}
				catch (EOFException eofe) {
				}
				finally {
					outputStream.flush();
				}
			}

			int exitCode = process.waitFor();

			if (exitCode != 0) {
				throw new ProcessException(
					"Subprocess terminated with exit code " + exitCode);
			}

			InputStream errorInputStream = process.getErrorStream();

			if (errorInputStream.available() > 0) {
				ProcessException processException =
					(ProcessException)_readObject(
						errorInputStream, errorStream, true);

				if (processException != null) {
					throw processException;
				}
			}

			if (loggable) {
				return null;
			}
			else {
				return (T)_readObject(
					process.getInputStream(), outputStream, true);
			}
		}
		catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	public static void main(String[] arguments)
		throws ClassNotFoundException, IOException {

		try {
			ProcessCallable<?> processCallable =
				(ProcessCallable<?>)_readObject(System.in, null, false);

			Object result = processCallable.call();

			if (!GetterUtil.getBoolean(arguments[0])) {
				_writeObject(System.out, result, false);
			}
		}
		catch (ProcessException pe) {
			_writeObject(System.err, pe, false);
		}
	}

	private static Object _readObject(
			InputStream inputStream, OutputStream outputStream,
			boolean close)
		throws ClassNotFoundException, IOException {

		if (outputStream != null) {
			inputStream = new UnsyncBufferedInputStream(inputStream);

			// Mark ObjectInputStream's magic header

			inputStream.mark(4);
		}

		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(
				inputStream);

			try {
				return objectInputStream.readObject();
			}
			finally {
				if (close) {
					objectInputStream.close();
				}
			}
		}
		catch (ObjectStreamException ose) {
			if (outputStream != null) {
				inputStream.reset();

				int b = -1;

				while ((b = inputStream.read()) != -1) {
					outputStream.write(b);
				}

				outputStream.flush();

				return null;
			}
			else {
				throw ose;
			}
		}
	}

	private static void _writeObject(
			OutputStream outputStream, Object object, boolean close)
		throws IOException {

		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
			outputStream);

		try {
			objectOutputStream.writeObject(object);
		}
		finally {
			if (close) {
				objectOutputStream.close();
			}
			else {
				objectOutputStream.flush();
			}
		}
	}

}