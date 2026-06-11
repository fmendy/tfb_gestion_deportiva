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
			specs.add(new UsuarioSedeSpecifications().equalsFieldLong("sede", "empresa", "id",
					filter.getEmpresaId()));
		}

		if (filter.getSedeId() != null) {
			specs.add(new UsuarioSedeSpecifications().equalsFieldLong("sede", "id",
					filter.getSedeId()));
		}

		if (filter.getUsuarioId() != null) {
			specs.add(new UsuarioSedeSpecifications().equalsFieldLong("usuario", "id",
					filter.getUsuarioId()));
		}	

		return new UsuarioSedeSpecifications().combine(specs);
	}
}
