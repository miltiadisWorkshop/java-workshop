require(["jquery", "context-menu", "map"], function($, contextMenu, layeredMap) {
	var factory = {
		factoryName: "link",
		getMenuItems: function(event, context) {
			var list = [];
			var addItem = $("<li><div>Add Link</div></li>").attr({
                funcName: "addLink",
                params: JSON.stringify([ context.coordinate ])
            })
			list.push(addItem);
			return list;
		},
		getActionMapping : function() {
			var map = new Map();
			map.set("addLink", this.add);
			return map;
		},
		add: function(coordinate) {
        	var requestJson = {
                    "ip": '126.124.12.12',
                    "lon": coordinate[0],
                    "lat": coordinate[1]
                }
            	$.ajax({
                    method: "POST",
                    url: window.location.href + "rest/ne/create",
                    contentType: "application/json; charset=UTF-8",
                    data: JSON.stringify(requestJson),
                    headers: {
                        "Accept": "application/json",
                    }
                }).done(function(data) {
                    if(data.status == 0) {
                    	layeredMap.loadFeatures();
                    }
                }).fail(function(jqXHR, textStatus, errorThrown) {
                    if(jqXHR.status === 599) {
                        alert(jqXHR.responseText);
                    }
                    else {
                        alert(errorThrown);
                    }
                });
            }
		}
	contextMenu.registerItemFactory(factory, 2);
});