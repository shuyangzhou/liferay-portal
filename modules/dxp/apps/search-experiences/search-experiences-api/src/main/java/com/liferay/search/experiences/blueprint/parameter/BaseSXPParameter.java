/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.search.experiences.blueprint.parameter;

import com.liferay.petra.string.StringBundler;

/**
 * @author Petteri Karttunen
 */
public abstract class BaseSXPParameter implements SXPParameter {

	public BaseSXPParameter(String name, boolean templateVariable) {
		this.name = name;
		this.templateVariable = templateVariable;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTemplateVariable() {
		if (isTemplateVariable()) {
			return "${" + name + "}";
		}

		return null;
	}

	@Override
	public boolean isTemplateVariable() {
		return templateVariable;
	}

	@Override
	public String toString() {
		return toString(String.valueOf(getValue()));
	}

	protected String toString(String valueString) {
		Class<?> clazz = getClass();

		return StringBundler.concat(
			"{className=", clazz.getSimpleName(), ", name=", name,
			", templateVariable=", templateVariable, ", value=", valueString,
			"}");
	}

	protected final String name;
	protected final boolean templateVariable;

}