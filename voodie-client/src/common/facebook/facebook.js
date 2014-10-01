/*
 * only displays is url is valid on facebook side
 */
angular.module('voodie').directive("voodieFb", ['$location',
    function($location){
        return {
            restrict: "E",
            replace: true,
            templateUrl: "facebook/facebook.tpl.html",
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
