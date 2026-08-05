package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UsuarioRolTest {

	@Test
	void gettersYSetters() {
		UsuarioRol usuarioRol = new UsuarioRol();
		Usuario usuario = new Usuario(1L);
		Rol rol = new Rol(2L);

		usuarioRol.setUsuario(usuario);
		usuarioRol.setRol(rol);

		assertThat(usuarioRol.getUsuario()).isEqualTo(usuario);
		assertThat(usuarioRol.getUsuario().getId()).isEqualTo(1L);
		assertThat(usuarioRol.getRol()).isEqualTo(rol);
		assertThat(usuarioRol.getRol().getId()).isEqualTo(2L);
	}

	@Test
	void constructorConUsuarioYRol() {
		Usuario usuario = new Usuario(10L);
		Rol rol = new Rol(20L);

		UsuarioRol usuarioRol = new UsuarioRol(usuario, rol);

		assertThat(usuarioRol.getUsuario()).isEqualTo(usuario);
		assertThat(usuarioRol.getUsuario().getId()).isEqualTo(10L);
		assertThat(usuarioRol.getRol()).isEqualTo(rol);
		assertThat(usuarioRol.getRol().getId()).isEqualTo(20L);
	}
}