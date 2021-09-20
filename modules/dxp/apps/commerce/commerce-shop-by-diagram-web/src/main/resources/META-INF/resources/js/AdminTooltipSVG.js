/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayAutocomplete from '@clayui/autocomplete';
import ClayDropDown from '@clayui/drop-down';
import ClayForm, {ClayInput, ClayRadio, ClayRadioGroup} from '@clayui/form';
import PropTypes from 'prop-types';
import React, {useEffect, useRef, useState} from 'react';

import {searchDiagrams, searchSkus} from './utilities/utilities';
const SKU = 'sku';
const DIAGRAM = 'diagram';
const AdminTooltipSVG = ({
	namespace,
	selectedProduct,
	selectedProductQuantity,
	selectedProductSequence,
	setSelectedProduct,
	setSelectedProductQuantity,
	setSelectedProductSequence,
	type,
}) => {
	const [active, setActive] = useState(false);
	const [products, setProducts] = useState(null);
	const node = useRef();
	const dropdownNode = useRef();
	const [query, setQuery] = useState('');
	const [linkedValue, setLinkedValue] = useState(SKU);

	useEffect(() => {
		const getProducts = linkedValue === SKU ? searchSkus : searchDiagrams;

		if (query?.length) {
			getProducts(query, linkedValue).then((jsonResponse) =>
				setProducts(jsonResponse.items)
			);
		}
	}, [query, linkedValue, setSelectedProduct]);

	useEffect(() => {
		function handleClick(event) {
			if (
				node.current.contains(event.target) ||
				(dropdownNode.current &&
					dropdownNode.current.contains(event.target))
			) {
				return;
			}

			setActive(false);
		}
		if (active) {
			document.addEventListener('mousedown', handleClick);
		}

		return () => {
			document.removeEventListener('mousedown', handleClick);
		};
	}, [active]);

	return (
		<>
			{type !== 'diagram.type.svg' && (
				<ClayForm.Group className="col-12 text-left" small>
					<label htmlFor={`${namespace}pin-position`}>
						{Liferay.Language.get('position')}
					</label>

					<ClayInput
						id={`${namespace}pin-position`}
						onChange={(event) => {
							setSelectedProductSequence(event.target.value);
						}}
						placeholder={Liferay.Language.get(
							'insert-your-position-here'
						)}
						type="text"
						value={selectedProductSequence || ''}
					/>
				</ClayForm.Group>
			)}

			<ClayForm.Group className="col-12" small>
				<ClayRadioGroup
					className="d-flex justify-content-start mt-4"
					inline
					onSelectedValueChange={setLinkedValue}
					selectedValue={linkedValue}
				>
					<ClayRadio
						label={Liferay.Language.get('linked-to-sku')}
						value="sku"
					/>

					<ClayRadio
						label={Liferay.Language.get('linked-to-diagram')}
						value="diagram"
					/>
				</ClayRadioGroup>
			</ClayForm.Group>

			<ClayForm.Group className="col-9 text-left" small>
				<label htmlFor={`${namespace}pin-sku`}>
					{Liferay.Language.get('select-sku')}
				</label>

				<ClayAutocomplete ref={node}>
					<ClayAutocomplete.Input
						onChange={(event) => {
							setQuery(event.target.value);
						}}
						onFocus={() => {
							setActive(true);
						}}
						onKeyUp={(event) => {
							setActive(event.keyCode !== 27);
						}}
						value={selectedProduct?.sku || query}
					/>

					<ClayAutocomplete.DropDown active={active && products}>
						<div ref={dropdownNode}>
							<ClayDropDown.ItemList>
								{!products?.length && (
									<ClayDropDown.Item disabled>
										{Liferay.Language.get(
											'no-results-found'
										)}
									</ClayDropDown.Item>
								)}

								{!!products?.length &&
									products.map((product) => (
										<ClayAutocomplete.Item
											key={product.id}
											match={selectedProduct?.sku}
											onClick={() => {
												setSelectedProduct(product);
												setActive(false);
											}}
											value={`${product.sku} - ${product.productName.en_US}`}
										/>
									))}
							</ClayDropDown.ItemList>
						</div>
					</ClayAutocomplete.DropDown>
				</ClayAutocomplete>
			</ClayForm.Group>

			<ClayForm.Group className="col-3" small>
				<label htmlFor={`${namespace}pin-quantity`}>
					{Liferay.Language.get('quantity')}
				</label>

				<ClayInput
					id={`${namespace}pin-quantity`}
					min={1}
					onChange={(event) =>
						setSelectedProductQuantity(
							parseInt(event.target.value, 10)
						)
					}
					type="number"
					value={selectedProductQuantity}
				/>
			</ClayForm.Group>
		</>
	);
};

AdminTooltipSVG.propTypes = {
	showTooltip: PropTypes.shape({
		details: PropTypes.shape({
			cx: PropTypes.double,
			cy: PropTypes.double,
			id: PropTypes.number,
			label: PropTypes.string,
			linkedToSku: PropTypes.oneOf([SKU, DIAGRAM]),
			quantity: PropTypes.number,
			sku: PropTypes.string,
		}),
		tooltip: PropTypes.bool,
	}),
};

export default AdminTooltipSVG;
