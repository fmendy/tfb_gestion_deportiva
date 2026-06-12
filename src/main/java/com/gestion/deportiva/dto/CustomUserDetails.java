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
	private List<Long> listEmpresaId = new ArrayList<>();
	private List<Long> listSedeId = new ArrayList<>();
	private List<Long> listInstalacionId = new ArrayList<>();
	private Collection<? extends GrantedAuthority> authorities;

	// getters, setters, etc.

	public CustomUserDetails(Long userId, String username, String password, Set<GrantedAuthority> authorities) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
	}

	public CustomUserDetails(Long userId, String username, String password, Set<GrantedAuthority> authorities,
			List<Long> listEmpresaId) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.listEmpresaId = listEmpresaId;
	}

	public CustomUserDetails(Long userId, String username, String password, Set<GrantedAuthority> authorities,
			List<Long> listEmpresaId, List<Long> listSedeId, List<Long> listInstalacionId) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.authorities = authorities;
		this.listEmpresaId = listEmpresaId;
		this.listSedeId = listSedeId;
		this.listInstalacionId = listInstalacionId;
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

	public List<Long> getListEmpresaId() {
		return listEmpresaId;
	}

	public void setListEmpresaId(List<Long> listEmpresaId) {
		this.listEmpresaId = listEmpresaId;
	}

	public List<Long> getListSedeId() {
		return listSedeId;
	}

	public void setListSedeId(List<Long> listSedeId) {
		this.listSedeId = listSedeId;
	}

	public List<Long> getListInstalacionId() {
		return listInstalacionId;
	}

	public void setListInstalacionId(List<Long> listInstalacionId) {
		this.listInstalacionId = listInstalacionId;
	}

}
