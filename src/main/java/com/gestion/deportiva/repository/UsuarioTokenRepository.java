package com.gestion.deportiva.repository;

import java.util.List;

import com.gestion.deportiva.model.UsuarioToken;

public interface UsuarioTokenRepository extends BaseEntityRepository<UsuarioToken, Long> {

	List<UsuarioToken> findByActivoTrueAndUsuarioId(Long usuarioId);
}
