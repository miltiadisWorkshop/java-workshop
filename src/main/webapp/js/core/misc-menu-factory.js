require(["context-menu"], function(contextMenu) {
    // Definition of our factory
	var factory = {
		factoryName: "misc",
		getMenuItems: function(event, context) {
			var list = [];
			var helpItem = $("<li><div>Help</div></li>").attr({
                funcName: "help"
            });
			list.push(helpItem);
			return list;
		},
		getActionMapping : function() {
			var map = new Map();
			map.set("help", this.help);
			return map;
		},
		help: function() {
	        createAskNameDialog();
	        //$("#askNameDialog input").each(function(index) {
	        //	$(this)[0].value = ""
	        //});
	        dialog.dialog("open");
		}
	}
	/* WORKSHOP NOTE: uncomment the following line to enable the Help command. */
	contextMenu.registerItemFactory(factory, 3);
	/* */
	// Now define our actions
	var dialog;
    function createAskNameDialog() {
        if(dialog) {
            return;
        }
        var dialogEl = $("<div></div>").attr({
            id: "askNameDialog",
            title: "Παρακαλώ εισάγετε το ονοματεπώνυμο σας"
        }).appendTo("body");
        var formEl = $("<form></form>").appendTo(dialogEl);
        var fieldsetEl = $("<fieldset></fieldset>").appendTo(formEl);
        var nameInput = $("<input>").attr({
            name: "name",
            id: "userNameInput",
            class: "text ui-widget-content ui-corner-all"
        }).appendTo(fieldsetEl);
        $("<input>").attr({
            type: "submit",
            tabindex: "-1",
            style: "position:absolute; top:-1000px"
        }).appendTo(fieldsetEl);
        dialog = $( "#askNameDialog" ).dialog({
            autoOpen: false,
            height: 220,
            width: 350,
            modal: true,
            buttons: {
                OK: function() {
                    requestHelp();
                },
                Cancel: function() {
                    dialog.dialog( "close" );
                }
            },
            close: function() {
            	form[0].reset();
            }
        });
        var form = dialog.find( "form" ).on( "submit", function( event ) {
            event.preventDefault();
            requestHelp();
        });

        // This function executes the Ajax HELP call
        function requestHelp(name) {
            var name = dialog.find( "form #userNameInput")[0].value;
            dialog.dialog("close");
            $.ajax({
                method: "PUT",
                url: window.location.href + "help",
                contentType: "text/plain; charset=UTF-8",
                data: name,
                headers: {
                	/* Third Exercise 
                	 * Comment out value "text/plain for header "Accept"
                	 * and uncomment "application/json"
                	 */
                	"Accept": "text/plain" /* "application/json" */
                }
            }).done(function(data) {
                alert(data);
            }).fail(function(jqXHR, textStatus, errorThrown) {
            	/* 2nd Exercise
            	 * Write your code here to show the application error.
            	 */
            	alert(errorThrown);
            });
        }
    }
});