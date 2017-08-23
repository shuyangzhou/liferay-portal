Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-radio/radio.es',
	function(MetalSoyBundle, Radio) {
		if (!window.DDMRadio) {
			window.DDMRadio = {};
		}

		window.DDMRadio.render = Radio.default;

		AUI.add('liferay-ddm-form-field-radio-template');
	}
);