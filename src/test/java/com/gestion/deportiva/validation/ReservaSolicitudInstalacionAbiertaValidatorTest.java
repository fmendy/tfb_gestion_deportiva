package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.service.InstalacionHorarioBloqueadoService;
import com.gestion.deportiva.service.InstalacionHorarioEspecialService;
import com.gestion.deportiva.service.InstalacionHorarioService;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class ReservaSolicitudInstalacionAbiertaValidatorTest {

	@Mock
	private InstalacionHorarioEspecialService instalacionHorarioEspecialService;

	@Mock
	private InstalacionHorarioService instalacionHorarioService;

	@Mock
	private InstalacionHorarioBloqueadoService instalacionHorarioBloqueadoService;

	@InjectMocks
	private ReservaSolicitudInstalacionAbiertaValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private ReservaSolicitudDTO dto;

	@BeforeEach
	void setUp() {
		dto = new ReservaSolicitudDTO();
		dto.setInstalacionId(1L);
		dto.setFecha(LocalDate.of(2026, 8, 15));
		dto.setHora(LocalTime.of(10, 0));
		dto.setDuracion(60L);
	}

	@Test
	void shouldReturnTrueWhenInstallationIsOpenAndAvailable() {
		when(instalacionHorarioEspecialService.estaAbierta(eq(1L), eq(dto.getFecha()), eq(dto.getHora()), eq(60L)))
				.thenReturn(true);
		when(instalacionHorarioService.estaAbierta(eq(1L), eq(dto.getFecha()), eq(dto.getHora()), eq(60L)))
				.thenReturn(true);
		when(instalacionHorarioBloqueadoService.estaDisponible(eq(1L), eq(dto.getFecha()), eq(dto.getHora()), eq(60L)))
				.thenReturn(true);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenSpecialScheduleIsNotOpen() {
		when(instalacionHorarioEspecialService.estaAbierta(eq(1L), eq(dto.getFecha()), eq(dto.getHora()), eq(60L)))
				.thenReturn(false);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnFalseWhenRegularScheduleIsNotOpen() {
		when(instalacionHorarioEspecialService.estaAbierta(eq(1L), eq(dto.getFecha()), eq(dto.getHora()), eq(60L)))
				.thenReturn(true);
		when(instalacionHorarioService.estaAbierta(eq(1L), eq(dto.getFecha()), eq(dto.getHora()), eq(60L)))
				.thenReturn(false);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnFalseWhenScheduleIsBlocked() {
		when(instalacionHorarioEspecialService.estaAbierta(eq(1L), eq(dto.getFecha()), eq(dto.getHora()), eq(60L)))
				.thenReturn(true);
		when(instalacionHorarioService.estaAbierta(eq(1L), eq(dto.getFecha()), eq(dto.getHora()), eq(60L)))
				.thenReturn(true);
		when(instalacionHorarioBloqueadoService.estaDisponible(eq(1L), eq(dto.getFecha()), eq(dto.getHora()), eq(60L)))
				.thenReturn(false);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}
}