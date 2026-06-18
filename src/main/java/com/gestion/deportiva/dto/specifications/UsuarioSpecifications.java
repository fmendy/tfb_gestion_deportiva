package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.UsuarioFilter;
import com.gestion.deportiva.model.Usuario;

public class UsuarioSpecifications extends BaseSpecifications<Usuario> {

	public static Specification<Usuario> filter(UsuarioFilter filter) {
		List<Specification<Usuario>> specs = new ArrayList<>();

		specs.add(new UsuarioSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new UsuarioSpecifications().likeIgnoreCase(filter.getNombre(), "nombre"));
		}

		if (StringUtils.hasText(filter.getUuid())) {
			specs.add(new UsuarioSpecifications().likeIgnoreCase(filter.getUuid(), "uuid"));
		}

		if (StringUtils.hasText(filter.getEmail())) {
			specs.add(new UsuarioSpecifications().likeIgnoreCase(filter.getEmail(), "email"));
		}

		return new UsuarioSpecifications().combine(specs);
	}
}
