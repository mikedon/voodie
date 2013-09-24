var app = angular.module('voodie', ['ngResource', 'ui.bootstrap']);
app.config(["$routeProvider", function($routeProvider){
    //TODO extract into JSON object and refactor logic to use that
	$routeProvider.when('/home', {
		templateUrl:'routes/home.html',
		resolve: Resolve});
	$routeProvider.when('/about', {
		templateUrl:'routes/about.html',
		resolve: Resolve});
    $routeProvider.when('/elections', {
        templateUrl:'routes/election/elections.html',
        resolve:Resolve});
    $routeProvider.when('/register', {
        templateUrl: 'routes/registration.html',
        resolve:Resolve});
	$routeProvider.when('/login', {
		templateUrl: 'routes/login.html',
		resolve: Resolve});
    $routeProvider.when('/foodie/registration', {
        templateUrl : 'routes/foodie/registration.html',
        resolve: Resolve});
    $routeProvider.when('/foodie/profile', {
        templateUrl : 'routes/foodie/profile.html',
        resolve: Resolve,
        access : {requiresLogin : true, role : "Foodie"}});
	$routeProvider.when('/foodtruck/registration', {
		templateUrl : 'routes/foodtruck/registration.html',
		resolve: Resolve});
	$routeProvider.when('/foodtruck/profile', {
		templateUrl : 'routes/foodtruck/profile.html',
		resolve: Resolve,
		access : {requiresLogin : true, role : "Food Truck"}});
    $routeProvider.when('/foodtruck/elections', {
        templateUrl : 'routes/foodtruck/elections.html',
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