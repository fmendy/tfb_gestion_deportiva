package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.InstalacionFilter;
import com.gestion.deportiva.model.Instalacion;

public class InstalacionSpecifications extends BaseSpecifications<Instalacion> {

	public static Specification<Instalacion> filter(InstalacionFilter filter) {
		List<Specification<Instalacion>> specs = new ArrayList<>();

		specs.add(new InstalacionSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new InstalacionSpecifications().likeIgnoreCase("nombre", filter.getNombre()));
		}

		if (filter.getInstalacionTipoId() != null) {
			specs.add(new InstalacionSpecifications().equalsFieldLong(filter.getInstalacionTipoId(),
					"instalacionTipoId"));
		}

		if (filter.getSedeId() != null) {
			specs.add(new InstalacionSpecifications().equalsFieldLong(filter.getSedeId(), "sedeId"));
		}

		return new InstalacionSpecifications().combine(specs);
	}
}
