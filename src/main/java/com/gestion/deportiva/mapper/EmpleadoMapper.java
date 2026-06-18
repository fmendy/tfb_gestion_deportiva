package com.gestion.deportiva.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import com.gestion.deportiva.dto.EmpleadoDTO;
import com.gestion.deportiva.model.Usuario;
import com.gestion.deportiva.util.Constantes;

@Component
public class EmpleadoMapper {

	@Autowired
	private UsuarioRolMapper usuarioRolMapper;

	@Autowired
	private UsuarioEmpresaMapper usuarioEmpresaMapper;

	@Autowired
	private UsuarioSedeMapper usuarioSedeMapper;

	@Autowired
	private UsuarioInstalacionMapper usuarioInstalacionMapper;

	public EmpleadoDTO modelToDTO(Usuario usuario) {
		EmpleadoDTO retVal = new EmpleadoDTO();
		List<String> listaRolesEmpleados = Arrays.asList(Constantes.Rol.ADMINISTRADOR, Constantes.Rol.USUARIO_EMPRESA,
				Constantes.Rol.USUARIO_SEDE, Constantes.Rol.USUARIO_INSTALACION);

		if (!usuario.getListUsuarioRol().isEmpty() && usuario.getListUsuarioRol().stream()
				.map(usuarioRol -> usuarioRol.getRol().getNombre()).anyMatch(listaRolesEmpleados::contains)) {

			retVal.setId(usuario.getId());
			retVal.setEmail(usuario.getEmail());
			retVal.setNombre(usuario.getNombre());
			retVal.setListUsuarioRolDTO(usuarioRolMapper.listModelToListDTO(usuario.getListUsuarioRol()));
			retVal.setListUsuarioEmpresaDTO(usuarioEmpresaMapper.listModelToListDTO(usuario.getListUsuarioEmpresa()));
			retVal.setListUsuarioSedeDTO(usuarioSedeMapper.listModelToListDTO(usuario.getListUsuarioSede()));
			retVal.setListUsuarioInstalacionDTO(
					usuarioInstalacionMapper.listModelToListDTO(usuario.getListUsuarioInstalacion()));
		}
		return retVal;
	}

	public List<EmpleadoDTO> listModelToListDTO(List<Usuario> list) {
		List<EmpleadoDTO> retVal = new ArrayList<>();
		for (Usuario bean : list) {
			retVal.add(modelToDTO(bean));
		}
		return retVal;
	}

	public Page<EmpleadoDTO> pageToPageDTO(Page<Usuario> page) {
		return new PageImpl<EmpleadoDTO>(listModelToListDTO(page.getContent()), page.getPageable(),
				page.getTotalElements());
	}

}
