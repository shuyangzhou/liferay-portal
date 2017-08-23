Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-date/date.es',
	function(MetalSoyBundle, Date) {
		if (!window.DDMDate) {
			window.DDMDate = {};
		}

		window.DDMDate.render = Date.default;

		AUI.add('liferay-ddm-form-field-date-template');
	}
);