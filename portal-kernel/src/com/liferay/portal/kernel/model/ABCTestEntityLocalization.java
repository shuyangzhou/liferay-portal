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
 * The extended model interface for the ABCTestEntityLocalization service. Represents a row in the &quot;ABCTestEntityLocalization&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see ABCTestEntityLocalizationModel
 * @see com.liferay.portal.model.impl.ABCTestEntityLocalizationImpl
 * @see com.liferay.portal.model.impl.ABCTestEntityLocalizationModelImpl
 * @generated
 */
@ImplementationClassName("com.liferay.portal.model.impl.ABCTestEntityLocalizationImpl")
@ProviderType
public interface ABCTestEntityLocalization
	extends ABCTestEntityLocalizationModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link com.liferay.portal.model.impl.ABCTestEntityLocalizationImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<ABCTestEntityLocalization, Long> ABC_TEST_ENTITY_LOCALIZATION_ID_ACCESSOR =
		new Accessor<ABCTestEntityLocalization, Long>() {
			@Override
			public Long get(ABCTestEntityLocalization abcTestEntityLocalization) {
				return abcTestEntityLocalization.getAbcTestEntityLocalizationId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<ABCTestEntityLocalization> getTypeClass() {
				return ABCTestEntityLocalization.class;
			}
		};
}