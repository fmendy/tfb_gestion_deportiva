package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ComunidadAutonomaDTO;
import com.gestion.deportiva.dto.filter.ComunidadAutonomaFilter;
import com.gestion.deportiva.model.ComunidadAutonoma;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ComunidadAutonomaUtil {

	public ComunidadAutonomaDTO modelToDTO(ComunidadAutonoma model) {
		ComunidadAutonomaDTO retVal = new ComunidadAutonomaDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		retVal.setCodigoIne(model.getCodigoIne());
		return retVal;
	}

	public List<ComunidadAutonomaDTO> listModelToListDTO(List<ComunidadAutonoma> list) {
		List<ComunidadAutonomaDTO> retVal = new ArrayList<>();
		for (ComunidadAutonoma bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<ComunidadAutonomaDTO> pageToPageDTO(Page<ComunidadAutonoma> page) {
		return new PageImpl<ComunidadAutonomaDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public ComunidadAutonoma dtoToModel(ComunidadAutonomaDTO dto, ComunidadAutonoma model) {
		if (model == null) {
			model = new ComunidadAutonoma();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		model.setCodigoIne(dto.getCodigoIne());
		return model;
	}

	public String cleanUrlPageFilter(ComunidadAutonomaFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}
			if (filter.getCodigoIne() != null) {
				retVal = retVal + "&codigoIne=" + filter.getCodigoIne();
			}
		}
		return retVal;
	}

	public List<ComboDTO> listModelToListComboDTO(List<ComunidadAutonoma> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getNombre())).toList();
	}

}
