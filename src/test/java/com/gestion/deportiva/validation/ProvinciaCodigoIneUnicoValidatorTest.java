package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.ProvinciaDTO;
import com.gestion.deportiva.model.Provincia;
import com.gestion.deportiva.repository.ProvinciaRepository;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class ProvinciaCodigoIneUnicoValidatorTest {

	@Mock
	private ProvinciaRepository provinciaRepository;

	@InjectMocks
	private ProvinciaCodigoIneUnicoValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private ProvinciaDTO provinciaDTO;
	private Provincia provinciaModel;

	@BeforeEach
	void setUp() {
		provinciaDTO = new ProvinciaDTO();
		provinciaDTO.setUuid("uuid-123");
		provinciaDTO.setCodigoIne(28l);

		provinciaModel = new Provincia();
		provinciaModel.setUuid("uuid-456");
		provinciaModel.setCodigoIne(28L);
	}

	@Test
	void shouldReturnTrueWhenCodigoIneIsNull() {
		provinciaDTO.setCodigoIne(null);

		boolean resultado = validator.isValid(provinciaDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenCodigoIneDoesNotExist() {
		when(provinciaRepository.findByActivoTrueAndCodigoIne(eq(28L))).thenReturn(null);

		boolean resultado = validator.isValid(provinciaDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenCodigoIneExistsButBelongsToSameUuid() {
		provinciaModel.setUuid("uuid-123");
		when(provinciaRepository.findByActivoTrueAndCodigoIne(eq(28L))).thenReturn(provinciaModel);

		boolean resultado = validator.isValid(provinciaDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenCodigoIneExistsAndBelongsToDifferentUuid() {
		when(provinciaRepository.findByActivoTrueAndCodigoIne(eq(28L))).thenReturn(provinciaModel);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(provinciaDTO, context);

		assertFalse(resultado);
	}
}