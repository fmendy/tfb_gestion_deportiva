package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioEmpresaDTO;
import com.gestion.deportiva.dto.filter.UsuarioEmpresaFilter;
import com.gestion.deportiva.model.UsuarioEmpresa;
import com.gestion.deportiva.model.Empresa;
import com.gestion.deportiva.model.Usuario;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioEmpresaUtil {

	public UsuarioEmpresaDTO modelToDTO(UsuarioEmpresa model) {
		UsuarioEmpresaDTO retVal = new UsuarioEmpresaDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setEmpresaId(model.getEmpresa().getId());
		retVal.setEmpresaNombre(model.getEmpresa().getNombre());
		retVal.setUsuarioId(model.getUsuario().getId());
		retVal.setUsuarioNombre(model.getUsuario().getNombre());

		return retVal;
	}

	public List<UsuarioEmpresaDTO> listModelToListDTO(List<UsuarioEmpresa> list) {
		List<UsuarioEmpresaDTO> retVal = new ArrayList<>();
		for (UsuarioEmpresa bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<UsuarioEmpresaDTO> pageToPageDTO(Page<UsuarioEmpresa> page) {
		return new PageImpl<UsuarioEmpresaDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public UsuarioEmpresa dtoToModel(UsuarioEmpresaDTO dto, UsuarioEmpresa model) {
		if (model == null) {
			model = new UsuarioEmpresa();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);

		model.setEmpresa(new Empresa(dto.getEmpresaId()));
		model.setUsuario(new Usuario(dto.getUsuarioId()));

		return model;
	}

	public String cleanUrlPageFilter(UsuarioEmpresaFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (filter.getEmpresaId() != null) {
				retVal = retVal + "&empresaId=" + filter.getEmpresaId();
			}

			if (filter.getUsuarioId() != null) {
				retVal = retVal + "&usuarioId=" + filter.getUsuarioId();
			}

		}
		return retVal;
	}

	public List<ComboDTO> listModelToListComboDTO(List<UsuarioEmpresa> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(),
				bean.getUsuario().getNombre() + " - " + bean.getEmpresa().getNombre())).toList();
	}

}
