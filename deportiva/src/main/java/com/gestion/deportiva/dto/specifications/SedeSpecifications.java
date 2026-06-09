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


		return new SedeSpecifications().combine(specs);
	}
}
