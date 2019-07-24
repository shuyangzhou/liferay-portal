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

package com.liferay.change.tracking.internal;

import com.liferay.change.tracking.constants.CTConstants;
import com.liferay.change.tracking.model.CTEntry;
import com.liferay.change.tracking.service.CTEntryLocalService;
import com.liferay.portal.change.tracking.CTSQLHelper;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Preston Crary
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = CTSQLHelper.class
)
public class CTSQLHelperImpl implements CTSQLHelper {

	@Override
	public Iterator<Change> getChanges(long ctCollectionId, long classNameId) {
		List<CTEntry> ctEntries = _ctEntryLocalService.getCTEntries(
			ctCollectionId, classNameId);

		if (ctEntries.isEmpty()) {
			return Collections.emptyIterator();
		}

		Iterator<CTEntry> iterator = ctEntries.iterator();

		return new Iterator<Change>() {

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Change next() {
				CTEntry ctEntry = iterator.next();

				return new Change() {

					@Override
					public long getChangePrimaryKey() {
						return ctEntry.getModelClassPK();
					}

					@Override
					public ChangeType getChangeType() {
						int changeType = ctEntry.getChangeType();

						if (changeType == CTConstants.CT_CHANGE_TYPE_ADDITION) {
							return ChangeType.ADD;
						}

						if (changeType == CTConstants.CT_CHANGE_TYPE_DELETION) {
							return ChangeType.DELETE;
						}

						return ChangeType.MODIFY;
					}

				};
			}

		};
	}

	@Reference
	private CTEntryLocalService _ctEntryLocalService;

}