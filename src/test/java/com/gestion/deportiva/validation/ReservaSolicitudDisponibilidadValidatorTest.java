package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.service.ReservaService;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class ReservaSolicitudDisponibilidadValidatorTest {

	@Mock
	private ReservaService reservaService;

	@InjectMocks
	private ReservaSolicitudDisponibilidadValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private ReservaSolicitudDTO dto;
	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	@BeforeEach
	void setUp() {
		dto = new ReservaSolicitudDTO();
		dto.setFecha(LocalDate.of(2026, java.time.Month.AUGUST, 15));
		dto.setHora(LocalTime.of(10, 0));
		dto.setDuracion(60L);
		dto.setInstalacionId(5L);

		securityUtilMockedStatic = mockStatic(SecurityUtil.class);
	}

	@AfterEach
	void tearDown() {
		securityUtilMockedStatic.close();
	}

	@Test
	void shouldReturnTrueWhenBothInstallationAndUserAreAvailable() {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(10L);

		when(reservaService.isFranjaHorariaDisponibleParaInstalacion(eq(dto.getFecha()), eq(dto.getHora()),
				eq(dto.getDuracion()), eq(dto.getInstalacionId()))).thenReturn(true);
		when(reservaService.isFranjaHorariaDisponibleParaUsuario(eq(dto.getFecha()), eq(dto.getHora()),
				eq(dto.getDuracion()), eq(10L))).thenReturn(true);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenInstallationIsNotAvailable() {
		when(reservaService.isFranjaHorariaDisponibleParaInstalacion(eq(dto.getFecha()), eq(dto.getHora()),
				eq(dto.getDuracion()), eq(dto.getInstalacionId()))).thenReturn(false);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void shouldReturnFalseWhenUserIsNotAvailable() {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(10L);

		when(reservaService.isFranjaHorariaDisponibleParaInstalacion(eq(dto.getFecha()), eq(dto.getHora()),
				eq(dto.getDuracion()), eq(dto.getInstalacionId()))).thenReturn(true);
		when(reservaService.isFranjaHorariaDisponibleParaUsuario(eq(dto.getFecha()), eq(dto.getHora()),
				eq(dto.getDuracion()), eq(10L))).thenReturn(false);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}
}