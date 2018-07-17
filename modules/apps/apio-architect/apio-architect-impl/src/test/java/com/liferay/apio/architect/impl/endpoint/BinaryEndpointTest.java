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

import static com.liferay.apio.architect.test.util.result.TryMatchers.aFailTry;

import static java.nio.charset.StandardCharsets.UTF_8;

import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import com.liferay.apio.architect.file.BinaryFile;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.impl.representor.RepresentorImpl.BuilderImpl;
import com.liferay.apio.architect.impl.single.model.SingleModelImpl;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.representor.Representor.Builder;
import com.liferay.apio.architect.single.model.SingleModel;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class BinaryEndpointTest {

	@Test
	public void testBinaryEndpointWithEmptyRepresentorReturnsFailure() {
		BinaryEndpoint binaryEndpoint = _getBinaryEndpoint(null);

		Try<BinaryFile> binaryFileTry =
			binaryEndpoint.getCollectionItemBinaryFileTry("", "", "");

		assertThat(binaryFileTry, is(aFailTry()));
	}

	@Test
	public void testBinaryEndpointWithFailSingleModelReturnsFailure() {
		BinaryEndpoint binaryEndpoint = new BinaryEndpoint(
			__ -> _representor(),
			(name, id) -> Try.fail(new IllegalArgumentException()));

		Try<BinaryFile> binaryFileTry =
			binaryEndpoint.getCollectionItemBinaryFileTry("", "", "");

		assertThat(binaryFileTry, is(aFailTry()));
	}

	@Test
	public void testBinaryEndpointWithNoPresentIdReturnFailure() {
		BinaryEndpoint binaryEndpoint = _getBinaryEndpoint(_representor());

		Try<BinaryFile> binaryFileTry =
			binaryEndpoint.getCollectionItemBinaryFileTry("", "", "");

		assertThat(binaryFileTry, is(aFailTry()));
	}

	@Test
	public void testBinaryEndpointWithValidFunctionsReturnsBinaryFile() {
		BinaryEndpoint binaryEndpoint = _getBinaryEndpoint(_representor());

		Try<BinaryFile> binaryFileTry =
			binaryEndpoint.getCollectionItemBinaryFileTry("", "", "binary");

		BinaryFile binaryFile = binaryFileTry.getUnchecked();

		assertThat(_readBinaryFile(binaryFile), is("Apio"));
		assertThat(binaryFile.getMimeType(), is("image/png"));
		assertThat(binaryFile.getSize(), is(0L));
	}

	@Test
	public void testFunctionsReceiveResourceNamesAndBinaryId() {
		List<String> names = new ArrayList<>();

		BinaryEndpoint binaryEndpoint = new BinaryEndpoint(
			name -> {
				names.add(name);

				return _representor();
			},
			(name, id) -> {
				names.add(name);
				names.add(id);

				return Try.success(_getSingleModel(name));
			});

		binaryEndpoint.getCollectionItemBinaryFileTry("a", "b", "binary");

		assertThat(names, contains("a", "a", "b"));
	}

	private static BinaryEndpoint _getBinaryEndpoint(
		Representor<Object> representor) {

		return new BinaryEndpoint(
			__ -> representor,
			(name, id) -> Try.success(_getSingleModel(name)));
	}

	private static SingleModel<Object> _getSingleModel(String name) {
		return new SingleModelImpl<>("apio", name, emptyList());
	}

	private static Representor<Object> _representor() {
		Builder<Object, Object> builder = new BuilderImpl<>(null);

		return builder.types(
			""
		).identifier(
			identity()
		).addBinary(
			"binary",
			__ -> new BinaryFile(
				new ByteArrayInputStream("Apio".getBytes(UTF_8)), 0L,
				"image/png")
		).build();
	}

	private String _readBinaryFile(BinaryFile binaryFile) {
		return Try.fromFallibleWithResources(
			() -> new BufferedReader(new InputStreamReader(
				binaryFile.getInputStream())),
			BufferedReader::readLine).getUnchecked();
	}

}