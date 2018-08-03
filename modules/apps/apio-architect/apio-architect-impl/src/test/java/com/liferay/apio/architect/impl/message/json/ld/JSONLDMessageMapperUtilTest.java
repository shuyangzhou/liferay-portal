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

package com.liferay.apio.architect.impl.message.json.ld;

import static com.liferay.apio.architect.impl.message.json.ld.JSONLDMessageMapperUtil.getOperationTypes;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import com.liferay.apio.architect.impl.operation.BatchCreateOperation;
import com.liferay.apio.architect.impl.operation.CreateOperation;
import com.liferay.apio.architect.impl.operation.DeleteOperation;
import com.liferay.apio.architect.impl.operation.RetrieveOperation;
import com.liferay.apio.architect.impl.operation.UpdateOperation;
import com.liferay.apio.architect.operation.Operation;
import com.liferay.apio.architect.test.util.internal.util.DescriptionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.List;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class JSONLDMessageMapperUtilTest {

	@Test(expected = UnsupportedOperationException.class)
	public void testConstructorThrowsException() throws Throwable {
		Constructor<?> constructor =
			DescriptionUtil.class.getDeclaredConstructors()[0];

		constructor.setAccessible(true);

		try {
			constructor.newInstance();
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	@Test
	public void testGetOperationTypesOnBatchCreateOperationReturnValidTypes() {
		Operation operation = new BatchCreateOperation(null, "", "");

		List<String> operationTypes = getOperationTypes(operation);

		assertThat(operationTypes, contains("BatchCreateAction", "Operation"));
	}

	@Test
	public void testGetOperationTypesOnCreateOperationReturnValidTypes() {
		Operation operation = new CreateOperation(null, "", "");

		List<String> operationTypes = getOperationTypes(operation);

		assertThat(operationTypes, contains("CreateAction", "Operation"));
	}

	@Test
	public void testGetOperationTypesOnDeleteOperationReturnValidTypes() {
		Operation operation = new DeleteOperation("", "");

		List<String> operationTypes = getOperationTypes(operation);

		assertThat(operationTypes, contains("DeleteAction", "Operation"));
	}

	@Test
	public void testGetOperationTypesOnRetrieveOperationReturnValidTypes() {
		Operation operation = new RetrieveOperation("", true);

		List<String> operationTypes = getOperationTypes(operation);

		assertThat(operationTypes, contains("Operation"));
	}

	@Test
	public void testGetOperationTypesOnUpdateOperationReturnValidTypes() {
		Operation operation = new UpdateOperation(null, "", "");

		List<String> operationTypes = getOperationTypes(operation);

		assertThat(operationTypes, contains("ReplaceAction", "Operation"));
	}

}