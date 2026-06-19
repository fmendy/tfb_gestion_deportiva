package com.gestion.deportiva.util;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gestion.deportiva.dto.CustomUserDetails;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtil {

	public static CustomUserDetails getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new IllegalStateException("No hay usuario autenticado");
		}
		return (CustomUserDetails) authentication.getPrincipal();
	}

	public boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.isAuthenticated()
				&& !(authentication instanceof AnonymousAuthenticationToken);
	}

	public static Long getCurrentUserId() {
		return getCurrentUser().getUserId();
	}

	public static String getCurrentUserUuid() {
		return getCurrentUser().getUserUuid();
	}

	public static String getCurrentUserUsername() {
		return getCurrentUser().getUsername();
	}

	public static boolean hasAuthority(String authority) {
		return getCurrentUser().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(authority));
	}

	public static List<String> getCurrentUserRol() {
		List<String> roles = getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.filter(auth -> auth.startsWith("ROLE_")).map(auth -> auth.substring(5))

				.toList();
		return roles;
	}

	public static List<Long> getCurrentUserListEmpresaId() {
		return getCurrentUser().getListEmpresaId();
	}

	public static List<Long> getCurrentUserListSedeId() {
		return getCurrentUser().getListSedeId();
	}

	public static List<Long> getCurrentUserListInstalacionId() {
		return getCurrentUser().getListInstalacionId();
	}

	public static boolean hasGlobalAccess() {
		return hasAuthority(Constantes.Permiso.GESTION_GLOBAL);
	}

	public static boolean hasAnyAuthority(String... authorities) {
		for (String auth: authorities) {
			if (hasAuthority(auth)) {
				return true;
			}
		}
		return false;
	}

}
