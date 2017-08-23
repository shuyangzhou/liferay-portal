;(function() {
	AUI().applyConfig({
		groups: {
			'field-document-library': {
				base: MODULE_PATH + '/',
				combine: Liferay.AUI.getCombine(),
				modules: {
					'liferay-ddm-form-field-document-library': {
						condition: {
							trigger: 'liferay-ddm-form-renderer'
						},
						path: 'document_library_field.js',
						requires: [
							'liferay-ddm-form-renderer-field'
						]
					},
					'liferay-ddm-form-field-document-library-template': {
						condition: {
							trigger: 'liferay-ddm-form-renderer'
						},
						path: 'document-library.js'
					}
				},
				root: MODULE_PATH + '/'
			}
		}
	});
})();