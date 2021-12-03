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

package com.liferay.portal.language.override.internal;

import com.liferay.petra.lang.HashUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.language.override.model.PLOEntry;
import com.liferay.portal.language.override.service.PLOEntryLocalService;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Drew Brokke
 */
@Component(service = PLOLanguageOverrideCache.class)
public class PLOLanguageOverrideCache {

	public void clear(long companyId, Locale locale) {
		_cache.remove(
			new PLOCacheKey(companyId, LanguageUtil.getLanguageId(locale)));
	}

	public Map<String, String> getOverrideMap(long companyId, Locale locale) {
		return _cache.computeIfAbsent(
			new PLOCacheKey(companyId, LanguageUtil.getLanguageId(locale)),
			ploCacheKey -> {
				List<PLOEntry> ploEntries =
					_ploEntryLocalService.getPLOEntriesByLanguageId(
						ploCacheKey.companyId, ploCacheKey.languageId);

				Stream<PLOEntry> ploEntryStream = ploEntries.stream();

				return ploEntryStream.collect(
					Collectors.toMap(PLOEntry::getKey, PLOEntry::getValue));
			});
	}

	private final ConcurrentHashMap<PLOCacheKey, Map<String, String>> _cache =
		new ConcurrentHashMap<>();

	@Reference
	private PLOEntryLocalService _ploEntryLocalService;

	private static class PLOCacheKey {

		public PLOCacheKey(long companyId, String languageId) {
			this.companyId = companyId;
			this.languageId = languageId;
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof PLOCacheKey) {
				PLOCacheKey ploCacheKey = (PLOCacheKey)object;

				if ((companyId == ploCacheKey.companyId) &&
					Objects.equals(languageId, ploCacheKey.languageId)) {

					return true;
				}
			}

			return false;
		}

		@Override
		public int hashCode() {
			int localeHashCode = HashUtil.hash(0, languageId);

			return HashUtil.hash(localeHashCode, companyId);
		}

		public final long companyId;
		public final String languageId;

	}

}