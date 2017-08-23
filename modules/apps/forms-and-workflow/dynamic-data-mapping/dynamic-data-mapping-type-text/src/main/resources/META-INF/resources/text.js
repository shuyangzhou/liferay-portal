Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-text/text.es',
	function(MetalSoyBundle, Text) {
		if (!window.DDMText) {
			window.DDMText = {};
		}

		window.DDMText.render = Text.default;

		AUI.add('liferay-ddm-form-field-text-template');
	}
);