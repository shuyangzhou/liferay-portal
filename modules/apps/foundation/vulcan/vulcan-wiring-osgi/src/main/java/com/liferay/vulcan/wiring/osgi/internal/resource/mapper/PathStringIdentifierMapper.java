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

package com.liferay.vulcan.wiring.osgi.internal.resource.mapper;

import com.liferay.vulcan.error.VulcanDeveloperError.UnresolvableURI;
import com.liferay.vulcan.resource.identifier.StringIdentifier;
import com.liferay.vulcan.resource.identifier.mapper.PathIdentifierMapper;
import com.liferay.vulcan.uri.Path;
import com.liferay.vulcan.wiring.osgi.manager.CollectionResourceManager;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Maps a {@link Path} to a {@link StringIdentifier}, and vice versa.
 *
 * <p>
 * {@code StringIdentifier} can then be provided as a parameter in the methods
 * of {@link com.liferay.vulcan.resource.builder.RoutesBuilder}.
 * </p>
 *
 * @author Alejandro Hernández
 */
@Component(immediate = true)
public class PathStringIdentifierMapper
	implements PathIdentifierMapper<StringIdentifier> {

	@Override
	public StringIdentifier map(Path path) {
		return path::getId;
	}

	@Override
	public <U> Path map(
		StringIdentifier stringIdentifier, Class<U> modelClass) {

		String className = modelClass.getName();

		Optional<String> optional = _collectionResourceManager.getNameOptional(
			className);

		String name = optional.orElseThrow(
			() -> new UnresolvableURI(className));

		return new Path(name, stringIdentifier.getId());
	}

	@Reference
	private CollectionResourceManager _collectionResourceManager;

}