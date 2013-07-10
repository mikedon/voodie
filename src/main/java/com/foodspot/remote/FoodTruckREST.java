package com.foodspot.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foodspot.domain.FoodTruck;
import com.foodspot.remote.domain.FoodTrucks;
import com.foodspot.remote.domain.Vote;
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

	// TODO returns FoodTrucks > FoodTrucks I want FoodTrucks > FoodTruck
	@Path("/entries")
	@GET
	public Response getFoodTruckEntries(
			@QueryParam("latitude") String latitude,
			@QueryParam("longitude") String longitude,
			@QueryParam("eatingTime") Long eatingTime) {
		List<FoodTruck> foodTrucks = yelpService.searchFoodTrucks(latitude,
				longitude);
		FoodTrucks foodTrucksRemote = new FoodTrucks();
		Date date = new Date(eatingTime);
		for (FoodTruck ft : foodTrucks) {
			com.foodspot.remote.domain.FoodTruck remoteFt = new com.foodspot.remote.domain.FoodTruck();
			remoteFt.setId(ft.getExternalId());
			remoteFt.setName(ft.getName());
			remoteFt.setRating(ft.getRating());
			remoteFt.setNumberOfVotes(votingService.getNumberOfVotes(
					ft.getExternalId(), date, latitude, longitude));
			foodTrucksRemote.getFoodTrucks().add(remoteFt);
		}
		return Response.ok(foodTrucksRemote).build();
	}

	@Path("/vote")
	@POST
	public Response vote(Vote vote) {
		Date date = new Date(vote.getEatingTime());
		votingService.vote(vote.getFoodTruckId(), date, vote.getLatitude(),
				vote.getLongitude());
		return Response.ok(Status.OK).build();
	}

}
