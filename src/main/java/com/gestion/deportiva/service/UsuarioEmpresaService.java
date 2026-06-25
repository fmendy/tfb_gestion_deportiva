package com.gestion.deportiva.service;

import java.util.List;


import com.gestion.deportiva.dto.UsuarioEmpresaDTO;
import com.gestion.deportiva.dto.filter.UsuarioEmpresaFilter;
import com.gestion.deportiva.model.UsuarioEmpresa;

public interface UsuarioEmpresaService extends BaseService<UsuarioEmpresaDTO, UsuarioEmpresaFilter> {

	Long asociarUsuarioEmpresa(Long usuarioId, Long empresaId);

	List<UsuarioEmpresa> getListByUsuarioId(Long usuarioId);

	void eliminarByUsuarioId(Long usuarioId);

	


}
