angular.module('voodie', ['ngResource', 'ngRoute', 'ui.bootstrap']);

angular.module('voodie').config(["$routeProvider", "$tooltipProvider", function($routeProvider, $tooltipProvider){
    //global options for tool tips
    $tooltipProvider.options({trigger: 'focus'});

	$routeProvider.when('/home', {
		templateUrl:'routes/home.html',
		resolve: Resolve
    });

	$routeProvider.when('/about', {
		templateUrl:'routes/about.html',
		resolve: Resolve
    });

    $routeProvider.when('/elections', {
        templateUrl:'routes/election/elections.html',
        controller: 'ElectionsCtrl',
        resolve: {
            districts: ['Voodie', function(Voodie){
                return Voodie.getDistricts().$promise;
            }],
            user: ['User', function(User){
                return User.initialize();
            }]
        }
    });

    $routeProvider.when('/election/:e', {
        templateUrl:'routes/election/election.html',
        controller: 'ElectionCtrl',
        resolve: {
            election: ['$route', '$q', 'Voodie', function($route, $q, Voodie){
                return Voodie.getElection($route.current.params.e).$promise;
            }],
            user : ['User', function(User){
                return User.initialize();
            }]
        },
        access: {requiresLogin: true, role : "Foodie"}
    });

    $routeProvider.when('/election/checkin/:e', {
        templateUrl:'routes/election/checkin.html',
        controller: 'CheckInCtrl',
        resolve: {
            election: ['$route', '$q', 'Voodie', function($route, $q, Voodie){
                return Voodie.getElection($route.current.params.e).$promise;
            }],
            user : ['User', function(User){
                return User.initialize();
            }]
        },
        access: {requiresLogin: true, role : "Foodie"}
    });

    $routeProvider.when('/register', {
        templateUrl: 'routes/registration.html',
        resolve:Resolve
    });

	$routeProvider.when('/login', {
		templateUrl: 'routes/login.html',
		resolve: Resolve
    });

    $routeProvider.when('/foodie/registration', {
        templateUrl : 'routes/foodie/registration.html',
        controller: 'FoodieRegistrationCtrl',
        resolve: {
            districts: ['Voodie', function(Voodie){
                return Voodie.getDistricts().$promise;
            }]
        }
    });

    $routeProvider.when('/foodie/profile', {
        templateUrl : 'routes/foodie/profile.html',
        controller: 'FoodieProfileCtrl',
        resolve: {
            profile: ['Voodie', 'User', function(Voodie, User){
               return Voodie.getFoodieProfile(User.username).$promise;
            }],
            user : ['User', function(User){
                return User.initialize();
            }]
        },
        access : {requiresLogin : true, role : "Foodie"}
    });

	$routeProvider.when('/foodtruck/registration', {
		templateUrl : 'routes/foodtruck/registration.html',
        controller: 'FoodTruckRegistrationCtrl',
		resolve: {
            districts: ['Voodie', function(Voodie){
                return Voodie.getDistricts().$promise;
            }]
        }
    });

	$routeProvider.when('/foodtruck/profile', {
		templateUrl : 'routes/foodtruck/profile.html',
        controller: 'FoodTruckProfileCtrl',
        resolve: {
            profile: ['Voodie', 'User', function(Voodie, User){
                return Voodie.getFoodTruckProfile(User.username).$promise;
            }],
            user : ['User', function(User){
                return User.initialize();
            }]
        },
		access : {requiresLogin : true, role : "Food Truck"}
    });

    $routeProvider.when('/foodtruck/elections', {
        templateUrl : 'routes/foodtruck/elections.html',
        controller: 'FoodTruckElectionCtrl',
        resolve: {
            elections: ['Voodie', 'User', function(Voodie, User){
                return Voodie.getElections(User.username).$promise;
            }],
            user : ['User', function(User){
                return User.initialize();
            }]
        },
        access: {requiresLogin : true, role : "Food Truck"}
    });

    $routeProvider.when('/foodtruck/viewElection/:e', {
        templateUrl : 'routes/foodtruck/view-election.html',
        controller: 'FoodTruckViewElectionCtrl',
        resolve: {
            election: ['$route', '$q', 'Voodie', function($route, $q, Voodie){
                return Voodie.getElectionForSelection($route.current.params.e).$promise;
            }],
            user : ['User', function(User){
                return User.initialize();
            }]
        },
        access: {requiresLogin : true, role : "Food Truck"}
    });

    $routeProvider.when('/foodtruck/createElection', {
        templateUrl: 'routes/foodtruck/create-election.html',
        controller: 'FoodTruckCreateElectionCtrl',
        resolve: {
            user : ['User', function(User){
                return User.initialize();
            }]
        },
        access: {requiresLogin: true, role: "Food Truck"}
    });

    $routeProvider.when('/foodtruck/editElection/:e', {
        templateUrl : 'routes/foodtruck/edit-election.html',
        controller:'FoodTruckEditElectionCtrl',
        resolve: {
            election: ['$route', '$q', 'Voodie', function($route, $q, Voodie){
                return Voodie.getSecureElection($route.current.params.e).$promise;
            }],
            user : ['User', function(User){
                return User.initialize();
            }]
        },
        access: {
            requiresLogin : true,
            role : "Food Truck",
            validate: function(locals){
                if(locals.election.alerts.length > 0){
                    return {msg: 'Invalid election', path: '/foodtruck/elections'};
                }
            }
        }
    });

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
 * http://blog.brunoscopelliti.com/show-route-only-after-all-promises-are-resolved
 */
angular.module('voodie').run(['$rootScope', '$location', '$timeout', 'User',
    function ($root, $location, $timeout, User){
        $root.$on('$routeChangeStart', function(e, curr, prev) {
            if (curr.$$route && curr.$$route.resolve) {
                // Show a loading message until promises are not resolved
                $root.loadingView = true;
            }
        });
        $root.$on('$routeChangeSuccess', function(event, currRoute){
            $timeout(function(){
                FB.XFBML.parse();
                twttr.widgets.load();
            }, 500);
            $root.loadingView = false;
            //clear out error msgs...better way?
            if($root.clearAlerts){
                $root.alerts = [];
            }
            $root.clearAlerts = true;
            if(currRoute.access && currRoute.access.requiresLogin){
                if(!User.isLoggedIn()){
                    $root.captureRedirect = $location.path();
                    $root.clearAlerts = false;
                    console.debug("Route Requires Login");
                    $root.alerts = [{type:'warning', message:'Please login first.'}];
                    $location.path("/login");
                }else if(!User.hasRole(currRoute.access.role)){
                    console.debug("Route Requires Role: " + currRoute.access.role);
                    $location.path("/elections");
                }else if(currRoute.access.validate){
                    var validateResponse = currRoute.access.validate(currRoute.locals);
                    if(validateResponse){
                        $root.clearAlerts = false;
                        $root.alerts = [{type:'warning', message:validateResponse.msg}];
                        $location.path(validateResponse.path);
                    }
                }
            }
        });
    }
]);