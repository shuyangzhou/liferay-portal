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

package com.liferay.apio.architect.internal.annotation;

import static com.liferay.apio.architect.internal.annotation.representor.StringUtil.toLowercaseSlug;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.execute;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.findPermissionMethodOptional;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.getBodyResourceClassName;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.getParamClasses;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.getResource;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.getReturnClass;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.isListBody;
import static com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil.needsParameterFromBody;
import static com.liferay.apio.architect.internal.annotation.util.AnnotationUtil.findAnnotationInMethodOrInItsAnnotations;
import static com.liferay.apio.architect.internal.wiring.osgi.manager.cache.ManagerCache.INSTANCE;

import static io.leangen.geantyref.GenericTypeReflector.annotate;
import static io.leangen.geantyref.GenericTypeReflector.getTypeParameter;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.some;

import static java.util.Objects.isNull;

import static org.osgi.service.component.annotations.ReferenceCardinality.MULTIPLE;
import static org.osgi.service.component.annotations.ReferencePolicyOption.GREEDY;

import static org.slf4j.LoggerFactory.getLogger;

import com.liferay.apio.architect.annotation.Actions.Action;
import com.liferay.apio.architect.annotation.Vocabulary.Type;
import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.internal.action.ActionSemantics;
import com.liferay.apio.architect.internal.annotation.util.ActionRouterUtil;
import com.liferay.apio.architect.internal.url.ApplicationURL;
import com.liferay.apio.architect.internal.url.ServerURL;
import com.liferay.apio.architect.internal.wiring.osgi.manager.provider.ProviderManager;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.resource.Resource;
import com.liferay.apio.architect.router.ActionRouter;

import io.vavr.CheckedFunction1;
import io.vavr.control.Option;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;

/**
 * Keeps track of all {@link ActionRouter} registered and provides a method to
 * computeActionRouters the actions from each action router.
 *
 * @author Javier Gamarra
 * @review
 */
@Component(service = ActionRouterManager.class)
public class ActionRouterManager {

	@Deactivate
	public void deactivate() {
		INSTANCE.clear();
	}

	public Stream<ActionSemantics> getActionSemantics() {
		return Optional.ofNullable(
			INSTANCE.getActionSemantics(this::_computeActionSemantics)
		).map(
			List::stream
		).orElseGet(
			Stream::empty
		);
	}

	@SuppressWarnings("Convert2MethodRef")
	private void _computeActionSemantics() {
		List<String> list = _providerManager.getMissingProviders(
			_mandatoryClassNames);

		if (!list.isEmpty()) {
			_logger.warn("Missing providers for mandatory classes: {}", list);

			return;
		}

		for (ActionRouter<?> actionRouter : _actionRouters) {
			Class<? extends ActionRouter> clazz = actionRouter.getClass();

			Option<String> optionName = Option.of(
				getTypeParameter(clazz, _actionRouterTypeParameter)
			).map(
				type -> annotate(type)
			).flatMap(
				type -> Option.of(type.getAnnotation(Type.class))
			).map(
				Type::value
			).map(
				string -> toLowercaseSlug(string)
			);

			if (optionName.isEmpty()) {
				_logger.warn("Unable to get name for action router {}", clazz);

				continue;
			}

			String name = optionName.get();

			Stream.of(
				clazz.getMethods()
			).map(
				method -> _getActionSemanticsOption(actionRouter, method, name)
			).filter(
				Option::isDefined
			).map(
				Option::get
			).forEach(
				INSTANCE::addActionSemantics
			);
		}
	}

	private Option<ActionSemantics> _getActionSemanticsOption(
		ActionRouter actionRouter, Method method, String name) {

		Action action = findAnnotationInMethodOrInItsAnnotations(
			method, Action.class);

		if (action == null) {
			return Option.none();
		}

		Form<Object> form = _formManager.getForm(
			getBodyResourceClassName(method));

		if (needsParameterFromBody(method) && isNull(form)) {
			_logger.warn(
				"Unable to find form for method with name: {}",
				method.getName());

			return none();
		}

		Resource resource = getResource(method, name);

		Optional<Method> permissionMethodOptional =
			findPermissionMethodOptional(
				actionRouter.getClass(), resource.getClass(), action.name(),
				action.httpMethod());

		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			resource
		).name(
			action.name()
		).method(
			action.httpMethod()
		).returns(
			getReturnClass(method)
		).permissionFunction(
			permissionMethodOptional.map(
				permissionMethod -> _getPermissionCheckedFunction1(
					permissionMethod, actionRouter)
			).orElse(
				params -> true
			)
		).permissionProvidedClasses(
			permissionMethodOptional.map(
				ActionRouterUtil::getParamClasses
			).orElse(
				new Class<?>[0]
			)
		).executeFunction(
			params -> execute(
				resource, params, array -> method.invoke(actionRouter, array))
		).bodyFunction(
			body -> isListBody(method) ? form.getList(body) : form.get(body)
		).receivesParams(
			getParamClasses(method)
		).annotatedWith(
			method.getDeclaredAnnotations()
		).build();

		return some(actionSemantics);
	}

	private CheckedFunction1<List<?>, Boolean> _getPermissionCheckedFunction1(
		Method permissionMethod, ActionRouter actionRouter) {

		return arguments -> (Boolean)permissionMethod.invoke(
			actionRouter, arguments.toArray(new Object[0]));
	}

	private static final TypeVariable<Class<ActionRouter>>
		_actionRouterTypeParameter = ActionRouter.class.getTypeParameters()[0];
	private static final List<String> _mandatoryClassNames = Arrays.asList(
		ApplicationURL.class.getName(), Credentials.class.getName(),
		Pagination.class.getName(), ServerURL.class.getName());

	@Reference(
		cardinality = MULTIPLE, policyOption = GREEDY,
		service = ActionRouter.class
	)
	private List<ActionRouter<?>> _actionRouters;

	@Reference
	private FormManager _formManager;

	private final Logger _logger = getLogger(getClass());

	@Reference
	private ProviderManager _providerManager;

}