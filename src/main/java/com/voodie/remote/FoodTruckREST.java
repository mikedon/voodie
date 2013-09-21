package com.voodie.remote;

import com.google.common.collect.Lists;
import com.voodie.domain.*;
import com.voodie.domain.Candidate;
import com.voodie.domain.Election;
import com.voodie.domain.FoodTruck;
import com.voodie.domain.User;
import com.voodie.remote.domain.*;
import com.voodie.service.FoodTruckService;
import com.voodie.service.UserService;
import com.voodie.service.YelpService;
import com.voodie.service.YelpService.SearchResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("/foodTruck")
@Stateless
public class FoodTruckREST {

	private static Logger log = LoggerFactory.getLogger(FoodTruckREST.class);

	@Inject
	protected YelpService yelpService;

	@Inject
	protected FoodTruckService foodTruckService;

	@Inject
	protected UserService userService;

	@Path("/info")
	@GET
	public Response getFoodTruck(@QueryParam("foodTruckId") String foodTruckId) {
		FoodTruck domainFoodTruck = yelpService.getFoodTruck(foodTruckId);
		com.voodie.remote.domain.FoodTruck remoteFt = mapToRemoteFoodTruck(domainFoodTruck, null);
		return Response.ok(remoteFt).build();
	}

    @Path("/secure/profile")
    @GET
    public Response getProfile(@QueryParam("username") String username){
        FoodTruck domainFoodTruck = foodTruckService.find(username);
        if(domainFoodTruck != null){
            User user = userService.getCurrentUser();
            com.voodie.remote.domain.FoodTruck remoteFt = mapToRemoteFoodTruck(domainFoodTruck, user);
            return Response.ok(remoteFt).build();
        }
        return Response.ok().build();
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
			com.voodie.remote.domain.FoodTruck remoteFt = mapToRemoteFoodTruck(ft, null);
			foodTrucksRemote.getFoodTrucks().add(remoteFt);
		}
		foodTrucksRemote.setNoOfResults(searchResults.getNoOfResults());
		foodTrucksRemote.setNoOfPages(searchResults.getNoOfPages());
		return Response.ok(foodTrucksRemote).build();
	}

	@Path("/register")
	@POST
	public Response register(FoodTruckRegistration registration) {
		User user = userService.create(registration.getFirstName(), registration.getLastName(), registration.getUsername(),
				registration.getPassword(), Authorities.FOOD_TRUCK);
		if (user != null) {
			boolean foodTruckCreated = foodTruckService.create(user, registration.getName());
			if (foodTruckCreated) {
                userService.autoLogin(user);
				return Response.ok().build();
			}
		}
		// TODO want to return something else...not an error code
		return Response.status(Status.CONFLICT).build();
	}

    @Path("/secure/createElection")
    @POST
    public Response createElection(com.voodie.remote.domain.Election remoteElection){
        List<Candidate> candidates = Lists.newArrayList();
        for(com.voodie.remote.domain.Candidate remoteCandidate : remoteElection.getCandidates()){
            Candidate candidate = new Candidate();
            candidate.setDisplayName(remoteCandidate.getDisplayName());
            candidate.setLongitude(remoteCandidate.getLongitude());
            candidate.setLatitude(remoteCandidate.getLatitude());
            candidates.add(candidate);
        }
        if(foodTruckService.createElection(userService.getCurrentUser().getUsername(),
                remoteElection.getTitle(),
                remoteElection.getServingStartTime(),
                remoteElection.getServingEndTime(),
                remoteElection.getPollOpeningDate(),
                remoteElection.getPollClosingDate(), candidates) != null){
            return Response.ok().build();
        }
        // TODO want to return something else...not an error code
        return Response.status(Status.CONFLICT).build();
    }

    @Path("/secure/getAllElections")
    @GET
    public Response getAllElections(@QueryParam("username") String username){
        List<com.voodie.remote.domain.Election> remoteElections = Lists.newArrayList();
        List<Election> domainElections = foodTruckService.findAllElections(username);
        for(Election domainElection : domainElections){
            com.voodie.remote.domain.Election remoteElection = new com.voodie.remote.domain.Election();
            remoteElection.setServingStartTime(domainElection.getServingStartTime());
            remoteElection.setServingEndTime(domainElection.getServingEndTime());
            remoteElection.setPollOpeningDate(domainElection.getPollOpeningDate());
            remoteElection.setPollClosingDate(domainElection.getPollClosingDate());
            remoteElection.setAllowWriteIn(domainElection.getAllowWriteIn());
            remoteElection.setTitle(domainElection.getTitle());
            for(Candidate domainCandidate : domainElection.getCandidates()){
                com.voodie.remote.domain.Candidate remoteCandidate = new com.voodie.remote.domain.Candidate();
                remoteCandidate.setDisplayName(domainCandidate.getDisplayName());
                remoteCandidate.setLatitude(domainCandidate.getLatitude());
                remoteCandidate.setLongitude(domainCandidate.getLongitude());
                remoteElection.getCandidates().add(remoteCandidate);
            }
            remoteElections.add(remoteElection);
        }
        return Response.ok(remoteElections).build();
    }

    // ---------------------------------

	protected com.voodie.remote.domain.FoodTruck mapToRemoteFoodTruck(
			FoodTruck ft, User user) {
		com.voodie.remote.domain.FoodTruck remoteFt = new com.voodie.remote.domain.FoodTruck();
		remoteFt.setId(ft.getExternalId());
		remoteFt.setName(ft.getName());
        if(user != null){
            remoteFt.setFirstName(user.getFirstName());
            remoteFt.setLastName(user.getLastName());
            remoteFt.setUsername(user.getUsername());
        }
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

}
