Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-password/password.es',
	function(MetalSoyBundle, Password) {
		if (!window.DDMPassword) {
			window.DDMPassword = {};
		}

		window.DDMPassword.render = Password.default;

		AUI.add('liferay-ddm-form-field-password-template');
	}
);