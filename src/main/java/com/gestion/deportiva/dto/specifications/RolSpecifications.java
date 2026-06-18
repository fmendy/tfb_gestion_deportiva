package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.RolFilter;
import com.gestion.deportiva.model.Rol;

public class RolSpecifications extends BaseSpecifications<Rol> {

	public static Specification<Rol> filter(RolFilter filter) {
		List<Specification<Rol>> specs = new ArrayList<>();

		specs.add(new RolSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new RolSpecifications().likeIgnoreCase(filter.getNombre(), "nombre"));
		}

		if (!filter.getListNombre().isEmpty()) {
			specs.add(new RolSpecifications().fieldInString("nombre", filter.getListNombre()));
		}

		return new RolSpecifications().combine(specs);
	}
}
