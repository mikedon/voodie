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

app.factory('FoodSpot', function($resource){
	return {
		getEntries : function(latitude, longitude, onSuccess){
			var foodTrucks = $resource('rest/foodTruck/entries', {"latitude":latitude,"longitude":longitude}).get(onSuccess);
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
		}
	}
});