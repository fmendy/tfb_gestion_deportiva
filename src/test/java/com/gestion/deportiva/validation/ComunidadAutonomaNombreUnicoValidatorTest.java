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
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComunidadAutonomaNombreUnicoValidatorTest {

	@Mock
	private ComunidadAutonomaRepository comunidadAutonomaRepository;

	@Mock
	private ConstraintValidatorContext constraintValidatorContext;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

	@InjectMocks
	private ComunidadAutonomaNombreUnicoValidator validator;

	private ComunidadAutonomaDTO dto;

	@BeforeEach
	void setUp() {
		dto = new ComunidadAutonomaDTO();
		dto.setNombre("Andalucía");
		dto.setUuid("uuid-123");
	}

	@Test
	@DisplayName("Debe retornar true si el nombre es nulo (la validación delegada a @NotBlank)")
	void shouldReturnTrueWhenNombreIsNull() {
		dto.setNombre(null);

		boolean isValid = validator.isValid(dto, constraintValidatorContext);

		assertTrue(isValid);
		verifyNoInteractions(comunidadAutonomaRepository);
	}

	@Test
	@DisplayName("Debe retornar true si no existe ninguna comunidad activa con ese nombre")
	void shouldReturnTrueWhenNombreDoesNotExist() {
		when(comunidadAutonomaRepository.findByActivoTrueAndNombreEqualsIgnoreCase("Andalucía")).thenReturn(null);

		boolean isValid = validator.isValid(dto, constraintValidatorContext);

		assertTrue(isValid);
		verify(comunidadAutonomaRepository).findByActivoTrueAndNombreEqualsIgnoreCase("Andalucía");
	}

	@Test
	@DisplayName("Debe retornar true si el nombre existe pero pertenece al mismo registro que se está editando (mismo uuid)")
	void shouldReturnTrueWhenNombreBelongsToSameRecord() {
		ComunidadAutonoma existingModel = new ComunidadAutonoma();
		existingModel.setUuid("uuid-123");
		existingModel.setNombre("Andalucía");

		when(comunidadAutonomaRepository.findByActivoTrueAndNombreEqualsIgnoreCase("Andalucía"))
				.thenReturn(existingModel);

		boolean isValid = validator.isValid(dto, constraintValidatorContext);

		assertTrue(isValid);
	}

	@Test
	@DisplayName("Debe retornar false y asociar la violación al campo 'nombre' si el nombre ya existe en otro registro")
	void shouldReturnFalseAndAddViolationWhenNombreAlreadyExistsInAnotherRecord() {
		ComunidadAutonoma existingModel = new ComunidadAutonoma();
		existingModel.setUuid("uuid-999"); // Distinto UUID (otro registro)
		existingModel.setNombre("Andalucía");

		when(comunidadAutonomaRepository.findByActivoTrueAndNombreEqualsIgnoreCase("Andalucía"))
				.thenReturn(existingModel);

		when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
				.thenReturn(constraintViolationBuilder);
		when(constraintViolationBuilder.addPropertyNode("nombre")).thenReturn(nodeBuilder);

		try (MockedStatic<Utils> utilsMock = Mockito.mockStatic(Utils.class)) {
			utilsMock.when(() -> Utils.getMessage("error.validacion.comunidad.autonoma.nombre.unico"))
					.thenReturn("El nombre de la comunidad autónoma ya existe");

			boolean isValid = validator.isValid(dto, constraintValidatorContext);

			assertFalse(isValid);
			verify(constraintValidatorContext).disableDefaultConstraintViolation();
			verify(constraintValidatorContext)
					.buildConstraintViolationWithTemplate("El nombre de la comunidad autónoma ya existe");
			verify(constraintViolationBuilder).addPropertyNode("nombre");
			verify(nodeBuilder).addConstraintViolation();
		}
	}
}