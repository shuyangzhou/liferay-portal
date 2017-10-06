Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-form-builder/templates/rule.es',
	function(MetalSoyBundle, Rule) {
		if (!window.DDMRule) {
			window.DDMRule = {};
		}

		Rule.default.forEach(function(item) {
			window.DDMRule[item.key] = item.component;
		});

		AUI.add('liferay-ddm-form-builder-rule-template');
	}
);