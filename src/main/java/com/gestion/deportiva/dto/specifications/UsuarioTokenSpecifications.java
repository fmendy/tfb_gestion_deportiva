package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.UsuarioTokenFilter;
import com.gestion.deportiva.model.UsuarioToken;

public class UsuarioTokenSpecifications extends BaseSpecifications<UsuarioToken> {

	public static Specification<UsuarioToken> filter(UsuarioTokenFilter filter) {
		List<Specification<UsuarioToken>> specs = new ArrayList<>();

		specs.add(new UsuarioTokenSpecifications().activoTrue());

		if (filter.getUsuarioId() != null) {
			specs.add(new UsuarioTokenSpecifications().equalsFieldLong(filter.getUsuarioId(), "usuario", "id"));
		}

		return new UsuarioTokenSpecifications().combine(specs);
	}
}
