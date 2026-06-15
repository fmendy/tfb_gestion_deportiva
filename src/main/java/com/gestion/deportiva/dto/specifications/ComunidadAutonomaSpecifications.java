package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.ComunidadAutonomaFilter;
import com.gestion.deportiva.model.ComunidadAutonoma;

public class ComunidadAutonomaSpecifications extends BaseSpecifications<ComunidadAutonoma> {

	public static Specification<ComunidadAutonoma> filter(ComunidadAutonomaFilter filter) {
		List<Specification<ComunidadAutonoma>> specs = new ArrayList<>();

		specs.add(new ComunidadAutonomaSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new ComunidadAutonomaSpecifications().likeIgnoreCase("nombre", filter.getNombre()));
		}
		if (filter.getCodigoIne() != null) {
			specs.add(new ComunidadAutonomaSpecifications().equalsFieldLong(filter.getCodigoIne(), "codigoIne"));
		}

		return new ComunidadAutonomaSpecifications().combine(specs);
	}
}
