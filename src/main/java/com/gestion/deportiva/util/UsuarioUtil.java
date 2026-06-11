package com.gestion.deportiva.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.MiPerfilDTO;
import com.gestion.deportiva.dto.MiPerfilPasswordDTO;
import com.gestion.deportiva.dto.RegistroEmpresaDTO;
import com.gestion.deportiva.dto.UsuarioDTO;
import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.dto.filter.UsuarioFilter;
import com.gestion.deportiva.model.Usuario;

import jakarta.validation.Valid;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioUtil {



	public UsuarioDTO modelToDTO(Usuario model) {
		UsuarioDTO retVal = new UsuarioDTO();
		retVal.setUuid(model.getUuid());
		retVal.setEmail(model.getEmail());
		retVal.setId(model.getId());
		retVal.setNombre(model.getNombre());
		retVal.setListUsuarioRolDTO(UsuarioRolUtil.listModelToListDTO(model.getListUsuarioRol()));
		return retVal;
	}

	public List<UsuarioDTO> listModelToListDTO(List<Usuario> list) {
		List<UsuarioDTO> retVal = new ArrayList<>();
		for (Usuario bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<UsuarioDTO> pageToPageDTO(Page<Usuario> page) {

		return new PageImpl<UsuarioDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

	public Usuario dtoToModel(UsuarioDTO dto, Usuario model) {
		if (model == null) {
			model = new Usuario();
			// Solo si es nuevo, asigna el ID manualmente (aunque normalmente JPA lo genera
			// solo)
			Optional.ofNullable(dto.getId()).ifPresent(model::setId);
			Optional.ofNullable(dto.getUuid()).filter(StringUtils::hasText).ifPresent(model::setUuid);
			model.setPassword("");
		}

		model.setEmail(dto.getEmail());
		model.setNombre(dto.getNombre());
		return model;
	}

	public Usuario registroDTOToModel(UsuarioRegistroDTO dto) {
		Usuario retVal = new Usuario();
		retVal.setEmail(dto.getEmail());
		retVal.setNombre(dto.getNombre());
		retVal.setPassword(dto.getPassword());
		return retVal;
	}

	public MiPerfilDTO modelToMiPerfilDTO(Usuario model) {
		MiPerfilDTO retVal = new MiPerfilDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		retVal.setNombre(model.getNombre());
		retVal.setEmail(model.getEmail());
		retVal.setRolNombre(SecurityUtil.getCurrentUserRol().toString());
		retVal.setFechaAlta(model.getFechaCreacion());
		return retVal;
	}

	public MiPerfilPasswordDTO modelToMiPerfilPasswordDTO(Usuario model) {
		MiPerfilPasswordDTO retVal = new MiPerfilPasswordDTO();
		retVal.setId(model.getId());
		retVal.setUuid(model.getUuid());
		return retVal;
	}

	public String cleanUrlPageFilter(UsuarioFilter filter, String url) {
		String retVal = url;
		if (retVal.indexOf("?") < 0) {
			retVal = retVal + "?";
		}
		if (filter != null) {
			if (StringUtils.hasText(filter.getNombre())) {
				retVal = retVal + "&nombre=" + filter.getNombre();
			}

			if (StringUtils.hasText(filter.getDemarcacionUuid())) {
				retVal = retVal + "&demarcacionUuid=" + filter.getDemarcacionUuid();
			}

			if (StringUtils.hasText(filter.getRolUuid())) {
				retVal = retVal + "&rolUuid=" + filter.getRolUuid();
			}
			if (StringUtils.hasText(filter.getEmail())) {
				retVal = retVal + "&email=" + filter.getEmail();
			}
		}
		return retVal;
	}

	public List<ComboDTO> listModelToListComboDTO(List<Usuario> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getNombre())).toList();
	}

	public  Usuario registroEmpresaDTOToModel(@Valid RegistroEmpresaDTO dto) {
		Usuario retVal = new Usuario();
		retVal.setNombre(dto.getNombre());
		retVal.setEmail(dto.getEmail());
		retVal.setPassword(dto.getPassword());
		return retVal;
	}

}
