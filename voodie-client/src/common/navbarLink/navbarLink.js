angular.module('navbarLink',[])
.directive('navbarLink', ["$location", "User",
    function($location, User){
        return {
            restrict: "E",
            replace: true,
            scope: {
                label: "@",
                href: "@",
                show: "@",
                click: "&"
            },
            controller: function($scope){
                $scope.logout = function(){
                    User.logout('login');
                };
            },
            link: function(scope, element, attrs){
                //need to make this directives controller functions available to the parent scope
                //in order to call it from the html.  the other way to make this work is to create
                //a controller outside the directive.
                scope.$parent.logout = scope.logout;
                scope.currentUser = User;
                scope.$parent.$on('$routeChangeSuccess', function(){
                    var href = scope.href.substring(1);
                    scope.active = $location.path() === href;
                });
            },
            templateUrl: 'navbarLink/navbarLink.tpl.html'
        };
    }
]);
