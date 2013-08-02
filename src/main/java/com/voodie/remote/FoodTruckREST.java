package com.voodie.remote;

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

import com.voodie.domain.Category;
import com.voodie.domain.FoodTruck;
import com.voodie.remote.domain.FoodTrucks;
import com.voodie.remote.domain.Vote;
import com.voodie.remote.domain.Votes;
import com.voodie.service.VotingService;
import com.voodie.service.YelpService;
import com.voodie.service.YelpService.SearchResults;

@Path("/foodTruck")
@Stateless
public class FoodTruckREST {

	private static Logger log = LoggerFactory.getLogger(FoodTruckREST.class);

	@Inject
	protected YelpService yelpService;

	@Inject
	protected VotingService votingService;

	@Path("/info")
	@GET
	public Response getFoodTruck(@QueryParam("foodTruckId") String foodTruckId) {
		FoodTruck domainFoodTruck = yelpService.getFoodTruck(foodTruckId);
		com.voodie.remote.domain.FoodTruck remoteFt = mapToRemoteFoodTruck(domainFoodTruck);
		return Response.ok(remoteFt).build();
	}

	protected com.voodie.remote.domain.FoodTruck mapToRemoteFoodTruck(
			FoodTruck ft) {
		com.voodie.remote.domain.FoodTruck remoteFt = new com.voodie.remote.domain.FoodTruck();
		remoteFt.setId(ft.getExternalId());
		remoteFt.setName(ft.getName());
		remoteFt.setRating(ft.getRating());
		remoteFt.setAddress(ft.getAddress());
		remoteFt.setImageUrl(ft.getImageUrl());
		remoteFt.setRatingImageUrl(ft.getRatingImageUrl());
		remoteFt.setUrl(ft.getUrl());
		for (Category c : ft.getCategories()) {
			remoteFt.getCategories().add(c.getName());
		}
		return remoteFt;
	}

	// TODO returns FoodTrucks > FoodTrucks I want FoodTrucks > FoodTruck
	@Path("/entries")
	@GET
	public Response getFoodTruckEntries(@QueryParam("page") Integer page,
			@QueryParam("latitude") Double latitude,
			@QueryParam("longitude") Double longitude) {
		SearchResults searchResults = yelpService.searchFoodTrucks(page,
				latitude, longitude);
		FoodTrucks foodTrucksRemote = new FoodTrucks();
		for (FoodTruck ft : searchResults.getFoodTrucks()) {
			com.voodie.remote.domain.FoodTruck remoteFt = mapToRemoteFoodTruck(ft);
			foodTrucksRemote.getFoodTrucks().add(remoteFt);
		}
		foodTrucksRemote.setNoOfResults(searchResults.getNoOfResults());
		foodTrucksRemote.setNoOfPages(searchResults.getNoOfPages());
		return Response.ok(foodTrucksRemote).build();
	}

	// TODO move votes to separate REST @Path

	@Path("/vote")
	@POST
	public Response vote(Vote vote) {
		Date date = new Date(vote.getEatingTime());
		votingService.vote(vote.getFoodTruckId(), date, vote.getLatitude(),
				vote.getLongitude());
		return Response.ok(Status.OK).build();
	}

	@Path("/vote/entries")
	@GET
	public Response getVotes(@QueryParam("foodTruckId") String foodTruckId,
			@QueryParam("eatingTime") Long eatingTime) {
		Date date = new Date(eatingTime);
		List<com.voodie.domain.Vote> domainVotes = votingService.getVotes(
				foodTruckId, date);
		Votes votes = new Votes();
		for (com.voodie.domain.Vote v : domainVotes) {
			Vote remoteVote = new Vote();
			remoteVote.setEatingTime(eatingTime);
			remoteVote.setFoodTruckId(v.getFoodTruckId());
			remoteVote.setLatitude(v.getLocation().getLatitude());
			remoteVote.setLongitude(v.getLocation().getLongitude());
			votes.getVotes().add(remoteVote);
		}
		return Response.ok(votes).build();
	}

}
