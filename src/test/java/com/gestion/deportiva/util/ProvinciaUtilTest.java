package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.ProvinciaFilter;

class ProvinciaUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/provincias";
		String resultado = ProvinciaUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/provincias?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/provincias?page=0";
		String resultado = ProvinciaUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/provincias?page=0");
	}

	@Test
	void cleanUrlPageFilterConTodosLosCampos() {
		ProvinciaFilter filter = new ProvinciaFilter();
		filter.setNombre("Madrid");
		filter.setComunidadAutonomaUuid("uuid-ca-1");
		filter.setCodigoIne(28L);

		String url = "/provincias";
		String resultado = ProvinciaUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/provincias?&nombre=Madrid&comunidadAutonomaUuid=uuid-ca-1&codigoIne=28");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		ProvinciaFilter filter = new ProvinciaFilter();
		filter.setNombre("Barcelona");

		String url = "/provincias?page=1";
		String resultado = ProvinciaUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/provincias?page=1&nombre=Barcelona");
	}
}