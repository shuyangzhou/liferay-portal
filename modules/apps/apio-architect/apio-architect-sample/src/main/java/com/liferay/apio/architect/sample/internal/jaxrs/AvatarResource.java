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

package com.liferay.apio.architect.sample.internal.jaxrs;

import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.sample.internal.model.PersonModel;

import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;

/**
 * Provides an example of a JAX-RS resource to show how to hook new resources to
 * the {@code ApioApplication}. This class also provides an endpoint for serving
 * {@link PersonModel} avatars that will be added to their {@code Representor}
 * via {@link
 * com.liferay.apio.architect.representor.BaseRepresentor.BaseBuilder.BaseFirstStep#addRelativeURL}.
 *
 * @author Alejandro Hernández
 */
@Component(
	immediate = true,
	property = {
		"osgi.jaxrs.application.select=(liferay.apio.architect.application=true)",
		"osgi.jaxrs.resource=true"
	},
	service = Object.class
)
@Path("/images")
public class AvatarResource {

	/**
	 * Returns a {@code Response} containing the avatar of the person identified
	 * with the provided ID.
	 *
	 * @param  id the ID of the {@code PersonModel}
	 * @return the response containing the person's avatar data
	 */
	@GET
	@Path("{id}")
	public Response getAvatar(@PathParam("id") long id) {
		return Try.fromOptional(
			() -> PersonModel.get(id), NotFoundException::new
		).map(
			PersonModel::getAvatar
		).map(
			URL::new
		).map(
			URL::openStream
		).map(
			inputStream -> Response.ok(
				inputStream
			).type(
				"image/jpg"
			).build()
		).orElseThrow(
			NotFoundException::new
		);
	}

}