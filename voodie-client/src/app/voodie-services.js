angular.module('voodie').factory('GoogleMaps', function(){
	return {
		googleMaps : new google.maps.Geocoder(),
		geolocate : function(address, onSuccess, onFailure){
			this.googleMaps.geocode({"address": address}, function(results, status){
				if(status == google.maps.GeocoderStatus.OK){
					onSuccess(results[0].geometry.location.lng(), results[0].geometry.location.lat());
				}else{
                    onFailure();
                }
			});
		}
	};
});

angular.module('voodie').factory('User', function($resource, VoodieResource, $http, $location, $rootScope, $q, apiUrl){
	return {
		initialized : false,
		loggedIn : false,
		username: '',
        firstName: '',
        lastName: '',
		password: '',
		roles: [],
        reset : function(){
            this.initialized = false;
            this.loggedIn = false;
            this.username = '';
            this.firstName = '';
            this.lastName = '';
            this.password = '';
            this.roles = [];
        },
		isInitialized : function(){
			return this.initialized;
		},
		isLoggedIn : function(){
			return this.loggedIn;
		},
		hasRole: function(role){
			return this.roles.indexOf(role) > -1;
		},
		initialize : function(){
			/**
			 * intialize the User object with currently logged in user.
             *
             * we call this more than we should
			 */
			var d = $q.defer();
			var that = this;
            var user = $resource(apiUrl + 'api/user/secure/currentUser');
            user.get({}, function(value, responseHeaders){
                if(value.username){
                    that.username = value.username;
                    that.firstName = value.firstName;
                    that.lastName = value.lastName;
                    that.district = value.district;
                    that.roles = value.roles;
                    that.initialized = true;
                    that.loggedIn = true;
                }else{
                    that.loggedIn = false;
                }
                d.resolve(that);
            });
			return d.promise;
		},
		login : function(redirect){
            VoodieResource.clearAlerts();
			var that = this;
            var payload = 'j_username=' + this.username + '&j_password=' + this.password;
            var config = {
                headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}
            };
            VoodieResource.post(apiUrl + 'j_spring_security_check', payload, config,
                function(){
                    that.password = '';
                    that.loggedIn = true;
                    $location.path(redirect);
                },
                function(){
                    that.reset();
                }
            );
        },
        logout : function(redirect){
            this.reset();
            $http.get(apiUrl + 'j_spring_security_logout').success(function(data){
				$location.path(redirect);
			});
        }
	};
});


angular.module('voodie').factory('VoodieResource', function($resource, $http, $rootScope){
    var addAlerts = function(alerts){
        if(!alerts || alerts.length === 0){
            return;
        }
        if(!$rootScope.alerts){
            $rootScope.alerts = [];
        }
        $rootScope.alerts = $rootScope.alerts.concat(alerts);
    };
    var hasErrors = function(){
        if($rootScope.alerts){
            for(var i=0; i < $rootScope.alerts.length; i++){
                if($rootScope.alerts[i].type === 'danger'){
                    return true;
                }
            }
        }
        return false;
    };
    var handleResponse = function(data, onSuccess, onFailure){
        afterCall();
        addAlerts(data.alerts);
        if(hasErrors()){
            if(onFailure){
                onFailure();
            }
        }else{
            if(onSuccess){
                onSuccess(data);
            }
        }
    };
    var beforeCall = function(){
        $rootScope.loadingView = true;
    };
    var afterCall = function(){
        $rootScope.loadingView = false;
    };
    return {
        save: function(resource, onSuccess, onFailure){
            beforeCall();
            return resource.$save(function(data){
                handleResponse(data, onSuccess, onFailure);
            });
        },
        get: function(resource, onSuccess, onFailure){
            beforeCall();
            return resource.get(function(data){
                handleResponse(data, onSuccess, onFailure);
            });
        },
        query: function(resource, onSuccess, onFailure){
            beforeCall();
            return resource.query(function(data){
                handleResponse(data, onSuccess, onFailure);
            });
        },
        post: function(url, payload, config, onSuccess, onFailure){
            beforeCall();
            return  $http.post(url, payload, config)
                .success(function(data) {
                    handleResponse(data, onSuccess, onFailure);
                });
        },
        clearAlerts: function(){
            $rootScope.alerts = [];
        }
    };
});

