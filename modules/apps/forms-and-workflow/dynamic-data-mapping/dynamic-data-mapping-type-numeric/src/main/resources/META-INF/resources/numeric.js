Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-numeric/numeric.es',
	function(MetalSoyBundle, Numeric) {
		if (!window.DDMNumeric) {
			window.DDMNumeric = {};
		}

		window.DDMNumeric.render = Numeric.default;

		AUI.add('liferay-ddm-form-field-numeric-template');
	}
);