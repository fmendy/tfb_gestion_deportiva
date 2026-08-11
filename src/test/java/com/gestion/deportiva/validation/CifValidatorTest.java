package com.gestion.deportiva.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CifValidatorTest {

	private CifValidator validator;
	private ConstraintValidatorContext context;

	@BeforeEach
	void setUp() {
		validator = new CifValidator();
		context = mock(ConstraintValidatorContext.class);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { " ", "   " })
	@DisplayName("Debe retornar true si el CIF es nulo, vacío o en blanco (validación opcional por defecto)")
	void shouldReturnTrueWhenCifIsNullOrEmpty(String input) {
		boolean isValid = validator.isValid(input, context);
		assertTrue(isValid);
	}

	@ParameterizedTest
	@ValueSource(strings = { "A12345678", "B87654321", "C1111111A", "P1234567J", "W12345673" })
	@DisplayName("Debe retornar true para formatos de CIF válidos")
	void shouldReturnTrueForValidCif(String cif) {
		boolean isValid = validator.isValid(cif, context);
		assertTrue(isValid);
	}

	@ParameterizedTest
	@ValueSource(strings = { "Z12345678", "A1234567", "A123456789", "12345678A", "A1234567K" })
	@DisplayName("Debe retornar false para formatos de CIF inválidos")
	void shouldReturnFalseForInvalidCif(String cif) {
		boolean isValid = validator.isValid(cif, context);
		assertFalse(isValid);
	}
}