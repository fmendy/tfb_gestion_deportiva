package com.gestion.deportiva.dto.specifications;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.filter.UsuarioFilter;
import com.gestion.deportiva.model.Usuario;

public class UsuarioSpecifications extends BaseSpecifications<Usuario> {

	public static Specification<Usuario> filter(UsuarioFilter filter) {
		List<Specification<Usuario>> specs = new ArrayList<>();

		specs.add(new UsuarioSpecifications().activoTrue());

		if (StringUtils.hasText(filter.getNombre())) {
			specs.add(new UsuarioSpecifications().likeIgnoreCase("nombre", filter.getNombre()));
		}
		
		if (StringUtils.hasText(filter.getUuid())) {
			specs.add(new UsuarioSpecifications().likeIgnoreCase("uuid", filter.getUuid()));
		}

		if (StringUtils.hasText(filter.getEmail())) {
			specs.add(new UsuarioSpecifications().likeIgnoreCase("email", filter.getEmail()));
		}

		if (StringUtils.hasText(filter.getDemarcacionUuid())) {
			specs.add(new UsuarioSpecifications().inIgnoreCase("listUsuarioDemarcacion", "demarcacion", "uuid",
					Arrays.asList(filter.getDemarcacionUuid())));
		}

		if (filter.getListDemarcacionUuid() != null && !filter.getListDemarcacionUuid().isEmpty()) {
			specs.add(new UsuarioSpecifications().inIgnoreCase("listUsuarioDemarcacion", "demarcacion", "uuid",
					filter.getListDemarcacionUuid()));
		}

		if (StringUtils.hasText(filter.getRolUuid())) {
			specs.add(
					new UsuarioSpecifications().equalsIgnoreCase("listUsuarioRol", "rol", "uuid", filter.getRolUuid()));
		}

		return new UsuarioSpecifications().combine(specs);
	}
}
