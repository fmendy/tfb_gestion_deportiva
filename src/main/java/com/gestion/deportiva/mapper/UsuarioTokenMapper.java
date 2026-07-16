package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioTokenDTO;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioToken;

@Component
public class UsuarioTokenMapper {

	public UsuarioTokenDTO modelToDTO(UsuarioToken model) {
		UsuarioTokenDTO retVal = new UsuarioTokenDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setUsuarioId(model.getUsuario().getId());
		retVal.setUsuarioNombre(model.getUsuario().getNombre());

		return retVal;
	}

	public List<UsuarioTokenDTO> listModelToListDTO(List<UsuarioToken> list) {
		List<UsuarioTokenDTO> retVal = new ArrayList<>();
		for (UsuarioToken bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<UsuarioTokenDTO> pageToPageDTO(Page<UsuarioToken> page) {
		return new PageImpl<UsuarioTokenDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public UsuarioToken dtoToModel(UsuarioTokenDTO dto, UsuarioToken model) {
		if (model == null) {
			model = new UsuarioToken();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);

		model.setUsuario(new Usuario(dto.getUsuarioId()));

		return model;
	}

	public List<ComboDTO> listModelToListComboDTO(List<UsuarioToken> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(),
				bean.getUsuario().getNombre() + " - " + bean.getUuid())).toList();
	}
}
