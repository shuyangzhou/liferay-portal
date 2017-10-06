Liferay.Loader.require(
	'frontend-js-metal-web/metal-soy-bundle/build/bundle',
	'dynamic-data-mapping-form-builder/templates/sidebar.es',
	function(MetalSoyBundle, SideBar) {
		if (!window.DDMSidebar) {
			window.DDMSidebar = {};
		}

		SideBar.default.forEach(function(item) {
			window.DDMSidebar[item.key] = item.component;
		});

		AUI.add('liferay-ddm-form-builder-sidebar-template');
	}
);