package com.gestion.deportiva.service;

import java.util.List;


import com.gestion.deportiva.dto.UsuarioSedeDTO;
import com.gestion.deportiva.dto.filter.UsuarioSedeFilter;
import com.gestion.deportiva.model.UsuarioSede;

public interface UsuarioSedeService extends BaseService<UsuarioSedeDTO, UsuarioSedeFilter> {

	Long asociarUsuarioSede(Long usuarioId, Long sedeId);

	List<UsuarioSede> getListByUsuarioId(Long usuarioId);

	void eliminarByUsuarioId(Long usuarioId);
	


}
