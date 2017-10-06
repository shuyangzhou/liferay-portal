Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-form-builder/templates/field-options-toolbar.es',
	function(MetalSoyBundle, FieldOptionsToolbar) {
		if (!window.DDMFieldSettingsToolbar) {
			window.DDMFieldSettingsToolbar = {};
		}

		FieldOptionsToolbar.default.forEach(function(item) {
			window.DDMFieldSettingsToolbar[item.key] = item.component;
		});

		AUI.add('liferay-ddm-form-builder-field-options-toolbar-template');
	}
);