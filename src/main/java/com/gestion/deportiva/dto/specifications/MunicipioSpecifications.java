package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.MunicipioFilter;
import com.gestion.deportiva.model.Municipio;

public class MunicipioSpecifications extends BaseSpecifications<Municipio> {

	public static Specification<Municipio> filter(MunicipioFilter filter) {
		List<Specification<Municipio>> specs = new ArrayList<>();

		specs.add(new MunicipioSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new MunicipioSpecifications().likeIgnoreCase("nombre", filter.getNombre()));
		}
		if (StringUtils.hasText(filter.getProvinciaUuid())) {
			specs.add(new MunicipioSpecifications().equalsIgnoreCase("provincia", "uuid", filter.getProvinciaUuid()));
		}

		if (StringUtils.hasText(filter.getComunidadAutonomaUuid())) {
			specs.add(new MunicipioSpecifications().equalsIgnoreCase("provincia", "comunidadAutonoma", "uuid",
					filter.getComunidadAutonomaUuid()));
		}

		return new MunicipioSpecifications().combine(specs);
	}
}
