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

import {
	FLUSH_INTERVAL,
	LIMIT_FAILED_ATTEMPTS,
	QUEUE_PRIORITY_DEFAULT,
	REQUEST_TIMEOUT,
} from './utils/constants';
import {getRetryDelay} from './utils/delay';

/**
 * A Queue Config.
 *
 * @typedef {Object} QueueConfig
 * @property {string} endpointUrl - Url to send the items
 * @property {string} name - The name to idetify the queue, it must be unique.
 * @property {number} [priority=1] - Priority of this queue.
 */

/**
 * Client used to abstract communication with the Analytics Endpoint. It exposes the send
 * as only valid entry point for sending and modifying requests.
 * It process queues to send messages to the server. It will periodically run a task to
 * process messages in its queue.
 */
class Client {
	constructor(config = {}) {
		this.attemptNumber = 1;
		this.initialDelay = config.delay || FLUSH_INTERVAL;
		this.delay = this.initialDelay;
		this.processing = false;
		this.queues = [];

		this._startsFlushLoop();
	}

	/**
	 * Returns a Request object with all data from the analytics instance
	 * including the batched event objects
	 * @param {Object} analytics The Analytics instance from which the data is extracted
	 * @param {string} userId The userId string representation
	 * @protected
	 * @returns {Object} Parameters of the request to be sent.
	 */
	_getRequestParameters() {
		const headers = {'Content-Type': 'application/json'};

		return {
			cache: 'default',
			credentials: 'same-origin',
			headers,
			method: 'POST',
			mode: 'cors',
		};
	}

	/**
	 * Returns the Response object or a rejected Promise based on the
	 * HTTP Response Code of the Response object
	 * @param {Object} response Response
	 * @returns {Object} Promise
	 */
	_validateResponse(response) {
		if (!response.ok) {
			response = new Promise((_, reject) => reject(response));
		}

		return response;
	}

	/**
	 * Returns a resolved or rejected promise as per the response status or if the request times out.
	 * @param {Object} analytics The Analytics instance from which the data is extracted
	 * @param {string} userId The userId string representation
	 * @returns {Object} Promise object representing the result of the operation
	 */
	sendWithTimeout({payload, timeout, url}) {
		return Promise.race([
			this.send({payload, url}),
			this._timeout(timeout),
		]);
	}

	/**
	 * Send a request with given payload and url.
	 */
	send({payload, url}) {
		const parameters = this._getRequestParameters();

		Object.assign(parameters, {
			body: JSON.stringify(payload),
		});

		return fetch(url, parameters).then(this._validateResponse);
	}

	/**
	 * Returns a promise that times out after the given time limit is exceeded
	 * @param {number} timeout
	 * @returns {Object} Promise
	 */
	_timeout(timeout) {
		return new Promise((_, reject) => setTimeout(reject, timeout));
	}

	/**
	 * Add a queue to be processed by the client
	 * @param {MessageQueue} queueInstance
	 * @param {QueueConfig} config
	 */
	addQueue(queueInstance, config) {
		this.queues.push(Object.assign(config, {instance: queueInstance}));
		this.queues.sort(this._prioritize);
	}

	/**
	 * Remove a queue
	 * @param {string} queueName
	 */
	removeQueue(queueName) {
		this.queues = this.queues.filter(({name}) => queueName !== name);
	}

	/**
	 * Function to order queues by priority
	 * @param {Object} queueA
	 * @param {Object} queueB
	 */
	_prioritize(
		{priority: pA = QUEUE_PRIORITY_DEFAULT},
		{priority: pB = QUEUE_PRIORITY_DEFAULT}
	) {
		return pB - pA;
	}

	/**
	 * Increase the interval time and restart processing loop
	 */
	onRequestFail() {
		this.delay = getRetryDelay(++this.attemptNumber, LIMIT_FAILED_ATTEMPTS);
		this._startsFlushLoop();
	}

	/**
	 * Reset interval time and restart processing loop
	 */
	onRequestSuccess() {
		this.attemptNumber = 1;

		if (this.delay !== this.initialDelay) {
			this.delay = this.initialDelay;
			this._startsFlushLoop();
		}
	}

	/**
	 * Go through queues and send their messages.
	 * If a queue fail, the processing loop is delayed
	 * and the next queues are not processed.
	 *
	 * Note: Because we are using a ProcessLock, no other process should
	 * be able to acquire a lock for a particular key to run its callback
	 * until the process with the active lock releases it.
	 *
	 */
	flush() {
		if (this.processing) {
			return;
		}

		this.queues.reduce(
			(previousPromise, {endpointUrl, instance: queue}) => {
				return previousPromise.then(
					() => {
						if (!queue.hasMessages()) {
							return Promise.resolve();
						}

						return queue.acquireLock().then((success) => {
							if (!success) {
								return Promise.resolve();
							}

							this.processing = true;
							const messages = queue.getMessages();
							const releaseLock = () =>
								queue.releaseLock().then(() => {
									this.processing = false;
								});

							if (!messages.length) {
								releaseLock();

								return Promise.resolve();
							}

							return Promise.all(
								messages.map((payload) =>
									this.sendWithTimeout({
										payload,
										timeout: REQUEST_TIMEOUT,
										url: endpointUrl,
									}).then(() => {
										queue._dequeue(payload.id);
									})
								)
							)
								.then(() => {
									this.onRequestSuccess();
									releaseLock();

									return Promise.resolve();
								})
								.catch(() => {
									releaseLock();

									return Promise.reject();
								});
						});
					},
					() => {
						this.onRequestFail();
					}
				);
			},
			Promise.resolve()
		);
	}

	/**
	 * Start a timer to process messages at a specified interval.
	 */
	_startsFlushLoop() {
		if (this.processInterval) {
			clearInterval(this.processInterval);
		}

		this.processInterval = setInterval(() => this.flush(), this.delay);
	}

	/**
	 * Method for clearing queues and any scheduled processing.
	 */
	dispose() {
		if (this.processInterval) {
			clearInterval(this.processInterval);
		}

		this.delay = this.initialDelay;
		this.queues = [];
	}
}

export {Client};
export default Client;
