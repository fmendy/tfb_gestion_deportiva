package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioSedeDTO;
import com.gestion.deportiva.model.Sede;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioSede;

@Component
public class UsuarioSedeMapper {

	public UsuarioSedeDTO modelToDTO(UsuarioSede model) {
		UsuarioSedeDTO retVal = new UsuarioSedeDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setEmpresaId(model.getSede().getEmpresa().getId());
		retVal.setEmpresaNombre(model.getSede().getEmpresa().getNombre());
		retVal.setSedeId(model.getSede().getId());
		retVal.setSedeNombre(model.getSede().getNombre());
		retVal.setUsuarioId(model.getUsuario().getId());
		retVal.setUsuarioNombre(model.getUsuario().getNombre());

		return retVal;
	}

	public List<UsuarioSedeDTO> listModelToListDTO(List<UsuarioSede> list) {
		List<UsuarioSedeDTO> retVal = new ArrayList<>();
		for (UsuarioSede bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<UsuarioSedeDTO> pageToPageDTO(Page<UsuarioSede> page) {
		return new PageImpl<UsuarioSedeDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public UsuarioSede dtoToModel(UsuarioSedeDTO dto, UsuarioSede model) {
		if (model == null) {
			model = new UsuarioSede();
		}
		Optional.ofNullable(dto.getId()).ifPresent(model::setId);
		Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);

		model.setSede(new Sede(dto.getSedeId()));
		model.setUsuario(new Usuario(dto.getUsuarioId()));

		return model;
	}

	public List<ComboDTO> listModelToListComboDTO(List<UsuarioSede> list) {
		return list.stream().map(
				bean -> new ComboDTO(bean.getId(), bean.getUsuario().getNombre() + " - " + bean.getSede().getNombre()))
				.toList();
	}
}
