package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.InstalacionConfiguracionReservaFilter;
import com.gestion.deportiva.model.InstalacionConfiguracionReserva;

public class InstalacionConfiguracionReservaSpecifications extends BaseSpecifications<InstalacionConfiguracionReserva> {

	public static Specification<InstalacionConfiguracionReserva> filter(InstalacionConfiguracionReservaFilter filter) {
		List<Specification<InstalacionConfiguracionReserva>> specs = new ArrayList<>();

		specs.add(new InstalacionConfiguracionReservaSpecifications().activoTrue());

		if (filter.getSedeId() != null) {
			specs.add(new InstalacionConfiguracionReservaSpecifications().equalsFieldLong("instalacion", "sede", "id",
					filter.getSedeId()));
		}

		if (filter.getInstalacionId() != null) {
			specs.add(new InstalacionConfiguracionReservaSpecifications().equalsFieldLong("instalacion", "id",
					filter.getInstalacionId()));
		}

		return new InstalacionConfiguracionReservaSpecifications().combine(specs);
	}
}
