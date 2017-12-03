define(["jquery", "jquery-ui"], function($){
	var itemFactoryMap = new Map();
	var itemFactoryIndexArray = [];
	var actionMap = new Map();
	var isSorted = true;
    $("#cMenu").menu({
		select: function(event, ui) {
			var i;
			var factoryName = ui.item.attr("actionFactory");
			var func = actionMap.get(factoryName).get(ui.item.attr("funcName"));
			var paramsAsStr = ui.item.attr("params");
			var params = paramsAsStr ? JSON.parse(paramsAsStr) : null;
			$("#cMenu").hide().empty();
			var argsArray = [];
			if(params) {
				for(i in params) {
					argsArray.push(params[i]);
				}
			}
			func.apply(null, argsArray);
		}
	});
	$("#cMenu").on({
		contextmenu: function(evt) {
			return false;
		}
	});
	return {
		registerItemFactory: function(itemFactory, order) {
			isSorted = false;
			itemFactoryMap.set(order, itemFactory);
			itemFactoryIndexArray.push(order);
		},
		showMenu: function(event, context) {
			if(!isSorted) {
				itemFactoryIndexArray.sort();
				isSorted = true;
			}
			var isFirst = true;
			var cMenu = $("#cMenu");
			cMenu.empty();
			itemFactoryIndexArray.forEach(function(factoryIndex) {
				if(!isFirst) {
					$("<li><div>-</div></li>").appendTo(cMenu);
				}
				else {
					isFirst = false;
				}
				var itemFactory = itemFactoryMap.get(factoryIndex);
				itemFactory.getMenuItems(event, context).forEach(function(item) {
					item.appendTo(cMenu);
					item.attr("actionFactory", itemFactory.factoryName);
					actionMap.set(itemFactory.factoryName, itemFactory.getActionMapping());
				});
			});
            cMenu.menu("refresh");
            cMenu.show();
            cMenu.position({
                colision: "flip fit",
                my: "left top",
                of: event
            });
		},
		hideMenu() {
			$("#cMenu").hide().empty();
		}
	}
});