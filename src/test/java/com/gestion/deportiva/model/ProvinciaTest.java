package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ProvinciaTest {

	@Test
	void gettersYSetters() {
		Provincia provincia = new Provincia();
		ComunidadAutonoma comunidadAutonoma = new ComunidadAutonoma(1L);
		List<Municipio> municipios = new ArrayList<>();
		Municipio municipio = new Municipio(10L);
		municipios.add(municipio);

		provincia.setComunidadAutonoma(comunidadAutonoma);
		provincia.setCodigoIne(28L);
		provincia.setListMunicipio(municipios);

		assertThat(provincia.getComunidadAutonoma()).isEqualTo(comunidadAutonoma);
		assertThat(provincia.getComunidadAutonoma().getId()).isEqualTo(1L);
		assertThat(provincia.getCodigoIne()).isEqualTo(28L);
		assertThat(provincia.getListMunicipio()).hasSize(1);
		assertThat(provincia.getListMunicipio().get(0).getId()).isEqualTo(10L);
	}

	@Test
	void constructorConId() {
		Provincia provincia = new Provincia(3L);
		assertThat(provincia.getId()).isEqualTo(3L);
	}
}