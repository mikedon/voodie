package com.voodie.remote.api;

import com.google.common.collect.Lists;
import com.voodie.domain.foodie.Foodie;
import com.voodie.domain.service.ElectionService;
import com.voodie.domain.service.FoodTruckService;
import com.voodie.domain.service.FoodieService;
import com.voodie.domain.service.UserService;
import com.voodie.remote.types.ErrorResponse;
import com.voodie.remote.types.election.Candidate;
import com.voodie.remote.types.election.Election;
import com.voodie.remote.types.election.Vote;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("/election")
@Stateless
public class ElectionREST {

    @Inject
    protected ElectionService electionService;

    @Inject
    protected FoodTruckService foodTruckService;

    @Inject
    protected FoodieService foodieService;

    @Inject
    protected UserService userService;

    // ---------------------------------

    @Path("/secure/vote")
    @POST
    public Response vote(Vote vote){
        Foodie foodie = foodieService.find(userService.getCurrentUser().getUsername());
        com.voodie.domain.election.Candidate candidate = electionService.findCandidate(vote.getCandidate());
        if(electionService.vote(foodie, candidate) != null){
            return Response.ok().build();
        }else{
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setHasErrors(true);
            errorResponse.getErrorMsgs().add("Duplicate Votes");
            return Response.ok(errorResponse).build();
        }
    }


    @Path("/secure/getElection")
    @GET
    public Response getElection(@QueryParam("election") Long election){
        com.voodie.domain.election.Election domainElection = electionService.findElection(election);
        Election remoteElection = new Election();
        remoteElection.setId(domainElection.getId());
        remoteElection.setTitle(domainElection.getTitle());
        remoteElection.setAllowWriteIn(domainElection.getAllowWriteIn());
        remoteElection.setServingStartTime(domainElection.getServingStartTime());
        remoteElection.setServingEndTime(domainElection.getServingEndTime());
        remoteElection.setPollClosingDate(domainElection.getPollClosingDate());
        remoteElection.setPollOpeningDate(domainElection.getPollOpeningDate());
        for(com.voodie.domain.election.Candidate domainCandidate : domainElection.getCandidates()){
            Candidate remoteCandidate = new Candidate();
            remoteCandidate.setId(domainCandidate.getId());
            remoteCandidate.setDisplayName(domainCandidate.getDisplayName());
            remoteCandidate.setLatitude(domainCandidate.getLatitude());
            remoteCandidate.setLongitude(domainCandidate.getLongitude());
            remoteElection.getCandidates().add(remoteCandidate);
        }
        return Response.ok(remoteElection).build();
    }

	@Path("/query")
	@GET
	public Response query() {
		List<com.voodie.domain.election.Election> domainElections = electionService.getAllElectionsInProgress(new Date(), new Date());
        List<Election> remoteElections = Lists.newArrayList();
        for(com.voodie.domain.election.Election domainElection : domainElections){
            Election remoteElection = new Election();
            remoteElection.setId(domainElection.getId());
            remoteElection.setTitle(domainElection.getTitle());
            remoteElection.setAllowWriteIn(domainElection.getAllowWriteIn());
            remoteElection.setServingStartTime(domainElection.getServingStartTime());
            remoteElection.setServingEndTime(domainElection.getServingEndTime());
            remoteElection.setPollClosingDate(domainElection.getPollClosingDate());
            remoteElection.setPollOpeningDate(domainElection.getPollOpeningDate());
            for(com.voodie.domain.election.Candidate domainCandidate : domainElection.getCandidates()){
                Candidate remoteCandidate = new Candidate();
                remoteCandidate.setDisplayName(domainCandidate.getDisplayName());
                remoteCandidate.setLatitude(domainCandidate.getLatitude());
                remoteCandidate.setLongitude(domainCandidate.getLongitude());
                remoteElection.getCandidates().add(remoteCandidate);
            }
            remoteElections.add(remoteElection);
        }
		return Response.ok(remoteElections).build();
	}

    @Path("/secure/getAllElections")
    @GET
    public Response getAllElections(@QueryParam("username") String username){
        List<Election> remoteElections = Lists.newArrayList();
        List<com.voodie.domain.election.Election> domainElections = foodTruckService.findAllElections(username);
        for(com.voodie.domain.election.Election domainElection : domainElections){
            Election remoteElection = new Election();
            remoteElection.setServingStartTime(domainElection.getServingStartTime());
            remoteElection.setServingEndTime(domainElection.getServingEndTime());
            remoteElection.setPollOpeningDate(domainElection.getPollOpeningDate());
            remoteElection.setPollClosingDate(domainElection.getPollClosingDate());
            remoteElection.setAllowWriteIn(domainElection.getAllowWriteIn());
            remoteElection.setTitle(domainElection.getTitle());
            for(com.voodie.domain.election.Candidate domainCandidate : domainElection.getCandidates()){
                Candidate remoteCandidate = new Candidate();
                remoteCandidate.setDisplayName(domainCandidate.getDisplayName());
                remoteCandidate.setLatitude(domainCandidate.getLatitude());
                remoteCandidate.setLongitude(domainCandidate.getLongitude());
                remoteElection.getCandidates().add(remoteCandidate);
            }
            remoteElections.add(remoteElection);
        }
        return Response.ok(remoteElections).build();
    }

    @Path("/secure/createElection")
    @POST
    public Response createElection(Election remoteElection){
        List<com.voodie.domain.election.Candidate> candidates = Lists.newArrayList();
        for(Candidate remoteCandidate : remoteElection.getCandidates()){
            com.voodie.domain.election.Candidate candidate = new com.voodie.domain.election.Candidate();
            candidate.setDisplayName(remoteCandidate.getDisplayName());
            candidate.setLongitude(remoteCandidate.getLongitude());
            candidate.setLatitude(remoteCandidate.getLatitude());
            candidates.add(candidate);
        }
        if(electionService.createElection(userService.getCurrentUser().getUsername(),
                remoteElection.getTitle(),
                remoteElection.getServingStartTime(),
                remoteElection.getServingEndTime(),
                remoteElection.getPollOpeningDate(),
                remoteElection.getPollClosingDate(), candidates, remoteElection.getAllowWriteIn()) != null){
            return Response.ok().build();
        }
        // TODO want to return something else...not an error code
        return Response.status(Response.Status.CONFLICT).build();
    }
}
