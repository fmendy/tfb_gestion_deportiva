package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class SancionTest {

	@Test
	void gettersYSetters() {
		Sancion sancion = new Sancion();
		Usuario usuario = new Usuario(1L);
		Reserva reserva = new Reserva(2L);
		LocalDate fechaInicio = LocalDate.of(2026, 8, 1);
		LocalDate fechaFin = LocalDate.of(2026, 8, 15);
		SancionTipo sancionTipo = new SancionTipo(3L);
		String descripcion = "Sanción por no presentarse a la reserva";

		sancion.setUsuario(usuario);
		sancion.setReserva(reserva);
		sancion.setFechaInicio(fechaInicio);
		sancion.setFechaFin(fechaFin);
		sancion.setSancionTipo(sancionTipo);
		sancion.setDescripcion(descripcion);

		assertThat(sancion.getUsuario()).isEqualTo(usuario);
		assertThat(sancion.getUsuario().getId()).isEqualTo(1L);
		assertThat(sancion.getReserva()).isEqualTo(reserva);
		assertThat(sancion.getReserva().getId()).isEqualTo(2L);
		assertThat(sancion.getFechaInicio()).isEqualTo(fechaInicio);
		assertThat(sancion.getFechaFin()).isEqualTo(fechaFin);
		assertThat(sancion.getSancionTipo()).isEqualTo(sancionTipo);
		assertThat(sancion.getSancionTipo().getId()).isEqualTo(3L);
		assertThat(sancion.getDescripcion()).isEqualTo(descripcion);
	}

	@Test
	void constructorConId() {
		Sancion sancion = new Sancion(10L);

		assertThat(sancion.getId()).isEqualTo(10L);
	}
}