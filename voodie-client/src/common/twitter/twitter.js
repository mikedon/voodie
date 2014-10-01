angular.module('voodie').directive("voodieTw", ['$location',
    function($location){
        return {
            restrict: "E",
            replace: true,
            templateUrl: "twitter/twitter.tpl.html",
            scope: {
                url: "@"
            },
            compile: function(element, attrs){
                if(attrs.url === undefined){
                    attrs.url = $location.absUrl();
                }
            }
        };
    }
]);
