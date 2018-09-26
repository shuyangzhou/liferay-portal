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

package com.liferay.apio.architect.sample.internal.action;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import static javax.ws.rs.HttpMethod.PUT;

import com.liferay.apio.architect.annotation.Actions;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that a method performs the action of subscribing a person to
 * something.
 *
 * <p>
 * This annotation must be used on a method that lives inside a class
 * implementing {@link com.liferay.apio.architect.router.ActionRouter}.
 * </p>
 *
 * @author Alejandro Hernández
 * @review
 */
@Actions.Action(httpMethod = PUT, name = "subscribe")
@Retention(RUNTIME)
@Target(METHOD)
public @interface Subscribe {
}