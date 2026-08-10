package com.gestion.deportiva.validation;

import com.gestion.deportiva.dto.EmpleadoRegistroDTO;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.UsuarioRepository;
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
class EmpleadoEmailUnicoValidatorTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private ConstraintValidatorContext constraintValidatorContext;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilder;

	@InjectMocks
	private EmpleadoEmailUnicoValidator validator;

	private EmpleadoRegistroDTO dto;

	@BeforeEach
	void setUp() {
		dto = new EmpleadoRegistroDTO();
		dto.setEmail("test@gestion.com");
		dto.setId(1L);
		dto.setUuid("uuid-123");
	}

	@Test
	@DisplayName("Debe retornar true si no existe ningún usuario activo con ese email y diferente ID")
	void shouldReturnTrueWhenEmailDoesNotExist() {
		when(usuarioRepository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot("test@gestion.com", 1L))
				.thenReturn(null);

		boolean isValid = validator.isValid(dto, constraintValidatorContext);

		assertTrue(isValid);
		verify(usuarioRepository).findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot("test@gestion.com", 1L);
	}

	@Test
	@DisplayName("Debe retornar true si el email existe pero pertenece al mismo registro que se está editando (mismo uuid)")
	void shouldReturnTrueWhenEmailBelongsToSameRecord() {
		Usuario existingUser = new Usuario();
		existingUser.setUuid("uuid-123");
		existingUser.setEmail("test@gestion.com");

		when(usuarioRepository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot("test@gestion.com", 1L))
				.thenReturn(existingUser);

		boolean isValid = validator.isValid(dto, constraintValidatorContext);

		assertTrue(isValid);
	}

	@Test
	@DisplayName("Debe retornar false y asociar la violación al campo 'email' si el email ya existe en otro registro")
	void shouldReturnFalseAndAddViolationWhenEmailAlreadyExistsInAnotherRecord() {
		Usuario existingUser = new Usuario();
		existingUser.setUuid("uuid-999"); // Distinto UUID (otro registro)
		existingUser.setEmail("test@gestion.com");

		when(usuarioRepository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot("test@gestion.com", 1L))
				.thenReturn(existingUser);

		when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString()))
				.thenReturn(constraintViolationBuilder);
		when(constraintViolationBuilder.addPropertyNode("email")).thenReturn(nodeBuilder);

		try (MockedStatic<Utils> utilsMock = Mockito.mockStatic(Utils.class)) {
			utilsMock.when(() -> Utils.getMessage("error.validacion.registro.empleado.email.unico"))
					.thenReturn("El email del empleado ya existe");

			boolean isValid = validator.isValid(dto, constraintValidatorContext);

			assertFalse(isValid);
			verify(constraintValidatorContext).disableDefaultConstraintViolation();
			verify(constraintValidatorContext).buildConstraintViolationWithTemplate("El email del empleado ya existe");
			verify(constraintViolationBuilder).addPropertyNode("email");
			verify(nodeBuilder).addConstraintViolation();
		}
	}
}