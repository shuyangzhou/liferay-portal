Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-form-builder/templates/autocomplete.es',
	function(MetalSoyBundle, AutoComplete) {
		if (!window.DDMAutoComplete) {
			window.DDMAutoComplete = {};
		}

		AutoComplete.default.forEach(function(item) {
			window.DDMAutoComplete[item.key] = item.component;
		});

		AUI.add('liferay-ddm-form-builder-autocomplete-template');
	}
);