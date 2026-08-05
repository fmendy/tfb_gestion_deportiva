package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class RolTest {

	@Test
	void gettersYSetters() {
		Rol rol = new Rol();
		List<RolPermiso> listRolPermiso = new ArrayList<>();
		RolPermiso rolPermiso = new RolPermiso();
		listRolPermiso.add(rolPermiso);

		rol.setNombre("ADMINISTRADOR");
		rol.setListRolPermiso(listRolPermiso);

		assertThat(rol.getNombre()).isEqualTo("ADMINISTRADOR");
		assertThat(rol.getListRolPermiso()).hasSize(1);
		assertThat(rol.getListRolPermiso()).contains(rolPermiso);
	}

	@Test
	void constructorConId() {
		Rol rol = new Rol(5L);

		assertThat(rol.getId()).isEqualTo(5L);
	}
}