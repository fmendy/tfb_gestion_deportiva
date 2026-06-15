package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.InstalacionHorarioEspecialFilter;
import com.gestion.deportiva.model.InstalacionHorarioEspecial;

public class InstalacionHorarioEspecialSpecifications extends BaseSpecifications<InstalacionHorarioEspecial> {

	public static Specification<InstalacionHorarioEspecial> filter(InstalacionHorarioEspecialFilter filter) {
		List<Specification<InstalacionHorarioEspecial>> specs = new ArrayList<>();

		specs.add(new InstalacionHorarioEspecialSpecifications().activoTrue());

		if (filter.getSedeId() != null) {
			specs.add(new InstalacionHorarioEspecialSpecifications().equalsFieldLong(filter.getSedeId(), "instalacion",
					"sede", "id"));
		}

		if (filter.getInstalacionId() != null) {
			specs.add(new InstalacionHorarioEspecialSpecifications().equalsFieldLong(filter.getInstalacionId(),
					"instalacion", "id"));
		}

		if (filter.getFechaDesde() != null) {
			specs.add(new InstalacionHorarioEspecialSpecifications().greaterThanOrEqualTo("fecha",
					filter.getFechaDesde()));
		}

		if (filter.getFechaHasta() != null) {
			specs.add(new InstalacionHorarioEspecialSpecifications().greaterThanOrEqualTo("fecha",
					filter.getFechaHasta()));
		}

		return new InstalacionHorarioEspecialSpecifications().combine(specs);
	}
}
