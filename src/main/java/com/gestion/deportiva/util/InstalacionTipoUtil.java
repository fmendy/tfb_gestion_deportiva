package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionTipoDTO;
import com.gestion.deportiva.dto.filter.InstalacionTipoFilter;
import com.gestion.deportiva.model.InstalacionTipo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InstalacionTipoUtil {

	public InstalacionTipoDTO modelToDTO(InstalacionTipo model) {
		InstalacionTipoDTO retVal = new InstalacionTipoDTO();
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setUuid(model.getUuid());
		retVal.setDescripcion(model.getDescripcion());
		return retVal;
	}

	public List<InstalacionTipoDTO> listModelToListDTO(List<InstalacionTipo> list) {
		List<InstalacionTipoDTO> retVal = new ArrayList<>();
		for (InstalacionTipo bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<InstalacionTipoDTO> pageToPageDTO(Page<InstalacionTipo> page) {
		return new PageImpl<InstalacionTipoDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public InstalacionTipo dtoToModel(InstalacionTipoDTO dto, InstalacionTipo model ) {
		if (model == null) {
			model = new InstalacionTipo();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
		model.setNombre(dto.getNombre());
		model.setDescripcion(dto.getDescripcion());
		
		return model;
	}

	public String cleanUrlPageFilter(InstalacionTipoFilter filter, String url) {
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

	public List<ComboDTO> listModelToListComboDTO(List<InstalacionTipo> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getUuid(), bean.getNombre())).toList();
	}

}
