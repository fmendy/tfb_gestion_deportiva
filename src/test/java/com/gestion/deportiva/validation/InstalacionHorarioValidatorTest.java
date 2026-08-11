package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.InstalacionHorarioDTO;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class InstalacionHorarioValidatorTest {

	@InjectMocks
	private InstalacionHorarioValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private InstalacionHorarioDTO dto;

	@BeforeEach
	void setUp() {
		dto = new InstalacionHorarioDTO();
	}

	@Test
	void shouldReturnTrueWhenHoraInicioIsNull() {
		dto.setHoraInicio(null);
		dto.setHoraFin(LocalTime.of(10, 0));

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenHoraFinIsNull() {
		dto.setHoraInicio(LocalTime.of(8, 0));
		dto.setHoraFin(null);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenBothAreNull() {
		dto.setHoraInicio(null);
		dto.setHoraFin(null);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenHoraInicioIsBeforeHoraFin() {
		dto.setHoraInicio(LocalTime.of(8, 0));
		dto.setHoraFin(LocalTime.of(10, 0));

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenHoraInicioEqualsHoraFin() {
		dto.setHoraInicio(LocalTime.of(9, 0));
		dto.setHoraFin(LocalTime.of(9, 0));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnFalseWhenHoraInicioIsAfterHoraFin() {
		dto.setHoraInicio(LocalTime.of(12, 0));
		dto.setHoraFin(LocalTime.of(10, 0));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}
}