angular.module('voodie').factory('Voodie', function($resource, VoodieResource, $location, $rootScope, $filter, apiUrl){
	return {
        //TODO abstract all voodie services so api path is set in one place
		registerTruck: function(truck, redirect){
            VoodieResource.clearAlerts();
			var FoodTruckRegistration = $resource(apiUrl + 'api/foodtruck/register');
			var newRegistration = new FoodTruckRegistration();
            newRegistration.firstName = truck.firstName;
            newRegistration.lastName = truck.lastName;
            newRegistration.emailAddress = truck.email;
			newRegistration.username = truck.username;
			newRegistration.password = truck.password;
			newRegistration.name = truck.foodTruckName;
            newRegistration.district = truck.district.name;
            VoodieResource.save(newRegistration, function(){
                $rootScope.clearAlerts = false;
                $location.path(redirect);
            });
		},
        registerFoodie: function(foodie, redirect){
            VoodieResource.clearAlerts();
            var FoodieRegistration = $resource(apiUrl + 'api/foodie/register');
            var newRegistration = new FoodieRegistration();
            newRegistration.firstName = foodie.firstName;
            newRegistration.lastName = foodie.lastName;
            newRegistration.emailAddress = foodie.email;
            newRegistration.homeDistrict = foodie.homeDistrict.name;
            newRegistration.username = foodie.username;
            newRegistration.password = foodie.password;
            VoodieResource.save(newRegistration, function(){
                $rootScope.clearAlerts = false;
                $location.path(redirect);
            });
        },
        getFoodTruckProfile: function(username){
            return VoodieResource.get($resource(apiUrl + 'api/foodtruck/secure/profile', {"username" : username}));
        },
        getFoodieProfile: function(username){
            return VoodieResource.get($resource(apiUrl + 'api/foodie/secure/profile', {"username" : username}));
        },
        createElection: function(election, onSuccess){
            var Election = $resource(apiUrl + 'api/election/secure/createElection');
            var newElection = new Election();
            newElection.title = election.title;
            newElection.servingStartTime = election.servingStartTime;
            newElection.servingEndTime = election.servingEndTime;
            newElection.pollOpeningDate = election.pollOpeningDate;
            newElection.pollClosingDate = election.pollClosingDate;
            newElection.allowWriteIn = election.allowWriteIn;
            VoodieResource.save(newElection, onSuccess);
        },
        addCandidate: function(election, candidate, onSuccess){
            var Candidate = $resource(apiUrl + 'api/election/secure/addCandidate');
            var newCandidate = new Candidate();
            newCandidate.displayName = candidate.displayName;
            newCandidate.latitude = candidate.latitude;
            newCandidate.longitude = candidate.longitude;
            newCandidate.electionId = election;
            VoodieResource.save(newCandidate, onSuccess);
        },
        getElections: function(username, onSuccess){
            return VoodieResource.query($resource(apiUrl + 'api/election/secure/getAllElections', {"username":username}), onSuccess);

        },
        getAllElections: function(district, startDate, endDate, onSuccess){
            var newStartDate = $filter('date')(startDate);
            var newEndDate = $filter('date')(endDate);
            return VoodieResource.query($resource(apiUrl + 'api/election/query', {"district": district, "startDate": newStartDate, "endDate": newEndDate}),onSuccess);
        },
        getSecureElection: function(election, onSuccess){
            return VoodieResource.get($resource(apiUrl + 'api/election/secure/getElection', {"election":election}), onSuccess);
        },
        getElection: function(election, onSuccess){
            return VoodieResource.get($resource(apiUrl + 'api/election/getElection', {"election":election}), onSuccess);
        },
        vote: function(candidate, onSuccess){
            VoodieResource.clearAlerts();
            var Vote = $resource(apiUrl + 'api/election/secure/vote');
            var newVote = new Vote();
            newVote.candidate = candidate;
            VoodieResource.save(newVote, onSuccess);
        },
        getElectionForSelection: function(election, onSuccess){
            return VoodieResource.get($resource(apiUrl + 'api/election/secure/getElectionForSelection', {"election":election}), onSuccess);
        },
        selectCandidate: function(candidate, onSuccess){
            VoodieResource.clearAlerts();
            var Candidate = $resource(apiUrl + 'api/election/secure/selectCandidate');
            var newCandidate = new Candidate();
            newCandidate.id = candidate;
            VoodieResource.save(newCandidate, onSuccess);
        },
        checkIn: function(election, onSuccess){
            VoodieResource.clearAlerts();
            var CheckIn = $resource(apiUrl + 'api/election/secure/checkIn');
            var newCheckIn = new CheckIn();
            newCheckIn.election = election;
            VoodieResource.save(newCheckIn, onSuccess);
        },
        getDistricts: function(onSuccess){
            return VoodieResource.query($resource(apiUrl + 'api/election/districts'), onSuccess);
        }
	};
});
