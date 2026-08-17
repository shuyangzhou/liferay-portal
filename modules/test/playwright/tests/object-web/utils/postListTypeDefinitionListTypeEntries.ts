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

	// Creating the picklist is answered before it is deployed, and an entry
	// posted in that window is refused with "The service parameter was not
	// provided by this object". Nothing on the definition is left to wait on,
	// so ask again until the entry is taken.

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
