package com.voodie.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;

public class VoodieSavedRequestAwareAuthenticationSuccessHandler extends
		SimpleUrlAuthenticationSuccessHandler {
	private RequestCache requestCache = new HttpSessionRequestCache();

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request,
			final HttpServletResponse response,
			final Authentication authentication) throws ServletException,
			IOException {
		final SavedRequest savedRequest = requestCache.getRequest(request,
				response);

		if (savedRequest == null) {
			clearAuthenticationAttributes(request);
			return;
		}
		final String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParameter != null && StringUtils.hasText(request
						.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			clearAuthenticationAttributes(request);
			return;
		}

		clearAuthenticationAttributes(request);

		LoginResponse user = new LoginResponse();
		for (GrantedAuthority a : authentication.getAuthorities()) {
			user.getRoles().add(a.getAuthority());
		}
		Gson gson = new Gson();
		String json = gson.toJson(user);
		response.getOutputStream().print(json);
	}

	public void setRequestCache(final RequestCache requestCache) {
		this.requestCache = requestCache;
	}
}
