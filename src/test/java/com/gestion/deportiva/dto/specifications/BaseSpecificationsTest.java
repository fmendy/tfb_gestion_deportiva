package com.gestion.deportiva.dto.specifications;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.util.SecurityUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class BaseSpecificationsTest {

	private TestSpecifications<DummyEntity> specifications;

	@Mock
	private Root<DummyEntity> root;

	@Mock
	private CriteriaQuery<?> query;

	@Mock
	private CriteriaBuilder cb;

	@Mock
	private Path<Object> path;

	private MockedStatic<SecurityUtil> securityUtilMockedStatic;

	private static class DummyEntity {
	}

	private static class TestSpecifications<T> extends BaseSpecifications<T> {
	}

	@BeforeEach
	void setUp() {
		specifications = new TestSpecifications<>();
		securityUtilMockedStatic = mockStatic(SecurityUtil.class);
	}

	@AfterEach
	void tearDown() {
		securityUtilMockedStatic.close();
	}

	@Test
	void shouldReturnSpecificationWhenActivoTrueIsCalled() {
		Specification<DummyEntity> spec = specifications.activoTrue();
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnNullWhenLikeIgnoreCaseHasNoText() {
		Specification<DummyEntity> spec = specifications.likeIgnoreCase("", "nombre");
		assertNotNull(spec);
		assertNull(spec.toPredicate(root, query, cb));
	}

	@Test
	void shouldReturnSpecificationWhenLikeIgnoreCaseHasText() {
		when(root.get(anyString())).thenReturn(path);
		Specification<DummyEntity> spec = specifications.likeIgnoreCase("test", "nombre");
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnNullWhenFieldInStringListIsEmpty() {
		Specification<DummyEntity> spec = specifications.fieldInString(Collections.emptyList(), "nombre");
		assertNotNull(spec);
		assertNull(spec.toPredicate(root, query, cb));
	}

	@Test
	void shouldReturnSpecificationWhenFieldInStringListIsValid() {
		when(root.get(anyString())).thenReturn(path);
		Specification<DummyEntity> spec = specifications.fieldInString(Arrays.asList("a", "b"), "nombre");
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnNullWhenFieldInLongListIsEmpty() {
		Specification<DummyEntity> spec = specifications.fieldInLong(Collections.emptyList(), "id");
		assertNotNull(spec);
		assertNull(spec.toPredicate(root, query, cb));
	}

	@Test
	void shouldReturnSpecificationWhenFieldInLongListIsValid() {
		when(root.get(anyString())).thenReturn(path);
		Specification<DummyEntity> spec = specifications.fieldInLong(Arrays.asList(1L, 2L), "id");
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnNullWhenGreaterThanOrEqualToValueIsNull() {
		Specification<DummyEntity> spec = specifications.greaterThanOrEqualTo("fecha", null);
		assertNotNull(spec);
		assertNull(spec.toPredicate(root, query, cb));
	}

	@Test
	void shouldReturnSpecificationWhenGreaterThanOrEqualToValueIsNotNull() {
		when(root.get(anyString())).thenReturn(path);
		Specification<DummyEntity> spec = specifications.greaterThanOrEqualTo("fecha", LocalDate.now());
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnNullWhenLessThanOrEqualToValueIsNull() {
		Specification<DummyEntity> spec = specifications.lessThanOrEqualTo("fecha", null);
		assertNotNull(spec);
		assertNull(spec.toPredicate(root, query, cb));
	}

	@Test
	void shouldReturnSpecificationWhenLessThanOrEqualToValueIsNotNull() {
		when(root.get(anyString())).thenReturn(path);
		Specification<DummyEntity> spec = specifications.lessThanOrEqualTo("fecha", LocalDate.now());
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenCreateByUserInSessionIsCalled() {
		securityUtilMockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(1L);
		when(root.get(anyString())).thenReturn(path);
		when(path.get(anyString())).thenReturn(path);

		Specification<DummyEntity> spec = specifications.createByUserInSession();
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenEqualsFieldLongSingleField() {
		when(root.get(anyString())).thenReturn(path);
		Specification<DummyEntity> spec = specifications.equalsFieldLong(1L, "id");
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenEqualsFieldLongMultipleFields() {
		var join = mock(jakarta.persistence.criteria.Join.class);
		when(root.join(anyString())).thenReturn(join);
		when(join.get(anyString())).thenReturn(path);

		Specification<DummyEntity> spec = specifications.equalsFieldLong(1L, "relacion", "id");
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenEqualsFieldBooleanSingleField() {
		when(root.get(anyString())).thenReturn(path);
		Specification<DummyEntity> spec = specifications.equalsFieldBoolean(true, "activo");
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenEqualsFieldBooleanMultipleFields() {
		var join = mock(jakarta.persistence.criteria.Join.class);
		when(root.join(anyString())).thenReturn(join);
		when(join.get(anyString())).thenReturn(path);

		Specification<DummyEntity> spec = specifications.equalsFieldBoolean(true, "relacion", "activo");
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenCreatedByUsuarioUuidIsCalled() {
		when(root.get(anyString())).thenReturn(path);
		when(path.get(anyString())).thenReturn(path);

		Specification<DummyEntity> spec = specifications.createdByUsuarioUuid("uuid-123");
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnNullWhenCombineListIsEmptyOrAllNull() {
		Specification<DummyEntity> spec = specifications.combine(Arrays.asList(null, null));
		assertNull(spec);
	}

	@Test
	void shouldReturnSpecificationWhenCombineListHasValidSpecs() {
		Specification<DummyEntity> spec = specifications.combine(Arrays.asList(specifications.activoTrue(), null));
		assertNotNull(spec);
	}

	@Test
	void shouldReturnNullWhenInListLeftJoinValuesIsEmpty() {
		Specification<DummyEntity> spec = specifications.inListLeftJoin("colleccion", Collections.emptyList(), "id");
		assertNotNull(spec);
		assertNull(spec.toPredicate(root, query, cb));
	}

	@Test
	void shouldReturnNullWhenInListValuesIsEmpty() {
		Specification<DummyEntity> spec = specifications.inList("colleccion", Collections.emptyList(), "id");
		assertNotNull(spec);
		assertNull(spec.toPredicate(root, query, cb));
	}
}