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

import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.UsuarioRepository;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class UsuarioRegistroEmailUnicoValidatorTest {

	@Mock
	private UsuarioRepository repository;

	@InjectMocks
	private UsuarioRegistroEmailUnicoValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private UsuarioRegistroDTO usuarioRegistroDTO;
	private Usuario usuarioModel;

	@BeforeEach
	void setUp() {
		usuarioRegistroDTO = new UsuarioRegistroDTO();
		usuarioRegistroDTO.setId(1L);
		usuarioRegistroDTO.setUuid("uuid-123");
		usuarioRegistroDTO.setEmail("registro@test.com");

		usuarioModel = new Usuario();
		usuarioModel.setId(2L);
		usuarioModel.setUuid("uuid-456");
		usuarioModel.setEmail("registro@test.com");
	}

	@Test
	void shouldReturnTrueWhenEmailDoesNotExist() {
		when(repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(eq("registro@test.com"), anyLong()))
				.thenReturn(null);

		boolean resultado = validator.isValid(usuarioRegistroDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenEmailExistsButBelongsToSameUuid() {
		usuarioModel.setUuid("uuid-123");
		when(repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(eq("registro@test.com"), anyLong()))
				.thenReturn(usuarioModel);

		boolean resultado = validator.isValid(usuarioRegistroDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenEmailExistsAndBelongsToDifferentUuid() {
		when(repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(eq("registro@test.com"), anyLong()))
				.thenReturn(usuarioModel);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(usuarioRegistroDTO, context);

		assertFalse(resultado);
	}
}