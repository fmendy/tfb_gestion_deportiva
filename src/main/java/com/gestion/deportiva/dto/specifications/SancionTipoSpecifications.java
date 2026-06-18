package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.SancionTipoFilter;
import com.gestion.deportiva.model.SancionTipo;

public class SancionTipoSpecifications extends BaseSpecifications<SancionTipo> {

	public static Specification<SancionTipo> filter(SancionTipoFilter filter) {
		List<Specification<SancionTipo>> specs = new ArrayList<>();

		specs.add(new SancionTipoSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new SancionTipoSpecifications().likeIgnoreCase(filter.getNombre(), "nombre"));
		}

		return new SancionTipoSpecifications().combine(specs);
	}
}
