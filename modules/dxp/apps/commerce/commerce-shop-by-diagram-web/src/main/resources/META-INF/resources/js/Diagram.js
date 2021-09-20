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

import ClayButton from '@clayui/button';
import {ClayIconSpriteContext} from '@clayui/icon';
import ClayModal, {useModal} from '@clayui/modal';
import {fetch, openToast} from 'frontend-js-web';
import {UPDATE_DATASET_DISPLAY} from 'frontend-taglib-clay/data_set_display/utils/eventsDefinitions';
import PropTypes from 'prop-types';
import React, {useCallback, useEffect, useState} from 'react';

import AdminTooltipDefault from './AdminTooltipDefault';
import AdminTooltipSVG from './AdminTooltipSVG';
import DiagramFooter from './DiagramFooter';
import DiagramHeader from './DiagramHeader';
import ImagePins from './ImagePins';
import {HEADERS} from './utilities/utilities';

import '../css/diagram.scss';

const PRODUCTS = 'products';
const PINS = 'pins';

const Diagram = ({
	datasetDisplayId,
	diagramId,
	enablePanZoom,
	enableResetZoom,
	imageSettings,
	imageURL,
	isAdmin,
	namespace,
	navigationController,
	newPinSettings,
	pinsEndpoint,
	productId,
	spritemap,
	type,
	zoomController,
}) => {
	const [pinImport, setPinImport] = useState([]);
	const [svgString, setSvgString] = useState('');
	const [imageState] = useState(imageURL);
	const [pinClickHandler, setPinClickHandler] = useState(false);
	const [addPinHandler, setAddPinHandler] = useState(false);
	const [removePinHandler, setRemovePinHandler] = useState({
		handler: false,
		pin: null,
	});
	const [diagramSizes, setDiagramSizes] = useState({k: 1, x: 0, y: 0});
	const [resetZoom, setResetZoom] = useState(false);
	const [zoomInHandler, setZoomInHandler] = useState(false);
	const [zoomOutHandler, setZoomOutHandler] = useState(false);
	const [changedScale, setChangedScale] = useState(false);
	const [scale, setScale] = useState(1);
	const [selectedOption, setSelectedOption] = useState(1);
	const [cPins, setCpins] = useState([]);
	const [selectedProductQuantity, setSelectedProductQuantity] = useState(1);
	const [selectedProductSequence, setSelectedProductSequence] = useState(1);
	const [showTooltip, setShowTooltip] = useState({
		details: {
			cx: 0,
			cy: 0,
			id: null,
			label: '',
			linkedToSku: 'sku',
			quantity: 1,
			sku: '',
			transform: '',
		},
		tooltip: null,
	});

	const [addNewPinState, setAddNewPinState] = useState({
		fill: newPinSettings.colorPicker.selectedColor,
		radius: newPinSettings.defaultRadius,
	});
	const [visible, setVisible] = useState(false);
	const {observer, onClose} = useModal({
		onClose: () => setVisible(false),
	});
	const [selectedProduct, setSelectedProduct] = useState(null);

	const importPinSchema = useCallback(() => {
		const textDatas = [];
		const pinData = [];
		const parser = new DOMParser();
		const xmlImage = parser.parseFromString(svgString, 'image/svg+xml');
		const rootLevel = xmlImage.getElementById('Livello_Testi');

		if (rootLevel) {
			const rects = rootLevel.getElementsByTagName('rect');
			const text = rootLevel.getElementsByTagName('text');

			Array.from(text).map((t) => {
				textDatas.push({
					label: t.textContent,
				});
			});

			Array.from(rects).map((r, i) => {
				pinData.push({
					cx: r.attributes.x.value / 2.78,
					cy: r.attributes.y.value / 2.78,
					id: i,
					label: textDatas[i].label,
				});
			});

			setPinImport(pinData);

			return pinData;
		}
	}, [svgString]);

	useEffect(() => {
		setCpins(pinImport);
	}, [pinImport]);

	const loadPins = useCallback(
		() =>
			fetch(`${pinsEndpoint}${PRODUCTS}/${productId}/${PINS}`, {
				headers: HEADERS,
			})
				.then((response) => response.json())
				.then((jsonResponse) => {
					const loadedPins = jsonResponse.items.map((item) => ({
						cx: item.positionX,
						cy: item.positionY,
						id: item.id,
						label: item.sequence,
					}));

					setCpins(loadedPins);
				}),
		[pinsEndpoint, productId]
	);

	const deletePin = (node) => {
		fetch(`${pinsEndpoint}${PINS}/${node.id}`, {
			headers: HEADERS,
			method: 'DELETE',
		});
	};

	const updatePin = (node) => {
		if (node.id) {
			return fetch(`${pinsEndpoint}${PINS}/${node.id}`, {
				body: JSON.stringify(node),
				headers: HEADERS,
				method: 'PATCH',
			});
		}

		return fetch(`${pinsEndpoint}${PRODUCTS}/${productId}/${PINS}`, {
			body: JSON.stringify(node),
			headers: HEADERS,
			method: 'POST',
		}).then(() => {
			if (datasetDisplayId) {
				Liferay.fire(UPDATE_DATASET_DISPLAY, {
					id: datasetDisplayId,
				});
			}
		});
	};

	const updateDiagramEntry = (node, entryId) => {
		return fetch(`${pinsEndpoint}diagramEntries/${entryId}`, {
			body: JSON.stringify(node),
			headers: HEADERS,
			method: 'PATCH',
		});
	};

	const updateMappedProduct = (node) => {
		return fetch(`${pinsEndpoint}${PRODUCTS}/${productId}/diagramEntries`, {
			headers: HEADERS,
			method: 'GET',
		})
			.then((res) => res.json())
			.then((jsonResponse) => {
				const pinToBeUpdated = jsonResponse.items.find(
					(pin) => pin.sequence === node.sequence
				);
				if (pinToBeUpdated) {
					return updateDiagramEntry(node, pinToBeUpdated?.id);
				}
				openToast({
					message: Liferay.Language.get(
						'unable-to-create-a-new-pin-definition'
					),
					type: 'danger',
				});
			});
	};

	const pinClickAction = (updatedPin) => {
		setShowTooltip({
			details: {
				cx: updatedPin.cx,
				cy: updatedPin.cy,
				id: updatedPin.id,
				label: updatedPin.label || '',
				linkedToSku: updatedPin.linkedToSku,
				quantity: updatedPin.quantity || 1,
				sku: updatedPin.sku,
				transform: updatedPin.transform,
			},
			tooltip: true,
		});
	};

	const handleTooltipSave = () => {
		if (type !== 'diagram.type.svg') {
			updatePin({
				diagramEntry: {
					diagram: showTooltip.details.linkedToSku === 'sku',
					productId: showTooltip.details.productId,
					quantity: showTooltip.details.quantity,
					sequence: selectedProductSequence,
					sku: selectedProduct.sku,
					skuUuid: showTooltip.details.id,
				},
				id: showTooltip.details.id,
				positionX: showTooltip.details.cx,
				positionY: showTooltip.details.cy,
				sequence: selectedProductSequence,
			});
		}
		else {
			updateMappedProduct({
				quantity: selectedProductQuantity,
				sequence: selectedProductSequence,
				sku: selectedProduct.sku,
			});
		}

		setShowTooltip({
			details: {
				cx: showTooltip.details.cx,
				cy: showTooltip.details.cy,
				id: showTooltip.details.id,
				label: showTooltip.details.label,
				linkedToSku: showTooltip.details.linkedToSku,
				quantity: showTooltip.details.quantity,
				sku: showTooltip.details.sku,
			},
			tooltip: false,
		});

		onClose();
	};

	useEffect(() => {
		fetch(imageState)
			.then((response) => response.text())
			.then((text) => {
				setSvgString(text);
				if (type === 'diagram.type.svg') {
					const schema = importPinSchema();
					setCpins(schema || []);
				}
				else if (type === 'diagram.type.default') {
					loadPins();
				}
			});
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [imageState, pinsEndpoint, productId, loadPins, type]);

	return imageState ? (
		<div className="diagram mx-auto">
			<ClayIconSpriteContext.Provider value={spritemap}>
				<DiagramHeader
					addNewPinState={addNewPinState}
					diagramId={diagramId}
					importPinSchema={importPinSchema}
					isAdmin={isAdmin}
					namespace={namespace}
					newPinSettings={newPinSettings}
					setAddNewPinState={setAddNewPinState}
					setAddPinHandler={setAddPinHandler}
					setSelectedOption={setSelectedOption}
					type={type}
				/>

				<ImagePins
					addNewPinState={addNewPinState}
					addPinHandler={addPinHandler}
					cPins={cPins}
					changedScale={changedScale}
					diagramSizes={diagramSizes}
					enablePanZoom={enablePanZoom}
					enableResetZoom={enableResetZoom}
					imageSettings={imageSettings}
					imageURL={imageURL}
					isAdmin={isAdmin}
					namespace={namespace}
					navigationController={navigationController}
					newPinSettings={newPinSettings}
					pinClickAction={pinClickAction}
					pinClickHandler={pinClickHandler}
					pinsEndpoint={pinsEndpoint}
					productId={productId}
					removePinHandler={removePinHandler}
					resetZoom={resetZoom}
					scale={scale}
					selectedOption={selectedOption}
					setAddPinHandler={setAddPinHandler}
					setChangedScale={setChangedScale}
					setCpins={setCpins}
					setDiagramSizes={setDiagramSizes}
					setPinClickHandler={setPinClickHandler}
					setRemovePinHandler={setRemovePinHandler}
					setResetZoom={setResetZoom}
					setScale={setScale}
					setSelectedOption={setSelectedOption}
					setSelectedProductSequence={setSelectedProductSequence}
					setShowTooltip={setShowTooltip}
					setVisible={setVisible}
					setZoomInHandler={setZoomInHandler}
					setZoomOutHandler={setZoomOutHandler}
					showTooltip={showTooltip}
					svgString={svgString}
					type={type}
					zoomController={zoomController}
					zoomInHandler={zoomInHandler}
					zoomOutHandler={zoomOutHandler}
				>
					{visible && type === 'diagram.type.svg' && (
						<ClayModal
							observer={observer}
							size="lg"
							spritemap={spritemap}
							status="info"
						>
							<ClayModal.Header>
								{showTooltip.details.label}
							</ClayModal.Header>
							<ClayModal.Body>
								<AdminTooltipSVG
									initialSequence={showTooltip.details.label}
									namespace={namespace}
									selectedProduct={selectedProduct}
									selectedProductQuantity={
										selectedProductQuantity
									}
									selectedProductSequence={
										selectedProductSequence
									}
									setSelectedProduct={setSelectedProduct}
									setSelectedProductQuantity={
										setSelectedProductQuantity
									}
									setSelectedProductSequence={
										setSelectedProductSequence
									}
									type={type}
								/>
							</ClayModal.Body>
							<ClayModal.Footer
								first={
									<ClayButton.Group spaced>
										<ClayButton
											displayType="link"
											onClick={() => {
												deletePin({
													id: showTooltip.details.id,
													positionX:
														showTooltip.details.cx,
													positionY:
														showTooltip.details.cy,
													sequence:
														showTooltip.details
															.label,
												});
												setRemovePinHandler({
													handler: true,
													pin: showTooltip.details.id,
												});
												setShowTooltip({
													details: {
														cx: null,
														cy: null,
														id: null,
														label: null,
														linkedToSku: 'sku',
														quantity: null,
														sku: '',
														transform: '',
													},
													tooltip: false,
												});
												onClose();
											}}
										>
											{Liferay.Language.get('delete')}
										</ClayButton>
										<ClayButton
											displayType="secondary"
											onClick={() => {
												setShowTooltip({
													details: {
														cx: '',
														cy: '',
														id: '',
														label: '',
														linkedToSku: 'sku',
														quantity: 1,
														sku: '',
														transform: '',
													},
													tooltip: false,
												});
												onClose();
											}}
										>
											{Liferay.Language.get('close')}
										</ClayButton>
									</ClayButton.Group>
								}
								last={
									<ClayButton
										displayType="primary"
										onClick={handleTooltipSave}
									>
										{Liferay.Language.get('save')}
									</ClayButton>
								}
							/>
						</ClayModal>
					)}
					{showTooltip.tooltip && type !== 'diagram.type.svg' && (
						<AdminTooltipDefault
							namespace={namespace}
							removePinHandler={removePinHandler}
							setRemovePinHandler={setRemovePinHandler}
							setShowTooltip={setShowTooltip}
							showTooltip={showTooltip}
							updatePin={updatePin}
						/>
					)}
				</ImagePins>

				<DiagramFooter
					changedScale={changedScale}
					enableResetZoom={enableResetZoom}
					isAdmin={isAdmin}
					selectedOption={selectedOption}
					setAddPinHandler={setAddPinHandler}
					setChangedScale={setChangedScale}
					setResetZoom={setResetZoom}
					setSelectedOption={setSelectedOption}
					setZoomInHandler={setZoomInHandler}
					setZoomOutHandler={setZoomOutHandler}
					type={type}
				/>
			</ClayIconSpriteContext.Provider>
		</div>
	) : (
		<div className="border-0 pt-0 sheet taglib-empty-result-message">
			<div className="taglib-empty-result-message-header"></div>
			<div className="sheet-text text-center">
				{Liferay.Language.get('no-diagram-is-loaded')}
			</div>
		</div>
	);
};

Diagram.defaultProps = {
	diagramId: 0,
	enablePanZoom: true,
	enableResetZoom: true,
	imageSettings: {
		height: '300px',
		width: '100%',
	},
	isAdmin: true,
	navigationController: {
		dragStep: 10,
		enable: false,
		enableDrag: false,
		position: {
			bottom: '15px',
			left: '',
			right: '50px',
			top: '',
		},
	},
	newPinSettings: {
		colorPicker: {
			defaultColors: [
				'AC68D7',
				'96D470',
				'F2EE8F',
				'F4C4A9',
				'F1A3BB',
				'67DC19',
				'959FEF',
				'A6C198',
				'FED998',
				'#38F95',
				'FD9945',
				'1A588B',
			],
			selectedColor: '0B5FFF',
			useNative: true,
		},
		defaultRadius: 10,
	},
	pins: [],
	pinsEndpoint: '/o/headless-commerce-admin-catalog/v1.0/',
	productId: 44206,
	showTooltip: {
		details: {
			cx: 0,
			cy: 0,
			id: 0,
			label: 'null',
			linkedToSku: 'sku',
			quantity: 1,
			sku: '',
			transform: '',
		},
		tooltip: false,
	},
	spritemap: './assets/clay/icons.svg',
	type: 'diagram.type.svg',
	zoomController: {
		enable: false,
		position: {
			bottom: '0px',
			left: '',
			right: '200px',
			top: '',
		},
	},
};

Diagram.propTypes = {
	cPins: PropTypes.arrayOf(
		PropTypes.shape({
			cx: PropTypes.double,
			cy: PropTypes.double,
			fill: PropTypes.string,
			id: PropTypes.number,
			label: PropTypes.string,
			linkedToSku: PropTypes.oneOf(['sku', 'diagram']),
			quantity: PropTypes.number,
			r: PropTypes.number,
			sku: PropTypes.string,
		})
	),
	diagramId: PropTypes.number,
	enablePanZoom: PropTypes.bool,
	enableResetZoom: PropTypes.bool,
	imageSettings: PropTypes.shape({
		height: PropTypes.string,
		width: PropTypes.string,
	}),
	imageURL: PropTypes.string.isRequired,
	isAdmin: PropTypes.bool.isRequired,
	namespace: PropTypes.string.isRequired,
	navigationController: PropTypes.shape({
		dragStep: PropTypes.number,
		enable: PropTypes.bool,
		enableDrag: PropTypes.bool,
		position: PropTypes.shape({
			bottom: PropTypes.string,
			left: PropTypes.string,
			right: PropTypes.string,
			top: PropTypes.string,
		}),
	}),
	newPinSettings: PropTypes.shape({
		colorPicker: PropTypes.shape({
			defaultColors: PropTypes.array,
			selectedColor: PropTypes.string,
			useNative: PropTypes.bool,
		}),
		defaultRadius: PropTypes.number,
	}),
	pinClickAction: PropTypes.func,
	productId: PropTypes.number,
	setPins: PropTypes.func,
	showTooltip: PropTypes.shape({
		details: PropTypes.shape({
			cx: PropTypes.double,
			cy: PropTypes.double,
			id: PropTypes.number,
			label: PropTypes.string,
			linkedToSku: PropTypes.oneOf(['sku', 'diagram']),
			quantity: PropTypes.number,
			sku: PropTypes.string,
			transform: PropTypes.string,
		}),
		tooltip: PropTypes.bool,
	}),
	spritemap: PropTypes.string,
	type: PropTypes.oneOf(['diagram.type.svg', 'diagram.type.default']),
	updatePin: PropTypes.func,
	zoomController: PropTypes.shape({
		enable: PropTypes.bool,
		position: PropTypes.shape({
			bottom: PropTypes.string,
			left: PropTypes.string,
			right: PropTypes.string,
			top: PropTypes.string,
		}),
	}),
};

export default Diagram;
