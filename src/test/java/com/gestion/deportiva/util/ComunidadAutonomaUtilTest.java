package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.ComunidadAutonomaFilter;

class ComunidadAutonomaUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/comunidades";
		String resultado = ComunidadAutonomaUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/comunidades?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/comunidades?page=0";
		String resultado = ComunidadAutonomaUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/comunidades?page=0");
	}

	@Test
	void cleanUrlPageFilterConNombreYCodigoIne() {
		ComunidadAutonomaFilter filter = new ComunidadAutonomaFilter();
		filter.setNombre("Madrid");
		filter.setCodigoIne(28L);

		String url = "/comunidades";
		String resultado = ComunidadAutonomaUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/comunidades?&nombre=Madrid&codigoIne=28");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroCompleto() {
		ComunidadAutonomaFilter filter = new ComunidadAutonomaFilter();
		filter.setNombre("Andalucia");
		filter.setCodigoIne(1L);

		String url = "/comunidades?page=1";
		String resultado = ComunidadAutonomaUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/comunidades?page=1&nombre=Andalucia&codigoIne=1");
	}
}