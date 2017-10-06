Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-grid/grid.es',
	function(MetalSoyBundle, Grid) {
		if (!window.DDMGrid) {
			window.DDMGrid = {};
		}

		if (!window.DDMSelect) {
			window.DDMSelect = {};
		}

		Grid.default.forEach(function(item) {
			window.DDMGrid[item.key] = item.component;
		});

		AUI.add('liferay-ddm-form-field-grid-template');
	}
);