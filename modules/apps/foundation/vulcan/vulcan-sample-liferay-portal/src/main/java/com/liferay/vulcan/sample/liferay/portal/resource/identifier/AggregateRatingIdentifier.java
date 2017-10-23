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

package com.liferay.vulcan.sample.liferay.portal.resource.identifier;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.vulcan.resource.identifier.Identifier;

/**
 * Instances of this identifier represents an identifier for aggregate rating
 * entities.
 *
 * <p>
 * This identifier should only be used to identify {@link
 * com.liferay.vulcan.sample.liferay.portal.rating.AggregateRating} single
 * items.
 * </p>
 *
 * @author Alejandro Hernández
 * @review
 */
@ProviderType
public interface AggregateRatingIdentifier extends Identifier {

	/**
	 * Returns a new {@code AggregateRatingIdentifier} from a combination of
	 * className/classPK.
	 *
	 * @param  className the className of the identifier.
	 * @param  classPK the classPK of the identifier.
	 * @return the {@code AggregateRatingIdentifier}.
	 * @review
	 */
	public static AggregateRatingIdentifier create(
		String className, long classPK) {

		return new AggregateRatingIdentifier() {

			@Override
			public String getClassName() {
				return className;
			}

			@Override
			public long getClassPK() {
				return classPK;
			}

		};
	}

	/**
	 * Returns a new {@code AggregateRatingIdentifier} created from a model
	 * which must be a {@link ClassedModel};
	 *
	 * @param  t a {@link ClassedModel} model.
	 * @return the {@code AggregateRatingIdentifier}.
	 * @review
	 */
	public static <T extends ClassedModel> AggregateRatingIdentifier create(
		T t) {

		return create(t.getModelClassName(), (long)t.getPrimaryKeyObj());
	}

	/**
	 * Returns the class name.
	 *
	 * @return the class name.
	 * @review
	 */
	public String getClassName();

	/**
	 * Returns the class PK.
	 *
	 * @return the class PK.
	 * @review
	 */
	public long getClassPK();

}