package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.ProvinciaFilter;
import com.gestion.deportiva.model.Provincia;

public class ProvinciaSpecifications extends BaseSpecifications<Provincia> {

	public static Specification<Provincia> filter(ProvinciaFilter filter) {
		List<Specification<Provincia>> specs = new ArrayList<>();

		specs.add(new ProvinciaSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new ProvinciaSpecifications().likeIgnoreCase("nombre", filter.getNombre()));
		}
		if (StringUtils.hasText(filter.getComunidadAutonomaUuid())) {
			specs.add(new ProvinciaSpecifications().equalsIgnoreCase("comunidadAutonoma", "uuid",
					filter.getComunidadAutonomaUuid()));
		}
		if (filter.getCodigoIne() != null) {
			specs.add(new ProvinciaSpecifications().equalsFieldLong("codigoIne", filter.getCodigoIne()));
		}

		return new ProvinciaSpecifications().combine(specs);
	}
}
