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

package com.liferay.apio.architect.test.util.json;

import static java.nio.charset.StandardCharsets.UTF_8;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import com.liferay.apio.architect.error.APIError;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.impl.message.json.BatchResultMessageMapper;
import com.liferay.apio.architect.impl.message.json.DocumentationMessageMapper;
import com.liferay.apio.architect.impl.message.json.EntryPointMessageMapper;
import com.liferay.apio.architect.impl.message.json.ErrorMessageMapper;
import com.liferay.apio.architect.impl.message.json.FormMessageMapper;
import com.liferay.apio.architect.impl.message.json.MessageMapper;
import com.liferay.apio.architect.impl.message.json.PageMessageMapper;
import com.liferay.apio.architect.impl.message.json.SingleModelMessageMapper;
import com.liferay.apio.architect.impl.writer.ErrorWriter;
import com.liferay.apio.architect.test.util.internal.writer.MockBatchResultWriter;
import com.liferay.apio.architect.test.util.internal.writer.MockDocumentationWriter;
import com.liferay.apio.architect.test.util.internal.writer.MockEntryPointWriter;
import com.liferay.apio.architect.test.util.internal.writer.MockFormWriter;
import com.liferay.apio.architect.test.util.internal.writer.MockPageWriter;
import com.liferay.apio.architect.test.util.internal.writer.MockSingleModelWriter;
import com.liferay.apio.architect.test.util.model.RootModel;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONException;

/**
 * Tests message mappers of the same media type by using the snapshot testing
 * technique.
 *
 * @author Alejandro Hernández
 */
public class MessageMapperTesterBuilder {

	/**
	 * Provides information about the test files' path. This class uses this
	 * path to get the files, and uses the {@link
	 * ClassLoader#getResource(String)} as a fallback.
	 *
	 * @param  path the path
	 * @return the builder's next step
	 */
	public static MediaTypeStep path(Path path) {
		return new MediaTypeStep(path);
	}

	public static class MediaTypeStep {

		/**
		 * Sets the media type that the message mappers should have.
		 *
		 * @param  mediaType the media type
		 * @return the builder's next step
		 */
		public MessageMapperStep mediaType(String mediaType) {
			return new MessageMapperStep(_path, mediaType);
		}

		private MediaTypeStep(Path path) {
			_path = path;
		}

		private final Path _path;

	}

	@SuppressWarnings("UnusedReturnValue")
	public static class MessageMapperStep {

