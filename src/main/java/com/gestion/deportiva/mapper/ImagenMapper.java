package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ImagenDTO;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Imagen;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Sede;

public class ImagenMapper {

	public ImagenDTO modelToDTO(Imagen model) {
		ImagenDTO retVal = new ImagenDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		if (model.getEmpresa() != null) {
			retVal.setEmpresaId(model.getEmpresa().getId());
			retVal.setEmpresaNombre(model.getEmpresa().getNombre());
		}

		if (model.getSede() != null) {
			retVal.setSedeId(model.getSede().getId());
			retVal.setSedeNombre(model.getSede().getNombre());
		}

		if (model.getInstalacion() != null) {
			retVal.setInstalacionId(model.getInstalacion().getId());
			retVal.setInstalacionNombre(model.getInstalacion().getNombre());
		}

		retVal.setUrl(model.getUrl());

		return retVal;
	}

	public List<ImagenDTO> listModelToListDTO(List<Imagen> list) {
		List<ImagenDTO> retVal = new ArrayList<>();
		for (Imagen bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<ImagenDTO> pageToPageDTO(Page<Imagen> page) {
		return new PageImpl<ImagenDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Imagen dtoToModel(ImagenDTO dto, Imagen model) {
		if (model == null) {
			model = new Imagen();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);

		if (dto.getEmpresaId() != null) {
			model.setEmpresa(new Empresa(dto.getEmpresaId()));
		}
		if (dto.getSedeId() != null) {
			model.setSede(new Sede(dto.getSedeId()));
		}

		if (dto.getInstalacionId() != null) {
			model.setInstalacion(new Instalacion(dto.getInstalacionId()));
		}
		model.setUrl(dto.getUrl());

		return model;
	}

	public List<ComboDTO> listModelToListComboDTO(List<Imagen> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getUrl())).toList();
	}
}
