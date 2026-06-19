package com.gestion.deportiva.service;

import com.gestion.deportiva.dto.UsuarioRolDTO;
import com.gestion.deportiva.dto.filter.UsuarioRolFilter;

public interface UsuarioRolService extends BaseService<UsuarioRolDTO, UsuarioRolFilter> {

	void asignarRol(Long usuarioId, String rolNombre);

	Long asignarRol(Long id, Long rolId);

	void eliminarRolesByUsuarioId(Long usuarioId);

}
