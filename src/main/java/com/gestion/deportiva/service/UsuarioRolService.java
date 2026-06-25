package com.gestion.deportiva.service;

import com.gestion.deportiva.dto.UsuarioRolDTO;
import com.gestion.deportiva.dto.filter.UsuarioRolFilter;
import com.gestion.deportiva.model.UsuarioRol;

public interface UsuarioRolService extends BaseService<UsuarioRolDTO, UsuarioRolFilter> {

	void asignarRol(Long usuarioId, String rolNombre);

	Long asignarRol(Long id, Long rolId);

	void eliminarRolesByUsuarioId(Long usuarioId);

	UsuarioRol getUsuarioRolByUsuarioId(Long usuarioId);

}
