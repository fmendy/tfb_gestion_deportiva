package com.gestion.deportiva.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gestion.deportiva.dto.MiPerfilPasswordDTO;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.repository.UsuarioRepository;
import com.gestion.deportiva.util.Utils;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

@ExtendWith(MockitoExtension.class)
class MiPerfilPasswordValidatorTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private MiPerfilPasswordValidator validator;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder constraintViolationBuilder;

	@Mock
	private NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

	private MockedStatic<Utils> utilsMocked;

	@BeforeEach
	void setUp() {
		utilsMocked = mockStatic(Utils.class);
		utilsMocked.when(() -> Utils.getMessage(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
	}

	@AfterEach
	void tearDown() {
		if (utilsMocked != null) {
			utilsMocked.close();
		}
	}

	@Test
	void invalidWhenFieldsAreBlankTest() {
		MiPerfilPasswordDTO dto = new MiPerfilPasswordDTO();
		dto.setPasswordActual("");
		dto.setPassword("Abc12345!");
		dto.setPasswordConfirmar("Abc12345!");

		boolean result = validator.isValid(dto, context);

		assertThat(result).isFalse();
	}

	@Test
	void invalidWhenCurrentPasswordDoesNotMatchTest() {
		MiPerfilPasswordDTO dto = new MiPerfilPasswordDTO();
		dto.setId(1L);
		dto.setPasswordActual("OldPasswordWrong");
		dto.setPassword("Abc12345!");
		dto.setPasswordConfirmar("Abc12345!");

		Usuario usuario = new Usuario();
		usuario.setPassword("encodedOldPassword");

		when(usuarioRepository.findByActivoTrueAndId(1L)).thenReturn(usuario);
		when(passwordEncoder.matches("OldPasswordWrong", "encodedOldPassword")).thenReturn(false);

		when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
		when(constraintViolationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilderCustomizableContext);

		boolean result = validator.isValid(dto, context);

		assertThat(result).isFalse();
		verify(context).disableDefaultConstraintViolation();
		verify(constraintViolationBuilder).addPropertyNode("passwordActual");
		verify(nodeBuilderCustomizableContext).addConstraintViolation();
	}

	@Test
	void invalidWhenNewPasswordsDoNotMatchTest() {
		MiPerfilPasswordDTO dto = new MiPerfilPasswordDTO();
		dto.setId(1L);
		dto.setPasswordActual("OldPasswordCorrect");
		dto.setPassword("Abc12345!");
		dto.setPasswordConfirmar("DifferentPassword1!");

		Usuario usuario = new Usuario();
		usuario.setPassword("encodedOldPassword");

		when(usuarioRepository.findByActivoTrueAndId(1L)).thenReturn(usuario);
		when(passwordEncoder.matches("OldPasswordCorrect", "encodedOldPassword")).thenReturn(true);

		when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
		when(constraintViolationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilderCustomizableContext);

		boolean result = validator.isValid(dto, context);

		assertThat(result).isFalse();
		verify(context).disableDefaultConstraintViolation();
		verify(constraintViolationBuilder).addPropertyNode("password");
		verify(nodeBuilderCustomizableContext).addConstraintViolation();
	}

	@Test
	void invalidWhenPasswordDoesNotMeetComplexityRegexTest() {
		MiPerfilPasswordDTO dto = new MiPerfilPasswordDTO();
		dto.setId(1L);
		dto.setPasswordActual("OldPasswordCorrect");
		dto.setPassword("simple"); // No cumple la regex (letras minúsculas simples, sin mayúsculas, números ni especiales)
		dto.setPasswordConfirmar("simple");

		Usuario usuario = new Usuario();
		usuario.setPassword("encodedOldPassword");

		when(usuarioRepository.findByActivoTrueAndId(1L)).thenReturn(usuario);
		when(passwordEncoder.matches("OldPasswordCorrect", "encodedOldPassword")).thenReturn(true);

		when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
		when(constraintViolationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilderCustomizableContext);

		boolean result = validator.isValid(dto, context);

		assertThat(result).isFalse();
		verify(constraintViolationBuilder).addPropertyNode("password");
		verify(nodeBuilderCustomizableContext).addConstraintViolation();
	}

	@Test
	void validWhenAllConditionsAreMetTest() {
		MiPerfilPasswordDTO dto = new MiPerfilPasswordDTO();
		dto.setId(1L);
		dto.setPasswordActual("OldPasswordCorrect");
		dto.setPassword("Abc12345!"); // Cumple min 8, mayús, minús, dígito y carácter especial
		dto.setPasswordConfirmar("Abc12345!");

		Usuario usuario = new Usuario();
		usuario.setPassword("encodedOldPassword");

		when(usuarioRepository.findByActivoTrueAndId(1L)).thenReturn(usuario);
		when(passwordEncoder.matches("OldPasswordCorrect", "encodedOldPassword")).thenReturn(true);

		boolean result = validator.isValid(dto, context);

		assertThat(result).isTrue();
	}
}