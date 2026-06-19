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

		// --- GRUPO DE PERMISOS (OR) ---
		List<Specification<Usuario>> permisosSpecs = new ArrayList<>();

		if (filter.getListEmpresaIds() != null && !filter.getListEmpresaIds().isEmpty()) {
			permisosSpecs.add(new UsuarioSpecifications().inListLeftJoin("listUsuarioEmpresa", filter.getListEmpresaIds(),
					"empresa", "id"));
		}

		if (filter.getListSedeIds() != null && !filter.getListSedeIds().isEmpty()) {
			permisosSpecs
					.add(new UsuarioSpecifications().inListLeftJoin("listUsuarioSede", filter.getListSedeIds(), "sede", "id"));
		}

		if (filter.getListInstalacionIds() != null && !filter.getListInstalacionIds().isEmpty()) {
			permisosSpecs.add(new UsuarioSpecifications().inListLeftJoin("listUsuarioInstalacion",
					filter.getListInstalacionIds(), "instalacion", "id"));
		}

		// Si hay filtros de permisos, los combinamos con OR y los añadimos a la lista
		// principal
		if (!permisosSpecs.isEmpty()) {
			specs.add((root, query, cb) -> {
				var predicates = permisosSpecs.stream().map(spec -> spec.toPredicate(root, query, cb))
						.toArray(jakarta.persistence.criteria.Predicate[]::new);
				return cb.or(predicates);
			});
		}

		return new UsuarioSpecifications().combine(specs);
	}
}
