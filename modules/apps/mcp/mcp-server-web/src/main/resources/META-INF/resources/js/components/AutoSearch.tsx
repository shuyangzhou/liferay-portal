/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {ClayInput} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import ClayManagementToolbar from '@clayui/management-toolbar';
import React, {forwardRef} from 'react';

interface AutoSearchProps {
	label?: string;
	onSearch: (value: string) => void;
	query: string;
}

const AutoSearch = forwardRef<HTMLInputElement, AutoSearchProps>(
	function AutoSearch(
		{label = Liferay.Language.get('search'), onSearch, query},
		ref
	) {
		return (
			<ClayManagementToolbar.Search
				onSubmit={(event) => event.preventDefault()}
				onlySearch
			>
				<ClayInput.Group>
					<ClayInput.GroupItem>
						<ClayInput
							aria-label={label}
							insetAfter
							onChange={(event) => onSearch(event.target.value)}
							placeholder={Liferay.Language.get('search')}
							ref={ref}
							type="search"
							value={query}
						/>

						<ClayInput.GroupInsetItem after tag="span">
							<span className="mx-1 px-2">
								<ClayIcon symbol="search" />
							</span>
						</ClayInput.GroupInsetItem>
					</ClayInput.GroupItem>
				</ClayInput.Group>
			</ClayManagementToolbar.Search>
		);
	}
);

export default AutoSearch;
