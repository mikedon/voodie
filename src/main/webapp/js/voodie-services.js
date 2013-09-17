app.factory('GoogleMaps', function(){
	return {
		googleMaps : new google.maps.Geocoder(),
		geolocate : function(address, onSuccess){
			this.googleMaps.geocode({"address": address}, function(results, status){
				if(status == google.maps.GeocoderStatus.OK){
					onSuccess(results[0].geometry.location.kb, results[0].geometry.location.jb);
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
				var user = $resource('rest/user/secure/currentUser');
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

app.factory('Voodie', function($resource, $location){
	return {
		getEntries : function(page, latitude, longitude, onSuccess){
			var foodTrucks = $resource('rest/foodTruck/entries', {"page":page,"latitude":latitude,"longitude":longitude}).get(onSuccess);
            return foodTrucks;
		},
		getFoodTruckInfo : function(foodTruckId, onSuccess){
			var foodTruck = $resource('rest/foodTruck/info', {"foodTruckId" : foodTruckId}).get(onSuccess);
			return foodTruck;
		},
		vote : function(foodTruckId, latitude, longitude, eatingTime){
			var Vote = $resource('rest/foodTruck/vote');
			var newVote = new Vote();
			newVote.foodTruckId = foodTruckId;
			newVote.latitude = latitude;
			newVote.longitude = longitude;
			newVote.eatingTime = eatingTime;
			newVote.$save();
		},
		getVotes : function(foodTruckId, eatingTime, onSuccess){
			var votes = $resource('rest/foodTruck/vote/entries', {"foodTruckId":foodTruckId, "eatingTime" : eatingTime}).get(onSuccess);
			return votes;
		},
		registerTruck: function(truck, redirect){
			var FoodTruckRegistration = $resource('rest/foodTruck/register');
			var newRegistration = new FoodTruckRegistration();
            newRegistration.firstName = truck.firstName;
            newRegistration.lastName = truck.lastName;
			newRegistration.username = truck.username;
			newRegistration.password = truck.password;
			newRegistration.name = truck.foodTruckName;
			newRegistration.$save(function(){
                $location.path(redirect);
            });
		},
        getFoodTruckProfile: function(username){
            var foodTruck = $resource('rest/foodTruck/secure/profile', {"username" : username}).get();
            return foodTruck;
        },
        createElection: function(election, onSuccess){
            var Election = $resource('rest/foodTruck/secure/createElection');
            var newElection = new Election();
            newElection.title = election.title;
            newElection.servingStartTime = election.servingStartTime;
            newElection.servingEndTime = election.servingEndTime;
            newElection.pollOpeningDate = election.pollOpeningDate;
            newElection.pollClosingDate = election.pollClosingDate;
            newElection.allWriteIn = election.allowWriteIn;
            newElection.candidates = election.candidates;
            //TODO add success message
            newElection.$save(onSuccess);
        },
        getElections: function(username){
            var elections = $resource('rest/foodTruck/secure/getAllElections', {"username":username}).query();
            return elections;
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