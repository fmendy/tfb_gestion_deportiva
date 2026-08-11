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

import com.gestion.deportiva.dto.MiPerfilDTO;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.UsuarioRepository;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class MiPerfilEmailUnicoValidatorTest {

	@Mock
	private UsuarioRepository repository;

	@InjectMocks
	private MiPerfilEmailUnicoValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	private MiPerfilDTO miPerfilDTO;
	private Usuario usuarioModel;

	@BeforeEach
	void setUp() {
		miPerfilDTO = new MiPerfilDTO();
		miPerfilDTO.setId(1L);
		miPerfilDTO.setUuid("uuid-123");
		miPerfilDTO.setEmail("perfil@test.com");

		usuarioModel = new Usuario();
		usuarioModel.setId(2L);
		usuarioModel.setUuid("uuid-456");
		usuarioModel.setEmail("perfil@test.com");
	}

	@Test
	void shouldReturnTrueWhenEmailDoesNotExist() {
		when(repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(eq("perfil@test.com"), anyLong()))
				.thenReturn(null);

		boolean resultado = validator.isValid(miPerfilDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnTrueWhenEmailExistsButBelongsToSameUuid() {
		usuarioModel.setUuid("uuid-123");
		when(repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(eq("perfil@test.com"), anyLong()))
				.thenReturn(usuarioModel);

		boolean resultado = validator.isValid(miPerfilDTO, context);

		assertTrue(resultado);
	}

	@Test
	void shouldReturnFalseWhenEmailExistsAndBelongsToDifferentUuid() {
		when(repository.findByActivoTrueAndEmailEqualsIgnoreCaseAndIdNot(eq("perfil@test.com"), anyLong()))
				.thenReturn(usuarioModel);

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(miPerfilDTO, context);

		assertFalse(resultado);
	}
}