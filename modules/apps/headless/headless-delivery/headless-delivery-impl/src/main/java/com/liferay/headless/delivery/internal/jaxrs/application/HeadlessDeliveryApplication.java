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

package com.liferay.headless.delivery.internal.jaxrs.application;

import javax.annotation.Generated;

import javax.ws.rs.core.Application;

import com.liferay.batch.engine.BatchEngineTaskItemClassRegistry;
import com.liferay.headless.delivery.dto.v1_0.BlogPosting;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Javier Gamarra
 * @generated
 */
@Component(
	property = {
		"osgi.jaxrs.application.base=/headless-delivery",
		"osgi.jaxrs.extension.select=(osgi.jaxrs.name=Liferay.Vulcan)",
		"osgi.jaxrs.name=Liferay.Headless.Delivery"
	},
	service = Application.class
)
@Generated("")
public class HeadlessDeliveryApplication extends Application {

	@Activate
	public void activate() {
		_batchEngineTaskItemClassRegistry.register(BlogPosting.class);
	}

	@Deactivate
	public void deactivate() {
		_batchEngineTaskItemClassRegistry.unregister(BlogPosting.class);
	}

	@Reference
	private BatchEngineTaskItemClassRegistry _batchEngineTaskItemClassRegistry;

}
