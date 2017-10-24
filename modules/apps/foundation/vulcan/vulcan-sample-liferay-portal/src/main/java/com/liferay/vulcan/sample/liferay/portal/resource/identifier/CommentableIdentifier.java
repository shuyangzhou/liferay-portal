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
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.vulcan.resource.identifier.Identifier;

/**
 * Instances of this identifier represents an identifier of something
 * "commentable".
 *
 * <p>
 * This identifier should only be used to identify comment collections added
 * through {@link
 * com.liferay.vulcan.resource.builder.RepresentorBuilder.FirstStep#addRelatedCollection(
 * String, Class, java.util.function.Function)}
 * </p>
 *
 * <p>
 * These values can be retrieved using {@link #getClassName()}, {@link
 * #getClassPK()} and {@link #getGroupId()} methods.
 * </p>
 *
 * @author Alejandro Hernández
 * @review
 */
@ProviderType
public interface CommentableIdentifier extends Identifier {

	/**
	 * Returns a new {@code CommentableIdentifier} from a combination of
	 * className/classPK/groupId.
	 *
	 * @param  className the className of the identifier.
	 * @param  classPK the classPK of the identifier.
	 * @param  groupId the groupId of the identifier.
	 * @return the {@code CommentableIdentifier}.
	 * @review
	 */
	public static CommentableIdentifier create(
		String className, long classPK, long groupId) {

		return new CommentableIdentifier() {

			@Override
			public String getClassName() {
				return className;
			}

			@Override
			public long getClassPK() {
				return classPK;
			}

			@Override
			public long getGroupId() {
				return groupId;
			}

		};
	}

	/**
	 * Returns a new {@code CommentableIdentifier} created from a model which
	 * must be both {@link GroupedModel} and {@link ClassedModel};
	 *
	 * @param  t a {@link GroupedModel} & {@link ClassedModel} model.
	 * @return the {@code CommentableIdentifier}.
	 * @review
	 */
	public static <T extends GroupedModel & ClassedModel> CommentableIdentifier
		create(T t) {

		return create(
			t.getModelClassName(), (long)t.getPrimaryKeyObj(), t.getGroupId());
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

	/**
	 * Returns the group ID.
	 *
	 * @return the group ID.
	 * @review
	 */
	public long getGroupId();

}