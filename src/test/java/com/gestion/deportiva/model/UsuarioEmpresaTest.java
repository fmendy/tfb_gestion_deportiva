package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UsuarioEmpresaTest {

	@Test
	void gettersYSetters() {
		UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa();
		Empresa empresa = new Empresa(1L);
		Usuario usuario = new Usuario(2L);

		usuarioEmpresa.setEmpresa(empresa);
		usuarioEmpresa.setUsuario(usuario);

		assertThat(usuarioEmpresa.getEmpresa()).isEqualTo(empresa);
		assertThat(usuarioEmpresa.getEmpresa().getId()).isEqualTo(1L);
		assertThat(usuarioEmpresa.getUsuario()).isEqualTo(usuario);
		assertThat(usuarioEmpresa.getUsuario().getId()).isEqualTo(2L);
	}

	@Test
	void constructorConId() {
		UsuarioEmpresa usuarioEmpresa = new UsuarioEmpresa(10L);

		assertThat(usuarioEmpresa.getId()).isEqualTo(10L);
	}
}