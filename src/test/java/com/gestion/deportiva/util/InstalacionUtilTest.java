package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.InstalacionFilter;
import com.gestion.deportiva.dto.filter.InstalacionPublicoFilter;

class InstalacionUtilTest {

	@Test
	void cleanUrlPageFilterInstalacionSinFiltroNiParametros() {
		String url = "/instalaciones";
		String resultado = InstalacionUtil.cleanUrlPageFilter((InstalacionFilter) null, url);
		assertThat(resultado).isEqualTo("/instalaciones?");
	}

	@Test
	void cleanUrlPageFilterInstalacionConUrlExistenteYFiltroNulo() {
		String url = "/instalaciones?page=0";
		String resultado = InstalacionUtil.cleanUrlPageFilter((InstalacionFilter) null, url);
		assertThat(resultado).isEqualTo("/instalaciones?page=0");
	}

	@Test
	void cleanUrlPageFilterInstalacionConTodosLosCampos() {
		InstalacionFilter filter = new InstalacionFilter();
		filter.setNombre("Pista Central");
		filter.setEmpresaId(1L);
		filter.setSedeId(2L);
		filter.setInstalacionTipoId(3L);

		String url = "/instalaciones";
		String resultado = InstalacionUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado)
				.isEqualTo("/instalaciones?&nombre=Pista Central&empresaId=1&sedeId=2&instalacionTipoId=3");
	}

	@Test
	void cleanUrlPageFilterInstalacionPublicoSinFiltroNiParametros() {
		String url = "/publico/instalaciones";
		String resultado = InstalacionUtil.cleanUrlPageFilter((InstalacionPublicoFilter) null, url);
		assertThat(resultado).isEqualTo("/publico/instalaciones?");
	}

	@Test
	void cleanUrlPageFilterInstalacionPublicoConTodosLosCampos() {
		InstalacionPublicoFilter filter = new InstalacionPublicoFilter();
		filter.setNombre("Piscina");
		filter.setMunicipioId(10L);
		filter.setFecha(LocalDate.of(2026, java.time.Month.AUGUST, 7));
		filter.setHoraInicio(LocalTime.of(10, 0));

		String url = "/publico/instalaciones?page=1";
		String resultado = InstalacionUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo(
				"/publico/instalaciones?page=1&nombre=Piscina&municipioId=10&fecha=2026-08-07&horaInicio=10:00");
	}
}