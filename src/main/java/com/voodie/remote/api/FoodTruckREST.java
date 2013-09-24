package com.voodie.remote.api;

import com.voodie.domain.foodtruck.Category;
import com.voodie.domain.identity.Authorities;
import com.voodie.domain.identity.User;
import com.voodie.domain.service.FoodTruckService;
import com.voodie.domain.service.UserService;
import com.voodie.remote.types.foodtruck.FoodTruck;
import com.voodie.remote.types.foodtruck.FoodTruckRegistration;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/foodtruck")
@Stateless
public class FoodTruckREST {


	@Inject
	protected FoodTruckService foodTruckService;

	@Inject
	protected UserService userService;

    // ---------------------------------

    @Path("/secure/profile")
    @GET
    public Response getProfile(@QueryParam("username") String username){
        com.voodie.domain.foodtruck.FoodTruck domainFoodTruck = foodTruckService.find(username);
        if(domainFoodTruck != null){
            User user = userService.getCurrentUser();
            FoodTruck remoteFt = mapToRemoteFoodTruck(domainFoodTruck, user);
            return Response.ok(remoteFt).build();
        }
        return Response.ok().build();
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


    // ---------------------------------

	protected FoodTruck mapToRemoteFoodTruck(
			com.voodie.domain.foodtruck.FoodTruck ft, User user) {
		FoodTruck remoteFt = new FoodTruck();
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
