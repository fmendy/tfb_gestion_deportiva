package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UsuarioTokenTest {

	@Test
	void gettersYSetters() {
		UsuarioToken usuarioToken = new UsuarioToken();
		Usuario usuario = new Usuario(1L);

		usuarioToken.setUsuario(usuario);

		assertThat(usuarioToken.getUsuario()).isEqualTo(usuario);
		assertThat(usuarioToken.getUsuario().getId()).isEqualTo(1L);
	}

	@Test
	void constructorConId() {
		UsuarioToken usuarioToken = new UsuarioToken(8L);

		assertThat(usuarioToken.getId()).isEqualTo(8L);
	}
}