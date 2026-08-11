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

import com.gestion.deportiva.dto.filter.ComunidadAutonomaFilter;
import com.gestion.deportiva.model.ComunidadAutonoma;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class ComunidadAutonomaSpecificationsTest {

	@Mock
	private Root<ComunidadAutonoma> root;

	@Mock
	private CriteriaQuery<?> query;

	@Mock
	private CriteriaBuilder cb;

	@Mock
	private Path<Object> path;

	private ComunidadAutonomaFilter filter;

	@BeforeEach
	void setUp() {
		filter = new ComunidadAutonomaFilter();
	}

	@Test
	void shouldReturnSpecificationWhenFilterIsEmpty() {
		Specification<ComunidadAutonoma> spec = ComunidadAutonomaSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenFilterHasNombre() {
		filter.setNombre("Madrid");
		when(root.get(anyString())).thenReturn(path);

		Specification<ComunidadAutonoma> spec = ComunidadAutonomaSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenFilterHasCodigoIne() {
		filter.setCodigoIne(28L);
		when(root.get(anyString())).thenReturn(path);

		Specification<ComunidadAutonoma> spec = ComunidadAutonomaSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

	@Test
	void shouldReturnSpecificationWhenFilterHasAllFields() {
		filter.setNombre("Madrid");
		filter.setCodigoIne(28L);
		when(root.get(anyString())).thenReturn(path);

		Specification<ComunidadAutonoma> spec = ComunidadAutonomaSpecifications.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}
}