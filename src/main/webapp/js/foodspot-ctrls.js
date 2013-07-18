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

function FoodTruckCtrl($scope, $routeParams, FoodSpot, GoogleMaps) {
	//get food truck info
	$scope.foodTruck = FoodSpot.getFoodTruckInfo($routeParams.id, function(data){
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
			var eatingTime = new Date();
			if(eatingTime.getMinutes() > 30){
				eatingTime.setMinutes(30);
			}else{
				eatingTime.setMinutes(0);
			}
			$scope.eatingTime = eatingTime;
			$scope.eatingDate = new Date();
			var eatingTimestamp = new Date($scope.eatingDate.getFullYear(), 
					$scope.eatingDate.getMonth(), $scope.eatingDate.getDate(), 
					$scope.eatingTime.getHours(), $scope.eatingTime.getMinutes(), 0, 0);
			FoodSpot.getVotes($routeParams.id, eatingTimestamp.getTime(), function(data){
				var votingData = [];
				var votes = data.votes;
				for(var i=0;i<votes.length;i++){
					 votingData.push(new google.maps.LatLng(votes[i].latitude, votes[i].longitude))	
				}
				var pointArray = new google.maps.MVCArray(votingData);
				var heatmap = new google.maps.visualization.HeatmapLayer({
					data: pointArray
				});
				heatmap.setMap($scope.map);
			});
		});
	});
};