package com.voodie.remote;

import com.google.common.collect.Lists;
import com.voodie.domain.Candidate;
import com.voodie.remote.domain.Election;
import com.voodie.service.ElectionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("/election")
@Stateless
public class ElectionREST {

    @Inject
    protected ElectionService electionService;

	@Path("/getAll")
	@GET
	public Response getAllElections() {
		List<com.voodie.domain.Election> domainElections = electionService.getAllElectionsInProgress(new Date(), new Date());
        List<Election> remoteElections = Lists.newArrayList();
        for(com.voodie.domain.Election domainElection : domainElections){
            Election remoteElection = new Election();
            remoteElection.setId(domainElection.getId());
            remoteElection.setTitle(domainElection.getTitle());
            remoteElection.setAllowWriteIn(domainElection.getAllowWriteIn());
            remoteElection.setServingStartTime(domainElection.getServingStartTime());
            remoteElection.setServingEndTime(domainElection.getServingEndTime());
            remoteElection.setPollClosingDate(domainElection.getPollClosingDate());
            remoteElection.setPollOpeningDate(domainElection.getPollOpeningDate());
            for(Candidate domainCandidate : domainElection.getCandidates()){
                com.voodie.remote.domain.Candidate remoteCandidate = new com.voodie.remote.domain.Candidate();
                remoteCandidate.setDisplayName(domainCandidate.getDisplayName());
                remoteCandidate.setLatitude(domainCandidate.getLatitude());
                remoteCandidate.setLongitude(domainCandidate.getLongitude());
                remoteElection.getCandidates().add(remoteCandidate);
            }
            remoteElections.add(remoteElection);
        }
		return Response.ok(remoteElections).build();
	}
}
