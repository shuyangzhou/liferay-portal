Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-editor/editor.es',
	function(MetalSoyBundle, Editor) {
		if (!window.DDMEditor) {
			window.DDMEditor = {};
		}

		window.DDMEditor.render = Editor.default;

		AUI.add('liferay-ddm-form-field-editor-template');
	}
);