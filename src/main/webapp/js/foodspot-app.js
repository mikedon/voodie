var app = angular.module('foodSpot', ['ngResource', 'ui.bootstrap']);
app.config(["$routeProvider", function($routeProvider){
	$routeProvider.when('/home', {templateUrl:'templates/home.html'});
	$routeProvider.when('/votes/:latitude+:longitude/:eatingTime', {templateUrl:'templates/votes.html'});
	$routeProvider.otherwise({redirectTo: '/home'});
}]);