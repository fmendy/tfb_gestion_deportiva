package com.gestion.deportiva.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.util.SecurityUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class AuditorAwareImpl implements AuditorAware<Usuario> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Optional<Usuario> getCurrentAuditor() {
		Long userId = 1L;
		if (SecurityUtil.isAuthenticated()) {
			userId = SecurityUtil.getCurrentUserId();
		}

		if (userId == null) {
			return Optional.empty();
		}

		Usuario proxyUser = entityManager.getReference(Usuario.class, userId);

		return Optional.of(proxyUser);
	}
}