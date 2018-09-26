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

package com.liferay.apio.architect.sample.internal.resource;

import static com.liferay.apio.architect.sample.internal.auth.PermissionChecker.hasPermission;

import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.resource.NestedCollectionResource;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.apio.architect.sample.internal.resource.BlogPostingCollectionResource.BlogPostingIdentifier;
import com.liferay.apio.architect.sample.internal.resource.PersonCollectionResource.PersonIdentifier;
import com.liferay.apio.architect.sample.internal.router.BlogPostingCommentActionRouter;
import com.liferay.apio.architect.sample.internal.type.Comment;

import java.util.Date;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 */
@Component(service = NestedCollectionResource.class)
public class BlogPostingCommentNestedCollectionResource
	implements NestedCollectionResource
		<Comment, Long,
		 BlogPostingCommentNestedCollectionResource.
			 BlogPostingCommentIdentifier,
		 Long, BlogPostingIdentifier> {

	@Override
	public NestedCollectionRoutes<Comment, Long, Long> collectionRoutes(
		NestedCollectionRoutes.Builder<Comment, Long, Long> builder) {

		return builder.addGetter(
			(id, pagination) ->
				_blogPostingCommentActionRouter.retrievePage(pagination, id)
		).addCreator(
			_blogPostingCommentActionRouter::create, Credentials.class,
			(credentials, blogPostingModelId) -> hasPermission(credentials),
			BlogPostingCommentNestedCollectionResource::_buildForm
		).build();
	}

	@Override
	public String getName() {
		return "comments-dsl";
	}

	@Override
	public ItemRoutes<Comment, Long> itemRoutes(
		ItemRoutes.Builder<Comment, Long> builder) {

		return builder.addGetter(
			_blogPostingCommentActionRouter::retrieve
		).addRemover(
			_blogPostingCommentActionRouter::remove, Credentials.class,
			(credentials, id) -> hasPermission(credentials)
		).addUpdater(
			_blogPostingCommentActionRouter::replace, Credentials.class,
			(credentials, id) -> hasPermission(credentials),
			BlogPostingCommentNestedCollectionResource::_buildForm
		).build();
	}

	@Override
	public Representor<Comment> representor(
		Representor.Builder<Comment, Long> builder) {

		return builder.types(
			"CommentDSL"
		).identifier(
			Comment::getId
		).addDate(
			"dateCreated", Comment::getDateCreated
		).addDate(
			"dateModified", Comment::getDateModified
		).addLinkedModel(
			"author", PersonIdentifier.class, Comment::getAuthorId
		).addString(
			"text", Comment::getText
		).build();
	}

	public interface BlogPostingCommentIdentifier extends Identifier<Long> {
	}

	private static Form<CommentForm> _buildForm(
		Form.Builder<CommentForm> formBuilder) {

		return formBuilder.title(
			__ -> "The blog posting comment creator form"
		).description(
			__ -> "This form can be used to create a blog posting comment"
		).constructor(
			CommentForm::new
		).addRequiredLinkedModel(
			"author", PersonIdentifier.class, CommentForm::_setAuthorId
		).addRequiredString(
			"text", CommentForm::_setText
		).build();
	}

	@Reference
	private BlogPostingCommentActionRouter _blogPostingCommentActionRouter;

	private static class CommentForm implements Comment {

		@Override
		public Long getAuthorId() {
			return _authorId;
		}

		@Override
		public Date getDateCreated() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Date getDateModified() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Long getId() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getText() {
			return _text;
		}

		private void _setAuthorId(Long authorId) {
			_authorId = authorId;
		}

		private void _setText(String text) {
			_text = text;
		}

		private Long _authorId;
		private String _text;

	}

}