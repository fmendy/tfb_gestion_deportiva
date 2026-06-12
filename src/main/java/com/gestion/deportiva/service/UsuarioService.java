package com.gestion.deportiva.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.gestion.deportiva.dto.MiPerfilDTO;
import com.gestion.deportiva.dto.MiPerfilPasswordDTO;
import com.gestion.deportiva.dto.RegistroEmpresaDTO;
import com.gestion.deportiva.dto.UsuarioDTO;
import com.gestion.deportiva.dto.UsuarioRegistroDTO;
import com.gestion.deportiva.dto.filter.UsuarioFilter;
import com.gestion.deportiva.model.Usuario;

import jakarta.validation.Valid;

public interface UsuarioService
		extends UserDetailsService, BaseService<UsuarioDTO, UsuarioFilter> {

	Usuario getByUsername(String username);

	UserDetails loadUserByUsername(String username);

	Usuario getUsuarioWithoutAuditor(String nombre);

	String registrar(UsuarioRegistroDTO dto);

	Long guardarDatos(UsuarioDTO form);

	String getNombreById(Long id);

	MiPerfilDTO getMiPerfilDTO();

	MiPerfilPasswordDTO getMiPerfilPasswordDTO();

	void actualizarPassword(MiPerfilPasswordDTO dto);

	Long registrarUsuario(@Valid RegistroEmpresaDTO dto);

	void actualizarMiPerfil(@Valid MiPerfilDTO dto);

}
