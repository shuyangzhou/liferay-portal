Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-document-library/document-library.es',
	function(MetalSoyBundle, DocumentLibrary) {
		if (!window.DDMDocumentLibrary) {
			window.DDMDocumentLibrary = {};
		}

		window.DDMDocumentLibrary.render = DocumentLibrary.default;

		AUI.add('liferay-ddm-form-field-document-library-template');
	}
);