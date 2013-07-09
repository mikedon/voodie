var app = angular.module('foodSpot', ['ngResource']);
app.config(["$routeProvider", function($routeProvider){
	$routeProvider.when('/home', {templateUrl:'templates/home.html'});
	$routeProvider.when('/votes/:latitude+:longitude/:eatingTime', {templateUrl:'templates/votes.html'});
	$routeProvider.otherwise({redirectTo: '/home'});
}]);

app.factory('GoogleMaps', function(){
	return {
		googleMaps : new google.maps.Geocoder(),
		geolocate : function(address, onSuccess){
			this.googleMaps.geocode({"address": address}, function(results, status){
				if(status == google.maps.GeocoderStatus.OK){
					onSuccess(results);
				}
			});
		}
	}
});

app.factory('FoodSpot', function($resource){
	return {
		getEntries : function(latitude, longitude){
			var foodTrucks = $resource('rest/foodTruck/entries', {"latitude":latitude,"longitude":longitude}).get();
            return foodTrucks;
		}
	}
});

app.directive("bsinput", function(){
	return {
		restrict: "E",
		compile: function(element, attrs){
			var type = attrs.type || 'text';
			var id = attrs.id;
			var label = attrs.label;
			var html = "<div class='control-group'><label class='control-label' for='" + id + "'>" + label + "</label><div class='controls'><input type='" + type + "' id='" + id + "' class='input-block-level' ng-model='" + id + "'></div></div>" 
			element.replaceWith(html);
		}
	}
})

app.directive("blur", function(){
	return function($scope, element, attrs){
            element.bind("blur", function(event) {
                $scope.$apply(attrs.blur);
            });
	}
})

var HomeCtrl = function ($scope, GoogleMaps) {
	$scope.geolocate = function(){
		if($scope.address){
			GoogleMaps.geolocate($scope.address,function(results){
				$scope.$apply(function(){
					$scope.latitude = results[0].geometry.location.jb;
					$scope.longitude = results[0].geometry.location.kb
				});
			});
		}
	}
};

var VoteCtrl = function ($scope, $routeParams, FoodSpot) {
	$scope.foodTrucks = FoodSpot.getEntries($routeParams.latitude, $routeParams.longitude);
};