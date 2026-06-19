package com.gestion.deportiva.service;

import com.gestion.deportiva.dto.UsuarioSedeDTO;
import com.gestion.deportiva.dto.filter.UsuarioSedeFilter;

public interface UsuarioSedeService extends BaseService<UsuarioSedeDTO, UsuarioSedeFilter> {

	Long asociarUsuarioSede(Long usuarioId, Long sedeId);
	


}
