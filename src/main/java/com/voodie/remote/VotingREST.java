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

import com.voodie.remote.domain.Vote;
import com.voodie.remote.domain.Votes;
import com.voodie.service.VotingService;

@Path("/voting")
@Stateless
public class VotingREST {

	@Inject
	protected VotingService votingService;

	@Path("/vote")
	@POST
	public Response vote(Vote vote) {
		Date date = new Date(vote.getEatingTime());
		votingService.vote(vote.getFoodTruckId(), date, vote.getLatitude(),
				vote.getLongitude());
		return Response.ok(Status.OK).build();
	}

	@Path("/entries")
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
