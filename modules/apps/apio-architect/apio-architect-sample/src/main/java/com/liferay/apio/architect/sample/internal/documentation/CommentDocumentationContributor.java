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

package com.liferay.apio.architect.sample.internal.documentation;

import com.liferay.apio.architect.documentation.contributor.CustomDocumentation;
import com.liferay.apio.architect.documentation.contributor.CustomDocumentationContributor;

import org.osgi.service.component.annotations.Component;

/**
 * @author Víctor Galán
 */
@Component
public class CommentDocumentationContributor
	implements CustomDocumentationContributor {

	@Override
	public CustomDocumentation customDocumentation(
		CustomDocumentation.Builder builder) {

		builder.addDescription(
			"author", "The author of this content."
		).addDescription(
			"comments/delete", "Delete an existing comment."
		).addDescription(
			"comments/retrieve", "Return the list of comments."
		).addDescription(
			"comments/update", "Update an existing comment."
		).addDescription(
			"dateCreated",
			"The date on which the Comment was created or the" +
				"item was added to a DataFeed."
		).addDescription(
			"dateModified", "The date on which the comment was modified."
		).addDescription(
			"text", "The content of the comment"
		);

		return builder.build();
	}

}