package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.dto.RegistroEmpresaDTO;
import com.gestion.deportiva.model.Empresa;

@Component
public class EmpresaMapper {
	
	public EmpresaDTO modelToDTO(Empresa model) {
		EmpresaDTO retVal = new EmpresaDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		retVal.setEmail(model.getEmail());
		retVal.setUrl(model.getUrl());
		retVal.setLogoUrl(model.getLogo());
		retVal.setDescripcion(model.getDescripcion());
		retVal.setCif(model.getCif());
		return retVal;
	}

	public List<EmpresaDTO> listModelToListDTO(List<Empresa> list) {
		List<EmpresaDTO> retVal = new ArrayList<>();
		for (Empresa bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<EmpresaDTO> pageToPageDTO(Page<Empresa> page) {
		return new PageImpl<EmpresaDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Empresa dtoToModel(EmpresaDTO dto, Empresa model ) {
		if (model == null) {
			model = new Empresa();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		model.setDescripcion(dto.getDescripcion());
		model.setEmail(dto.getEmail());
		model.setUrl(dto.getUrl());
		model.setLogo(dto.getLogoUrl());
		model.setCif(dto.getCif());
		return model;
	}
	

	public List<ComboDTO> listModelToListComboDTO(List<Empresa> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getNombre())).toList();
	}

	public  Empresa registroEmpresaDTOToModel(RegistroEmpresaDTO dto) {
		Empresa retVal = new Empresa();
		retVal.setNombre(dto.getNombre());
		retVal.setDescripcion(dto.getDescripcion());
		retVal.setEmail(dto.getEmail());
		retVal.setUrl(dto.getUrl());
		retVal.setLogo(dto.getLogo());
		retVal.setCif(dto.getCif());
		return retVal;
	}

}
