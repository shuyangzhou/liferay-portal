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

package com.liferay.apio.architect.internal.routes;

import static com.liferay.apio.architect.internal.routes.RoutesTestUtil.FORM_BUILDER_FUNCTION;
import static com.liferay.apio.architect.internal.routes.RoutesTestUtil.REQUEST_PROVIDE_FUNCTION;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

import com.liferay.apio.architect.alias.routes.CustomPageFunction;
import com.liferay.apio.architect.custom.actions.CustomRoute;
import com.liferay.apio.architect.custom.actions.GetRoute;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.internal.routes.CollectionRoutesImpl.BuilderImpl;
import com.liferay.apio.architect.pagination.Pagination;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.CollectionRoutes.Builder;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Javier Gamarra
 */
public class CollectionCustomRoutesImplTest {

	@Before
	public void setUp() {
		_neededProviders = new TreeSet<>();

		_builder = new BuilderImpl<>(
			"name", REQUEST_PROVIDE_FUNCTION, _neededProviders::add,
			__ -> null, __ -> null, __ -> Optional.of("name"));
	}

	@Test
	public void testEmptyBuilderBuildsEmptyRoutes() {
		CollectionRoutes<Map<String, Object>, ?> collectionRoutes =
			_builder.build();

		Optional<Map<String, CustomPageFunction<?>>>
			customPageFunctionsOptional =
				collectionRoutes.getCustomPageFunctionsOptional();

		assertThat(customPageFunctionsOptional, is(not(emptyOptional())));

		Map<String, CustomPageFunction<?>> customRouteFunctions =
			customPageFunctionsOptional.get();

		assertThat(customRouteFunctions, is(anEmptyMap()));

		Map<String, CustomRoute> customRoutes =
			collectionRoutes.getCustomRoutes();

		assertThat(customRoutes, is(anEmptyMap()));
	}

	@Test
	public void testFourParameterBuilderMethodsCreatesValidRoutes() {
		CollectionRoutes<Map<String, Object>, ?> collectionRoutes =
			_builder.addCustomRoute(
				_customRoute, this::_testAndReturnFourParameterCustomRoute,
				String.class, Long.class, Boolean.class, Integer.class,
				_identifier.getClass(), __ -> true, FORM_BUILDER_FUNCTION
			).build();

		assertThat(
			_neededProviders,
			contains(
				Boolean.class.getName(), Integer.class.getName(),
				Long.class.getName(), String.class.getName()));

		_testCollectionRoutes(collectionRoutes);
	}

	@Test
	public void testOneParameterBuilderMethodsCreatesValidRoutes() {
		CollectionRoutes<Map<String, Object>, ?> collectionRoutes =
			_builder.addCustomRoute(
				_customRoute, this::_testAndReturnOneParameterCustomRoute,
				String.class, _identifier.getClass(), __ -> true,
				FORM_BUILDER_FUNCTION
			).build();

		assertThat(_neededProviders, contains(String.class.getName()));

		_testCollectionRoutes(collectionRoutes);
	}

	@Test
	public void testThreeParameterCustomRouteCreatesValidRoutes() {
		CollectionRoutes<Map<String, Object>, ?> collectionRoutes =
			_builder.addCustomRoute(
				_customRoute, this::_testAndReturnThreeParameterCustomRoute,
				String.class, Long.class, Boolean.class, _identifier.getClass(),
				__ -> true, FORM_BUILDER_FUNCTION
			).build();

		assertThat(
			_neededProviders,
			contains(
				Boolean.class.getName(), Long.class.getName(),
				String.class.getName()));

		_testCollectionRoutes(collectionRoutes);
	}

	@Test
	public void testTwoParameterCustomRouteCreatesValidRoutes() {
		CollectionRoutes<Map<String, Object>, ?> collectionRoutes =
			_builder.addCustomRoute(
				_customRoute, this::_testAndReturnTwoParameterCustomRoute,
				String.class, Long.class, _identifier.getClass(),
				__ -> true, FORM_BUILDER_FUNCTION
			).build();

		assertThat(
			_neededProviders,
			contains(Long.class.getName(), String.class.getName()));

		_testCollectionRoutes(collectionRoutes);
	}

