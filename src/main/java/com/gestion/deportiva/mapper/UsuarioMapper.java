package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.gestion.deportiva.dto.UsuarioDTO;
import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.dto.ComboDTO;
import com.gestion.deportiva.dto.MiPerfilDTO;
import com.gestion.deportiva.dto.MiPerfilPasswordDTO;
import com.gestion.deportiva.dto.RegistroEmpresaDTO;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.util.SecurityUtil;
import com.gestion.deportiva.util.UsuarioRolUtil;


@Component
public class UsuarioMapper {

    private final PasswordEncoder passwordEncoder;

    public UsuarioMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registroEmpresaDTOToModel(RegistroEmpresaDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        return usuario;
    }



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
	
	public List<ComboDTO> listModelToListComboDTO(List<Usuario> list) {
		return list.stream().map(bean -> new ComboDTO(bean.getId(), bean.getNombre())).toList();
	}



}