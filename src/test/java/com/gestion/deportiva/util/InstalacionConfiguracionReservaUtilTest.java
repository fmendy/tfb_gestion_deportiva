package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.InstalacionConfiguracionReservaFilter;

class InstalacionConfiguracionReservaUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/configuraciones";
		String resultado = InstalacionConfiguracionReservaUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/configuraciones?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/configuraciones?page=0";
		String resultado = InstalacionConfiguracionReservaUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/configuraciones?page=0");
	}

	@Test
	void cleanUrlPageFilterConTodosLosCampos() {
		InstalacionConfiguracionReservaFilter filter = new InstalacionConfiguracionReservaFilter();
		filter.setEmpresaId(1L);
		filter.setSedeId(2L);
		filter.setInstalacionId(3L);

		String url = "/configuraciones";
		String resultado = InstalacionConfiguracionReservaUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/configuraciones?&empresaId=1&sedeId=2&instalacionId=3");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		InstalacionConfiguracionReservaFilter filter = new InstalacionConfiguracionReservaFilter();
		filter.setSedeId(5L);

		String url = "/configuraciones?page=1";
		String resultado = InstalacionConfiguracionReservaUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/configuraciones?page=1&sedeId=5");
	}
}