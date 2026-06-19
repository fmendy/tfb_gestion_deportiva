package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.SancionDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Sancion;
import com.gestion.deportiva.model.SancionTipo;
import com.gestion.deportiva.model.Usuario;

@Component
public class SancionMapper {

	public SancionDTO modelToDTO(Sancion model) {
		SancionDTO retVal = new SancionDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setFechaFin(model.getFechaFin());
		retVal.setFechaInicio(model.getFechaInicio());
		retVal.setDescripcion(model.getDescripcion());
		retVal.setEmpresaId(model.getEmpresa().getId());
		retVal.setUsuarioId(model.getUsuario().getId());
		retVal.setSancionTipoId(model.getSancionTipo().getId());
		retVal.setEmpresaNombre(model.getEmpresa().getNombre());
		retVal.setUsuarioNombre(model.getUsuario().getNombre());
		retVal.setSancionTipoNombre(model.getSancionTipo().getNombre());

		return retVal;
	}

	public List<SancionDTO> listModelToListDTO(List<Sancion> list) {
		List<SancionDTO> retVal = new ArrayList<>();
		for (Sancion bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<SancionDTO> pageToPageDTO(Page<Sancion> page) {
		return new PageImpl<SancionDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Sancion dtoToModel(SancionDTO dto, Sancion model) {
		if (model == null) {
			model = new Sancion();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);

		model.setFechaFin(dto.getFechaFin());
		model.setFechaInicio(dto.getFechaInicio());
		model.setDescripcion(dto.getDescripcion());
		model.setEmpresa(new Empresa(dto.getEmpresaId()));
		model.setUsuario(new Usuario(dto.getUsuarioId()));
		model.setSancionTipo(new SancionTipo(dto.getSancionTipoId()));

		return model;
	}

	public List<ComboDTO> listModelToListComboDTO(List<Sancion> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getSancionTipo().getNombre())).toList();
	}

}
