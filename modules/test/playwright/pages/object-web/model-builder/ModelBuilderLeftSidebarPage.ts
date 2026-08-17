/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect} from '@playwright/test';

import type {Locator, Page} from '@playwright/test';

export class ModelBuilderLeftSidebarPage {
	readonly page: Page;
	readonly createNewObjectDefinitionButton: Locator;
	readonly collapseOtherFoldersButton: Locator;
	readonly goToFolderButton: Locator;
	readonly sidebarItems: Locator;
	readonly otherObjectFolders: Locator;
	readonly selectedObjectFolder: Locator;

	constructor(page: Page) {
		this.page = page;
		this.collapseOtherFoldersButton = page.getByRole('button', {
			name: 'Other Folders',
		});
		this.createNewObjectDefinitionButton = page
			.getByText('Create New Object')
			.first();
		this.goToFolderButton = page.getByRole('button', {
			exact: true,
			name: 'Go to Folder',
		});
		this.sidebarItems = page.locator('li.treeview-item div.autofit-row');
		this.otherObjectFolders = page
			.getByRole('region')
			.filter({has: page.getByTitle('Go to Folder')});
		this.selectedObjectFolder = page
			.getByRole('tabpanel')
			.getByRole('treeitem')
			.filter({hasNot: page.getByTitle('Go to Folder')})
			.first();
	}

	async clickSideBarItem(objectDefinitionLabel: string) {
		const sidebarItem = this.sidebarItems.filter({
			hasText: objectDefinitionLabel,
		});

		// The model builder reads its structure once per page load and has no
		// failure path, so a load that missed the object never recovers,
		// however long anything waits on it. Load the page again until the
		// object is listed.

		let reload = false;

		await expect(async () => {
			if (reload) {
				await this.page.reload({waitUntil: 'load'});
			}

			reload = true;

			// Each attempt follows a full page load, so it gets a load's worth
			// of looking; the whole ask stops at half the test budget so the
			// work after the click keeps the other half.

			await expect(sidebarItem).toBeVisible({timeout: 10000});
		}).toPass({timeout: 45000});

		await sidebarItem.click();
	}

	async clickObjectDefinitionActionsButtonInSidebar(
		objectDefinitionLabel: string
	) {
		const sidebarItem = this.sidebarItems.filter({
			hasText: objectDefinitionLabel,
		});

		await sidebarItem.hover();

		await sidebarItem.getByLabel('Actions').click();
	}

	getOtherObjectFolderLocator(objectFolderLabel: string) {
		return this.otherObjectFolders
			.getByRole('treeitem')
			.filter({hasText: objectFolderLabel});
	}
}
