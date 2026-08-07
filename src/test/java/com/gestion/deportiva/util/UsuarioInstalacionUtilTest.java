package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.UsuarioInstalacionFilter;

class UsuarioInstalacionUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/usuario-instalacion";
		String resultado = UsuarioInstalacionUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/usuario-instalacion?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/usuario-instalacion?page=0";
		String resultado = UsuarioInstalacionUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/usuario-instalacion?page=0");
	}

	@Test
	void cleanUrlPageFilterConTodosLosCampos() {
		UsuarioInstalacionFilter filter = new UsuarioInstalacionFilter();
		filter.setEmpresaId(1L);
		filter.setSedeId(2L);
		filter.setInstalacionId(3L);
		filter.setUsuarioId(4L);

		String url = "/usuario-instalacion";
		String resultado = UsuarioInstalacionUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/usuario-instalacion?&empresaId=1&sedeId=2&instalacionId=3&usuarioId=4");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		UsuarioInstalacionFilter filter = new UsuarioInstalacionFilter();
		filter.setUsuarioId(5L);

		String url = "/usuario-instalacion?page=1";
		String resultado = UsuarioInstalacionUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/usuario-instalacion?page=1&usuarioId=5");
	}
}