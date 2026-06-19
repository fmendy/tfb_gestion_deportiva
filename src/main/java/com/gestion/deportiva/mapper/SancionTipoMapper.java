package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.SancionTipoDTO;
import com.gestion.deportiva.model.SancionTipo;

@Component
public class SancionTipoMapper {

	public SancionTipoDTO modelToDTO(SancionTipo model) {
		SancionTipoDTO retVal = new SancionTipoDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		return retVal;
	}

	public List<SancionTipoDTO> listModelToListDTO(List<SancionTipo> list) {
		List<SancionTipoDTO> retVal = new ArrayList<>();
		for (SancionTipo bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<SancionTipoDTO> pageToPageDTO(Page<SancionTipo> page) {
		return new PageImpl<SancionTipoDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public SancionTipo dtoToModel(SancionTipoDTO dto, SancionTipo model) {
		if (model == null) {
			model = new SancionTipo();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		return model;
	}

	public List<ComboDTO> listModelToListComboDTO(List<SancionTipo> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getNombre())).toList();
	}
}
