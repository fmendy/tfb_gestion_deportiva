package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.SancionTipoFilter;

class SancionTipoUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/tipos-sancion";
		String resultado = SancionTipoUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/tipos-sancion?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/tipos-sancion?page=0";
		String resultado = SancionTipoUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/tipos-sancion?page=0");
	}

	@Test
	void cleanUrlPageFilterConNombre() {
		SancionTipoFilter filter = new SancionTipoFilter();
		filter.setNombre("Leve");

		String url = "/tipos-sancion";
		String resultado = SancionTipoUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/tipos-sancion?&nombre=Leve");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYNombre() {
		SancionTipoFilter filter = new SancionTipoFilter();
		filter.setNombre("Grave");

		String url = "/tipos-sancion?page=1";
		String resultado = SancionTipoUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/tipos-sancion?page=1&nombre=Grave");
	}
}