package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.UsuarioRolDTO;
import com.gestion.deportiva.model.Rol;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.model.UsuarioRol;

@Component
public class UsuarioRolMapper {

	public UsuarioRolDTO modelToDTO(UsuarioRol model) {
		UsuarioRolDTO retVal = new UsuarioRolDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setRolId(model.getRol().getId());
		retVal.setRolNombre(model.getRol().getNombre());
		retVal.setRolUuid(model.getRol().getUuid());
		retVal.setUsuarioId(model.getUsuario().getId());
		retVal.setUsuarioNombre(model.getUsuario().getNombre());
		retVal.setUsuarioUuid(model.getUsuario().getUuid());
		return retVal;
	}

	public List<UsuarioRolDTO> listModelToListDTO(List<UsuarioRol> list) {
		List<UsuarioRolDTO> retVal = new ArrayList<>();
		for (UsuarioRol bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<UsuarioRolDTO> pageToPageDTO(Page<UsuarioRol> page) {
		return new PageImpl<UsuarioRolDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public UsuarioRol dtoToModel(UsuarioRolDTO dto, UsuarioRol model) {
		if (model == null) {
			model = new UsuarioRol();
		}
		model.setId(dto.getId());
		model.setUsuario(new Usuario(dto.getUsuarioId()));
		model.setRol(new Rol(dto.getRolId()));
		return model;
	}

	public List<ComboDTO> listModelToListComboDTO(List<UsuarioRol> list) {
		return null;
	}

}
