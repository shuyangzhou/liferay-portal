/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

/**
 * Serves the repository's own learn resources so that a portal started with
 * learn.resources.mode=dev resolves them without reaching the internet.
 * LearnMessageUtil swallows a failed fetch into an empty resource set and caches
 * it for four hours, and LearnMessage renders nothing at all when a resource
 * carries no url, so a test that clicks a learn link has nothing to click and
 * waits out its whole timeout.
 *
 * Usage: node learnResourcesServer.js <learn-resources-data-dir> <port>
 */

const fs = require('fs');
const http = require('http');
const path = require('path');

const directory = process.argv[2];
const port = Number(process.argv[3]);

http.createServer((request, response) => {
	const fileName = path.basename(request.url.split('?')[0]);

	fs.readFile(path.join(directory, fileName), (error, data) => {
		if (error) {

			// An unknown resource answers an empty object, which is what the
			// portal's own fetch failure produces, rather than an error the
			// caller cannot read.

			response.writeHead(404, {'Content-Type': 'application/json'});
			response.end('{}');
		}
		else {
			response.writeHead(200, {'Content-Type': 'application/json'});
			response.end(data);
		}
	});
}).listen(port);
