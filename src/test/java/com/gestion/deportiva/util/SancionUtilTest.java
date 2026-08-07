package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.gestion.deportiva.dto.filter.SancionFilter;

class SancionUtilTest {

	private MockedStatic<Utils> utilsMockedStatic;

	@BeforeEach
	void setUp() {
		utilsMockedStatic = mockStatic(Utils.class);
		utilsMockedStatic.when(() -> Utils.appendParam(any(StringBuilder.class), any(String.class), any()))
				.thenAnswer(invocation -> {
					StringBuilder sb = invocation.getArgument(0);
					String name = invocation.getArgument(1);
					Object val = invocation.getArgument(2);
					if (val != null) {
						sb.append("&").append(name).append("=").append(val);
					}
					return null;
				});
	}

	@AfterEach
	void tearDown() {
		utilsMockedStatic.close();
	}

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/sanciones";
		String resultado = SancionUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/sanciones?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/sanciones?page=0";
		String resultado = SancionUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/sanciones?page=0");
	}

	@Test
	void cleanUrlPageFilterConFiltroCompleto() {
		SancionFilter filter = new SancionFilter();
		filter.setReservaId(10L);
		filter.setUsuarioId(20L);
		filter.setSancionTipoId(2L);
		filter.setUsuarioNombre("Pedro");
		filter.setFechaInicioDesde(LocalDate.of(2026, 8, 1));
		filter.setFechaInicioHasta(LocalDate.of(2026, 8, 31));

		String url = "/sanciones";
		String resultado = SancionUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).contains("/sanciones?");
		assertThat(resultado).contains("reservaId=10");
		assertThat(resultado).contains("usuarioId=20");
		assertThat(resultado).contains("sancionTipoId=2");
		assertThat(resultado).contains("usuarioNombre=Pedro");
		assertThat(resultado).contains("fechaInicioDesde=2026-08-01");
		assertThat(resultado).contains("fechaInicioHasta=2026-08-31");
	}
}