package com.voodie.web;

import com.google.gson.Gson;
import com.voodie.domain.election.Election;
import com.voodie.domain.election.ElectionStatus;
import com.voodie.domain.identity.User;
import com.voodie.domain.service.ElectionService;
import com.voodie.domain.service.UserService;
import com.voodie.jmx.CheckInConfiguration;
import com.voodie.remote.types.Alert;
import com.voodie.remote.types.AlertType;
import com.voodie.remote.types.VoodieResponse;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Voodie
 * User: MikeD
 */
@WebServlet("/secure/checkInQrCode")
public class CheckInQRCodeServlet extends HttpServlet {

    public static final String ELECTION_PARAM = "e";

    @Inject
    protected ElectionService electionService;

    @Inject
    protected UserService userService;

    @Inject
    protected CheckInConfiguration checkInConfiguration;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String electionParam = req.getParameter(ELECTION_PARAM);
        Election election = null;
        try{
            election = electionService.findElection(Long.valueOf(electionParam));
        }catch(NumberFormatException e){
            invalidElection(resp.getOutputStream());
        }
        if(isValidElection(election)){
            CheckInPdf checkIn = new CheckInPdf();
            ByteArrayOutputStream output = checkIn.getCheckInPdf(String.format(checkInConfiguration.getCheckInUrl(), election.getId()));
            resp.addHeader("Content-Type", "application/force-download");
            resp.addHeader("Content-Disposition", "attachment; filename=\"yourFile.pdf\"");
            resp.getOutputStream().write(output.toByteArray());
        }else{
            invalidElection(resp.getOutputStream());
        }
    }

    // ---------------------------------

    private void invalidElection(ServletOutputStream stream) throws IOException {
        VoodieResponse resp = new VoodieResponse();
        resp.getAlerts().add(new Alert("Invalid Election", AlertType.danger));
        Gson gson = new Gson();
        String json = gson.toJson(resp);
        stream.print(json);
    }

    private boolean isValidElection(Election election){
        User currentUser = userService.getCurrentUser();
        if(election != null && currentUser != null &&
                election.getFoodTruck().getUser().equals(currentUser) &&
                election.getStatus().equals(ElectionStatus.CLOSED)){
            return true;
        }
        return false;
    }
}
