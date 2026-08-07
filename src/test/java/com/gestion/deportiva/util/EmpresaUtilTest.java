package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.gestion.deportiva.dto.filter.EmpresaFilter;

class EmpresaUtilTest {

	@Test
	void cleanUrlPageFilterSinFiltroNiParametros() {
		String url = "/empresas";
		String resultado = EmpresaUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/empresas?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/empresas?page=0";
		String resultado = EmpresaUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/empresas?page=0");
	}

	@Test
	void cleanUrlPageFilterConCifYNombre() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setCif("B12345678");
		filter.setNombre("Deportes S.L.");

		String url = "/empresas";
		String resultado = EmpresaUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/empresas?&cif=B12345678&nombre=Deportes S.L.");
	}

	@Test
	void cleanUrlPageFilterConUrlConParametrosYFiltroParcial() {
		EmpresaFilter filter = new EmpresaFilter();
		filter.setCif("A87654321");

		String url = "/empresas?page=1";
		String resultado = EmpresaUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).isEqualTo("/empresas?page=1&cif=A87654321");
	}
}