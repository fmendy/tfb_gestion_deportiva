package com.gestion.deportiva.dto.specifications;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.util.SecurityUtil;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;

public abstract class BaseSpecifications<T> {

	protected Specification<T> activoTrue() {
		return (root, query, cb) -> cb.equal(root.get("activo"), true);
	}

	protected Specification<T> likeIgnoreCase(String value, String... fields) {
		return (root, query, cb) -> {
			if (!StringUtils.hasText(value)) {
				return null;
			}
			// 'current' empieza siendo el Root (que implementa From)
			From<?, ?> current = root;
			// Iteramos hasta el penúltimo elemento para hacer los joins
			for (int i = 0; i < fields.length - 1; i++) {
				// Hacemos el join y actualizamos 'current'
				current = current.join(fields[i], JoinType.INNER);
			}
			// El último elemento es el campo final (el atributo a comparar)
			String fieldFinal = fields[fields.length - 1];
			Path<String> path = current.get(fieldFinal);

			return cb.like(cb.upper(path), "%" + value.toUpperCase() + "%");
		};
	}

	protected Specification<T> fieldInString(List<String> listString, String... fields) {
		return (root, query, cb) -> {
			if (listString == null || listString.isEmpty() || fields == null || fields.length == 0) {
				return null;
			}

			// Caminamos dinámicamente a través de los campos
			Path<?> path = root;
			for (String field : fields) {
				path = path.get(field);
			}

			return path.in(listString);
		};
	}

	protected Specification<T> fieldInLong(List<Long> listLong, String... fields) {
		return (root, query, cb) -> {
			if (listLong == null || listLong.isEmpty() || fields == null || fields.length == 0) {
				return null;
			}

			// Caminamos dinámicamente a través de los campos
			Path<?> path = root;
			for (String field : fields) {
				path = path.get(field);
			}

			return path.in(listLong);
		};
	}

	protected Specification<T> greaterThanOrEqualTo(String field, LocalDate value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.greaterThanOrEqualTo(root.get(field), value);
			}
			return null;
		};
	}

	protected Specification<T> lessThanOrEqualTo(String field, LocalDate value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.lessThanOrEqualTo(root.get(field), value);
			}
			return null;
		};
	}

	protected Specification<T> createByUserInSession() {
		return (root, query, cb) -> {
			return cb.equal(root.get("usuarioCreacion").get("id"), SecurityUtil.getCurrentUserId());
		};
	}

	protected Specification<T> equalsFieldLong(Long value, String... fields) {
		return (root, query, cb) -> {
			// Obtenemos el path inicial empezando por el primer campo
			Path<Object> path = root.get(fields[0]);

			// Iteramos sobre los campos restantes para ir haciendo "get" encadenados
			for (int i = 1; i < fields.length; i++) {
				path = path.get(fields[i]);
			}

			return cb.equal(path, value);
		};
	}

	protected Specification<T> equalsFieldBoolean(Boolean value, String... fields) {
		return (root, query, cb) -> {
			// Obtenemos el path inicial empezando por el primer campo
			Path<Object> path = root.get(fields[0]);

			// Iteramos sobre los campos restantes para ir haciendo "get" encadenados
			for (int i = 1; i < fields.length; i++) {
				path = path.get(fields[i]);
			}

			return cb.equal(path, value);
		};
	}

	protected Specification<T> createdByUsuarioUuid(String uuid) {
		return (root, query, cb) -> cb.equal(cb.upper(root.get("usuarioCreacion").get("uuid")), uuid.toUpperCase());
	}

	protected Specification<T> combine(List<Specification<T>> specs) {
		return specs.stream().filter(spec -> spec != null).reduce(Specification::and).orElse(null);
	}



	protected Specification<T> inListLeftJoin(String collectionName, List<?> values, String... pathParts) {
		return (root, query, cb) -> {
			if (values == null || values.isEmpty())
				return null;

			// En lugar de join, usamos un Subquery EXISTS
			jakarta.persistence.criteria.Subquery<Long> subquery = query.subquery(Long.class);
			jakarta.persistence.criteria.Root<T> subRoot = subquery.correlate(root);

			var join = subRoot.join(collectionName);
			jakarta.persistence.criteria.Path<Object> path = join;

			for (String part : pathParts) {
				path = path.get(part);
			}

			// Filtro de activo en la relación
			jakarta.persistence.criteria.Predicate activePredicate = cb.isTrue(join.get("activo"));

			subquery.select(subRoot.get("id")).where(cb.and(activePredicate, path.in(values)));

			return cb.exists(subquery);
		};
	}

	// Inner Join
	protected Specification<T> inList(String collectionName, List<?> values, String... pathParts) {
		return (root, query, cb) -> {
			if (values == null || values.isEmpty()) {
				return null;
			}

			// 1. Join inicial a la colección
			var join = root.join(collectionName);

			// Creamos una lista de predicados para incluir el filtro de 'activo' en cada
			// paso
			List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

			// 2. Navegación dinámica
			jakarta.persistence.criteria.Path<Object> path = join;

			for (String part : pathParts) {
				// Antes de avanzar, verificamos si el nivel actual tiene la propiedad 'activo'
				// Esto evita errores si una de las entidades intermedias no tiene este campo
				try {
					// Intentamos obtener el campo "activo" del objeto actual (path)
					var activoPath = path.get("activo");
					predicates.add(cb.isTrue(activoPath.as(Boolean.class)));
				} catch (IllegalArgumentException e) {
					// El campo no existe en este nivel, continuamos normalmente
				}

				// Avanzamos al siguiente subcampo
				path = path.get(part);
			}

			// 3. Aplicamos el predicado IN final y los filtros de activo acumulados
			query.distinct(true);
			predicates.add(path.in(values));

			return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
		};
	}

}
