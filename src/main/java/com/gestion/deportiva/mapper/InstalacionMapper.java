package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.InstalacionDTO;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.InstalacionTipo;
import com.gestion.deportiva.model.Sede;

@Component
public class InstalacionMapper {

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
		retVal.setEmpresaId(model.getSede().getEmpresa().getId());
		retVal.setEmpresaNombre(model.getSede().getEmpresa().getNombre());

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

	public Instalacion dtoToModel(InstalacionDTO dto, Instalacion model) {
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

	public List<ComboDTO> listModelToListComboDTO(List<Instalacion> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getNombre())).toList();
	}
}
