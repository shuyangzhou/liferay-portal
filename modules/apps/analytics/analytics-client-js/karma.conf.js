const liferayKarmaConfig = require('liferay-karma-config');

module.exports = function(config) {
	liferayKarmaConfig(config);

	config.babelPreprocessor = {
		options: {
			presets: ['env'],
			plugins: ['transform-object-rest-spread'],
		},
	};

	config.files = [
		'build/analytics-all-min.js',
		'node_modules/fetch-mock/es5/client-browserified.js',
		'test/**/*.test.js',
	];

	config.frameworks = ['mocha', 'chai', 'sinon'];

	config.preprocessors['test/**/*.js'] = ['babel'];

	config.singleRun = true;
};