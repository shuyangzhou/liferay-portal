Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-validation/validation.es',
	function(MetalSoyBundle, Validation) {
		if (!window.DDMValidation) {
			window.DDMValidation = {};
		}

		window.DDMValidation.render = Validation.default;

		AUI.add('liferay-ddm-form-field-validation-template');
	}
);