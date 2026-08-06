package com.gestion.deportiva.repository;

import java.util.List;

import com.gestion.deportiva.model.UsuarioSede;

public interface UsuarioSedeRepository extends BaseEntityRepository<UsuarioSede, Long> {

	List<UsuarioSede> findByActivoTrueAndUsuarioId(Long id);

}
