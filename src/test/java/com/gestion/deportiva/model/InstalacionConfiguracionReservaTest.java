package com.gestion.deportiva.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InstalacionConfiguracionReservaTest {

	@Test
	void gettersYSetters() {
		InstalacionConfiguracionReserva configuracion = new InstalacionConfiguracionReserva();
		Instalacion instalacion = new Instalacion(1L);

		configuracion.setInstalacion(instalacion);
		configuracion.setDuracionMin(30L);
		configuracion.setDuracionMax(120L);
		configuracion.setIntervaloHorario(15L);

		assertThat(configuracion.getInstalacion()).isEqualTo(instalacion);
		assertThat(configuracion.getInstalacion().getId()).isEqualTo(1L);
		assertThat(configuracion.getDuracionMin()).isEqualTo(30L);
		assertThat(configuracion.getDuracionMax()).isEqualTo(120L);
		assertThat(configuracion.getIntervaloHorario()).isEqualTo(15L);
	}

	@Test
	void constructorConId() {
		InstalacionConfiguracionReserva configuracion = new InstalacionConfiguracionReserva(5L);
		assertThat(configuracion.getId()).isEqualTo(5L);
	}
}