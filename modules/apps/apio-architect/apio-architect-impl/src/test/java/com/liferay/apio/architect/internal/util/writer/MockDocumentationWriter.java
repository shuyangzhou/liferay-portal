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

package com.liferay.apio.architect.internal.util.writer;

import static com.liferay.apio.architect.internal.util.writer.MockWriterUtil.getRequestInfo;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import com.liferay.apio.architect.documentation.contributor.CustomDocumentation;
import com.liferay.apio.architect.internal.action.ActionSemantics;
import com.liferay.apio.architect.internal.documentation.Documentation;
import com.liferay.apio.architect.internal.documentation.contributor.CustomDocumentationImpl;
import com.liferay.apio.architect.internal.message.json.DocumentationMessageMapper;
import com.liferay.apio.architect.internal.util.model.RootModel;
import com.liferay.apio.architect.internal.util.representor.MockRepresentorCreator;
import com.liferay.apio.architect.internal.writer.DocumentationWriter;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.resource.Resource;
import com.liferay.apio.architect.resource.Resource.Item;
import com.liferay.apio.architect.resource.Resource.Paged;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Provides methods that test {@code DocumentationMessageMapper} objects.
 *
 * <p>
 * This class shouldn't be instantiated.
 * </p>
 *
 * @author Alejandro Hernández
 */
public class MockDocumentationWriter {

	/**
	 * Writes a {@code Documentation} object.
	 *
	 * @param  documentationMessageMapper the {@code DocumentationMessageMapper}
	 *         to use for writing the JSON object
	 * @return the string containing the JSON object
	 */
	public static String write(
		DocumentationMessageMapper documentationMessageMapper) {

		CustomDocumentation.Builder customDocumentationBuilder =
			new CustomDocumentationImpl.BuilderImpl();

		customDocumentationBuilder.addDescription(
			"binary1", "binary description");
		customDocumentationBuilder.addLocalizedDescription(
			"root/retrieve", __ -> "retrieve description");

		Representor<RootModel> rootModelRepresentor =
			MockRepresentorCreator.createRootModelRepresentor(false);

		Map<String, Representor> root = Collections.singletonMap(
			"root", rootModelRepresentor);

		CustomDocumentation customDocumentation =
			customDocumentationBuilder.build();

		Stream<Resource> stream = Stream.of(Paged.of("root"), Item.of("root"));

		Documentation documentation = new Documentation(
			() -> Optional.of(() -> "Title"),
			() -> Optional.of(() -> "Description"),
			() -> Optional.of(() -> "Entrypoint"), () -> root, stream,
			MockDocumentationWriter::_getActionSemantics,
			() -> customDocumentation);

		DocumentationWriter documentationWriter = DocumentationWriter.create(
			builder -> builder.documentation(
				documentation
			).documentationMessageMapper(
				documentationMessageMapper
			).requestInfo(
				getRequestInfo()
			).typeFunction(
				identifierClass -> Optional.of(identifierClass.getSimpleName())
			).build());

		return documentationWriter.write();
	}

	private static Stream<ActionSemantics> _getActionSemantics(
		Resource resource) {

		if (Paged.of("root").equals(resource)) {
			return _rootActionSemantics.stream();
		}

		if (Item.of("root").equals(resource)) {
			return _itemActionSemantics.stream();
		}

		return Stream.empty();
	}

	private MockDocumentationWriter() {
		throw new UnsupportedOperationException();
	}

	private static final List<ActionSemantics> _itemActionSemantics;
	private static final List<ActionSemantics> _rootActionSemantics;

	static {
		ActionSemantics retrievePage = ActionSemantics.ofResource(
			Paged.of("root")
		).name(
			"retrieve"
		).method(
			"GET"
		).returns(
			Page.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).build();

		_rootActionSemantics = singletonList(retrievePage);

		ActionSemantics retrieve = ActionSemantics.ofResource(
			Item.of("root")
		).name(
			"retrieve"
		).method(
			"GET"
		).returns(
			SingleModel.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).build();

		ActionSemantics remove = ActionSemantics.ofResource(
			Item.of("root")
		).name(
			"remove"
		).method(
			"DELETE"
		).returns(
			Void.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).build();

		ActionSemantics replace = ActionSemantics.ofResource(
			Item.of("root")
		).name(
			"replace"
		).method(
			"PUT"
		).returns(
			SingleModel.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).build();

		_itemActionSemantics = asList(retrieve, remove, replace);
	}

}