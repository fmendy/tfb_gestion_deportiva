package com.gestion.deportiva.validation;

import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.repository.EmpresaRepository;
import com.gestion.deportiva.util.Utils;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaCifUnicoValidatorTest {

	@Mock
	private EmpresaRepository empresaRepository;

	@Mock
	private ConstraintValidatorContext constraintValidatorContext;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

	@InjectMocks
	private EmpresaCifUnicoValidator validator;

	private EmpresaDTO dto;

	@BeforeEach
	void setUp() {
		dto = new EmpresaDTO();
		dto.setCif("A12345678");
		dto.setId(1L);
		dto.setUuid("uuid-123");
	}

	@Test
	@DisplayName("Debe retornar true si no existe ninguna empresa activa con ese CIF y diferente ID")
	void shouldReturnTrueWhenCifDoesNotExist() {
		when(empresaRepository.findByActivoTrueAndCifEqualsIgnoreCaseAndIdNot("A12345678", 1L)).thenReturn(null);

		boolean isValid = validator.isValid(dto, constraintValidatorContext);

		assertTrue(isValid);
		verify(empresaRepository).findByActivoTrueAndCifEqualsIgnoreCaseAndIdNot("A12345678", 1L);
	}

	@Test
	@DisplayName("Debe retornar true si el CIF existe pero pertenece al mismo registro que se está editando (mismo uuid)")
	void shouldReturnTrueWhenCifBelongsToSameRecord() {
		Empresa existingEmpresa = new Empresa();
		existingEmpresa.setUuid("uuid-123");
		existingEmpresa.setCif("A12345678");

		when(empresaRepository.findByActivoTrueAndCifEqualsIgnoreCaseAndIdNot("A12345678", 1L))
				.thenReturn(existingEmpresa);

		boolean isValid = validator.isValid(dto, constraintValidatorContext);

		assertTrue(isValid);
	}

	@Test
	@DisplayName("Debe retornar false y asociar la violación al campo 'cif' si el CIF ya existe en otro registro")
	void shouldReturnFalseAndAddViolationWhenCifAlreadyExistsInAnotherRecord() {
		Empresa existingEmpresa = new Empresa();
		existingEmpresa.setUuid("uuid-999"); // Distinto UUID (otro registro)
		existingEmpresa.setCif("A12345678");

		when(empresaRepository.findByActivoTrueAndCifEqualsIgnoreCaseAndIdNot("A12345678", 1L))
				.thenReturn(existingEmpresa);

		when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
				.thenReturn(constraintViolationBuilder);
		when(constraintViolationBuilder.addPropertyNode("cif")).thenReturn(nodeBuilder);

		try (MockedStatic<Utils> utilsMock = Mockito.mockStatic(Utils.class)) {
			utilsMock.when(() -> Utils.getMessage("error.validacion.empresa.cif.unico"))
					.thenReturn("El CIF de la empresa ya existe");

			boolean isValid = validator.isValid(dto, constraintValidatorContext);

			assertFalse(isValid);
			verify(constraintValidatorContext).disableDefaultConstraintViolation();
			verify(constraintValidatorContext).buildConstraintViolationWithTemplate("El CIF de la empresa ya existe");
			verify(constraintViolationBuilder).addPropertyNode("cif");
			verify(nodeBuilder).addConstraintViolation();
		}
	}
}