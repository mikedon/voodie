package com.voodie.web;

import java.util.List;

import com.google.common.collect.Lists;

public class LoginResponse extends Response {

	private List<String> roles = Lists.newArrayList();

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
