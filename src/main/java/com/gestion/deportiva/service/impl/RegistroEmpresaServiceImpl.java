package com.gestion.deportiva.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gestion.deportiva.dto.EmpresaRegistroDTO;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.RegistroEmpresaService;
import com.gestion.deportiva.service.UsuarioEmpresaService;
import com.gestion.deportiva.service.UsuarioRolService;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.Constantes;

@Service
public class RegistroEmpresaServiceImpl implements RegistroEmpresaService {

	private final EmpresaService empresaService;

	private final UsuarioService usuarioService;

	private final UsuarioEmpresaService usuarioEmpresaService;

	private final UsuarioRolService usuarioRolService;

	RegistroEmpresaServiceImpl(EmpresaService empresaService, UsuarioService usuarioService,
			UsuarioEmpresaService usuarioEmpresaService, UsuarioRolService usuarioRolService) {
		this.empresaService = empresaService;
		this.usuarioService = usuarioService;
		this.usuarioEmpresaService = usuarioEmpresaService;
		this.usuarioRolService = usuarioRolService;
	}

	@Transactional(rollbackFor = Exception.class)
	public void registrarEmpresa(EmpresaRegistroDTO dto) {
		Long empresaId = empresaService.registrarEmpresa(dto);
		dto.setPassword(dto.getPassword());
		Long usuarioId = usuarioService.registrarUsuarioEmpresa(dto);
		usuarioEmpresaService.asociarUsuarioEmpresa(usuarioId, empresaId);
		usuarioRolService.asignarRol(usuarioId, Constantes.Rol.USUARIO_EMPRESA);
	}

}
