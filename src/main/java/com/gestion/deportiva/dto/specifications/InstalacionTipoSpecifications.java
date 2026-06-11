package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.InstalacionTipoFilter;
import com.gestion.deportiva.model.InstalacionTipo;

public class InstalacionTipoSpecifications extends BaseSpecifications<InstalacionTipo> {

	public static Specification<InstalacionTipo> filter(InstalacionTipoFilter filter) {
		List<Specification<InstalacionTipo>> specs = new ArrayList<>();

		specs.add(new InstalacionTipoSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new InstalacionTipoSpecifications().likeIgnoreCase("nombre", filter.getNombre()));
		}

		return new InstalacionTipoSpecifications().combine(specs);
	}
}
