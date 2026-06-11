package com.gestion.deportiva.service;

import com.gestion.deportiva.dto.UsuarioEmpresaDTO;
import com.gestion.deportiva.dto.filter.UsuarioEmpresaFilter;

public interface UsuarioEmpresaService extends BaseService<UsuarioEmpresaDTO, UsuarioEmpresaFilter> {

	void asociarUsuarioEmpresa(Long usuarioId, Long empresaId);
	


}
