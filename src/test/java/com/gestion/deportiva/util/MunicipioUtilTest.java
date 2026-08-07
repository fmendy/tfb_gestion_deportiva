package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.MunicipioFilter;

class MunicipioUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/municipios";
		String resultado = MunicipioUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/municipios?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/municipios?page=0";
		String resultado = MunicipioUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/municipios?page=0");
	}

	@Test
	void cleanUrlPageFilterConTodosLosCampos() {
		MunicipioFilter filter = new MunicipioFilter();
		filter.setNombre("Madrid");
		filter.setComunidadAutonomaUuid("uuid-ca-1");
		filter.setProvinciaUuid("uuid-prov-1");
		filter.setCodigoIne(28079L);
		filter.setDc(6L);

		String url = "/municipios";
		String resultado = MunicipioUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo(
				"/municipios?&nombre=Madrid&comunidadAutonomaUuid=uuid-ca-1&provinciaUuid=uuid-prov-1&codigoIne=28079&dc=6");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		MunicipioFilter filter = new MunicipioFilter();
		filter.setNombre("Alcalá");

		String url = "/municipios?page=1";
		String resultado = MunicipioUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/municipios?page=1&nombre=Alcalá");
	}
}