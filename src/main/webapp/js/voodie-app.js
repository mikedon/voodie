var app = angular.module('voodie', ['ngResource', 'ui.bootstrap']);
app.config(["$routeProvider", function($routeProvider){
	$routeProvider.when('/home', {templateUrl:'templates/home.html'});
	$routeProvider.when('/about', {templateUrl:'templates/about.html'});
	$routeProvider.when('/votes/:latitude+:longitude/:eatingTime', {templateUrl:'templates/votes.html'});
	$routeProvider.when('/foodTruck/:id', {templateUrl: 'templates/foodtruck.html'});
	$routeProvider.when('/login', {templateUrl: 'templates/login.html'});
	$routeProvider.when('/register/foodTruck', {templateUrl : 'templates/foodtruck-registration.html'});
	$routeProvider.when('/profile/foodTruck', {templateUrl : 'templates/foodtruck-profile.html', access : {requiresLogin : true, role : "FoodTruck"}});
	$routeProvider.otherwise({redirectTo: '/home'});
}]);

/**
 * http://blog.brunoscopelliti.com/deal-with-users-authentication-in-an-angularjs-web-app
 * 
 * see comments on better ways to do this not using directive
 */
app.run(['$rootScope', '$location', 'User', function ($root, $location, User){
	$root.$on('$routeChangeSuccess', function(event, currRoute){
		if(currRoute.access && currRoute.access.requiresLogin){
			if(!User.isLoggedIn){
				console.log("Route Requires Login")
				$location.path("/login");
			}else if(!User.hasRole(currRoute.access.role)){
				console.log("Route Requires Role: " + currRoute.access.role);
			}
		}
		/*
		* IMPORTANT:
		* It's not difficult to fool the previous control,
		* so it's really IMPORTANT to repeat the control also in the backend,
		* before sending back from the server reserved information.
		*/
	});
}]);