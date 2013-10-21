package com.voodie.remote.api;

import com.voodie.domain.identity.Authorities;
import com.voodie.domain.identity.User;
import com.voodie.domain.service.FoodieService;
import com.voodie.domain.service.UserService;
import com.voodie.remote.types.foodie.Foodie;
import com.voodie.remote.types.foodie.FoodieRegistration;
import org.dozer.Mapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/foodie")
@Stateless
public class FoodieREST {

	@Inject
	protected UserService userService;

    @Inject
    protected FoodieService foodieService;

    @Inject
    protected Mapper mapper;

    // ---------------------------------

    @Path("/secure/profile")
    @GET
    public Response getProfile(@QueryParam("username") String username){
        com.voodie.domain.foodie.Foodie domainFoodie = foodieService.find(username);
        if(domainFoodie != null){
            Foodie remoteFoodie = mapper.map(domainFoodie, Foodie.class);
            return Response.ok(remoteFoodie).build();
        }
        return Response.ok().build();
    }

	@Path("/register")
	@POST
	public Response register(FoodieRegistration registration) {
		User user = userService.create(registration.getFirstName(), registration.getLastName(), registration.getUsername(),
				registration.getPassword(), registration.getEmailAddress(), Authorities.FOODIE);
		if (user != null) {
			boolean foodieCreated = foodieService.create(user);
			if (foodieCreated) {
                userService.autoLogin(user);
				return Response.ok().build();
			}
		}
		// TODO want to return something else...not an error code
		return Response.status(Status.CONFLICT).build();
	}

}
