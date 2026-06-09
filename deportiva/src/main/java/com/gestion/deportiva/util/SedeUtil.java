package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.SedeDTO;
import com.gestion.deportiva.dto.filter.SedeFilter;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Municipio;
import com.gestion.deportiva.model.Sede;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SedeUtil {

	public SedeDTO modelToDTO(Sede model) {
		SedeDTO retVal = new SedeDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		retVal.setEmail(model.getEmail());
		retVal.setUrl(model.getUrl());
		retVal.setLogo(model.getLogo());
		retVal.setDescripcion(model.getDescripcion());
		retVal.setEmpresaId(model.getEmpresa().getId());
		retVal.setEmpresaNombre(model.getEmpresa().getNombre());
		retVal.setDireccion(model.getDireccion());
		retVal.setLatitud(model.getLatitud());
		retVal.setLongitud(model.getLongitud());
		return retVal;
	}

	public List<SedeDTO> listModelToListDTO(List<Sede> list) {
		List<SedeDTO> retVal = new ArrayList<>();
		for (Sede bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<SedeDTO> pageToPageDTO(Page<Sede> page) {
		return new PageImpl<SedeDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Sede dtoToModel(SedeDTO dto, Sede model ) {
		if (model == null) {
			model = new Sede();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		model.setDescripcion(dto.getDescripcion());
		model.setEmail(dto.getEmail());
		model.setUrl(dto.getUrl());
		model.setLogo(dto.getLogo());
		model.setDireccion(dto.getDireccion());
		model.setLatitud(dto.getLatitud());
		model.setLongitud(dto.getLongitud());
		model.setEmpresa(new Empresa(dto.getEmpresaId()));
		model.setMunicipio(new Municipio(dto.getMunicipioId()));
		
		return model;
	}

	public String cleanUrlPageFilter(SedeFilter filter, String url) {
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

	public List<ComboDTO> listModelToListComboDTO(List<Sede> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getUuid(), bean.getNombre())).toList();
	}

}
