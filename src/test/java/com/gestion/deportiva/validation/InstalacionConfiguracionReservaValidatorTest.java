package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.InstalacionConfiguracionReservaDTO;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class InstalacionConfiguracionReservaValidatorTest {

	@InjectMocks
	private InstalacionConfiguracionReservaValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private InstalacionConfiguracionReservaDTO dto;

	@BeforeEach
	void setUp() {
		dto = new InstalacionConfiguracionReservaDTO();
	}

	@Test
	void shouldReturnTrueWhenDuracionMinIsNull() {
		dto.setDuracionMin(null);
		dto.setDuracionMax(60L);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenDuracionMaxIsNull() {
		dto.setDuracionMin(30L);
		dto.setDuracionMax(null);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenBothAreNull() {
		dto.setDuracionMin(null);
		dto.setDuracionMax(null);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenDuracionMinIsLessThanOrEqualDuracionMax() {
		dto.setDuracionMin(30L);
		dto.setDuracionMax(60L);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenDuracionMinIsGreaterThanDuracionMax() {
		dto.setDuracionMin(90L);
		dto.setDuracionMax(60L);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}
}