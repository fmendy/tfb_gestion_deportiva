package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.UsuarioEmpresaFilter;

class UsuarioTokenUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/usuario-token";
		String resultado = UsuarioTokenUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/usuario-token?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/usuario-token?page=0";
		String resultado = UsuarioTokenUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/usuario-token?page=0");
	}

	@Test
	void cleanUrlPageFilterConUsuarioId() {
		UsuarioEmpresaFilter filter = new UsuarioEmpresaFilter();
		filter.setUsuarioId(5L);

		String url = "/usuario-token";
		String resultado = UsuarioTokenUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/usuario-token?&usuarioId=5");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYUsuarioId() {
		UsuarioEmpresaFilter filter = new UsuarioEmpresaFilter();
		filter.setUsuarioId(12L);

		String url = "/usuario-token?page=1";
		String resultado = UsuarioTokenUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/usuario-token?page=1&usuarioId=12");
	}
}