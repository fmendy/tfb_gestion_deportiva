package com.gestion.deportiva.service;

import java.util.List;

import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.dto.EmpresaRegistroDTO;
import com.gestion.deportiva.dto.filter.EmpresaFilter;

public interface EmpresaService extends MaestraService<EmpresaDTO, EmpresaFilter> {

	Long registrarEmpresa(EmpresaRegistroDTO dto);

	List<EmpresaDTO> getListDTOParaInstalacion();
	
	List<EmpresaDTO> getListDTOParaEmpleado();

}
