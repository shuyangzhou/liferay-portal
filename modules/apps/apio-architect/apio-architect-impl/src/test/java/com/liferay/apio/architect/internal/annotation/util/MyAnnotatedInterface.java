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

package com.liferay.apio.architect.internal.annotation.util;

import static com.liferay.apio.architect.annotation.Permissions.CanCreate;
import static com.liferay.apio.architect.annotation.Permissions.CanRetrieve;

import com.liferay.apio.architect.annotation.Actions.Action;
import com.liferay.apio.architect.annotation.Actions.Create;
import com.liferay.apio.architect.annotation.Actions.Remove;
import com.liferay.apio.architect.annotation.Body;
import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.annotation.ParentId;
import com.liferay.apio.architect.annotation.Vocabulary.Type;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;

import java.util.List;

/**
 * @author Alejandro Hernández
 */
public interface MyAnnotatedInterface {

	@Action(httpMethod = "GET", name = "name")
	public void annotatedWithAction();

	@CanCreate
	public boolean annotatedWithCanCreate();

	@CanRetrieve
	public boolean annotatedWithCanRetrieve(@Id long id);

	@Create
	public void annotatedWithCreate();

	@Remove
	public void annotatedWithRemove();

	public void notAnnotated();

	public List<MyType> returningList();

	public MyType returningMyType();

	public PageItems<MyType> returningPageItems();

	public String returningString();

	public void returningVoid();

	public void withIdAndBodyParameters(
		Pagination pagination, Credentials credentials, @Id Long aLong,
		@Body MyType string);

	public void withListBodyParameters(@Body List<MyType> list);

	public void withParameterAnnotated(@Id long id);

	public void withParametersNotAnnotated(long id);

	public void withParentId(@ParentId(MyType.class) long aLong);

	@Type("MyType")
	public interface MyType extends Identifier<Long> {
	}

}