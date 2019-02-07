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

package com.liferay.headless.foundation.internal.resource;

import com.liferay.headless.foundation.dto.Category;
import com.liferay.headless.foundation.resource.CategoryResource;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.context.AcceptLanguage;
import com.liferay.portal.vulcan.context.Pagination;
import com.liferay.portal.vulcan.dto.Page;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.List;
import java.util.function.Function;

import javax.ws.rs.core.Context;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/category.properties", scope = ServiceScope.PROTOTYPE,
	service = CategoryResource.class
)
public class CategoryResourceImpl implements CategoryResource {

	@Override
	public Category getCategories(Long categoriesId) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<Category> getCategoriesCategoriesPage(
			Long categoriesId, Pagination pagination)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	public Page<Category> getVocabulariesCategoriesPage(
			Long vocabulariesId, Pagination pagination)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	public Category postCategoriesCategories(
			Long categoriesId, Category category)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	public Category postCategoriesCategoriesBatchCreate(
			Long categoriesId, Category category)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	public Category postVocabulariesCategories(
			Long vocabulariesId, Category category)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	public Category postVocabulariesCategoriesBatchCreate(
			Long vocabulariesId, Category category)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	public Category putCategories(Long categoriesId, Category category)
		throws Exception {

		throw new UnsupportedOperationException();
	}

	protected <T, R> List<R> transform(
		List<T> list, Function<T, R> transformFunction) {

		return TransformUtil.transform(list, transformFunction);
	}

	@Context
	protected AcceptLanguage acceptLanguage;

	@Context
	protected Company company;

}