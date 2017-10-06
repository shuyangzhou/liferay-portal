Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-select/select.es',
	function(MetalSoyBundle, Select) {
		if (!window.DDMSelect) {
			window.DDMSelect = {};
		}

		Select.default.forEach(function(item) {
			window.DDMSelect[item.key] = item.component;
		});

		AUI.add('liferay-ddm-form-field-select-template');
	}
);