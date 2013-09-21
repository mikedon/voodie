var app = angular.module('voodie', ['ngResource', 'ui.bootstrap']);
app.config(["$routeProvider", function($routeProvider){
    //TODO extract into JSON object and refactor logic to use that
	$routeProvider.when('/home', {
		templateUrl:'templates/home.html', 
		resolve: Resolve});
	$routeProvider.when('/about', {
		templateUrl:'templates/about.html', 
		resolve: Resolve});
    $routeProvider.when('/elections', {
        templateUrl:'templates/elections.html',
        resolve:Resolve});
    $routeProvider.when('/register', {
        templateUrl: 'templates/registration.html',
        resolve:Resolve});
	$routeProvider.when('/votes/:latitude+:longitude/:eatingTime', {
		templateUrl:'templates/votes.html', 
		resolve: Resolve});
//	$routeProvider.when('/foodTruck/:id', {
//		templateUrl: 'templates/foodtruck.html',
//		resolve: Resolve});
	$routeProvider.when('/login', {
		templateUrl: 'templates/login.html', 
		resolve: Resolve});
    $routeProvider.when('/foodie/registration', {
        templateUrl : 'templates/foodie-registration.html',
        resolve: Resolve});
    $routeProvider.when('/foodie/profile', {
        templateUrl : 'templates/foodie-profile.html',
        resolve: Resolve,
        access : {requiresLogin : true, role : "Foodie"}});
	$routeProvider.when('/foodTruck/registration', {
		templateUrl : 'templates/foodtruck-registration.html', 
		resolve: Resolve});
	$routeProvider.when('/foodTruck/profile', {
		templateUrl : 'templates/foodtruck-profile.html', 
		resolve: Resolve,
		access : {requiresLogin : true, role : "Food Truck"}});
    $routeProvider.when('/foodTruck/elections', {
        templateUrl : 'templates/foodtruck-elections.html',
        resolve: Resolve,
        access: {requiresLogin : true, role : "Food Truck"}});
	$routeProvider.otherwise({redirectTo: '/elections'});
}]);

/**
 * All routes must resolve this.  Loads user service.
 */
var Resolve = {
	user : function(User){
		return User.initialize();
	}	
};

/**
 * http://blog.brunoscopelliti.com/deal-with-users-authentication-in-an-angularjs-web-app
 * 
 * see comments on better ways to do this not using directive
 */
app.run(['$rootScope', '$location', 'User', function ($root, $location, User){
	$root.$on('$routeChangeSuccess', function(event, currRoute){
		if(currRoute.access && currRoute.access.requiresLogin){
			if(!User.isLoggedIn()){
				console.log("Route Requires Login")
				$location.path("/login");
			}else if(!User.hasRole(currRoute.access.role)){
				console.log("Route Requires Role: " + currRoute.access.role);
				$location.path("/home");
			}
		}
	});
}]);