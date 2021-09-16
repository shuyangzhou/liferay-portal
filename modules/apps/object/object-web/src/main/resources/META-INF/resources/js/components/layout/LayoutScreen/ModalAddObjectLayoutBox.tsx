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
import ClayForm, {ClayInput, ClaySelect} from '@clayui/form';
import ClayModal from '@clayui/modal';
import React, {useContext, useState} from 'react';

import {normalizeLanguageId} from '../../../utils/string';
import RequiredMask from '../../RequiredMask';
import LayoutContext, {TYPES} from '../context';

const partitionNumbers = [
	Liferay.Language.get('one-column'),
	Liferay.Language.get('two-columns'),
	Liferay.Language.get('three-columns'),
];

const defaultLanguageId = normalizeLanguageId(
	Liferay.ThemeDisplay.getDefaultLanguageId()
);

interface IModalAddObjectLayoutBoxProps
	extends React.HTMLAttributes<HTMLElement> {
	observer: any;
	onClose: () => void;
}

const ModalAddObjectLayoutBox: React.FC<IModalAddObjectLayoutBoxProps> = ({
	observer,
	onClose,
	tabIndex,
}) => {
	const [name, setName] = useState<string>('');
	const [, dispatch] = useContext(LayoutContext);

	return (
		<ClayModal observer={observer}>
			<ClayModal.Header>
				{Liferay.Language.get('add-block')}
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
					<label htmlFor="inputPartitionNumbers">
						{Liferay.Language.get('partition-numbers')}

						<RequiredMask />
					</label>

					<ClaySelect
						aria-label="Select Label"
						defaultValue={partitionNumbers[0]}
						disabled
						id="inputPartitionNumbers"
						onChange={() => {}}
					>
						{partitionNumbers.map((item, index) => (
							<ClaySelect.Option
								key={item.replace(' ', '')}
								label={item}
								value={index + 1}
							/>
						))}
					</ClaySelect>
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
										tabIndex,
									},
									type: TYPES.ADD_OBJECT_LAYOUT_BOX,
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

export default ModalAddObjectLayoutBox;
