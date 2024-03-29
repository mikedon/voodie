package com.voodie.remote.api;

import com.google.common.collect.Lists;
import com.voodie.domain.election.VotingDao;
import com.voodie.domain.foodie.Foodie;
import com.voodie.domain.identity.User;
import com.voodie.domain.service.ElectionService;
import com.voodie.domain.service.FoodTruckService;
import com.voodie.domain.service.FoodieService;
import com.voodie.domain.service.UserService;
import com.voodie.remote.types.Alert;
import com.voodie.remote.types.AlertType;
import com.voodie.remote.types.VoodieResponse;
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
            VoodieResponse errorResponse = new VoodieResponse();
            errorResponse.getAlerts().add(new Alert("Duplicate Votes", AlertType.danger));
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
            VoodieResponse errorResponse = new VoodieResponse();
            errorResponse.getAlerts().add(new Alert("Invalid Candidate", AlertType.danger));
            return Response.ok(errorResponse).build();
        }
    }

    //only difference between this and getElection is numberOfVotes is set
    @Path("/secure/getElectionForSelection")
    @GET
    public Response getElectionForSelection(@QueryParam("election") Long electionId){
        com.voodie.domain.election.Election domainElection = electionService.findElection(electionId);
        User currentUser = userService.getCurrentUser();
        if(currentUser != null && currentUser.equals(domainElection.getFoodTruck().getUser())){
            Election remoteElection = new Election();
            mapper.map(domainElection, remoteElection);
            setNumberOfVotesForElection(remoteElection);
            return Response.ok(remoteElection).build();
        }else{
            VoodieResponse errorResponse = new VoodieResponse();
            errorResponse.getAlerts().add(new Alert("Unable to get election", AlertType.danger));
            return Response.ok(errorResponse).build();
        }
    }

    @Path("/secure/getElection")
    @GET
    public Response getSecureElection(@QueryParam("election") Long electionId){
        com.voodie.domain.election.Election domainElection = electionService.findElection(electionId);
        User currentUser = userService.getCurrentUser();
        if(currentUser != null && currentUser.equals(domainElection.getFoodTruck().getUser())){
            Election remoteElection = new Election();
            mapper.map(domainElection, remoteElection);
            return Response.ok(remoteElection).build();
        }else{
            VoodieResponse errorResponse = new VoodieResponse();
            errorResponse.getAlerts().add(new Alert("Unable to get election", AlertType.danger));
            return Response.ok(errorResponse).build();
        }
    }

    @Path("/getElection")
    @GET
    public Response getElection(@QueryParam("election") Long election){
        Election remoteElection = getRemoteElection(election);
        return Response.ok(remoteElection).build();
    }

	@Path("/query")
	@GET
	public Response query(@QueryParam("district") String district, @QueryParam("startDate") Date startDate, @QueryParam("endDate") Date endDate) {
		List<com.voodie.domain.election.Election> domainElections = electionService.getAllElectionsInProgress(district, startDate, endDate);
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
        com.voodie.domain.election.Election domainElection = electionService.createElection(userService.getCurrentUser().getUsername(),
                remoteElection.getTitle(),
                remoteElection.getServingStartTime(),
                remoteElection.getServingEndTime(),
                remoteElection.getPollOpeningDate(),
                remoteElection.getPollClosingDate(), null, remoteElection.getAllowWriteIn());
        if(domainElection != null){
                Election election = new Election();
                election.setId(domainElection.getId());
                election.getAlerts().add(new Alert("Election created successfully", AlertType.success));
                return Response.ok(election).build();
        }
        VoodieResponse response = new VoodieResponse();
        response.getAlerts().add(new Alert("Duplicate Election", AlertType.danger));
        return Response.ok(response).build();
    }

    @Path("/secure/addCandidate")
    @POST
    public Response addCandidate(Candidate remoteCandidate){
        com.voodie.domain.election.Candidate domainCandidate =
                mapper.map(remoteCandidate, com.voodie.domain.election.Candidate.class);
        boolean added = electionService.addCandidate(remoteCandidate.getElectionId(), domainCandidate);
        VoodieResponse response = new VoodieResponse();
        if(added){
            response.getAlerts().add(new Alert("Candidate added successfully", AlertType.success));
        }else{
            response.getAlerts().add(new Alert("Candidate already exists.", AlertType.danger));
        }
        return Response.ok(response).build();
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
        Long totalVotes = votingDao.getNumberOfVotesInElection(election.getId());
        if(totalVotes > 0){
            for(Candidate c : election.getCandidates()){
                c.setNumberOfVotes(votingDao.getNumberOfVotesForCandidate(c.getId()));
                c.setPercentageOfVotes(c.getNumberOfVotes()/(double)totalVotes * 100);
            }
        }
    }
}
