package com.gestion.deportiva.dto.specifications;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.EmpresaFilter;
import com.gestion.deportiva.model.Empresa;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class EmpresaSpecificationsTest {

	@Mock
	private Root<Empresa> root;

	@Mock
	private CriteriaQuery<?> query;

	@Mock
	private CriteriaBuilder cb;

	@Mock
	private Path<Object> path;

	private EmpresaFilter filter;

	@BeforeEach
	void setUp() {
		filter = new EmpresaFilter();
		filter.setListIds(Collections.emptyList());
	}

	@Test
	void shouldReturnSpecificationWhenFilterIsEmpty() {
		Specification<Empresa> spec = EmpresaSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenFilterHasNombre() {
		filter.setNombre("Empresa S.A.");
		when(root.get(anyString())).thenReturn(path);

		Specification<Empresa> spec = EmpresaSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenFilterHasCif() {
		filter.setCif("A12345678");
		when(root.get(anyString())).thenReturn(path);

		Specification<Empresa> spec = EmpresaSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenFilterHasListIds() {
		filter.setListIds(Arrays.asList(1L, 2L));
		when(root.get(anyString())).thenReturn(path);

		Specification<Empresa> spec = EmpresaSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenFilterHasAllFields() {
		filter.setNombre("Empresa S.A.");
		filter.setCif("A12345678");
		filter.setListIds(Arrays.asList(1L, 2L));
		when(root.get(anyString())).thenReturn(path);

		Specification<Empresa> spec = EmpresaSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}
}