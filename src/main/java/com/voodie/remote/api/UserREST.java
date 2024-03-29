package com.voodie.remote.api;

import com.voodie.domain.foodie.Foodie;
import com.voodie.domain.foodtruck.FoodTruck;
import com.voodie.domain.service.FoodTruckService;
import com.voodie.domain.service.FoodieService;
import com.voodie.domain.service.UserService;
import com.voodie.remote.types.identity.User;
import org.dozer.Mapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/user")
@Stateless
public class UserREST {

	@Inject
	protected UserService userService;

    @Inject
    protected FoodieService foodieService;

    @Inject
    protected FoodTruckService foodTruckService;

    @Inject
    protected Mapper mapper;

    // ---------------------------------

	@Path("/secure/currentUser")
	@GET
	public Response getCurrentUser() {
		User user = new User();
		com.voodie.domain.identity.User currentUser = userService.getCurrentUser();
		if (currentUser != null) {
            mapper.map(currentUser, user);
		}
        String district = null;
        if(currentUser.isFoodie()){
            Foodie foodie = foodieService.find(currentUser.getUsername());
            district = foodie.getHomeDistrict().getName();
        }else if(currentUser.isFoodTruck()){
            FoodTruck foodTruck = foodTruckService.find(currentUser.getUsername());
            district = foodTruck.getDistrict().getName();
        }
        user.setDistrict(district);
		return Response.ok(user).build();
	}

}
