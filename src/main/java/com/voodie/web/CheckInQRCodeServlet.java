package com.voodie.web;

import com.voodie.domain.service.ElectionService;
import com.voodie.jmx.CheckInConfiguration;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    protected CheckInConfiguration checkInConfiguration;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String election = req.getParameter(ELECTION_PARAM);
        if(election != null){
            //TODO validate election
            QRCode.from(String.format(checkInConfiguration.getCheckInUrl(), election))
                .to(ImageType.PNG).writeTo(resp.getOutputStream());
        }

    }
}
