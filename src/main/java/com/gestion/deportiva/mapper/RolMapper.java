package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.RolDTO;
import com.gestion.deportiva.model.Rol;

@Component
public class RolMapper {

	public RolDTO modelToDTO(Rol model) {
		RolDTO retVal = new RolDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		return retVal;
	}

	public List<RolDTO> listModelToListDTO(List<Rol> list) {
		List<RolDTO> retVal = new ArrayList<>();
		for (Rol bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<RolDTO> pageToPageDTO(Page<Rol> page) {
		return new PageImpl<RolDTO>(listModelToListDTO(page.getContent()), page.getPageable(), page.getTotalElements());
	}

	public Rol dtoToModel(RolDTO dto, Rol model) {
		if (model == null) {
			model = new Rol();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		return model;
	}

	public List<ComboDTO> listModelToListComboDTO(List<Rol> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getNombre())).toList();
	}

}
