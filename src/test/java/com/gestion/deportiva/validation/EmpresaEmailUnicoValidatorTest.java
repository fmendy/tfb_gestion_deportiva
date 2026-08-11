package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.repository.EmpresaRepository;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class EmpresaEmailUnicoValidatorTest {

	@Mock
	private EmpresaRepository repository;

	@InjectMocks
	private EmpresaEmailUnicoValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private EmpresaDTO empresaDTO;
	private Empresa empresaModel;

	@BeforeEach
	void setUp() {
		empresaDTO = new EmpresaDTO();
		empresaDTO.setId(1L);
		empresaDTO.setUuid("uuid-123");
		empresaDTO.setEmail("test@empresa.com");

		empresaModel = new Empresa();
		empresaModel.setId(2L);
		empresaModel.setUuid("uuid-456");
		empresaModel.setEmail("test@empresa.com");
	}

	@Test
	void isValidWhenEmailDoesNotExist_ShouldReturnTrue() {
		when(repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(eq("test@empresa.com"), anyLong()))
				.thenReturn(null);

		boolean resultado = validator.isValid(empresaDTO, context);

		assertTrue(resultado);
	}

	@Test
	void isValidWhenEmailExistsButBelongsToSameUuidShouldReturnTrue() {
		empresaModel.setUuid("uuid-123");
		when(repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(eq("test@empresa.com"), anyLong()))
				.thenReturn(empresaModel);

		boolean resultado = validator.isValid(empresaDTO, context);

		assertTrue(resultado);
	}

	@Test
	void isValidWhenEmailExistsAndBelongsToDifferentUuidShouldReturnFalse() {
		when(repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(eq("test@empresa.com"), anyLong()))
				.thenReturn(empresaModel);
		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(empresaDTO, context);

		assertFalse(resultado);
	}
}