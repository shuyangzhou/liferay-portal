package com.liferay.portal.osgi.web.servlet.jsp.compiler.internal;

import java.io.IOException;
import java.io.InputStream;

import java.net.URI;

import java.nio.file.Files;
import java.nio.file.Path;

public class RegularJavaFileObject extends BaseJavaFileObject {

	public RegularJavaFileObject(String className, Path path) {
		super(Kind.CLASS, className);

		_path = path;
	}

	@Override
	public InputStream openInputStream() throws IOException {
		return Files.newInputStream(_path);
	}

	@Override
	public URI toUri() {
		return _path.toUri();
	}

	private final Path _path;

}