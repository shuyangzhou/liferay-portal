;(function() {
	var LiferayAUI = Liferay.AUI;

	AUI().applyConfig(
		{
			groups: {
				'field-validation': {
					base: MODULE_PATH + '/',
					combine: Liferay.AUI.getCombine(),
					filter: LiferayAUI.getFilterConfig(),
					modules: {
						'liferay-ddm-form-field-validation': {
							condition: {
								trigger: 'liferay-ddm-form-renderer'
							},
							path: 'validation_field.js',
							requires: [
								'liferay-ddm-form-renderer-field'
							]
						},
						'liferay-ddm-form-field-validation-template': {
							condition: {
								trigger: 'liferay-ddm-form-renderer'
							},
							path: 'validation.js'
						}
					},
					root: MODULE_PATH + '/'
				}
			}
		}
	);
})();