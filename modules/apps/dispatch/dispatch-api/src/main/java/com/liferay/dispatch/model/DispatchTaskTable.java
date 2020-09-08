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

package com.liferay.dispatch.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;DispatchTask&quot; database table.
 *
 * @author Matija Petanjek
 * @see DispatchTask
 * @generated
 */
public class DispatchTaskTable extends BaseTable<DispatchTaskTable> {

	public static final DispatchTaskTable INSTANCE = new DispatchTaskTable();

	public final Column<DispatchTaskTable, Long> mvccVersion = createColumn(
		"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<DispatchTaskTable, Long> dispatchTaskId = createColumn(
		"dispatchTaskId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<DispatchTaskTable, Long> companyId = createColumn(
		"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<DispatchTaskTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<DispatchTaskTable, String> userName = createColumn(
		"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<DispatchTaskTable, Date> createDate = createColumn(
		"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<DispatchTaskTable, Date> modifiedDate = createColumn(
		"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<DispatchTaskTable, Boolean> active = createColumn(
		"active_", Boolean.class, Types.BOOLEAN, Column.FLAG_DEFAULT);
	public final Column<DispatchTaskTable, String> cronExpression =
		createColumn(
			"cronExpression", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<DispatchTaskTable, String> name = createColumn(
		"name", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<DispatchTaskTable, Boolean> system = createColumn(
		"system_", Boolean.class, Types.BOOLEAN, Column.FLAG_DEFAULT);
	public final Column<DispatchTaskTable, String> type = createColumn(
		"type_", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<DispatchTaskTable, String> typeSettings = createColumn(
		"typeSettings", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);

	private DispatchTaskTable() {
		super("DispatchTask", DispatchTaskTable::new);
	}

}