package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.UsuarioEmpresaFilter;

class UsuarioEmpresaUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/usuario-empresa";
		String resultado = UsuarioEmpresaUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/usuario-empresa?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/usuario-empresa?page=0";
		String resultado = UsuarioEmpresaUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/usuario-empresa?page=0");
	}

	@Test
	void cleanUrlPageFilterConTodosLosCampos() {
		UsuarioEmpresaFilter filter = new UsuarioEmpresaFilter();
		filter.setEmpresaId(1L);
		filter.setUsuarioId(2L);

		String url = "/usuario-empresa";
		String resultado = UsuarioEmpresaUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/usuario-empresa?&empresaId=1&usuarioId=2");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		UsuarioEmpresaFilter filter = new UsuarioEmpresaFilter();
		filter.setEmpresaId(10L);

		String url = "/usuario-empresa?page=1";
		String resultado = UsuarioEmpresaUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/usuario-empresa?page=1&empresaId=10");
	}
}