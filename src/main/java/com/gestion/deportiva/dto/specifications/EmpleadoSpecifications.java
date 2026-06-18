package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.EmpleadoFilter;
import com.gestion.deportiva.model.Usuario;

public class EmpleadoSpecifications extends BaseSpecifications<Usuario> {

	public static Specification<Usuario> filter(EmpleadoFilter filter) {
		List<Specification<Usuario>> specs = new ArrayList<>();

		specs.add(new UsuarioSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new UsuarioSpecifications().likeIgnoreCase(filter.getNombre(), "nombre"));
		}

		if (StringUtils.hasText(filter.getUuid())) {
			specs.add(new UsuarioSpecifications().likeIgnoreCase(filter.getUuid(), "uuid"));
		}

		if (StringUtils.hasText(filter.getEmail())) {
			specs.add(new UsuarioSpecifications().likeIgnoreCase(filter.getEmail(), "email"));
		}

		if (filter.getListEmpresaIds() != null && !filter.getListEmpresaIds().isEmpty()) {
			specs.add(new UsuarioSpecifications().inList("listUsuarioEmpresa", filter.getListEmpresaIds(), "empresa",
					"id"));
		}

		if (filter.getListSedeIds() != null && !filter.getListSedeIds().isEmpty()) {
			specs.add(new UsuarioSpecifications().inList("listUsuarioSede", filter.getListEmpresaIds(), "sede", "id"));
		}

		if (filter.getListInstalacionIds() != null && !filter.getListInstalacionIds().isEmpty()) {
			specs.add(new UsuarioSpecifications().inList("listUsuarioInstalacion", filter.getListEmpresaIds(),
					"instalacion", "id"));
		}

		if (filter.getEmpresaId() != null) {
			specs.add(new UsuarioSpecifications().inList("listUsuarioEmpresa", Arrays.asList(filter.getEmpresaId()),
					"empresa", "id"));
		}

		if (filter.getSedeId() != null) {
			specs.add(new UsuarioSpecifications().inList("listUsuarioSede", Arrays.asList(filter.getSedeId()), "sede",
					"id"));
		}

		if (filter.getInstalacionId() != null) {
			specs.add(new UsuarioSpecifications().inList("listUsuarioInstalacion",
					Arrays.asList(filter.getInstalacionId()), "instalacion", "id"));
		}

		return new UsuarioSpecifications().combine(specs);
	}
}
