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

package com.liferay.apio.architect.internal.form;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.liferay.apio.architect.file.BinaryFile;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.functional.Try;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.BadRequestException;

/**
 * @author Javier Gamarra
 */
public class JSONBodyImpl implements Body {

	public JSONBodyImpl(JsonNode jsonNode) {
		_jsonNode = jsonNode;
	}

	@Override
	public Optional<List<Body>> getBodyMembersOptional() {
		return Try.success(
			_jsonNode
		).filter(
			JsonNode::isArray
		).map(
			ArrayNode.class::cast
		).map(
			JSONBodyImpl::_getJsonElements
		).map(
			List::stream
		).map(
			stream -> stream.filter(
				JsonNode::isObject
			).map(
				ObjectNode.class::cast
			).map(
				JSONBodyImpl::new
			).map(
				Body.class::cast
			).collect(
				Collectors.toList()
			)
		).map(
			Optional::ofNullable
		).orElseThrow(
			() -> new BadRequestException("Body is not a valid JSON Array")
		);
	}

	@Override
	public Optional<List<BinaryFile>> getFileListOptional(String key) {
		return Optional.empty();
	}

	@Override
	public Optional<BinaryFile> getFileOptional(String key) {
		return Optional.empty();
	}

	@Override
	public Optional<List<Body>> getNestedBodyListOptional(String key) {
		return Optional.ofNullable(
			_jsonNode.get(key)
		).filter(
			JsonNode::isArray
		).map(
			ArrayNode.class::cast
		).map(
			JSONBodyImpl::_getJsonElements
		).map(
			List::stream
		).map(
			stream -> stream.filter(
				JsonNode::isObject
			).map(
				JSONBodyImpl::new
			).collect(
				Collectors.toList()
			)
		);
	}

	@Override
	public Optional<Body> getNestedBodyOptional(String key) {
		return Optional.ofNullable(
			_jsonNode.get(key)
		).filter(
			JsonNode::isObject
		).map(
			ObjectNode.class::cast
		).map(
			JSONBodyImpl::new
		);
	}

	@Override
	public Optional<List<String>> getValueListOptional(String key) {
		return Optional.ofNullable(
			_jsonNode.get(key)
		).filter(
			JsonNode::isArray
		).map(
			ArrayNode.class::cast
		).map(
			JSONBodyImpl::_getJsonElements
		).map(
			List::stream
		).map(
			stream -> stream.filter(
				JsonNode::isValueNode
			).map(
				JsonNode::asText
			).collect(
				Collectors.toList()
			)
		);
	}

	@Override
	public Optional<String> getValueOptional(String key) {
		return Optional.ofNullable(
			_jsonNode.get(key)
		).filter(
			JsonNode::isValueNode
		).map(
			JsonNode::asText
		);
	}

	private static List<JsonNode> _getJsonElements(ArrayNode arrayNode) {
		List<JsonNode> jsonNodes = new ArrayList<>();

		Iterator<JsonNode> iterator = arrayNode.iterator();

		iterator.forEachRemaining(jsonNodes::add);

		return jsonNodes;
	}

	private final JsonNode _jsonNode;

}