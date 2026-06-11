package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ProvinciaDTO;
import com.gestion.deportiva.dto.filter.ProvinciaFilter;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.model.Provincia;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProvinciaUtil {

	public ProvinciaDTO modelToDTO(Provincia model) {
		ProvinciaDTO retVal = new ProvinciaDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		retVal.setComunidadAutonomaNombre(model.getComunidadAutonoma().getNombre());
		retVal.setComunidadAutonomaUuid(model.getComunidadAutonoma().getUuid());
		retVal.setCodigoIne(model.getCodigoIne());
		return retVal;
	}

	public List<ProvinciaDTO> listModelToListDTO(List<Provincia> list) {
		List<ProvinciaDTO> retVal = new ArrayList<>();
		for (Provincia bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<ProvinciaDTO> pageToPageDTO(Page<Provincia> page) {
		return new PageImpl<ProvinciaDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Provincia dtoToModel(ProvinciaDTO dto, Provincia model, ComunidadAutonoma comunidadAutonoma) {
		if (model == null) {
			model = new Provincia();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		model.setComunidadAutonoma(comunidadAutonoma);
		model.setCodigoIne(dto.getCodigoIne());
		return model;
	}

	public String cleanUrlPageFilter(ProvinciaFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}
			if (StringUtils.hasText(filter.getComunidadAutonomaUuid())) {
				retVal = retVal + "&comunidadAutonomaUuid=" + filter.getComunidadAutonomaUuid();
			}
			if (filter.getCodigoIne() != null) {
				retVal = retVal + "&codigoIne=" + filter.getCodigoIne();
			}
		}
		return retVal;
	}

	public List<ComboDTO> listModelToListComboDTO(List<Provincia> list) {
		return list.stream()
				.map(bean -> new ComboDTO(bean.getId(), bean.getNombre(), bean.getComunidadAutonoma().getId()))
				.toList();
	}

}
