package com.gestion.deportiva.dto.specifications;
import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.UsuarioRolFilter;
import com.gestion.deportiva.model.UsuarioRol;


public class UsuarioRolSpecifications extends BaseSpecifications<UsuarioRol> {

	public static Specification<UsuarioRol> filter(UsuarioRolFilter filter) {
		List<Specification<UsuarioRol>> specs = new ArrayList<>();

		specs.add(new UsuarioRolSpecifications().activoTrue());

		return new UsuarioRolSpecifications().combine(specs);
	}
}
