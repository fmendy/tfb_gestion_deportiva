package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ReservaEstadoDTO;
import com.gestion.deportiva.dto.filter.ReservaEstadoFilter;
import com.gestion.deportiva.model.ReservaEstado;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ReservaEstadoUtil {

	public ReservaEstadoDTO modelToDTO(ReservaEstado model) {
		ReservaEstadoDTO retVal = new ReservaEstadoDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		return retVal;
	}

	public List<ReservaEstadoDTO> listModelToListDTO(List<ReservaEstado> list) {
		List<ReservaEstadoDTO> retVal = new ArrayList<>();
		for (ReservaEstado bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<ReservaEstadoDTO> pageToPageDTO(Page<ReservaEstado> page) {
		return new PageImpl<ReservaEstadoDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public ReservaEstado dtoToModel(ReservaEstadoDTO dto, ReservaEstado model) {
		if (model == null) {
			model = new ReservaEstado();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		return model;
	}

	public String cleanUrlPageFilter(ReservaEstadoFilter filter, String url) {
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

	public List<ComboDTO> listModelToListComboDTO(List<ReservaEstado> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getNombre())).toList();
	}

}
