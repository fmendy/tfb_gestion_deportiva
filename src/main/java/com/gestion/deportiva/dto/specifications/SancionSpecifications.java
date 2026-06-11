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
			specs.add(new SancionSpecifications().equalsFieldLong("empresa", "id", filter.getEmpresaId()));
		}

		if (filter.getUsuarioId() != null) {
			specs.add(new SancionSpecifications().equalsFieldLong("usuario", "id", filter.getUsuarioId()));
		}

		if (filter.getSancionTipoId() != null) {
			specs.add(new SancionSpecifications().equalsFieldLong("sancionTipo", "id", filter.getSancionTipoId()));
		}

		return new SancionSpecifications().combine(specs);
	}
}
