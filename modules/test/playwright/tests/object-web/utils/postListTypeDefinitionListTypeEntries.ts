/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {DataApiHelpers} from '../../../helpers/ApiHelpers';
import {getRandomInt} from '../../../utils/getRandomInt';

export async function postListTypeDefinitionListTypeEntries({
	apiHelpers,
	listTypeEntriesLength = 4,
	locale,
}: {
	apiHelpers: DataApiHelpers;
	listTypeEntriesLength?: number;
	locale?: Locale;
}): Promise<{
	listTypeDefinition: ListTypeDefinition;
	listTypeEntries: ListTypeEntry[];
}> {
	const listTypeDefinition =
		await apiHelpers.listTypeAdmin.postRandomListTypeDefinition();

	apiHelpers.data.push({
		id: listTypeDefinition.id,
		type: 'listTypeEntries',
	});

	const listTypeEntries: LocalizedValue<string>[] = Array.from(
		{length: listTypeEntriesLength},
		() => {
			const entry: LocalizedValue<string> = {
				en_US: getRandomInt().toString(),
			};

			if (locale) {
				entry[locale] = getRandomInt().toString();
			}

			return entry;
		}
	);

	// A request that rides the JAX-RS whiteboard while an object definition
	// deploys or undeploys nearby can be refused with Equinox's "The service
	// parameter was not provided by this object" answered as a 400, and a
	// refused post commits nothing. Nothing marks the churn window from the
	// client, so ask again on exactly that answer.

	const postListTypeEntry = async (
		listTypeDefinitionEntry: LocalizedValue<string>
	) => {
		for (let attempt = 0; ; attempt++) {
			try {
				return await apiHelpers.listTypeAdmin.postListTypeEntry({
					key: listTypeDefinitionEntry.en_US,
					listTypeDefinitionExternalReferenceCode:
						listTypeDefinition.externalReferenceCode,
					name_i18n: listTypeDefinitionEntry,
				});
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
	};

	const listTypeEntry = listTypeEntries.map(postListTypeEntry);

	const promiseResolvedListTypeEntries = await Promise.all(listTypeEntry);

	return {
		listTypeDefinition,
		listTypeEntries: promiseResolvedListTypeEntries,
	};
}
