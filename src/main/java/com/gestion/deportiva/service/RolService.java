package com.gestion.deportiva.service;

import java.util.List;

import com.gestion.deportiva.dto.RolDTO;
import com.gestion.deportiva.dto.filter.RolFilter;

public interface RolService extends MaestraService<RolDTO, RolFilter> {

	RolFilter getFilterParaUsuarioController();

	List<RolDTO> getListDTOParaEmpleado();

}
