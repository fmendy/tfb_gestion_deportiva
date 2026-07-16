package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.UsuarioToken;

@Repository
public interface UsuarioTokenRepository extends BaseEntityRepository<UsuarioToken, Long> {

	List<UsuarioToken> findByActivoTrueAndUsuarioId(Long usuarioId);
}
