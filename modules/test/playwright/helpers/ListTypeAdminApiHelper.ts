/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {getRandomInt} from '../utils/getRandomInt';
import {ApiHelpers} from './ApiHelpers';

export class ListTypeAdminApiHelper {
	readonly apiHelpers: ApiHelpers;
	readonly basePath: string;

	constructor(apiHelpers: ApiHelpers) {
		this.apiHelpers = apiHelpers;
		this.basePath = 'headless-admin-list-type/v1.0';
	}

	async deleteListTypeDefinition(listTypeDefinitionId: number) {
		return this.apiHelpers.delete(
			`${this.apiHelpers.baseUrl}${this.basePath}/list-type-definitions/${listTypeDefinitionId}`
		);
	}

	async getFilteredListTypeDefinition(
		filterParamKey: string,
		filterParamValue: string
	): Promise<ListTypeDefinition[]> {
		const response: ListTypeDefinitions = await this.apiHelpers.get(
			`${this.apiHelpers.baseUrl}${this.basePath}/list-type-definitions?filter=${filterParamKey} eq '${filterParamValue}'`
		);

		return response.items;
	}

	async getListTypeDefinitions(): Promise<ListTypeDefinitions> {
		return this.apiHelpers.get(
			`${this.apiHelpers.baseUrl}${this.basePath}/list-type-definitions`
		);
	}

	async postListTypeEntry({
		key,
		listTypeDefinitionExternalReferenceCode,
		name_i18n,
	}: {
		key: string;
		listTypeDefinitionExternalReferenceCode: string;
		name_i18n: LocalizedValue<string>;
	}): Promise<ListTypeDefinition> {
		return this.apiHelpers.post(
			`${this.apiHelpers.baseUrl}${this.basePath}/list-type-definitions/by-external-reference-code/${listTypeDefinitionExternalReferenceCode}/list-type-entries`,
			{
				data: {
					key: key.toLowerCase(),
					name_i18n,
				},
			}
		);
	}

	async postRandomListTypeDefinition(): Promise<ListTypeDefinition> {
		const listTypeDefinitionExternalReferenceCode =
			'ListTypeDefinition' + getRandomInt();

		const requestBody = {
			externalReferenceCode: listTypeDefinitionExternalReferenceCode,
			name: listTypeDefinitionExternalReferenceCode,
			name_i18n: {
				en_US: listTypeDefinitionExternalReferenceCode,
			},
		};

		// A request that rides the JAX-RS whiteboard while an object
		// definition deploys or undeploys nearby can be refused with
		// Equinox's "The service parameter was not provided by this object"
		// answered as a 400, and a refused post commits nothing. Nothing
		// marks the churn window from the client, so ask again on exactly
		// that answer.

		for (let attempt = 0; ; attempt++) {
			try {
				return await this.apiHelpers.post(
					`${this.apiHelpers.baseUrl}${this.basePath}/list-type-definitions`,
					{data: requestBody}
				);
			}
			catch (error) {
				if (
					attempt >= 9 ||
					!String(error.message).includes(
						'The service parameter was not provided by this object'
					)
				) {
					throw error;
				}

				// Half a second puts the next ask past the sub-second churn
				// window observed locally.

				await new Promise((resolve) => setTimeout(resolve, 500));
			}
		}
	}
}
