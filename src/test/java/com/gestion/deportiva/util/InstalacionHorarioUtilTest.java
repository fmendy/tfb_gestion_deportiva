package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.InstalacionHorarioFilter;

class InstalacionHorarioUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/horarios";
		String resultado = InstalacionHorarioUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/horarios?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/horarios?page=0";
		String resultado = InstalacionHorarioUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/horarios?page=0");
	}

	@Test
	void cleanUrlPageFilterConTodosLosCampos() {
		InstalacionHorarioFilter filter = new InstalacionHorarioFilter();
		filter.setEmpresaId(1L);
		filter.setSedeId(2L);
		filter.setInstalacionId(3L);
		filter.setDiaSemana(1L);

		String url = "/horarios";
		String resultado = InstalacionHorarioUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/horarios?&empresaId=1&sedeId=2&instalacionId=3&diaSemana=1");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		InstalacionHorarioFilter filter = new InstalacionHorarioFilter();
		filter.setDiaSemana(5L);

		String url = "/horarios?page=1";
		String resultado = InstalacionHorarioUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/horarios?page=1&diaSemana=5");
	}
}