package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UsuarioInstalacionTest {

	@Test
	void gettersYSetters() {
		UsuarioInstalacion usuarioInstalacion = new UsuarioInstalacion();
		Instalacion instalacion = new Instalacion(1L);
		Usuario usuario = new Usuario(2L);

		usuarioInstalacion.setInstalacion(instalacion);
		usuarioInstalacion.setUsuario(usuario);

		assertThat(usuarioInstalacion.getInstalacion()).isEqualTo(instalacion);
		assertThat(usuarioInstalacion.getInstalacion().getId()).isEqualTo(1L);
		assertThat(usuarioInstalacion.getUsuario()).isEqualTo(usuario);
		assertThat(usuarioInstalacion.getUsuario().getId()).isEqualTo(2L);
	}

	@Test
	void constructorConId() {
		UsuarioInstalacion usuarioInstalacion = new UsuarioInstalacion(15L);
		assertThat(usuarioInstalacion.getId()).isEqualTo(15L);
	}
}