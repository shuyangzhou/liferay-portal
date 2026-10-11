/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';
import {render, screen} from '@testing-library/react';

// @ts-ignore

import fetchMock from 'fetch-mock';
import React from 'react';

import ActionBuilder from '../../components/ObjectAction/tabs/ActionBuilder';

afterAll(() => {
	jest.restoreAllMocks();
});

afterEach(() => {
	fetchMock.hardReset();
});

beforeEach(() => {
	fetchMock.mockGlobal();

	fetchMock.get(
		'/o/object-admin/v1.0/object-definitions/by-external-reference-code/',
		{
			body: {},
		}
	);
});

function renderActionBuilder({
	companyAdmin,
	values,
}: {
	companyAdmin: boolean;
	values: Partial<ObjectAction>;
}) {
	render(
		<ActionBuilder
			companyAdmin={companyAdmin}
			disableGroovyAction={false}
			errors={{}}
			hasUserNotificationHandler={false}
			isApproved={false}
			objectActionCodeEditorElements={[]}
			objectActionExecutors={[]}
			objectActionTriggers={[]}
			objectDefinitionExternalReferenceCode=""
			objectDefinitionId={0}
			objectDefinitionsRelationshipsURL=""
			objectFields={[]}
			scriptManagementConfigurationPortletURL=""
			setValues={jest.fn()}
			systemObject={false}
			validateExpressionURL=""
			values={values}
		/>
	);
}

describe('The ActionBuilder component should', () => {
	it('disable the webhook hosts allowed input when local network access is disabled', () => {
		renderActionBuilder({
			companyAdmin: true,
			values: {
				objectActionExecutorKey: 'webhook',
				parameters: {
					url: 'https://standalone.com',
				},
			},
		});

		expect(
			screen.getByLabelText('allow-local-network-access')
		).not.toBeChecked();
		expect(screen.getByLabelText('hosts-allowed')).toBeDisabled();
	});

	it('disable the webhook network access settings for non-administrators', () => {
		renderActionBuilder({
			companyAdmin: false,
			values: {
				objectActionExecutorKey: 'webhook',
				parameters: {
					url: 'http://127.0.0.1/webhook',
					urlHostsAllowed: '127.0.0.1',
					urlLocalNetworkAccessEnabled: true,
				},
			},
		});

		expect(
			screen.getByLabelText('allow-local-network-access')
		).toBeDisabled();
		expect(screen.getByLabelText('hosts-allowed')).toBeDisabled();
		expect(screen.getByLabelText('url', {exact: false})).toBeDisabled();
	});

	it('display the enable condition checkbox for the onAfterLogin trigger', () => {
		renderActionBuilder({
			companyAdmin: false,
			values: {
				objectActionTriggerKey: 'onAfterLogin',
			},
		});

		expect(screen.getByText('enable-condition')).toBeInTheDocument();
	});

	it('display the webhook network access settings for administrators', () => {
		renderActionBuilder({
			companyAdmin: true,
			values: {
				objectActionExecutorKey: 'webhook',
				parameters: {
					url: 'http://127.0.0.1/webhook',
					urlHostsAllowed: '127.0.0.1',
					urlLocalNetworkAccessEnabled: true,
				},
			},
		});

		expect(
			screen.getByLabelText('allow-local-network-access')
		).toBeChecked();
		expect(
			screen.getByLabelText('allow-local-network-access')
		).toBeEnabled();
		expect(screen.getByLabelText('hosts-allowed')).toHaveValue('127.0.0.1');
		expect(screen.getByLabelText('hosts-allowed')).toBeEnabled();
	});
});
