package com.gestion.deportiva.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.InstalacionDisponibilidadDTO;
import com.gestion.deportiva.dto.InstalacionPublicoDTO;
import com.gestion.deportiva.dto.filter.InstalacionFilter;
import com.gestion.deportiva.dto.filter.InstalacionPublicoFilter;

public interface InstalacionService extends MaestraService<InstalacionDTO, InstalacionFilter> {

	List<InstalacionDTO> getListDTOParaEmpleado(Long empresaId, Long sedeId);

	InstalacionPublicoDTO getPublicoDTOById(Long id);

	InstalacionDisponibilidadDTO getDisponibilidadDTOById(Long id, LocalDate fecha);

	List<InstalacionDTO> getListByFilter(InstalacionPublicoFilter filter, Pageable pageable);

}
