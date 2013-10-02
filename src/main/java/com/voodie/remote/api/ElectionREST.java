package com.voodie.remote.api;

import com.google.common.collect.Lists;
import com.voodie.domain.election.VotingDao;
import com.voodie.domain.foodie.Foodie;
import com.voodie.domain.service.ElectionService;
import com.voodie.domain.service.FoodTruckService;
import com.voodie.domain.service.FoodieService;
import com.voodie.domain.service.UserService;
import com.voodie.remote.types.ErrorResponse;
import com.voodie.remote.types.election.*;
import org.dozer.Mapper;

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

    @Inject
    protected VotingDao votingDao;

    @Inject
    protected Mapper mapper;

    // ---------------------------------

    @Path("/districts")
    @GET
    public Response getDistricts(){
        List<District> remoteDistricts = Lists.newArrayList();
        for(com.voodie.domain.election.District d : electionService.getDistricts()){
            remoteDistricts.add(mapper.map(d, District.class));
        }
        return Response.ok(remoteDistricts).build();
    }

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

    @Path("/secure/selectCandidate")
    @POST
    public Response selectCandidate(Candidate candidate){
        com.voodie.domain.election.Candidate domainCandidate = electionService.findCandidate(candidate.getId());
        if(domainCandidate != null){
            electionService.selectCandidate(domainCandidate);
            return Response.ok().build();
        }else{
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setHasErrors(true);
            errorResponse.getErrorMsgs().add("Invalid Candidate");
            return Response.ok(errorResponse).build();
        }
    }

    //only difference between this and getElection is numberOfVotes is set
    @Path("/secure/getElectionForSelection")
    @GET
    public Response getElectionForSelection(@QueryParam("election") Long election){
        Election remoteElection = getRemoteElection(election);
        setNumberOfVotesForElection(remoteElection);
        return Response.ok(remoteElection).build();
    }

    @Path("/secure/getElection")
    @GET
    public Response getElection(@QueryParam("election") Long election){
        Election remoteElection = getRemoteElection(election);
        return Response.ok(remoteElection).build();
    }

	@Path("/query")
	@GET
	public Response query(@QueryParam("district") String district) {
		List<com.voodie.domain.election.Election> domainElections = electionService.getAllElectionsInProgress(district, new Date(), new Date());
        List<Election> remoteElections = Lists.newArrayList();
        for(com.voodie.domain.election.Election domainElection : domainElections){
            remoteElections.add(mapper.map(domainElection, Election.class));
        }
		return Response.ok(remoteElections).build();
	}

    @Path("/secure/getAllElections")
    @GET
    public Response getAllElections(@QueryParam("username") String username){
        List<Election> remoteElections = Lists.newArrayList();
        List<com.voodie.domain.election.Election> domainElections = foodTruckService.findAllElections(username);
        for(com.voodie.domain.election.Election domainElection : domainElections){
            Election remoteElection = mapper.map(domainElection, Election.class);
            setNumberOfVotesForElection(remoteElection);
            remoteElections.add(remoteElection);
        }
        return Response.ok(remoteElections).build();
    }

    @Path("/secure/createElection")
    @POST
    public Response createElection(Election remoteElection){
        List<com.voodie.domain.election.Candidate> candidates = Lists.newArrayList();
        for(Candidate remoteCandidate : remoteElection.getCandidates()){
            com.voodie.domain.election.Candidate domainCandidate =
                    mapper.map(remoteCandidate, com.voodie.domain.election.Candidate.class);
            candidates.add(domainCandidate);
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

    @Path("/secure/checkIn")
    @POST
    public Response checkIn(CheckIn checkIn){
        //TODO validate election
        electionService.createCheckIn(foodieService.find(userService.getCurrentUser().getUsername())
                , electionService.findElection(checkIn.getElection()));
        return Response.ok().build();
    }

    // ---------------------------------

    protected Election getRemoteElection(Long electionId){
        com.voodie.domain.election.Election domainElection = electionService.findElection(electionId);
        Election remoteElection = new Election();
        mapper.map(domainElection, remoteElection);
        return remoteElection;
    }

    protected void setNumberOfVotesForElection(Election election){
        for(Candidate c : election.getCandidates()){
            c.setNumberOfVotes(votingDao.getNumberOfVotesForCandidate(c.getId()));
        }
    }
}
