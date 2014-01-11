package com.voodie.web;

import com.google.gson.Gson;
import com.voodie.remote.types.Alert;
import com.voodie.remote.types.AlertType;
import com.voodie.remote.types.VoodieResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * http://www.baeldung.com/2011/10/31/securing-a-restful-web-service-with-spring
 * -security-3-1-part-3/#ch_3_2
 * 
 * 
 * @author MikeD
 * 
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		VoodieResponse resp = new VoodieResponse();
		resp.getAlerts().add(new Alert("Please log in first.", AlertType.warning));
		Gson gson = new Gson();
		String json = gson.toJson(resp);
		response.getOutputStream().print(json);
	}

}
