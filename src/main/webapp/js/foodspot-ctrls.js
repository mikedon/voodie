var HomeCtrl = function ($scope, $location, GoogleMaps) {
	var eatingTime = new Date();
    if(eatingTime.getMinutes() > 30){
        eatingTime.setMinutes(30);
    }else{
        eatingTime.setMinutes(0);
    }
    $scope.eatingTime = eatingTime;
    $scope.eatingDate = new Date();
    $scope.submit = function(){
    	//TODO check if valid
    	if($scope.address){
    		GoogleMaps.geolocate($scope.address,function(longitude, latitude){
				$scope.$apply(function(){
					$scope.latitude = latitude;
					$scope.longitude = longitude;
                    var eatingTimestamp = new Date($scope.eatingDate.getFullYear(), 
                        $scope.eatingDate.getMonth(), $scope.eatingDate.getDate(), 
                        $scope.eatingTime.getHours(), $scope.eatingTime.getMinutes(), 0, 0);
					$location.path('/votes/' + $scope.latitude + '+' + $scope.longitude + '/' + eatingTimestamp.getTime());
				});
			});
    	}
    };
};

var VoteCtrl = function ($scope, $routeParams, FoodSpot) {
	$scope.foodTrucks = FoodSpot.getEntries($routeParams.latitude, $routeParams.longitude, $routeParams.eatingTime);
	$scope.vote = function(foodTruck){
		FoodSpot.vote(foodTruck.id, $routeParams.latitude, $routeParams.longitude, $routeParams.eatingTime);
		foodTruck.numberOfVotes++;	
	}
};