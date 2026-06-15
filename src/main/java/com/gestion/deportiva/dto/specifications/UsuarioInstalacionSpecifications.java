package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.UsuarioInstalacionFilter;
import com.gestion.deportiva.model.UsuarioInstalacion;

public class UsuarioInstalacionSpecifications extends BaseSpecifications<UsuarioInstalacion> {

	public static Specification<UsuarioInstalacion> filter(UsuarioInstalacionFilter filter) {
		List<Specification<UsuarioInstalacion>> specs = new ArrayList<>();

		specs.add(new UsuarioInstalacionSpecifications().activoTrue());

		if (filter.getSedeId() != null) {
			specs.add(new UsuarioInstalacionSpecifications().equalsFieldLong(
					filter.getSedeId(),"instalacion", "sede", "id"));
		}

		if (filter.getInstalacionId() != null) {
			specs.add(new UsuarioInstalacionSpecifications().equalsFieldLong(
					filter.getInstalacionId(),"instalacion", "id"));
		}

		if (filter.getUsuarioId() != null) {
			specs.add(new UsuarioInstalacionSpecifications().equalsFieldLong(
					filter.getUsuarioId(),"usuario", "id"));
		}	

		return new UsuarioInstalacionSpecifications().combine(specs);
	}
}
