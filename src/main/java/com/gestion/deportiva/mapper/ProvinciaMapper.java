package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ProvinciaDTO;
import com.gestion.deportiva.model.ComunidadAutonoma;
import com.gestion.deportiva.model.Provincia;

@Component
public class ProvinciaMapper {

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

	public List<ComboDTO> listModelToListComboDTO(List<Provincia> list) {
		return list.stream()
				.map(bean -> new ComboDTO(bean.getId(), bean.getNombre(), bean.getComunidadAutonoma().getId()))
				.toList();
	}
}
