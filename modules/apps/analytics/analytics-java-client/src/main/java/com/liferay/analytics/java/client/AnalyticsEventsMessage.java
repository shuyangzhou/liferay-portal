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

package com.liferay.analytics.java.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eduardo Garcia
 * @see com.liferay.lcs.messaging.AnalyticsEventsMessage
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
	include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "messageFormat",
	use = JsonTypeInfo.Id.NAME, visible = true
)
public class AnalyticsEventsMessage implements Serializable {

	public static AnalyticsEventsMessage.Builder builder() {
		return new AnalyticsEventsMessage.Builder();
	}

	public String getAnalyticsKey() {
		return _analyticsKey;
	}

	public long getAnonymousUserId() {
		return _anonymousUserId;
	}

	public String getApplicationId() {
		return _applicationId;
	}

	public String getChannel() {
		return _channel;
	}

	public String getClientIP() {
		return _clientIP;
	}

	public Context getContext() {
		return _context;
	}

	public List<Event> getEvents() {
		return _events;
	}

	public String getMessageFormat() {
		return _messageFormat;
	}

	public String getProtocolVersion() {
		return _protocolVersion;
	}

	public String getUserAgent() {
		return _userAgent;
	}

	public static class Builder {

		public Builder analyticsKey(String analyticsKey) {
			_analyticsEventsMessage._analyticsKey = analyticsKey;

			return this;
		}

		public Builder anonymousUserId(long anonymousUserId) {
			_analyticsEventsMessage._anonymousUserId = anonymousUserId;

			return this;
		}

		public Builder applicationId(String applicationId) {
			_analyticsEventsMessage._applicationId = applicationId;

			return this;
		}

		public AnalyticsEventsMessage build() {
			return _analyticsEventsMessage;
		}

		public Builder channel(String channel) {
			_analyticsEventsMessage._channel = channel;

			return this;
		}

		public Builder clientIP(String clientIP) {
			_analyticsEventsMessage._clientIP = clientIP;

			return this;
		}

		public Builder context(Context context) {
			_analyticsEventsMessage._context = context;

			return this;
		}

		public Builder event(Event event) {
			_analyticsEventsMessage._events.add(event);

			return this;
		}

		public Builder messageFormat(String messageFormat) {
			_analyticsEventsMessage._messageFormat = messageFormat;

			return this;
		}

		public Builder protocolVersion(String protocolVersion) {
			_analyticsEventsMessage._protocolVersion = protocolVersion;

			return this;
		}

		public Builder userAgent(String userAgent) {
			_analyticsEventsMessage._userAgent = userAgent;

			return this;
		}

		private final AnalyticsEventsMessage _analyticsEventsMessage =
			new AnalyticsEventsMessage();

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Context {

		public static Context.Builder builder() {
			return new Context.Builder();
		}

		public long getInstanceId() {
			return _instanceId;
		}

		public String getLanguageId() {
			return _languageId;
		}

		public String getURL() {
			return _url;
		}

		public long getUserId() {
			return _userId;
		}

		public static class Builder {

			public AnalyticsEventsMessage.Context build() {
				return _context;
			}

			public Context.Builder instanceId(long instanceId) {
				_context._instanceId = instanceId;

				return this;
			}

			public Context.Builder languageId(String languageId) {
				_context._languageId = languageId;

				return this;
			}

			public Context.Builder url(String url) {
				_context._url = url;

				return this;
			}

			public Context.Builder userId(long userId) {
				_context._userId = userId;

				return this;
			}

			private AnalyticsEventsMessage.Context _context =
				new AnalyticsEventsMessage.Context();

		}

		private Context() {
		}

		private long _instanceId;
		private String _languageId;
		private String _url;
		private long _userId;

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Event {

		public static Event.Builder builder() {
			return new Event.Builder();
		}

		public String getAdditionalInfo() {
			return _additionalInfo;
		}

		public String getEvent() {
			return _event;
		}

		public Map<String, String> getProperties() {
			return Collections.unmodifiableMap(_properties);
		}

		public List<Referrer> getReferrers() {
			return Collections.unmodifiableList(_referrers);
		}

		public Date getTimestamp() {
			return _timestamp;
		}

		public static class Builder {

			public Event.Builder additionalInfo(String additionalInfo) {
				_event._additionalInfo = additionalInfo;

				return this;
			}

			public AnalyticsEventsMessage.Event build() {
				return _event;
			}

			public Event.Builder event(String event) {
				_event._event = event;

				return this;
			}

			public Event.Builder property(String key, String value) {
				_event._properties.put(key, value);

				return this;
			}

			public Event.Builder referrer(Referrer referrer) {
				_event._referrers.add(referrer);

				return this;
			}

			public Event.Builder timestamp(Date timestamp) {
				_event._timestamp = timestamp;

				return this;
			}

			private AnalyticsEventsMessage.Event _event =
				new AnalyticsEventsMessage.Event();

		}

		private Event() {
		}

		private String _additionalInfo;
		private String _event;
		private final Map<String, String> _properties = new HashMap<>();
		private List<Referrer> _referrers = new ArrayList<>();

		@JsonFormat(
			pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			shape = JsonFormat.Shape.STRING, timezone = "UTC"
		)
		private Date _timestamp;

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Referrer {

		public static Referrer.Builder builder() {
			return new Referrer.Builder();
		}

		public List<String> getReferrerEntityIds() {
			return _referrerEntityIds;
		}

		public String getReferrerEntityType() {
			return _referrerEntityType;
		}

		public static class Builder {

			public AnalyticsEventsMessage.Referrer build() {
				return _referrer;
			}

			public Referrer.Builder referrerEntityIds(
				List<String> referrerEntityIds) {

				_referrer._referrerEntityIds = referrerEntityIds;

				return this;
			}

			public Referrer.Builder referrerEntityType(
				String referrerEntityType) {

				_referrer._referrerEntityType = referrerEntityType;

				return this;
			}

			private final AnalyticsEventsMessage.Referrer _referrer =
				new AnalyticsEventsMessage.Referrer();

		}

		private Referrer() {
		}

		private List<String> _referrerEntityIds = Collections.emptyList();
		private String _referrerEntityType;

	}

	private AnalyticsEventsMessage() {
	}

	private String _analyticsKey;
	private long _anonymousUserId;
	private String _applicationId;
	private String _channel;
	private String _clientIP;
	private Context _context;
	private final List<Event> _events = new ArrayList<>();
	private String _messageFormat;
	private String _protocolVersion;
	private String _userAgent;

}