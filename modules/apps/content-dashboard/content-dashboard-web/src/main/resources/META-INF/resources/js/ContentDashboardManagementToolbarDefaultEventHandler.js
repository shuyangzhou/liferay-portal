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

import {
	DefaultEventHandler,
	ItemSelectorDialog,
	addParams,
	navigate,
} from 'frontend-js-web';

class ContentDashboardManagementToolbarDefaultEventHandler extends DefaultEventHandler {
	selectAuthor(itemData) {
		const itemSelectorDialog = new ItemSelectorDialog({
			buttonAddLabel: Liferay.Language.get('select'),
			eventName: this.ns('selectedAuthorItem'),
			title: itemData.dialogTitle,
			url: itemData.selectAuthorURL,
		});

		itemSelectorDialog.on('selectedItemChange', (event) => {
			const selectedItem = event.selectedItem;

			if (selectedItem) {
				var redirectURL = itemData.redirectURL;

				selectedItem.forEach((item) => {
					redirectURL = addParams(
						this.namespace + 'authorIds=' + item.id,
						redirectURL
					);
				});

				navigate(redirectURL);
			}
		});

		itemSelectorDialog.open();
	}
}

export default ContentDashboardManagementToolbarDefaultEventHandler;
