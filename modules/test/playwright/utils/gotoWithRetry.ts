/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Page} from '@playwright/test';

/**
 * Navigates like page.goto, surviving a navigation left pending by an earlier
 * step. An action whose success signal renders before its navigation commits,
 * like saving an object layout, leaves that navigation in flight, and when it
 * lands during a later goto the browser reports the goto as aborted or
 * interrupted even though nothing is wrong with the address. Let the pending
 * navigation land and go again: unlike settling for whatever page won, the
 * retry ends on the address the caller asked for.
 *
 * One retry is not enough. The wait between attempts resolves against the
 * document that is already loaded, so when the pending navigation has not
 * committed yet, the wait returns at once and the retry collides with the same
 * navigation again. Each failed attempt waits once more, so a retry follows
 * every landing until one goes through.
 */
export async function gotoWithRetry(
	page: Page,
	url: string,
	options?: Parameters<Page['goto']>[1]
) {
	for (let attempt = 0; ; attempt++) {
		try {
			return await page.goto(url, options);
		}
		catch (error) {
			const message = String(error.message);

			if (
				attempt >= 3 ||
				(!message.includes('interrupted by another navigation') &&
					!message.includes('net::ERR_ABORTED'))
			) {
				throw error;
			}

			await page.waitForLoadState();
		}
	}
}
