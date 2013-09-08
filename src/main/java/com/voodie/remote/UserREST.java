package com.voodie.remote;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.voodie.service.UserService;

@Path("/user")
@Stateless
public class UserREST {

	@Inject
	protected UserService userService;

	@Path("/secure/roles")
	@GET
	public Response getRoles(@QueryParam("user") String user) {
		return Response.ok(Status.OK).build();
	}

	@Path("/secure/currentUser")
	@GET
	public Response getCurrentUser() {
		return Response.ok(userService.getCurrentUser()).build();
	}
}
