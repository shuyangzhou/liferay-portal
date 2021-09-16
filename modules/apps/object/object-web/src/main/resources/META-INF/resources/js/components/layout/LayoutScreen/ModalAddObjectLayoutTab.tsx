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

import ClayButton from '@clayui/button';
import ClayForm, {ClayInput, ClayRadio, ClayRadioGroup} from '@clayui/form';
import ClayModal from '@clayui/modal';
import React, {useContext, useState} from 'react';

import {normalizeLanguageId} from '../../../utils/string';
import RequiredMask from '../../RequiredMask';
import LayoutContext, {TYPES} from '../context';

const tabType = [
	{
		description: Liferay.Language.get(
			'to-display-fields-and-one-to-one-relationships'
		),
		label: Liferay.Language.get('fields'),
		value: 'fields',
	},
];

const defaultLanguageId = normalizeLanguageId(
	Liferay.ThemeDisplay.getDefaultLanguageId()
);

interface IModalAddObjectLayoutTabProps
	extends React.HTMLAttributes<HTMLElement> {
	observer: any;
	onClose: () => void;
}

const ModalAddObjectLayoutTab: React.FC<IModalAddObjectLayoutTabProps> = ({
	observer,
	onClose,
}) => {
	const [name, setName] = useState<string>('');
	const [selectedType, setSelectedType] = useState<string>('fields');
	const [, dispatch] = useContext(LayoutContext);

	return (
		<ClayModal observer={observer}>
			<ClayModal.Header>
				{Liferay.Language.get('add-tab')}
			</ClayModal.Header>
			<ClayModal.Body>
				<ClayForm.Group>
					<label htmlFor="inputName">
						{Liferay.Language.get('label')}

						<RequiredMask />
					</label>

					<ClayInput
						id="inputName"
						onChange={(event) => setName(event.target.value)}
						type="text"
						value={name}
					/>
				</ClayForm.Group>

				<ClayForm.Group>
					<label htmlFor="inputType">
						{Liferay.Language.get('type')}
					</label>

					<ClayRadioGroup
						id="inputType"
						onSelectedValueChange={(value) =>
							setSelectedType(value as string)
						}
						selectedValue={selectedType}
					>
						{tabType.map(({label, value}) => (
							<ClayRadio
								key={value}
								label={label}
								value={value}
							/>
						))}
					</ClayRadioGroup>
				</ClayForm.Group>
			</ClayModal.Body>
			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton displayType="secondary" onClick={onClose}>
							{Liferay.Language.get('cancel')}
						</ClayButton>
						<ClayButton
							onClick={() => {
								dispatch({
									payload: {
										name: {
											[defaultLanguageId]: name,
										},
										type: selectedType,
									},
									type: TYPES.ADD_OBJECT_LAYOUT_TAB,
								});

								onClose();
							}}
						>
							{Liferay.Language.get('save')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</ClayModal>
	);
};

export default ModalAddObjectLayoutTab;
