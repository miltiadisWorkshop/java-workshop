require(["map"], function(layeredMap) {
	var selectTag = $("<select></select>", {
	    "class": "layerChooser",
	    change: function() {
	    	for(var [layerKey, layerValue] of layeredMap.getLayerMap()) {
	    		// "this" variable points to selectTag
				layerValue.layer.setZIndex(layerKey === this.value ? 1 : 0);
			}
	    }
	}).appendTo($("#map"));
    // create the option elements
	for(var [layerKey, layerValue] of layeredMap.getLayerMap()) {
		$("<option></option>", {
			"value": layerKey,
			text: layerValue.name
		}).appendTo(selectTag);
	}
});