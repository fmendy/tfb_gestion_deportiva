package com.gestion.deportiva.dto.specifications;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.EmpleadoFilter;
import com.gestion.deportiva.model.Usuario;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class EmpleadoSpecificationsTest {

	@Mock
	private Root<Usuario> root;

	@Mock
	private CriteriaQuery<?> query;

	@Mock
	private CriteriaBuilder cb;

	@Mock
	private Path<Object> path;

	private EmpleadoFilter filter;

	@BeforeEach
	void setUp() {
		filter = new EmpleadoFilter();
	}

	@Test
	void shouldReturnSpecificationWhenFilterHasNombre() {
		filter.setNombre("Juan");
		when(root.get(anyString())).thenReturn(path);

		Specification<Usuario> spec = EmpleadoSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenFilterHasEmail() {
		filter.setEmail("test@test.com");
		when(root.get(anyString())).thenReturn(path);

		Specification<Usuario> spec = EmpleadoSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

}