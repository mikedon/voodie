angular.module('voodie', [
	'templates-app',
	'templates-common',
	'ngResource', 
	'ngRoute', 
	'ui.bootstrap',
	'voodie.election',
	'voodie.home',
	'navbar',
	'navbarLink',
	'input'
]);

angular.module('voodie').config(["$routeProvider", "$tooltipProvider", function($routeProvider, $tooltipProvider){
    //global options for tool tips
    $tooltipProvider.options({trigger: 'focus'});

	$routeProvider.when('/home', {
		templateUrl:'home/home.tpl.html',
		resolve: Resolve
    });

	$routeProvider.when('/about', {
		templateUrl:'about/about.tpl.html',
		resolve: Resolve
    });

    $routeProvider.when('/elections', {
        templateUrl:'election/search/search.tpl.html',
        controller: 'ElectionsCtrl',
        resolve: {
            districts: ['Voodie', function(Voodie){
                return {};//Voodie.getDistricts().$promise;
            }],
            user: ['User', function(User){
                return {};//User.initialize();
            }]
        }
    });

    $routeProvider.when('/election/:e', {
        templateUrl:'election/election.tpl.html',
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
        templateUrl:'election/checkin/checkin.tpl.html',
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
        templateUrl: 'registration/registration.tpl.html',
        resolve:Resolve
    });

	$routeProvider.when('/login', {
		templateUrl: 'login/login.tpl.html',
		resolve: Resolve
    });

    $routeProvider.when('/foodie/registration', {
        templateUrl : 'foodie/registration/registration.tpl.html',
        controller: 'FoodieRegistrationCtrl',
        resolve: {
            districts: ['Voodie', function(Voodie){
                return Voodie.getDistricts().$promise;
            }]
        }
    });

    $routeProvider.when('/foodie/profile', {
        templateUrl : 'foodie/profile/profile.tpl.html',
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
		templateUrl : 'foodtruck/registration/registration.tpl.html',
        controller: 'FoodTruckRegistrationCtrl',
		resolve: {
            districts: ['Voodie', function(Voodie){
                return Voodie.getDistricts().$promise;
            }]
        }
    });

	$routeProvider.when('/foodtruck/profile', {
		templateUrl : 'foodtruck/profile/profile.tpl.html',
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
        templateUrl : 'foodtruck/elections/elections.tpl.html',
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
        templateUrl : 'foodtruck/view-election/view-election.tpl.html',
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
        templateUrl: 'foodtruck/create-election/create-election.tpl.html',
        controller: 'FoodTruckCreateElectionCtrl',
        resolve: {
            user : ['User', function(User){
                return User.initialize();
            }]
        },
        access: {requiresLogin: true, role: "Food Truck"}
    });

    $routeProvider.when('/foodtruck/editElection/:e', {
        templateUrl : 'foodtruck/edit-election/edit-election.tpl.html',
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
	/*user : function(User){
		return User.initialize();
	}*/	
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
				/*if(FB && twttr){
					FB.XFBML.parse();
					twttr.widgets.load();
				}*/
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
