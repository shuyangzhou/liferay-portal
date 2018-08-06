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

package com.liferay.apio.architect.impl.endpoint;

import static com.liferay.apio.architect.impl.endpoint.EndpointsTestUtil.emptyNestedCollectionRoutes;
import static com.liferay.apio.architect.impl.endpoint.EndpointsTestUtil.nestedCollectionRoutes;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;

import com.liferay.apio.architect.batch.BatchResult;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.impl.representor.RepresentorImpl;
import com.liferay.apio.architect.impl.single.model.SingleModelImpl;
import com.liferay.apio.architect.representor.Representor;

import java.util.Optional;

import javax.ws.rs.NotAllowedException;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class BatchEndpointTest {

	@Test(expected = NotAllowedException.class)
	public void testEmptyCollectionRoutesSupplierProvokesFailure()
		throws Exception {

		BatchEndpoint<Object> batchEndpoint = BatchEndpointBuilder.name(
			"name"
		).httpServletRequest(
			null
		).singleModelFunction(
			__ -> null
		).representorSupplier(
			() -> null
		).collectionRoutesSupplier(
			EndpointsTestUtil::emptyCollectionRoutes
		).nestedCollectionRoutesFunction(
			__ -> null
		).build();

		Try<BatchResult<Object>> batchResultTry =
			batchEndpoint.addBatchCollectionItems(__ -> Optional.empty());

		batchResultTry.get();
	}

	@Test(expected = NotAllowedException.class)
	public void testEmptyNestedCollectionRoutesSupplierProvokesFailure()
		throws Exception {

		BatchEndpoint<Object> batchEndpoint = BatchEndpointBuilder.name(
			"name"
		).httpServletRequest(
			null
		).singleModelFunction(
			__ -> null
		).representorSupplier(
			() -> null
		).collectionRoutesSupplier(
			() -> null
		).nestedCollectionRoutesFunction(
			__ -> emptyNestedCollectionRoutes()
		).build();

		Try<BatchResult<Object>> batchResultTry =
			batchEndpoint.addBatchNestedCollectionItems(
				"", "", __ -> Optional.empty());

		batchResultTry.get();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailureCollectionRoutesSupplierProvokesFailure()
		throws Exception {

		BatchEndpoint<Object> batchEndpoint = BatchEndpointBuilder.name(
			"name"
		).httpServletRequest(
			null
		).singleModelFunction(
			__ -> null
		).representorSupplier(
			() -> null
		).collectionRoutesSupplier(
			() -> {
				throw new IllegalArgumentException();
			}
		).nestedCollectionRoutesFunction(
			__ -> null
		).build();

		Try<BatchResult<Object>> batchResultTry =
			batchEndpoint.addBatchCollectionItems(__ -> Optional.empty());

		batchResultTry.get();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFailureNestedCollectionRoutesSupplierProvokesFailure()
		throws Exception {

		BatchEndpoint<Object> batchEndpoint = BatchEndpointBuilder.name(
			"name"
		).httpServletRequest(
			null
		).singleModelFunction(
			__ -> null
		).representorSupplier(
			() -> null
		).collectionRoutesSupplier(
			() -> null
		).nestedCollectionRoutesFunction(
			__ -> {
				throw new IllegalArgumentException();
			}
		).build();

		Try<BatchResult<Object>> batchResultTry =
			batchEndpoint.addBatchNestedCollectionItems(
				"", "", __ -> Optional.empty());

		batchResultTry.get();
	}

	@Test
	public void testValidBatchEndpointCreatesValidBatchResult() {
		BatchEndpoint<Object> batchEndpoint = BatchEndpointBuilder.name(
			"name"
		).httpServletRequest(
			null
		).singleModelFunction(
			__ -> Try.success(
				new SingleModelImpl<>("Apio", "name", emptyList()))
		).representorSupplier(
			BatchEndpointTest::_representor
		).collectionRoutesSupplier(
			EndpointsTestUtil::collectionRoutes
		).nestedCollectionRoutesFunction(
			__ -> nestedCollectionRoutes()
		).build();

		Body body = Body.create(
			asList(__ -> Optional.of("Apio"), __ -> Optional.of("Hypermedia")));

		Try<BatchResult<Object>> batchResultTry =
			batchEndpoint.addBatchCollectionItems(body);

		BatchResult<Object> batchResult = batchResultTry.getUnchecked();

		assertThat(batchResult.resourceName, is("name"));
		assertThat(
			batchResult.getIdentifiers(), contains("Apio", "Hypermedia"));
	}

	@Test
	public void testValidBatchEndpointCreatesValidNestedBatchResult() {
		BatchEndpoint<Object> batchEndpoint = BatchEndpointBuilder.name(
			"name"
		).httpServletRequest(
			null
		).singleModelFunction(
			id -> {
				assertThat(id, is("id"));

				return Try.success(
					new SingleModelImpl<>("Apio", "name", emptyList()));
			}
		).representorSupplier(
			BatchEndpointTest::_representor
		).collectionRoutesSupplier(
			EndpointsTestUtil::collectionRoutes
		).nestedCollectionRoutesFunction(
			name -> {
				assertThat(name, is("nested"));

				return nestedCollectionRoutes();
			}
		).build();

		Body body = Body.create(
			asList(__ -> Optional.of("Apio"), __ -> Optional.of("Hypermedia")));

		Try<BatchResult<Object>> batchResultTry =
			batchEndpoint.addBatchNestedCollectionItems("id", "nested", body);

		BatchResult<Object> batchResult = batchResultTry.getUnchecked();

		assertThat(batchResult.resourceName, is("nestedName"));
		assertThat(
			batchResult.getIdentifiers(), contains("Apio", "Hypermedia"));
	}

	private static Representor<Object> _representor() {
		Representor.Builder<Object, Object> builder =
			new RepresentorImpl.BuilderImpl<>(null);

		return builder.types(
			""
		).identifier(
			string -> string
		).build();
	}

}