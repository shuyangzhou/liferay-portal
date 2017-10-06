Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-form-builder/templates/rule-builder.es',
	function(MetalSoyBundle, RuleBuilder) {
		if (!window.DDMRuleBuilder) {
			window.DDMRuleBuilder = {};
		}

		RuleBuilder.default.forEach(function(item) {
			window.DDMRuleBuilder[item.key] = item.component;
		});

		Liferay.fire('RuleBuilderLoaded');

		AUI.add('liferay-ddm-form-builder-rule-builder-template');
	}
);