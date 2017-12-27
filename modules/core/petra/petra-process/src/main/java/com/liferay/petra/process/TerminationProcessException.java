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

package com.liferay.petra.process;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;

import java.io.File;

import java.util.List;

/**
 * @author Shuyang Zhou
 */
public class TerminationProcessException extends ProcessException {

	public TerminationProcessException(int exitCode) {
		this("Subprocess terminated with exit code " + exitCode, exitCode);
	}

	public TerminationProcessException(
		ProcessBuilder processBuilder, int exitCode) {

		this(_getProcessBuilderMessage(processBuilder, exitCode), exitCode);
	}

	public TerminationProcessException(String message, int exitCode) {
		super(message);

		_exitCode = exitCode;
	}

	public int getExitCode() {
		return _exitCode;
	}

	private static String _getProcessBuilderMessage(
		ProcessBuilder processBuilder, int exitCode) {

		List<String> command = processBuilder.command();
		File directory = processBuilder.directory();

		StringBundler sb = new StringBundler(2 * command.size() + 4);

		sb.append("Subprocess <");

		for (String argument : command) {
			sb.append(argument);
			sb.append(StringPool.SPACE);
		}

		sb.setIndex(sb.index() - 1);

		sb.append("> running with working directory <");
		sb.append(directory.getAbsolutePath());
		sb.append("> terminated with exit code ");
		sb.append(exitCode);

		return sb.toString();
	}

	private final int _exitCode;

}