/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect} from '@playwright/test';

import {PORTLET_URLS} from '../../../utils/portletUrls';

import type {Locator, Page} from '@playwright/test';

export class ModelBuilderDiagramPage {
	readonly deletionNotAllowed: Locator;
	readonly diagramArea: Locator;
	readonly editInPageViewOption: Locator;
	readonly editObjectFolderDetailsButton: Locator;
	readonly fitViewButton: Locator;
	readonly newObjectFieldName: Locator;
	readonly objectDefinitionNodes: Locator;
	readonly objectRelationshipEdges: Locator;
	readonly openPageViewButton: Locator;
	readonly page: Page;
	readonly postalAddressObjectRelationshipWarning: Locator;
	readonly toggleSidebarsButton: Locator;

	constructor(page: Page) {
		this.deletionNotAllowed = page.getByRole('heading', {
			name: 'Deletion Not Allowed',
		});
		this.diagramArea = page.locator('.react-flow');
		this.editInPageViewOption = page.getByRole('menuitem', {
			name: 'Edit in page view',
		});
		this.editObjectFolderDetailsButton = page.locator(
			'button[name=editObjectFolderButton]'
		);
		this.fitViewButton = page.locator(
			'button.react-flow__controls-button.react-flow__controls-fitview'
		);
		this.objectDefinitionNodes = page.locator('.react-flow__node');
		this.objectRelationshipEdges = page.locator('.react-flow__edge');
		this.openPageViewButton = page.getByRole('button', {
			name: 'Open Page View',
		});
		this.page = page;
		this.postalAddressObjectRelationshipWarning = page.locator(
			'.alert-warning',
			{
				hasText:
					'Postal Address can only have a relationship with the Account object.',
			}
		);
		this.toggleSidebarsButton = page.getByLabel('Toggle Sidebars');
	}

	async clickObjectRelationshipEdge(objectRelationshipLabel: string) {
		await this.objectRelationshipEdges
			.filter({hasText: objectRelationshipLabel})
			.click();
	}

	async connectObjectDefinitionsNodeHandles(
		objectDefinitionId1: number,
		objectDefinitionId2: number,
		direction: [Direction, Direction] = ['right', 'left']
	) {
		await this.getObjectDefinitionNodeRelationshipHandle(
			objectDefinitionId1,
			direction[0]
		).dragTo(
			this.getObjectDefinitionNodeRelationshipHandle(
				objectDefinitionId2,
				direction[1]
			)
		);
	}

	async dragNodeThroughDiagram(
		objectDefinitionLabel: string,
		targetX: number,
		targetY: number
	) {
		await this.objectDefinitionNodes
			.getByText(objectDefinitionLabel, {exact: true})
			.dragTo(this.diagramArea, {
				targetPosition: {x: targetX, y: targetY},
			});
	}

	getObjectDefinitionNodeRelationshipHandle(
		objectDefinitionId: number,
		position: string
	) {
		let dataHandled = 'fixedRightHandle';

		if (position === 'left') {
			dataHandled = 'fixedLeftHandle';
		}

		return this.page.locator(
			`div[data-handleid="${objectDefinitionId}_${position}"]:not([data-handleid="${dataHandled}"])`
		);
	}

	getObjectFolderERCHeaderLocator(objectFolderERC: string) {
		return this.page.getByTitle(`ERC: ${objectFolderERC}`);
	}

	getObjectFolderLabelHeaderLocator(objectFolderLabel: string) {
		return this.page.getByTitle(
			`Object Folder Label: ${objectFolderLabel}`
		);
	}

	async goto({
		objectFolderName,
		siteUrl,
	}: {
		objectFolderName: string;
		siteUrl?: Site['friendlyUrlPath'];
	}) {
		await this.page.goto(
			`/group${siteUrl || '/guest'}${
				PORTLET_URLS.modelBuilder
			}&objectFolderName=${objectFolderName}`,
			{waitUntil: 'load'}
		);
	}

	async gotoWithObjectDefinitionNode({
		objectDefinitionName,
		objectFolderName,
		siteUrl,
	}: {
		objectDefinitionName: string;
		objectFolderName: string;
		siteUrl?: Site['friendlyUrlPath'];
	}) {

		// The diagram reads its structure once per page load and has no
		// failure path, so a load that missed the object never recovers,
		// however long anything waits on it. Load again until the object's
		// node is on the canvas.

		await expect(async () => {
			await this.goto({objectFolderName, siteUrl});

			await expect(
				this.objectDefinitionNodes.filter({
					hasText: objectDefinitionName,
				})
			).toHaveCount(1);

			// A minute gives the reload rounds their room and still leaves a
			// third of the test budget for the field work that follows.

		}).toPass({timeout: 60000});
	}

	async openObjectDefinitionMenu(objectDefinitionLabel: string) {
		this.page
			.locator('.lfr-objects__model-builder-node-header-label-container')
			.filter({hasText: objectDefinitionLabel})
			.getByRole('button')
			.click();
	}
}
