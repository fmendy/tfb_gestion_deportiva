package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

class ReservaTest {

	@Test
	void gettersYSetters() {
		Reserva reserva = new Reserva();
		Instalacion instalacion = new Instalacion(1L);
		ReservaEstado reservaEstado = new ReservaEstado(2L);
		LocalDate fecha = LocalDate.of(2026, 8, 5);
		LocalTime horaInicio = LocalTime.of(10, 0);
		LocalTime horaFin = LocalTime.of(11, 0);

		reserva.setInstalacion(instalacion);
		reserva.setReservaEstado(reservaEstado);
		reserva.setFecha(fecha);
		reserva.setHoraInicio(horaInicio);
		reserva.setHoraFin(horaFin);

		assertThat(reserva.getInstalacion()).isEqualTo(instalacion);
		assertThat(reserva.getInstalacion().getId()).isEqualTo(1L);
		assertThat(reserva.getReservaEstado()).isEqualTo(reservaEstado);
		assertThat(reserva.getReservaEstado().getId()).isEqualTo(2L);
		assertThat(reserva.getFecha()).isEqualTo(fecha);
		assertThat(reserva.getHoraInicio()).isEqualTo(horaInicio);
		assertThat(reserva.getHoraFin()).isEqualTo(horaFin);
	}

	@Test
	void constructorConId() {
		Reserva reserva = new Reserva(15L);

		assertThat(reserva.getId()).isEqualTo(15L);
	}
}