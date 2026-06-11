package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.MunicipioDTO;
import com.gestion.deportiva.dto.filter.MunicipioFilter;
import com.gestion.deportiva.model.Municipio;
import com.gestion.deportiva.model.Provincia;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MunicipioUtil {

	public MunicipioDTO modelToDTO(Municipio model) {
		MunicipioDTO retVal = new MunicipioDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		retVal.setCodigoIne(model.getCodigoIne());
		retVal.setDc(model.getDc());
		retVal.setComunidadAutonomaNombre(model.getProvincia().getComunidadAutonoma().getNombre());
		retVal.setComunidadAutonomaUuid(model.getProvincia().getComunidadAutonoma().getUuid());
		retVal.setProvinciaNombre(model.getProvincia().getNombre());
		retVal.setProvinciaUuid(model.getProvincia().getUuid());
		return retVal;
	}

	public List<MunicipioDTO> listModelToListDTO(List<Municipio> list) {
		List<MunicipioDTO> retVal = new ArrayList<>();
		for (Municipio bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<MunicipioDTO> pageToPageDTO(Page<Municipio> page) {
		return new PageImpl<MunicipioDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Municipio dtoToModel(MunicipioDTO dto, Municipio model, Provincia provincia) {
		if (model == null) {
			model = new Municipio();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		model.setProvincia(provincia);
		model.setCodigoIne(dto.getCodigoIne());
		model.setDc(dto.getDc());
		return model;
	}

	public String cleanUrlPageFilter(MunicipioFilter filter, String url) {
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
			if (StringUtils.hasText(filter.getProvinciaUuid())) {
				retVal = retVal + "&provinciaUuid=" + filter.getProvinciaUuid();
			}
			if (filter.getCodigoIne() != null) {
				retVal = retVal + "&codigoIne=" + filter.getCodigoIne();
			}
			if (filter.getDc() != null) {
				retVal = retVal + "&dc=" + filter.getDc();
			}
		}
		return retVal;
	}

	public List<ComboDTO> listModelToListComboDTO(List<Municipio> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getNombre(),
				bean.getProvincia().getComunidadAutonoma().getId(), bean.getProvincia().getId().toString())).toList();
	}

}
