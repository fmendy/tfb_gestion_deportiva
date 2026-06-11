package com.gestion.deportiva.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.util.SecurityUtil;

@Component
public class AuditorAwareImpl implements AuditorAware<Usuario> {

    @Override
    public Optional<Usuario> getCurrentAuditor() {
        if (!SecurityUtil.isAuthenticated()) {
            // System user (ID = 1)
            return Optional.of(new Usuario(1L));
        }

        // Return only the ID to avoid triggering DB queries or flushes
        Long userId = SecurityUtil.getCurrentUserId();
        Usuario minimalUser = new Usuario();
        minimalUser.setId(userId);
        return Optional.of(minimalUser);
    }
}
