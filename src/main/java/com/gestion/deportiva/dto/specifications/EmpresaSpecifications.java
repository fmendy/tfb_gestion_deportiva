package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.EmpresaFilter;
import com.gestion.deportiva.model.Empresa;

public class EmpresaSpecifications extends BaseSpecifications<Empresa> {

	public static Specification<Empresa> filter(EmpresaFilter filter) {
		List<Specification<Empresa>> specs = new ArrayList<>();

		specs.add(new EmpresaSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new EmpresaSpecifications().likeIgnoreCase(filter.getNombre(), "nombre"));
		}

		if (StringUtils.hasText(filter.getCif())) {
			specs.add(new EmpresaSpecifications().likeIgnoreCase(filter.getCif(), "cif"));
		}

		if (!filter.getListIds().isEmpty()) {
			specs.add(new EmpresaSpecifications().fieldInLong(filter.getListIds(), "id"));
		}
		return new EmpresaSpecifications().combine(specs);
	}

}
