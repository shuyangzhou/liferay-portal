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

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackRegistryUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.service.LayoutSetPrototypeLocalServiceUtil;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author Raymond Augé
 */
public class LayoutSetPrototypeLayoutSetModelListener
	extends BaseModelListener<LayoutSet> {

	@Override
	public void onAfterCreate(LayoutSet layoutSet) {
		updateLayoutSetPrototype(layoutSet, layoutSet.getModifiedDate());
	}

	@Override
	public void onAfterRemove(LayoutSet layoutSet) {
		updateLayoutSetPrototype(layoutSet, new Date());
	}

	@Override
	public void onAfterUpdate(LayoutSet layoutSet) {
		updateLayoutSetPrototype(layoutSet, layoutSet.getModifiedDate());
	}

	protected void updateLayoutSetPrototype(
		final LayoutSet layoutSet, final Date modifiedDate) {

		TransactionCommitCallbackRegistryUtil.registerCallback(
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					if (layoutSet == null) {
						return null;
					}

					Group group = null;

					try {
						group = layoutSet.getGroup();

						if (!group.isLayoutSetPrototype()) {
							return null;
						}
					}
					catch (PortalException pe) {
						return null;
					}

					try {
						LayoutSetPrototype layoutSetPrototype =
							LayoutSetPrototypeLocalServiceUtil.
								getLayoutSetPrototype(group.getClassPK());

						layoutSetPrototype.setModifiedDate(modifiedDate);

						UnicodeProperties settingsProperties =
							layoutSet.getSettingsProperties();

						settingsProperties.remove("merge-fail-count");

						LayoutSetPrototypeLocalServiceUtil.
							updateLayoutSetPrototype(layoutSetPrototype);
					}
					catch (Exception e) {
						_log.error(e, e);
					}

					return null;
				}

			});
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutSetPrototypeLayoutSetModelListener.class);

}