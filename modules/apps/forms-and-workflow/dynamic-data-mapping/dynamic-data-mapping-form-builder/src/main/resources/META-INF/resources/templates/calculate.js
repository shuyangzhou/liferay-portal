Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-form-builder/templates/calculate.es',
	function(MetalSoyBundle, Calculate) {
		if (!window.DDMCalculate) {
			window.DDMCalculate = {};
		}

		Calculate.default.forEach(function(item) {
			window.DDMCalculate[item.key] = item.component;
		});

		AUI.add('liferay-ddm-form-builder-calculate-template');
	}
);