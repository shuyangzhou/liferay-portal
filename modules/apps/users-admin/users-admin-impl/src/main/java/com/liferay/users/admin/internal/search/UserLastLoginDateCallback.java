/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.users.admin.internal.search;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.DocumentImpl;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.util.BatchProcessor;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.document.BulkDocumentRequest;
import com.liferay.portal.search.engine.adapter.document.UpdateDocumentRequest;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.search.model.uid.UIDFactory;
import com.liferay.portal.search.script.ScriptBuilder;
import com.liferay.portal.search.script.ScriptType;
import com.liferay.portal.search.script.Scripts;
import com.liferay.portal.util.PropsValues;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 */
@Component(
	property = "key=com.liferay.portal.kernel.model.User#lastLoginDate",
	service = Indexable.Callback.class
)
public class UserLastLoginDateCallback implements Indexable.Callback {

	@Override
	public void reindex(BaseModel<?> baseModel) {
		if (!(baseModel instanceof User)) {
			return;
		}

		User user = (User)baseModel;

		Document document = new DocumentImpl();

		document.addDate(Field.MODIFIED_DATE, user.getModifiedDate());
		document.addKeyword(Field.UID, _uidFactory.getUID(user));
		document.addDate("lastLoginDate", user.getLastLoginDate());

		_batchProcessor.add(
			new AbstractMap.SimpleImmutableEntry<>(
				user.getCompanyId(), document));
	}

	@Activate
	protected void activate() {
		_painlessScript = StringUtil.read(
			UserLastLoginDateCallback.class,
			"UserLastLoginDateCallback.painless");
	}

	@Deactivate
	protected void deactivate() {
		_batchProcessor.close();
	}

	private void _flush(List<Map.Entry<Long, Document>> entries) {
		Map<Long, Map<String, Document>> documentsMap = new HashMap<>();

		for (Map.Entry<Long, Document> entry : entries) {
			Map<String, Document> documents = documentsMap.computeIfAbsent(
				entry.getKey(), key -> new HashMap<>());

			Document document = entry.getValue();

			Field field = document.getField(Field.UID);

			documents.put(field.getValue(), document);
		}

		for (Map.Entry<Long, Map<String, Document>> entry :
				documentsMap.entrySet()) {

			Map<String, Document> documents = entry.getValue();

			String indexName = _indexNameBuilder.getIndexName(entry.getKey());

			BulkDocumentRequest bulkDocumentRequest = new BulkDocumentRequest();

			for (Map.Entry<String, Document> documentEntry :
					documents.entrySet()) {

				Document document = documentEntry.getValue();

				Field lastLoginDateField = document.getField("lastLoginDate");
				Field lastLoginDateSortableField = document.getField(
					"lastLoginDate_sortable");
				Field modifiedDateField = document.getField(
					Field.MODIFIED_DATE);
				Field modifiedDateSortableField = document.getField(
					"modified_sortable");

				ScriptBuilder scriptBuilder = _scripts.builder();

				scriptBuilder.idOrCode(_painlessScript);
				scriptBuilder.language("painless");

				scriptBuilder.putParameter(
					"lastLoginDate", lastLoginDateField.getValue());
				scriptBuilder.putParameter(
					"lastLoginDate_sortable",
					GetterUtil.getLong(lastLoginDateSortableField.getValue()));
				scriptBuilder.putParameter(
					"modified", modifiedDateField.getValue());
				scriptBuilder.putParameter(
					"modified_sortable",
					GetterUtil.getLong(modifiedDateSortableField.getValue()));

				scriptBuilder.scriptType(ScriptType.INLINE);

				UpdateDocumentRequest updateDocumentRequest =
					new UpdateDocumentRequest(
						indexName, documentEntry.getKey(),
						scriptBuilder.build());

				updateDocumentRequest.setScriptedUpsert(true);

				bulkDocumentRequest.addBulkableDocumentRequest(
					updateDocumentRequest);
			}

			_searchEngineAdapter.execute(bulkDocumentRequest);
		}
	}

	private final BatchProcessor<Map.Entry<Long, Document>> _batchProcessor =
		new BatchProcessor<>(
			UserLastLoginDateCallback.class.getName(), this::_flush,
			PropsValues.USERS_UPDATE_LAST_LOGIN_BATCH_INTERVAL,
			PropsValues.USERS_UPDATE_LAST_LOGIN_BATCH_SIZE);

	@Reference
	private IndexNameBuilder _indexNameBuilder;

	private String _painlessScript;

	@Reference
	private Scripts _scripts;

	@Reference
	private SearchEngineAdapter _searchEngineAdapter;

	@Reference
	private UIDFactory _uidFactory;

}