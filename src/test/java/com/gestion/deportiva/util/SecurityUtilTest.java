package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gestion.deportiva.dto.CustomUserDetails;

class SecurityUtilTest {

	private SecurityContext securityContext;

	@BeforeEach
	void setUp() {
		securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void getCurrentUserCuandoNoHayAutenticacionDevuelveDefaultUser() {
		when(securityContext.getAuthentication()).thenReturn(null);

		CustomUserDetails user = SecurityUtil.getCurrentUser();

		assertThat(user).isNotNull();
		assertThat(user.getUserId()).isEqualTo(1L);
	}

	@Test
	void getCurrentUserCuandoEsAnonimoDevuelveDefaultUser() {
		Authentication auth = mock(AnonymousAuthenticationToken.class);
		when(auth.isAuthenticated()).thenReturn(true);
		when(securityContext.getAuthentication()).thenReturn(auth);

		CustomUserDetails user = SecurityUtil.getCurrentUser();

		assertThat(user).isNotNull();
		assertThat(user.getUserId()).isEqualTo(1L);
	}

	@Test
	void getCurrentUserConUsuarioAutenticadoValido() {
		CustomUserDetails customUser = new CustomUserDetails(42L, "uuid-123", "testuser", "password",
				Set.of(new SimpleGrantedAuthority("ROLE_USER")));
		Authentication auth = new UsernamePasswordAuthenticationToken(customUser, null,
				List.of(new SimpleGrantedAuthority("ROLE_USER")));
		when(securityContext.getAuthentication()).thenReturn(auth);

		CustomUserDetails user = SecurityUtil.getCurrentUser();

		assertThat(user).isNotNull();
		assertThat(user.getUserId()).isEqualTo(42L);
		assertThat(user.getUserUuid()).isEqualTo("uuid-123");
		assertThat(user.getUsername()).isEqualTo("testuser");
	}

	@Test
	void isAuthenticatedDevuelveFalseSiAuthenticationEsNull() {
		when(securityContext.getAuthentication()).thenReturn(null);

		boolean result = SecurityUtil.isAuthenticated();

		assertThat(result).isFalse();
	}

	@Test
	void isAuthenticatedDevuelveFalseSiEsTokenAnonimo() {
		Authentication auth = mock(AnonymousAuthenticationToken.class);
		when(auth.isAuthenticated()).thenReturn(true);
		when(securityContext.getAuthentication()).thenReturn(auth);

		boolean result = SecurityUtil.isAuthenticated();

		assertThat(result).isFalse();
	}

	@Test
	void isAuthenticatedDevuelveTrueSiEstaAutenticado() {
		CustomUserDetails customUser = new CustomUserDetails(10L, "uuid", "admin", "password",
				Set.of(new SimpleGrantedAuthority("ROLE_USER")));
		Authentication auth = new UsernamePasswordAuthenticationToken(customUser, null,
				List.of(new SimpleGrantedAuthority("ROLE_USER")));
		when(securityContext.getAuthentication()).thenReturn(auth);

		boolean result = SecurityUtil.isAuthenticated();

		assertThat(result).isTrue();
	}

	@Test
	void getCurrentUserIdYMetodosRelacionados() {
		CustomUserDetails customUser = new CustomUserDetails(5L, "uuid-5", "user5", "password",
				Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("PERMISO_X")));
		customUser.setListEmpresaId(List.of(100L));
		customUser.setListSedeId(List.of(200L));
		customUser.setListInstalacionId(List.of(300L));

		Authentication auth = new UsernamePasswordAuthenticationToken(customUser, null,
				List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("PERMISO_X")));
		when(securityContext.getAuthentication()).thenReturn(auth);

		assertThat(SecurityUtil.getCurrentUserId()).isEqualTo(5L);
		assertThat(SecurityUtil.getCurrentUserUuid()).isEqualTo("uuid-5");
		assertThat(SecurityUtil.getCurrentUserUsername()).isEqualTo("user5");
		assertThat(SecurityUtil.hasAuthority("PERMISO_X")).isTrue();
		assertThat(SecurityUtil.hasAuthority("PERMISO_Y")).isFalse();
		assertThat(SecurityUtil.getCurrentUserRol()).containsExactly("ADMIN");
		assertThat(SecurityUtil.getCurrentUserListEmpresaId()).containsExactly(100L);
		assertThat(SecurityUtil.getCurrentUserListSedeId()).containsExactly(200L);
		assertThat(SecurityUtil.getCurrentUserListInstalacionId()).containsExactly(300L);
	}

	@Test
	void hasAnyAuthorityFuncionaCorrectamente() {
		CustomUserDetails customUser = new CustomUserDetails(1L, "uuid", "user", "password",
				Set.of(new SimpleGrantedAuthority("ROLE_USER")));
		Authentication auth = new UsernamePasswordAuthenticationToken(customUser, null,
				List.of(new SimpleGrantedAuthority("ROLE_USER")));
		when(securityContext.getAuthentication()).thenReturn(auth);

		assertThat(SecurityUtil.hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")).isTrue();
		assertThat(SecurityUtil.hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERVISOR")).isFalse();
	}
}