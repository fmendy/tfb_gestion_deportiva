package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.ReservaFilter;
import com.gestion.deportiva.model.Reserva;

public class ReservaSpecifications extends BaseSpecifications<Reserva> {

	public static Specification<Reserva> filter(ReservaFilter filter) {
		List<Specification<Reserva>> specs = new ArrayList<>();

		specs.add(new ReservaSpecifications().activoTrue());

		if (filter.getSedeId() != null) {
			specs.add(new ReservaSpecifications().equalsFieldLong(filter.getSedeId(), "instalacion", "sede", "id"));
		}

		if (filter.getInstalacionId() != null) {
			specs.add(new ReservaSpecifications().equalsFieldLong(filter.getInstalacionId(), "instalacion", "id"));
		}

		if (filter.getFechaDesde() != null) {
			specs.add(new ReservaSpecifications().greaterThanOrEqualTo("fecha", filter.getFechaDesde()));
		}

		if (filter.getFechaHasta() != null) {
			specs.add(new ReservaSpecifications().lessThanOrEqualTo("fecha", filter.getFechaHasta()));
		}

		if (filter.getReservaEstadoId() != null) {
			specs.add(new ReservaSpecifications().equalsFieldLong(filter.getReservaEstadoId(), "reservaEstado", "id"));
		}

		if (filter.getUsuarioCreacionId() != null) {
			specs.add(new ReservaSpecifications().equalsFieldLong(
					filter.getUsuarioCreacionId(),"usuarioCreacion", "id"));
		}

		return new ReservaSpecifications().combine(specs);
	}
}
