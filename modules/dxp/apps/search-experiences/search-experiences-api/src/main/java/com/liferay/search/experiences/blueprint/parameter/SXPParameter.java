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

import com.liferay.search.experiences.blueprint.parameter.exception.SXPParameterException;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public interface SXPParameter {

	public boolean accept(EvaluationVisitor evaluationVisitor)
		throws SXPParameterException;

	public String accept(
			ToStringVisitor toStringVisitor, Map<String, String> options)
		throws Exception;

	public String getName();

	public String getTemplateVariable();

	public Object getValue();

	public boolean isTemplateVariable();

	public interface EvaluationVisitor {

		public boolean visit(BooleanSXPParameter booleanSXPParameter)
			throws SXPParameterException;

		public boolean visit(DateSXPParameter dateSXPParameter)
			throws SXPParameterException;

		public boolean visit(DoubleSXPParameter doubleSXPParameter)
			throws SXPParameterException;

		public boolean visit(FloatSXPParameter floatSXPParameter)
			throws SXPParameterException;

		public boolean visit(IntegerArraySXPParameter integerArraySXPParameter)
			throws SXPParameterException;

		public boolean visit(IntegerSXPParameter integerSXPParameter)
			throws SXPParameterException;

		public boolean visit(LongArraySXPParameter longArraySXPParameter)
			throws SXPParameterException;

		public boolean visit(LongSXPParameter longSXPParameter)
			throws SXPParameterException;

		public boolean visit(StringArraySXPParameter stringArraySXPParameter)
			throws SXPParameterException;

		public boolean visit(StringSXPParameter stringSXPParameter)
			throws SXPParameterException;

	}

	public interface ToStringVisitor {

		public String visit(
				BooleanSXPParameter booleanSXPParameter,
				Map<String, String> options)
			throws Exception;

		public String visit(
				DateSXPParameter dateSXPParameter, Map<String, String> options)
			throws Exception;

		public String visit(
				DoubleSXPParameter parameter, Map<String, String> options)
			throws Exception;

		public String visit(
				FloatSXPParameter floatSXPParameter,
				Map<String, String> options)
			throws Exception;

		public String visit(
				IntegerArraySXPParameter integerArraySXPParameter,
				Map<String, String> options)
			throws Exception;

		public String visit(
				IntegerSXPParameter integerSXPParameter,
				Map<String, String> options)
			throws Exception;

		public String visit(
				LongArraySXPParameter longArraySXPParameter,
				Map<String, String> options)
			throws Exception;

		public String visit(
				LongSXPParameter longSXPParameter, Map<String, String> options)
			throws Exception;

		public String visit(
				StringArraySXPParameter stringArraySXPParameter,
				Map<String, String> options)
			throws Exception;

		public String visit(
				StringSXPParameter stringSXPParameter,
				Map<String, String> options)
			throws Exception;

	}

}