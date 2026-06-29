package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.SedePublicoFilter;
import com.gestion.deportiva.model.Sede;

public class SedeMapaSpecifications extends BaseSpecifications<Sede> {

	public static Specification<Sede> filter(SedePublicoFilter filter) {
		List<Specification<Sede>> specs = new ArrayList<>();

		specs.add(new SedeSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new SedeSpecifications().likeIgnoreCase(filter.getNombre(), "nombre"));
		}

		if (filter.getMunicipioId() != null) {
			specs.add(new SedeSpecifications().equalsFieldLong(filter.getMunicipioId(), "municipio", "id"));
		}

		if (filter.getProvinciaId() != null) {
			specs.add(
					new SedeSpecifications().equalsFieldLong(filter.getProvinciaId(), "municipio", "provincia", "id"));
		}

		if (filter.getComunidadAutonomaId() != null) {
			specs.add(new SedeSpecifications().equalsFieldLong(filter.getComunidadAutonomaId(), "municipio",
					"provincia", "comunidadAutonoma", "id"));
		}

		return new SedeSpecifications().combine(specs);
	}
}
