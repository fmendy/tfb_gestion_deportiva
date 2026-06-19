package com.gestion.deportiva.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gestion.deportiva.model.UsuarioInstalacion;

@Repository
public interface UsuarioInstalacionRepository extends BaseEntityRepository<UsuarioInstalacion, Long> {

	List<UsuarioInstalacion> findByActivoTrueAndUsuarioId(Long id);

}
