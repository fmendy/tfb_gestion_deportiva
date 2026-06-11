package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioInstalacionDTO;
import com.gestion.deportiva.dto.filter.UsuarioInstalacionFilter;
import com.gestion.deportiva.model.UsuarioInstalacion;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Usuario;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioInstalacionUtil {

	public UsuarioInstalacionDTO modelToDTO(UsuarioInstalacion model) {
		UsuarioInstalacionDTO retVal = new UsuarioInstalacionDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setEmpresaId(model.getInstalacion().getSede().getEmpresa().getId());
		retVal.setEmpresaNombre(model.getInstalacion().getSede().getEmpresa().getNombre());
		retVal.setSedeId(model.getInstalacion().getSede().getId());
		retVal.setSedeNombre(model.getInstalacion().getSede().getNombre());
		retVal.setInstalacionId(model.getInstalacion().getId());
		retVal.setInstalacionNombre(model.getInstalacion().getNombre());
		retVal.setUsuarioId(model.getUsuario().getId());
		retVal.setUsuarioNombre(model.getUsuario().getNombre());

		return retVal;
	}

	public List<UsuarioInstalacionDTO> listModelToListDTO(List<UsuarioInstalacion> list) {
		List<UsuarioInstalacionDTO> retVal = new ArrayList<>();
		for (UsuarioInstalacion bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<UsuarioInstalacionDTO> pageToPageDTO(Page<UsuarioInstalacion> page) {
		return new PageImpl<UsuarioInstalacionDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public UsuarioInstalacion dtoToModel(UsuarioInstalacionDTO dto, UsuarioInstalacion model) {
		if (model == null) {
			model = new UsuarioInstalacion();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);


		model.setInstalacion(new Instalacion(dto.getInstalacionId()));
		model.setUsuario(new Usuario(dto.getUsuarioId()));

		return model;
	}

	public String cleanUrlPageFilter(UsuarioInstalacionFilter filter, String url) {
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
			if (filter.getUsuarioId() != null) {
				retVal = retVal + "&usuarioId=" + filter.getUsuarioId();
			}

		}
		return retVal;
	}

	public List<ComboDTO> listModelToListComboDTO(List<UsuarioInstalacion> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getUuid(), bean.getUsuario().getNombre() + " - " + bean.getInstalacion().getNombre()))
				.toList();
	}

}
