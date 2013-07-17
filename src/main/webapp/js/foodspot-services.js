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
		getEntries : function(latitude, longitude, eatingTime){
			var foodTrucks = $resource('rest/foodTruck/entries', {"latitude":latitude,"longitude":longitude,"eatingTime":eatingTime}).get();
            return foodTrucks;
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
		getVotes : function(foodTruckId, eatingTime){
			var votes = $resource('rest/foodTruck/vote/entries', {"foodTruckId":foodTruckId, "eatingTime" : eatingTime}).get();
			return votes;
		}
	}
});