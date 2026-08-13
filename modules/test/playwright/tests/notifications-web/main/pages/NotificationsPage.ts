/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Locator, Page} from '@playwright/test';

import {gotoWithRetry} from '../../../../utils/gotoWithRetry';
import {PORTLET_URLS} from '../../../../utils/portletUrls';

export class NotificationsPage {
	readonly page: Page;
	readonly backButton: Locator;
	readonly deleteButton: Locator;
	readonly notificationNoLongerAppliesMessage: Locator;
	readonly requestsTab: Locator;
	readonly selectAllItemsCheckbox: Locator;
	readonly sharingNotificationMessage: (
		userName: string,
		documentTitle: string
	) => Locator;
	readonly workflowReviewMessage: (
		asset: string,
		userName?: string
	) => Locator;

	constructor(page: Page) {
		this.page = page;

		this.backButton = this.page.getByRole('link', {
			name: 'Return to Full Page',
		});
		this.deleteButton = page.getByRole('button', {name: 'Delete'});
		this.notificationNoLongerAppliesMessage = this.page.getByText(
			'Notification no longer applies.'
		);
		this.requestsTab = this.page.getByRole('link', {
			name: 'Requests List (0)',
		});
		this.selectAllItemsCheckbox = page.getByLabel(
			'Select All Items on the Page'
		);
		this.sharingNotificationMessage = (
			userName: string,
			documentTitle: string
		) => {
			return page.getByText(
				`${userName} has shared ${documentTitle} with you for viewing.`
			);
		};
		this.workflowReviewMessage = (
			asset: string,
			userName: string = 'Test Test'
		) => {
			return page.getByText(
				`${userName} sent you a ${asset} for review in the workflow.`
			);
		};
	}

	async goto() {

		// Clicking through the profile dropdown leaves the test waiting out its
		// whole timeout for a menu item that never appears. No test asserts
		// that walk, so ask for the portlet by address, which does not depend
		// on a menu opening or on what it holds. The address names its site,
		// because the root path only reaches the site where a default site
		// host is configured, and it carries the back url the dropdown's own
		// link carries, because the back button the portlet renders from it is
		// asserted here.

		await gotoWithRetry(
			this.page,
			`/group/guest${PORTLET_URLS.notifications}&_com_liferay_notifications_web_portlet_NotificationsPortlet_backURL=%2F`
		);
	}
}
