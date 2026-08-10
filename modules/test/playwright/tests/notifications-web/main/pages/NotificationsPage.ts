/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Locator, Page} from '@playwright/test';

import {clickAndExpectToBeVisible} from '../../../../utils/clickAndExpectToBeVisible';

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

	async goto(userName: string = 'Test Test') {

		// Scope to the open dropdown: the control panel sidebar can hold a
		// second menu item also named Notifications, the push notifications
		// portlet's entry. The profile click occasionally lands before the menu
		// hydrates and the dropdown never opens, so retry the click until the
		// entry it reveals is visible, then follow it.

		await clickAndExpectToBeVisible({
			autoClick: true,
			target: this.page
				.locator('.dropdown-menu.show')
				.getByRole('menuitem', {name: 'Notifications'}),
			trigger: this.page.getByLabel(`${userName} User Profile`),
		});
	}
}
