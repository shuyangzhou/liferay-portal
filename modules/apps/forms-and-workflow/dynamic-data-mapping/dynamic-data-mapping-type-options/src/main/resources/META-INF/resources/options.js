Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-options/options.es',
	function(MetalSoyBundle, Options) {
		if (!window.DDMOptions) {
			window.DDMOptions = {};
		}

		window.DDMOptions.render = Options.default;

		AUI.add('liferay-ddm-form-field-options-template');
	}
);