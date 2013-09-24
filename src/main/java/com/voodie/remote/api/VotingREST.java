package com.voodie.remote.api;

import com.voodie.domain.service.VotingService;
import com.voodie.remote.types.election.Vote;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/voting")
@Stateless
public class VotingREST {

	@Inject
	protected VotingService votingService;

    // ---------------------------------

	@Path("/vote")
	@POST
	public Response vote(Vote vote) {
		return Response.ok(Status.OK).build();
	}
}
