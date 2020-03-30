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

import {waitForElementToBeRemoved} from '@testing-library/dom';
import {act, cleanup, fireEvent, render} from '@testing-library/react';
import {createMemoryHistory} from 'history';
import React from 'react';
import {HashRouter} from 'react-router-dom';

import {AppContextProvider} from '../../../../src/main/resources/META-INF/resources/js/AppContext.es';
import ListCustomObjects from '../../../../src/main/resources/META-INF/resources/js/pages/custom-object/ListCustomObjects.es';
import * as time from '../../../../src/main/resources/META-INF/resources/js/utils/time.es';
import * as toast from '../../../../src/main/resources/META-INF/resources/js/utils/toast.es';
import {RESPONSES} from '../../constants.es';

describe('ListCustomObject', () => {
	const ListCustomObjectsWithRouter = ({history = createMemoryHistory()}) => (
		<AppContextProvider value={{}}>
			<div className="tools-control-group">
				<div className="control-menu-level-1-heading" />
			</div>

			<HashRouter>
				<ListCustomObjects history={history} />
			</HashRouter>
		</AppContextProvider>
	);

	let spySuccessToast;
	let spyFromNow;

	beforeEach(() => {
		jest.useFakeTimers();
		spyFromNow = jest
			.spyOn(time, 'fromNow')
			.mockImplementation(() => 'months ago');
		spySuccessToast = jest
			.spyOn(toast, 'successToast')
			.mockImplementation();
	});

	afterEach(() => {
		cleanup();
		jest.clearAllTimers();
		jest.restoreAllMocks();
	});

	afterAll(() => {
		jest.useRealTimers();
	});

	it('renders with empty state and create new object with popover checkbox enabled', async () => {
		fetch
			.mockResponseOnce(JSON.stringify(RESPONSES.NO_ITEMS))
			.mockResponseOnce(JSON.stringify({}));

		const {
			asFragment,
			container,
			queryAllByRole,
			queryAllByText,
			queryByText,
		} = render(<ListCustomObjectsWithRouter />);

		await waitForElementToBeRemoved(() =>
			document.querySelector('span.loading-animation')
		);

		expect(spyFromNow).not.toHaveBeenCalled();

		expect(
			queryByText(
				'custom-objects-define-the-types-of-data-your-business-application-needs'
			)
		).toBeTruthy();
		expect(queryByText('there-are-no-custom-objects-yet')).toBeTruthy();

		expect(fetch.mock.calls.length).toEqual(1);

		const input = container.querySelector('#customObjectNameInput');
		const checkbox = container.querySelector('input[type=checkbox]');
		const [newCustomObject] = queryAllByText('new-custom-object');

		expect(input.value).toBe('');
		expect(checkbox.checked).toBe(true);

		fireEvent.click(newCustomObject);

		window.Liferay.Util.PortletURL = {
			createRenderURL: jest.fn(),
		};

		fireEvent.change(input, {target: {value: 'test'}});

		expect(input.value).toBe('test');

		const form = queryAllByRole('form');

		fireEvent.submit(form[0]);

		expect(fetch.mock.calls.length).toEqual(2);
		expect(asFragment()).toMatchSnapshot();
	});

	it('renders with empty state and create new object with popver checkbox disabled', async () => {
		fetch
			.mockResponseOnce(JSON.stringify(RESPONSES.NO_ITEMS))
			.mockResponseOnce(JSON.stringify({}));

		const {asFragment, container, queryAllByText} = render(
			<ListCustomObjectsWithRouter />
		);

		await waitForElementToBeRemoved(() =>
			document.querySelector('span.loading-animation')
		);

		expect(spyFromNow).not.toHaveBeenCalled();
		expect(fetch.mock.calls.length).toEqual(1);

		const continueButton = container.querySelector('.btn-sm.btn-primary');
		const input = container.querySelector('#customObjectNameInput');
		const checkbox = container.querySelector('input[type=checkbox]');
		const [newCustomObject] = queryAllByText('new-custom-object');

		const [cancel] = queryAllByText('cancel');

		expect(input.value).toBe('');
		expect(checkbox.checked).toBe(true);
		expect(container.querySelector('.form-group.has-error')).toBeFalsy();

		fireEvent.click(newCustomObject);
		expect(container.querySelector('.popover.hide')).toBeFalsy();

		fireEvent.click(cancel);
		expect(container.querySelector('.popover.hide')).toBeTruthy();

		fireEvent.click(newCustomObject);
		fireEvent.click(continueButton);
		fireEvent.click(checkbox);

		expect(container.querySelector('.form-group.has-error')).toBeTruthy();

		fireEvent.change(input, {target: {value: 'test'}});

		expect(input.value).toBe('test');
		expect(checkbox.checked).toBe(false);

		fireEvent.click(continueButton);

		expect(fetch.mock.calls.length).toEqual(2);
		expect(asFragment()).toMatchSnapshot();
	});

	it('renders with one item and removes it', async () => {
		const ONE_ITEM = {
			...RESPONSES.ONE_ITEM,
			items: [
				{
					dateCreated: '2020-01-01T00:00:00.262Z',
					dateModified: '2020-01-01T00:00:00.262Z',
					id: 1,
					name: {
						en_US: 'Item Name',
					},
				},
			],
		};

		fetch
			.mockResponseOnce(JSON.stringify(ONE_ITEM))
			.mockResponseOnce(JSON.stringify({}))
			.mockResponseOnce(JSON.stringify(RESPONSES.NO_ITEMS));

		const confirmationDialog = jest
			.spyOn(window, 'confirm')
			.mockImplementation(() => true);

		const {asFragment, container, queryByText} = render(
			<ListCustomObjectsWithRouter />
		);

		await waitForElementToBeRemoved(() =>
			document.querySelector('span.loading-animation')
		);

		expect(spyFromNow).toHaveBeenCalled();

		expect(
			queryByText(
				'custom-objects-define-the-types-of-data-your-business-application-needs'
			)
		).toBeFalsy();
		expect(queryByText('there-are-no-custom-objects-yet')).toBeFalsy();

		expect(fetch.mock.calls.length).toBe(1);

		expect(queryByText('Item Name')).toBeTruthy();

		const deleteButton = queryByText('delete');

		fireEvent.click(
			container.querySelector(
				'.dropdown-toggle.page-link.btn.btn-unstyled'
			)
		);
		expect(document.querySelector('.dropdown-menu.show')).toBeTruthy();
		expect(
			document.querySelectorAll('.dropdown-menu.show .dropdown-item')
				.length
		).toBe(5);

		await act(async () => {
			fireEvent.click(deleteButton);
		});

		expect(confirmationDialog.mock.calls.length).toBe(1);
		expect(spySuccessToast.mock.calls.length).toBe(1);
		expect(fetch.mock.calls.length).toBe(3);

		expect(
			queryByText(
				'custom-objects-define-the-types-of-data-your-business-application-needs'
			)
		).toBeTruthy();
		expect(queryByText('there-are-no-custom-objects-yet')).toBeTruthy();
		expect(asFragment()).toMatchSnapshot();
	});

	it('renders with one item and updates its permissions', async () => {
		const permissionItem = {
			availableLanguages: ['en-US'],
			dateCreated: '2020-03-05T20:06:51Z',
			dateModified: '2020-03-05T20:06:51Z',
			description: '',
			id: 30302,
			name: 'Account Administrator',
			roleType: 'regular',
		};

		const permissionResponse = {
			...RESPONSES.ONE_ITEM,
			actions: {},
			items: [permissionItem],
		};

		fetch
			.mockResponseOnce(JSON.stringify(RESPONSES.ONE_ITEM))
			.mockResponseOnce(
				JSON.stringify({
					dataDefinitionId: 38408,
					dataRecordCollectionKey: '38407',
					description: {},
					id: 38410,
					name: {
						en_US: 'Name',
					},
					siteId: 20129,
				})
			)
			.mockResponseOnce(JSON.stringify(permissionResponse))
			.mockResponse(JSON.stringify({}));

		const {asFragment, container, queryByText} = render(
			<ListCustomObjectsWithRouter />
		);

		await waitForElementToBeRemoved(() =>
			document.querySelector('span.loading-animation')
		);

		expect(spyFromNow).toHaveBeenCalled();

		expect(fetch.mock.calls.length).toBe(1);
		expect(container.querySelectorAll('tbody tr').length).toBe(1);

		const permission = queryByText('app-permissions');

		await act(async () => {
			fireEvent.click(permission);
		});

		await act(async () => {
			jest.runAllTimers();
		});

		expect(fetch.mock.calls.length).toBe(4);
		expect(queryByText(permissionItem.name)).toBeTruthy();

		const checkboxes = document.querySelectorAll(
			'.modal-body input[type="checkbox"]'
		);

		expect(checkboxes.length).toBe(4);

		const [addPermission] = checkboxes;

		expect(addPermission.checked).toBe(false);

		fireEvent.click(addPermission);

		expect(addPermission.checked).toBe(true);

		const save = queryByText('save');

		await act(async () => {
			fireEvent.click(save);
		});

		expect(fetch.mock.calls.length).toBe(6);
		expect(spySuccessToast.mock.calls.length).toBe(1);
		expect(asFragment()).toMatchSnapshot();
	});

	it('renders with data and hit actions', async () => {
		fetch.mockResponseOnce(JSON.stringify(RESPONSES.ONE_ITEM));

		const history = createMemoryHistory();

		const {asFragment, baseElement} = render(
			<ListCustomObjectsWithRouter history={history} />
		);

		expect(history.length).toBe(1);
		expect(history.location.pathname).toBe('/');

		await waitForElementToBeRemoved(() =>
			baseElement.querySelector('span.loading-animation')
		);

		expect(spyFromNow).toHaveBeenCalled();

		const dropDownMenu = baseElement.querySelectorAll('.dropdown-menu');
		const actions = dropDownMenu[1].querySelectorAll('.dropdown-item');

		expect(actions.length).toBe(5);
		expect(history.length).toBe(1);
		expect(history.location.pathname).toBe('/');

		const [formView, tableView, apps] = actions;

		fireEvent.click(formView);

		expect(history.length).toBe(2);
		expect(history.location.pathname).toBe('/custom-object/1/form-views');

		fireEvent.click(tableView);

		expect(history.length).toBe(3);
		expect(history.location.pathname).toBe('/custom-object/1/table-views');

		fireEvent.click(apps);

		expect(history.length).toBe(4);
		expect(history.location.pathname).toBe('/custom-object/1/apps');
		expect(asFragment()).toMatchSnapshot();
	});
});
