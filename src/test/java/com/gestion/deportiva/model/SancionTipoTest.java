package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SancionTipoTest {

	@Test
	void constructorConId() {
		SancionTipo sancionTipo = new SancionTipo(6L);

		assertThat(sancionTipo.getId()).isEqualTo(6L);
	}

	@Test
	void heredadoDeMaestra() {
		SancionTipo sancionTipo = new SancionTipo();
		sancionTipo.setNombre("LEVE");
		assertThat(sancionTipo.getNombre()).isEqualTo("LEVE");
	}
}