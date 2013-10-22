app.factory('GoogleMaps', function(){
	return {
		googleMaps : new google.maps.Geocoder(),
		geolocate : function(address, onSuccess){
			this.googleMaps.geocode({"address": address}, function(results, status){
				if(status == google.maps.GeocoderStatus.OK){
					onSuccess(results[0].geometry.location.lng(), results[0].geometry.location.lat());
				}
			});
		}
	}
});

app.factory('User', function($resource, $http, $location, $rootScope, $q){
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
			 */
			var d = $q.defer();
			var that = this;
			if(!this.initialized){
				var user = $resource('api/user/secure/currentUser');
				user.get({}, function(value, responseHeaders){
					if(value.username){
						that.username = value.username;
                        that.firstName = value.firstName;
                        that.lastName = value.lastName;
						that.roles = value.roles;
						that.initialized = true;
						that.loggedIn = true;
					}
					d.resolve(that)
				});
			}else{
				d.resolve(that);
			}
			return d.promise;
		},
		login : function(redirect){
			var that = this;
            var payload = 'j_username=' + this.username + '&j_password=' + this.password;
            var config = {
                headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}
            }
	        $http.post('j_spring_security_check', payload,config)
	        .success(function(data) {
	        	if(data.hasErrors){
                    that.reset();
	        		$rootScope.error = data.errorMsgs;
	        		return;
	        	}else{
	        		that.password = '';
	        		that.loggedIn = true;
	        		$location.path(redirect);
	        		return;
	        	}
            })
        },
        logout : function(redirect){
            this.reset();
        	$http.get('j_spring_security_logout').success(function(data){
        		$location.path(redirect);
        	});
        }
	}
});

app.factory('Voodie', function($resource, $location, $rootScope, $filter){
	return {
        //TODO abstract all voodie services so api path is set in one place
		registerTruck: function(truck, redirect){
			var FoodTruckRegistration = $resource('api/foodtruck/register');
			var newRegistration = new FoodTruckRegistration();
            newRegistration.firstName = truck.firstName;
            newRegistration.lastName = truck.lastName;
            newRegistration.emailAddress = truck.email;
			newRegistration.username = truck.username;
			newRegistration.password = truck.password;
			newRegistration.name = truck.foodTruckName;
            newRegistration.district = truck.district;
			newRegistration.$save(function(){
                $location.path(redirect);
            });
		},
        registerFoodie: function(truck, redirect){
            var FoodieRegistration = $resource('api/foodie/register');
            var newRegistration = new FoodieRegistration();
            newRegistration.firstName = truck.firstName;
            newRegistration.lastName = truck.lastName;
            newRegistration.emailAddress = truck.email;
            newRegistration.username = truck.username;
            newRegistration.password = truck.password;
            newRegistration.$save(function(){
                $location.path(redirect);
            });
        },
        getFoodTruckProfile: function(username){
            var foodTruck = $resource('api/foodtruck/secure/profile', {"username" : username}).get();
            return foodTruck;
        },
        getFoodieProfile: function(username){
            var foodie = $resource('api/foodie/secure/profile', {"username" : username}).get();
            return foodie;
        },
        createElection: function(election, onSuccess){
            var Election = $resource('api/election/secure/createElection');
            var newElection = new Election();
            newElection.title = election.title;
            newElection.servingStartTime = election.servingStartTime;
            newElection.servingEndTime = election.servingEndTime;
            newElection.pollOpeningDate = election.pollOpeningDate;
            newElection.pollClosingDate = election.pollClosingDate;
            newElection.allowWriteIn = election.allowWriteIn;
            newElection.candidates = election.candidates;
            //TODO add success message
            newElection.$save(onSuccess);
        },
        getElections: function(username){
            var elections = $resource('api/election/secure/getAllElections', {"username":username}).query();
            return elections;
        },
        getAllElections: function(district, startDate, endDate, onSuccess){
            var startDate = $filter('date')(startDate);
            var endDate = $filter('date')(endDate);
            var elections = $resource('api/election/query', {"district": district, "startDate": startDate, "endDate": endDate}).query(onSuccess);
            return elections;
        },
        getElection: function(election, onSuccess){
            var election = $resource('api/election/secure/getElection', {"election":election}).get(onSuccess);
            return election;
        },
        vote: function(candidate, onSuccess){
            var Vote = $resource('api/election/secure/vote');
            var newVote = new Vote();
            newVote.candidate = candidate;
            newVote.$save(function(data){
                if(data.hasErrors){
                    $rootScope.error = data.errorMsgs;
                    return;
                }else{
                    onSuccess(data);
                }
            });
        },
        getElectionForSelection: function(election){
            var election = $resource('api/election/secure/getElectionForSelection', {"election":election}).get();
            return election;
        },
        selectCandidate: function(candidate, onSuccess){
            var Candidate = $resource('api/election/secure/selectCandidate');
            var newCandidate = new Candidate();
            newCandidate.id = candidate;
            newCandidate.$save(function(data){
                if(data.hasErrors){
                    $rootScope.error = data.errorMsgs;
                    return;
                }else{
                    onSuccess(data);
                }
            });
        },
        checkIn: function(election, onSuccess){
            var CheckIn = $resource('api/election/secure/checkIn');
            var newCheckIn = new CheckIn();
            newCheckIn.election = election;
            newCheckIn.$save(function(data){
                if(data.hasErrors){
                    $rootScope.error = data.errorMsgs;
                    return;
                }else{
                    onSuccess(data);
                }
            })
        },
        getDistricts: function(){
            var districts = $resource('api/election/districts').query();
            return districts;
        }
	}
});

app.factory('EatingTime', function(){
    return {
        roundedTime: function(){
            var roundedTime = new Date();
            roundedTime.setMilliseconds(0);
            roundedTime.setSeconds(0);
            if(roundedTime.getMinutes() > 30){
                roundedTime.setMinutes(0);
                roundedTime.setHours(roundedTime.getHours() + 1);
            }else{
                roundedTime.setMinutes(30);
            }
            return roundedTime;
        },
        timeStamp: function(date, time){
            var _date,_time;
            if(!date){
                _date = this.date;
            }else{
                _date = date;
            }
            if(!time){
                _time = this.roundedTime();
            }else{
                _time = time;
            }
            return new Date(_date.getFullYear(), 
                _date.getMonth(), _date.getDate(), 
                _time.getHours(), _time.getMinutes(), 0, 0);
        },
        getEatingTime: function(){
            if(!this._eatingTime){
                this._eatingTime = this.roundedTime();
            }
            return this._eatingTime;
        },
        setEatingTime : function(eatingTime){
            this._eatingTime = eatingTime;
        }
    }
});