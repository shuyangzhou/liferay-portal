Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-form-renderer/form.es',
	function(MetalSoyBundle, Form) {
		if (!window.ddm) {
			window.ddm = {};
		}

		Form.default.forEach(function(item) {
			window.ddm[item.key] = item.component;
		});

		AUI.add('liferay-ddm-form-soy');

		Liferay.fire('DDMFormLoaded');
	}
);