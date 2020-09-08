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

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the DispatchTask service. Represents a row in the &quot;DispatchTask&quot; database table, with each column mapped to a property of this class.
 *
 * @author Matija Petanjek
 * @see DispatchTaskModel
 * @generated
 */
@ImplementationClassName("com.liferay.dispatch.model.impl.DispatchTaskImpl")
@ProviderType
public interface DispatchTask extends DispatchTaskModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.dispatch.model.impl.DispatchTaskImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<DispatchTask, Long> DISPATCH_TASK_ID_ACCESSOR =
		new Accessor<DispatchTask, Long>() {

			@Override
			public Long get(DispatchTask dispatchTask) {
				return dispatchTask.getDispatchTaskId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<DispatchTask> getTypeClass() {
				return DispatchTask.class;
			}

		};

	public java.util.Date getEndDate()
		throws com.liferay.portal.kernel.scheduler.SchedulerException;

	public java.util.Date getStartDate()
		throws com.liferay.portal.kernel.scheduler.SchedulerException;

	public com.liferay.portal.kernel.util.UnicodeProperties
		getTypeSettingsProperties();

	public void setEndDate(java.util.Date endDate);

	public void setStartDate(java.util.Date startDate);

	public void setTypeSettingsUnicodeProperties(
		com.liferay.portal.kernel.util.UnicodeProperties
			typeSettingsUnicodeProperties);

}