		/**
		 * Creates a {@code batch.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file contains
		 * the representation created by the provided {@code
		 * BatchResultMessageMapper}.
		 *
		 * @param  batchResultMessageMapper the {@code BatchResultMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep createBatchResultFile(
			BatchResultMessageMapper<String> batchResultMessageMapper) {

			String result = MockBatchResultWriter.write(
				batchResultMessageMapper);

			_createFile(result, "batch");

			return this;
		}

		/**
		 * Creates a {@code documentation.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file contains
		 * the representation created by the provided {@code
		 * DocumentationMessageMapper}.
		 *
		 * @param  documentationMessageMapper the {@code
		 *         DocumentationMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep createDocumentationFile(
			DocumentationMessageMapper documentationMessageMapper) {

			String result = MockDocumentationWriter.write(
				documentationMessageMapper);

			_createFile(result, "documentation");

			return this;
		}

		/**
		 * Creates a {@code error.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file contains
		 * the representation created by the provided {@code
		 * ErrorMessageMapper}.
		 *
		 * @param  errorMessageMapper the {@code ErrorMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep createErrorFile(
			ErrorMessageMapper errorMessageMapper) {

			String actual = ErrorWriter.writeError(
				errorMessageMapper, _MOCK_API_ERROR);

			_createFile(actual, "error");

			return this;
		}

		/**
		 * Creates a {@code form.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file contains
		 * the representation created by the provided {@code FormMessageMapper}.
		 *
		 * @param  formMessageMapper the {@code FormMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep createFormFile(
			FormMessageMapper formMessageMapper) {

			String result = MockFormWriter.write(formMessageMapper);

			_createFile(result, "form");

			return this;
		}

		/**
		 * Creates a {@code page.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file contains
		 * the representation created by the provided {@code PageMessageMapper}.
		 *
		 * @param  pageMessageMapper the {@code PageMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep createPageFile(
			PageMessageMapper<RootModel> pageMessageMapper) {

			String result = MockPageWriter.write(pageMessageMapper);

			_createFile(result, "page");

			return this;
		}

		/**
		 * Creates a {@code single_model.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file contains
		 * the representation created by the provided {@code
		 * SingleModelMessageMapper}.
		 *
		 * @param  singleModelMessageMapper the {@code SingleModelMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep createSingleModelFile(
			SingleModelMessageMapper<RootModel> singleModelMessageMapper) {

			String result = MockSingleModelWriter.write(
				singleModelMessageMapper);

			_createFile(result, "single_model");

			return this;
		}

		/**
		 * Validates that the output of the provided {@code
		 * BatchResultMessageMapper} matches the content of {@code
		 * /src/test/resources/batch.json}.
		 *
		 * @param  batchResultMessageMapper the {@code BatchResultMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep validateBatchResultMessageMapper(
			BatchResultMessageMapper<String> batchResultMessageMapper) {

			String result = MockBatchResultWriter.write(
				batchResultMessageMapper);

			_validateMessageMapper(batchResultMessageMapper, result, "batch");

			return this;
		}

		/**
		 * Validates that the output of the provided {@code
		 * DocumentationMessageMapper} matches the content of {@code
		 * /src/test/resources/documentation.json}.
		 *
		 * @param  documentationMessageMapper the {@code
		 *         DocumentationMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep validateDocumentationMessageMapper(
			DocumentationMessageMapper documentationMessageMapper) {

			String result = MockDocumentationWriter.write(
				documentationMessageMapper);

			_validateMessageMapper(
				documentationMessageMapper, result, "documentation");

			return this;
		}

		/**
		 * Validates that the output of the provided {@code
		 * EntryPointMessageMapper} matches the content of {@code
		 * /src/test/resources/entrypoint.json}.
		 *
		 * @param  entryPointMessageMapper the {@code EntryPointMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep validateEntryPointMessageMapper(
			EntryPointMessageMapper entryPointMessageMapper) {

			String actual = MockEntryPointWriter.write(entryPointMessageMapper);

			_validateMessageMapper(
				entryPointMessageMapper, actual, "entrypoint");

			return this;
		}

		/**
		 * Validates that the output of the provided {@code ErrorMessageMapper}
		 * matches the content of {@code /src/test/resources/error.json}.
		 *
		 * @param  errorMessageMapper the {@code ErrorMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep validateErrorMessageMapper(
			ErrorMessageMapper errorMessageMapper) {

			String actual = ErrorWriter.writeError(
				errorMessageMapper, _MOCK_API_ERROR);

			_validateMessageMapper(errorMessageMapper, actual, "error");

			return this;
		}

		/**
		 * Validates that the output of the provided {@code FormMessageMapper}
		 * matches the content of {@code /src/test/resources/form.json}.
		 *
		 * @param  formMessageMapper the {@code FormMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep validateFormMessageMapper(
			FormMessageMapper formMessageMapper) {

			String result = MockFormWriter.write(formMessageMapper);

			_validateMessageMapper(formMessageMapper, result, "form");

			return this;
		}

		/**
		 * Validates that the output of the provided {@code PageMessageMapper}
		 * matches the content of {@code /src/test/resources/page.json}.
		 *
		 * @param  pageMessageMapper the {@code PageMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep validatePageMessageMapper(
			PageMessageMapper<RootModel> pageMessageMapper) {

			String result = MockPageWriter.write(pageMessageMapper);

			_validateMessageMapper(pageMessageMapper, result, "page");

			return this;
		}

		/**
		 * Validates that the output of the provided {@code
		 * SingleModelMessageMapper} matches the content of {@code
		 * /src/test/resources/single_model.json}.
		 *
		 * @param  singleModelMessageMapper the {@code SingleModelMessageMapper}
		 * @return the builder's next step
		 */
		public MessageMapperStep validateSingleModelMessageMapper(
			SingleModelMessageMapper<RootModel> singleModelMessageMapper) {

			String result = MockSingleModelWriter.write(
				singleModelMessageMapper);

			_validateMessageMapper(
				singleModelMessageMapper, result, "single_model");

			return this;
		}

		private MessageMapperStep(Path path, String mediaType) {
			_path = path;
			_mediaType = mediaType;
		}

		private void _createFile(String actual, String fileName) {
			Path newPath = Paths.get(_path.toString(), fileName + ".json.auto");

			try {
				Files.write(newPath, actual.getBytes());
			}
			catch (IOException ioe) {
				throw new AssertionError(
					"Unable to create the file with path: " + newPath);
			}

			throw new AssertionError("File does not exist. Creating...");
		}

		private void _validateMessageMapper(
			MessageMapper messageMapper, String actual, String fileName) {

			assertThat(messageMapper.getMediaType(), is(_mediaType));

			String file = fileName + ".json";

			String stringPath = _path.toString();

			String folder = stringPath.substring(
				stringPath.lastIndexOf(File.separator) + 1);

			String expected = Try.success(
				stringPath
			).map(
				path -> Paths.get(path, file)
			).map(
				Files::readAllBytes
			).recoverWith(
				__ -> Try.fromFallible(
					this::getClass
				).map(
					Class::getClassLoader
				).map(
					classLoader -> classLoader.getResource(
						folder + File.separator + file)
				).map(
					URL::getPath
				).map(
					File::new
				).map(
					File::toPath
				).map(
					Files::readAllBytes
				)
			).map(
				bytes -> new String(bytes, UTF_8)
			).orElseThrow(
				() -> new AssertionError("Unable to found the file: " + file)
			);

			try {
				assertEquals(expected, actual, true);
			}
			catch (JSONException jsone) {
				throw new AssertionError(jsone.getMessage());
			}
		}

		private static final APIError _MOCK_API_ERROR = new APIError(
			new IllegalArgumentException(), "A title", "A description",
			"A type", 404);

		private final String _mediaType;
		private final Path _path;

	}

}