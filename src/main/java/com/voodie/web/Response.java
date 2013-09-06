package com.voodie.web;

import java.util.List;

import com.google.common.collect.Lists;

public class Response {

	private List<String> errorMsgs = Lists.newArrayList();
	private Boolean hasErrors = false;

	public List<String> getErrorMsgs() {
		return errorMsgs;
	}

	public void setErrorMsgs(List<String> errorMsgs) {
		this.errorMsgs = errorMsgs;
	}

	public Boolean getHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(Boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

}
