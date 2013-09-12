package com.voodie.remote;

import com.voodie.domain.Authorities;
import com.voodie.domain.Category;
import com.voodie.domain.FoodTruck;
import com.voodie.domain.User;
import com.voodie.remote.domain.FoodTruckRegistration;
import com.voodie.remote.domain.FoodTrucks;
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
		com.voodie.remote.domain.FoodTruck remoteFt = mapToRemoteFoodTruck(domainFoodTruck);
		return Response.ok(remoteFt).build();
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

	@Path("/register")
	@POST
	public Response register(FoodTruckRegistration registration) {
		User user = userService.create(registration.getFirstName(), registration.getLastName(), registration.getUsername(),
				registration.getPassword(), Authorities.FOOD_TRUCK);
		if (user != null) {
			boolean foodTruckCreated = foodTruckService.create(registration.getName());
			if (foodTruckCreated) {
                Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
				return Response.ok().build();
			}
		}
		// TODO want to return something else...not an error code
		return Response.status(Status.CONFLICT).build();
	}

	// ----------------------

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

}
