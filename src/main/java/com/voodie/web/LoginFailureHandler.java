package com.voodie.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		Response resp = new Response();
		resp.getErrorMsgs().add("Invalid Credentials");
		resp.setHasErrors(true);
		Gson gson = new Gson();
		String json = gson.toJson(resp);
		response.getOutputStream().print(json);
	}
}
