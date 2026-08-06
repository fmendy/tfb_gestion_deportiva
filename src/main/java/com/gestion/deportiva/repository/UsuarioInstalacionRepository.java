package com.gestion.deportiva.repository;

import java.util.List;

import com.gestion.deportiva.model.UsuarioInstalacion;

public interface UsuarioInstalacionRepository extends BaseEntityRepository<UsuarioInstalacion, Long> {

	List<UsuarioInstalacion> findByActivoTrueAndUsuarioId(Long id);

}
