package com.gestion.deportiva.validation;

import com.gestion.deportiva.dto.ComunidadAutonomaDTO;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.repository.ComunidadAutonomaRepository;
import com.gestion.deportiva.util.Utils;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComunidadAutonomaCodigoIneUnicoValidatorTest {

	@Mock
	private ComunidadAutonomaRepository comunidadAutonomaRepository;

	@Mock
	private ConstraintValidatorContext constraintValidatorContext;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

	@InjectMocks
	private ComunidadAutonomaCodigoIneUnicoValidator validator;

	private ComunidadAutonomaDTO dto;

	@BeforeEach
	void setUp() {
		dto = new ComunidadAutonomaDTO();
		dto.setCodigoIne(01L);
		dto.setUuid("uuid-123");
	}

	@Test
	@DisplayName("Debe retornar true si el codigoIne es nulo (delegado a @NotBlank)")
	void shouldReturnTrueWhenCodigoIneIsNull() {
		dto.setCodigoIne(null);

		boolean isValid = validator.isValid(dto, constraintValidatorContext);

		assertTrue(isValid);
		verifyNoInteractions(comunidadAutonomaRepository);
	}

	@Test
	@DisplayName("Debe retornar true si no existe ninguna comunidad activa con ese codigoIne")
	void shouldReturnTrueWhenCodigoIneDoesNotExist() {
		when(comunidadAutonomaRepository.findByActivoTrueAndCodigoIne(01L)).thenReturn(null);

		boolean isValid = validator.isValid(dto, constraintValidatorContext);

		assertTrue(isValid);
		verify(comunidadAutonomaRepository).findByActivoTrueAndCodigoIne(01L);
	}

	@Test
	@DisplayName("Debe retornar true si el codigoIne existe pero pertenece al mismo registro que se está editando (mismo uuid)")
	void shouldReturnTrueWhenCodigoIneBelongsToSameRecord() {
		ComunidadAutonoma existingModel = new ComunidadAutonoma();
		existingModel.setUuid("uuid-123");
		existingModel.setCodigoIne(01L);

		when(comunidadAutonomaRepository.findByActivoTrueAndCodigoIne(01L)).thenReturn(existingModel);

		boolean isValid = validator.isValid(dto, constraintValidatorContext);

		assertTrue(isValid);
	}

	@Test
	@DisplayName("Debe retornar false y añadir violación si el codigoIne ya existe en otro registro diferente")
	void shouldReturnFalseAndAddViolationWhenCodigoIneAlreadyExistsInAnotherRecord() {
		ComunidadAutonoma existingModel = new ComunidadAutonoma();
		existingModel.setUuid("uuid-999");
		existingModel.setCodigoIne(01L);

		when(comunidadAutonomaRepository.findByActivoTrueAndCodigoIne(01L)).thenReturn(existingModel);

		when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
				.thenReturn(constraintViolationBuilder);
		when(constraintViolationBuilder.addPropertyNode(anyString())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		try (var utilsMock = Mockito.mockStatic(Utils.class)) {
			utilsMock.when(() -> Utils.getMessage("error.validacion.comunidad.autonoma.codigo.ine.unico"))
					.thenReturn("El código INE ya existe");

			boolean isValid = validator.isValid(dto, constraintValidatorContext);

			assertFalse(isValid);
			verify(constraintValidatorContext).disableDefaultConstraintViolation();
			verify(constraintValidatorContext).buildConstraintViolationWithTemplate("El código INE ya existe");
		}
	}
}