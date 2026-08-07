package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.ReservaEstadoFilter;

class ReservaEstadoUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/estados-reserva";
		String resultado = ReservaEstadoUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/estados-reserva?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/estados-reserva?page=0";
		String resultado = ReservaEstadoUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/estados-reserva?page=0");
	}

	@Test
	void cleanUrlPageFilterConNombre() {
		ReservaEstadoFilter filter = new ReservaEstadoFilter();
		filter.setNombre("Confirmada");

		String url = "/estados-reserva";
		String resultado = ReservaEstadoUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/estados-reserva?&nombre=Confirmada");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYNombre() {
		ReservaEstadoFilter filter = new ReservaEstadoFilter();
		filter.setNombre("Pendiente");

		String url = "/estados-reserva?page=1";
		String resultado = ReservaEstadoUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/estados-reserva?page=1&nombre=Pendiente");
	}
}