package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.ReservaEstadoFilter;
import com.gestion.deportiva.model.ReservaEstado;

public class ReservaEstadoSpecifications extends BaseSpecifications<ReservaEstado> {

	public static Specification<ReservaEstado> filter(ReservaEstadoFilter filter) {
		List<Specification<ReservaEstado>> specs = new ArrayList<>();

		specs.add(new ReservaEstadoSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new ReservaEstadoSpecifications().likeIgnoreCase(filter.getNombre(), "nombre"));
		}

		return new ReservaEstadoSpecifications().combine(specs);
	}
}
