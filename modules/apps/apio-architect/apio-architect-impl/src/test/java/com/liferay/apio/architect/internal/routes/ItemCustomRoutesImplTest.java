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
import static com.liferay.apio.architect.internal.routes.RoutesTestUtil.IDENTIFIER_TO_PATH_FUNCTION;
import static com.liferay.apio.architect.internal.routes.RoutesTestUtil.REQUEST_PROVIDE_FUNCTION;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

import com.liferay.apio.architect.alias.routes.CustomItemFunction;
import com.liferay.apio.architect.custom.actions.CustomRoute;
import com.liferay.apio.architect.custom.actions.PutRoute;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.ItemRoutes.Builder;
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
public class ItemCustomRoutesImplTest {

	@Before
	public void setUp() {
		_neededProviders = new TreeSet<>();

		_builder = new ItemRoutesImpl.BuilderImpl<>(
			"name", REQUEST_PROVIDE_FUNCTION, _neededProviders::add,
			__ -> null, IDENTIFIER_TO_PATH_FUNCTION, __ -> Optional.of("name"));
	}

	@Test
	public void testEmptyBuilderBuildsEmptyRoutes() {
		ItemRoutes<Map<String, Object>, Long> collectionRoutes =
			_builder.build();

		Optional<Map<String, CustomItemFunction<?, Long>>>
			customItemFunctionsOptional =
				collectionRoutes.getCustomItemFunctionsOptional();

		assertThat(customItemFunctionsOptional, is(not(emptyOptional())));

		Map<String, CustomItemFunction<?, Long>> customRouteFunctions =
			customItemFunctionsOptional.get();

		assertThat(customRouteFunctions, is(anEmptyMap()));

		Map<String, CustomRoute> customRoutes =
			collectionRoutes.getCustomRoutes();

		assertThat(customRoutes, is(anEmptyMap()));
	}

	@Test
	public void testFourParameterBuilderMethodsCreatesValidRoutes() {
		ItemRoutes<Map<String, Object>, Long> itemRoutes =
			_builder.addCustomRoute(
				_customRoute, this::_testAndReturnFourParameterCustomRoute,
				String.class, Long.class, Boolean.class, Integer.class,
				_identifier.getClass(), (credentials, aLong) -> true,
				FORM_BUILDER_FUNCTION
			).build();

		assertThat(
			_neededProviders,
			contains(
				Boolean.class.getName(), Integer.class.getName(),
				Long.class.getName(), String.class.getName()));

		_testCollectionRoutes(itemRoutes);
	}

	@Test
	public void testOneParameterBuilderMethodsCreatesValidRoutes() {
		ItemRoutes<Map<String, Object>, Long> itemRoutes =
			_builder.addCustomRoute(
				_customRoute, this::_testAndReturnOneParameterCustomRoute,
				String.class, _identifier.getClass(),
				(credentials, aLong) -> true, FORM_BUILDER_FUNCTION
			).build();

		assertThat(_neededProviders, contains(String.class.getName()));

		_testCollectionRoutes(itemRoutes);
	}

	@Test
	public void testThreeParameterCustomRouteCreatesValidRoutes() {
		ItemRoutes<Map<String, Object>, Long> itemRoutes =
			_builder.addCustomRoute(
				_customRoute, this::_testAndReturnThreeParameterCustomRoute,
				String.class, Long.class, Boolean.class, _identifier.getClass(),
				(credentials, aLong) -> true, FORM_BUILDER_FUNCTION
			).build();

		assertThat(
			_neededProviders,
			contains(
				Boolean.class.getName(), Long.class.getName(),
				String.class.getName()));

		_testCollectionRoutes(itemRoutes);
	}

	@Test
	public void testTwoParameterCustomRouteCreatesValidRoutes() {
		ItemRoutes<Map<String, Object>, Long> itemRoutes =
			_builder.addCustomRoute(
				_customRoute, this::_testAndReturnTwoParameterCustomRoute,
				String.class, Long.class, _identifier.getClass(),
				(credentials, aLong) -> true, FORM_BUILDER_FUNCTION
			).build();

		assertThat(
			_neededProviders,
			contains(Long.class.getName(), String.class.getName()));

		_testCollectionRoutes(itemRoutes);
	}

	@Test
	public void testZeroParameterCustomRouteCreatesValidRoutes() {
		ItemRoutes<Map<String, Object>, Long> itemRoutes =
			_builder.addCustomRoute(
				_customRoute, this::_testAndReturnNoParameterCustomRoute,
				_identifier.getClass(), (credentials, aLong) -> true,
				FORM_BUILDER_FUNCTION
			).build();

		assertThat(_neededProviders.size(), is(0));

		_testCollectionRoutes(itemRoutes);
	}

	private String _testAndReturnFourParameterCustomRoute(
		Long identifier, Map<String, Object> body, String string, Long aLong,
		Boolean aBoolean, Integer integer) {

		assertThat(integer, is(2017));

		return _testAndReturnThreeParameterCustomRoute(
			identifier, body, string, aLong, aBoolean);
	}

	private String _testAndReturnNoParameterCustomRoute(
		Long identifier, Map<String, Object> body) {

		Optional<String> optional = _body.getValueOptional("key");

		assertThat(body.get("key"), is(optional.get()));

		assertThat(identifier, is(42L));

		return "Apio";
	}

	private String _testAndReturnOneParameterCustomRoute(
		Long identifier, Map<String, Object> body, String string) {

		assertThat(string, is("Apio"));

		return _testAndReturnNoParameterCustomRoute(identifier, body);
	}

	private String _testAndReturnThreeParameterCustomRoute(
		Long identifier, Map<String, Object> body, String string, Long aLong,
		Boolean aBoolean) {

		assertThat(aBoolean, is(true));

		return _testAndReturnTwoParameterCustomRoute(
			identifier, body, string, aLong);
	}

	private String _testAndReturnTwoParameterCustomRoute(
		Long identifier, Map<String, Object> body, String string, Long aLong) {

		assertThat(aLong, is(42L));

		return _testAndReturnOneParameterCustomRoute(identifier, body, string);
	}

	private void _testCollectionRoutes(
		ItemRoutes<Map<String, Object>, Long> collectionRoutes) {

		Optional<ItemRoutes<Map<String, Object>, Long>>
			collectionRoutesOptional = Optional.of(collectionRoutes);

		Map map = collectionRoutesOptional.map(
			ItemRoutes::getCustomRoutes
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
			ItemRoutes::getCustomItemFunctionsOptional
		).map(
			stringCustomPageFunctionMap ->
				stringCustomPageFunctionMap.get(_CUSTOM_ROUTE_NAME)
		).get(
		).apply(
			null
		).apply(
			42L
		).apply(
			_body
		).getUnchecked();

		assertThat(singleModel.getResourceName(), is("name"));
		assertThat(singleModel.getModel(), is("Apio"));
	}

	private static final String _CUSTOM_ROUTE_NAME = "read";

	private final Body _body = __ -> Optional.of("Apio");
	private Builder<Map<String, Object>, Long> _builder;

	private CustomRoute _customRoute = new PutRoute() {

		@Override
		public String getName() {
			return "read";
		}

	};

	private final Identifier<Long> _identifier = new Identifier<Long>() {
	};
	private Set<String> _neededProviders;

}