package com.gestion.deportiva.service;

import com.gestion.deportiva.dto.UsuarioTokenDTO;
import com.gestion.deportiva.dto.filter.UsuarioTokenFilter;
import com.gestion.deportiva.model.UsuarioToken;

public interface UsuarioTokenService extends BaseService<UsuarioTokenDTO, UsuarioTokenFilter> {

	String crearToken(Long usuarioId);

	void desactivarTokensByUsuarioId(Long usuarioId);

	Boolean isValidToken(UsuarioToken usuarioToken, Long minValid);

	UsuarioToken getTokenActivoByUuid(String uuid);

}
