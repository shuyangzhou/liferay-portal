/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Locator, Page} from '@playwright/test';

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
		const profileButton = this.page.getByLabel(`${userName} User Profile`);

		// DEBUG do not merge. Record what the page holds when the menu item is
		// not there. The recorded failure waits the whole ninety seconds on a
		// locator that never matches, and the call log names only the locator,
		// so the question is whether the dropdown opened at all, closed again,
		// or rendered without the entry.

		const profileCount = await profileButton.count();

		await profileButton.click();

		try {
			await this.page
				.locator('.dropdown-menu.show')
				.getByRole('menuitem', {name: 'Notifications'})
				.click({timeout: 20000});
		}
		catch (error) {
			const diagnostics = await this.page.evaluate(() => {
				const describe = (element: Element) => {
					const rect = element.getBoundingClientRect();
					const style = getComputedStyle(element);

					let hidden = '';

					for (
						let parent = element.parentElement;
						parent;
						parent = parent.parentElement
					) {
						const parentStyle = getComputedStyle(parent);

						if (
							parentStyle.visibility === 'hidden' ||
							parentStyle.display === 'none' ||
							parentStyle.opacity === '0'
						) {
							hidden = `${parent.className}:${parentStyle.visibility}/${parentStyle.display}/${parentStyle.opacity}`;
							break;
						}
					}

					return `${style.visibility}/${style.display} ${Math.round(rect.width)}x${Math.round(rect.height)} hiddenAncestor=${hidden || 'none'}`;
				};

				const menus = Array.from(
					document.querySelectorAll('.dropdown-menu')
				);
				const shown = menus.filter((menu) =>
					menu.classList.contains('show')
				);
				const named = Array.from(
					document.querySelectorAll('[role="menuitem"]')
				).filter((item) =>
					(item.textContent || '').includes('Notifications')
				);

				return {
					anyNotifications: named.length,
					expanded: document.querySelectorAll(
						'[aria-expanded="true"]'
					).length,
					menus: menus.length,
					notificationsState: named.map(describe),
					shown: shown.length,
					shownItems: shown.map((menu) =>
						Array.from(
							menu.querySelectorAll('[role="menuitem"]')
						).map((item) =>
							(item.textContent || '').trim().slice(0, 24)
						)
					),
				};
			});

			throw new Error(
				`NOTIFDIAG profileButtons=${profileCount} shown=${diagnostics.shown} anyNotifications=${diagnostics.anyNotifications} menus=${diagnostics.menus} expanded=${diagnostics.expanded} state=${JSON.stringify(diagnostics.notificationsState)} items=${JSON.stringify(diagnostics.shownItems)}`
			);
		}
	}
}
