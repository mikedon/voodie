var NavbarCtrl = function($scope, $location, User) {
	$scope.home = function() {
		return $location.path().indexOf('home') > 0;
	};
	$scope.about = function() {
		return $location.path().indexOf('about') > 0;
	};
	$scope.login = function() {
		return $location.path().indexOf('login') > 0;
	};
	$scope.register = function() {
		return $location.path().indexOf('register') > 0;
	};
    $scope.profile = function() {
        return $location.path().indexOf('profile') > 0;
    };
    $scope.elections = function() {
        return $location.path().indexOf('foodtruck/elections') > 0;
    }
	$scope.currentUser = User;
	$scope.logout = function(){
		User.logout('login');
	}
};

var HomeCtrl = function ($scope, $location, $dialog, EatingTime, GoogleMaps) {
    $scope.eatingTimestamp = EatingTime.getEatingTime();
    $scope.submit = function(){
    	//TODO check if valid
    	if($scope.address){
    		GoogleMaps.geolocate($scope.address,function(longitude, latitude){
				$scope.$apply(function(){
					$scope.latitude = latitude;
					$scope.longitude = longitude;
					$location.path('/votes/' + $scope.latitude + '+' + $scope.longitude + '/' + $scope.eatingTimestamp.getTime());
				});
			});
    	}
    };
    $scope.opts = {
        backdrop: true,
        keyboard: true,
        backdropClick: true,
        backdropFade: true,
        dialogFade: true,
        templateUrl: 'includes/date-time-picker.html',
        controller: 'DateTimeCtrl'
    };
    $scope.openDialog = function(){
        var d = $dialog.dialog($scope.opts);
        d.open().then(function(){
            $scope.eatingTimestamp = EatingTime.getEatingTime();
        });
    };
};

//This controller syntax is slightly different to bind the controller with the view
var DateTimeCtrl = function($scope, EatingTime, dialog){
    this.eatingDate = EatingTime.roundedTime();
    this.eatingTime = EatingTime.roundedTime();
    this.save = function(){
        EatingTime.setEatingTime(EatingTime.timeStamp(this.eatingDate, this.eatingTime));
        dialog.close();
    };
    this.close = function(){
        dialog.close();
    };
    return $scope.DateTimeCtrl = this;
};

var ElectionsCtrl = function($scope, $location, Voodie){
    //district filter
    $scope.district = "";
    $scope.districts = Voodie.getDistricts();
    $scope.goToElection = function(election){
        //TODO polite check to see if they are allowed to vote
        $location.path('election/' + election.id);
    }
    $scope.$watch('district + startDate + endDate', function(){
        if(!$scope.renderNoFiltersText()){
            $scope.elections = Voodie.getAllElections($scope.district, $scope.startDate, $scope.endDate);
        }
    });
    // date filters
    $scope.startOpened = false;
    $scope.endOpened = false;
    $scope.startDate = "";
    $scope.endDate = "";
    $scope.openStart = function() {
        $scope.startOpened = true;
    };
    $scope.openEnd = function() {
        $scope.endOpened = true;
    };
    // render rules
    $scope.renderNoElectionsText = function(){
        var isRendered = $scope.elections && $scope.elections.length == 0;
        isRendered &= !$scope.renderNoFiltersText();
        return Boolean(isRendered);
    };
    $scope.renderNoFiltersText = function(){
        var isRendered = !$scope.startDate || !$scope.endDate || !$scope.district;
        return Boolean(isRendered);
    };
};

var ElectionCtrl = function($scope, $routeParams, $location, Voodie){
    $scope.election = Voodie.getElection($routeParams.e);
    $scope.candidateChoice = "";
    $scope.vote = function(){
        Voodie.vote($scope.candidateChoice, function(data){
            $location.path('/elections');
        })
    }
};

var VoteCtrl = function ($scope, $routeParams, Voodie) {
	$scope.currentPage = 1;
    $scope.categories = {};
    //pagination changes
    $scope.$watch('numPages + currentPage + maxSize', function(){
    	$scope.foodTrucks = Voodie.getEntries($scope.currentPage, $routeParams.latitude, $routeParams.longitude, function(data){
    		$scope.noOfPages = data.noOfPages;
    		$scope.foodTruckRows = [];
    		for( var i = 0; i < data.foodTrucks.length; i = i + 2 ){
    			$scope.foodTruckRows.push(i);
    		}
    		for(var i=0;i<data.foodTrucks.length;i++){
    			for(var c=0;c<data.foodTrucks[i].categories.length; c++){
    				var cat = data.foodTrucks[i].categories[c];
    				$scope.categories[cat] = cat;
    			}
    		}
    	});
    });
    	
	$scope.vote = function(foodTruck){
		Voodie.vote(foodTruck.id, $routeParams.latitude, $routeParams.longitude, $routeParams.eatingTime);
		foodTruck.numberOfVotes++;	
	};
};

function FoodTruckRegistrationCtrl($scope, Voodie){
    $scope.districts = Voodie.getDistricts();
	$scope.submit = function(){
		Voodie.registerTruck($scope, 'foodtruck/elections');
	}
};

function FoodieRegistrationCtrl($scope, Voodie){
    $scope.submit = function(){
        Voodie.registerFoodie($scope, 'elections');
    }
}

