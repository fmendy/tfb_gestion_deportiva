package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.InstalacionHorarioBloqueadoFilter;
import com.gestion.deportiva.model.InstalacionHorarioBloqueado;

public class InstalacionHorarioBloqueadoSpecifications extends BaseSpecifications<InstalacionHorarioBloqueado> {

	public static Specification<InstalacionHorarioBloqueado> filter(InstalacionHorarioBloqueadoFilter filter) {
		List<Specification<InstalacionHorarioBloqueado>> specs = new ArrayList<>();

		specs.add(new InstalacionHorarioBloqueadoSpecifications().activoTrue());

		if (filter.getSedeId() != null) {
			specs.add(new InstalacionHorarioBloqueadoSpecifications().equalsFieldLong(filter.getSedeId(), "instalacion",
					"sede", "id"));
		}

		if (filter.getInstalacionId() != null) {
			specs.add(new InstalacionHorarioBloqueadoSpecifications().equalsFieldLong(filter.getInstalacionId(),
					"instalacion", "id"));
		}

		if (filter.getFechaDesde() != null) {
			specs.add(new InstalacionHorarioBloqueadoSpecifications().greaterThanOrEqualTo("fecha",
					filter.getFechaDesde()));
		}

		if (filter.getFechaHasta() != null) {
			specs.add(new InstalacionHorarioBloqueadoSpecifications().greaterThanOrEqualTo("fecha",
					filter.getFechaHasta()));
		}

		return new InstalacionHorarioBloqueadoSpecifications().combine(specs);
	}
}
