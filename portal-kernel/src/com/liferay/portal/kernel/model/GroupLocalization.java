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

package com.liferay.portal.kernel.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.util.Accessor;

/**
 * The extended model interface for the GroupLocalization service. Represents a row in the &quot;GroupLocalization&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see GroupLocalizationModel
 * @see com.liferay.portal.model.impl.GroupLocalizationImpl
 * @see com.liferay.portal.model.impl.GroupLocalizationModelImpl
 * @generated
 */
@ImplementationClassName("com.liferay.portal.model.impl.GroupLocalizationImpl")
@ProviderType
public interface GroupLocalization extends GroupLocalizationModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link com.liferay.portal.model.impl.GroupLocalizationImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<GroupLocalization, Long> GROUP_LOCALIZATION_ID_ACCESSOR =
		new Accessor<GroupLocalization, Long>() {
			@Override
			public Long get(GroupLocalization groupLocalization) {
				return groupLocalization.getGroupLocalizationId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<GroupLocalization> getTypeClass() {
				return GroupLocalization.class;
			}
		};
}