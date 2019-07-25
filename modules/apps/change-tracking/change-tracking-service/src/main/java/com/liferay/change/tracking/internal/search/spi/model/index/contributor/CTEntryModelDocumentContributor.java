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

package com.liferay.change.tracking.internal.search.spi.model.index.contributor;

import com.liferay.change.tracking.model.CTEntry;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Daniel Kocsis
 */
@Component(
	immediate = true,
	property = "indexer.class.name=com.liferay.change.tracking.model.CTEntry",
	service = ModelDocumentContributor.class
)
public class CTEntryModelDocumentContributor
	implements ModelDocumentContributor<CTEntry> {

	@Override
	public void contribute(Document document, CTEntry ctEntry) {
		document.addDate(Field.CREATE_DATE, ctEntry.getCreateDate());
		document.addDate(Field.MODIFIED_DATE, ctEntry.getModifiedDate());
		document.addKeyword(Field.STATUS, ctEntry.getStatus());
		document.addKeyword("changeType", ctEntry.getChangeType());
		document.addKeyword("collision", ctEntry.isCollision());
		document.addKeyword("ctCollectionId", ctEntry.getCtCollectionId());
		document.addKeyword(
			"modelClassName",
			_portal.getClassName(ctEntry.getModelClassNameId()));
		document.addKeyword("modelClassNameId", ctEntry.getModelClassNameId());
		document.addKeyword("modelClassPK", ctEntry.getModelClassPK());
		document.addKeyword(
			"modelResourcePrimKey", ctEntry.getModelResourcePrimKey());
		document.addKeyword(
			"originalCTCollectionId", ctEntry.getOriginalCTCollectionId());
	}

	@Reference
	private Portal _portal;

}