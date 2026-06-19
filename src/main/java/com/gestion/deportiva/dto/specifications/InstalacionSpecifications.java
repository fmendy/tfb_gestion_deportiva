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
			specs.add(new InstalacionSpecifications().likeIgnoreCase(filter.getNombre(), "nombre"));
		}

		if (filter.getInstalacionTipoId() != null) {
			specs.add(new InstalacionSpecifications().equalsFieldLong(filter.getInstalacionTipoId(),
					"instalacionTipoId"));
		}

		if (filter.getSedeId() != null) {
			specs.add(new InstalacionSpecifications().equalsFieldLong(filter.getSedeId(), "sede", "id"));
		}

		if (filter.getListEmpresaIds() != null && !filter.getListEmpresaIds().isEmpty()) {
			specs.add(new InstalacionSpecifications().fieldInLong(filter.getListEmpresaIds(), "sede", "empresa", "id"));
		}

		if (filter.getListSedeIds() != null && !filter.getListSedeIds().isEmpty()) {
			specs.add(new InstalacionSpecifications().fieldInLong(filter.getListSedeIds(), "sede", "id"));
		}

		if (filter.getListInstalacionIds() != null && !filter.getListInstalacionIds().isEmpty()) {
			specs.add(new InstalacionSpecifications().fieldInLong(filter.getListInstalacionIds(), "id"));
		}
		return new InstalacionSpecifications().combine(specs);
	}
}
