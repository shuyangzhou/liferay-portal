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

import static com.liferay.apio.architect.form.FieldType.BOOLEAN;
import static com.liferay.apio.architect.form.FieldType.BOOLEAN_LIST;
import static com.liferay.apio.architect.form.FieldType.DATE;
import static com.liferay.apio.architect.form.FieldType.DATE_LIST;
import static com.liferay.apio.architect.form.FieldType.DOUBLE;
import static com.liferay.apio.architect.form.FieldType.DOUBLE_LIST;
import static com.liferay.apio.architect.form.FieldType.FILE;
import static com.liferay.apio.architect.form.FieldType.FILE_LIST;
import static com.liferay.apio.architect.form.FieldType.LINKED_MODEL;
import static com.liferay.apio.architect.form.FieldType.LINKED_MODEL_LIST;
import static com.liferay.apio.architect.form.FieldType.LONG;
import static com.liferay.apio.architect.form.FieldType.LONG_LIST;
import static com.liferay.apio.architect.form.FieldType.NESTED_MODEL;
import static com.liferay.apio.architect.form.FieldType.NESTED_MODEL_LIST;
import static com.liferay.apio.architect.form.FieldType.STRING;
import static com.liferay.apio.architect.form.FieldType.STRING_LIST;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalBoolean;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalBooleanList;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalDate;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalDateList;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalDouble;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalDoubleList;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalFile;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalFileList;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalFormFieldStream;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalLinkedModel;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalLinkedModelList;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalLong;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalLongList;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalString;
import static com.liferay.apio.architect.internal.form.FormUtil.getOptionalStringList;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredBoolean;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredBooleanList;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredDate;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredDateList;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredDouble;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredDoubleList;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredFile;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredFileList;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredFormFieldStream;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredLinkedModel;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredLinkedModelList;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredLong;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredLongList;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredString;
import static com.liferay.apio.architect.internal.form.FormUtil.getRequiredStringList;

import com.liferay.apio.architect.alias.IdentifierFunction;
import com.liferay.apio.architect.alias.form.FormBuilderFunction;
import com.liferay.apio.architect.file.BinaryFile;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.form.FieldType;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.form.FormField;
import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.language.AcceptLanguage;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.BadRequestException;

/**
 * @author Alejandro Hernández
 */
public class FormImpl<T> implements Form<T> {

	@Override
	public T get(Body body) {
		T t = _supplier.get();

		_getFormFields(this, body, t);

		return t;
	}

	@Override
	public String getDescription(AcceptLanguage acceptLanguage) {
		return _descriptionFunction.apply(acceptLanguage);
	}

	@Override
	public List<FormField> getFormFields() {
		return _getFormFields(this);
	}

	@Override
	public String getId() {
		return _id;
	}

	@Override
	public List<T> getList(Body body) {
		Optional<List<Body>> optional = body.getBodyMembersOptional();

		List<Body> bodyMembers = optional.orElseThrow(
			() -> new BadRequestException("Body does not contain members"));

		Stream<Body> stream = bodyMembers.stream();

		return stream.map(
			this::get
		).collect(
			Collectors.toList()
		);
	}

	@Override
	public String getTitle(AcceptLanguage acceptLanguage) {
		return _titleFunction.apply(acceptLanguage);
	}

