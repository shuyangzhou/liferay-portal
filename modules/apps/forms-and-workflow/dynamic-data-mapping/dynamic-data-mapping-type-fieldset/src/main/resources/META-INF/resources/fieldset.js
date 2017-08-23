Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-fieldset/fieldset.es',
	function(MetalSoyBundle, Fieldset) {
		if (!window.DDMFieldset) {
			window.DDMFieldset = {};
		}

		window.DDMFieldset.render = Fieldset.default;

		AUI.add('liferay-ddm-form-field-fieldset-template');
	}
);