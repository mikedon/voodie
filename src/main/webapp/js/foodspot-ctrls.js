var HomeCtrl = function ($scope, $location, $dialog, EatingTime, GoogleMaps) {
    $scope.eatingTimestamp = EatingTime.getEatingTime(), 
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
        templateUrl: 'templates/includes/date-time-picker.html',
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

var VoteCtrl = function ($scope, $routeParams, FoodSpot) {
	$scope.currentPage = 1;
    $scope.categories = {};
    //pagination changes
    $scope.$watch('numPages + currentPage + maxSize', function(){
    	$scope.foodTrucks = FoodSpot.getEntries($scope.currentPage, $routeParams.latitude, $routeParams.longitude, function(data){
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
		FoodSpot.vote(foodTruck.id, $routeParams.latitude, $routeParams.longitude, $routeParams.eatingTime);
		foodTruck.numberOfVotes++;	
	};
};

function FoodTruckCtrl($scope, $routeParams, EatingTime, FoodSpot, GoogleMaps) {
    $scope.eatingDate = EatingTime.roundedTime();
    $scope.eatingTime = EatingTime.roundedTime();
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
            $scope.eatingTimestamp = EatingTime.getEatingTime(), 
			FoodSpot.getVotes($routeParams.id, EatingTime.getEatingTime().getTime(), function(data){
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
        FoodSpot.getVotes($routeParams.id, EatingTime.getEatingTime().getTime(), function(data){
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