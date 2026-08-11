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

import com.gestion.deportiva.dto.MunicipioDTO;
import com.gestion.deportiva.model.Municipio;
import com.gestion.deportiva.repository.MunicipioRepository;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class MunicipioCodigoIneUnicoValidatorTest {

	@Mock
	private MunicipioRepository municipioRepository;

	@InjectMocks
	private MunicipioCodigoIneUnicoValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private MunicipioDTO municipioDTO;
	private Municipio municipioModel;

	@BeforeEach
	void setUp() {
		municipioDTO = new MunicipioDTO();
		municipioDTO.setUuid("uuid-123");
		municipioDTO.setCodigoIne(28079L);
		municipioDTO.setProvinciaUuid("uuid-prov-1");

		municipioModel = new Municipio();
		municipioModel.setUuid("uuid-456");
		municipioModel.setCodigoIne(28079L);
	}

	@Test
	void shouldReturnTrueWhenCodigoIneIsNull() {
		municipioDTO.setCodigoIne(null);

		boolean resultado = validator.isValid(municipioDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenCodigoIneDoesNotExist() {
		when(municipioRepository.findByActivoTrueAndCodigoIneAndProvincia_Uuid(eq(28079L), eq("uuid-prov-1")))
				.thenReturn(null);

		boolean resultado = validator.isValid(municipioDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenCodigoIneExistsButBelongsToSameUuid() {
		municipioModel.setUuid("uuid-123");
		when(municipioRepository.findByActivoTrueAndCodigoIneAndProvincia_Uuid(eq(28079L), eq("uuid-prov-1")))
				.thenReturn(municipioModel);

		boolean resultado = validator.isValid(municipioDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenCodigoIneExistsAndBelongsToDifferentUuid() {
		when(municipioRepository.findByActivoTrueAndCodigoIneAndProvincia_Uuid(eq(28079L), eq("uuid-prov-1")))
				.thenReturn(municipioModel);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(municipioDTO, context);

		assertFalse(resultado);
	}
}