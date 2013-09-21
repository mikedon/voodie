package com.voodie.remote;

import com.google.common.collect.Lists;
import com.voodie.domain.*;
import com.voodie.remote.domain.FoodTruckRegistration;
import com.voodie.remote.domain.FoodTrucks;
import com.voodie.remote.domain.FoodieRegistration;
import com.voodie.service.FoodTruckService;
import com.voodie.service.FoodieService;
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

@Path("/foodie")
@Stateless
public class FoodieREST {

	private static Logger log = LoggerFactory.getLogger(FoodieREST.class);

	@Inject
	protected UserService userService;

    @Inject
    protected FoodieService foodieService;

    // ---------------------------------

    @Path("/secure/profile")
    @GET
    public Response getProfile(@QueryParam("username") String username){
        Foodie domainFoodie = foodieService.find(username);
        if(domainFoodie != null){
            User user = userService.getCurrentUser();
            com.voodie.remote.domain.Foodie remoteFoodie = new com.voodie.remote.domain.Foodie();
            remoteFoodie.setKarma(domainFoodie.getKarma());
            remoteFoodie.setUsername(domainFoodie.getUser().getUsername());
            remoteFoodie.setFirstName(domainFoodie.getUser().getFirstName());
            remoteFoodie.setLastName(domainFoodie.getUser().getLastName());
            return Response.ok(remoteFoodie).build();
        }
        return Response.ok().build();
    }

	@Path("/register")
	@POST
	public Response register(FoodieRegistration registration) {
		User user = userService.create(registration.getFirstName(), registration.getLastName(), registration.getUsername(),
				registration.getPassword(), Authorities.FOODIE);
		if (user != null) {
			boolean foodieCreated = foodieService.create(user);
			if (foodieCreated) {
                userService.autoLogin(user);
				return Response.ok().build();
			}
		}
//		// TODO want to return something else...not an error code
		return Response.status(Status.CONFLICT).build();
	}

}
