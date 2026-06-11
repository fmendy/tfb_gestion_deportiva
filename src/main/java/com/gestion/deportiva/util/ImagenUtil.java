package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.ImagenDTO;
import com.gestion.deportiva.dto.filter.ImagenFilter;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Imagen;
import com.gestion.deportiva.model.Instalacion;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ImagenUtil {

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

	public String cleanUrlPageFilter(ImagenFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (filter.getEmpresaId() != null) {
				retVal = retVal + "&empresaId=" + filter.getEmpresaId();
			}

			if (filter.getSedeId() != null) {
				retVal = retVal + "&sedeId=" + filter.getSedeId();
			}

			if (filter.getInstalacionId() != null) {
				retVal = retVal + "&instalacionId=" + filter.getInstalacionId();
			}
		}
		return retVal;
	}

	public List<ComboDTO> listModelToListComboDTO(List<Imagen> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getUuid(), bean.getUrl())).toList();
	}

}
