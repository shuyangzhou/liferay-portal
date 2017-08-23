Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-captcha/captcha.es',
	function(MetalSoyBundle, Captcha) {
		if (!window.DDMCaptcha) {
			window.DDMCaptcha = {};
		}

		window.DDMCaptcha.render = Captcha.default;

		AUI.add('liferay-ddm-form-field-captcha-template');
	}
);