package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.UsuarioEmpresaFilter;
import com.gestion.deportiva.model.UsuarioEmpresa;

public class UsuarioEmpresaSpecifications extends BaseSpecifications<UsuarioEmpresa> {

	public static Specification<UsuarioEmpresa> filter(UsuarioEmpresaFilter filter) {
		List<Specification<UsuarioEmpresa>> specs = new ArrayList<>();

		specs.add(new UsuarioEmpresaSpecifications().activoTrue());

		if (filter.getEmpresaId() != null) {
			specs.add(new UsuarioEmpresaSpecifications().equalsFieldLong( filter.getEmpresaId(),"empresa", "id"));
		}

		if (filter.getUsuarioId() != null) {
			specs.add(new UsuarioEmpresaSpecifications().equalsFieldLong( filter.getUsuarioId(),"usuario", "id"));
		}

		return new UsuarioEmpresaSpecifications().combine(specs);
	}
}
