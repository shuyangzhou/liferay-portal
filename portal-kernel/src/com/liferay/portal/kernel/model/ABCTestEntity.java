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
 * The extended model interface for the ABCTestEntity service. Represents a row in the &quot;ABCTestEntity&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see ABCTestEntityModel
 * @see com.liferay.portal.model.impl.ABCTestEntityImpl
 * @see com.liferay.portal.model.impl.ABCTestEntityModelImpl
 * @generated
 */
@ImplementationClassName("com.liferay.portal.model.impl.ABCTestEntityImpl")
@ProviderType
public interface ABCTestEntity extends ABCTestEntityModel, PersistedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link com.liferay.portal.model.impl.ABCTestEntityImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<ABCTestEntity, String> ABC_TEST_ENTITY_ID_ACCESSOR =
		new Accessor<ABCTestEntity, String>() {
			@Override
			public String get(ABCTestEntity abcTestEntity) {
				return abcTestEntity.getAbcTestEntityId();
			}

			@Override
			public Class<String> getAttributeClass() {
				return String.class;
			}

			@Override
			public Class<ABCTestEntity> getTypeClass() {
				return ABCTestEntity.class;
			}
		};
}