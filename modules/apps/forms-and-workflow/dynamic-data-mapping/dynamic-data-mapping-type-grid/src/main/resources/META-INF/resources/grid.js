Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-grid/grid.es',
	function(MetalSoyBundle, Grid) {
		if (!window.DDMGrid) {
			window.DDMGrid = {};
		}

		window.DDMGrid.render = Grid.default;

		AUI.add('liferay-ddm-form-field-grid-template');
	}
);