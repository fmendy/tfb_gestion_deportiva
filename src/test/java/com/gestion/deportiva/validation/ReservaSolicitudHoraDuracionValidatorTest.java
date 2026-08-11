package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.service.InstalacionConfiguracionReservaService;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class ReservaSolicitudHoraDuracionValidatorTest {

	@Mock
	private InstalacionConfiguracionReservaService instalacionConfiguracionReservaService;

	@InjectMocks
	private ReservaSolicitudHoraDuracionValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private ReservaSolicitudDTO dto;

	@BeforeEach
	void setUp() {
		dto = new ReservaSolicitudDTO();
		dto.setInstalacionId(1L);
		dto.setHora(LocalTime.of(10, 0));
		dto.setDuracion(60L);
	}

	@Test
	void shouldReturnTrueWhenHoraAndDuracionAreValid() {
		when(instalacionConfiguracionReservaService.isValid(eq(1L), eq(LocalTime.of(10, 0)), eq(60L))).thenReturn(true);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenHoraAndDuracionAreInvalid() {
		when(instalacionConfiguracionReservaService.isValid(eq(1L), eq(LocalTime.of(10, 0)), eq(60L)))
				.thenReturn(false);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}
}