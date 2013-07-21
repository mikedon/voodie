app.directive("bsinput", function(){
	return {
		restrict: "E",
		compile: function(element, attrs){
			var type = attrs.type || 'text';
			var id = attrs.id;
			var label = attrs.label;
			var html = "<div class='control-group'><label class='control-label' for='" + id + "'>" + label + "</label><div class='controls'><input type='" + type + "' id='" + id + "' class='input-block-level' ng-model='" + id + "'></div></div>" 
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