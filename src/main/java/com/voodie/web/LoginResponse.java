package com.voodie.web;

import java.util.List;

import com.google.common.collect.Lists;
import com.voodie.remote.types.VoodieResponse;

public class LoginResponse extends VoodieResponse {

	private List<String> roles = Lists.newArrayList();

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
