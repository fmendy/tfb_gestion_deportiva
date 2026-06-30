package com.gestion.deportiva.service;

import java.time.LocalDate;
import java.util.List;

import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.InstalacionDisponibilidadDTO;
import com.gestion.deportiva.dto.InstalacionPublicoDTO;
import com.gestion.deportiva.dto.filter.InstalacionFilter;

public interface InstalacionService extends MaestraService<InstalacionDTO, InstalacionFilter> {

	List<InstalacionDTO> getListDTOParaEmpleado(Long empresaId, Long sedeId);

	InstalacionPublicoDTO getPublicoDTOById(Long id);

	InstalacionDisponibilidadDTO getDisponibilidadDTOById(Long id, LocalDate fecha);

}
