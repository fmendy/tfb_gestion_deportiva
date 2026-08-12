package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.InstalacionHorarioEspecialDTO;
import com.gestion.deportiva.model.InstalacionHorarioEspecial;
import com.gestion.deportiva.repository.InstalacionHorarioEspecialRepository;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class InstalacionHorarioEspecialValidatorTest {

	@Mock
	private InstalacionHorarioEspecialRepository repository;

	@InjectMocks
	private InstalacionHorarioEspecialValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private InstalacionHorarioEspecialDTO dto;
	private InstalacionHorarioEspecial horarioExistente;
	private LocalDate fechaPrueba;

	@BeforeEach
	void setUp() {
		fechaPrueba = LocalDate.of(2026, java.time.Month.AUGUST, 15);
		dto = new InstalacionHorarioEspecialDTO();
		dto.setFecha(fechaPrueba);

		horarioExistente = new InstalacionHorarioEspecial();
		horarioExistente.setFecha(fechaPrueba);
	}

	@Test
	void shouldReturnTrueWhenValidOpenSchedule() {
		dto.setCerrado(false);
		dto.setHoraInicio(LocalTime.of(9, 0));
		dto.setHoraFin(LocalTime.of(18, 0));

		when(repository.findByActivoTrueAndCerradoTrueAndFecha(eq(fechaPrueba))).thenReturn(null);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenValidClosedSchedule() {
		dto.setCerrado(true);
		dto.setHoraInicio(null);
		dto.setHoraFin(null);

		when(repository.findByActivoTrueAndCerradoTrueAndFecha(eq(fechaPrueba))).thenReturn(null);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenNotClosedAndBothTimesAreNull() {
		dto.setCerrado(false);
		dto.setHoraInicio(null);
		dto.setHoraFin(null);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnFalseWhenClosedAndHasStartTime() {
		dto.setCerrado(true);
		dto.setHoraInicio(LocalTime.of(9, 0));
		dto.setHoraFin(null);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnFalseWhenClosedAndHasEndTime() {
		dto.setCerrado(true);
		dto.setHoraInicio(null);
		dto.setHoraFin(LocalTime.of(18, 0));

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnFalseWhenNotClosedAndHoraInicioIsAfterHoraFin() {
		dto.setCerrado(false);
		dto.setHoraInicio(LocalTime.of(18, 0));
		dto.setHoraFin(LocalTime.of(9, 0));

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnFalseWhenNotClosedAndExistingClosedSchedule() {
		dto.setCerrado(false);
		dto.setHoraInicio(LocalTime.of(9, 0));
		dto.setHoraFin(LocalTime.of(18, 0));

		when(repository.findByActivoTrueAndCerradoTrueAndFecha(eq(fechaPrueba))).thenReturn(horarioExistente);
		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnFalseWhenClosedAndExistingClosedSchedule() {
		dto.setCerrado(true);
		dto.setHoraInicio(null);
		dto.setHoraFin(null);

		when(repository.findByActivoTrueAndCerradoTrueAndFecha(eq(fechaPrueba))).thenReturn(horarioExistente);
		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}
}