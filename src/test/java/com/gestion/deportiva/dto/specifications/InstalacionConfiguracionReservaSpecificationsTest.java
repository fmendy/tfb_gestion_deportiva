package com.gestion.deportiva.dto.specifications;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.InstalacionConfiguracionReservaFilter;
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class InstalacionConfiguracionReservaSpecificationsTest {

	@Mock
	private Root<InstalacionConfiguracionReserva> root;

	@Mock
	private CriteriaQuery<?> query;

	@Mock
	private CriteriaBuilder cb;

	@Mock
	private Path<Object> path;

	private InstalacionConfiguracionReservaFilter filter;

	@BeforeEach
	void setUp() {
		filter = new InstalacionConfiguracionReservaFilter();
	}

	@Test
	void shouldReturnSpecificationWhenFilterIsEmpty() {
		Specification<InstalacionConfiguracionReserva> spec = InstalacionConfiguracionReservaSpecifications
				.filter(filter);
		assertNotNull(spec);
		spec.toPredicate(root, query, cb);
	}

}