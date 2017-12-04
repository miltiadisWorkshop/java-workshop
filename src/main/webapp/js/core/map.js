define(["ol", "jquery", "context-menu"], function(ol, $, contextMenu) {
	// removes the background image
	$("#map").empty();
	// The definition for requirejs
	var mapDefinition = {
		getLayerMap: function() {
			return layerMap;
		},
		loadFeatures: function() {
			/*
	        $.ajax({
	            method: "GET",
	         	url: window.location.href + "rest/ne/get",
	         	headers: {
	         		   "Accept": "application/json"
	         	}
	        }).done(function(data) {
		            // data is a json object
		     	var i;
		     	var element;
		     	var elementFeature;
		     	vectorSource.clear(true);
		     	var fArray = [];
		     	for(i in data.elements) {
		     		element = data.elements[i];
		     		elementFeature = new ol.Feature({
		     		    geometry: new ol.geom.Point(ol.proj.transform(
		     		    		[element.lon, element.lat], "EPSG:4326",     
		     					"EPSG:3857")
		     			),
		     			name: element.ip,
		                type: "ne"
		     		});
		     		elementFeature.setStyle(
		     			        element.ports ? fullNeStyle : emptyNeStyle);
		     		fArray.push(elementFeature);
		     	}
		     	vectorSource.addFeatures(fArray);
		    }).fail(function(jqXHR, textStatus, errorThrown) {
		     	
		    });
		    */
		}
	}
	// Get OpenStreetMap Tiles
    var openStreeMapLayer = new ol.layer.Tile({
        source: new ol.source.OSM(),
		zIndex: 0
    });
    // Get Google Map Tiles
    var googleMapLayer = new ol.layer.Tile({
        source: new ol.source.TileImage({
            url: "http://khm{0-3}.googleapis.com/kh?v=742&hl=pl&&x={x}&y={y}&z={z}"
		}),
		zIndex: 1
	});
    // Create the layer for our ol.Feature objects
    var vectorSource = new ol.source.Vector({});
    var vectorLayer = new ol.layer.Vector({
        source: vectorSource,
        zIndex: 2
    });
    // Put all layers in an array
	var layers = [];
	layers.push(openStreeMapLayer);
    layers.push(googleMapLayer);
    layers.push(vectorLayer);
    // Create our ol.Map object
    var map = new ol.Map({
        layers: layers,
        target: "map",
        controls: ol.control.defaults({
            attributionOptions: /** @type {olx.control.AttributionOptions} */ ({
                collapsible: false
            })
        }),
        view: new ol.View({
            center: ol.proj.fromLonLat([23.781975, 37.978253]),
            zoom: 14
        })
    });
    map.getViewport().addEventListener("contextmenu", function (event) {
        event.preventDefault();
        var point = map.getEventPixel(event);
        var coord3857 = map.getEventCoordinate(event);
        var coord4326 = ol.proj.transform(coord3857, "EPSG:3857", "EPSG:4326");
        var context = {
        	point: map.getEventPixel(event),
            coordinate: coord4326
        }
        contextMenu.showMenu(event, context);
    });
    map.getViewport().addEventListener("click", function (evt) {
        contextMenu.hideMenu();
    });
    // now construct the select tag that keeps the all the map tiles
	var GOOGLE_KEY = "GoogleMapLayer",
    	OPEN_STREET_LAYER = "OpenStreetLayer";
	var layerMap = new Map();
	layerMap.set(GOOGLE_KEY, { layer: googleMapLayer, name: "Google Satelite" });
	layerMap.set(OPEN_STREET_LAYER, { layer: openStreeMapLayer, name: "Open Street Map" });

	// Construct the styles
    var emptyNeStyle = new ol.style.Style({
        image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
            anchor: [0.5, 0.5],
            src: window.location.href + "icons/emptyNe.png"
        }))
    });
    var fullNeStyle = new ol.style.Style({
        image: new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
            anchor: [0.5, 0.5],
            src: window.location.href + "icons/fullNe.png"
        }))
    });
	return mapDefinition;
});