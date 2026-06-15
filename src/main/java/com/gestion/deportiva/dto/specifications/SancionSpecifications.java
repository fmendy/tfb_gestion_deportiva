package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.SancionFilter;
import com.gestion.deportiva.model.Sancion;

public class SancionSpecifications extends BaseSpecifications<Sancion> {

	public static Specification<Sancion> filter(SancionFilter filter) {
		List<Specification<Sancion>> specs = new ArrayList<>();

		specs.add(new SancionSpecifications().activoTrue());

		if (filter.getEmpresaId() != null) {
			specs.add(new SancionSpecifications().equalsFieldLong( filter.getEmpresaId(),"empresa", "id"));
		}

		if (filter.getUsuarioId() != null) {
			specs.add(new SancionSpecifications().equalsFieldLong( filter.getUsuarioId(),"usuario", "id"));
		}

		if (filter.getSancionTipoId() != null) {
			specs.add(new SancionSpecifications().equalsFieldLong( filter.getSancionTipoId(),"sancionTipo", "id"));
		}

		return new SancionSpecifications().combine(specs);
	}
}
