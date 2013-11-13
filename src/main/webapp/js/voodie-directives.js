app.directive("bsinput", function(){
	return {
		restrict: "E",
		require: '^ngModel',
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

app.directive("fbshare", function(){
    return {
        restrict: "EA",
        link: function(scope, element, attrs) {
            attrs.$observe('election', function(election){
                var url = window.location.protocol + "//" +
                    window.location.hostname + ":" +
                    window.location.port +
                    "/voodie/#/election/" + election;
                var htmlText = "<div class='fb-share-button' " +
                    "data-href='" + url +"' " +
                    "data-type='box_count'></div>";
                element.html(htmlText);
            });
        }
    }
});

app.directive("tweetshare", function(){
    return {
        restrict: "EA",
        link: function(scope, element, attrs){
            var htmlText = "<a href='https://twitter.com/share' class='twitter-share-button' " +
                "data-size='large' data-hashtags='voodielicious' data-text='Checkout our election!'>Tweet</a>"+
                "<script>" +
                    "!function(d,s,id){" +
                        "var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';" +
                        "if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';" +
                        "fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');" +
                "</script>";
            element.html(htmlText);
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
