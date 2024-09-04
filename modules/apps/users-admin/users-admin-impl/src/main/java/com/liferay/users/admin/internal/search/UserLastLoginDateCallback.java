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
import com.liferay.portal.search.index.UpdateDocumentIndexWriter;
import com.liferay.portal.search.model.uid.UIDFactory;
import com.liferay.portal.util.PropsValues;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		document.add(
			new Field(Field.ENTRY_CLASS_NAME, user.getModelClassName()));
		document.add(
			new Field(Field.ENTRY_CLASS_PK, String.valueOf(user.getUserId())));
		document.addDate(Field.MODIFIED_DATE, user.getModifiedDate());
		document.addKeyword(Field.UID, _uidFactory.getUID(user));
		document.addDate("lastLoginDate", user.getLastLoginDate());

		_batchProcessor.add(
			new AbstractMap.SimpleImmutableEntry<>(
				user.getCompanyId(), document));
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

			Field field = document.getField(Field.ENTRY_CLASS_PK);

			documents.put(field.getValue(), document);
		}

		for (Map.Entry<Long, Map<String, Document>> entry :
				documentsMap.entrySet()) {

			Map<String, Document> documents = entry.getValue();

			_updateDocumentIndexWriter.updateDocumentsPartially(
				entry.getKey(), documents.values(), false);
		}
	}

	private final BatchProcessor<Map.Entry<Long, Document>> _batchProcessor =
		new BatchProcessor<>(
			UserLastLoginDateCallback.class.getName(), this::_flush,
			PropsValues.USERS_UPDATE_LAST_LOGIN_BATCH_INTERVAL,
			PropsValues.USERS_UPDATE_LAST_LOGIN_BATCH_SIZE);

	@Reference
	private UIDFactory _uidFactory;

	@Reference
	private UpdateDocumentIndexWriter _updateDocumentIndexWriter;

}