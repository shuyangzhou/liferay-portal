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

package com.liferay.portal.mail.test;

import com.liferay.portal.kernel.util.ListUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Adam Brandizzi
 */
public class MailServiceTestUtil {

	public static void clearMessages() {
		com.liferay.portal.util.test.MailServiceTestUtil.clearMessages();
	}

	public static int getInboxSize() {
		return com.liferay.portal.util.test.MailServiceTestUtil.getInboxSize();
	}

	public static MailMessage getLastMailMessage() {
		com.dumbster.smtp.MailMessage mailMessage =
			com.liferay.portal.util.test.MailServiceTestUtil.
				getLastMailMessage();

		return wrapMailMessage(mailMessage);
	}

	public static List<MailMessage> getMailMessages(
		String headerName, String headerValue) {

		List<com.dumbster.smtp.MailMessage> mailMessages =
			com.liferay.portal.util.test.MailServiceTestUtil.getMailMessages(
				headerName, headerValue);

		return wrapMailMessages(mailMessages);
	}

	public static boolean lastMailMessageContains(String text) {
		MailMessage mailMessage = getLastMailMessage();

		String bodyMailMessage = mailMessage.getBody();

		return bodyMailMessage.contains(text);
	}

	protected static Map<String, List<String>> getHeadersMap(
		com.dumbster.smtp.MailMessage mailMessage) {

		Map<String, List<String>> headers = new HashMap<>();

		Iterator<String> headerNames = mailMessage.getHeaderNames();

		while (headerNames.hasNext()) {
			String headerName = headerNames.next();

			List<String> headerValues = ListUtil.fromArray(
				mailMessage.getHeaderValues(headerName));

			headers.put(headerName, Collections.unmodifiableList(headerValues));
		}

		return headers;
	}

	protected static MailMessage wrapMailMessage(
		com.dumbster.smtp.MailMessage mailMessage) {

		Map<String, List<String>> headers = getHeadersMap(mailMessage);

		return new MailMessageImpl(mailMessage.getBody(), headers);
	}

	protected static List<MailMessage> wrapMailMessages(
		List<com.dumbster.smtp.MailMessage> mailMessages) {

		List<MailMessage> wrappedMailMessages = new ArrayList<>();

		for (com.dumbster.smtp.MailMessage mailMessage : mailMessages) {
			wrappedMailMessages.add(wrapMailMessage(mailMessage));
		}

		return wrappedMailMessages;
	}

	protected static class MailMessageImpl implements MailMessage {

		public MailMessageImpl(String body, Map<String, List<String>> headers) {
			_body = body;
			_headers = Collections.unmodifiableMap(headers);
		}

		@Override
		public String getBody() {
			return _body;
		}

		@Override
		public String getFirstHeaderValue(String headerName) {
			List<String> headerValues = getHeaderValues(headerName);

			return headerValues.get(0);
		}

		@Override
		public Set<String> getHeaderNames() {
			return _headers.keySet();
		}

		@Override
		public List<String> getHeaderValues(String headerName) {
			return _headers.get(headerName);
		}

		private final String _body;
		private final Map<String, List<String>> _headers;

	}

}