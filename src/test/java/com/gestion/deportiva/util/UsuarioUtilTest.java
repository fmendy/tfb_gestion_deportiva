package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.UsuarioFilter;

class UsuarioUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/usuarios";
		String resultado = UsuarioUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/usuarios?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/usuarios?page=0";
		String resultado = UsuarioUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/usuarios?page=0");
	}

	@Test
	void cleanUrlPageFilterConTodosLosCampos() {
		UsuarioFilter filter = new UsuarioFilter();
		filter.setNombre("Juan");
		filter.setEmail("juan@example.com");

		String url = "/usuarios";
		String resultado = UsuarioUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/usuarios?&nombre=Juan&email=juan@example.com");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		UsuarioFilter filter = new UsuarioFilter();
		filter.setEmail("maria@example.com");

		String url = "/usuarios?page=1";
		String resultado = UsuarioUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/usuarios?page=1&email=maria@example.com");
	}
}