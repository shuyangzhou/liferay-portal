;(function() {
	var PATH_FRONTEND_MAP_GOOGLE_MAPS_WEB = Liferay.ThemeDisplay.getPathContext() + '/o/map-google-maps';

	AUI().applyConfig(
		{
			groups: {
				mapgoogle: {
					base: PATH_FRONTEND_MAP_GOOGLE_MAPS_WEB + '/js/',
					modules: {
						'liferay-map-google-maps': {
							path: 'map.js',
							requires: [
								'liferay-map-common'
							]
						}
					},
					root: PATH_FRONTEND_MAP_GOOGLE_MAPS_WEB + '/js/'
				}
			}
		}
	);
})();