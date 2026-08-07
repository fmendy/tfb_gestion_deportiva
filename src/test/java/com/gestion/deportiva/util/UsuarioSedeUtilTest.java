package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.UsuarioSedeFilter;

class UsuarioSedeUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/usuario-sede";
		String resultado = UsuarioSedeUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/usuario-sede?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/usuario-sede?page=0";
		String resultado = UsuarioSedeUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/usuario-sede?page=0");
	}

	@Test
	void cleanUrlPageFilterConTodosLosCampos() {
		UsuarioSedeFilter filter = new UsuarioSedeFilter();
		filter.setEmpresaId(1L);
		filter.setSedeId(2L);
		filter.setUsuarioId(3L);

		String url = "/usuario-sede";
		String resultado = UsuarioSedeUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/usuario-sede?&empresaId=1&sedeId=2&usuarioId=3");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		UsuarioSedeFilter filter = new UsuarioSedeFilter();
		filter.setSedeId(5L);

		String url = "/usuario-sede?page=1";
		String resultado = UsuarioSedeUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/usuario-sede?page=1&sedeId=5");
	}
}