package com.gestion.deportiva.service;


import com.gestion.deportiva.dto.UsuarioRolDTO;
import com.gestion.deportiva.dto.UsuarioRolesDTO;
import com.gestion.deportiva.dto.filter.UsuarioRolFilter;
import com.gestion.deportiva.model.Usuario;


public interface UsuarioRolService extends BaseService<UsuarioRolDTO, UsuarioRolFilter> {

	void asignarRolPorDefecto(Usuario usuario);

	UsuarioRolesDTO findUsuarioRolesDTOByUsuarioUuid(String usuarioUuid);

	void guardar(UsuarioRolesDTO dto);

}
