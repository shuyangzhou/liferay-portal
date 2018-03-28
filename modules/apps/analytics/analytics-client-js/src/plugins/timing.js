/**
 * Sends page load information on the window load event
 * @param {object} analytics The Analytics client
 */
function onload(analytics) {
	const perfData = window.performance.timing;
	const pageLoadTime = perfData.loadEventStart - perfData.navigationStart;

	const props = {
		pageLoadTime,
	};

	analytics.send('load', 'timing', props);
}

/**
 * Sends view duration information on the window unload event
 * @param {object} analytics The Analytics client
 */
function unload(analytics) {
	const perfData = window.performance.timing;
	const viewDuration = new Date().getTime() - perfData.navigationStart;

	const props = {
		viewDuration,
	};

	analytics.send('unload', 'timing', props);
}

/**
 * Plugin function that registers listeners against browser time events
 * @param {object} analytics The Analytics client
 */
function timing(analytics) {
	window.addEventListener('load', onload.bind(null, analytics));
	window.addEventListener('unload', unload.bind(null, analytics));
}

export {timing};
export default timing;