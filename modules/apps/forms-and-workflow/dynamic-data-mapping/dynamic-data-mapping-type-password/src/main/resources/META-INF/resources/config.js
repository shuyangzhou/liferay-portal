;(function() {
	AUI().applyConfig(
		{
			groups: {
				'field-password': {
					base: MODULE_PATH + '/',
					combine: Liferay.AUI.getCombine(),
					filter: Liferay.AUI.getFilterConfig(),
					modules: {
						'liferay-ddm-form-field-password': {
							condition: {
								trigger: 'liferay-ddm-form-renderer'
							},
							path: 'password_field.js',
							requires: [
								'liferay-ddm-form-renderer-field'
							]
						},
						'liferay-ddm-form-field-password-template': {
							condition: {
								trigger: 'liferay-ddm-form-renderer'
							},
							path: 'password.soy.js',
							requires: [
								'soyutils'
							]
						}
					},
					root: MODULE_PATH + '/'
				}
			}
		}
	);
})();