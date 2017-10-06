Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-form-builder/templates/data-provider-parameter.es',
	function(MetalSoyBundle, DataProviderParameter) {
		if (!window.DDMDataProviderParameter) {
			window.DDMDataProviderParameter = {};
		}

		DataProviderParameter.default.forEach(function(item) {
			window.DDMDataProviderParameter[item.key] = item.component;
		});

		AUI.add('liferay-ddm-form-builder-data-provider-parameter-template');
	}
);