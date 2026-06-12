package com.gestion.deportiva.dto.specifications;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.util.SecurityUtil;

public abstract class BaseSpecifications<T> {

	protected Specification<T> activoTrue() {
		return (root, query, cb) -> cb.equal(root.get("activo"), true);
	}

	protected Specification<T> likeIgnoreCase(String field, String value) {
		return (root, query, cb) -> {
			if (StringUtils.hasText(value)) {
				return cb.like(cb.upper(root.get(field)), "%" + value.toUpperCase() + "%");
			}
			return null;
		};
	}

	protected Specification<T> likeIgnoreCase(String field, String field2, String value) {
		return (root, query, cb) -> {
			if (StringUtils.hasText(value)) {
				return cb.like(cb.upper(root.get(field).get(field2)), "%" + value.toUpperCase() + "%");
			}
			return null;
		};
	}

	protected Specification<T> likeIgnoreCase(String field, String field2, String field3, String value) {
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
			var valuePredicate = cb.like(cb.upper(join2.get(field3)), "%" + value.toUpperCase() + "%");

			// Combinamos los tres predicados
			return cb.and(activo1, activo2, valuePredicate);
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
	
	protected Specification<T> fieldInLong(String field, List<Long> listLong) {
		return (root, query, cb) -> {
			if (listLong != null && !listLong.isEmpty()) {
				return root.get(field).in(listLong);
			}
			return null;
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

	protected Specification<T> equalsFieldLong(String field, Long value) {
		return (root, query, cb) -> {
			return cb.equal(root.get(field), value);
		};
	}

	protected Specification<T> equalsFieldLong(String field, String field2, Long value) {
		return (root, query, cb) -> {
			return cb.equal(root.get(field).get(field2), value);
		};
	}
	
	protected Specification<T> equalsFieldLong(String field, String field2, String field3, Long value) {
		return (root, query, cb) -> {
			return cb.equal(root.get(field).get(field2).get(field3), value);
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

}
