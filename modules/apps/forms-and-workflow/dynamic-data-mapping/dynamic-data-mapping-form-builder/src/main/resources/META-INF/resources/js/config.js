;(function() {
	var LiferayAUI = Liferay.AUI;

	AUI().applyConfig(
		{
			groups: {
				'form-builder': {
					base: MODULE_PATH + '/js/',
					combine: Liferay.AUI.getCombine(),
					filter: LiferayAUI.getFilterConfig(),
					modules: {
						'liferay-ddm-form-builder': {
							path: 'form_builder.js',
							requires: [
								'aui-form-builder',
								'aui-form-builder-pages',
								'liferay-ddm-form-builder-confirmation-dialog',
								'liferay-ddm-form-builder-field-list',
								'liferay-ddm-form-builder-field-options-toolbar',
								'liferay-ddm-form-builder-field-settings-sidebar',
								'liferay-ddm-form-builder-field-support',
								'liferay-ddm-form-builder-field-types-modal',
								'liferay-ddm-form-builder-layout-builder-support',
								'liferay-ddm-form-builder-layout-deserializer',
								'liferay-ddm-form-builder-layout-visitor',
								'liferay-ddm-form-builder-pages-manager',
								'liferay-ddm-form-builder-rule',
								'liferay-ddm-form-builder-rule-builder',
								'liferay-ddm-form-builder-util',
								'liferay-ddm-form-field-types',
								'liferay-ddm-form-renderer'
							]
						},
						'liferay-ddm-form-builder-action': {
							path: 'form_builder_action.js',
							requires: ['liferay-ddm-form-builder-action-template', 'liferay-ddm-form-renderer-field']
						},
						'liferay-ddm-form-builder-action-autofill': {
							path: 'form_builder_action_autofill.js',
							requires: [
								'aui-component',
								'aui-io-request',
								'liferay-ddm-form-builder-action',
								'liferay-ddm-form-builder-data-provider-parameter-template'
							]
						},
						'liferay-ddm-form-builder-action-calculate': {
							path: 'form_builder_action_calculate.js',
							requires: [
								'liferay-ddm-form-builder-action',
								'liferay-ddm-form-builder-calculate-template',
								'liferay-ddm-form-builder-calculator'
							]
						},
						'liferay-ddm-form-builder-action-factory': {
							path: 'form_builder_action_factory.js',
							requires: [
								'liferay-ddm-form-builder-action-autofill',
								'liferay-ddm-form-builder-action-calculate',
								'liferay-ddm-form-builder-action-jump-to-page',
								'liferay-ddm-form-builder-action-property'
							]
						},
						'liferay-ddm-form-builder-action-jump-to-page': {
							path: 'form_builder_action_jump_to_page.js',
							requires: ['liferay-ddm-form-builder-action']
						},
						'liferay-ddm-form-builder-action-property': {
							path: 'form_builder_action_property.js',
							requires: ['liferay-ddm-form-builder-action']
						},
						'liferay-ddm-form-builder-autocomplete-template': {
							path: '../templates/autocomplete.js'
						},
						'liferay-ddm-form-builder-calculate-template': {
							path: '../templates/calculate.js'
						},
						'liferay-ddm-form-builder-calculator': {
							path: 'form_builder_calculator.js',
							requires: [
								'liferay-ddm-form-builder-calculator-template',
								'liferay-ddm-form-field-select'
							]
						},
						'liferay-ddm-form-builder-calculator-template': {
							path: '../templates/calculator.js'
						},
						'liferay-ddm-form-builder-confirmation-dialog': {
							path: 'form_builder_confirmation_dialog.js',
							requires: []
						},
						'liferay-ddm-form-builder-data-provider-parameter-template': {
							path: '../templates/data-provider-parameter.js'
						},
						'liferay-ddm-form-builder-definition-serializer': {
							path: 'form_definition_serializer.js',
							requires: [
								'json',
								'liferay-ddm-form-builder-layout-visitor'
							]
						},
						'liferay-ddm-form-builder-field-list': {
							path: 'form_builder_field_list.js',
							requires: [
								'aui-form-builder-field-list'
							]
						},
						'liferay-ddm-form-builder-field-options-toolbar': {
							path: 'form_builder_field_options_toolbar.js',
							requires: ['liferay-ddm-form-builder-field-options-toolbar-template']
						},
						'liferay-ddm-form-builder-field-options-toolbar-template': {
							path: '../templates/field-options-toolbar.js'
						},
						'liferay-ddm-form-builder-field-settings-form': {
							path: 'form_builder_field_settings_form.js',
							requires: [
								'liferay-ddm-form-builder-autocomplete-template',
								'liferay-ddm-form-renderer',
								'liferay-ddm-soy-template-util',
								'liferay-form'
							]
						},
						'liferay-ddm-form-builder-field-settings-sidebar': {
							path: 'form_builder_field_settings_sidebar.js',
							requires: ['aui-tabview', 'liferay-ddm-form-builder-sidebar', 'liferay-ddm-form-renderer-types']
						},
						'liferay-ddm-form-builder-field-support': {
							path: 'form_builder_field_support.js',
							requires: [
								'liferay-ddm-form-builder-field-settings-form',
								'liferay-ddm-form-builder-settings-retriever'
							]
						},
						'liferay-ddm-form-builder-field-types-modal': {
							path: 'form_builder_field_types_modal.js',
							requires: [
								'aui-form-builder-field-types-modal',
								'liferay-ddm-form-builder-modal-support'
							]
						},
						'liferay-ddm-form-builder-layout-builder-support': {
							path: 'form_builder_layout_builder_support.js',
							requires: []
						},
						'liferay-ddm-form-builder-layout-deserializer': {
							path: 'form_layout_deserializer.js',
							requires: [
								'aui-form-builder-field-list',
								'aui-layout',
								'liferay-ddm-form-builder-field-support',
								'liferay-ddm-form-field-types'
							]
						},
						'liferay-ddm-form-builder-layout-serializer': {
							path: 'form_layout_serializer.js',
							requires: [
								'json',
								'liferay-ddm-form-builder-layout-visitor'
							]
						},
						'liferay-ddm-form-builder-layout-visitor': {
							path: 'form_layout_visitor.js',
							requires: [
								'aui-form-builder-field-list',
								'aui-layout'
							]
						},
						'liferay-ddm-form-builder-modal-support': {
							path: 'form_builder_modal_support.js',
							requires: [
								'aui-modal'
							]
						},
						'liferay-ddm-form-builder-pages-manager': {
							path: 'form_builder_pages_manager.js',
							requires: [
								'aui-form-builder-page-manager',
								'liferay-ddm-form-builder-pagination',
								'liferay-ddm-form-builder-wizard'
							]
						},
						'liferay-ddm-form-builder-pagination': {
							path: 'form_builder_pagination.js',
							requires: [
								'aui-pagination'
							]
						},
						'liferay-ddm-form-builder-render-rule': {
							path: 'form_builder_render_rule.js',
							requires: ['liferay-ddm-form-builder-action-factory', 'liferay-ddm-form-builder-rule-template', 'liferay-ddm-form-builder-rule-validator', 'liferay-ddm-form-renderer-field']
						},
						'liferay-ddm-form-builder-render-rule-condition': {
							path: 'form_builder_render_rule_condition.js',
							requires: [
								'liferay-ddm-form-renderer-field'
							]
						},
						'liferay-ddm-form-builder-rule-builder': {
							path: 'form_builder_rule_builder.js',
							requires: [
								'liferay-ddm-form-builder-render-rule',
								'liferay-ddm-form-builder-render-rule-condition',
								'liferay-ddm-form-builder-rule-builder-template'
							]
						},
						'liferay-ddm-form-builder-rule-builder-template': {
							path: '../templates/rule-builder.js'
						},
						'liferay-ddm-form-builder-rule-template': {
							path: '../templates/rule.js'
						},
						'liferay-ddm-form-builder-rule-validator': {
							path: 'form_builder_rule_validator.js',
							requires: []
						},
						'liferay-ddm-form-builder-settings-retriever': {
							path: 'form_builder_settings_retriever.js',
							requires: [
								'aui-request'
							]
						},
						'liferay-ddm-form-builder-sidebar': {
							path: 'form_builder_sidebar.js',
							requires: ['aui-tabview', 'liferay-ddm-form-builder-field-options-toolbar', 'liferay-ddm-form-builder-sidebar-template']
						},
						'liferay-ddm-form-builder-sidebar-template': {
							path: '../templates/sidebar.js'
						},
						'liferay-ddm-form-builder-util': {
							path: 'form_builder_util.js',
							requires: [
								'liferay-ddm-form-builder-field-support',
								'liferay-ddm-form-renderer-layout-visitor',
								'liferay-ddm-form-renderer-util'
							]
						},
						'liferay-ddm-form-builder-wizard': {
							path: 'form_builder_wizard.js',
							requires: [
								'liferay-ddm-form-renderer-wizard'
							]
						},
					},
					root: MODULE_PATH + '/js/'
				}
			}
		}
	);
})();