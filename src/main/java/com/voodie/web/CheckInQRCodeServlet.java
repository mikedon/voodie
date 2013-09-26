package com.voodie.web;

import com.voodie.domain.service.ElectionService;
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
@WebServlet("/checkIn")
public class CheckInQRCodeServlet extends HttpServlet {

    @Inject
    protected ElectionService electionService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QRCode.from("http://www.google.com").to(ImageType.PNG).writeTo(resp.getOutputStream());
    }
}