	public static class BuilderImpl<T>
		implements Form.Builder<T>, Form.Builder.FieldStep<T>,
				   Form.Builder.ConstructorStep<T>,
				   Form.Builder.DescriptionStep<T> {

		/**
		 * Creates a new builder with empty paths.
		 *
		 * @return the new builder
		 */
		public static <T> BuilderImpl<T> empty() {
			return new BuilderImpl<>(Collections.emptyList(), __ -> null);
		}

		public BuilderImpl(
			List<String> paths,
			IdentifierFunction<?> pathToIdentifierFunction) {

			_form = new FormImpl<>(paths, pathToIdentifierFunction);
		}

		@Override
		public FieldStep<T> addOptionalBoolean(
			String key, BiConsumer<T, Boolean> biConsumer) {

			_form._optionalBooleans.put(
				key, t -> aBoolean -> biConsumer.accept(t, aBoolean));

			return this;
		}

		@Override
		public FieldStep<T> addOptionalBooleanList(
			String key, BiConsumer<T, List<Boolean>> biConsumer) {

			_form._optionalBooleanLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public FieldStep<T> addOptionalDate(
			String key, BiConsumer<T, Date> biConsumer) {

			_form._optionalDates.put(
				key, t -> date -> biConsumer.accept(t, date));

			return this;
		}

		@Override
		public FieldStep<T> addOptionalDateList(
			String key, BiConsumer<T, List<Date>> biConsumer) {

			_form._optionalDateLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public FieldStep<T> addOptionalDouble(
			String key, BiConsumer<T, Double> biConsumer) {

			_form._optionalDoubles.put(
				key, t -> aDouble -> biConsumer.accept(t, aDouble));

			return this;
		}

		@Override
		public FieldStep<T> addOptionalDoubleList(
			String key, BiConsumer<T, List<Double>> biConsumer) {

			_form._optionalDoubleLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public FieldStep<T> addOptionalFile(
			String key, BiConsumer<T, BinaryFile> biConsumer) {

			_form._optionalFiles.put(
				key, t -> binaryFile -> biConsumer.accept(t, binaryFile));

			return this;
		}

		@Override
		public FieldStep<T> addOptionalFileList(
			String key, BiConsumer<T, List<BinaryFile>> biConsumer) {

			_form._optionalFileLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public <C> FieldStep<T> addOptionalLinkedModel(
			String key, Class<? extends Identifier<C>> aClass,
			BiConsumer<T, C> biConsumer) {

			_form._optionalLinkedModel.put(
				key, t -> c -> biConsumer.accept(t, (C)c));

			return this;
		}

		@Override
		public <C> FieldStep<T> addOptionalLinkedModelList(
			String key, Class<? extends Identifier<C>> aClass,
			BiConsumer<T, List<C>> biConsumer) {

			_form._optionalLinkedModelList.put(
				key, t -> c -> biConsumer.accept(t, (List)c));

			return this;
		}

		@Override
		public FieldStep<T> addOptionalLong(
			String key, BiConsumer<T, Long> biConsumer) {

			_form._optionalLongs.put(
				key, t -> aLong -> biConsumer.accept(t, aLong));

			return this;
		}

		@Override
		public FieldStep<T> addOptionalLongList(
			String key, BiConsumer<T, List<Long>> biConsumer) {

			_form._optionalLongLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public <U> FieldStep<T> addOptionalNestedModel(
			String key, FormBuilderFunction<U> formBuilderFunction,
			BiConsumer<T, U> biConsumer) {

			_form._optionalNestedModel.put(
				key, t -> object -> biConsumer.accept(t, (U)object));

			_form._formBuilderFunctionsMap.put(key, formBuilderFunction);

			return this;
		}

		@Override
		public <U> FieldStep<T> addOptionalNestedModelList(
			String key, FormBuilderFunction<U> formBuilderFunction,
			BiConsumer<T, List<U>> biConsumer) {

			_form._optionalNestedModelLists.put(
				key, t -> list -> biConsumer.accept(t, (List<U>)list));

			_form._formBuilderFunctionsMap.put(key, formBuilderFunction);

			return this;
		}

		@Override
		public FieldStep<T> addOptionalString(
			String key, BiConsumer<T, String> biConsumer) {

			_form._optionalStrings.put(
				key, t -> string -> biConsumer.accept(t, string));

			return this;
		}

		@Override
		public FieldStep<T> addOptionalStringList(
			String key, BiConsumer<T, List<String>> biConsumer) {

			_form._optionalStringLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredBoolean(
			String key, BiConsumer<T, Boolean> biConsumer) {

			_form._requiredBooleans.put(
				key, t -> aBoolean -> biConsumer.accept(t, aBoolean));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredBooleanList(
			String key, BiConsumer<T, List<Boolean>> biConsumer) {

			_form._requiredBooleanLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredDate(
			String key, BiConsumer<T, Date> biConsumer) {

			_form._requiredDates.put(
				key, t -> date -> biConsumer.accept(t, date));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredDateList(
			String key, BiConsumer<T, List<Date>> biConsumer) {

			_form._requiredDateLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredDouble(
			String key, BiConsumer<T, Double> biConsumer) {

			_form._requiredDoubles.put(
				key, t -> aDouble -> biConsumer.accept(t, aDouble));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredDoubleList(
			String key, BiConsumer<T, List<Double>> biConsumer) {

			_form._requiredDoubleLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredFile(
			String key, BiConsumer<T, BinaryFile> biConsumer) {

			_form._requiredFiles.put(
				key, t -> binaryFile -> biConsumer.accept(t, binaryFile));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredFileList(
			String key, BiConsumer<T, List<BinaryFile>> biConsumer) {

			_form._requiredFileLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public <C> FieldStep<T> addRequiredLinkedModel(
			String key, Class<? extends Identifier<C>> aClass,
			BiConsumer<T, C> biConsumer) {

			_form._requiredLinkedModel.put(
				key, t -> c -> biConsumer.accept(t, (C)c));

			return this;
		}

		@Override
		public <C> FieldStep<T> addRequiredLinkedModelList(
			String key, Class<? extends Identifier<C>> aClass,
			BiConsumer<T, List<C>> biConsumer) {

			_form._requiredLinkedModelList.put(
				key, t -> c -> biConsumer.accept(t, (List)c));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredLong(
			String key, BiConsumer<T, Long> biConsumer) {

			_form._requiredLongs.put(
				key, t -> aLong -> biConsumer.accept(t, aLong));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredLongList(
			String key, BiConsumer<T, List<Long>> biConsumer) {

			_form._requiredLongLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public <U> FieldStep<T> addRequiredNestedModel(
			String key, FormBuilderFunction<U> formBuilderFunction,
			BiConsumer<T, U> biConsumer) {

			_form._requiredNestedModel.put(
				key, t -> object -> biConsumer.accept(t, (U)object));

			_form._formBuilderFunctionsMap.put(key, formBuilderFunction);

			return this;
		}

		@Override
		public <U> FieldStep<T> addRequiredNestedModelList(
			String key, FormBuilderFunction<U> formBuilderFunction,
			BiConsumer<T, List<U>> biConsumer) {

			_form._optionalNestedModelLists.put(
				key, t -> list -> biConsumer.accept(t, (List<U>)list));

			_form._formBuilderFunctionsMap.put(key, formBuilderFunction);

			return this;
		}

		@Override
		public FieldStep<T> addRequiredString(
			String key, BiConsumer<T, String> biConsumer) {

			_form._requiredStrings.put(
				key, t -> string -> biConsumer.accept(t, string));

			return this;
		}

		@Override
		public FieldStep<T> addRequiredStringList(
			String key, BiConsumer<T, List<String>> biConsumer) {

			_form._requiredStringLists.put(
				key, t -> list -> biConsumer.accept(t, list));

			return this;
		}

		@Override
		public Form<T> build() {
			return _form;
		}

		@Override
		public FieldStep<T> constructor(Supplier<T> supplier) {
			_form._supplier = supplier;

			return this;
		}

		@Override
		public ConstructorStep<T> description(
			Function<AcceptLanguage, String> descriptionFunction) {

			_form._descriptionFunction = descriptionFunction;

			return this;
		}

		@Override
		public DescriptionStep<T> title(
			Function<AcceptLanguage, String> titleFunction) {

			_form._titleFunction = titleFunction;

			return this;
		}

		private final FormImpl<T> _form;

	}

	private FormImpl(
		List<String> paths, IdentifierFunction<?> pathToIdentifierFunction) {

		_id = String.join("/", paths);
		_pathToIdentifierFunction = pathToIdentifierFunction;
	}

	private List<FormField> _getFormFields(FormImpl<T> form) {
		return Stream.of(
			getOptionalFormFieldStream(form._optionalBooleans, BOOLEAN),
			getOptionalFormFieldStream(
				form._optionalBooleanLists, BOOLEAN_LIST),
			getOptionalFormFieldStream(form._optionalDates, DATE),
			getOptionalFormFieldStream(form._optionalDateLists, DATE_LIST),
			getOptionalFormFieldStream(form._optionalDoubles, DOUBLE),
			getOptionalFormFieldStream(form._optionalDoubleLists, DOUBLE_LIST),
			getOptionalFormFieldStream(form._optionalFiles, FILE),
			getOptionalFormFieldStream(form._optionalFileLists, FILE_LIST),
			getOptionalFormFieldStream(form._optionalLinkedModel, LINKED_MODEL),
			getOptionalFormFieldStream(
				form._optionalLinkedModelList, LINKED_MODEL_LIST),
			getOptionalFormFieldStream(form._optionalLongs, LONG),
			getOptionalFormFieldStream(form._optionalLongLists, LONG_LIST),
			_getNestedModelFormFieldStream(
				form._optionalNestedModel, NESTED_MODEL, false),
			_getNestedModelFormFieldStream(
				form._optionalNestedModelLists, NESTED_MODEL_LIST, false),
			getOptionalFormFieldStream(form._optionalStrings, STRING),
			getOptionalFormFieldStream(form._optionalStringLists, STRING_LIST),
			getRequiredFormFieldStream(form._requiredBooleans, BOOLEAN),
			getRequiredFormFieldStream(
				form._requiredBooleanLists, BOOLEAN_LIST),
			getRequiredFormFieldStream(form._requiredDates, DATE),
			getRequiredFormFieldStream(form._requiredDateLists, DATE_LIST),
			getRequiredFormFieldStream(form._requiredDoubles, DOUBLE),
			getRequiredFormFieldStream(form._requiredDoubleLists, DOUBLE_LIST),
			getRequiredFormFieldStream(form._requiredFiles, FILE),
			getRequiredFormFieldStream(form._requiredFileLists, FILE_LIST),
			getRequiredFormFieldStream(form._requiredLinkedModel, LINKED_MODEL),
			getRequiredFormFieldStream(
				form._requiredLinkedModelList, LINKED_MODEL_LIST),
			getRequiredFormFieldStream(form._requiredLongs, LONG),
			getRequiredFormFieldStream(form._requiredLongLists, LONG_LIST),
			_getNestedModelFormFieldStream(
				form._requiredNestedModel, NESTED_MODEL, true),
			_getNestedModelFormFieldStream(
				form._requiredNestedModelLists, NESTED_MODEL_LIST, true),
			getRequiredFormFieldStream(form._requiredStrings, STRING),
			getRequiredFormFieldStream(form._requiredStringLists, STRING_LIST)
		).flatMap(
			Function.identity()
		).collect(
			Collectors.toList()
		);
	}

	private <U> void _getFormFields(FormImpl<U> formImpl, Body body, U u) {
		formImpl._optionalBooleans.forEach(getOptionalBoolean(body, u));
		formImpl._optionalBooleanLists.forEach(getOptionalBooleanList(body, u));
		formImpl._optionalDates.forEach(getOptionalDate(body, u));
		formImpl._optionalDateLists.forEach(getOptionalDateList(body, u));
		formImpl._optionalDoubles.forEach(getOptionalDouble(body, u));
		formImpl._optionalDoubleLists.forEach(getOptionalDoubleList(body, u));
		formImpl._optionalFiles.forEach(getOptionalFile(body, u));
		formImpl._optionalFileLists.forEach(getOptionalFileList(body, u));
		formImpl._optionalLinkedModel.forEach(
			getOptionalLinkedModel(body, u, _pathToIdentifierFunction));
		formImpl._optionalLinkedModelList.forEach(
			getOptionalLinkedModelList(body, u, _pathToIdentifierFunction));
		formImpl._optionalLongs.forEach(getOptionalLong(body, u));
		formImpl._optionalLongLists.forEach(getOptionalLongList(body, u));
		formImpl._optionalNestedModel.forEach(
			formImpl._getOptionalNestedModel(body, u));
		formImpl._optionalNestedModelLists.forEach(
			formImpl._getOptionalNestedModelList(body, u));
		formImpl._optionalStrings.forEach(getOptionalString(body, u));
		formImpl._optionalStringLists.forEach(getOptionalStringList(body, u));
		formImpl._requiredBooleans.forEach(getRequiredBoolean(body, u));
		formImpl._requiredBooleanLists.forEach(getRequiredBooleanList(body, u));
		formImpl._requiredDates.forEach(getRequiredDate(body, u));
		formImpl._requiredDateLists.forEach(getRequiredDateList(body, u));
		formImpl._requiredDoubles.forEach(getRequiredDouble(body, u));
		formImpl._requiredDoubleLists.forEach(getRequiredDoubleList(body, u));
		formImpl._requiredFiles.forEach(getRequiredFile(body, u));
		formImpl._requiredFileLists.forEach(getRequiredFileList(body, u));
		formImpl._requiredLinkedModel.forEach(
			getRequiredLinkedModel(body, u, _pathToIdentifierFunction));
		formImpl._requiredLinkedModelList.forEach(
			getRequiredLinkedModelList(body, u, _pathToIdentifierFunction));
		formImpl._requiredLongs.forEach(getRequiredLong(body, u));
		formImpl._requiredLongLists.forEach(getRequiredLongList(body, u));
		formImpl._requiredNestedModel.forEach(
			formImpl._getRequiredNestedModel(body, u));
		formImpl._requiredNestedModelLists.forEach(
			formImpl._getRequiredNestedModelList(body, u));
		formImpl._requiredStrings.forEach(getRequiredString(body, u));
		formImpl._requiredStringLists.forEach(getRequiredStringList(body, u));
	}

	private <V> FormImpl<V> _getNestedForm(String key) {
		List<String> paths = Collections.singletonList(_id);

		Builder<V> builder = new BuilderImpl<>(
			paths, _pathToIdentifierFunction);

		FormBuilderFunction<V> formBuilderFunction =
			(FormBuilderFunction<V>)_formBuilderFunctionsMap.get(key);

		return (FormImpl<V>)formBuilderFunction.apply(builder);
	}

	private <U, V> void _getNestedModel(
		Body body, U u, String key, Function<U, Consumer<V>> consumerFunction,
		boolean required) {

		FormImpl<V> nestedForm = _getNestedForm(key);

		V v = nestedForm._supplier.get();

		Optional<Body> bodyOptional = body.getNestedBodyOptional(key);

		if (bodyOptional.isPresent()) {
			_getFormFields(nestedForm, bodyOptional.get(), v);
		}
		else if (required) {
			throw new BadRequestException("Field \"" + key + "\" is required");
		}

		consumerFunction.apply(
			u
		).accept(
			v
		);
	}

	private Stream<FormField> _getNestedModelFormFieldStream(
		Map<String, ?> nestedModelMap, FieldType fieldType, boolean required) {

		Set<String> nestedModelKeys = nestedModelMap.keySet();

		Stream<String> stream = nestedModelKeys.stream();

		return stream.map(
			key -> new FormFieldImpl(
				key, required, fieldType, _getNestedForm(key)));
	}

	private <U, V> void _getNestedModelList(
		Body body, U u, String key,
		Function<U, Consumer<List<V>>> consumerFunction, boolean required) {

		FormImpl<V> nestedForm = _getNestedForm(key);

		Optional<List<Body>> bodyListOptional = body.getNestedBodyListOptional(
			key);

		if (bodyListOptional.isPresent()) {
			List<Body> bodies = bodyListOptional.get();

			Stream<Body> stream = bodies.stream();

			List<V> models = stream.map(
				nestedBody -> {
					V v = nestedForm._supplier.get();

					_getFormFields(nestedForm, nestedBody, v);

					return v;
				}
			).collect(
				Collectors.toList()
			);

			consumerFunction.apply(
				u
			).accept(
				models
			);
		}
		else if (required) {
			throw new BadRequestException("Field \"" + key + "\" is required");
		}
	}

	private <U> BiConsumer
		<String, Function<U, Consumer<Object>>> _getOptionalNestedModel(
			Body body, U u) {

		return (key, consumerFunction) -> _getNestedModel(
			body, u, key, consumerFunction, false);
	}

	private <U> BiConsumer<String, Function<U, Consumer<List<Object>>>>
		_getOptionalNestedModelList(Body body, U u) {

		return (key, consumerFunction) -> _getNestedModelList(
			body, u, key, consumerFunction, false);
	}

	private <U> BiConsumer
		<String, Function<U, Consumer<Object>>> _getRequiredNestedModel(
			Body body, U u) {

		return (key, consumerFunction) -> _getNestedModel(
			body, u, key, consumerFunction, true);
	}

	private <U> BiConsumer<String, Function<U, Consumer<List<Object>>>>
		_getRequiredNestedModelList(Body body, U u) {

		return (key, consumerFunction) -> _getNestedModelList(
			body, u, key, consumerFunction, true);
	}

	private Function<AcceptLanguage, String> _descriptionFunction;
	private final Map<String, FormBuilderFunction<?>> _formBuilderFunctionsMap =
		new HashMap<>();
	private final String _id;
	private final Map<String, Function<T, Consumer<List<Boolean>>>>
		_optionalBooleanLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<Boolean>>>
		_optionalBooleans = new HashMap<>();
	private final Map<String, Function<T, Consumer<List<Date>>>>
		_optionalDateLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<Date>>> _optionalDates =
		new HashMap<>();
	private final Map<String, Function<T, Consumer<List<Double>>>>
		_optionalDoubleLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<Double>>> _optionalDoubles =
		new HashMap<>();
	private final Map<String, Function<T, Consumer<List<BinaryFile>>>>
		_optionalFileLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<BinaryFile>>>
		_optionalFiles = new HashMap<>();
	private final Map<String, Function<T, Consumer<?>>> _optionalLinkedModel =
		new HashMap<>();
	private final Map<String, Function<T, Consumer<List<?>>>>
		_optionalLinkedModelList = new HashMap<>();
	private final Map<String, Function<T, Consumer<List<Long>>>>
		_optionalLongLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<Long>>> _optionalLongs =
		new HashMap<>();
	private final Map<String, Function<T, Consumer<Object>>>
		_optionalNestedModel = new HashMap<>();
	private final Map<String, Function<T, Consumer<List<Object>>>>
		_optionalNestedModelLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<List<String>>>>
		_optionalStringLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<String>>> _optionalStrings =
		new HashMap<>();
	private final IdentifierFunction<?> _pathToIdentifierFunction;
	private final Map<String, Function<T, Consumer<List<Boolean>>>>
		_requiredBooleanLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<Boolean>>>
		_requiredBooleans = new HashMap<>();
	private final Map<String, Function<T, Consumer<List<Date>>>>
		_requiredDateLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<Date>>> _requiredDates =
		new HashMap<>();
	private final Map<String, Function<T, Consumer<List<Double>>>>
		_requiredDoubleLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<Double>>> _requiredDoubles =
		new HashMap<>();
	private final Map<String, Function<T, Consumer<List<BinaryFile>>>>
		_requiredFileLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<BinaryFile>>>
		_requiredFiles = new HashMap<>();
	private final Map<String, Function<T, Consumer<?>>> _requiredLinkedModel =
		new HashMap<>();
	private final Map<String, Function<T, Consumer<List<?>>>>
		_requiredLinkedModelList = new HashMap<>();
	private final Map<String, Function<T, Consumer<List<Long>>>>
		_requiredLongLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<Long>>> _requiredLongs =
		new HashMap<>();
	private final Map<String, Function<T, Consumer<Object>>>
		_requiredNestedModel = new HashMap<>();
	private final Map<String, Function<T, Consumer<List<Object>>>>
		_requiredNestedModelLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<List<String>>>>
		_requiredStringLists = new HashMap<>();
	private final Map<String, Function<T, Consumer<String>>> _requiredStrings =
		new HashMap<>();
	private Supplier<T> _supplier;
	private Function<AcceptLanguage, String> _titleFunction;

}