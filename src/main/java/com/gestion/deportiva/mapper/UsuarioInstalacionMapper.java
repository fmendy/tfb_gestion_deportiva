package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioInstalacionDTO;
import com.gestion.deportiva.model.Instalacion;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioInstalacion;

@Component
public class UsuarioInstalacionMapper {

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

	public List<ComboDTO> listModelToListComboDTO(List<UsuarioInstalacion> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(),
				bean.getUsuario().getNombre() + " - " + bean.getInstalacion().getNombre())).toList();
	}
}
