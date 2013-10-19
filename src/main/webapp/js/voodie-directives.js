app.directive("bsinput", function(){
	return {
		restrict: "E",
		compile: function(element, attrs){
			var type = attrs.type || 'text';
			var id = attrs.id;
			var label = attrs.label;
            var html = "";
            html += "<div class='form-group'>";
            if(label){
                html += "<label class='control-label' for='" + id + "'>" + label + "</label>";
            }
            html += "<input type='" + type + "' id='" + id + "'";
            html += "class='form-control'";
            html += "ng-model='" + id + "'>";
            html += "</div></div>";
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