package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.gestion.deportiva.dto.filter.EmpleadoFilter;

class EmpleadoUtilTest {

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
		String url = "/empleados";
		String resultado = EmpleadoUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/empleados?");
	}

	@Test
	void cleanUrlPageFilterConUrlExistenteYFiltroNulo() {
		String url = "/empleados?page=0";
		String resultado = EmpleadoUtil.cleanUrlPageFilter(null, url);
		assertThat(resultado).isEqualTo("/empleados?page=0");
	}

	@Test
	void cleanUrlPageFilterConFiltroCompleto() {
		EmpleadoFilter filter = new EmpleadoFilter();
		filter.setEmail("test@test.com");
		filter.setNombre("Juan");
		filter.setEmpresaId(1L);

		String url = "/empleados";
		String resultado = EmpleadoUtil.cleanUrlPageFilter(filter, url);

		assertThat(resultado).contains("/empleados?");
		assertThat(resultado).contains("email=test@test.com");
		assertThat(resultado).contains("nombre=Juan");
		assertThat(resultado).contains("empresaId=1");
	}
}