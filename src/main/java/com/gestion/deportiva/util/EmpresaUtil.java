package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.EmpresaDTO;
import com.gestion.deportiva.dto.filter.EmpresaFilter;
import com.gestion.deportiva.model.Empresa;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmpresaUtil {

	public EmpresaDTO modelToDTO(Empresa model) {
		EmpresaDTO retVal = new EmpresaDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		retVal.setEmail(model.getEmail());
		retVal.setUrl(model.getUrl());
		retVal.setLogo(model.getLogo());
		retVal.setDescripcion(model.getDescripcion());
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
		model.setLogo(dto.getLogo());
		return model;
	}

	public String cleanUrlPageFilter(EmpresaFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}
		}
		return retVal;
	}

	public List<ComboDTO> listModelToListComboDTO(List<Empresa> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getUuid(), bean.getNombre())).toList();
	}

}
