package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.dto.filter.InstalacionFilter;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionTipo;

import lombok.experimental.UtilityClass;

@UtilityClass
public class InstalacionUtil {

	public InstalacionDTO modelToDTO(Instalacion model) {
		InstalacionDTO retVal = new InstalacionDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		retVal.setSedeId(model.getSede().getId());
		retVal.setSedeNombre(model.getSede().getNombre());
		retVal.setInstalacionTipoId(model.getInstalacionTipo().getId());
		retVal.setInstalacionTipoNombre(model.getInstalacionTipo().getNombre());
		retVal.setDescripcion(model.getDescripcion());

		return retVal;
	}

	public List<InstalacionDTO> listModelToListDTO(List<Instalacion> list) {
		List<InstalacionDTO> retVal = new ArrayList<>();
		for (Instalacion bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<InstalacionDTO> pageToPageDTO(Page<Instalacion> page) {
		return new PageImpl<InstalacionDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Instalacion dtoToModel(InstalacionDTO dto, Instalacion model ) {
		if (model == null) {
			model = new Instalacion();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		model.setDescripcion(dto.getDescripcion());

		model.setSede(new Sede(dto.getSedeId()));
		model.setInstalacionTipo(new InstalacionTipo(dto.getInstalacionTipoId()));
		
		return model;
	}

	public String cleanUrlPageFilter(InstalacionFilter filter, String url) {
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

	public List<ComboDTO> listModelToListComboDTO(List<Instalacion> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getUuid(), bean.getNombre())).toList();
	}

}
