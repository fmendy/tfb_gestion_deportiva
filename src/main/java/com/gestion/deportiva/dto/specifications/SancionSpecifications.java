package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.SancionFilter;
import com.gestion.deportiva.model.Sancion;

public class SancionSpecifications extends BaseSpecifications<Sancion> {

	public static Specification<Sancion> filter(SancionFilter filter) {
		List<Specification<Sancion>> specs = new ArrayList<>();

		specs.add(new SancionSpecifications().activoTrue());

		if (filter.getReservaId() != null) {
			specs.add(new SancionSpecifications().equalsFieldLong(filter.getReservaId(), "reserva", "id"));
		}

		if (filter.getUsuarioId() != null) {
			specs.add(new SancionSpecifications().equalsFieldLong(filter.getUsuarioId(), "usuario", "id"));
		}

		if (filter.getSancionTipoId() != null) {
			specs.add(new SancionSpecifications().equalsFieldLong(filter.getSancionTipoId(), "sancionTipo", "id"));
		}

		if (StringUtils.isBlank(filter.getUsuarioNombre())) {
			specs.add(new SancionSpecifications().likeIgnoreCase(filter.getUsuarioNombre(), "usuario", "nombre"));
		}

		if (filter.getFechaFinDesde() != null) {
			specs.add(new SancionSpecifications().greaterThanOrEqualTo("fechaFin", filter.getFechaFinDesde()));
		}

		if (filter.getFechaFinHasta() != null) {
			specs.add(new SancionSpecifications().lessThanOrEqualTo("fechaFin", filter.getFechaFinHasta()));
		}

		if (filter.getFechaInicioDesde() != null) {
			specs.add(new SancionSpecifications().greaterThanOrEqualTo("fechaInicio", filter.getFechaInicioDesde()));
		}

		if (filter.getFechaInicioHasta() != null) {
			specs.add(new SancionSpecifications().lessThanOrEqualTo("fechaInicio", filter.getFechaInicioHasta()));
		}

		return new SancionSpecifications().combine(specs);
	}
}
