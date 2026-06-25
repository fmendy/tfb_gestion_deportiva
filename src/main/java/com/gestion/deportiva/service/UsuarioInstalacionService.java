package com.gestion.deportiva.service;

import java.util.List;

import com.gestion.deportiva.dto.UsuarioInstalacionDTO;
import com.gestion.deportiva.dto.filter.UsuarioInstalacionFilter;
import com.gestion.deportiva.model.UsuarioInstalacion;

public interface UsuarioInstalacionService extends BaseService<UsuarioInstalacionDTO, UsuarioInstalacionFilter> {

	Long asociarUsuarioInstalacion(Long usuarioId, Long instalacionId);

	List<UsuarioInstalacion> getListByUsuarioId(Long usuarioId);

	void eliminarByUsuarioId(Long usuarioId);
	


}
