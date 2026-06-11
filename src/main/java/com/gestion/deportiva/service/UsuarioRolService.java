package com.gestion.deportiva.service;


import com.gestion.deportiva.dto.UsuarioRolDTO;
import com.gestion.deportiva.dto.UsuarioRolesDTO;
import com.gestion.deportiva.dto.filter.UsuarioRolFilter;


public interface UsuarioRolService extends BaseService<UsuarioRolDTO, UsuarioRolFilter> {

	UsuarioRolesDTO findUsuarioRolesDTOByUsuarioUuid(String usuarioUuid);

	void guardar(UsuarioRolesDTO dto);

}
