package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import com.gestion.deportiva.dto.filter.ImagenFilter;
import com.gestion.deportiva.model.Imagen;

public class ImagenSpecifications extends BaseSpecifications<Imagen> {

	public static Specification<Imagen> filter(ImagenFilter filter) {
		List<Specification<Imagen>> specs = new ArrayList<>();

		specs.add(new ImagenSpecifications().activoTrue());

		if (filter.getEmpresaId() != null) {
			specs.add(new ImagenSpecifications().equalsFieldLong("empresaId", filter.getEmpresaId()));
		}
		
		if (filter.getSedeId() != null) {
			specs.add(new ImagenSpecifications().equalsFieldLong("sedeId", filter.getSedeId()));
		}
		
		if (filter.getInstalacionId() != null) {
			specs.add(new ImagenSpecifications().equalsFieldLong("instalacionId", filter.getInstalacionId()));
		}
		
		return new ImagenSpecifications().combine(specs);
	}
}
