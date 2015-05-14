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

package com.liferay.util.ant;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;

/**
 * @author Shuyang Zhou
 */
public class BndChecksumTask extends Task implements Condition {

	@Override
	public boolean eval() throws BuildException {
		if (_bndFile == null) {
			throw new BuildException("Attribute bndFile must be set");
		}

		if (_algorithm == null) {
			_algorithm = "MD5";
		}

		try {
			_messageDigest = MessageDigest.getInstance(_algorithm);
		}
		catch (NoSuchAlgorithmException nsae) {
			throw new BuildException(nsae);
		}

		if (_checksumFile == null) {
			_checksumFile = new File(_bndFile.getName() + "." + _algorithm);
		}

		if (_value == null) {
			_value = StringPool.TRUE;
		}

		BndChecksum bndChecksum = null;

		try {
			Map<String, String> includeResourceChecksums = new HashMap<>();

			for (File file : _parseIncludeResourceFiles()) {
				includeResourceChecksums.put(
					file.getPath(), _calculateChecksum(file));
			}

			bndChecksum = new BndChecksum(
				_calculateChecksum(_bndFile), includeResourceChecksums);
		}
		catch (IOException ioe) {
			throw new BuildException("Unable to generate bnd checksum", ioe);
		}

		if (!_checksumFile.exists()) {
			try {
				_write(bndChecksum);
			}
			catch (IOException ioe) {
				throw new BuildException(
					"Unable to write " + _checksumFile, ioe);
			}

			return false;
		}

		boolean match = false;

		try {
			match = bndChecksum.equals(_read());
		}
		catch (IOException ioe) {
			log(
				"Unable to parse " + _checksumFile + ", ignore it.", ioe,
				Project.MSG_WARN);
		}
		finally {
			if (!match) {
				try {
					_write(bndChecksum);
				}
				catch (IOException ioe) {
					throw new BuildException(
						"Unable to write " + _checksumFile, ioe);
				}
			}
		}

		return match;
	}

	@Override
	public void execute() throws BuildException {
		if (_property == null) {
			throw new BuildException("Attribute property must be set");
		}

		if (eval()) {
			Project currentProject = getProject();

			currentProject.setNewProperty(_property, _value);
		}
	}

	public void setAlgorithm(String algorithm) {
		_algorithm = algorithm;
	}

	public void setBndFile(File bndFile) {
		_bndFile = bndFile;
	}

	public void setChecksumFile(File checksumFile) {
		_checksumFile = checksumFile;
	}

	public void setProperty(String property) {
		_property = property;
	}

	public void setValue(String value) {
		_value = value;
	}

	private String _calculateChecksum(File file) throws IOException {
		try (FileChannel fileChannel = FileChannel.open(
			file.toPath(), StandardOpenOption.READ)) {

			_messageDigest.update(
				fileChannel.map(
					FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()));

			return StringUtil.bytesToHexString(_messageDigest.digest());
		}
	}

	private Set<File> _parseIncludeResourceFiles() throws IOException {
		Properties properties = new Properties();

		try (InputStream inputStream = new FileInputStream(_bndFile)) {
			properties.load(inputStream);
		}

		Set<File> files = new HashSet<>();

		for (String includeResource :
				StringUtil.split(properties.getProperty("Include-Resource"))) {

			includeResource = includeResource.trim();

			if (includeResource.isEmpty() ||
				(includeResource.charAt(0) != CharPool.AT)) {

				continue;
			}

			int index = includeResource.indexOf(CharPool.EXCLAMATION);

			if (index == -1) {
				includeResource = includeResource.substring(1);
			}
			else {
				includeResource = includeResource.substring(1, index);
			}

			files.add(new File(includeResource));
		}

		return files;
	}

	private BndChecksum _read() throws IOException {
		Properties properties = new Properties();

		try (InputStream inputStream = new FileInputStream(_checksumFile)) {
			properties.load(inputStream);
		}

		String selfChecksum = (String)properties.remove(_bndFile.getName());

		Map<String, String> includeResourceChecksums = new HashMap<>();

		for (Entry<Object, Object> entry : properties.entrySet()) {
			includeResourceChecksums.put(
				(String)entry.getKey(), (String)entry.getValue());
		}

		return new BndChecksum(selfChecksum, includeResourceChecksums);
	}

	private void _write(BndChecksum bndChecksum) throws IOException {
		Properties properties = new Properties();

		properties.put(_bndFile.getName(), bndChecksum._selfChecksum);

		Map<String, String> includeResourceChecksums =
			bndChecksum._includeResourceChecksums;

		for (Entry<String, String> entry :
				includeResourceChecksums.entrySet()) {

			properties.put(entry.getKey(), entry.getValue());
		}

		try (OutputStream outputStream = new FileOutputStream(_checksumFile)) {
			properties.store(outputStream, null);
		}
	}

	private String _algorithm;
	private File _bndFile;
	private File _checksumFile;
	private MessageDigest _messageDigest;
	private String _property;
	private String _value;

	private static class BndChecksum {

		@Override
		public boolean equals(Object obj) {
			BndChecksum bndChecksum = (BndChecksum)obj;

			if (Validator.equals(_selfChecksum, bndChecksum._selfChecksum) &&
				_includeResourceChecksums.equals(
					bndChecksum._includeResourceChecksums)) {

				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {
			int hashCode = HashUtil.hash(0, _selfChecksum);

			return HashUtil.hash(hashCode, _includeResourceChecksums);
		}

		private BndChecksum(
			String selfChecksum, Map<String, String> includeResourceChecksums) {

			_selfChecksum = selfChecksum;
			_includeResourceChecksums = includeResourceChecksums;
		}

		private final Map<String, String> _includeResourceChecksums;
		private final String _selfChecksum;

	}

}