package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.SedeFilter;
import com.gestion.deportiva.model.Sede;

public class SedeSpecifications extends BaseSpecifications<Sede> {

	public static Specification<Sede> filter(SedeFilter filter) {
		List<Specification<Sede>> specs = new ArrayList<>();

		specs.add(new SedeSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new SedeSpecifications().likeIgnoreCase("nombre", filter.getNombre()));
		}

		if (filter.getEmpresaId() != null) {
			specs.add(new SedeSpecifications().equalsFieldLong("empresa", "id", filter.getEmpresaId()));
		}

		if (!filter.getListIds().isEmpty()) {
			specs.add(new SedeSpecifications().fieldInLong(filter.getListIds(), "id"));
		}

		if (!filter.getListEmpresaIds().isEmpty()) {
			specs.add(new SedeSpecifications().fieldInLong(filter.getListEmpresaIds(), "empresa", "id"));
		}

		return new SedeSpecifications().combine(specs);
	}
}
