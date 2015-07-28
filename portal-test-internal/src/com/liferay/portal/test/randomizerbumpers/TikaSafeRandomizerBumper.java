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
import com.liferay.portal.kernel.test.randomizerbumpers.RandomizerBumper;

import java.io.ByteArrayInputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.WriteOutContentHandler;

/**
 * @author Matthew Tambara
 */
public class TikaSafeRandomizerBumper implements RandomizerBumper<String> {

	public static final TikaSafeRandomizerBumper INSTANCE =
		new TikaSafeRandomizerBumper();

	@Override
	public boolean accept(String randomValue) {
		Parser parser = new AutoDetectParser();

		ParseContext parserContext = new ParseContext();

		parserContext.set(Parser.class, parser);

		Metadata metadata = new Metadata();
		try {
			parser.parse(
				new ByteArrayInputStream(randomValue.getBytes()),
				new WriteOutContentHandler(new DummyWriter()), metadata,
				parserContext);

			String contentType = metadata.get("Content-Type");

			if (contentType.equals("text/plain")) {
				return true;
			}

			return false;
		}
		catch (Exception e) {
			return false;
		}
	}

}