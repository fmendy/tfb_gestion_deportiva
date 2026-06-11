package com.gestion.deportiva.service;

import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.dto.RegistroEmpresaDTO;
import com.gestion.deportiva.dto.filter.EmpresaFilter;

public interface EmpresaService extends MaestraService<EmpresaDTO, EmpresaFilter> {

	Long registrarEmpresa(RegistroEmpresaDTO dto);
	


}
