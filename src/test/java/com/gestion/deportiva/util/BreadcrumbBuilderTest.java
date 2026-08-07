package com.gestion.deportiva.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.gestion.deportiva.dto.BreadcrumbDTO;

class BreadcrumbBuilderTest {

	private MockedStatic<Utils> utilsMockedStatic;

	@BeforeEach
	void setUp() {
		utilsMockedStatic = mockStatic(Utils.class);
		utilsMockedStatic.when(() -> Utils.getMessage(anyString())).thenAnswer(inv -> inv.getArgument(0));
		utilsMockedStatic.when(() -> Utils.getMessage(anyString())).thenAnswer(inv -> inv.getArgument(0));
	}

	@AfterEach
	void tearDown() {
		utilsMockedStatic.close();
	}

	@Test
	void crearBreadcrumbSimple() {
		List<BreadcrumbDTO> result = BreadcrumbBuilder.start().add("Panel", "/panel").build();

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getLabel()).isEqualTo("Panel");
		assertThat(result.get(0).getUrl()).isEqualTo("/panel");
	}

	@Test
	void crearBreadcrumbConTraduccion() {
		List<BreadcrumbDTO> result = BreadcrumbBuilder.start().add("breadcrumb.usuarios", "/usuarios").build();

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getLabel()).isEqualTo("breadcrumb.usuarios");
		assertThat(result.get(0).getUrl()).isEqualTo("/usuarios");
	}

	@Test
	void incluirHomeAlInicio() {
		List<BreadcrumbDTO> result = BreadcrumbBuilder.start().includeHome().add("Detalle", null).build();

		assertThat(result).hasSize(2);
		assertThat(result.get(0).getLabel()).isEqualTo("breadcrumb.inicio");
		assertThat(result.get(0).getUrl()).isEqualTo("/");
		assertThat(result.get(1).getLabel()).isEqualTo("Detalle");
		assertThat(result.get(1).getUrl()).isNull();
	}
}