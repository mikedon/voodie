var app = angular.module('voodie', ['ngResource', 'ui.bootstrap']);
app.config(["$routeProvider", function($routeProvider){
	$routeProvider.when('/home', {templateUrl:'templates/home.html'});
	$routeProvider.when('/about', {templateUrl:'templates/about.html'});
	$routeProvider.when('/votes/:latitude+:longitude/:eatingTime', {templateUrl:'templates/votes.html'});
	$routeProvider.when('/foodTruck/:id', {templateUrl: 'templates/foodtruck.html'});
	$routeProvider.otherwise({redirectTo: '/home'});
}]);