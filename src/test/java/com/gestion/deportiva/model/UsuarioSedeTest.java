package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UsuarioSedeTest {

	@Test
	void gettersYSetters() {
		UsuarioSede usuarioSede = new UsuarioSede();
		Sede sede = new Sede(1L);
		Usuario usuario = new Usuario(2L);

		usuarioSede.setSede(sede);
		usuarioSede.setUsuario(usuario);

		assertThat(usuarioSede.getSede()).isEqualTo(sede);
		assertThat(usuarioSede.getSede().getId()).isEqualTo(1L);
		assertThat(usuarioSede.getUsuario()).isEqualTo(usuario);
		assertThat(usuarioSede.getUsuario().getId()).isEqualTo(2L);
	}

	@Test
	void constructorConId() {
		UsuarioSede usuarioSede = new UsuarioSede(12L);

		assertThat(usuarioSede.getId()).isEqualTo(12L);
	}
}