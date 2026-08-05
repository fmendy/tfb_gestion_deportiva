package com.gestion.deportiva.dto.specifications;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.util.SecurityUtil;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;

public abstract class BaseSpecifications<T> {

	protected Specification<T> activoTrue() {
		return (root, query, cb) -> cb.equal(root.get("activo"), true);
	}

	private From<?, ?> joinAndFilterActivo(From<?, ?> root, String... fields) {
		From<?, ?> current = root;
		for (int i = 0; i < fields.length - 1; i++) {
			Join<Object, Object> join = current.join(fields[i], JoinType.INNER);
			join.on(join.get("activo").in(true));
			current = join;
		}
		return current;
	}

	protected Specification<T> likeIgnoreCase(String value, String... fields) {
		return (root, query, cb) -> {
			if (!StringUtils.hasText(value))
				return null;

			From<?, ?> current = joinAndFilterActivo(root, fields);
			Path<String> path = current.get(fields[fields.length - 1]);

			return cb.like(cb.upper(path), "%" + value.toUpperCase() + "%");
		};
	}

	protected Specification<T> fieldInString(List<String> listString, String... fields) {
		return (root, query, cb) -> {
			if (listString == null || listString.isEmpty())
				return null;

			From<?, ?> current = joinAndFilterActivo(root, fields);
			Path<String> path = current.get(fields[fields.length - 1]);

			return path.in(listString);
		};
	}

	protected Specification<T> fieldInLong(List<Long> listLong, String... fields) {
		return (root, query, cb) -> {
			if (listLong == null || listLong.isEmpty())
				return null;

			From<?, ?> current = joinAndFilterActivo(root, fields);
			Path<Long> path = current.get(fields[fields.length - 1]);

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
			if (fields.length == 1) {
				return cb.equal(root.get(fields[0]), value);
			}

			Join<Object, Object> currentJoin = root.join(fields[0]);
			currentJoin.on(cb.isTrue(currentJoin.get("activo")));

			for (int i = 1; i < fields.length - 1; i++) {
				currentJoin = currentJoin.join(fields[i]);
				currentJoin.on(cb.isTrue(currentJoin.get("activo")));
			}

			Path<Object> path = currentJoin.get(fields[fields.length - 1]);

			return cb.equal(path, value);
		};
	}

	protected Specification<T> equalsFieldBoolean(Boolean value, String... fields) {
	    return (root, query, cb) -> {
	        if (fields.length == 1) {
	            return cb.equal(root.get(fields[0]), value);
	        }

	        Join<Object, Object> currentJoin = root.join(fields[0]);
	        currentJoin.on(cb.isTrue(currentJoin.get("activo")));

	        for (int i = 1; i < fields.length - 1; i++) {
	            currentJoin = currentJoin.join(fields[i]);
	            currentJoin.on(cb.isTrue(currentJoin.get("activo")));
	        }

	        Path<Object> path = currentJoin.get(fields[fields.length - 1]);

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

			jakarta.persistence.criteria.Subquery<Long> subquery = query.subquery(Long.class);
			jakarta.persistence.criteria.Root<T> subRoot = subquery.correlate(root);

			var join = subRoot.join(collectionName);
			jakarta.persistence.criteria.Path<Object> path = join;

			for (String part : pathParts) {
				path = path.get(part);
			}

			jakarta.persistence.criteria.Predicate activePredicate = cb.isTrue(join.get("activo"));

			subquery.select(subRoot.get("id")).where(cb.and(activePredicate, path.in(values)));

			return cb.exists(subquery);
		};
	}

	protected Specification<T> inList(String collectionName, List<?> values, String... pathParts) {
		return (root, query, cb) -> {
			if (values == null || values.isEmpty()) {
				return null;
			}

			var join = root.join(collectionName);

			List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

			jakarta.persistence.criteria.Path<Object> path = join;

			for (String part : pathParts) {
				try {
					var activoPath = path.get("activo");
					predicates.add(cb.isTrue(activoPath.as(Boolean.class)));
				} catch (IllegalArgumentException e) {
				}

				path = path.get(part);
			}

			query.distinct(true);
			predicates.add(path.in(values));

			return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
		};
	}

}
