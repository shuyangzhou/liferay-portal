package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.io.ProtectedObjectInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamClass;
public class ProtectedClassLoaderObjectInputStream
	extends ProtectedObjectInputStream {

	public ProtectedClassLoaderObjectInputStream(
			InputStream inputStream, ClassLoader classLoader)
		throws IOException {

		super(inputStream);

		_classLoader = classLoader;
	}

	@Override
	protected Class<?> doResolveClass(ObjectStreamClass objectStreamClass)
		throws ClassNotFoundException {

		String name = objectStreamClass.getName();

		return ClassResolverUtil.resolve(name, _classLoader);
	}

	private final ClassLoader _classLoader;

}