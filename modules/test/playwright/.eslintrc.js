/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

const path = require('path');

const config = {
	env: {
		es2021: true,
		node: true,
	},
	extends: ['plugin:@liferay/general'],
	parserOptions: {
		ecmaFeatures: {
			jsx: true,
		},
		ecmaVersion: 2023,
	},
	plugins: ['@liferay'],
	root: true,
	rules: {
		'@liferay/portal/no-global-fetch': 'off',
		'no-restricted-imports': [
			'error',
			{
				paths: [
					{
						message:
							'Import from the playwright-core source by relative path. The package entry point is built output that CI does not build.',
						name: '@liferay/playwright-core',
					},
				],
			},
		],
		'notice/notice': [
			'error',
			{
				nonMatchingTolerance: 0.95,
				onNonMatchingHeader: 'replace',
				templateFile: path.join(__dirname, 'copyright.js'),
			},
		],
	},
};

module.exports = config;
