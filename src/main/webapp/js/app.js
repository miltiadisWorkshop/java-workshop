requirejs.config({
	paths: {
		"map": "core/map",
		"select-map": "core/select-map",
		"context-menu": "core/context-menu",
		"misc-menu-factory": "core/misc-menu-factory",
		"element-menu-factory": "core/element-menu-factory"
	},
	shim: {
		"jquery-ui": ["jquery"],
		"context-menu": ["jquery-ui"],
		"map": ["ol", "context-menu"],
		"select-map": ["map"],
		"misc-menu-factory": ["context-menu"],
		"element-menu-factory": ["context-menu"]
	}
});
require(["ol", "jquery", "map", "select-map", "misc-menu-factory", "element-menu-factory", "jquery-ui"], function(
		ol, $, layerMap) {
	layerMap.loadFeatures();
});