package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MunicipioTest {

	@Test
	void gettersYSetters() {
		Municipio municipio = new Municipio();
		Provincia provincia = new Provincia(1L);

		municipio.setProvincia(provincia);
		municipio.setCodigoIne(28079L);
		municipio.setDc(6L);

		assertThat(municipio.getProvincia()).isEqualTo(provincia);
		assertThat(municipio.getProvincia().getId()).isEqualTo(1L);
		assertThat(municipio.getCodigoIne()).isEqualTo(28079L);
		assertThat(municipio.getDc()).isEqualTo(6L);
	}

	@Test
	void debeProbarConstructorConId() {
		Municipio municipio = new Municipio(12L);
		assertThat(municipio.getId()).isEqualTo(12L);
	}
}