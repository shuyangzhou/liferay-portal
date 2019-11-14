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

import PropTypes from 'prop-types';
import React, {useCallback, useEffect} from 'react';

function PageTypeSelector(props) {
	useEffect(() => {
		return () => {
			Liferay.destroyComponent(`${props.namespace}pagesTree`);
		};
	}, [props.namespace]);

	const handleOnChange = useCallback(
		event => {
			const pageType = event.target.value;

			Liferay.Util.Session.set(
				`${props.namespace}PRIVATE_LAYOUT`,
				pageType === 'private-pages'
			).then(() => Liferay.Util.navigate(window.location.href));
		},
		[props.namespace]
	);

	const handleOnClick = useCallback(() => {
		const tree = Liferay.component(`${props.namespace}pagesTree`);

		tree.collapseAll();
	}, [props.namespace]);

	return (
		<div className="align-items-center d-flex page-type-selector">
			<div className="flex-fill flex-grow-1">
				<select
					className="form-control form-control-sm"
					defaultValue={
						props.privateLayout ? 'private-pages' : 'public-pages'
					}
					onChange={handleOnChange}
				>
					<option value="public-pages">
						{Liferay.Language.get('public-pages')}
					</option>
					<option value="private-pages">
						{Liferay.Language.get('private-pages')}
					</option>
				</select>
			</div>
			<div className="collapse-all-link flex-fill flex-grow-1 text-right">
				<button className="btn btn-unstyled" onClick={handleOnClick}>
					{Liferay.Language.get('collapse-all')}
				</button>
			</div>
		</div>
	);
}

PageTypeSelector.propTypes = {
	namespace: PropTypes.string,
	privateLayout: PropTypes.bool
};

export default function(props) {
	return <PageTypeSelector {...props} />;
}
