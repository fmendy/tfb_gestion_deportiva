package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.UsuarioSedeFilter;
import com.gestion.deportiva.model.UsuarioSede;

public class UsuarioSedeSpecifications extends BaseSpecifications<UsuarioSede> {

	public static Specification<UsuarioSede> filter(UsuarioSedeFilter filter) {
		List<Specification<UsuarioSede>> specs = new ArrayList<>();

		specs.add(new UsuarioSedeSpecifications().activoTrue());

		if (filter.getEmpresaId() != null) {
			specs.add(new UsuarioSedeSpecifications().equalsFieldLong(filter.getEmpresaId(), "sede", "empresa", "id"));
		}

		if (filter.getSedeId() != null) {
			specs.add(new UsuarioSedeSpecifications().equalsFieldLong(filter.getSedeId(), "sede", "id"));
		}

		if (filter.getUsuarioId() != null) {
			specs.add(new UsuarioSedeSpecifications().equalsFieldLong(filter.getUsuarioId(), "usuario", "id"));
		}

		return new UsuarioSedeSpecifications().combine(specs);
	}
}
