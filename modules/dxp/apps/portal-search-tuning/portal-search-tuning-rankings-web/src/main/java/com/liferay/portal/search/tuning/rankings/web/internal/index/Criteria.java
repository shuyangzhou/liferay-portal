/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.tuning.rankings.web.internal.index;

import com.liferay.portal.search.tuning.rankings.index.name.RankingIndexName;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author André de Oliveira
 */
public class Criteria {

	public String getGroupExternalReferenceCode() {
		return _groupExternalReferenceCode;
	}

	public String getIndex() {
		return _index;
	}

	public Collection<String> getQueryStrings() {
		return _queryStrings;
	}

	public RankingIndexName getRankingIndexName() {
		return _rankingIndexName;
	}

	public String getSXPBlueprintExternalReferenceCode() {
		return _sxpBlueprintExternalReferenceCode;
	}

	public String getUnlessRankingDocumentId() {
		return _unlessRankingDocumentId;
	}

	public static class Builder {

		public Criteria build() {
			return new Criteria(_criteria);
		}

		public Builder groupExternalReferenceCode(
			String groupExternalReferenceCode) {

			_criteria._groupExternalReferenceCode = groupExternalReferenceCode;

			return this;
		}

		public Builder index(String index) {
			_criteria._index = index;

			return this;
		}

		public Builder queryStrings(Collection<String> queryStrings) {
			if (queryStrings == null) {
				_criteria._queryStrings = Collections.emptySet();
			}
			else {
				_criteria._queryStrings = new HashSet<>(queryStrings);
			}

			return this;
		}

		public Builder rankingIndexName(RankingIndexName rankingIndexName) {
			_criteria._rankingIndexName = rankingIndexName;

			return this;
		}

		public Builder sxpBlueprintExternalReferenceCode(
			String sxpBlueprintExternalReferenceCode) {

			_criteria._sxpBlueprintExternalReferenceCode =
				sxpBlueprintExternalReferenceCode;

			return this;
		}

		public Builder unlessRankingDocumentId(String unlessRankingDocumentId) {
			_criteria._unlessRankingDocumentId = unlessRankingDocumentId;

			return this;
		}

		private final Criteria _criteria = new Criteria(null);

	}

	protected Criteria(Criteria criteria) {
		if (criteria == null) {
			return;
		}

		_groupExternalReferenceCode = criteria._groupExternalReferenceCode;
		_index = criteria._index;
		_queryStrings = new HashSet<>(criteria._queryStrings);
		_rankingIndexName = criteria._rankingIndexName;
		_sxpBlueprintExternalReferenceCode =
			criteria._sxpBlueprintExternalReferenceCode;
		_unlessRankingDocumentId = criteria._unlessRankingDocumentId;
	}

	private String _groupExternalReferenceCode;
	private String _index;
	private Collection<String> _queryStrings = new HashSet<>();
	private RankingIndexName _rankingIndexName;
	private String _sxpBlueprintExternalReferenceCode;
	private String _unlessRankingDocumentId;

}