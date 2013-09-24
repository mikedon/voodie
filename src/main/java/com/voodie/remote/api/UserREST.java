package com.voodie.remote.api;

import com.voodie.domain.service.UserService;
import com.voodie.remote.types.identity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/user")
@Stateless
public class UserREST {

	@Inject
	protected UserService userService;

    // ---------------------------------

	@Path("/secure/roles")
	@GET
	public Response getRoles(@QueryParam("user") String user) {
		return Response.ok(Status.OK).build();
	}

	@Path("/secure/currentUser")
	@GET
	public Response getCurrentUser() {
		User user = new User();
		com.voodie.domain.identity.User currentUser = userService.getCurrentUser();
		if (currentUser != null) {
			user.setUsername(currentUser.getUsername());
            user.setFirstName(currentUser.getFirstName());
            user.setLastName(currentUser.getLastName());
			for (com.voodie.domain.identity.Authorities authority : currentUser.getAuthorities()) {
				user.getRoles().add(authority.getAuthority());
			}
		}
		return Response.ok(user).build();
	}
}