	@Test
	public void testZeroParameterCustomRouteCreatesValidRoutes() {
		CollectionRoutes<Map<String, Object>, ?> collectionRoutes =
			_builder.addCustomRoute(
				_customRoute, this::_testAndReturnNoParameterCustomRoute,
				_identifier.getClass(), __ -> true, FORM_BUILDER_FUNCTION
			).build();

		assertThat(_neededProviders.size(), is(0));

		_testCollectionRoutes(collectionRoutes);
	}

	private String _testAndReturnFourParameterCustomRoute(
		Pagination pagination, Map<String, Object> body, String string,
		Long aLong, Boolean aBoolean, Integer integer) {

		assertThat(integer, is(2017));

		return _testAndReturnThreeParameterCustomRoute(
			pagination, body, string, aLong, aBoolean);
	}

	private String _testAndReturnNoParameterCustomRoute(
		Pagination pagination, Map<String, Object> body) {

		Optional<String> optional = _body.getValueOptional("key");

		assertThat(body.get("key"), is(optional.get()));

		assertThat(pagination.getItemsPerPage(), is(4));

		return "Apio";
	}

	private String _testAndReturnOneParameterCustomRoute(
		Pagination pagination, Map<String, Object> body, String string) {

		assertThat(string, is("Apio"));

		return _testAndReturnNoParameterCustomRoute(pagination, body);
	}

	private String _testAndReturnThreeParameterCustomRoute(
		Pagination pagination, Map<String, Object> body, String string,
		Long aLong, Boolean aBoolean) {

		assertThat(aBoolean, is(true));

		return _testAndReturnTwoParameterCustomRoute(
			pagination, body, string, aLong);
	}

	private String _testAndReturnTwoParameterCustomRoute(
		Pagination pagination, Map<String, Object> body, String string,
		Long aLong) {

		assertThat(aLong, is(42L));

		return _testAndReturnOneParameterCustomRoute(pagination, body, string);
	}

	private void _testCollectionRoutes(
		CollectionRoutes<Map<String, Object>, ?> collectionRoutes) {

		Optional<CollectionRoutes<Map<String, Object>, ?>>
			collectionRoutesOptional = Optional.of(collectionRoutes);

		Map map = collectionRoutesOptional.map(
			CollectionRoutes::getCustomRoutes
		).map(
			stringCustomRouteMap -> stringCustomRouteMap.get(_CUSTOM_ROUTE_NAME)
		).flatMap(
			CustomRoute::getFormOptional
		).map(
			form -> {
				assertThat(form.getId(), is("p/name/" + _CUSTOM_ROUTE_NAME));

				return (Map)form.get(_body);
			}
		).get();

		Optional<String> valueOptional = _body.getValueOptional("key");

		assertThat(map.get("key"), is(valueOptional.get()));

		SingleModel singleModel = collectionRoutesOptional.flatMap(
			CollectionRoutes::getCustomPageFunctionsOptional
		).map(
			stringCustomPageFunctionMap -> (CustomPageFunction<?>)
				stringCustomPageFunctionMap.get(_CUSTOM_ROUTE_NAME)
		).get(
		).apply(
			null
		).apply(
			_body
		).getUnchecked();

		assertThat(singleModel.getResourceName(), is("name"));
		assertThat(singleModel.getModel(), is("Apio"));
	}

	private static final String _CUSTOM_ROUTE_NAME = "read";

	private final Body _body = __ -> Optional.of("Apio");
	private Builder<Map<String, Object>, ?> _builder;

	private CustomRoute _customRoute = new GetRoute() {

		@Override
		public String getName() {
			return "read";
		}

	};

	private final Identifier<Long> _identifier = new Identifier<Long>() {
	};
	private Set<String> _neededProviders;

}