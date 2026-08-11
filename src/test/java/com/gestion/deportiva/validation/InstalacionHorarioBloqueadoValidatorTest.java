package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.InstalacionHorarioBloqueadoDTO;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class InstalacionHorarioBloqueadoValidatorTest {

	@InjectMocks
	private InstalacionHorarioBloqueadoValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private InstalacionHorarioBloqueadoDTO dto;

	@BeforeEach
	void setUp() {
		dto = new InstalacionHorarioBloqueadoDTO();
	}

	@Test
	void shouldReturnTrueWhenHoraInicioIsBeforeHoraFin() {
		dto.setHoraInicio(LocalTime.of(8, 0));
		dto.setHoraFin(LocalTime.of(10, 0));

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenHoraInicioEqualsHoraFin() {
		dto.setHoraInicio(LocalTime.of(9, 0));
		dto.setHoraFin(LocalTime.of(9, 0));

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenHoraInicioIsAfterHoraFin() {
		dto.setHoraInicio(LocalTime.of(12, 0));
		dto.setHoraFin(LocalTime.of(10, 0));

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}
}