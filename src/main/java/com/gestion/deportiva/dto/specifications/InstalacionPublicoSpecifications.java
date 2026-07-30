package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.InstalacionPublicoFilter;
import com.gestion.deportiva.model.Instalacion;

public class InstalacionPublicoSpecifications extends BaseSpecifications<Instalacion> {

	public static Specification<Instalacion> filter(InstalacionPublicoFilter filter) {
		List<Specification<Instalacion>> specs = new ArrayList<>();

		specs.add(new InstalacionPublicoSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new InstalacionPublicoSpecifications().likeIgnoreCase(filter.getNombre(), "nombre"));
		}

		if (filter.getInstalacionTipoId() != null) {
			specs.add(new InstalacionPublicoSpecifications().equalsFieldLong(filter.getInstalacionTipoId(),
					"instalacionTipo", "id"));
		}

		if (filter.getMunicipioId() != null) {
			specs.add(new InstalacionPublicoSpecifications().equalsFieldLong(filter.getMunicipioId(), "sede",
					"municipio", "id"));
		}

		return new InstalacionPublicoSpecifications().combine(specs);
	}
}
