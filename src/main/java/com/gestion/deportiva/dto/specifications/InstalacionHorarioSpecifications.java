package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.InstalacionHorarioFilter;
import com.gestion.deportiva.model.InstalacionHorario;

public class InstalacionHorarioSpecifications extends BaseSpecifications<InstalacionHorario> {

	public static Specification<InstalacionHorario> filter(InstalacionHorarioFilter filter) {
		List<Specification<InstalacionHorario>> specs = new ArrayList<>();

		specs.add(new InstalacionHorarioSpecifications().activoTrue());

		if (filter.getSedeId() != null) {
			specs.add(new InstalacionHorarioSpecifications().equalsFieldLong("instalacion", "sede", "id",
					filter.getSedeId()));
		}

		if (filter.getInstalacionId() != null) {
			specs.add(new InstalacionHorarioSpecifications().equalsFieldLong("instalacion", "id",
					filter.getInstalacionId()));
		}

		if (filter.getDiaSemana() != null) {
			specs.add(new InstalacionHorarioSpecifications().equalsFieldLong("diaSemana", filter.getDiaSemana()));
		}

		return new InstalacionHorarioSpecifications().combine(specs);
	}
}
