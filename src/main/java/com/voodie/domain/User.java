package com.voodie.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	private String username;

	@NotNull
	private String password;

	private Boolean enabled;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Authorities> authorities;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public List<Authorities> getAuthorities() {
		if (authorities == null) {
			authorities = new ArrayList<Authorities>();
		}
		return authorities;
	}

	public void setAuthorities(List<Authorities> authorities) {
		this.authorities = authorities;
	}

	public void addAuthority(Authorities authority) {
		getAuthorities().add(authority);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
