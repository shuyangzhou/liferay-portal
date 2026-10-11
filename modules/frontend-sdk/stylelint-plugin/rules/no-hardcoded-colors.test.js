/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

const assert = require('node:assert');
const {describe, it} = require('node:test');
const stylelint = require('stylelint');

const config = {
	plugins: [require.resolve('../index.js')],
	rules: {
		'liferay/no-hardcoded-colors': true,
	},
};

function lint(code) {
	return stylelint
		.lint({code, config})
		.then((result) => result.results[0].warnings);
}

describe('liferay/no-hardcoded-colors', () => {
	it('accepts a color that references a design token', async () => {
		assert.deepStrictEqual(
			await lint('a { color: var(--cadmin-body-color); }'),
			[]
		);
	});

	it('accepts a token reference with a hardcoded fallback', async () => {
		assert.deepStrictEqual(
			await lint('a { color: var(--cadmin-body-color, #fff); }'),
			[]
		);
	});

	it('accepts a light-dark() value', async () => {
		assert.deepStrictEqual(
			await lint('a { color: light-dark(#fff, #111116); }'),
			[]
		);
	});

	it('accepts color keywords', async () => {
		assert.deepStrictEqual(
			await lint('a { color: inherit; border-color: transparent; }'),
			[]
		);
	});

	it('accepts a hex inside a url() data URI', async () => {
		assert.deepStrictEqual(
			await lint(
				'a { background-image: url("data:image/svg+xml,<svg fill=\'%23fff\'/>"); }'
			),
			[]
		);
	});

	it('accepts a multi-value property of only tokens', async () => {
		assert.deepStrictEqual(
			await lint('a { background: var(--foo), var(--bar); }'),
			[]
		);
	});

	it('accepts a relative color derived from a token', async () => {
		assert.deepStrictEqual(
			await lint('a { background: rgb(from var(--shimmer) r g b / 0); }'),
			[]
		);
	});

	it('accepts a hex inside a content string', async () => {
		assert.deepStrictEqual(await lint('a { content: "#fff"; }'), []);
	});

	it('rejects a hardcoded color mixed with a token in a shorthand', async () => {
		const reports = await lint('a { background: var(--foo), #fff; }');

		assert.strictEqual(reports.length, 1);
		assert.strictEqual(reports[0].rule, 'liferay/no-hardcoded-colors');
	});

	it('rejects a hardcoded color in a gradient beside a token', async () => {
		const reports = await lint(
			'a { background: linear-gradient(0deg, #fff, #000), var(--y); }'
		);

		assert.strictEqual(reports.length, 1);
		assert.strictEqual(reports[0].rule, 'liferay/no-hardcoded-colors');
	});

	it('rejects a hardcoded color mixed with a token in a space-separated shorthand', async () => {
		const reports = await lint('a { background: var(--foo) #fff; }');

		assert.strictEqual(reports.length, 1);
		assert.strictEqual(reports[0].rule, 'liferay/no-hardcoded-colors');
	});

	it('rejects a hardcoded color in a gradient that also contains a token', async () => {
		const reports = await lint(
			'a { background: linear-gradient(0deg, var(--x), #000); }'
		);

		assert.strictEqual(reports.length, 1);
		assert.strictEqual(reports[0].rule, 'liferay/no-hardcoded-colors');
	});

	it('rejects a hardcoded color in a custom function whose name ends in a token keyword', async () => {
		const reports = await lint('a { color: custom-url(#abc); }');

		assert.strictEqual(reports.length, 1);
		assert.strictEqual(reports[0].rule, 'liferay/no-hardcoded-colors');
	});

	it('rejects a hardcoded hex color', async () => {
		const reports = await lint('a { color: #fff; }');

		assert.strictEqual(reports.length, 1);
		assert.strictEqual(reports[0].rule, 'liferay/no-hardcoded-colors');
	});

	it('names the offending color in the message', async () => {
		const reports = await lint('a { color: #868896; }');

		assert.ok(reports[0].text.includes('"#868896"'));
	});

	it('rejects a hardcoded rgb() color', async () => {
		const reports = await lint('a { background-color: rgb(0, 0, 0); }');

		assert.strictEqual(reports.length, 1);
		assert.strictEqual(reports[0].rule, 'liferay/no-hardcoded-colors');
	});

	it('rejects a hardcoded hsl() color in a border shorthand', async () => {
		const reports = await lint('a { border: 1px solid hsl(0, 0%, 0%); }');

		assert.strictEqual(reports.length, 1);
		assert.strictEqual(reports[0].rule, 'liferay/no-hardcoded-colors');
	});
});
