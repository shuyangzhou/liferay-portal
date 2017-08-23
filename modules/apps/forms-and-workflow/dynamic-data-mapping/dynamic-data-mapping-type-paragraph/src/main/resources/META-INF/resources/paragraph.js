Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-type-paragraph/paragraph.es',
	function(MetalSoyBundle, Paragraph) {
		if (!window.DDMParagraph) {
			window.DDMParagraph = {};
		}

		window.DDMParagraph.render = Paragraph.default;

		AUI.add('liferay-ddm-form-field-paragraph-template');
	}
);