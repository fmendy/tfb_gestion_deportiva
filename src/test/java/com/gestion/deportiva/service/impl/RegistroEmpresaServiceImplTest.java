package com.gestion.deportiva.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gestion.deportiva.dto.EmpresaRegistroDTO;
import com.gestion.deportiva.service.EmpresaService;
import com.gestion.deportiva.service.UsuarioEmpresaService;
import com.gestion.deportiva.service.UsuarioRolService;
import com.gestion.deportiva.service.UsuarioService;
import com.gestion.deportiva.util.Constantes;

@ExtendWith(MockitoExtension.class)
class RegistroEmpresaServiceImplTest {

	@Mock
	private EmpresaService empresaService;

	@Mock
	private UsuarioService usuarioService;

	@Mock
	private UsuarioEmpresaService usuarioEmpresaService;

	@Mock
	private UsuarioRolService usuarioRolService;

	@InjectMocks
	private RegistroEmpresaServiceImpl registroEmpresaService;

	@Test
	void registrarEmpresaConExito() {
		EmpresaRegistroDTO dto = new EmpresaRegistroDTO();
		dto.setPassword("password123");

		Long empresaId = 1L;
		Long usuarioId = 2L;

		when(empresaService.registrarEmpresa(dto)).thenReturn(empresaId);
		when(usuarioService.registrarUsuarioEmpresa(dto)).thenReturn(usuarioId);

		registroEmpresaService.registrarEmpresa(dto);

		verify(empresaService).registrarEmpresa(dto);
		verify(usuarioService).registrarUsuarioEmpresa(dto);
		verify(usuarioEmpresaService).asociarUsuarioEmpresa(usuarioId, empresaId);
		verify(usuarioRolService).asignarRol(usuarioId, Constantes.Rol.USUARIO_EMPRESA);
	}
}