package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.gestion.deportiva.dto.filter.SedeFilter;

class SedeUtilTest {

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
		String url = "/sedes";
		String resultado = SedeUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/sedes?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/sedes?page=0";
		String resultado = SedeUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/sedes?page=0");
	}

	@Test
	void cleanUrlPageFilterConFiltroCompleto() {
		SedeFilter filter = new SedeFilter();
		filter.setNombre("Sede Central");
		filter.setEmpresaId(1L);
		filter.setMunicipioId(28079L);

		String url = "/sedes";
		String resultado = SedeUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).contains("/sedes?");
		assertThat(resultado).contains("nombre=Sede Central");
		assertThat(resultado).contains("empresaId=1");
		assertThat(resultado).contains("municipioId=28079");
	}
}