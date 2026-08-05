package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RolPermisoTest {

	@Test
	void gettersYSetters() {
		RolPermiso rolPermiso = new RolPermiso();
		Permiso permiso = new Permiso(1L);
		Rol rol = new Rol(2L);

		rolPermiso.setPermiso(permiso);
		rolPermiso.setRol(rol);

		assertThat(rolPermiso.getPermiso()).isEqualTo(permiso);
		assertThat(rolPermiso.getPermiso().getId()).isEqualTo(1L);
		assertThat(rolPermiso.getRol()).isEqualTo(rol);
		assertThat(rolPermiso.getRol().getId()).isEqualTo(2L);
	}

	@Test
	void constructorConPermisoYRol() {
		Permiso permiso = new Permiso(10L);
		Rol rol = new Rol(20L);

		RolPermiso rolPermiso = new RolPermiso(permiso, rol);

		assertThat(rolPermiso.getPermiso()).isEqualTo(permiso);
		assertThat(rolPermiso.getPermiso().getId()).isEqualTo(10L);
		assertThat(rolPermiso.getRol()).isEqualTo(rol);
		assertThat(rolPermiso.getRol().getId()).isEqualTo(20L);
	}
}