angular.module('voodie').directive("bsInput", function(){
	return {
		restrict: "A",
        require: "^form",
        replace: true,
        transclude: true,
        scope: {
            label: "@", // Gets the string contents of the `label` attribute.
            requiredMsg: "@",
            validationMsg: "@"
        },
        link: function(scope, element, attrs, formController) {
            var input = element.find(":input") ? element.find(":input") : element.find(":select");
            input.addClass('form-control');
            // The <label> should have a `for` attribute that links it to the input.
            // Get the `id` attribute from the input element
            // and add it to the scope so our template can access it.
            var id = input.attr("id");
            scope.for = id;

            // Get the `name` attribute of the input
            var inputName = input.attr("name");
            // Build the scope expression that contains the validation status.
            // e.g. "form.example.$invalid"
            var errorExpression = [formController.$name, inputName, "$invalid"].join(".");
            // Watch the parent scope, because current scope is isolated.

            //should we show errors at all?
            var showErrorsExpression = [formController.$name, 'submitted'].join(".");
            scope.$parent.$watch(showErrorsExpression, function(showErrors){
                scope.showErrors = showErrors;
            });

            //does this input have errors?
            scope.$parent.$watch(errorExpression, function(isError) {
                scope.isError = isError;
            });

            //did the required validation fail?
            var requiredErrorExpression = [formController.$name, inputName, "$error", "required"].join(".");
            scope.$parent.$watch(requiredErrorExpression, function(isRequiredError) {
                scope.isRequiredError = isRequiredError;
            });

            //TODO did the other types of validations fail?
        },
        templateUrl: 'includes/bsinput.html'
	}
});

/**
 * directive that controls the ajax loading display
 */
angular.module('voodie').directive("voodLoading", ["$modal",
    function($modal){
        return {
            link: function(scope, element, attrs){
                var modalInstance;
                //this is a dummy controller because $modal needs it
                var ModalInstanceCtrl = function ($scope, $modalInstance) {
                }
                scope.$watch('loadingView', function(loadingView){
                    if(loadingView === false && modalInstance){
                        modalInstance.close();
                    }else if(loadingView === true){
                        modalInstance = $modal.open({
                            template: "<img src='css/img/ajax-loader.gif'>",
                            controller: ModalInstanceCtrl,
                            windowClass: "loading",
                            backdrop: "static",
                            keyboard: false
                        });
                    }
                })
            }
        }
    }
]);

angular.module('voodie').directive("fbshare", function(){
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

angular.module('voodie').directive("tweetshare", function(){
    return {
        restrict: "EA",
        link: function(scope, element, attrs){
            attrs.$observe('election', function(election){
                var url = window.location.protocol + "//" +
                    window.location.hostname + ":" +
                    window.location.port +
                    "/voodie/#/election/" + election;
                var htmlText = "<a href='https://twitter.com/share' class='twitter-share-button' " +
                    "data-size='large' data-hashtags='voodielicious' data-text='Checkout our election!'" +
                    " data-url='" + url + "'>Tweet</a>"+
                    "<script>" +
                    "!function(d,s,id){" +
                    "var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';" +
                    "if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';" +
                    "fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');" +
                    "</script>";
                element.html(htmlText);
            });
        }
    }
});

angular.module('voodie').directive("blur", function(){
	return function($scope, element, attrs){
            element.bind("blur", function(event) {
                $scope.$apply(attrs.blur);
            });
	}
});

angular.module('voodie').directive("focus", function(){
    return function($scope, element, attrs){
        element.bind("focus", function(event) {
            $scope.$apply(attrs.focus);
        });
    }
});
