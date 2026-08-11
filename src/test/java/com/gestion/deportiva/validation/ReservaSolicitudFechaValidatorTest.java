package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class ReservaSolicitudFechaValidatorTest {

	@InjectMocks
	private ReservaSolicitudFechaValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private ReservaSolicitudDTO dto;
	private LocalDate hoy;

	@BeforeEach
	void setUp() {
		hoy = LocalDate.now(ZoneId.of("Europe/Madrid"));
		dto = new ReservaSolicitudDTO();
	}

	@Test
	void shouldReturnTrueWhenFechaIsNull() {
		dto.setFecha(null);
		dto.setHora(LocalTime.of(12, 0));

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenHoraIsNull() {
		dto.setFecha(hoy.plusDays(1));
		dto.setHora(null);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenBothAreNull() {
		dto.setFecha(null);
		dto.setHora(null);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenFechaIsBeforeToday() {
		dto.setFecha(hoy.minusDays(1));
		dto.setHora(LocalTime.of(12, 0));

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnTrueWhenFechaIsAfterToday() {
		dto.setFecha(hoy.plusDays(1));
		dto.setHora(LocalTime.of(12, 0));

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenFechaIsTodayAndHoraIsBeforeNow() {
		LocalTime horaPasada = LocalTime.now(ZoneId.of("Europe/Madrid")).minusHours(1);
		dto.setFecha(hoy);
		dto.setHora(horaPasada);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnTrueWhenFechaIsTodayAndHoraIsAfterNow() {
		LocalTime horaFutura = LocalTime.now(ZoneId.of("Europe/Madrid")).plusHours(2);
		dto.setFecha(hoy);
		dto.setHora(horaFutura);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}
}