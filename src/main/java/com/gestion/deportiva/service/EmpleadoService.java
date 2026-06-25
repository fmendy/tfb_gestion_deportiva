package com.gestion.deportiva.service;

import com.gestion.deportiva.dto.EmpleadoDTO;
import com.gestion.deportiva.dto.EmpleadoRegistroDTO;
import com.gestion.deportiva.dto.EmpleadoRolDTO;
import com.gestion.deportiva.dto.filter.EmpleadoFilter;

import jakarta.validation.Valid;

public interface EmpleadoService extends MaestraService<EmpleadoDTO, EmpleadoFilter> {

	Long guardar(@Valid EmpleadoRegistroDTO dto);

	EmpleadoRegistroDTO findEmpleadoRegistroById(Long id);
	
	EmpleadoRolDTO getEmpleadoRolDTO(Long id);

	void guardarEmpleadoRol(@Valid EmpleadoRolDTO dto);

}
