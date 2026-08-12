package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.InstalacionHorarioEspecialFilter;

class InstalacionHorarioEspecialUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/horarios-especiales";
		String resultado = InstalacionHorarioEspecialUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/horarios-especiales?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/horarios-especiales?page=0";
		String resultado = InstalacionHorarioEspecialUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/horarios-especiales?page=0");
	}

	@Test
	void cleanUrlPageFilterConTodosLosCampos() {
		InstalacionHorarioEspecialFilter filter = new InstalacionHorarioEspecialFilter();
		filter.setEmpresaId(1L);
		filter.setSedeId(2L);
		filter.setInstalacionId(3L);
		filter.setFechaDesde(LocalDate.of(2026, java.time.Month.AUGUST, 1));
		filter.setFechaHasta(LocalDate.of(2026, java.time.Month.AUGUST, 31));
		filter.setCerrado(true);

		String url = "/horarios-especiales";
		String resultado = InstalacionHorarioEspecialUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo(
				"/horarios-especiales?&empresaId=1&sedeId=2&instalacionId=3&fechaDesde=2026-08-01&fechaHasta=2026-08-31&cerrado=true");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		InstalacionHorarioEspecialFilter filter = new InstalacionHorarioEspecialFilter();
		filter.setCerrado(false);

		String url = "/horarios-especiales?page=1";
		String resultado = InstalacionHorarioEspecialUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/horarios-especiales?page=1&cerrado=false");
	}
}