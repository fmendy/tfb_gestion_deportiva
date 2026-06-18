package com.gestion.deportiva.dto.specifications;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

	protected Specification<T> fieldInString(String field, List<String> listString) {
		return (root, query, cb) -> {
			if (listString != null && !listString.isEmpty()) {
				return root.get(field).in(listString);
			}
			return null;
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

	protected Specification<T> equalsIgnoreCase(String field, String value) {
		return (root, query, cb) -> {
			if (StringUtils.hasText(value)) {
				return cb.equal(cb.upper(root.get(field)), value.toUpperCase());
			}
			return null;
		};
	}

	protected Specification<T> greaterThanOrEqualTo(String field, Date value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.greaterThanOrEqualTo(root.get(field), value);
			}
			return null;
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

	protected Specification<T> greaterThanOrEqualTo(String field, Double value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.greaterThanOrEqualTo(root.get(field), value);
			}
			return null;
		};
	}

	protected Specification<T> greaterThanOrEqualTo(String field, LocalDateTime value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.greaterThanOrEqualTo(root.get(field), value);
			}
			return null;
		};
	}

	protected Specification<T> greaterThanOrEqualTo(String field, String field2, LocalDateTime value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.greaterThanOrEqualTo(root.get(field).get(field2), value);
			}
			return null;
		};
	}

	protected Specification<T> lessThanOrEqualTo(String field, String field2, Date value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.lessThanOrEqualTo(root.get(field).get(field2), value);
			}
			return null;
		};
	}

	protected Specification<T> lessThanOrEqualTo(String field, Date value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.lessThanOrEqualTo(root.get(field), value);
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

	protected Specification<T> lessThanOrEqualTo(String field, Double value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.lessThanOrEqualTo(root.get(field), value);
			}
			return null;
		};
	}

	protected Specification<T> lessThanOrEqualTo(String field, LocalDateTime value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.lessThanOrEqualTo(root.get(field), value);
			}
			return null;
		};
	}

	protected Specification<T> lessThanOrEqualTo(String field, String field2, LocalDateTime value) {
		return (root, query, cb) -> {
			if (value != null) {
				return cb.lessThanOrEqualTo(root.get(field).get(field2), value);
			}
			return null;
		};
	}

	protected Specification<T> equalsIgnoreCase(String field, String field2, String value) {
		return (root, query, cb) -> {
			if (!StringUtils.hasText(value)) {
				return null;
			}

			// Join del primer nivel
			var join = root.join(field);

			// Filtramos solo los activos en el join
			var activoPredicate = cb.isTrue(join.get("activo"));

			// Comparación ignorando mayúsculas/minúsculas en field2
			var valuePredicate = cb.equal(cb.upper(join.get(field2)), value.toUpperCase());

			// Combinamos ambos predicados
			return cb.and(activoPredicate, valuePredicate);
		};
	}

	protected Specification<T> equalsIgnoreCase(String field, String field2, String field3, String value) {
		return (root, query, cb) -> {
			if (!StringUtils.hasText(value)) {
				return null;
			}

			// Join del primer nivel y filtramos solo activos
			var join1 = root.join(field);
			var activo1 = cb.isTrue(join1.get("activo"));

			// Join del segundo nivel y filtramos solo activos
			var join2 = join1.join(field2);
			var activo2 = cb.isTrue(join2.get("activo"));

			// Comparación del campo final ignorando mayúsculas/minúsculas
			var valuePredicate = cb.equal(cb.upper(join2.get(field3)), value.toUpperCase());

			// Combinamos los tres predicados
			return cb.and(activo1, activo2, valuePredicate);
		};
	}

	protected Specification<T> inIgnoreCase(String field, String field2, List<String> list) {
		return (root, query, cb) -> {
			if (list == null || list.isEmpty()) {
				return null; // No filtramos si la lista está vacía
			}

			// Convertimos todos los valores de la lista a mayúsculas y filtramos
			// nulos/vacíos
			List<String> upperList = list.stream().filter(StringUtils::hasText).map(String::toUpperCase).toList();

			if (upperList.isEmpty()) {
				return null; // Si después de filtrar no hay elementos
			}

			// Join del primer nivel y filtramos solo activos
			var join1 = root.join(field).on(cb.isTrue(root.join(field).get("activo")));

			// Expresión final para el campo que vamos a comparar
			var expression = cb.upper(join1.get(field2));

			// Retornamos un predicate IN ignorando mayúsculas/minúsculas
			return expression.in(upperList);
		};
	}

	protected Specification<T> inIgnoreCase(String field, String field2, String field3, List<String> list) {
		return (root, query, cb) -> {
			if (list == null || list.isEmpty()) {
				return null; // No filtramos si la lista está vacía
			}

			// Convertimos todos los valores de la lista a mayúsculas y filtramos
			// nulos/vacíos
			List<String> upperList = list.stream().filter(StringUtils::hasText).map(String::toUpperCase).toList();

			if (upperList.isEmpty()) {
				return null; // Si después de filtrar no hay elementos
			}

			// Join del primer nivel y filtramos solo activos
			var join1 = root.join(field).on(cb.isTrue(root.join(field).get("activo")));

			// Join del segundo nivel y filtramos solo activos (si aplica)
			var join2 = join1.join(field2).on(cb.isTrue(join1.get(field2).get("activo")));

			// Expresión final para el campo que vamos a comparar
			var expression = cb.upper(join2.get(field3));

			// Retornamos un predicate IN ignorando mayúsculas/minúsculas
			return expression.in(upperList);
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

	protected <E> Specification<T> fieldInJoinEquals(String relationField, // nombre de la relación en la entidad
			String joinField, // nombre del campo en la entidad relacionada
			String targetField, // nombre del campo final dentro del join
			E value // valor a comparar
	) {
		return (root, query, cb) -> {
			if (value == null)
				return null;
			return cb.equal(root.join(relationField).get(joinField).get(targetField), value);
		};
	}

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
