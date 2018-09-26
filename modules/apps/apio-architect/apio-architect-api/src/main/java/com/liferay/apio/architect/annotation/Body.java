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

package com.liferay.apio.architect.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotating an action parameter with {@link Body} indicates that the parameter
 * must be obtained from the request body.
 *
 * <p>
 * This annotation should always be used on an {@link
 * com.liferay.apio.architect.router.ActionRouter} method parameter with a class
 * annotated with {@link Vocabulary.Type}.
 * </p>
 *
 * @author Alejandro Hernández
 * @review
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface Body {
}