function FoodTruckElectionCtrl($scope, $dialog, $location, Voodie, User, GoogleMaps, EatingTime){
    function reset(){
        $scope.elections = Voodie.getElections(User.username);
        $scope.election = {
            //TODO smart defaults?
            servingStartTime : EatingTime.roundedTime(),
            servingEndTime : EatingTime.roundedTime(),
            pollOpeningDate : EatingTime.roundedTime(),
            pollClosingDate : EatingTime.roundedTime(),
            candidates : []
        };
        $scope.candidate  = {};
    };
    reset();
    $scope.submit = function(){
        Voodie.createElection($scope.election, function(){
              reset();
        });
    };
    $scope.addCandidate = function(){
        var address = $scope.candidate.address;
        if(address){
            GoogleMaps.geolocate(address,function(longitude, latitude){
                $scope.$apply(function(){
                    var candidate = {
                        "latitude" : latitude,
                        "longitude" : longitude,
                        "displayName": $scope.candidate.displayName
                    };
                    //FIXME lat/long not set
                    $scope.election.candidates.push(candidate);
                    $scope.candidate = {};
                });
            });
        }
    };
    $scope.opts = {
        backdrop: true,
        keyboard: true,
        backdropClick: true,
        backdropFade: true,
        dialogFade: true,
        templateUrl: 'includes/date-time-picker.html',
        controller: 'DateTimeCtrl'
    };
    $scope.openDialog = function(time){
        var d = $dialog.dialog($scope.opts);
        d.open().then(function(){
            $scope.election[time] = EatingTime.getEatingTime();
        });
    };
    $scope.chooseCandidate = function(election){
        $location.path("foodtruck/selection/" + election.id)
    }
};

function FoodTruckSelectionCtrl($scope, $routeParams, $location, Voodie){
    $scope.election = Voodie.getElectionForSelection($routeParams.e);
    $scope.makeSelection = function(candidate){
        Voodie.selectCandidate($scope.selectedCandidate, function(data){
            $location.path('/foodtruck/elections');
        });
    };
}

function FoodTruckProfileCtrl($scope, Voodie, User){
    $scope.foodTruck = Voodie.getFoodTruckProfile(User.username);
}

function FoodieProfileCtrl($scope, Voodie, User){
    $scope.foodie = Voodie.getFoodieProfile(User.username);
}

function LoginCtrl($scope, User){
	$scope.submit = function(){
		User.username = $scope.username;
		User.password = $scope.password;
		User.login("elections");
	}
}

function CheckInCtrl($scope, $routeParams, $location, Voodie){
    $scope.election = Voodie.getElection($routeParams.e);
    $scope.checkedIn = false;
    $scope.checkIn = function(){
        Voodie.checkIn($scope.election.id, function(data){
//            $location.path('elections');
            $scope.checkedIn = true;
        });
    }
}

function FoodTruckCtrl($scope, $routeParams, EatingTime, Voodie, GoogleMaps) {
    $scope.eatingDate = EatingTime.roundedTime();
    $scope.eatingTime = EatingTime.roundedTime();
	//get food truck info
	$scope.foodTruck = Voodie.getFoodTruckInfo($routeParams.id, function(data){
		//get food truck coordinates
    	GoogleMaps.geolocate(data.address,function(longitude, latitude){
			$scope.latitude = latitude;
			$scope.longitude = longitude;
			//init map based on coords
    		var ll = new google.maps.LatLng($scope.latitude, $scope.longitude);
    		$scope.mapOptions = {
        		center: ll,
        		zoom: 12,
        		mapTypeId: google.maps.MapTypeId.ROADMAP
    		};
			$scope.map = new google.maps.Map(document.getElementById("map-canvas"), $scope.mapOptions);
			//get voting data and create heat map
            $scope.eatingTimestamp = EatingTime.getEatingTime(), 
			Voodie.getVotes($routeParams.id, EatingTime.getEatingTime().getTime(), function(data){
				var votingData = [];
				var votes = data.votes;
				for(var i=0;i<votes.length;i++){
					 votingData.push(new google.maps.LatLng(votes[i].latitude, votes[i].longitude))	
				}
				$scope.pointArray = new google.maps.MVCArray(votingData);
				$scope.heatmap = new google.maps.visualization.HeatmapLayer({
					data: $scope.pointArray
				});
				$scope.heatmap.setMap($scope.map);
			});
		});
	});
    $scope.updateMap = function(){
        EatingTime.setEatingTime(EatingTime.timeStamp($scope.eatingDate, $scope.eatingTime));
        //reset heat map
        if($scope.heatmap){
            $scope.heatmap.setMap(null);
        }
        //this should be moved to a promise?
        Voodie.getVotes($routeParams.id, EatingTime.getEatingTime().getTime(), function(data){
			var votingData = [];
			var votes = data.votes;
			for(var i=0;i<votes.length;i++){
				 votingData.push(new google.maps.LatLng(votes[i].latitude, votes[i].longitude))	
			}
			$scope.pointArray = new google.maps.MVCArray(votingData);
			$scope.heatmap = new google.maps.visualization.HeatmapLayer({
				data: $scope.pointArray
			});
			$scope.heatmap.setMap($scope.map);
		});
    };
};