Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-checkbox-multiple/checkbox-multiple.es',
	function(MetalSoyBundle, CheckboxMultiple) {
		if (!window.DDMCheckboxMultiple) {
			window.DDMCheckboxMultiple = {};
		}

		window.DDMCheckboxMultiple.render = CheckboxMultiple.default;

		AUI.add('liferay-ddm-form-field-checkbox-multiple-template');
	}
);