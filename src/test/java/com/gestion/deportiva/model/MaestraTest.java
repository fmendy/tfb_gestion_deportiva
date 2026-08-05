package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaestraTest {

	private static class MaestraConcreta extends Maestra {
		public MaestraConcreta() {
			super();
		}

		public MaestraConcreta(Long id) {
			super(id);
		}
	}

	@Test
	void gettersYSetters() {
		Maestra maestra = new MaestraConcreta();
		maestra.setNombre("Nombre de prueba");
		assertThat(maestra.getNombre()).isEqualTo("Nombre de prueba");
	}

	@Test
	void constructorConId() {
		Maestra maestra = new MaestraConcreta(15L);

		assertThat(maestra.getId()).isEqualTo(15L);
	}
}