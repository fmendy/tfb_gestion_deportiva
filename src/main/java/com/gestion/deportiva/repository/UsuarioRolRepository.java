package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.UsuarioRol;


@Repository
public interface UsuarioRolRepository extends BaseEntityRepository<UsuarioRol, Long> {

	List<UsuarioRol> findByActivoTrueAndUsuarioId(Long usuarioId);
	
	List<UsuarioRol> findByActivoTrueAndUsuarioIdAndRolNombreIn(Long usuarioId, List<String> listRolNombres);
	
	List<UsuarioRol> findByActivoTrueAndUsuario_UuidEqualsIgnoreCase(String usuarioUuid);
	
}
