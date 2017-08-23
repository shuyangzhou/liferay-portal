Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-key-value/key-value.es',
	function(MetalSoyBundle, KeyValue) {
		if (!window.DDMKeyValue) {
			window.DDMKeyValue = {};
		}

		window.DDMKeyValue.render = KeyValue.default;

		AUI.add('liferay-ddm-form-field-key-value-template');
	}
);