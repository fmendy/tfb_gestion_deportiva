package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.InstalacionHorarioBloqueadoFilter;

class InstalacionHorarioBloqueadoUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/bloqueos";
		String resultado = InstalacionHorarioBloqueadoUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/bloqueos?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/bloqueos?page=0";
		String resultado = InstalacionHorarioBloqueadoUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/bloqueos?page=0");
	}

	@Test
	void cleanUrlPageFilterConTodosLosCampos() {
		InstalacionHorarioBloqueadoFilter filter = new InstalacionHorarioBloqueadoFilter();
		filter.setEmpresaId(1L);
		filter.setSedeId(2L);
		filter.setInstalacionId(3L);
		filter.setFechaDesde(LocalDate.of(2026, 8, 1));
		filter.setFechaHasta(LocalDate.of(2026, 8, 31));

		String url = "/bloqueos";
		String resultado = InstalacionHorarioBloqueadoUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo(
				"/bloqueos?&empresaId=1&sedeId=2&instalacionId=3&fechaDesde=2026-08-01&fechaHasta=2026-08-31");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		InstalacionHorarioBloqueadoFilter filter = new InstalacionHorarioBloqueadoFilter();
		filter.setFechaDesde(LocalDate.of(2026, 8, 1));

		String url = "/bloqueos?page=1";
		String resultado = InstalacionHorarioBloqueadoUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/bloqueos?page=1&fechaDesde=2026-08-01");
	}
}