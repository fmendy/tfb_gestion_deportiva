package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.ReservaSolicitudDTO;
import com.gestion.deportiva.repository.SancionRepository;
import com.gestion.deportiva.util.SecurityUtil;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class ReservaSolicitudSancionValidatorTest {

	@Mock
	private SancionRepository repository;

	@InjectMocks
	private ReservaSolicitudSancionValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private ReservaSolicitudDTO dto;
	private MockedStatic<SecurityUtil> securityUtilMockedStatic;
	private LocalDate fechaPrueba;

	@BeforeEach
	void setUp() {
		fechaPrueba = LocalDate.of(2026, java.time.Month.AUGUST, 15);
		dto = new ReservaSolicitudDTO();
		dto.setFecha(fechaPrueba);

		securityUtilMockedStatic = mockStatic(SecurityUtil.class);
	}

	@AfterEach
	void tearDown() {
		securityUtilMockedStatic.close();
	}

	@Test
	void shouldReturnTrueWhenUserIsNotSanctioned() {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(10L);

		when(repository.existsByActivoTrueAndUsuarioIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(eq(10L),
				eq(fechaPrueba), eq(fechaPrueba))).thenReturn(false);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenUserIsSanctioned() {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(10L);

		when(repository.existsByActivoTrueAndUsuarioIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(eq(10L),
				eq(fechaPrueba), eq(fechaPrueba))).thenReturn(true);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}
}