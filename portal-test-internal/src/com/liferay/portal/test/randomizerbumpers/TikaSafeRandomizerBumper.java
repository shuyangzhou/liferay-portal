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

package com.liferay.portal.test.randomizerbumpers;

import com.liferay.portal.kernel.io.DummyWriter;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.test.randomizerbumpers.RandomizerBumper;
import com.liferay.portal.kernel.util.ContentTypes;

import java.util.HashSet;
import java.util.Set;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.WriteOutContentHandler;

/**
 * @author Matthew Tambara
 */
public class TikaSafeRandomizerBumper implements RandomizerBumper<byte[]> {

	public static final TikaSafeRandomizerBumper TEXT_PLAIN_INSTANCE =
		new TikaSafeRandomizerBumper(ContentTypes.TEXT_PLAIN);

	public static Set<byte[]> getZipHeaders() {
		return _ZIP_HEADERS;
	}

	public TikaSafeRandomizerBumper(String contentType) {
		_contentType = contentType;
	}

	@Override
	public boolean accept(byte[] randomValue) {
		if (!_contentType.contains("application")) {
			for (byte[] header : _ZIP_HEADERS) {
				if ((header.length >= randomValue.length) &&
					matches(randomValue, header)) {

					return false;
				}
			}
		}

		try {
			ParseContext parserContext = new ParseContext();

			Parser parser = new AutoDetectParser(new TikaConfig());

			parserContext.set(Parser.class, parser);

			Metadata metadata = new Metadata();

			parser.parse(
				new UnsyncByteArrayInputStream(randomValue),
				new WriteOutContentHandler(new DummyWriter()), metadata,
				parserContext);

			String contentType = metadata.get("Content-Type");

			return contentType.contains(_contentType);
		}
		catch (Exception e) {
			return false;
		}
	}

	protected boolean matches(byte[] randomValue, byte[] header) {
		for (int i = 0; i < header.length; i++) {
			if (randomValue[i] != header[i]) {
				return false;
			}
		}

		return true;
	}

	private static final Set<byte[]> _ZIP_HEADERS;

	static {
		_ZIP_HEADERS = new HashSet<>();

		_ZIP_HEADERS.add(new byte[] {'B', 'Z', 'h'});
		_ZIP_HEADERS.add(new byte[] {31, -117});
		_ZIP_HEADERS.add(new byte[] {-3, 55, 122, 88, 90, 0});
		_ZIP_HEADERS.add(new byte[] {-3, 55, 122, 88, 90, 0});
		_ZIP_HEADERS.add(
			new byte[] {(byte)0xCA, (byte)0xFE, (byte)0xD0, (byte)0x0D});
		_ZIP_HEADERS.add(
			new byte[] {(byte)0xFF, 6, 0, 0, 's', 'N', 'a', 'P', 'p', 'Y'});
		_ZIP_HEADERS.add(new byte[] {(byte)0x1F, (byte)0x9D});
	}

	private final String _contentType;

}