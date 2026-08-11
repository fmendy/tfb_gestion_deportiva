package com.gestion.deportiva.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

@ExtendWith(MockitoExtension.class)
class FieldMatchValidatorTest {

	@InjectMocks
	private FieldMatchValidator validator;

	@Mock
	private FieldMatchValid constraintAnnotation;

	@Mock
	private ConstraintValidatorContext context;

	@Mock
	private ConstraintViolationBuilder violationBuilder;

	public static class TestDTO {
		private String password;
		private String confirmPassword;

		public TestDTO(String password, String confirmPassword) {
			this.password = password;
			this.confirmPassword = confirmPassword;
		}

		public String getPassword() {
			return password;
		}

		public String getConfirmPassword() {
			return confirmPassword;
		}
	}

	@BeforeEach
	void setUp() {
		when(constraintAnnotation.first()).thenReturn("password");
		when(constraintAnnotation.second()).thenReturn("confirmPassword");

		validator.initialize(constraintAnnotation);
	}

	@Test
	void isValidWhenBothFieldsAreNullShouldReturnTrue() {
		TestDTO dto = new TestDTO(null, null);

		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void isValidWhenFieldsAreEqualShouldReturnTrue() {
		TestDTO dto = new TestDTO("secret123", "secret123");
		boolean resultado = validator.isValid(dto, context);

		assertTrue(resultado);
	}

	@Test
	void isValidWhenFieldsAreDifferentShouldReturnFalse() {
		TestDTO dto = new TestDTO("secret123", "different123");

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}

	@Test
	void isValidWhenFirstIsNullAndSecondIsNotNullShouldReturnFalse() {
		TestDTO dto = new TestDTO(null, "secret123");

		when(context.buildConstraintViolationWithTemplate(any())).thenReturn(violationBuilder);
		when(violationBuilder.addPropertyNode(any())).thenReturn(
				mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

		boolean resultado = validator.isValid(dto, context);

		assertFalse(resultado);
	}
}