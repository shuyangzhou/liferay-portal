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

package com.liferay.apio.architect.sample.internal.router;

import static com.liferay.apio.architect.sample.internal.auth.PermissionChecker.hasPermission;
import static com.liferay.apio.architect.sample.internal.converter.CommentConverter.toComment;

import com.liferay.apio.architect.annotation.Actions.Create;
import com.liferay.apio.architect.annotation.Actions.Remove;
import com.liferay.apio.architect.annotation.Actions.Replace;
import com.liferay.apio.architect.annotation.Actions.Retrieve;
import com.liferay.apio.architect.annotation.Body;
import com.liferay.apio.architect.annotation.GenericParentId;
import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.annotation.ParentId;
import com.liferay.apio.architect.annotation.Permissions.CanRetrieve;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.router.ActionRouter;
import com.liferay.apio.architect.sample.internal.converter.CommentConverter;
import com.liferay.apio.architect.sample.internal.dao.BlogPostingCommentModelService;
import com.liferay.apio.architect.sample.internal.dto.BlogPostingCommentModel;
import com.liferay.apio.architect.sample.internal.identifier.ModelNameModelIdIdentifier;
import com.liferay.apio.architect.sample.internal.type.BlogPosting;
import com.liferay.apio.architect.sample.internal.type.Comment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides all the information necessary to expose <a
 * href="http://schema.org/Comment">Comment</a> resources through a web API. The
 * resources are mapped from the internal {@link BlogPostingCommentModel} model.
 *
 * @author Alejandro Hernández
 */
@Component(
	immediate = true,
	service = {ActionRouter.class, BlogPostingCommentActionRouter.class}
)
public class BlogPostingCommentActionRouter implements ActionRouter<Comment> {

	@CanRetrieve
	public boolean canRetrieve(Credentials credentials, @Id long id) {
		if (hasPermission(credentials)) {
			return true;
		}

		return false;
	}

	@Create
	public Comment create(
		@ParentId(BlogPosting.class) long id, @Body Comment comment,
		Credentials credentials) {

		if (!hasPermission(credentials)) {
			throw new ForbiddenException();
		}

		BlogPostingCommentModel blogPostingCommentModel =
			_blogPostingCommentModelService.create(
				comment.getCreatorId(), id, comment.getText());

		return toComment(blogPostingCommentModel);
	}

	@Remove
	public void remove(@Id long id, Credentials credentials) {
		if (!hasPermission(credentials)) {
			throw new ForbiddenException();
		}

		_blogPostingCommentModelService.remove(id);
	}

	@Replace
	public Comment replace(
		@Id long id, @Body Comment comment, Credentials credentials) {

		if (!hasPermission(credentials)) {
			throw new ForbiddenException();
		}

		Optional<BlogPostingCommentModel> optional =
			_blogPostingCommentModelService.update(id, comment.getText());

		return optional.map(
			CommentConverter::toComment
		).orElseThrow(
			() -> new NotFoundException(
				"Unable to get blog posting comment " + id)
		);
	}

	@Retrieve
	public Comment retrieve(@Id long id) {
		Optional<BlogPostingCommentModel> optional =
			_blogPostingCommentModelService.get(id);

		return optional.map(
			CommentConverter::toComment
		).orElseThrow(
			() -> new NotFoundException(
				"Unable to get blog posting comment " + id)
		);
	}

	@Retrieve
	public PageItems<Comment> retrieveCommentsForAModel(
		@GenericParentId ModelNameModelIdIdentifier
			modelNameModelIdIdentifier) {

		List<BlogPostingCommentModel> blogPostingCommentModels =
			_blogPostingCommentModelService.getPage(
				modelNameModelIdIdentifier.getModelId(), 0, Integer.MAX_VALUE);

		Stream<BlogPostingCommentModel> stream =
			blogPostingCommentModels.stream();

		List<Comment> blogPostingComments = stream.map(
			CommentConverter::toComment
		).collect(
			Collectors.toList()
		);

		return new PageItems<>(blogPostingComments, blogPostingComments.size());
	}

	@Retrieve
	public PageItems<Comment> retrievePage(
		@ParentId(BlogPosting.class) long id, Pagination pagination) {

		List<BlogPostingCommentModel> blogPostingCommentModels =
			_blogPostingCommentModelService.getPage(
				id, pagination.getStartPosition(), pagination.getEndPosition());
		int count = _blogPostingCommentModelService.getCount(id);

		Stream<BlogPostingCommentModel> stream =
			blogPostingCommentModels.stream();

		List<Comment> blogPostingComments = stream.map(
			CommentConverter::toComment
		).collect(
			Collectors.toList()
		);

		return new PageItems<>(blogPostingComments, count);
	}

	@Reference
	private BlogPostingCommentModelService _blogPostingCommentModelService;

}