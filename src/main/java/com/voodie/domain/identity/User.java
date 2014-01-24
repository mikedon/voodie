package com.voodie.domain.identity;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "username"}))
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
    @Column(nullable = false)
	private String username;

	@NotNull
    @Column(nullable = false)
	private String password;

	private Boolean enabled;

    @NotNull
    @Column(nullable = false)
    private String firstName;

    @NotNull
    @Column(nullable = false)
    private String lastName;

    @NotNull
    @Column(nullable = false)
    private String emailAddress;

	@ManyToMany(fetch = FetchType.EAGER)
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Transient
    public boolean isFoodie(){
        for(Authorities authority : getAuthorities()){
            if(authority.getAuthority().equals(Authorities.FOODIE)){
                return true;
            }
        }
        return false;
    }

    @Transient
    public boolean isFoodTruck() {
        for(Authorities authority : getAuthorities()){
            if(authority.getAuthority().equals(Authorities.FOOD_TRUCK)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other){
            return true;
        }
        if (!(other instanceof User)){
            return false;
        }
        final User user = (User) other;
        if (!user.getUsername().equals(getUsername())){
            return false;
        }
        return true;
    }
}
