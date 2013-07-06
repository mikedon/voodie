package com.foodpsot.remote;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foodpsot.remote.domain.FoodTrucks;
import com.foodpsot.remote.domain.Vote;
import com.foodspot.domain.FoodTruck;
import com.foodspot.service.VotingService;
import com.foodspot.service.YelpService;

@Path("/foodTruck")
@Stateless
public class FoodTruckREST {

	private static Logger log = LoggerFactory.getLogger(FoodTruckREST.class);

	@Inject
	protected YelpService yelpService;

	@Inject
	protected VotingService votingService;

	@Path("/entries")
	@GET
	public Response getFoodTruckEntries(String latitude, String longitude) {
		List<FoodTruck> foodTrucks = yelpService.searchFoodTrucks("Arlington");
		FoodTrucks foodTrucksRemote = new FoodTrucks();
		for (FoodTruck ft : foodTrucks) {
			com.foodpsot.remote.domain.FoodTruck remoteFt = new com.foodpsot.remote.domain.FoodTruck();
			remoteFt.setId(ft.getExternalId());
			remoteFt.setName(ft.getName());
			remoteFt.setRating(ft.getRating());
			foodTrucksRemote.getFoodTrucks().add(remoteFt);
		}
		return Response.ok(foodTrucksRemote).build();
	}

	@Path("/vote")
	@POST
	public Response vote(Vote vote) {
		votingService.vote(vote.getFoodTruckId(), vote.getEatingTime(),
				vote.getLatitude(), vote.getLongitude());
		return Response.ok(Status.OK).build();
	}

}
