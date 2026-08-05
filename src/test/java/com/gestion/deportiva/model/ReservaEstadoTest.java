package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReservaEstadoTest {

	@Test
	void constructorConId() {
		ReservaEstado reservaEstado = new ReservaEstado(5L);

		assertThat(reservaEstado.getId()).isEqualTo(5L);
	}

	@Test
	void heredadoDeMaestra() {
		ReservaEstado reservaEstado = new ReservaEstado();
		reservaEstado.setNombre("CONFIRMADA");

		assertThat(reservaEstado.getNombre()).isEqualTo("CONFIRMADA");
	}
}