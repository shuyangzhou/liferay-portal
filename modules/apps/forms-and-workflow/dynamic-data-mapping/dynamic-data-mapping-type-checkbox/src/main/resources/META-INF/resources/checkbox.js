Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-checkbox/checkbox.es',
	function(MetalSoyBundle, Checkbox) {
		if (!window.DDMCheckbox) {
			window.DDMCheckbox = {};
		}

		window.DDMCheckbox.render = Checkbox.default;

		AUI.add('liferay-ddm-form-field-checkbox-template');
	}
);