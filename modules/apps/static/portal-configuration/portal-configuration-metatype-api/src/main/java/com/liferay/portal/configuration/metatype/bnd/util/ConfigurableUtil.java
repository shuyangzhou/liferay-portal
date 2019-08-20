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

package com.liferay.portal.configuration.metatype.bnd.util;

import aQute.bnd.annotation.metatype.Configurable;

import com.liferay.petra.concurrent.ConcurrentReferenceKeyHashMap;
import com.liferay.petra.memory.FinalizeManager;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author Shuyang Zhou
 */
public class ConfigurableUtil {

	public static <T> T createConfigurable(
		Class<T> clazz, Dictionary<?, ?> properties) {

		return _createConfigurableSnapshot(
			clazz, Configurable.createConfigurable(clazz, properties));
	}

	public static <T> T createConfigurable(
		Class<T> clazz, Map<?, ?> properties) {

		return _createConfigurableSnapshot(
			clazz, Configurable.createConfigurable(clazz, properties));
	}

	private static <T> T _createConfigurableSnapshot(
		Class<T> interfaceClass, T configurable) {

		Constructor<?> snapshotClassConstructor =
			_snapshotClassConstructorCache.computeIfAbsent(
				interfaceClass,
				key -> {
					String snapshotClassName = StringBundler.concat(
						interfaceClass.getName(), "Snapshot",
						_counter.getAndIncrement());

					byte[] snapshotClassData = _generateSnapshotClassData(
						interfaceClass, snapshotClassName);

					try {
						Class<?> snapshotClass =
							(Class<?>)_defineClassMethod.invoke(
								interfaceClass.getClassLoader(),
								snapshotClassName, snapshotClassData, 0,
								snapshotClassData.length);

						return snapshotClass.getConstructor(interfaceClass);
					}
					catch (Throwable t) {
						throw new RuntimeException(
							"Unable to create snapshot class constructor for " +
								interfaceClass,
							t);
					}
				});

		try {
			return (T)snapshotClassConstructor.newInstance(configurable);
		}
		catch (Throwable t) {
			throw new RuntimeException(
				"Unable to create snapshot class instance for " +
					interfaceClass,
				t);
		}
	}

	private static <T> byte[] _generateSnapshotClassData(
		Class<T> interfaceClass, String snapshotClassName) {

		String snapshotClassBinaryName = _getClassBinaryName(snapshotClassName);
		String objectClassBinaryName = _getClassBinaryName(
			Object.class.getName());

		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

		classWriter.visit(
			Opcodes.V1_6, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER,
			snapshotClassBinaryName, null, objectClassBinaryName,
			new String[] {_getClassBinaryName(interfaceClass.getName())});

		Method[] declaredMethods = interfaceClass.getDeclaredMethods();

		// Fields

		for (Method method : declaredMethods) {
			FieldVisitor fieldVisitor = classWriter.visitField(
				Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL, method.getName(),
				Type.getDescriptor(method.getReturnType()), null, null);

			fieldVisitor.visitEnd();
		}

		// Constructor

		MethodVisitor constructorMethodVisitor = classWriter.visitMethod(
			Opcodes.ACC_PUBLIC, "<init>",
			Type.getMethodDescriptor(
				Type.VOID_TYPE, Type.getType(interfaceClass)),
			null, null);

		constructorMethodVisitor.visitCode();

		constructorMethodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		constructorMethodVisitor.visitMethodInsn(
			Opcodes.INVOKESPECIAL, objectClassBinaryName, "<init>", "()V",
			false);

		for (Method method : declaredMethods) {
			Class<?> returnType = method.getReturnType();

			constructorMethodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
			constructorMethodVisitor.visitVarInsn(Opcodes.ALOAD, 1);

			String methodName = method.getName();

			constructorMethodVisitor.visitMethodInsn(
				Opcodes.INVOKEINTERFACE,
				_getClassBinaryName(interfaceClass.getName()), methodName,
				Type.getMethodDescriptor(method), true);

			constructorMethodVisitor.visitFieldInsn(
				Opcodes.PUTFIELD, snapshotClassBinaryName, methodName,
				Type.getDescriptor(returnType));
		}

		constructorMethodVisitor.visitInsn(Opcodes.RETURN);

		constructorMethodVisitor.visitMaxs(0, 0);

		constructorMethodVisitor.visitEnd();

		// Methods

		for (Method method : declaredMethods) {
			String methodName = method.getName();
			Class<?> returnType = method.getReturnType();

			MethodVisitor methodVisitor = classWriter.visitMethod(
				Opcodes.ACC_PUBLIC, methodName,
				Type.getMethodDescriptor(method), null, null);

			methodVisitor.visitCode();

			method.setAccessible(true);

			methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);

			methodVisitor.visitFieldInsn(
				Opcodes.GETFIELD, snapshotClassBinaryName, methodName,
				Type.getDescriptor(returnType));

			if (returnType.isPrimitive()) {
				Type returnValueType = Type.getType(returnType);

				methodVisitor.visitInsn(
					returnValueType.getOpcode(Opcodes.IRETURN));
			}
			else {
				methodVisitor.visitInsn(Opcodes.ARETURN);
			}

			methodVisitor.visitMaxs(0, 0);

			methodVisitor.visitEnd();
		}

		classWriter.visitEnd();

		return classWriter.toByteArray();
	}

	private static String _getClassBinaryName(String className) {
		return className.replace(CharPool.PERIOD, CharPool.FORWARD_SLASH);
	}

	private static final AtomicLong _counter = new AtomicLong();
	private static final Method _defineClassMethod;
	private static final Map<Class<?>, Constructor<?>>
		_snapshotClassConstructorCache = new ConcurrentReferenceKeyHashMap<>(
			FinalizeManager.WEAK_REFERENCE_FACTORY);

	static {
		try {
			_defineClassMethod = ReflectionUtil.getDeclaredMethod(
				ClassLoader.class, "defineClass", String.class, byte[].class,
				int.class, int.class);
		}
		catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
	}

}