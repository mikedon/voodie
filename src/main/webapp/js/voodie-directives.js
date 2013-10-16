app.directive("bsinput", function(){
	return {
		restrict: "E",
		require: '^ngModel',
		compile: function(element, attrs){
			var type = attrs.type || 'text';
			var id = attrs.id;
			var label = attrs.label;
            var inline = attrs.inline || true;
            var html = "";
            if(!inline){
            	html += "<div class='control-group'>";
            }
            if(label){
                html += "<label class='control-label' for='" + id + "'>" + label + "</label>";
            }
            if(!inline){
                html += "<div class='controls'>";
            }
            html += "<input type='" + type + "' id='" + id + "'";
            if(!inline){
                html += "class='input-block-level'";
            }
            html += "ng-model='" + id + "'>";
            if(!inline){
                html += "</div></div>";
            }
            element.replaceWith(html);
		}
	}
});

app.directive("blur", function(){
	return function($scope, element, attrs){
            element.bind("blur", function(event) {
                $scope.$apply(attrs.blur);
            });
	}
});

app.directive("focus", function(){
    return function($scope, element, attrs){
        element.bind("focus", function(event) {
            $scope.$apply(attrs.focus);
        });
    }
});
