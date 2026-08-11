package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.SancionDTO;
import com.gestion.deportiva.model.Sancion;
import com.gestion.deportiva.repository.SancionRepository;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class SancionValidatorTest {

	@Mock
	private SancionRepository repository;

	@InjectMocks
	private SancionValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private SancionDTO sancionDTO;
	private Sancion sancionModel;

	@BeforeEach
	void setUp() {
		sancionDTO = new SancionDTO();
		sancionDTO.setReservaId(1L);
		sancionDTO.setFechaInicio(LocalDate.of(2026, 8, 15));
		sancionDTO.setFechaFin(LocalDate.of(2026, 8, 20));

		sancionModel = new Sancion();
		sancionModel.setId(2L);
	}

	@Test
	void shouldReturnTrueWhenSanctionDoesNotExistForReservaAndDatesAreValid() {
		when(repository.findByActivoTrueAndReservaId(eq(1L))).thenReturn(null);

		boolean resultado = validator.isValid(sancionDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenSanctionAlreadyExistsForReserva() {
		when(repository.findByActivoTrueAndReservaId(eq(1L))).thenReturn(sancionModel);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(sancionDTO, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnFalseWhenFechaInicioIsAfterFechaFin() {
		sancionDTO.setFechaInicio(LocalDate.of(2026, 8, 25));
		sancionDTO.setFechaFin(LocalDate.of(2026, 8, 20));

		when(repository.findByActivoTrueAndReservaId(eq(1L))).thenReturn(null);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(sancionDTO, context);

		assertFalse(resultado);
	}
}