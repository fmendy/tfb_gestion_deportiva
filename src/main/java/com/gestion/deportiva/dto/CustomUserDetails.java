package com.gestion.deportiva.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = -8698409853397020145L;
	private Long userId;
	private String userUuid;
	private String username;
	private String password;
	private List<String> listDemarcacionUuid = new ArrayList<>();
	private Collection<? extends GrantedAuthority> authorities;

	// getters, setters, etc.

	public CustomUserDetails(Long userId, String username, String password, Set<GrantedAuthority> authorities) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public CustomUserDetails(Long userId, String username, String password, Set<GrantedAuthority> authorities,
			List<String> listDemarcacionUuid) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.listDemarcacionUuid = listDemarcacionUuid;
	}

	public CustomUserDetails(Long userId, String userUuid, String username, String password,
			Set<GrantedAuthority> authorities) {
		this.userId = userId;
		this.userUuid = userUuid;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public Long getUserId() {
		return userId;
	}

	public String getUserUuid() {
		return userUuid;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public List<String> getListDemarcacionUuid() {
		return listDemarcacionUuid;
	}

	public void setListDemarcacionUuid(List<String> listDemarcacionUuid) {
		this.listDemarcacionUuid = listDemarcacionUuid;
	}

}
