package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InstalacionTipoTest {

	@Test
	void gettersYSetters() {
		InstalacionTipo instalacionTipo = new InstalacionTipo();
		instalacionTipo.setDescripcion("Tipo de instalación polideportivo");

		assertThat(instalacionTipo.getDescripcion()).isEqualTo("Tipo de instalación polideportivo");
	}

	@Test
	void constructorConId() {
		InstalacionTipo instalacionTipo = new InstalacionTipo(4L);

		assertThat(instalacionTipo.getId()).isEqualTo(4L);
	}
}