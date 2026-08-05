package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PermisoTest {

	@Test
	void constructorConId() {
		Permiso permiso = new Permiso(8L);

		assertThat(permiso.getId()).isEqualTo(8L);
	}

	@Test
	void heredadoDeMaestra() {
		Permiso permiso = new Permiso();
		permiso.setNombre("PERMISO_ADMIN");
		assertThat(permiso.getNombre()).isEqualTo("PERMISO_ADMIN");
	}
}