package com.voodie.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

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
		Response resp = new Response();
		resp.getErrorMsgs().add("Authentication Required");
		resp.setHasErrors(true);
		Gson gson = new Gson();
		String json = gson.toJson(resp);
		response.getOutputStream().print(json);
	}

}
