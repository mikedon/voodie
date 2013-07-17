app.factory('GoogleMaps', function(){
	return {
		geolocate : function(address, onSuccess){
			this.googleMaps.geocode({"address": address}, function(results, status){
				if(status == google.maps.GeocoderStatus.OK){
					onSuccess("100.00", "100.00");
				}
			});
		}
	}
});

app.factory('FoodSpot', function($resource){
	return {
		getEntries : function(latitude, longitude, eatingTime){
			var foodTrucks = $resource('rest/foodTruck/entries', {"latitude":latitude,"longitude":longitude,"eatingTime":eatingTime}).get();
			var foodTrucks = new Array();
			for(int i=0; i < 10; i++){
				foodTrucks.push({
					id: i,
					name: "Food  Truck" + i,
					rating: "4.0",
					numberOfVotes: i
				});
			}
            return foodTrucks;
		},
		vote : function(foodTruckId, latitude, longitude, eatingTime){
			//do nothing
		}
	}
});