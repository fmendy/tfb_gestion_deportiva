package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UsuarioTest {

	@Test
	void gettersYSetters() {
		Usuario usuario = new Usuario();
		String email = "test@gestion.com";
		String password = "encodedPassword";

		usuario.setEmail(email);
		usuario.setPassword(password);

		assertThat(usuario.getEmail()).isEqualTo(email);
		assertThat(usuario.getPassword()).isEqualTo(password);
	}

	@Test
	void constructorConId() {
		Usuario usuario = new Usuario(99L);

		assertThat(usuario.getId()).isEqualTo(99L);
	}

	@Test
	void filtrarListasPorActivo() {
		Usuario usuario = new Usuario();

		UsuarioRol activoRol = Mockito.mock(UsuarioRol.class);
		Mockito.when(activoRol.isActivo()).thenReturn(true);
		UsuarioRol inactivoRol = Mockito.mock(UsuarioRol.class);
		Mockito.when(inactivoRol.isActivo()).thenReturn(false);

		List<UsuarioRol> roles = new ArrayList<>();
		roles.add(activoRol);
		roles.add(inactivoRol);
		usuario.setListUsuarioRol(roles);

		UsuarioEmpresa activoEmpresa = Mockito.mock(UsuarioEmpresa.class);
		Mockito.when(activoEmpresa.isActivo()).thenReturn(true);
		List<UsuarioEmpresa> empresas = List.of(activoEmpresa);
		usuario.setListUsuarioEmpresa(empresas);

		UsuarioSede activoSede = Mockito.mock(UsuarioSede.class);
		Mockito.when(activoSede.isActivo()).thenReturn(true);
		List<UsuarioSede> sedes = List.of(activoSede);
		usuario.setListUsuarioSede(sedes);

		UsuarioInstalacion activoInstalacion = Mockito.mock(UsuarioInstalacion.class);
		Mockito.when(activoInstalacion.isActivo()).thenReturn(true);
		List<UsuarioInstalacion> instalaciones = List.of(activoInstalacion);
		usuario.setListUsuarioInstalacion(instalaciones);

		// Act & Assert
		assertThat(usuario.getListUsuarioRol()).hasSize(1);
		assertThat(usuario.getListUsuarioRol()).contains(activoRol);

		assertThat(usuario.getListUsuarioEmpresa()).hasSize(1);
		assertThat(usuario.getListUsuarioEmpresa()).contains(activoEmpresa);

		assertThat(usuario.getListUsuarioSede()).hasSize(1);
		assertThat(usuario.getListUsuarioSede()).contains(activoSede);

		assertThat(usuario.getListUsuarioInstalacion()).hasSize(1);
		assertThat(usuario.getListUsuarioInstalacion()).contains(activoInstalacion);
	}
}