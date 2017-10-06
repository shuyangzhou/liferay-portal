Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-form-builder/templates/calculator.es',
	function(MetalSoyBundle, Calculator) {
		if (!window.DDMCalculator) {
			window.DDMCalculator = {};
		}

		window.DDMCalculator.render = Calculator.default;

		AUI.add('liferay-ddm-form-builder-calculator-template');
	}
);